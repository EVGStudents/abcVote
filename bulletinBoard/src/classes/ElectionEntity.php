<?php

/**
 *
 * abcVote (https://github.com/EVGStudents/abcVote)
 * Bulletin-Board for abcVote's e-voting applications
 *
 * BallotEntity.php: an object representing a ballot
 *
 * @author Sebastian Nellen <sebastian at nellen.it>
 */

class ElectionEntity
{
    protected $id;
    protected $jsonData;
    protected $electionTitle;
    protected $beginDate;
    protected $endDate;
    protected $coefficients;
    protected $appVersion;
    /**
     * Accept an array of data matching properties of this class
     * and create the class
     *
     * @param array $data The data to use to create
     */
    public function __construct(array $data) {
        // no id if we're creating as it comes from the database
        if(isset($data['id'])) {
            $this->id = $data['id'];
        }
        $this->jsonData = $data['jsonData'];
        $this->electionTitle = $data['electionTitle'];
        $this->beginDate = $data['beginDate'];
        $this->endDate = $data['endDate'];
        $this->coefficients = $data['coefficients'];
        $this->appVersion = $data['appVersion'];
    }

    // GETters

    /**
    * method returning this election's id (identifier)
    */
    public function getId() {
        return $this->id;
    }
    /**
    * method returning this election's json data
    */
    public function getJsonData() {
        return $this->jsonData;
    }
    /**
    * method returning this election's title
    */
    public function getElectionTitle() {
        return $this->electionTitle;
    }
    /**
    * method returning this election's begin date
    */
    public function getBeginDate() {
        return $this->beginDate;
    }
    /**
    * method returning this election's end date
    */
    public function getEndDate() {
        return $this->endDate;
    }
    /**
    * method returning this election's coefficients
    */
    public function getCoefficients() {
        return $this->coefficients;
    }
    /**
    * method returning the app version to which this election is associated
    */
    public function getAppVersion() {
        return $this->appVersion;
    }

    // SETters

    /**
    * method for settings this election's json data
    */
    public function setJsonData($jsonData) {
        $this->jsonData = $jsonData;
    }
    /**
    * method for settings this election's title
    */
    public function setElectionTitle($electionTitle) {
        $this->electionTitle = $electionTitle;
    }
    /**
    * method for settings this election's begin date
    */
    public function setBeginDate($beginDate) {
        $this->beginDate = $beginDate;
    }
    /**
    * method for settings this election's end date
    */
    public function setEndDate($endDate) {
        $this->endDate = $endDate;
    }
    /**
    * method for settings this election's coefficients
    */
    public function setCoefficients($coefficients) {
        $this->coefficients = $coefficients;
    }
    /**
    * method for settings the app version to which this election is associated
    */
    public function setAppVersion($appVersion) {
        $this->appVersion = $appVersion;
    }
}
