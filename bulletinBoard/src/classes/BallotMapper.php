<?php
class BallotMapper extends Mapper
{
    public function getBallots() {
      $sql = "SELECT id, electionIdentifier, jsonData, timestamp
        from tbl_ballots";
      $stmt = $this->db->query($sql);
      $results = [];
      while($row = $stmt->fetch()) {
        $results[] = new BallotEntity($row);
      }
      return $results;
    }

    public function storeBallot(BallotEntity $ballot) {
      $stmt = $this->db->prepare("INSERT INTO tbl_ballots (electionIdentifier, jsonData)
        VALUES (:electionIdentifier, :jsonData)");

      $stmt->bindParam(':electionIdentifier', $electionIdentifier);
      $stmt->bindParam(':jsonData', $jsonData);

      $electionIdentifier = $ballot->getElectionIdentifier();
      $jsonData = $ballot->getJsonData();

      return $stmt->execute();
    }
}
