/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.experisit.my_processmining.query.filtrage;

import static com.experisit.processminingusingds.log.summarize.LogSummary.transformFileToDataset;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import scala.collection.Seq;

/**
 *
 * @author youssef.hissou
 */
public class FiltrageUtils {
    
    public static Seq caseID;
    private Date start_date;
    private Date stop_date;
    
    public static List<String> role;
    public static List<String> resource;
    
    public static Dataset builDataSetWithdCritere(SparkSession spark, Critere c, String appliName, String appliDesc, String sourceFile, String delimiter){
        Dataset raw_data = transformFileToDataset(spark, sourceFile,delimiter);
        raw_data.printSchema();
        //Column 
        raw_data = raw_data.select(scala.collection.JavaConversions.asScalaBuffer(c.getColumns_list()).seq())
                .where(raw_data.col("Role")
                        .isin(c.getRole().toArray((String[]) new String[c.getRole().size()])))
                .where(raw_data.col("Activity")
                        .isin(c.getRole().toArray((String[]) new String[c.getActivity().size()])))
                .where(raw_data.col("Start Timestamp")
                        .$greater(c.getStart_date()))
//                .where(raw_data.col("Complete Timestamp")
//                        .$minus(c.getStop_date()))
                .limit(c.getLine());
        
        return raw_data;
    }

}
