<?php
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
        // no id if we're creating
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
    public function getId() {
        return $this->id;
    }
    public function getJsonData() {
        return $this->jsonData;
    }
    public function getElectionTitle() {
        return $this->electionTitle;
    }
    public function getBeginDate() {
        return $this->beginDate;
    }
    public function getEndDate() {
        return $this->endDate;
    }
    public function getCoefficients() {
        return $this->coefficients;
    }
    public function getAppVersion() {
        return $this->appVersion;
    }

    // SETters
    public function setJsonData($jsonData) {
        $this->jsonData = $jsonData;
    }
    public function setElectionTitle($electionTitle) {
        $this->electionTitle = $electionTitle;
    }
    public function setBeginDate($beginDate) {
        $this->beginDate = $beginDate;
    }
    public function setEndDate($endDate) {
        $this->endDate = $endDate;
    }
    public function setCoefficients($coefficients) {
        $this->coefficients = $coefficients;
    }
    public function setAppVersion($appVersion) {
        $this->appVersion = $appVersion;
    }
}
