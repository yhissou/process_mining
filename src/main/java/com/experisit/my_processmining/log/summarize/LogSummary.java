/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.experisit.my_processmining.log.summarize;

import com.experisit.my_processmining.storage.MongoUtils;
import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.config.WriteConfig;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.RelationalGroupedDataset;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

/**
 *
 * @author youssef.hissou
 */
public class LogSummary implements Serializable {

    public static LogSummary buildSummary(SparkSession spark, String appliName, String appliDesc, String sourceFile, String delimiter) {

        Dataset raw_data = transformFileToDataset(spark, sourceFile,delimiter);
        
        //Calcul minute diff :)
        raw_data = raw_data.withColumn("minute_diff (s)",
                    org.apache.spark.sql.functions.unix_timestamp(raw_data.col("Complete Timestamp"))
                        .$minus(
                    org.apache.spark.sql.functions.unix_timestamp(raw_data.col("Start Timestamp"))));
        
        //case_overview
        calcul_view_case_overview(raw_data,spark);
        //activity
        calcul_view_activity(raw_data,spark);
        //resource
        calcul_view_resource(raw_data,spark);
        //role
        calcul_view_role(raw_data,spark);

        return null;
    }

    private static void calcul_view_activity(Dataset raw_data,SparkSession jsc) {
        //activity_overview
        Dataset activity = raw_data.groupBy("Activity").agg(
                org.apache.spark.sql.functions.count("Activity").alias("Frequency"),
                org.apache.spark.sql.functions.count("Activity").$div(9119).alias("Relative Frequency"),
                org.apache.spark.sql.functions.mean("minute_diff (s)").alias("Mean Duration"),
                org.apache.spark.sql.functions.avg("minute_diff (s)").alias("Avg Duration"));

        MongoSpark.save(activity,getWriteConfig(jsc,"activity"));
    }

    private static void calcul_view_resource(Dataset raw_data,SparkSession jsc) {
        //activity_overview
        RelationalGroupedDataset resource_gb = raw_data.groupBy("Resource");
        long nb_resource = resource_gb.count().count();

        Dataset resource = resource_gb.agg(
                org.apache.spark.sql.functions.count("Resource").alias("Frequency"),
                org.apache.spark.sql.functions.count("Resource").$div(nb_resource).alias("Relative Frequency"),
                org.apache.spark.sql.functions.mean("minute_diff (s)").alias("Mean Duration")
        );

        MongoSpark.save(resource,getWriteConfig(jsc,"resource"));
    }

    private static void calcul_view_role(Dataset raw_data,SparkSession jsc) {

        //activity_overview
        long nb_role = raw_data.count();
        RelationalGroupedDataset role_gb = raw_data.groupBy("Role");

        Dataset role = role_gb
                .agg(
                        org.apache.spark.sql.functions.count("Role").alias("Frequency"),
                        org.apache.spark.sql.functions.count("Role").$div(nb_role).alias("Relative Frequency")
                );
        
        MongoSpark.save(role,getWriteConfig(jsc,"role"));
    }

    private static void calcul_view_case_overview(Dataset ds,SparkSession jsc) {
//        Case Overview
        Dataset case_overview = ds.groupBy(ds.col("Case ID"), ds.col("Variant")).agg(
                org.apache.spark.sql.functions.min("Start Timestamp"),
                org.apache.spark.sql.functions.max("Complete Timestamp"),
                org.apache.spark.sql.functions.count("Case ID")
        );
        case_overview = case_overview.withColumn("date_dif",
                org.apache.spark.sql.functions.datediff(
                        case_overview.col("max(Complete Timestamp)"),
                        case_overview.col("min(Start Timestamp)")));
        
        MongoSpark.save(case_overview,getWriteConfig(jsc,"case_overview"));
    }

    private static StructType create_schema() {
        StructType schema = DataTypes
                .createStructType(new StructField[]{
            DataTypes.createStructField("Case ID", DataTypes.IntegerType, false),
            DataTypes.createStructField("Activity", DataTypes.StringType, false),
            DataTypes.createStructField("Resource", DataTypes.StringType, true),
            DataTypes.createStructField("Start Timestamp", DataTypes.TimestampType, true),
            DataTypes.createStructField("Complete Timestamp", DataTypes.TimestampType, true),
            DataTypes.createStructField("Variant", DataTypes.StringType, true),
            DataTypes.createStructField("Variant index", DataTypes.IntegerType, true),
            DataTypes.createStructField("Role", DataTypes.StringType, true)
        });
        return schema;
    }
    
    
    public static Dataset transformFileToDataset(SparkSession spark, StructType schema, String sourceFile) {
        return spark.read()
                .option("delimiter", ";")
                .option("header", true)
                .schema(schema)
                .csv(sourceFile);
    }
    
    public static Dataset transformFileToDataset(SparkSession spark, String sourceFile, String delimiter) {
        Dataset raw_data = spark.read()
                .option("delimiter", delimiter)
                .option("header", true)
                .schema(create_schema())
                .csv(sourceFile);
        
        return raw_data;
    }
    
     private static WriteConfig getWriteConfig(SparkSession jsc,String collectionName) {
        
        Map<String, String> writeOverrides = new HashMap<String, String>();
        writeOverrides.put("collection", collectionName);
        writeOverrides.put("writeConcern.w", "majority");
        WriteConfig writeConfig = WriteConfig.create(jsc).withOptions(writeOverrides);
        
        return writeConfig;
    }
}
