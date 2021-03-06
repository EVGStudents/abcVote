<?php

/**
 *
 * abcVote (https://github.com/EVGStudents/abcVote)
 * Bulletin-Board for abcVote's e-voting applications
 *
 * functions.php: Bulletin-Board's functions
 *
 * @author Sebastian Nellen <sebastian at nellen.it>
 */

 use Jose\Factory\JWKFactory;
 use Jose\Factory\JWSFactory;
 use Jose\Loader;

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

//function gets some informations about elections and delivers those concatenated as string
function get_elections_shortInfo(Array $data){
  $returnString = "";
  foreach ($data as $value) {
    $returnString .= $returnString ? ',' : '';
    $returnString .= '{"id":"' . $value->getId() . '",';
    $returnString .= '"electionTitle":"' . $value->getElectionTitle() . '",';
    $returnString .= '"beginDate":"' . $value->getBeginDate() . '",';
    $returnString .= '"endDate":"' . $value->getEndDate() . '"}';
  }
  $returnString = "[" . $returnString . "]";
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

//function gets the ballots as array
function get_ballots_as_JSON(Array $data){
  $returnString = "";
  foreach ($data as $value) {
    $returnString .= $returnString ? ',' : '';
    $returnString .= '{"id":"' . $value->getId() . '",';
    $returnString .= '"electionIdentifier":"' . $value->getElectionIdentifier() . '",';
    $returnString .= '"jsonData":' . $value->getJsonData() . ',';
    $returnString .= '"timestamp":"' . $value->getTimestamp() . '"}';
  }
  $returnString = "[" . $returnString . "]";
  return $returnString;
}

//function verifies the JWS signature passed in jsonData against the value stored in DB
function verify_signature($email, $certificate, $jwsSignature){
  // create JWK from X.509 certificate
  $jwk = JWKFactory::createFromCertificate($certificate, ['use' => 'sig', 'alg'=> 'RS256']);

  $loader = new Loader();

  try {
    // The JwsSignature is verified using our key.
    $jws = $loader->loadAndVerifySignatureUsingKey(
        $jwsSignature,
        $jwk,
        ['RS256'],
        $signature_index
    );
  } catch (Exception $e) {
      //echo 'Exception: ',  $e->getMessage(), "\n";
      return false; // exception is thrown if the verification failed
  }
  // verification suceeded
  return true;
}

//function returns the payload of a given JWS without verifiying it, as that was done before
function get_jwsPayload($jwsSignature){
  $loader = new Loader();
  $jws = $loader->load($jwsSignature);
  $payload = $jws->getPayload();
  return $payload;
}

//function returns certificates as array
function get_certificates_as_JSON(Array $data){
  $returnString = "";
  foreach ($data as $value) {
    $returnString .= $returnString ? ',' : '';
    $returnString .= '{"email":"' . $value->getEmailAdress() . '",';
    $returnString .= '"certificate":"' . $value->getCertificate() . '"';
  }
  $returnString = "[" . $returnString . "]";
  return $returnString;
}

?>
