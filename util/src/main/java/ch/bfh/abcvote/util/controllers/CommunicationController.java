/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.util.controllers;

import ch.bfh.abcvote.util.model.ElectionFilterTyp;
import ch.bfh.abcvote.util.model.ElectionHeader;
import ch.bfh.abcvote.util.model.Parameters;
import ch.bfh.abcvote.util.model.PrivateCredentials;
import ch.bfh.abcvote.util.model.Vote;
import ch.bfh.abcvote.util.model.VoteTopic;
import ch.bfh.abcvote.util.model.Voter;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
                   
                String pString = obj.getString("p");       
                String qString = obj.getString("q");
                String h1String = obj.getString("h1");
                String h2String = obj.getString("h2");

                   
                Parameters voteInfo = new Parameters(pString, qString, h1String, h2String);
                return voteInfo;
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

    public void postVote(Vote vote){
        JsonObjectBuilder jBuilder = Json.createObjectBuilder();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        jBuilder.add("electionTitle", vote.getTitle());
        jBuilder.add("beginDate", vote.getStartDate().format(format));
        jBuilder.add("endDate", vote.getEndDate().format(format));
        //toDo: get AppVersion dynamically from build 
        jBuilder.add("appVersion", "0.15");
       
        
        jBuilder.add("coefficients", vote.getCredentialPolynomialString());

        //toDo: get vote generator Dynamically from vote object
        jBuilder.add("electionGenerator", vote.getH_HatString());
        
        JsonObjectBuilder votingTopicBuilder = Json.createObjectBuilder();
        votingTopicBuilder.add("topic", vote.getTopic().getTitle());
        votingTopicBuilder.add("pick", vote.getTopic().getPick());
        
        JsonArrayBuilder optionsBuilder = Json.createArrayBuilder();
        for (String option : vote.getTopic().getOptions()) {
                optionsBuilder.add(option);
        }
        votingTopicBuilder.add("options", optionsBuilder);
        
        jBuilder.add("votingTopic", votingTopicBuilder);
        
        JsonArrayBuilder votersBuilder = Json.createArrayBuilder();
        
        for (Voter voter : vote.getVoterList()){
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
                System.out.println("Vote posted!");
            }
            else{
                System.out.println("Was not able to post Vote!");
            }
        } catch (IOException ex) {
            System.out.println("Was not able to post Vote!");
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

    public void registerNewVoter(String email) {
        //get Parameters
        Parameters parameters = getParameters();
        
        //pick private Credentials
        PrivateCredentials privateCredentials = new PrivateCredentials(parameters);
        //calculate public credentials
        Element u = privateCredentials.getU();
        //Store private Credentials
        storePrivateCredentials(privateCredentials);
        //create Voter json
        JsonObjectBuilder jBuilder = Json.createObjectBuilder();
        
        jBuilder.add("email", email);
        jBuilder.add("signature", "standInSignatur");
        jBuilder.add("publicCredential", u.convertToString());
        jBuilder.add("appVersion", "1.15");
        
        JsonObject model = jBuilder.build();
                
        //post Voter
        try { 
            boolean requestOK = postJsonStringToURL(bulletinBoardUrl +  "/voters", model.toString());
            if (requestOK) {
                System.out.println("Voter posted!");
            }
            else{
                System.out.println("Was not able to post Voter!");
            }
        } catch (IOException ex) {
            System.out.println("Was not able to post Voter!");
        }
        
    }

    private void storePrivateCredentials(PrivateCredentials privateCredentials) {

            //ToDo might need to be moved once Credentials are stored diffrentliy
            JsonObjectBuilder jBuilder = Json.createObjectBuilder();
            jBuilder.add("alpha", privateCredentials.getAlpha().convertToString());
            jBuilder.add("beta", privateCredentials.getBeta().convertToString());
            JsonObject credentialsJson = jBuilder.build();
            String path = System.getProperty("user.home") + File.separator + "Documents";
            path += File.separator + "abcVote";
            File josnDir = new File(path);
            if (josnDir.exists()|| josnDir.mkdirs()) {
                
                FileWriter file = null;
                try {
                    file = new FileWriter(path + File.separator +  "privateCredentials.json");
                    file.write(credentialsJson.toString());
                    file.close();
                    System.out.println("File created");
                } catch (IOException ex) {
                    System.out.println("File not created");
                } finally{
                    
                }
                
                
            } else {
                System.out.println("Directory not created");
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

    public Vote getElectionById(int electionId) {
        Vote vote = null;
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
             JsonObject voteTopicObj = obj.getJsonObject("votingTopic");
             
             String topic = voteTopicObj.getString("topic");
             int pick = voteTopicObj.getInt("pick");
             
             VoteTopic voteTopic = new VoteTopic(topic, pick);
             JsonArray optionsArray = voteTopicObj.getJsonArray("options");
             for(int i = 0; i < optionsArray.size(); i++){
                   voteTopic.addOption(optionsArray.getString(i));
             }
               
             
             vote = new Vote(electionId, title, voterlist, parameters, beginDate, endDate, voteTopic, appVersion, h_HatString, coefficientsString); 
             
         } catch (IOException x) {
             System.err.println(x);
         }
        return vote;
    }
    
       
    
}
