<?php
$DB_SERVER="localhost"; #la dirección del servidor
$DB_USER="Xeonate006"; #el usuario para esa base de datos
$DB_PASS="2B5vLPJ5h"; #la clave para ese usuario
$DB_DATABASE="Xeonate006_entrega2"; #la base de datos a la que hay que conectarse
# Se establece la conexión:
$con = mysqli_connect($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);
#Comprobamos conexión
if (mysqli_connect_errno()) {
    echo 'Error de conexion: ' . mysqli_connect_error();
}
#Obtener los valores ingresados
$usuario = $_POST['usuario'];
$contraseña = $_POST['contraseña'];

#Crear la consulta SQL
$sql = "SELECT * FROM usuarios WHERE usuario = '$usuario'";

#Ejecutar la consulta
$result = $con->query($sql);

#Comprobar si la consulta devuelve algún resultado
if ($result->num_rows > 0) {
    #Obtener el valor hash almacenado en la base de datos
    $fila = $result->fetch_assoc();
    $hash = $fila['contraseña'];
    if(password_verify($contraseña, $hash)){
        #Los valores ingresados existen en la base de datos
        $respuesta = array("existe" => true);
        echo 'log correcto';
    }else{
        $respuesta = array("existe" => false);
        echo 'passwd incorrecto';
    }
    
} else {
    #Los valores ingresados no existen en la base de datos
    $respuesta = array("existe" => false);
    echo 'no existe';
}

#Devolver la respuesta en formato JSON
#echo json_encode($respuesta);
$json_respuesta = json_encode($respuesta);
#Cerrar la conexión

$con->close();
?>