/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.experisit.my_processmining.query.filtrage;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.apache.commons.net.ntp.TimeStamp;
import org.apache.spark.sql.Column;
import scala.collection.Seq;

/**
 *
 * @author youssef.hissou
 */
public class Critere {
    
    
    private Seq<Column> columns;   

    private List<String> caseID;
    
    private List<Column> columns_list;
    private List<String> role;
    private List<String> activity;
    private Timestamp start_date;
    private Timestamp stop_date;
    private int line;

    public Critere(List<Column> columns_list,List<String> caseID, Timestamp start_date, Timestamp stop_date, List<String> role, List<String> activity, int line) {
        this.columns_list = columns_list;
        this.caseID = caseID;
        this.start_date = start_date;
        this.stop_date = stop_date;
        this.role = role;
        this.activity = activity;
        this.line = line;
    }

    public Seq<Column> getColumns() {
        return columns;
    }

    public void setColumns(Seq<Column> columns) {
        this.columns = columns;
    }

    public List<String> getCaseID() {
        return caseID;
    }

    public void setCaseID(List<String> caseID) {
        this.caseID = caseID;
    }

    public Timestamp getStart_date() {
        return start_date;
    }

    public void setStart_date(Timestamp start_date) {
        this.start_date = start_date;
    }

    public Timestamp getStop_date() {
        return stop_date;
    }

    public void setStop_date(Timestamp stop_date) {
        this.stop_date = stop_date;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public List<String> getActivity() {
        return activity;
    }

    public void setActivity(List<String> activity) {
        this.activity = activity;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public List<Column> getColumns_list() {
        return columns_list;
    }

    public void setColumns_list(List<Column> columns_list) {
        this.columns_list = columns_list;
    }
    
    
    
    
}
