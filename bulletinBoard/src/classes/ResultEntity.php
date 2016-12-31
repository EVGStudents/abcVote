<?php

/**
 *
 * abcVote (https://github.com/EVGStudents/abcVote)
 * Bulletin-Board for abcVote's e-voting applications
 *
 * ResultEntity.php: an object representing an election's result
 *
 * @author Sebastian Nellen <sebastian at nellen.it>
 */

class ResultEntity
{
    protected $id;
    protected $electionIdentifier;
    protected $jsonData;
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
    }

    // GETters

    /**
    * method returning this result's id
    */
    public function getId() {
        return $this->id;
    }
    /**
    * method returning the election identifier to which this result is associated
    */
    public function getElectionIdentifier() {
        return $this->electionIdentifier;
    }
    /**
    * method returning this result's json data
    */
    public function getJsonData() {
        return $this->jsonData;
    }

    // SETters

    /**
    * method for settings the election identifier to which this result is associated
    */
    public function setElectionIdentifier($electionIdentifier) {
        $this->electionIdentifier = $electionIdentifier;
    }
    /**
    * method for settings this result's json data
    */
    public function setJsonData($jsonData) {
        $this->jsonData = $jsonData;
    }
}
