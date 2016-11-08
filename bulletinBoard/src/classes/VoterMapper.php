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
}
