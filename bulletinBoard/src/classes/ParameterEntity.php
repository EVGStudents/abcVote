<?php

/**
 *
 * abcVote (https://github.com/EVGStudents/abcVote)
 * Bulletin-Board for abcVote's e-voting applications
 *
 * ParamterEntity.php: an object representing Bulletin-Board's parameters
 *  like the values h1, h2, p, q
 *
 * @author Sebastian Nellen <sebastian at nellen.it>
 */

class ParameterEntity
{
    protected $parameterName;
    protected $parameterValue;
    /**
     * Accept an array of data matching properties of this class
     * and create the class
     *
     * @param array $data The data to use to create
     */
    public function __construct(array $data) {
      // no id if we're creating as it comes from the database
      if(isset($data['parameterName'])) {
        $this->parameterName = $data['parameterName'];
      }
      $this->parameterValue = $data['parameterValue'];
    }

    /**
    * method returning a parameter's name
    */
    public function getParameterName() {
        return $this->parameterName;
    }
    /**
    * method returning a parameter's value
    */
    public function getParameterValue() {
        return $this->parameterValue;
    }
}
