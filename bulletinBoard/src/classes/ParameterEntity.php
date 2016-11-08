<?php
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
      // no id if we're creating
      if(isset($data['parameterName'])) {
        $this->parameterName = $data['parameterName'];
      }
      $this->parameterValue = $data['parameterValue'];
    }
    public function getParameterName() {
        return $this->parameterName;
    }
    public function getParameterValue() {
        return $this->parameterValue;
    }
}
