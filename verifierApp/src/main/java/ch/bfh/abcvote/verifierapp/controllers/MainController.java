/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.verifierapp.controllers;

import ch.bfh.abcvote.util.controllers.CommunicationController;
import ch.bfh.abcvote.util.helpers.BallotComparator;
import ch.bfh.abcvote.util.model.Ballot;
import ch.bfh.abcvote.util.model.ElectionFilterTyp;
import ch.bfh.abcvote.util.model.ElectionHeader;
import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.util.model.ElectionResult;
import ch.bfh.abcvote.util.model.Parameters;
import ch.bfh.abcvote.util.model.Voter;
import ch.bfh.abcvote.verifierapp.ControlledScreen;
import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.classes.FiatShamirSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.classes.DoubleDiscreteLogProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.classes.EqualityPreimageProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.classes.PolynomialMembershipProofSystem;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.DiscreteLogarithmCommitmentScheme;
import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.helper.math.Polynomial;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
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
import java.util.Collections;
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
 * The MainController Class acts as the root element of the Stage.
 * It acts as a hub for the communication between the diffrent Screencontrollers
 * and between the Screencontrollers and the other Controller-Classes
 * @author t.buerk
 */
public class MainController extends StackPane {


    
    //Hashmap that holds all the registred Controllers and thie Screens
    private HashMap<String, Pair<ControlledScreen,Node>> screens = new HashMap<>();
    private CommunicationController communicationController;
    
    public MainController(){
        super();
        //new CommunicationController is created with the url of the bulletin board
        communicationController = new CommunicationController("https://abc.2488.ch/");   
    }
    
    /**
     * Adds a new Controller and Screen Pair to the Hashmap
     * @param name 
     * key value to store the screenPair in the hashmap
     * @param screenPair 
     * Pair<ControlledScreen,Node> containing the Controller and the corepsonding Screen 
     */
    public void addScreen(String name, Pair<ControlledScreen,Node> screenPair){
        screens.put(name, screenPair);
    }
    
    /**
     * Returns the Controller and Screen Pair for the given name
     * @param name
     * key value to reference the element to be return from the hashmap
     * @return 
     */
    public Pair<ControlledScreen,Node> getScreen(String name) {
        return screens.get(name);
        
    }
    
    /**
     * Loads the fxml file and add its Controller and Screen Pair and stores it with the key name in the Hashmap for further reference
     * @param name
     * key value under which the created Controller and Screen Pair gets stored
     * @param rescource
     * contains path of the referenced fxml file
     * @return 
     */
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
    
    /**
     * This method exchanges the current Screen with the new Screen given by the name  
     * @param name
     * @return 
     */
    private boolean changeScreen(final String name){
       //Checks if there is a Stored Screen for the given name
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
    
    /**
     * This method exchanges the current Screen with the new Screen given by the name and calls the new screens setScene method without passing any information  
     * @param name
     * @return 
     */
    public boolean setScreen(final String name){
        changeScreen(name);
        screens.get(name).getKey().setScene();
        return true;
    }
    
    /**
     * This method will remove the Controller and screen  pair with the given name from the collection of Pairs
     * @param name
     * @return 
     */
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null){
            System.out.println("Screen didn't exist");
            return false;
        }
        else{
            return true;
        }
    }

    

    /**
     * this method changes the current Screen to the given name
     * and afterwards calls that screens setScene Method to pass the given election object for display 
     * @param name
     * @param election
     * @return 
     */
    public boolean setScreenWithElection(String name, Election election) {
        
        changeScreen(name);
        screens.get(name).getKey().setScene(election);
        
        return true;
    }

    /**
     * this method changes the current Screen to the given name
     * and afterwards calls that screens setScene Method to pass the given ElectionResult object for display 
     * @param name
     * @param result
     * @return 
     */
    public boolean setScreenWithResult(String name, ElectionResult result) {
        changeScreen(name);
        screens.get(name).getKey().setScene(result);
        
        return true;
    }
    
    /**
     * Gets a list of ElectionHeaders from the bulletin board. The selected ElectionHeaders depend on the passed filter option
     * @param electionFilterTyp
     * given filter option can be set to ALL, CLOSED or OPEN
     * @return 
     */
    List<ElectionHeader> getElectionHeaders(ElectionFilterTyp electionFilterTyp) {
        List<ElectionHeader> electionHeadersList = communicationController.getElectionHeaders(electionFilterTyp);
        return electionHeadersList;
    }

    /**
     * Gets the election data for the given electionID from the bulletin board and rturns it as an election object
     * @param electionId
     * @return 
     */
    Election getElectionById(int electionId) {
        Election election = communicationController.getElectionById(electionId);
        return election;
    }

    /**
     * Gets a list of all the posted ballots for the given election and returns it
     * @param election
     * @return 
     */
    public List<Ballot>  getBallotsByElection(Election election) {
        List<Ballot> ballots = communicationController.getBallotsByElection(election);
        return ballots;
    }

    /**
     * Takes a ballot list and a corresponding election object an calculates the result. The result is return as an ElectionResult object
     * @param election
     * @param ballots
     * @return 
     */
    ElectionResult calculateElectionResult(Election election, List<Ballot> ballots) {
        ElectionResult result = doTimestampVaildation(election, ballots);
        result = doNIZKPVaildation(result);
        result = SelectFromValidBallotsAndCalculateResult(result);
        return result;
    }
    
    /**
     * Takes a list of voters and a the ZMOd Group Z_p and calculates the credential polynom with the public credentials of the listed voters
     * @param Z_p
     * @param voters
     * @return 
     */
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
                System.out.println("could not convert u");
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

    /**
     * Passes the recieved ElectionResult to the communicationController to post it to the bulletin board
     * @param result 
     */
    public void postResult(ElectionResult result) {
        communicationController.postResult(result);
    }

    /**
     * Method checks if the ballots were commited during the voting period. Ballots that were not commited during the voting period are marked as invalid
     * @param election
     * @param ballots
     * @return 
     */
    ElectionResult doTimestampVaildation(Election election, List<Ballot> ballots) {
        ElectionResult result = new ElectionResult(election);
    
        //sort ballots in the order of thier timestamp
        Collections.sort(ballots, new BallotComparator());
        
        //check if ballot was posted druing voting period
        for(Ballot ballot: ballots){
            if (!ballot.isValid()){
                continue;
            }
            LocalDateTime startDate = election.getStartDate();
            LocalDateTime endDate = election.getEndDate();
            LocalDateTime ballotTimeStamp = ballot.getTimeStamp();
            if (!(startDate.compareTo(ballotTimeStamp) < 0 && endDate.compareTo(ballotTimeStamp) > 0)){
                ballot.setInvalid("not in voting period");
                System.out.println("Rejected! Reason: not in voting period ID: " + ballot.getId());
            }  
        }
        
        result.setBallots(ballots);

        return result;
    }

    /**
     * Method checks all for all valid ballots in the given result object if thier NIZKPs are valid
     * If a ballot fails one of the checks it gest marked as invalid
     * @param result
     * @return 
     */
    ElectionResult doNIZKPVaildation(ElectionResult result) {
        Election election = result.getElection();
        List<Ballot> ballots = result.getBallots();
        List<Voter> voters = election.getVoterList();
        Parameters parameters = election.getGenerators();
        ZMod Z_p = parameters.getZ_p();
        PolynomialElement credentialPolynomial = calculateCredentialPolynomial(Z_p, voters); 
        
        HashMap<String, Pair<SigmaChallengeGenerator,SigmaChallengeGenerator>> challengeGenerators = new HashMap<>();
        
        //pereparation for Pi3
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
        
        //generate ChallengeGenerators for all valid Ballots
        for(Ballot ballot: ballots){
            if (!ballot.isValid()){
                continue;
            }
            if (!challengeGenerators.containsKey(ballot.getSelectedOptionsString())){
                Element e = StringMonoid.getInstance(Alphabet.ALPHANUMERIC).getElement(ballot.getSelectedOptionsString());
                SigmaChallengeGenerator fsscg = FiatShamirSigmaChallengeGenerator.getInstance(parameters.getCommitmentSchemeP().getMessageSpace(), e);
                SigmaChallengeGenerator fsscg2 = FiatShamirSigmaChallengeGenerator.getInstance(parameters.getZ_q(), e);
                challengeGenerators.put(ballot.getSelectedOptionsString(), new Pair(fsscg,fsscg2));
            }
 
        }
        
        //check if proves are valid     
        for(Ballot ballot: ballots){
            if (!ballot.isValid()){
                continue;
            }
            SigmaChallengeGenerator fsscg = challengeGenerators.get(ballot.getSelectedOptionsString()).getKey();
            SigmaChallengeGenerator fsscg2 = challengeGenerators.get(ballot.getSelectedOptionsString()).getValue();
            //P1 Proofsystem
            PolynomialMembershipProofSystem pmps = PolynomialMembershipProofSystem.getInstance(fsscg,credentialPolynomial, parameters.getCommitmentSchemeP());
            if(!pmps.verify(ballot.getPi1(), ballot.getC())){
               ballot.setInvalid("PI1-Proof failed");
                System.out.println("Rejected! Reason: PI1-Proof failed: " + ballot.getId());
               continue;
            }
            //P2 Proofsystem
            DoubleDiscreteLogProofSystem ddlps = DoubleDiscreteLogProofSystem.getInstance(fsscg, parameters.getCommitmentSchemeP(), parameters.getCommitmentSchemeQ(), parameters.getSECURITY_FACTOR());
            if(!ddlps.verify(ballot.getPi2(), Tuple.getInstance(ballot.getC(), ballot.getD()))){
               ballot.setInvalid("PI2-Proof failed");
               System.out.println("Rejected! Reason: PI2-Proof failed: " + ballot.getId());
               continue;
            }
            //P3
            //Create a EqualityPreimageProofSystem with the PedersenCommitmentFunction and the adapted DicreteLogFunction
            EqualityPreimageProofSystem epps = EqualityPreimageProofSystem.getInstance(fsscg2, pedersenFunction, adaptedDiscreteLogFunction);
            if(!epps.verify(ballot.getPi3(), Tuple.getInstance(ballot.getD(), ballot.getU_Hat()))){
               ballot.setInvalid("PI3-Proof failed");
               System.out.println("Rejected! Reason: PI3-Proof failed: " + ballot.getId());
               continue; 
            }
        }
        
        return result; 
    }

    /**
     * Selects the first posted ballot of each voter that is still valid and calculates the result of the election
     * @param result
     * @return 
     */
    ElectionResult SelectFromValidBallotsAndCalculateResult(ElectionResult result) {
        Election election = result.getElection();
        List<Ballot> ballots = result.getBallots();

        //select the first ballot posted by each voter that is still valid. 
        List<Element> usedU_Hats = new ArrayList<Element>();
        List<String> electionOptions = election.getTopic().getOptions();
        int pick = election.getTopic().getPick();
        for(Ballot ballot: ballots){
            if (!ballot.isValid()){
                continue;
            }
            if (usedU_Hats.contains(ballot.getU_Hat())){
                //ballot gets rejected if a ballot of the same voter has already been selected
                ballot.setInvalid("Already selected another vote of the same voter");
                System.out.println("Rejected! Reason: Already selected another vote: " + ballot.getId());
            }
            else{
                //ballot gets selected regardless of wether the vote itself is valid or not
                usedU_Hats.add(ballot.getU_Hat());
                //check if the right amount of options was picked
                if(pick == ballot.getSelectedOptions().size()){
                    //check if all selected options are part of the original options
                    if (electionOptions.containsAll(ballot.getSelectedOptions())){
                        //Check for duplicates
                        Set<String> set = new HashSet<String>(ballot.getSelectedOptions());
                        if(!(set.size() < ballot.getSelectedOptions().size())){
                            //if vote is valid it gets added to the totals
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
    
    
}
