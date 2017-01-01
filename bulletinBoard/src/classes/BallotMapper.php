<?php

/**
 *
 * abcVote (https://github.com/EVGStudents/abcVote)
 * Bulletin-Board for abcVote's e-voting applications
 *
 * BallotMapper.php: mapping ballots php objects and database entries
 *
 * @author Sebastian Nellen <sebastian at nellen.it>
 */

class BallotMapper extends Mapper
{
  /**
  * method reading ballots from the database
  * and storing it into ballot object's
  */
  public function getBallots() {
    $sql = "SELECT id, electionIdentifier, jsonData, ballotTimestamp
      FROM tbl_ballots";
    $stmt = $this->db->query($sql);
    $results = [];
    while($row = $stmt->fetch()) {
      $results[] = new BallotEntity($row);
    }
    return $results;
  }

  /**
  * method storing a ballot object into the database
  */
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
