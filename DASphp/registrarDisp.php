<?php
include 'headers.php';
$DB_SERVER="localhost"; #la dirección del servidor
$DB_USER="Xeonate006"; #el usuario para esa base de datos
$DB_PASS="2B5vLPJ5h"; #la clave para ese usuario
$DB_DATABASE="Xeonate006_entrega2"; #la base de datos a la que hay que conectarse

class RegistroResultado {
    public $code = "";
    public $message = "";
    public $id = 0;
}

$response = new RegistroResultado;
$response-> code = "OK";
$response-> message = "";
$response-> id = 0;

try{
    //Se verifican los campos
    if(isset($_POST['DEVICED'])){
        $deviceid = $_POST['DEVICED'];

        if($deviceid==NULL){
            $response-> code = "ERR";
            $response-> message = "Token nulo";
            $response-> id = 0;
        } else{
            $id = null;
            if(isset($_POST['ID'])){
                $id = $_POST['ID'];
            }
            $mysqli = new mysqli($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);
            if($id != null){
                $stat = $mysqli -> prepare("UPDATE ")
            }
        }
    }
}
