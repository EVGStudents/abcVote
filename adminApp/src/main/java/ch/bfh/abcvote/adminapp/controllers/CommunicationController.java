/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.controllers;

import ch.bfh.abcvote.util.model.Parameters;
import ch.bfh.abcvote.util.model.Vote;
import ch.bfh.abcvote.util.model.Voter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    
    Parameters getParameters() {
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
    List<Voter> getAllARegisteredVoters() {
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

    void postVote(Vote vote){
        JsonObjectBuilder jBuilder = Json.createObjectBuilder();
        
        jBuilder.add("electionTitle", vote.getTitle());
        jBuilder.add("beginDate", vote.getStartDate().toString());
        jBuilder.add("endDate", vote.getEndDate().toString());
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
        
        try { 
            boolean requestOK = postJsonStringToURL(bulletinBoardUrl +  "/elections", model.toString());
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
    
}
