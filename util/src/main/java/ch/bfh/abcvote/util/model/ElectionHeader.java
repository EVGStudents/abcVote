/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.model;

import java.time.LocalDate;

/**
 *
 * @author t.buerk
 */
public class ElectionHeader {
    
   private int id;
   private String title;
   private LocalDate startDate;
   private LocalDate endDate;

    public ElectionHeader(int id, String title, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
   
   public String toString(){
       String result = title + ", voting period: " + startDate.toString() + " - " + endDate.toString();
       return result;
   }
}
