/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.helpers;

import ch.bfh.abcvote.util.model.Ballot;
import java.util.Comparator;

/**
 * Helper Class for sorting a list of ballots by timestamp attribute
 * @author t.buerk
 */
public class BallotComparator implements Comparator<Ballot> {

    @Override
    public int compare(Ballot ballot1, Ballot ballot2) {
        return ballot1.getTimeStamp().compareTo(ballot2.getTimeStamp());
    }
    
}
