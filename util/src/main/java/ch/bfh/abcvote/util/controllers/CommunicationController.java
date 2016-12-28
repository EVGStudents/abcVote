/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.controllers;

import ch.bfh.abcvote.util.model.Ballot;
import ch.bfh.abcvote.util.model.ElectionFilterTyp;
import ch.bfh.abcvote.util.model.ElectionHeader;
import ch.bfh.abcvote.util.model.Parameters;
import ch.bfh.abcvote.util.model.Election;
import ch.bfh.abcvote.util.model.ElectionResult;
import ch.bfh.abcvote.util.model.ElectionTopic;
import ch.bfh.abcvote.util.model.Voter;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jose4j.lang.JoseException;


/**
 * Class that is in charge of communicating with the Bulletin Board.
 * It sends requests to the Bulletin Board and converts the recieved JSON Data into Java Objects and returns them.
 * Or converts java objects into JSON Objects and posts them to the Bulletin Board
 * @author t.buerk
 */
public class CommunicationController {

    String bulletinBoardUrl;
    
    /**
     * Creates a new CommunicationController Object with the given bulletinBoardUrl.
     * The bulletinBoardUrl is stored in a global variable and used as the baseUrl for every conntction with the bulletin board
     * @param bulletinBoardUrl 
     */
    public CommunicationController(String bulletinBoardUrl){
        this.bulletinBoardUrl = bulletinBoardUrl;
    }
    
    /**
     * Gets the parameters of the bulletin board from the bulletin board and converts the recieved Json in a Parameters object and returns it
     * @return 
     */
    public Parameters getParameters() {
        try {
             //Get parameters json with a get request 
             URL url = new URL(bulletinBoardUrl + "/parameters");
             
             InputStream urlInputStream = url.openStream();
             JsonReader jsonReader = Json.createReader(urlInputStream);
             JsonObject obj = jsonReader.readObject();
                //Json contains String representations of the parameter elements
                String oString = obj.getString("o");
                String pString = obj.getString("p");
                String h0String = obj.getString("h0");
                String h1String = obj.getString("h1");
                String h2String = obj.getString("h2");
                String g0String = obj.getString("g0");
                String g1String = obj.getString("g1");
                
                //Converting the parameter Element Strings into Parameters Object containing the restored Parameter Elements
                Parameters parameters = new Parameters(oString, pString, h0String, h1String, h2String, g0String, g1String);
                return parameters;
         } catch (Exception x) {
             System.err.println(x);
             return null;
         }
 
        
    }

    /**
     * gets the all registered voters from the bulletin board and returns them as a List of Voter objects
     * @return 
     */
    public List<Voter> getAllARegisteredVoters() {
       List<Voter> voterlist = new ArrayList<Voter>(); 
        	
         try {
             URL url = new URL(bulletinBoardUrl + "/voters");
             
             InputStream urlInputStream = url.openStream();
             JsonReader jsonReader = Json.createReader(urlInputStream);
             JsonArray obj = jsonReader.readArray();
             //For each Voter-Json in the Json Array
             for (JsonObject result : obj.getValuesAs(JsonObject.class)) {
                 //JsonData converted into Voter Object and added to the list  
                String email = result.getString("email");       
                String publicCredential = result.getString("publicCredential");
                String appVersion = result.getString("appVersion");
                
                Voter voter = new Voter(email, publicCredential, appVersion);
                voterlist.add(voter);
             }
             
         } catch (IOException x) {
             System.err.println(x);
         }
       return voterlist;
    }

    /**
     * Converts the given election object to a Json String. The Json string is then signed.
     * The resulting JWS gets posted to the bulletin board
     * @param election
     * election object to be posted to the bulletin board
     */
    public void postElection(Election election){
        JsonObjectBuilder jBuilder = Json.createObjectBuilder();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        //Add Header information to the json object
        //TODO get email address from certificate 
        jBuilder.add("author", "alice@bfh.ch");
        jBuilder.add("electionTitle", election.getTitle());
        jBuilder.add("beginDate", election.getStartDate().format(format));
        jBuilder.add("endDate", election.getEndDate().format(format));
        //toDo: get AppVersion dynamically from build 
        jBuilder.add("appVersion", "0.15");
             
        jBuilder.add("coefficients", election.getCredentialPolynomialString());
        jBuilder.add("electionGenerator", election.getH_HatString());
        
        //Add votingTopic to the Json Object
        JsonObjectBuilder votingTopicBuilder = Json.createObjectBuilder();
        votingTopicBuilder.add("topic", election.getTopic().getTitle());
        votingTopicBuilder.add("pick", election.getTopic().getPick());
        
        JsonArrayBuilder optionsBuilder = Json.createArrayBuilder();
        for (String option : election.getTopic().getOptions()) {
                optionsBuilder.add(option);
        }
        votingTopicBuilder.add("options", optionsBuilder);
        
        jBuilder.add("votingTopic", votingTopicBuilder);
        
        //Add the list of selected Voters to the Json object
        JsonArrayBuilder votersBuilder = Json.createArrayBuilder();
        
        for (Voter voter : election.getVoterList()){
            JsonObjectBuilder voterBuilder = Json.createObjectBuilder();
            voterBuilder.add("email", voter.getEmail());
            voterBuilder.add("publicCredential", voter.getPublicCredential());
            voterBuilder.add("appVersion", voter.getAppVersion());
            
            votersBuilder.add(voterBuilder);
        }
        
        jBuilder.add("voters", votersBuilder);
        
        JsonObject model = jBuilder.build();
        //finished Json gets singed
        SignatureController signController = new SignatureController();
        JsonObject signedModel = null;
                
        try {
            signedModel = signController.signJson(model);
        } catch (Exception ex) {
            Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //JWS gets posted to the bulletin board
        try { 
            boolean requestOK = postJsonStringToURL(bulletinBoardUrl +  "/elections", signedModel.toString(), false);
            if (requestOK) {
                System.out.println("Election posted!");
            }
            else{
                System.out.println("Was not able to post Election! Did not receive expected http 200 status.");
            }
        } catch (IOException ex) {
            System.out.println("Was not able to post Election! IOException");
        }

    }
    
    /**
     * Helper method which posts the given json-string json to the given url 
     * @param url
     * Exact url where the jsonData should get posted to
     * @param json
     * String of the JsonData which should be posted to the bulletin board
     * @param useTor
     * Boolean indicating if Json should be posted over Tor network
     * @return
     * retruns whether or not the data was sucessfully posted
     * @throws IOException 
     */
    public boolean postJsonStringToURL(String url, String json, Boolean useTor) throws IOException{
        boolean responseOK = true;
        boolean usingTor = useTor;
        
        HttpHost proxy = new HttpHost("127.0.0.1", 9050);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setProxy(proxy);
        CloseableHttpClient httpClient = httpClientBuilder.create().build();
  
        //TODO App only get's new Tor circuit, when it's closed :-/
        
        if (usingTor == true) {
            try {
                // Set Proxy
                System.setProperty("socksProxyHost", "127.0.0.1");
                System.setProperty("socksProxyPort", "9050");
                
                //prepare the post request
                HttpPost request = new HttpPost(url);
                StringEntity params = new StringEntity(json);
                request.addHeader("content-type", "application/json");
                request.setEntity(params);

                //sending post request and checking response
                CloseableHttpResponse  response = httpClient.execute(request);
                System.out.println(response);
                if (response.getStatusLine().getStatusCode() != 200){
                    responseOK = false;
                }
                response.close();
                request.completed();
                
            } catch (Exception ex) {
                responseOK = false;
            } finally {
                httpClient.close();
                
                // 'Unset' the proxy.
                System.clearProperty("socksProxyHost");  
            }
        } else {
            try {
                //prepare the post request
                HttpPost request = new HttpPost(url);
                StringEntity params = new StringEntity(json);
                request.addHeader("content-type", "application/json");
                request.setEntity(params);

                //sending post request and checking response
                HttpResponse  response = httpClient.execute(request);
                System.out.println(response);
                if (response.getStatusLine().getStatusCode() != 200){
                    responseOK = false;
                }

            } catch (Exception ex) {
                responseOK = false;
            } finally {
                httpClient.close();
            }
        } 
        return responseOK;
    }    

    /**
     * Registers a new voter with the given email address and the given public credentials u on the bulletin board
     * @param email
     * email address under which the new voter should be registered
     * @param parameters
     * parameters used by the bulletin board
     * @param u
     * @return
     * @throws JoseException
     * @throws Exception 
     */
    //TODO check if parameters are still needed
    public boolean registerNewVoter(String email, Parameters parameters, Element u) throws JoseException, Exception{
        
        //create Voter json
        JsonObjectBuilder jBuilder = Json.createObjectBuilder();
        
        jBuilder.add("email", email);
        jBuilder.add("publicCredential", u.convertToString());
        jBuilder.add("appVersion", "1.15");
        
        JsonObject model = jBuilder.build();
        
        SignatureController signController = new SignatureController();
        JsonObject signedModel = null;
                
        try {
            signedModel = signController.signJson(model); 
        } catch (Exception ex) {
            Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //post Voter
        try { 
            boolean requestOK = postJsonStringToURL(bulletinBoardUrl +  "/voters", signedModel.toString(), false);
            if (requestOK) {
                System.out.println("Voter posted!");
                return true;
            }
            else{
                System.out.println("Was not able to post Voter!");
                return false;
            }
        } catch (IOException ex) {
            System.out.println("Was not able to post Voter!");
            return false;
        }
        
    }

    /**
     * Gets a List of ElectionHeaders from the Bulletin Board and returns it. Fetched list depends on the given ElectionFilterTyp
     * @param filter
     * The filter can be set to All, Open or Closed
     * @return 
     */
    public List<ElectionHeader> getElectionHeaders(ElectionFilterTyp filter) {
        List<ElectionHeader> electionHeaderlist = new ArrayList<ElectionHeader>(); 
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime actualDateTime = LocalDateTime.now();
        String dateTimeString = actualDateTime.format(format);
        
        URL url = null;
        //depending on the filter a different request is sent to the bulletin board
        switch (filter) {
            // if the filter is set to ALL, all the electionheaders on the bulletin board are requested
            case ALL: 
                {
                    try {
                        url = new URL(bulletinBoardUrl + "/elections");
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }  
                break; 
            
                // if the filter is set to OPEN only ElectionHeaders where the VotingPeriod is still going are requested from the bulletin board
            case OPEN:
                {
                    try {
                        url = new URL(bulletinBoardUrl + "/elections/open?date=" + URLEncoder.encode(dateTimeString, "UTF-8").replace("+", "%20"));
                    } catch (UnsupportedEncodingException | MalformedURLException ex) {
                        System.err.println(ex);
                    }
                }
                break; 
                // if the filter is set to CLOSED only ElectionHeaders where the VotingPeriod is already over are requested from the bulletin board
            case CLOSED:
                {
                    try {
                        url = new URL(bulletinBoardUrl + "/elections/closed?date=" + URLEncoder.encode(dateTimeString, "UTF-8").replace("+", "%20"));
                    } catch (UnsupportedEncodingException | MalformedURLException ex) {
                        System.err.println(ex);
                    }
                }
                break; 
        }        
            
        try {    
            
            InputStream urlInputStream = url.openStream();
            JsonReader jsonReader = Json.createReader(urlInputStream);
            JsonArray obj = jsonReader.readArray();
            //Recieved Json String is transformed into a list of ElectionHeader objects
            for (JsonObject result : obj.getValuesAs(JsonObject.class)) {

               int id = Integer.parseInt(result.getString("id"));       
               String title = result.getString("electionTitle");
               LocalDateTime beginDate = LocalDateTime.parse(result.getString("beginDate"), format);
               LocalDateTime endDate= LocalDateTime.parse(result.getString("endDate"), format);

               ElectionHeader electionHeader = new ElectionHeader(id, title, beginDate, endDate);
               electionHeaderlist.add(electionHeader);
            }
        } catch (IOException x) {
            System.err.println(x); 
       }
        
        
       return electionHeaderlist;
    }

    /**
     * Gets the information for the given ElectionID from the bulletin board and returns it as a election object
     * @param electionId
     * @return 
     */
    public Election getElectionById(int electionId) {
        Election election = null;
        try {

             URL url = new URL(bulletinBoardUrl + "/elections/" + electionId);
             
             InputStream urlInputStream = url.openStream();
             JsonReader jsonReader = Json.createReader(urlInputStream);
             JsonObject obj = jsonReader.readObject();
             DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
             Parameters parameters = this.getParameters();
             
             //gets the json string and transforms it into a election object
             
             //translates the header information of the election
             String title = obj.getString("electionTitle");
             LocalDateTime beginDate = LocalDateTime.parse(obj.getString("beginDate"), format);
             LocalDateTime endDate = LocalDateTime.parse(obj.getString("endDate"), format);
             String appVersion = obj.getString("appVersion");
             String coefficientsString = obj.getString("coefficients");
             String h_HatString = obj.getString("electionGenerator");
             List<Voter> voterlist = new ArrayList<Voter>(); 
             
             //get th list of voters
             for (JsonObject result : obj.getJsonArray("voters").getValuesAs(JsonObject.class)) {
                   
                String voterEmail = result.getString("email");       
                String voterPublicCredential = result.getString("publicCredential");
                String voterAppVersion = result.getString("appVersion");
                   
                Voter voter = new Voter(voterEmail, voterPublicCredential, voterAppVersion);
                voterlist.add(voter);
             }
             //get the votingTopic
             JsonObject electionTopicObj = obj.getJsonObject("votingTopic");
             
             String topic = electionTopicObj.getString("topic");
             int pick = electionTopicObj.getInt("pick");
             
             ElectionTopic electionTopic = new ElectionTopic(topic, pick);
             JsonArray optionsArray = electionTopicObj.getJsonArray("options");
             for(int i = 0; i < optionsArray.size(); i++){
                   electionTopic.addOption(optionsArray.getString(i));
             }
               
             
             election = new Election(electionId, title, voterlist, parameters, beginDate, endDate, electionTopic, appVersion, h_HatString, coefficientsString); 
             
         } catch (IOException x) {
             System.err.println(x);
         }
        return election;
    }

    /**
     * Posts the given ballot to the bulletin board
     * @param ballot
     * @param useTor Boolean indicating if Ballot should be posted over the Tor network
     * Ballot to be posted to the bulletin board
     */
    public void postBallot(Ballot ballot, Boolean useTor) {
        JsonObjectBuilder jBuilder = Json.createObjectBuilder();
        //Translate ballot object into a json string 
        JsonArrayBuilder optionsBuilder = Json.createArrayBuilder();
        for (String option : ballot.getSelectedOptions()) {
                optionsBuilder.add(option);
        }
        jBuilder.add("e", optionsBuilder);
        jBuilder.add("u_Hat", ballot.getU_HatString());
        jBuilder.add("c", ballot.getCString());
        jBuilder.add("d", ballot.getDString());
        jBuilder.add("pi1", ballot.getPi1String());
        jBuilder.add("pi2", ballot.getPi2String());
        jBuilder.add("pi3", ballot.getPi3String());
        
        JsonObject model = jBuilder.build();
        
        //josn gets posted to the bulletin board
        try { 
            boolean requestOK = postJsonStringToURL(bulletinBoardUrl +  "/elections/" + ballot.getElection().getId() + "/ballots", model.toString(), useTor);
            if (requestOK) {
                System.out.println("Ballot posted!");
            }
            else{
                System.out.println("Was not able to post Ballot!");
            }
        } catch (IOException ex) {
            System.out.println("Was not able to post Ballot!");
        }
    }

    /**
     * Gets a list of all the posted ballots of the given election form the bulletin board and returns it
     * @param election
     * Election for which the list of ballots should be retrieved 
     * @return 
     * the list of all posted ballots to the given election
     */
    public List<Ballot> getBallotsByElection(Election election) {
        List<Ballot> ballots = new ArrayList<Ballot>();
        try {

             URL url = new URL(bulletinBoardUrl + "/elections/" + election.getId() + "/ballots");
             
             InputStream urlInputStream = url.openStream();
             JsonReader jsonReader = Json.createReader(urlInputStream);
             JsonArray obj = jsonReader.readArray();
             DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
             //transforms the recieved json string into a list of ballot objects
             for (JsonObject result : obj.getValuesAs(JsonObject.class)) {

                int id = Integer.parseInt(result.getString("id")); 
                LocalDateTime timeStamp = LocalDateTime.parse(result.getString("timestamp"), format);
                
                JsonObject jsonData = result.getJsonObject("jsonData");
                List<String> selectedOptions = new ArrayList<String>();
                JsonArray optionsArray = jsonData.getJsonArray("e");
                for(int i = 0; i < optionsArray.size(); i++){
                   selectedOptions.add(optionsArray.getString(i));
                }
                String u_HatString = jsonData.getString("u_Hat");       
                String cString = jsonData.getString("c");
                String dString = jsonData.getString("d");
                String pi1String = jsonData.getString("pi1");
                String pi2String = jsonData.getString("pi2");
                String pi3String = jsonData.getString("pi3");
                //create ballot object and add it to the list
                Ballot ballot = new Ballot(id, election, selectedOptions,u_HatString, cString, dString, pi1String, pi2String, pi3String, timeStamp);
                System.out.println(ballot.isValid());
                ballots.add(ballot);
             }
             
             
         } catch (IOException x) {
             System.err.println(x);
         }
        return ballots;
        
        
    }

    /**
     * Post the given result to a election to the bulletin board
     * @param result 
     * result to be posted to the bulletin board
     */
    public void postResult(ElectionResult result) {
        Election election = result.getElection();
        ElectionTopic topic = election.getTopic();
        HashMap<String, Integer> optionResults = result.getOptionCount();
        //translate the ElectionResult object into a json String
        JsonObjectBuilder jBuilder = Json.createObjectBuilder();
        
        //TODO get email address from certificate 
        jBuilder.add("author", "alice@bfh.ch");
        
        JsonArrayBuilder resultBuilder = Json.createArrayBuilder();
        
        JsonObjectBuilder topicBuilder = Json.createObjectBuilder();
        
        topicBuilder.add("topic", topic.getTitle());
        topicBuilder.add("pick", topic.getPick());
        
        JsonArrayBuilder optionsBuilder = Json.createArrayBuilder();
        
        for (String option : topic.getOptions()){
           JsonObjectBuilder optionBuilder = Json.createObjectBuilder(); 
            optionBuilder.add("optTitle", option);
            optionBuilder.add("count", optionResults.get(option));
            optionsBuilder.add(optionBuilder);
        }
        topicBuilder.add("options", optionsBuilder);
        
        resultBuilder.add(topicBuilder);
        
        jBuilder.add("result", resultBuilder);
        
        JsonObject model = jBuilder.build();
        
        //sign json string 
        SignatureController signController = new SignatureController();
        JsonObject signedModel = null;
                
        try {
            signedModel = signController.signJson(model);  
        } catch (Exception ex) {
            Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try { 
            //post JWS with the result to the bulletin board
            boolean requestOK = postJsonStringToURL(bulletinBoardUrl +  "/elections/" + election.getId() + "/results", signedModel.toString(), false);
            if (requestOK) {
                System.out.println("Results posted!");
            }
            else{
                System.out.println("Was not able to post Results! Did not receive expected http 200 status.");
            }
        } catch (IOException ex) {
            System.out.println("Was not able to post Results! IOException");
        }

    }

    
       
}
