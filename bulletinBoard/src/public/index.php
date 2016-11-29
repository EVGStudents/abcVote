<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';
require './configuration.php';
require './functions.php';

spl_autoload_register(function ($classname) {
    require ("../classes/" . $classname . ".php");
});

$app = new \Slim\App(["settings" => $config]);
$container = $app->getContainer();

$container['view'] = new \Slim\Views\PhpRenderer("../templates/");

$container['db'] = function ($c) {
    $db = $c['settings']['db'];
    $pdo = new PDO("mysql:host=" . $db['host'] . ";dbname=" . $db['dbname'] .
        ';charset=utf8',
        $db['user'], $db['pass']);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $pdo->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
    return $pdo;
};

$container['errorHandler'] = function ($c) {
    return function ($request, $response, $exception) use ($c) {
        return $c['response']->withStatus(500)
                             ->withHeader('Content-Type', 'text/html')
                             ->write('Something went wrong!');
    };
};

// GET / : routine for testing purposes
$app->get('/', function (Request $request, Response $response) {
    $response->getBody()->write("Hello, I'm Slim - a micro framework for PHP");
    return $response;
});

// GET /voters : get the list of voters
/*
* UC 2.03: adminApp requests list of voters
* Request: Liste der verfügbaren Wähler
* Response: Liste der verfügbaren Wähler
*/
$app->get('/voters', function (Request $request, Response $response) {
  $mapper = new VoterMapper($this->db);
  $voters = $mapper->getVoters();
  $response = $response->withHeader('Content-type', 'application/json')
                      ->withAddedHeader('Content-Disposition', 'attachment; filename=voters.json')
                      ->write(get_voters_jsonData($voters));
  return $response;
});

// POST /elections : receives a new election
/*
* UC 2.19: adminApp sends a new election
* Request: Wahl erstellen (Wahloptionen, U, A, h^, Wahlperiode, Signatur)
* Response: Status
*/
$app->post('/elections', function (Request $request, Response $response) {
  // if ContentType is JSON, than store entry, else return 406
  if (is_Content_Type_JSON($request, $response) === TRUE) {
    try {
      $jsonBody = $request->getParsedBody();
      $jwsSignature = $jsonBody['signature'];
      $electMapper = new ElectionMapper($this->db);

      $certMapper = new CertificateMapper($this->db);
      $certs = $certMapper->getCertificates();

      $email = get_jwsPayload($jwsSignature)['author'];
      $certificate = "";

      foreach ($certs as $cert) {
        if ($cert->getEmailAdress() == $email) {
          $certificate = $cert->getCertificate();
        }
      }

      if (verify_signature($email, $certificate, $jwsSignature . "") === TRUE) {
        $payload = get_jwsPayload($jwsSignature);
        $electionArray = array('jsonData' => json_encode($payload),
                          'electionTitle' => $payload['electionTitle'],
                          'beginDate' => $payload['beginDate'],
                          'endDate' => $payload['endDate'],
                          'coefficients' => $payload['coefficients'],
                          'appVersion' => $payload['appVersion']);
        $election = new ElectionEntity($electionArray);
        return $electMapper->storeElection($election);
      } else {
        $response = $response->withStatus(401);
        $response = $response->withHeader('Content-Type', 'text/html')
                              ->write('Wrong signature!');
        return $response;
      }

    } catch (Exception $e) {
      $response = $response->withStatus(400);
      $response = $response->withHeader('X-Status-Reason', $e->getMessage());
      return $response;
    }
  } else {
    $response = $response->withStatus(406)
                         ->withHeader('Content-Type', 'text/html')
                         ->write('Wrong header, needs to be application/json!');
    return $response;
  }
});

// GET /parameters : get parameters from bulletin board
/*
* UC 1.04: clientApp requests parameters
* Request: (h1, h2, p, q) zum Berechnen des Wähler-Credentials u
* Response: (h1, h2, p, q)
* Remark: The adminApp also need those information for setting the electionGenerator h^
*/
$app->get('/parameters', function (Request $request, Response $response) {
  $mapper = new ParameterMapper($this->db);
  $parameters = $mapper->getParameters();
  $response = $response->withHeader('Content-type', 'application/json')
                      ->withAddedHeader('Content-Disposition', 'attachment; filename=parameters.json')
                      ->write(get_parameters_as_JSON($parameters));
  return $response;
});

// POST /voters : receives a new voter's registration
/*
* UC 1.12: voterApp registers a new voter
* Request: Wähler-Registrierung (Public Wähler-Credential, Signatur)
* Response: Status
*/
$app->post('/voters', function (Request $request, Response $response) {
  // if ContentType is JSON, than store entry, else return 406
  if (is_Content_Type_JSON($request, $response) === TRUE) {
    // if request is not empty, than proceed
    if (empty($request->getParsedBody()) === FALSE) {
      try {
        $jsonBody = $request->getBody();
        $voterArray = array('jsonData' => $jsonBody);
        $voter = new VoterEntity($voterArray);
        $mapper = new VoterMapper($this->db);
        return $mapper->storeVoter($voter);
      } catch (Exception $e) {
        return $response->withStatus(400)
                        ->withHeader('X-Status-Reason', $e->getMessage());
      }
    } else { //request was empty
      return $response->withStatus(400)
                      ->withHeader('Content-Type', 'text/html')
                      ->write('Empty request!');
    }
  } else { //wrong ContentType
    return $response->withStatus(406)
                    ->withHeader('Content-Type', 'text/html')
                    ->write('Wrong header, needs to be application/json!');
  }
});

// GET /elections/open: get general information about open elections
/*
* UC 3.04: clientApp requests general information about open elections
* Request: Kopfdaten aller offenen Abstimmungen
* Response: Kopfdaten der offenen Abstimmungen (ID, Titel, Wahlperiode)
*/
$app->get('/elections/open', function (Request $request, Response $response) {
    // if request is not empty, than proceed
    if (empty($request->getQueryParam('date')) === FALSE) {
      try{
        $date = $request->getQueryParam('date');
        $mapper = new ElectionMapper($this->db);
        $elections = $mapper->getElections();
        $openElections = array();
        // search for open elections
        foreach ($elections as $election) {
          if ($date <= $election->getEndDate()) {
            array_push($openElections, $election);
          }
        }
        if (empty($openElections)) { //no open elections found...
          $response = $response->withStatus(404)
                               ->withHeader('Content-Type', 'text/html')
                               ->write('No open elections found at the given date!');
        } else { //open elections found, returning them
          $response = $response->withHeader('Content-type', 'application/json')
                              ->withAddedHeader('Content-Disposition', 'attachment; filename=open-elections.json')
                              ->write(get_elections_shortInfo($openElections));
        }
      return $response;
    } catch (Exception $e) {
      return $response->withStatus(400)
                      ->withHeader('X-Status-Reason', $e->getMessage());
    }
  } else { //request was empty
    return $response->withStatus(400)
                    ->withHeader('Content-Type', 'text/html')
                    ->write('Empty request!');
  }
});

// GET /elections/closed: get general information about closed elections
/*
* UC 4.02: verifierApp requests general information about closed elections
* Request: Abfrage der abgeschlossenen Wahlen
* Response: Kopfdaten der abgeschlossenen Wahlen (Wahl-ID, Titel, Wahlperiode)
*/
$app->get('/elections/closed', function (Request $request, Response $response) {
    // if request is not empty, than proceed
    if (empty($request->getQueryParam('date')) === FALSE) {
      try{
        $date = $request->getQueryParam('date');
        $mapper = new ElectionMapper($this->db);
        $elections = $mapper->getElections();
        $closedElections = array();
        // search for closed elections
        foreach ($elections as $election) {
          if ($date >= $election->getEndDate()) {
            array_push($closedElections, $election);
          }
        }
        if (empty($closedElections)) { //no closed elections found...
          $response = $response->withStatus(404)
                               ->withHeader('Content-Type', 'text/html')
                               ->write('No closed elections found before the given date!');
        } else { //closed elections found, returning them
          $response = $response->withHeader('Content-type', 'application/json')
                              ->withAddedHeader('Content-Disposition', 'attachment; filename=open-elections.json')
                              ->write(get_elections_shortInfo($closedElections));
        }
      return $response;
    } catch (Exception $e) {
      return $response->withStatus(400)
                      ->withHeader('X-Status-Reason', $e->getMessage());
    }
  } else { //request was empty
    return $response->withStatus(400)
                    ->withHeader('Content-Type', 'text/html')
                    ->write('Empty request!');
  }
});

// GET /elections/{id} : get all information about election X with id='id'
/*
* UC 3.08: clientApp requests all information about the election with id='id'
* Request: Alle Informationen zur Abstimmung mit ID x (inkl. Koeffizienten A & Wählerliste U)
* Response: Alle Informationen zur Abstimmung mit ID x (inkl. Koeffizienten A & Wählerliste U)
*/
$app->get('/elections/{id}', function (Request $request, Response $response) {
  $mapper = new ElectionMapper($this->db);
  $elections = $mapper->getElections();
  $route = $request->getAttribute('route');
  $electionIdentifier = $route->getArgument('id');
  foreach ($elections as $election) {
    if ($electionIdentifier == $election->getId()) {
      $response = $response->withHeader('Content-type', 'application/json')
                          ->withAddedHeader('Content-Disposition', 'attachment; filename=election-'. $electionIdentifier .'.json')
                          ->write($election->getJsonData());
      return $response;
    }
  }
  $response = $response->withStatus(404)
                       ->withHeader('Content-Type', 'text/html')
                       ->write('Election ID not found!');
  return $response;
});

// POST /elections/{id}/ballots : receives a ballot for election X with id='id'
/*
* UC 3.18: voterApp sends a ballot
* Request: Stimmabgabe (Ballot B) für Abstimmung mit ID x
* Response: Status
*/
$app->post('/elections/{id}/ballots', function (Request $request, Response $response) {
  // if ContentType is JSON, than store entry, else return 406
  if (is_Content_Type_JSON($request, $response) === TRUE) {
    // if request is not empty, than proceed
    if (empty($request->getParsedBody()) === FALSE) {
      try {
        $jsonBody = $request->getBody();
        $route = $request->getAttribute('route');
        $electionIdentifier = $route->getArgument('id');
        $ballotArray = array('electionIdentifier' => $electionIdentifier,
                              'jsonData' => $jsonBody);
        $ballot = new BallotEntity($ballotArray);
        $mapper = new BallotMapper($this->db);
        return $mapper->storeBallot($ballot);
      } catch (Exception $e) {
        return $response->withStatus(400)
                        ->withHeader('X-Status-Reason', $e->getMessage());
      }
    } else { //request was empty
      return $response->withStatus(400)
                      ->withHeader('Content-Type', 'text/html')
                      ->write('Empty request!');
    }
  } else { //wrong ContentType
    return $response->withStatus(406)
                    ->withHeader('Content-Type', 'text/html')
                    ->write('Wrong header, needs to be application/json!');
  }
});

// GET /elections/{id}/ballots : gets all ballot for election X with id='id'
/*
* UC 4.06: verifierApp requests all ballots for a given election
* Request: Abfrage aller Ballots B zur Abstimmung mit ID x
* Response: Ballots B zur Abstimmung mit ID x
*/
$app->get('/elections/{id}/ballots', function (Request $request, Response $response) {
  try {
    $mapper = new BallotMapper($this->db);
    $ballots = $mapper->getBallots();
    $ballotArray = array();
    $route = $request->getAttribute('route');
    $electionIdentifier = $route->getArgument('id');
    foreach ($ballots as $ballot) {
      if ($electionIdentifier == $ballot->getElectionIdentifier()) {
        array_push($ballotArray, $ballot);
      }
    }
    if (empty($ballotArray)) { //no ballots found...
      $response = $response->withStatus(404)
                           ->withHeader('Content-Type', 'text/html')
                           ->write('No ballots for the given election id!');
    } else { //closed elections found, returning them
      $response = $response->withHeader('Content-type', 'application/json')
                          ->withAddedHeader('Content-Disposition', 'attachment; filename=ballots_for_election-'. $electionIdentifier . '.json')
                          ->write(get_ballots_as_JSON($ballotArray));
    }
    return $response;
  } catch (Exception $e) {
    return $response->withStatus(400)
                    ->withHeader('X-Status-Reason', $e->getMessage());
  }
});

// GET /elections : get all elections from bulletin board
$app->get('/elections', function (Request $request, Response $response) {
  $mapper = new ElectionMapper($this->db);
  $elections = $mapper->getElections();
  $response = $response->withHeader('Content-type', 'application/json')
                      ->withAddedHeader('Content-Disposition', 'attachment; filename=elections.json')
                      ->write(get_elections_shortInfo($elections));
  return $response;
});

// GET /view/voters: generates a html page which show the tbl_voters' content
$app->get('/view/voters', function (Request $request, Response $response) {
  $mapper = new VoterMapper($this->db);
  $voters = $mapper->getVoters();
  $response = $this->view->render($response, "voters.phtml", ["voters" => $voters]);
  return $response;
});

// GET /view/parameters: generates a html page which show the tbl_parameters' content
$app->get('/view/parameters', function (Request $request, Response $response) {
  $mapper = new ParameterMapper($this->db);
  $parameters = $mapper->getParameters();
  $response = $this->view->render($response, "parameters.phtml", ["parameters" => $parameters]);
  return $response;
});

// GET /view/elections: generates a html page which show the tbl_elections' content
$app->get('/view/elections', function (Request $request, Response $response) {
  $mapper = new ElectionMapper($this->db);
  $elections = $mapper->getElections();
  $response = $this->view->render($response, "elections.phtml", ["elections" => $elections]);
  return $response;
});

// GET /view/ballots: generates a html page which show the tbl_ballots' content
$app->get('/view/ballots', function (Request $request, Response $response) {
  $mapper = new BallotMapper($this->db);
  $ballots = $mapper->getBallots();
  $response = $this->view->render($response, "ballots.phtml", ["ballots" => $ballots]);
  return $response;
});

// Run App
$app->run();
