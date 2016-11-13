<?php

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
    $returnString .= '"' . $value->getId() . '":';
    $returnString .= $value->getJsonData();
  }
  $returnString = "{" . $returnString . "}";
  return $returnString;
}

// function checks if request header is JSON
function is_Content_Type_JSON($request, $response) {
  // check, if header is "Content-Type: application/json"
  $contentType = $request->getHeader('Content-Type');
  if (array_search("application/json", $contentType) === FALSE) {
    return FALSE;
  } else {
    return TRUE;
  }
}

?>
