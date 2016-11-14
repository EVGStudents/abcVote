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
  $response = $response->withHeader('Content-type', 'application/json');
  $response = $response->withAddedHeader('Content-Disposition', 'attachment; filename=voters.json');
  $response = $response->write(get_voters_jsonData($voters));
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
      $jsonBodyAsArray = json_decode($request->getBody(), true);
      $jsonBody = $request->getBody();
      $electionArray = array('jsonData' => $jsonBody,
                        'electionTitle' => $jsonBodyAsArray['electionTitle'],
                        'beginDate' => $jsonBodyAsArray['beginDate'],
                        'endDate' => $jsonBodyAsArray['endDate'],
                        'coefficients' => $jsonBodyAsArray['coefficients'],
                        'appVersion' => $jsonBodyAsArray['appVersion']);
      $election = new ElectionEntity($electionArray);
      $mapper = new ElectionMapper($this->db);
      return $mapper->storeElection($election);
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
  $response = $response->withHeader('Content-type', 'application/json');
  $response = $response->withAddedHeader('Content-Disposition', 'attachment; filename=parameters.json');
  $response = $response->write(get_parameters_as_JSON($parameters));
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
    try {
      $jsonBody = $request->getBody();
      $voterArray = array('jsonData' => $jsonBody);
      $voter = new VoterEntity($voterArray);
      $mapper = new VoterMapper($this->db);
      return $mapper->storeVoter($voter);
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

// GET /elections/open: get general information about open elections
/*
* UC 3.04: clientApp requests general information about open elections
* Request: Kopfdaten aller offenen Abstimmungen
* Response: Kopfdaten der offenen Abstimmungen (ID, Titel, Wahlperiode)
*/
$app->get('/elections/open', function (Request $request, Response $response) {
  $jsonBody = $request->getParsedBody();
  $mapper = new ElectionMapper($this->db);
  $elections = $mapper->getElections();
  $openElections = array();
  foreach ($elections as $election) {
    if ($jsonBody['date'] <= $election->getEndDate()) {
      array_push($openElections, $election);
    }
  }
  if (empty($openElections)) {
    $response = $response->withStatus(404)
                         ->withHeader('Content-Type', 'text/html')
                         ->write('No open election not found before this date!');
  } else {
    $response = $response->withHeader('Content-type', 'application/json');
    $response = $response->withAddedHeader('Content-Disposition', 'attachment; filename=open-elections.json');
    $response = get_elections_shortInfo($openElections);
  }
    return $response;
});

// GET /elections/{id} : get all information about election X with id='id'
/*
* UC 3.08: clientApp requests all information about the election with id='id'
* Request: Alle Informationen zur Abstimmung mit ID x
* Response: Alle Informationen zur Abstimmung mit ID x
*/
$app->get('/elections/{id}', function (Request $request, Response $response) {
  $mapper = new ElectionMapper($this->db);
  $elections = $mapper->getElections();
  $route = $request->getAttribute('route');
  $electionIdentifier = $route->getArgument('id');
  foreach ($elections as $election) {
    if ($electionIdentifier == $election->getId()) {
      $response = $response->withHeader('Content-type', 'application/json');
      $response = $response->withAddedHeader('Content-Disposition', 'attachment; filename=election-'. $electionIdentifier .'.json');
      $response = $response->write($election->getJsonData());
      return $response;
    }
  }
  $response = $response->withStatus(404)
                       ->withHeader('Content-Type', 'text/html')
                       ->write('Election ID not found!');
  return $response;
});

// GET /elections : get all elections from bulletin board
$app->get('/elections', function (Request $request, Response $response) {
  $mapper = new ElectionMapper($this->db);
  $elections = $mapper->getElections();
  $response = $response->withHeader('Content-type', 'application/json');
  $response = $response->withAddedHeader('Content-Disposition', 'attachment; filename=elections.json');
  $response = $response->write(get_elections_shortInfo($elections));
  return $response;
});

// GET /view/voters: generates a html page which show the tblVoters' content
$app->get('/view/voters', function (Request $request, Response $response) {
  $mapper = new VoterMapper($this->db);
  $voters = $mapper->getVoters();
  $response = $this->view->render($response, "voters.phtml", ["voters" => $voters]);
  return $response;
});

// GET /view/parameters: generates a html page which show the tblParameters' content
$app->get('/view/parameters', function (Request $request, Response $response) {
  $mapper = new ParameterMapper($this->db);
  $parameters = $mapper->getParameters();
  $response = $this->view->render($response, "parameters.phtml", ["parameters" => $parameters]);
  return $response;
});

// GET /view/elections: generates a html page which show the tblElections' content
$app->get('/view/elections', function (Request $request, Response $response) {
  $mapper = new ElectionMapper($this->db);
  $elections = $mapper->getElections();
  $response = $this->view->render($response, "elections.phtml", ["elections" => $elections]);
  return $response;
});

// Run App
$app->run();
