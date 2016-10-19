/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.experisit.my_processmining.storage.main;

import com.experisit.my_processmining.query.filtrage.Critere;
import com.experisit.my_processmining.query.filtrage.FiltrageUtils;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.net.ntp.TimeStamp;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.SparkSession;
import scala.collection.Seq;

/**
 *
 * @author youssef.hissou
 */
public class run {
    
     public static void main(String[] args) throws ParseException {
         
        String applicationName = "Process Mining using Apache Spark";
        String applicationDesc = "Building statistics about the process";
        SparkConf conf = new SparkConf( )
                .setAppName(applicationName)
                .setMaster("local[*]")
                .set("spark.mongodb.input.uri", "mongodb://192.168.99.100:32768/processminingdb.role")
                .set("spark.mongodb.output.uri", "mongodb://192.168.99.100:32768/processminingdb.role");
        
        SparkSession spark =  SparkSession.builder()
                                            .config(conf)
                                            .getOrCreate();

        List<Column> b = new LinkedList<Column>();
        b.add(new Column("Activity"));
        b.add(new Column("Role"));
        
        List<String> r = new LinkedList<String>();
        r.add("Requester");
        r.add("Supplier");
        
        List<String> a = new LinkedList<String>();
        a.add("Create Purchase Requisition");
        a.add("Supplier");
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date start_date = sdf.parse("01/01/2011");
        Timestamp start_date_sql = new Timestamp(start_date.getTime());

        Date end_date =  sdf.parse("30/01/2011");
        Timestamp end_date_sql = new Timestamp(end_date.getTime());

         Critere c1 = new Critere(b, null, start_date_sql, end_date_sql, r, a, 10);
         FiltrageUtils.builDataSetWithdCritere(spark, c1, applicationName, applicationDesc, "C:\\Users\\youssef.hissou\\Documents\\Project\\Projet perso\\process_mining\\PurchaseProcessTest.csv",";").show();
        //LogSummary lss = LogSummary.buildSummary(spark, applicationName, applicationDesc, "C:\\Users\\youssef.hissou\\Documents\\Project\\Projet perso\\process_mining\\PurchaseProcessTest.csv",";");

     }
    
}
