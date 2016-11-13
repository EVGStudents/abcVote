<?php
class ElectionMapper extends Mapper
{
    public function getElections() {
        $sql = "SELECT id, jsonData, electionTitle, beginDate, endDate, coefficients, appVersion
            from tblElections";
        $stmt = $this->db->query($sql);
        $results = [];
        while($row = $stmt->fetch()) {
            $results[] = new ElectionEntity($row);
        }
        return $results;
    }

    public function storeElection(ElectionEntity $election) {
      $stmt = $this->db->prepare("INSERT INTO tblElections
        (jsonData, electionTitle, beginDate, endDate, coefficients, appVersion)
        VALUES (:jsonData, :electionTitle, :beginDate, :endDate, :coefficients, :appVersion)");

      $stmt->bindParam(':jsonData', $jsonData);
      $stmt->bindParam(':electionTitle', $electionTitle);
      $stmt->bindParam(':beginDate', $beginDate);
      $stmt->bindParam(':endDate', $endDate);
      $stmt->bindParam(':coefficients', $coefficients);
      $stmt->bindParam(':appVersion', $appVersion);

      $jsonData = $election->getJsonData();
      $electionTitle = $election->getElectionTitle();
      $beginDate = $election->getBeginDate();
      $endDate = $election->getEndDate();
      $coefficients = $election->getCoefficients();
      $appVersion = $election->getAppVersion();

      return $stmt->execute();
    }
}
