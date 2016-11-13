<?php
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
        // no id if we're creating
        if(isset($data['id'])) {
            $this->id = $data['id'];
        }
        $this->jsonData = $data['jsonData'];
    }

    // GETters
    public function getId() {
        return $this->id;
    }
    public function getJsonData() {
        return $this->jsonData;
    }

    // SETters
    public function setJsonData($jsonData) {
        $this->jsonData = $jsonData;
    }
}
