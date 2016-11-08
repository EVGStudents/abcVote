<?php
class GeneratorEntity
{
    protected $generatorName;
    protected $generatorValue;
    /**
     * Accept an array of data matching properties of this class
     * and create the class
     *
     * @param array $data The data to use to create
     */
    public function __construct(array $data) {
      // no id if we're creating
      if(isset($data['generatorName'])) {
        $this->generatorName = $data['generatorName'];
      }
      $this->generatorValue = $data['generatorValue'];
    }
    public function getGeneratorName() {
        return $this->generatorName;
    }
    public function getGeneratorValue() {
        return $this->generatorValue;
    }
}
