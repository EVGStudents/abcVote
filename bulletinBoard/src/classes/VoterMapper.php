<?php
class VoterMapper extends Mapper
{
    public function getVoters() {
      $sql = "SELECT id, jsonData
        from tblVoters";
      $stmt = $this->db->query($sql);
      $results = [];
      while($row = $stmt->fetch()) {
        $results[] = new VoterEntity($row);
      }
      return $results;
    }

    public function storeVoter(VoterEntity $voter) {
      $stmt = $this->db->prepare("INSERT INTO tblVoters (jsonData)
        VALUES (:jsonData)");

      $stmt->bindParam(':jsonData', $jsonData);

      $jsonData = $voter->getJsonData();

      return $stmt->execute();
    }
}
