<?php

/**
 *
 * abcVote (https://github.com/EVGStudents/abcVote)
 * Bulletin-Board for abcVote's e-voting applications
 *
 * Mapper.php: abstract class used to map database elements into objects
 *
 * @author Sebastian Nellen <sebastian at nellen.it>
 */

abstract class Mapper {
    protected $db;
    public function __construct($db) {
        $this->db = $db;
    }
}
