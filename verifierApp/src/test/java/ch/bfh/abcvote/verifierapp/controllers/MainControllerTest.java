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
import ch.bfh.abcvote.util.model.ElectionTopic;
import ch.bfh.abcvote.util.model.Parameters;
import ch.bfh.abcvote.util.model.PrivateCredentials;
import ch.bfh.abcvote.util.model.Voter;
import ch.bfh.unicrypt.UniCryptException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author t.buerk
 */
public class MainControllerTest {
    
    public MainControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of calculateElectionResult method, of class MainController.
     */
    @Test
    public void testCalculateElectionResult() throws UniCryptException {
        System.out.println("calculateElectionResult");
        Parameters params = new Parameters("40448000884072788995622599165144363221155726031031478997903583665043809745595304992166786737342137113028748765427233355214255107412303945920180372449259813818667549261471482552671511271314283862875381595353822127863166765975992405783762216944489355258403762145541226521248038609141424643757184145777620358939789",
                "124839508901459225295131478904766553151715203799479873450319702669888301683936126519033292399204126892064039399466769614858812059914518351605494976695246338946504781671208279483554047133686061305170930849857475703281378907333309894394327830075584429809888154770188970744592711756609335320238222672153149255987",
                "18434172550262004524867380404285237923351701000947219674488129438625992053333138607032081687751194164605396231231083738331674830461446678479358725278661019796398797016480199779050696145705601716432860566644098005599721421382988033685879219167545263376405365319220030792167591294706066196067652617135892075384",
                "101514461084882367868595044272346737081022178014756069512901796371740146181727964093914176019073829580724985074919953772441540697397738520366711879378337772829887871049649974488394526354738167886082205713216717277976463707828161307520345715622162742391586004193235363261344344663132579615685997623813833536319",
                "109855503059451287835960726017264937935599654898101341364122275113508717959973321365663670615783945865395991349794005943789935272847066080186095420188759930736247402645730629928151869553562610235581680229345973351942031809567053197480552021377745768990472078587678504913384953890156789247781215819787059178296",
                "35826684109134534840392927893619617758058820923000592544271785947661664316826625293992698641693134443974967268704554818416691715134711577526265565956272928516247151382908196221368412102401167491048133130772814706454141689407232402999346252682352990096358852633005485454182536398874395243274287280094515670267528",
                "27732285431348660064870501057482694354826999156820928918968720423459323749344328447245750445230768289006246223633953239715722198090019548218580271935847981318753840131730132507078668295754159721417558389062310191474324697652927278936854657919602375114912924733973154334014687839904090230452175177698207733661063");
        LocalDateTime startDate = LocalDateTime.of(LocalDate.of(2016, 12, 6), LocalTime.parse("00:00:00"));
        LocalDateTime endDate = LocalDateTime.of(LocalDate.of(2016, 12, 24), LocalTime.parse("00:00:00"));
        List<Voter> voters = new ArrayList<Voter>();
        voters.add(new Voter("hector@bfh.ch","94956178162896520723239362371073940305674247435120044260320086560276183967368593002006586863853181720996647706342256473558202406072892529886111606074334013316878156782640293460686748348380081299625124535896789214117108395497724126075350180043371970512165030240341853422559117114538604391717049413860173528478", "1.15"));
        voters.add(new Voter("alice@bfh.ch","20675110888229975134537834262132428922107086374998449626153299194267801123774252949886446560645837323382925642338531169189172850547192678425943628272858081838679270943657517761598991043160399596186367379520096810174456607315917528528739226812909050942423504669796547776683297734229690101568360513250756518960", "1.15"));
        ElectionTopic topic = new ElectionTopic("JUnit topic", 1);
        topic.addOption("A");
        topic.addOption("B");
        Election election = new Election(42,"JUnit Test", voters, params,startDate,endDate,topic, "0.15",
                "88192098242801927528387979699174276950963233720677051803010025682601663921584349962929257528279847011241262761662437252315783890628584160046236644873155168823975593454967339725273440588203364365007073171711702580867541448274814543510872615725089006242632827789323923195807020805808544440927511816307963021495",
                "51689869742014201751155130216834808964983799790292422622351088279378203492848047320809915145355025871329186770900267255920713182606679146887800163309348557699101324731909999054019166870635061525544417209699124860257065464235976116147548739327725846822931266794201096362728150573853461155256612352244486266638010983662263335622105402678577220851373155778812053461907761935020876250590837517035630517790015232054134462486617817511339798978262592445714764792552910242532534646487052360631104730646268036523268036407752110735808188357903469828016174124290844803682372302436942281722838651287335605035956917488355391144469263137201120104288189929854114095846506993078726670183503163092592065744397040809246621748890083912016914834313003964162054361446206480945692487457692843900269599877837795415100926511078901849618022573760903044574835843395159669431918164983739332948741152213198695068301603741146318773515153283455180023226648781280314341611760542751581564481246531951317843310590380219013656808777609006813827640359219942003890334420451044218340430271065127177111020056118352196058115407091032203855051425831906327225206357569554421537154390203437649029899706947519042092006695984258266003122315726981327586087845457244348469107165846401411169776757911940191107589130623829455336452302823086425035464980419749439992899701850961700445569780720161728381102979162564202578357881889923817642566107826541492410778216248446101738909267895180778423063753717334678254257735177881363250292083714211863384290048710933108322000158232334416483001933192327190357049406255580378407759447693910445767411145108343476784680164084532919937959365404275846597284202328103114953531502115706512688145965588262507773782398027241463402589749199449804306643913745758107079521196954603589420009787834744467715634598300749561755996626778372117256178059495588358329096060899171603586695223525304693495588850434733218838247455852803983351587396912800196301011641673562314476671902030809260834470492312306417744136465977888605715592875144857207806365782671708446491400866986085894605184740592224832396791064166774919753620403884117163716626604658578145629779926711833391665220927493272331949179607974427622102475388905816076424862234018955267689033332801436743399803539712537655945038835619242111057446053522879612957466790784295685771632496828688394380276537136041347612122718543354634743516744349807560037341913091085222884804676580306245299559822350757632922966485643133773929543559");
        List<String> selectedOptions = new ArrayList<String>();
        selectedOptions.add("A");
        
        List<String> altSelectedOptions = new ArrayList<String>();
        altSelectedOptions.add("B");
        
        PrivateCredentials privatCredentials = new PrivateCredentials(params, "48050196416718903648183476050020155770582061670420669938200665538666243805818428274460819364985466203728439951213905411939966829716782529150549725850674838118767698892189045460316045480291722110898996953524430735291702644355645879211394330093607391095888925677352310611033155236929441202136812492896383836096",
                "54795148641104864012232735068384387265703071489067240518864603692375303421858837231983128931981008595096100930107860831938339394053253647568790097693492090298457677072345937619145005201920399110711732665162317237196954381171170941204579905913000476296200637573953143288630499930917162792498932434919224397738");      
        Ballot ballotBlueprint = new Ballot(election, selectedOptions);
        ballotBlueprint.calculateProves(privatCredentials);
        
        Ballot altBallot = new Ballot(election, selectedOptions);
        altBallot.calculateProves(privatCredentials);
        
        
        String u_HatString = ballotBlueprint.getU_HatString();
        String cString = ballotBlueprint.getCString();
        String dString = ballotBlueprint.getDString();
        String pi1String = ballotBlueprint.getPi1String();
        String pi2String = ballotBlueprint.getPi2String();
        String pi3String = ballotBlueprint.getPi3String();
        
        List<Ballot> ballots = new ArrayList<Ballot>();
        
        Ballot validBallot = new Ballot(1,election, selectedOptions, u_HatString,cString, dString, pi1String, pi2String, pi3String,LocalDateTime.of(LocalDate.of(2016, 12, 22), LocalTime.parse("00:00:00")));
        Ballot lateBallot = new Ballot(2,election, selectedOptions, u_HatString,cString, dString, pi1String, pi2String, pi3String,LocalDateTime.of(LocalDate.of(2016, 12, 25), LocalTime.parse("00:00:00")));
        Ballot changedVoteBallot = new Ballot(3,election, altSelectedOptions, u_HatString,cString, dString, pi1String, pi2String, pi3String,LocalDateTime.of(LocalDate.of(2016, 12, 23), LocalTime.parse("00:00:00")));
        Ballot changedPi1Ballot = new Ballot(4,election, selectedOptions, u_HatString,cString, dString, altBallot.getPi1String(), pi2String, pi3String,LocalDateTime.of(LocalDate.of(2016, 12, 23), LocalTime.parse("00:00:00")));
        Ballot changedPi2Ballot = new Ballot(5,election, selectedOptions, u_HatString,cString, dString, pi1String, altBallot.getPi2String(), pi3String,LocalDateTime.of(LocalDate.of(2016, 12, 23), LocalTime.parse("00:00:00")));
        Ballot changedPi3Ballot = new Ballot(6,election, selectedOptions, u_HatString,cString, dString, pi1String, pi2String, altBallot.getPi3String(),LocalDateTime.of(LocalDate.of(2016, 12, 23), LocalTime.parse("00:00:00")));
        Ballot duplicatBallot = new Ballot(7,election, selectedOptions, u_HatString,cString, dString, pi1String, pi2String, pi3String,LocalDateTime.of(LocalDate.of(2016, 12, 23), LocalTime.parse("00:00:00")));
        
        
        ballots.add(validBallot);
        ballots.add(lateBallot);
        ballots.add(changedVoteBallot);
        ballots.add(changedPi1Ballot);
        ballots.add(changedPi2Ballot);
        ballots.add(changedPi3Ballot);
        ballots.add(duplicatBallot);
        
        MainController mainController = new MainController();
        
        ElectionResult result = mainController.calculateElectionResult(election, ballots);
        
        
        for(Ballot checkBallot : result.getBallots()){
            
            switch (checkBallot.getId()){
                case 1 :
                    System.out.println("checking correct ballot validation");
                    assertTrue(checkBallot.isValid());
                    break;
                case 2:
                    System.out.println("checking late ballot validation");
                    assertFalse(checkBallot.isValid());
                    assertEquals("not in voting period", checkBallot.getReason());
                    break;
                case 3:
                    System.out.println("checking ballot with changed vote validation");
                    assertFalse(checkBallot.isValid());
                    assertEquals("PI1-Proof failed", checkBallot.getReason());
                    break;
                case 4:
                    System.out.println("checking ballot with tamperd Pi1 validation");
                    assertFalse(checkBallot.isValid());
                    assertEquals("PI1-Proof failed", checkBallot.getReason());
                    break;
                case 5:
                    System.out.println("checking ballot with tamperd Pi2 validation");
                    assertFalse(checkBallot.isValid());
                    assertEquals("PI2-Proof failed", checkBallot.getReason());
                    break;
                case 6:
                    System.out.println("checking ballot with tamperd Pi3 validation");
                    assertFalse(checkBallot.isValid());
                    assertEquals("PI3-Proof failed", checkBallot.getReason());
                    break;
                case 7:
                    System.out.println("checking duplicate ballot validation");
                    assertFalse(checkBallot.isValid());
                    assertEquals("Already selected another vote of the same voter", checkBallot.getReason());
                    break;
            }
        }
    }
    
}
