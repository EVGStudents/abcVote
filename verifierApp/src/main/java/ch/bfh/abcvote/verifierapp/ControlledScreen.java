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
package ch.bfh.abcvote.verifierapp;

import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.util.model.ElectionResult;
import ch.bfh.abcvote.verifierapp.controllers.MainController;

/**
 * Interface all scene controllers implement in order to create a uniform way to pass along maincontroller, election or electionResult objects
 * @author t.buerk
 */
public interface ControlledScreen {
    
    /**
     * This method allwos to pass the MainController to the controller of a screen 
     * @param screenParent 
     */
    public void setScreenParent(MainController screenParent);
    
    /**
     * This method will allow the maincontroller to pass the controller a election object to save and/or display
     * @param election 
     */
    public void setScene(Election election);
    
    /**
     * This method will allow the maincontroller to pass the controller a electionResult object to save and/or display
     * @param result 
     */
    public void setScene(ElectionResult result);
    
    /**
     * This method will allow the maincontroller to signal the controller that it is about to be displayed 
     */
    public void setScene();
}
