<?php

/**
 *
 * abcVote (https://github.com/EVGStudents/abcVote)
 * Bulletin-Board for abcVote's e-voting applications
 *
 * BallotMapper.php: mapping election php objects and database entries
 *
 * @author Sebastian Nellen <sebastian at nellen.it>
 */

class ElectionMapper extends Mapper
{
  /**
  * method reading elections from the database
  * and storing it into election object's
  */
  public function getElections() {
      $sql = "SELECT id, jsonData, electionTitle,
          beginDate, endDate, coefficients, appVersion
          FROM tbl_elections";
      $stmt = $this->db->query($sql);
      $results = [];
      while($row = $stmt->fetch()) {
          $results[] = new ElectionEntity($row);
      }
      return $results;
  }

  /**
  * method storing an election object into the database
  */
  public function storeElection(ElectionEntity $election) {
    $stmt = $this->db->prepare("INSERT INTO tbl_elections
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
