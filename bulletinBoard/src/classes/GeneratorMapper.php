<?php
class GeneratorMapper extends Mapper
{
    public function getGenerators() {
        $sql = "SELECT generatorName, generatorValue
            from tblGenerators";
        $stmt = $this->db->query($sql);
        $results = [];
        while($row = $stmt->fetch()) {
            $results[] = new GeneratorEntity($row);
        }
        return $results;
    }
}
