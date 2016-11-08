/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.abcvote.adminapp.Controllers;

import ch.bfh.abcvote.adminapp.model.VoteInfo;
import ch.bfh.abcvote.adminapp.model.Voter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Class that is in charge of communicating with the Bulletin Board.
 * It sends requests to the Bulletin Board and converts the recieved JSON Data into Java Objects and returns them.
 * Or converts java objects into JSON Objects and posts them to the Bulletin Board
 * @author t.buerk
 */
public class CommunicationController {

    
    VoteInfo getVoteInformation() {
        
        VoteInfo voteInfo = new VoteInfo();
        return voteInfo;
    }

    //gets the all registered voters from the bulletin board and returns them as al List of Voter objects
    List<Voter> getAllARegisteredVoters() {
       List<Voter> voterlist = new ArrayList<Voter>(); 
        	
         try {
             //At the moment the method gets the json containing the voters from a rescource file until bulletin board is available
             
             //URL url = new URL("https://graph.facebook.com/search?q=java&type=post");
             URL url = getClass().getResource("/JSONFiles/voters.json");
             
             InputStream urlInputStream = url.openStream();
             JsonReader jsonReader = Json.createReader(urlInputStream);
             JsonArray obj = jsonReader.readArray();
             for (JsonObject result : obj.getValuesAs(JsonObject.class)) {
                   
                String givenName = result.getString("givenName");       
                String middleName = result.getString("middleName");
                String surname = result.getString("surname");
                String location = result.getString("location");
                String signature = result.getString("signature");
                String publicCredential = result.getString("publicCredential");
                String appVersion = result.getString("appVersion");
                   
                Voter voter = new Voter(givenName, middleName, surname, location, signature, publicCredential, appVersion);
                voterlist.add(voter);
             }
             
         } catch (IOException x) {
             System.err.println(x);
         }
       return voterlist;
    }
    
}
