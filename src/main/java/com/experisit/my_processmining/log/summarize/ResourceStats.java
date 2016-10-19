/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.experisit.my_processmining.log.summarize;

/**
 *
 * @author youssef.hissou
 */
public class ResourceStats {
    
    private Integer min;
    private Long max;
    private Long avg;
    private Long mean;

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    public Long getAvg() {
        return avg;
    }

    public void setAvg(Long avg) {
        this.avg = avg;
    }

    public Long getMean() {
        return mean;
    }

    public void setMean(Long mean) {
        this.mean = mean;
    }

    public ResourceStats() {
    }
    
    
}
