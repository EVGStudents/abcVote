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
import java.math.BigDecimal;
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
import org.apache.http.HttpResponse;
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
    
    public CommunicationController(String bulletinBoardUrl){
        this.bulletinBoardUrl = bulletinBoardUrl;
    }
    
    public Parameters getParameters() {
        try {
             //At the moment the method gets the json containing the voters from a rescource file until bulletin board is available
             
             URL url = new URL(bulletinBoardUrl + "/parameters");
             //URL url = getClass().getResource("/JSONFiles/parameters.json");
             
             InputStream urlInputStream = url.openStream();
             JsonReader jsonReader = Json.createReader(urlInputStream);
             JsonObject obj = jsonReader.readObject();
                
                String oString = obj.getString("o");
                String pString = obj.getString("p");
                String h0String = obj.getString("h0");
                String h1String = obj.getString("h1");
                String h2String = obj.getString("h2");
                String g0String = obj.getString("g0");
                String g1String = obj.getString("g1");
                   
                Parameters parameters = new Parameters(oString, pString, h0String, h1String, h2String, g0String, g1String);
                return parameters;
         } catch (Exception x) {
             System.err.println(x);
             return null;
         }
 
        
    }

    //gets the all registered voters from the bulletin board and returns them as al List of Voter objects
    public List<Voter> getAllARegisteredVoters() {
       List<Voter> voterlist = new ArrayList<Voter>(); 
        	
         try {
             //At the moment the method gets the json containing the voters from a rescource file until bulletin board is available
             
             URL url = new URL(bulletinBoardUrl + "/voters");
             //URL url = getClass().getResource("/JSONFiles/voters.json");
             
             InputStream urlInputStream = url.openStream();
             JsonReader jsonReader = Json.createReader(urlInputStream);
             JsonArray obj = jsonReader.readArray();
             for (JsonObject result : obj.getValuesAs(JsonObject.class)) {
                   
                String email = result.getString("email");       
                String signature = result.getString("signature");
                String publicCredential = result.getString("publicCredential");
                String appVersion = result.getString("appVersion");
                   
                Voter voter = new Voter(email, signature, publicCredential, appVersion);
                voterlist.add(voter);
             }
             
         } catch (IOException x) {
             System.err.println(x);
         }
       return voterlist;
    }

    public void postElection(Election election){
        JsonObjectBuilder jBuilder = Json.createObjectBuilder();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        //TODO get email address from certificate 
        jBuilder.add("author", "alice@bfh.ch");
        jBuilder.add("electionTitle", election.getTitle());
        jBuilder.add("beginDate", election.getStartDate().format(format));
        jBuilder.add("endDate", election.getEndDate().format(format));
        //toDo: get AppVersion dynamically from build 
        jBuilder.add("appVersion", "0.15");
       
        
        jBuilder.add("coefficients", election.getCredentialPolynomialString());

        //toDo: get election generator Dynamically from election object
        jBuilder.add("electionGenerator", election.getH_HatString());
        
        JsonObjectBuilder votingTopicBuilder = Json.createObjectBuilder();
        votingTopicBuilder.add("topic", election.getTopic().getTitle());
        votingTopicBuilder.add("pick", election.getTopic().getPick());
        
        JsonArrayBuilder optionsBuilder = Json.createArrayBuilder();
        for (String option : election.getTopic().getOptions()) {
                optionsBuilder.add(option);
        }
        votingTopicBuilder.add("options", optionsBuilder);
        
        jBuilder.add("votingTopic", votingTopicBuilder);
        
        JsonArrayBuilder votersBuilder = Json.createArrayBuilder();
        
        for (Voter voter : election.getVoterList()){
            JsonObjectBuilder voterBuilder = Json.createObjectBuilder();
            voterBuilder.add("email", voter.getEmail());
            voterBuilder.add("signature", voter.getSignature());
            voterBuilder.add("publicCredential", voter.getPublicCredential());
            voterBuilder.add("appVersion", voter.getAppVersion());
            
            votersBuilder.add(voterBuilder);
        }
        
        jBuilder.add("voters", votersBuilder);
        
        JsonObject model = jBuilder.build();
        
        SignatureController signController = new SignatureController();
        JsonObject signedModel = null;
                
        try {
            signedModel = signController.signJson(model);   
        } catch (Exception ex) {
            Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try { 
            boolean requestOK = postJsonStringToURL(bulletinBoardUrl +  "/elections", signedModel.toString());
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
    public boolean postJsonStringToURL(String url, String json) throws IOException{
        boolean responseOK = true;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(json);
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            
            HttpResponse  response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != 200){
                responseOK = false;
            }
            
        } catch (Exception ex) {
            responseOK = false;
        } finally {
            httpClient.close();

        } 
        return responseOK;
    }    

    public boolean registerNewVoter(String email, Parameters parameters, Element u) throws JoseException, Exception{
        
        //create Voter json
        JsonObjectBuilder jBuilder = Json.createObjectBuilder();
        
        jBuilder.add("email", email);
        //TODO remove standInStignatur
        jBuilder.add("signature", "standInSignatur");
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
            boolean requestOK = postJsonStringToURL(bulletinBoardUrl +  "/voters", signedModel.toString());
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

    public List<ElectionHeader> getElectionHeaders(ElectionFilterTyp filter) {
        List<ElectionHeader> electionHeaderlist = new ArrayList<ElectionHeader>(); 
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime actualDateTime = LocalDateTime.now();
        String dateTimeString = actualDateTime.getYear() + "-" 
                + actualDateTime.getMonthValue() + "-"
                + actualDateTime.getDayOfMonth() + " "
                + actualDateTime.getHour() + ":"
                + actualDateTime.getMinute() + ":"
                + actualDateTime.getSecond();
        
        URL url = null;
        
        switch (filter) {
            
            case ALL: 
                {
                    try {
                        url = new URL(bulletinBoardUrl + "/elections");
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }  
                break; 
             
            case OPEN:
                {
                    try {
                        url = new URL(bulletinBoardUrl + "/elections/open?date=" + URLEncoder.encode(dateTimeString, "UTF-8").replace("+", "%20"));
                    } catch (UnsupportedEncodingException | MalformedURLException ex) {
                        System.err.println(ex);
                    }
                }
                break; 
                
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

    public Election getElectionById(int electionId) {
        Election election = null;
        try {

             URL url = new URL(bulletinBoardUrl + "/elections/" + electionId);
             
             InputStream urlInputStream = url.openStream();
             JsonReader jsonReader = Json.createReader(urlInputStream);
             JsonObject obj = jsonReader.readObject();
             DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
             Parameters parameters = this.getParameters();
             
             //get Election Data       
             String title = obj.getString("electionTitle");
             LocalDateTime beginDate = LocalDateTime.parse(obj.getString("beginDate"), format);
             LocalDateTime endDate = LocalDateTime.parse(obj.getString("endDate"), format);
             String appVersion = obj.getString("appVersion");
             String coefficientsString = obj.getString("coefficients");
             String h_HatString = obj.getString("electionGenerator");
             List<Voter> voterlist = new ArrayList<Voter>(); 
             //get voterlist
             for (JsonObject result : obj.getJsonArray("voters").getValuesAs(JsonObject.class)) {
                   
                String voterEmail = result.getString("email");       
                String voterSignature = result.getString("signature");
                String voterPublicCredential = result.getString("publicCredential");
                String voterAppVersion = result.getString("appVersion");
                   
                Voter voter = new Voter(voterEmail, voterSignature, voterPublicCredential, voterAppVersion);
                voterlist.add(voter);
             }
             //get votingTopic
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

    public void postBallot(Ballot ballot) {
        JsonObjectBuilder jBuilder = Json.createObjectBuilder();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
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
        

        try { 
            boolean requestOK = postJsonStringToURL(bulletinBoardUrl +  "/elections/" + ballot.getElection().getId() + "/ballots", model.toString());
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

    public List<Ballot> getBallotsByElection(Election election) {
        List<Ballot> ballots = new ArrayList<Ballot>();
        try {

             URL url = new URL(bulletinBoardUrl + "/elections/" + election.getId() + "/ballots");
             
             InputStream urlInputStream = url.openStream();
             JsonReader jsonReader = Json.createReader(urlInputStream);
             JsonArray obj = jsonReader.readArray();
             DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
             
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
 
                
                
                Ballot ballot = new Ballot(id, election, selectedOptions,u_HatString, cString, dString, pi1String, pi2String, pi3String, timeStamp);
                System.out.println(ballot.isValid());
                ballots.add(ballot);
             }
             
             
         } catch (IOException x) {
             System.err.println(x);
         }
        return ballots;
        
        
    }

    public void postResult(ElectionResult result) {
        Election election = result.getElection();
        ElectionTopic topic = election.getTopic();
        HashMap<String, Integer> optionResults = result.getOptionCount();
        JsonObjectBuilder jBuilder = Json.createObjectBuilder();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
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
        
        SignatureController signController = new SignatureController();
        JsonObject signedModel = null;
                
        try {
            signedModel = signController.signJson(model);   
        } catch (Exception ex) {
            Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try { 
            boolean requestOK = postJsonStringToURL(bulletinBoardUrl +  "/elections/" + election.getId() + "/results", signedModel.toString());
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
