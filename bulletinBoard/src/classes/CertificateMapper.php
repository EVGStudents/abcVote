<?php
class CertificateMapper extends Mapper
{
    public function getCertificates() {
        $sql = "SELECT email, certificate
            from tbl_certificates";
        $stmt = $this->db->query($sql);
        $results = [];
        while($row = $stmt->fetch()) {
            $results[] = new CertificateEntity($row);
        }
        return $results;
    }
}
