<?php

/**
 *
 * abcVote (https://github.com/EVGStudents/abcVote)
 * Bulletin-Board for abcVote's e-voting applications
 *
 * VoterEntity.php: an object representing a voter
 *
 * @author Sebastian Nellen <sebastian at nellen.it>
 */

class VoterEntity
{
    protected $id;
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
        $this->jsonData = $data['jsonData'];
    }

    // GETters

    /**
    * method returning this voter's id
    */
    public function getId() {
        return $this->id;
    }
    /**
    * method returning this voter's json data
    */
    public function getJsonData() {
        return $this->jsonData;
    }

    // SETters

    /**
    * method for settings this voter's json data
    */
    public function setJsonData($jsonData) {
        $this->jsonData = $jsonData;
    }
}
