/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.model;

import java.time.LocalDateTime;

/**
 *
 * @author t.buerk
 */
public class ElectionHeader {
    
   private int id;
   private String title;
   private LocalDateTime startDate;
   private LocalDateTime endDate;

    public ElectionHeader(int id, String title, LocalDateTime startDate, LocalDateTime endDate) {
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
   
   public String toString(){
       String result = title + ", voting period: " + startDate.toString() + " - " + endDate.toString();
       return result;
   }
}
