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

import java.util.HashMap;
import java.util.List;

/**
 * Class to store the Results of an election
 * @author t.buerk
 */
public class ElectionResult {

    private HashMap<String, Integer> optionCount;
    private List<Ballot> ballots;
    private Election election;
    
    /**
     * Creates a new ElectioResult object for the given electipon
     * @param election 
     */
    public ElectionResult(Election election) {
        this.election = election;
        //initializes the optionCount Hashmap and creates an entry for every Option of the election
        optionCount = new HashMap<String, Integer>();
        
        for (String option : election.getTopic().getOptions()){
            optionCount.put(option, 0);
        }
    }

    /**
     * GEt the OptionCount Hashmap
     * @return 
     */
    public HashMap<String, Integer> getOptionCount() {
        return optionCount;
    }
    
    /**
     * Add one to the option counter to the given option 
     * @param option 
     */
    public void addToOptionCounter(String option){
        if(optionCount.containsKey(option)){
            optionCount.put(option, optionCount.get(option) + 1);
        }
    }

    /**
     * get a List of all the ballots
     * @return 
     */
    public List<Ballot> getBallots() {
        return ballots;
    }

    /**
     * Set the list off all the ballots
     * @param ballots 
     */
    public void setBallots(List<Ballot> ballots) {
        this.ballots = ballots;
    }

    /**
     * Get the election of the result
     * @return 
     */
    public Election getElection() {
        return election;
    }
    
    
    
}
