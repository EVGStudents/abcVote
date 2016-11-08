<?php
class ElectionMapper extends Mapper
{
    public function getElections() {
        $sql = "SELECT id, jsonData, electionTitle, beginDate, endDate, appVersion
            from tblElections";
        $stmt = $this->db->query($sql);
        $results = [];
        while($row = $stmt->fetch()) {
            $results[] = new ElectionEntity($row);
        }
        return $results;
    }
}
