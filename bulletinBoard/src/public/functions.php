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
  $returnString = "";
  foreach ($data as $value) {
    $returnString .= $returnString ? ',' : '';
    $returnString .= $value->getJsonData();
  }
  $returnString = "[" . $returnString . "]";
  return $returnString;
}

//function gets the parameters as array and generates the key-value-pairs for the parameters.json, but delivers it as string.
function get_parameters_as_JSON(Array $data){
  $returnString = "";
  foreach ($data as $value) {
    $returnString .= $returnString ? ',' : '';
    $returnString .= '"' . $value->getParameterName() . '":';
    $returnString .= '"' . $value->getParameterValue() . '"';
  }
  $returnString = "{" . $returnString . "}";
  return $returnString;
}

//function gets all elections as array and delivers the jsonData concatenated as string
function get_elections_jsonData(Array $data){
  $returnString = "";
  foreach ($data as $value) {
    $returnString .= $returnString ? ',' : '';
    $returnString .= $value->getJsonData();
  }
  $returnString = "[" . $returnString . "]";
  return $returnString;
}


?>
