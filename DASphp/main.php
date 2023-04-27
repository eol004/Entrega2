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
#Recoger parametros de android studio
$usuario = $_POST["usuario"];
#$usuario ="prueba";
$nombre = $_POST["nombre"];
$contraseña = $_POST["contraseña"];

#Guardar contraseña en forma de hash
$contraseña_hash = password_hash($contraseña, PASSWORD_DEFAULT);

#Insertar en la base de datos
$sql = "INSERT INTO usuarios (usuario, nombre, contraseña) VALUES ('$usuario', '$nombre', '$contraseña_hash')";

if ($con->query($sql) === TRUE) {
    echo "Registro exitoso " . $usuario . " " . $nombre . " " . $contraseña_hash;
} else {
  echo "Error al registrar el usuario: " . $con->error;
}

n->close();

?>