<?php

/**
 *
 * abcVote (https://github.com/EVGStudents/abcVote)
 * Bulletin-Board for abcVote's e-voting applications
 *
 * VoterMapper.php: mapping voter php objects and database entries
 *
 * @author Sebastian Nellen <sebastian at nellen.it>
 */

class VoterMapper extends Mapper
{
  /**
  * method reading voters from the database
  * and storing it into voter object's
  */
  public function getVoters() {
    $sql = "SELECT id, jsonData
      FROM tbl_voters";
    $stmt = $this->db->query($sql);
    $results = [];
    while($row = $stmt->fetch()) {
      $results[] = new VoterEntity($row);
    }
    return $results;
  }

  /**
  * method storing a voter object into the database
  */
  public function storeVoter(VoterEntity $voter) {
    $stmt = $this->db->prepare("INSERT INTO tbl_voters (jsonData)
      VALUES (:jsonData)");

    $stmt->bindParam(':jsonData', $jsonData);

    $jsonData = $voter->getJsonData();

    return $stmt->execute();
  }
}
