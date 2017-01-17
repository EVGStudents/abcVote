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

import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

/**
 * Class to generate and store the private Credentials
 * @author t.buerk
 */
public class PrivateCredentials {
    
    private Element alpha;
    private Element beta;
    
    private Parameters parameters;
    
    /**
     * Creates new private Credentials with the given parameters.
     * @param parameters 
     */
    public PrivateCredentials(Parameters parameters){
        // pick Random Private Credentials
        ZMod Z_q = parameters.getZ_q();
        alpha = Z_q.getRandomElement();
        beta = Z_q.getRandomElement();
        this.parameters = parameters;
    }
    
    /**
     * Restores the private Credentials from the string repersentation of the alpha and beta elements
     * @param parameters
     * @param alphaString
     * @param betaString
     * @throws UniCryptException 
     */
    public PrivateCredentials(Parameters parameters, String alphaString, String betaString) throws UniCryptException{
        // restore private Credentials from String
        ZMod Z_q = parameters.getZ_q();
        alpha = Z_q.getElementFrom(alphaString);
        beta = Z_q.getElementFrom(betaString);
        this.parameters = parameters;
    }
    
    /**
     * get Alpha
     * @return 
     */
    public Element getAlpha() {
        return alpha;
    }
    
    /**
     * Get Beta
     * @return 
     */
    public Element getBeta() {
        return beta;
    }

    /**
     * Calculate and returns u
     * @return 
     */
    public Element getU() {        
        Element u = parameters.getH1().selfApply(alpha);
        u = u.apply(parameters.getH2().selfApply(beta));
        return u;
    }
    
    
}
