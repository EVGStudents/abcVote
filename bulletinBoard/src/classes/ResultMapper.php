<?php

/**
 *
 * abcVote (https://github.com/EVGStudents/abcVote)
 * Bulletin-Board for abcVote's e-voting applications
 *
 * ResultMapper.php: mapping result php objects and database entries
 *
 * @author Sebastian Nellen <sebastian at nellen.it>
 */


class ResultMapper extends Mapper
{
  /**
  * method reading results from the database
  * and storing it into result object's
  */
  public function getResults() {
    $sql = "SELECT id, electionIdentifier, jsonData
      FROM tbl_results";
    $stmt = $this->db->query($sql);
    $results = [];
    while($row = $stmt->fetch()) {
      $results[] = new ResultEntity($row);
    }
    return $results;
  }

  /**
  * method storing a result object into the database
  */
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
