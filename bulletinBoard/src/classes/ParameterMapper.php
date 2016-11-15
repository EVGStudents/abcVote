<?php
class ParameterMapper extends Mapper
{
    public function getParameters() {
        $sql = "SELECT parameterName, parameterValue
            from tbl_parameters";
        $stmt = $this->db->query($sql);
        $results = [];
        while($row = $stmt->fetch()) {
            $results[] = new ParameterEntity($row);
        }
        return $results;
    }
}
