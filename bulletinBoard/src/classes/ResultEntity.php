<?php
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
      // no id if we're creating
      if(isset($data['id'])) {
        $this->id = $data['id'];
      }
      $this->electionIdentifier = $data['electionIdentifier'];
      $this->jsonData = $data['jsonData'];
    }

    // GETters
    public function getId() {
        return $this->id;
    }
    public function getElectionIdentifier() {
        return $this->electionIdentifier;
    }
    public function getJsonData() {
        return $this->jsonData;
    }

    // SETters
    public function setElectionIdentifier($electionIdentifier) {
        $this->electionIdentifier = $electionIdentifier;
    }
    public function setJsonData($jsonData) {
        $this->jsonData = $jsonData;
    }
}
