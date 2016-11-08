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
    $pdo = new PDO("mysql:host=" . $db['host'] . ";dbname=" . $db['dbname'],
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

// GET /hello/{name} : hello routine for testing purposes
$app->get('/hello/{name}', function (Request $request, Response $response) {
    $name = $request->getAttribute('name');
    $response->getBody()->write("Hello, $name");
    return $response;
});

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
  $response = $response->withJson($voters{"jsonData"});
  /*foreach ($voters as &$voter) {
    echo json_encode($voters{'jsonData'});
    print_r($voter);
    var_export($voter);
    get_object_vars($voter);
  }*/
  $voters = $mapper->jsonifyVoters();
    print_r($voters);
    //var_export($voters);
    //get_object_vars($voters);

  return;
});

// POST /elections : receives a new election
/*
* UC 2.19: adminApp sends a new election
* Request: Wahl erstellen (Wahloptionen, U, A, h^, Wahlperiode, Signatur)
* Response: Status
*/
$app->post('/elections', function (Request $request, Response $response) {
  // check, if header is "Content-Type: application/json"
  $contentType = $request->getHeader('Content-Type');
  if (array_search("application/json", $contentType) === FALSE) {
    $response = $response->withStatus(406)
                         ->withHeader('Content-Type', 'text/html')
                         ->write('Wrong header, needs to be application/json!');
    return $response;
  }
  try {
    $json = $request->getParsedBody();
  } catch (Exception $e) {
    $app->response()->status(400);
    $app->response()->header('X-Status-Reason', $e->getMessage());
  }
  return;
  //return $response->write(store_new_election($postdata));
});

// GET /generators : get generators from bulletin board
/*
* UC 1.04: clientApp requests generators
* Request: (h1, h2, p, q) zum Berechnen des Wähler-Credentials u
* Response: (h1, h2, p, q)
* Remark: The adminApp also need those information for setting the electionGenerator h^
*/
$app->get('/generators', function (Request $request, Response $response) {
  $mapper = new GeneratorMapper($this->db);
  $generators = $mapper->getGenerators();
  $response = $response->withHeader('Content-type', 'application/json');
  $response = $response->withAddedHeader('Content-Disposition', 'attachment; filename=generators.json');
  $response = $response->withJson(["generators" => $generators]);
  return $response;
});

// GET /elections : get all elections from bulletin board
$app->get('/elections', function (Request $request, Response $response) {
  $mapper = new ElectionMapper($this->db);
  $elections = $mapper->getElections();
  $response = $response->withHeader('Content-type', 'application/json');
  $response = $response->withAddedHeader('Content-Disposition', 'attachment; filename=elections.json');
  $response = $response->withJson(get_elections_jsonData($elections));
  return $response;
});

// GET /view/voters: generates a html page which show the tblVoters' content
$app->get('/view/voters', function (Request $request, Response $response) {
  $mapper = new VoterMapper($this->db);
  $voters = $mapper->getVoters();
  $response = $this->view->render($response, "voters.phtml", ["voters" => $voters]);
  return $response;
});

// GET /view/generators: generates a html page which show the tblGenerators' content
$app->get('/view/generators', function (Request $request, Response $response) {
  $mapper = new GeneratorMapper($this->db);
  $generators = $mapper->getGenerators();
  $response = $this->view->render($response, "generators.phtml", ["generators" => $generators]);
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
