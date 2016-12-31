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

class BallotEntity
{
    protected $id;
    protected $electionIdentifier;
    protected $jsonData;
    protected $timestamp;
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
        $this->electionIdentifier = $data['electionIdentifier'];
        $this->jsonData = $data['jsonData'];
        $this->timestamp = $data['ballotTimestamp'];
    }

    // GETters

    /**
    * method returning this ballot's id
    */
    public function getId() {
        return $this->id;
    }
    /**
    * method returning the election identifier to which this ballot is associated
    */
    public function getElectionIdentifier() {
        return $this->electionIdentifier;
    }
    /**
    * method returning this ballot's json data
    */
    public function getJsonData() {
        return $this->jsonData;
    }
    /**
    * method returning this ballot's timestamp
    */
    public function getTimestamp() {
        return $this->timestamp;
    }

    // SETters

    /**
    * method for setting the election identifier to which this ballot is associated
    */
    public function setElectionIdentifier($electionIdentifier) {
        $this->electionIdentifier = $electionIdentifier;
    }
    /**
    * method for settings this ballot's json data
    */
    public function setJsonData($jsonData) {
        $this->jsonData = $jsonData;
    }
}
