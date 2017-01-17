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
package ch.bfh.abcvote.verifierapp.controllers;

import ch.bfh.abcvote.util.model.Ballot;
import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.util.model.ElectionResult;
import ch.bfh.abcvote.verifierapp.ControlledScreen;
import ch.bfh.abcvote.verifierapp.VerifierApp;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * FXML Controller class for the TimeStampValidation.fxml Screen
 *
 * @author t.buerk
 */
public class TimeStampValidationController implements Initializable, ControlledScreen {

    private MainController parentController;
    private ElectionResult result;
    
    @FXML
    private Button btBack;
    @FXML
    private Button btNext;
    @FXML
    private TableView<Ballot> tvBallots;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    /**
     * Sets the parentController for the communication with other controllers
     * @param screenParent 
     */
    @Override
    public void setScreenParent(MainController screenParent) {
        parentController = screenParent;
    }

    /**
     * Method to set the screen with a given election. Method not used in this controller class
     * @param election 
     */
    @Override
    public void setScene(Election election) {
    }

    /**
     * Method to set the screen with a given ElectionResult. 
     * @param result 
     */    
    @Override
    public void setScene(ElectionResult result) {
        this.result = result;
        populateBallotsTableView(result.getBallots());
    }
    
    /**
     * Method to set the screen when the screen of the corresponding controller is displayed. Method not used in this controller class
     */
    @Override
    public void setScene() {
        
    }

    /**
     * btBack-Button Clicked-Event: Transfers the user back to the election overview screen
     * @param event 
     */
    @FXML
    private void btBackClicked(ActionEvent event) {
         parentController.setScreen(VerifierApp.ELECTIONSOVERVIEWSCREENID);
    }
    
    /**
     * btNext-Button Clicked-Event: Preforms the validation process of the NIZKProofs on the passed Electionresult 
     * Afterwards the result gets passed to the NIZKP Validation screen
     * @param event 
     */
    @FXML
    private void btNextClicked(ActionEvent event) {
           ElectionResult result = parentController.doNIZKPVaildation(this.result);
           // pass electionResult to Resultscreen
           parentController.setScreenWithResult(VerifierApp.NIZKPVALIDATIONSCREENID, result);
    }
    
    /**
     * Displays the given list of Ballots in the tvBallots-TableView
     * @param ballotList 
     */
    private void populateBallotsTableView(List<Ballot> ballotList) {
        
        ObservableList<Ballot> listViewList = FXCollections.observableArrayList(ballotList);
        //construct tableView
        tvBallots.getColumns().clear();
        TableColumn idColumn = new TableColumn("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory("id"));
        TableColumn timestampColumn = new TableColumn("Timestamp");
        timestampColumn.setCellValueFactory(new PropertyValueFactory("timeStamp"));
        TableColumn optionColumn = new TableColumn("Option");
        optionColumn.setCellValueFactory(new PropertyValueFactory("selectedOptionsString"));
        TableColumn validColumn = new TableColumn("Valid");
        validColumn.setCellValueFactory(new PropertyValueFactory("valid"));
        TableColumn reasonColumn = new TableColumn("Reason");
        reasonColumn.setCellValueFactory(new PropertyValueFactory("reason"));        
        TableColumn identifierColumn = new TableColumn("Identifier");
        identifierColumn.setCellValueFactory(new PropertyValueFactory("u_HatString"));
        
        //add conditional coloring to validColumn cells
        validColumn.setCellFactory(new Callback<TableColumn<Ballot, Boolean>, TableCell<Ballot, Boolean>>() {
            @Override
            public TableCell<Ballot, Boolean> call(TableColumn<Ballot, Boolean> personStringTableColumn) {
                return new TableCell<Ballot, Boolean>() {
                    @Override
                    protected void updateItem(Boolean name, boolean empty) {
                        super.updateItem(name, empty);
                        if (name != null) {
                            setText(name.toString());
                            if (name){
                                setStyle("-fx-text-fill:green;");
                            }
                            else{
                                setStyle("-fx-text-fill:red;");
                            }
                            
                        }
                    }
                };
            }
        });

        tvBallots.getColumns().addAll(idColumn, timestampColumn, optionColumn, validColumn, reasonColumn, identifierColumn);
        tvBallots.setItems(listViewList);
        
    }
    
}
