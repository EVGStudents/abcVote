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

/**
 * Class that stores all the inforamation of a voter
 * @author t.buerk
 */
public class Voter {
    
    String email;
    String publicCredential;
    String appVersion;
    
    /**
     * Create a new voter object
     * @param email
     * @param publicCredential
     * @param appVersion 
     */
    public Voter(String email, String publicCredential, String appVersion){
        this.email = email;
        this.publicCredential = publicCredential;
        this.appVersion = appVersion;
    }
    
    /**
     * Get email adsress
     * @return 
     */
    public String getEmail(){
        return email;
    }
    
    /**
     * get publicCredentials u
     * @return 
     */
    public String getPublicCredential(){
        return publicCredential;
    }
    
    /**
     * Get AppVersion of the voter
     * @return 
     */
    public String getAppVersion(){
        return appVersion;
    }
    
    /**
     * Get the String representation of the voter for listing it
     * @return 
     */
    @Override
    public String toString(){
        String printedString = email + ", version: " + appVersion;
        return printedString;
    }
}
