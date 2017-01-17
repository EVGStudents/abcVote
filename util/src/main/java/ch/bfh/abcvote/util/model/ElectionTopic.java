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

import java.util.ArrayList;
import java.util.List;

/**
 * Class that stores the topic and the available options off an election as well as the number of options a voter can pick
 * @author t.buerk
 */
public class ElectionTopic {
    
    String title;
    List<String> options;
    int pick;
    
    /**
     * Creates a new ElectionTopic object with the given title and pick and an empty options list
     * @param title
     * @param pick 
     */
    public ElectionTopic(String title, int pick){
        this.title = title;
        this.pick = pick;
        this.options = new ArrayList<String>();
    }
    
    /**
     * returns the topic Title
     * @return 
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the topic of the Topic
     * @param title 
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the number of options that can be picked by the voter
     * @return 
     */
    public int getPick() {
        return pick;
    }
    /**
     * Set the number of options that can be picked by the voter
     * @param pick 
     */
    public void setPick(int pick) {
        this.pick = pick;
    }

    /**
     * Gets a list of all the options of the topic
     * @return 
     */
    public List<String> getOptions() {
        return options;
    }

    /**
     * add the given String to the options list
     * @param option 
     */
    public void addOption(String option){
        options.add(option);
    }

    
}
