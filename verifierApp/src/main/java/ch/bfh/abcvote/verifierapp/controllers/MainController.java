/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.verifierapp.controllers;

import ch.bfh.abcvote.util.controllers.CommunicationController;
import ch.bfh.abcvote.util.model.Ballot;
import ch.bfh.abcvote.util.model.ElectionFilterTyp;
import ch.bfh.abcvote.util.model.ElectionHeader;
import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.util.model.ElectionResult;
import ch.bfh.abcvote.util.model.Parameters;
import ch.bfh.abcvote.util.model.Voter;
import ch.bfh.abcvote.verifierapp.ControlledScreen;
import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.crypto.proofsystem.classes.DoubleDiscreteLogProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.classes.EqualityPreimageProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.classes.PolynomialMembershipProofSystem;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.DiscreteLogarithmCommitmentScheme;
import ch.bfh.unicrypt.helper.math.Polynomial;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 *
 * @author t.buerk
 */
public class MainController extends StackPane {


    
    //Hashmap that holds all the registred Controllers and thie Screens
    private HashMap<String, Pair<ControlledScreen,Node>> screens = new HashMap<>();
    private CommunicationController communicationController;
    
    public MainController(){
        super();
        communicationController = new CommunicationController("http://abc.2488.ch/");   
    }
    
    //Adds a new a new Controller and Screen Pair to the Hashmap
    public void addScreen(String name, Pair<ControlledScreen,Node> screenPair){
        screens.put(name, screenPair);
    }
    
    //Returns the Controller and Screen Pair for the given name
    public Pair<ControlledScreen,Node> getScreen(String name) {
        return screens.get(name);
        
    }
    
    //load the fxml file, and add its Controller and Screen Pair to the Hashmap for further refrence
    public boolean loadScreen(String name, String rescource){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rescource));
            Parent loadScreen = (Parent) loader.load();
            ControlledScreen screenController = (ControlledScreen) loader.getController();
            screenController.setScreenParent(this);
            Pair<ControlledScreen,Node> screenPair = new Pair<ControlledScreen,Node>(screenController, loadScreen);
            addScreen(name, screenPair);
            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    //This method exchanges the current Screen with the new Screen given by the name
    private boolean changeScreen(final String name){
       //Checks if there is a Stored Screnn for the given name
        if (screens.get(name) != null){
            final DoubleProperty opacity = opacityProperty();
            //checks if there is screen that is already displayed
            if ( !getChildren().isEmpty()){
                //if there is an old screen present it gets faded out before the new screen gets faded in
               Timeline fade = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                new KeyFrame(new Duration(1000), new EventHandler<ActionEvent>() {
                    
                    @Override
                    public void handle(ActionEvent t) {
                        getChildren().remove(0);
                        getChildren().add(0,screens.get(name).getValue());
                        Timeline fadeIn = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));
                        fadeIn.play();
                        
                    }
                }, new KeyValue(opacity, 0.0)));
                fade.play();   
            }
            else{
               //if there is no old screen the new screen just gets faded in
               setOpacity(0.0);
               getChildren().add(screens.get(name).getValue());
               Timeline fadeIn = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                    new KeyFrame(new Duration(2500), new KeyValue(opacity, 1.0)));
               fadeIn.play();
            }
            return true;
               
        }  
        else{
           System.out.println("screen hasn't been loaded! \n");
           return false;
        
        } 
    }
    
    //This method exchanges the current Screen with the new Screen given by the name
    //And afterwards calls its setScrene Method
    public boolean setScreen(final String name){
        changeScreen(name);
        screens.get(name).getKey().setScene();
        return true;
    }
    
    //This method will remove the Controller and screen  pair with the given name from the collection of Pairs
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null){
            System.out.println("Screen didn't exist");
            return false;
        }
        else{
            return true;
        }
    }

    
    //this method changes the current Screen to the given name
    //and afterwards calls that screens setScene Method to pass the given election object for display 
    public boolean setScreenWithElection(String name, Election election) {
        
        changeScreen(name);
        screens.get(name).getKey().setScene(election);
        
        return true;
    }

    List<ElectionHeader> getElectionHeaders(ElectionFilterTyp electionFilterTyp) {
        List<ElectionHeader> electionHeadersList = communicationController.getElectionHeaders(electionFilterTyp);
        return electionHeadersList;
    }

    Election getElectionById(int electionId) {
        Election election = communicationController.getElectionById(electionId);
        return election;
    }

    public List<Ballot>  getBallotsByElection(Election election) {
        List<Ballot> ballots = communicationController.getBallotsByElection(election);
        return ballots;
    }

    ElectionResult calculateElectionResult(Election election, List<Ballot> ballots) {
        ElectionResult result = new ElectionResult(election);
        List<Voter> voters = election.getVoterList();
        Parameters parameters = election.getGenerators();
        ZMod Z_p = parameters.getZ_p();
        PolynomialElement credentialPolynomial = calculateCredentialPolynomial(Z_p, voters);  
        //P1 Proofsystem
        PolynomialMembershipProofSystem pmps = PolynomialMembershipProofSystem.getInstance(election.getCredentialPolynomial(), parameters.getCommitmentSchemeP());
        //P2 Proofsystem
        DoubleDiscreteLogProofSystem ddlps = DoubleDiscreteLogProofSystem.getInstance(parameters.getCommitmentSchemeP(), parameters.getCommitmentSchemeQ(), parameters.getSECURITY_FACTOR());
        //P3
        DiscreteLogarithmCommitmentScheme discreteLogCommitmentScheme = DiscreteLogarithmCommitmentScheme.getInstance(election.getH_Hat());
        //Extract DicreteLogFunction
        Function discreteLogFunction = discreteLogCommitmentScheme.getCommitmentFunction();
        //Extract the the CommitmentFunction from the pedersenCommitmentScheme
        Function pedersenFunction = parameters.getCommitmentSchemeQ().getCommitmentFunction();
	
        //in order to put the two functions in the same ProofSystem they need to have the same Domains.	
        //define a selection Function with the same Domain as the PedersenCommitmentFunction that simply selects the Beta Element and returns it
        ProductSet space = (ProductSet) pedersenFunction.getDomain();		
        Function adapterFunction = SelectionFunction.getInstance(space, 0,1); //0,1 : Pfad für selektiertes Element 0 für MessageElements -> 1 für Beta
        // We chain the selection Function and the discreteLogFunction together. So the output of the selection function acts as input for the discreteLogFunction
        Function adaptedDiscreteLogFunction = CompositeFunction.getInstance(adapterFunction,discreteLogFunction);

        //Create a EqualityPreimageProofSystem with the PedersenCommitmentFunction and the adapted DicreteLogFunction
        EqualityPreimageProofSystem epps = EqualityPreimageProofSystem.getInstance(pedersenFunction, adaptedDiscreteLogFunction);
            
        //check if ballot was posted druing voting period
        for(Ballot ballot: ballots){
            if (!ballot.isValid()){
                continue;
            }
            LocalDateTime startDate = election.getStartDate();
            LocalDateTime endDate = election.getEndDate();
            LocalDateTime ballotTimeStamp = ballot.getTimeStamp();
            if (!(startDate.compareTo(ballotTimeStamp) < 0 && endDate.compareTo(ballotTimeStamp) > 0)){
                ballot.setValid(false);
            }  
        }
        //check if proves are valid     
        for(Ballot ballot: ballots){
            if (!ballot.isValid()){
                continue;
            }
            if(!pmps.verify(ballot.getPi1(), ballot.getC())){
               ballot.setValid(false);
               continue;
            }
            if(!ddlps.verify(ballot.getPi2(), Tuple.getInstance(ballot.getC(), ballot.getD()))){
               ballot.setValid(false);
               continue;
            }
            if(!epps.verify(ballot.getPi3(), Tuple.getInstance(ballot.getD(), ballot.getU_Hat()))){
               ballot.setValid(false);
               continue; 
            }
        }
        
        List<Element> usedU_Hats = new ArrayList<Element>();
        List<String> electionOptions = election.getTopic().getOptions();
        int pick = election.getTopic().getPick();
        for(Ballot ballot: ballots){
            if (!ballot.isValid()){
                continue;
            }
            if (usedU_Hats.contains(ballot.getU_Hat())){
                ballot.setValid(false);
            }
            else{
                usedU_Hats.add(ballot.getU_Hat());
                //check if vote is valid
                
                //check if the right amount of options was picked
                if(pick == ballot.getSelectedOptions().size()){
                    //check if all selected options are part of the original options
                    if (electionOptions.containsAll(ballot.getSelectedOptions())){
                        //Check for duplicates
                        Set<String> set = new HashSet<String>(ballot.getSelectedOptions());
                        if(!(set.size() < ballot.getSelectedOptions().size())){
                            for (String option : ballot.getSelectedOptions()){
                              result.addToOptionCounter(option);  
                            }   
                        }
                    }
                }             
            }           
        }
        return result;
    }
    
    private PolynomialElement calculateCredentialPolynomial(ZMod Z_p, List<Voter> voters){
        DualisticElement zero = Z_p.getZeroElement();
	DualisticElement one = Z_p.getOneElement();
        PolynomialElement credentialPolynomial = null;
        for (Voter voter : voters){
            Element u;
            try {
                u = Z_p.getElementFrom(voter.getPublicCredential());
            } catch (UniCryptException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }
            Polynomial newRoot = Polynomial.getInstance(new DualisticElement[]{(DualisticElement) u.invert(), one}, zero, one);
            
            if (credentialPolynomial == null){
                
                credentialPolynomial = PolynomialSemiRing.getInstance(Z_p).getElement(newRoot);               
            }
            else{      
                credentialPolynomial = credentialPolynomial.multiply(PolynomialSemiRing.getInstance(Z_p).getElement(newRoot));
            }
        }
        return credentialPolynomial;
    }

    public boolean setScreenWithResult(String name, ElectionResult result) {
        changeScreen(name);
        screens.get(name).getKey().setScene(result);
        
        return true;
    }

    public void postResult(ElectionResult result) {
        communicationController.postResult(result);
    }
    
    
}
