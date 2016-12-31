<?php

/**
 *
 * abcVote (https://github.com/EVGStudents/abcVote)
 * Bulletin-Board for abcVote's e-voting applications
 *
 * BallotMapper.php: mapping parameter php objects and database entries
 *
 * @author Sebastian Nellen <sebastian at nellen.it>
 */

class ParameterMapper extends Mapper
{
  /**
  * method reading bulletinBoard's parameters from the database
  * and storing it into parameters object's
  */
  public function getParameters() {
      $sql = "SELECT parameterName, parameterValue
          FROM tbl_parameters";
      $stmt = $this->db->query($sql);
      $results = [];
      while($row = $stmt->fetch()) {
          $results[] = new ParameterEntity($row);
      }
      return $results;
  }
}
