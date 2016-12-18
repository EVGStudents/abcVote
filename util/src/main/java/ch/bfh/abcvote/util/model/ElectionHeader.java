/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.model;

import java.time.LocalDateTime;

/**
 * Class that only stores the header information of an election. The ElectionHeader Class is mostly used to list all elections
 * @author t.buerk
 */
public class ElectionHeader {
    
   private int id;
   private String title;
   private LocalDateTime startDate;
   private LocalDateTime endDate;

   /**
    * Constructor of the ElectionHeader
    * @param id
    * @param title
    * @param startDate
    * @param endDate 
    */
    public ElectionHeader(int id, String title, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Get the election id
     * @return 
     */
    public int getId() {
        return id;
    }

    /**
     * Get the election title
     * @return 
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the election starting Date
     * @return 
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Get the election ending Date
     * @return 
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }
   
    /**
     * Get the a string reptesentation of the ElectionHeader Data
     * @return 
     */
   @Override
   public String toString(){
       String result = title + ", voting period: " + startDate.toString() + " - " + endDate.toString();
       return result;
   }
}
