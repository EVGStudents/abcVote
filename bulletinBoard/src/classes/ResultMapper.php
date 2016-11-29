<?php
class ResultMapper extends Mapper
{
    public function getResults() {
      $sql = "SELECT id, electionIdentifier, jsonData
        from tbl_results";
      $stmt = $this->db->query($sql);
      $results = [];
      while($row = $stmt->fetch()) {
        $results[] = new ResultEntity($row);
      }
      return $results;
    }

    public function storeResult(ResultEntity $resultEntity) {
      $stmt = $this->db->prepare("INSERT INTO tbl_results (electionIdentifier, jsonData)
        VALUES (:electionIdentifier, :jsonData)");

      $stmt->bindParam(':electionIdentifier', $electionIdentifier);
      $stmt->bindParam(':jsonData', $jsonData);

      $electionIdentifier = $resultEntity->getElectionIdentifier();
      $jsonData = $resultEntity->getJsonData();

      return $stmt->execute();
    }
}
