/*
 * abcVote
 *
 *  abcVote - an e-voting prototype with everlasting privacy
 *  Copyright (c) 2017 Timo Buerk and Sebastian Nellen
 *
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 *
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *  2. Licensees holding valid commercial licenses for abcVote may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and us.
 *
 *
 *   For further information contact <e-mail: burkt4@gmail.com> or <e-mail: sebastian@nellen.it>
 *
 *
 * Redistributions of files must retain the above copyright notice.
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
