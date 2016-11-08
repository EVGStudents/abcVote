<?php

function store_new_election($postdata)
{
  global $election;
  global $app;
  $data = json_decode($postdata,true);
  echo $data["hello"];
  print_r(array_values($data));
  echo $data["electionTitle"];
  file_put_contents("election-t.json",json_encode($data["electionTitle"]));
  return;
}


//function gets the voters as array and delivers the jsonData concatenated as string
function get_voters_jsonData(Array $data){
  $returnString = "[";
  foreach ($data as &$value) {
    $returnString = $returnString . $value->getJsonData() . ",";
  }
  // remove last "," and close
  $returnString = substr_replace($returnString, '', -1);
  $returnString = $returnString . "]";
  return $returnString;
}

//function gets the generators as array and generates the key-value-pairs for the generators.json, but delivers it as string.
function get_generators_as_JSON(Array $data){
  $returnString = "[";
  foreach ($data as &$value) {
    $returnString = $returnString . '"' . $value->getGeneratorName() . '":';
    $returnString = $returnString . '"' . $value->getGeneratorValue() . '"' . ",";
  }
  // remove last "," and close
  $returnString = substr_replace($returnString, '', -1);
  $returnString = $returnString . "]";
  return $returnString;
}

//function gets all elections as array and delivers the jsonData concatenated as string
function get_elections_jsonData(Array $data){
  $returnString = "[";
  foreach ($data as &$value) {
    $returnString = $returnString . $value->getJsonData() . ",";
  }
  // remove last "," and close
  $returnString = substr_replace($returnString, '', -1);
  $returnString = $returnString . "]";
  return $returnString;
}


?>
