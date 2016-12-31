<?php

/**
 *
 * abcVote (https://github.com/EVGStudents/abcVote)
 * Bulletin-Board for abcVote's e-voting applications
 *
 * CertificateMapper.php: mapping certificate php objects and database entries
 *
 * @author Sebastian Nellen <sebastian at nellen.it>
 */

class CertificateMapper extends Mapper
{
  /**
  * method reading certificates from the database
  * and storing it into certificate object's
  */
  public function getCertificates() {
      $sql = "SELECT email, certificate
          FROM tbl_certificates";
      $stmt = $this->db->query($sql);
      $results = [];
      while($row = $stmt->fetch()) {
          $results[] = new CertificateEntity($row);
      }
      return $results;
  }
}
