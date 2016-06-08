<?php

    class Validation{
        public static $error = null;

        static function validateRequest(){

            if (!array_key_exists("password", $_POST)
                || !array_key_exists("passwordConfirm", $_POST)) {
                Validation::$error = "All the fields are required!";
                return false;
            }

            if (strlen($_POST["username"]) <= 4
                && !preg_match("/^[a-zA-Z0-9]{4,10}$/", $_POST["username"])) {
                Validation::$error = "The username must be at least 4 characters long (maximum 10) and can not contain special characters.";
                return false;
            }
            if (strlen($_POST["password"]) <= 4
                && !preg_match("/^.{4,32}$/", $_POST["password"])) {
                Validation::$error = "The password must contain at least 4 characters (maximum 32).";
                return false;
            }
            if (strcmp($_POST["passwordConfirm"], $_POST["password"])) {
                Validation::$error = "The passwords are not identical.";
                return false;
            }
            return true;
        }
    }
    /* Code from http://stackoverflow.com/questions/23066005/sha256-in-php-java */
    function hash256($input) {
        $hash = hash("sha256", $input, true);
        $output = "";
        foreach(str_split($hash, 2) as $key => $value) {
            if (strpos($value, "0") === 0) {
                $output .= str_replace("0", "", $value);
            } else {
                $output .= $value;
            }
        }
        return $output;
    }
    if(array_key_exists("username", $_POST)) {
        if(Validation::validateRequest()){
            try{
                $mongo = new MongoClient();
                $norocDB = $mongo->selectDB("Noroc");

                $collection = $norocDB->User;
                $result = $collection->find(array("username" => $_POST["username"]));
                if($result->count() < 1){
                    $collection->insert(array(
                        "username" => $_POST["username"],
                        "password" => base64_encode(hash256($_POST["password"]))
                    ));
                    $message = "Success!<br>After you start the game don't forget to set up the server address in the options:<br>Host - noroc.ddns.net<br>Port - 1234";
                }else {
                    Validation::$error = "Username already taken.";
                }
            }catch (MongoConnectionException $e){
                Validation::$error = "Couldn't connect to database.<br>In case of this error, contact me please: <b>balazs.peter.horvath@gmail.com</b>";
            }finally{
                $mongo->close();
            }
        }
//
//        if(strlen($_POST["email"]) <= 4
//            && preg_match("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\\b", $_POST["email"])){
//            $error = "The email address must be valid.";
//        }




    }
?>
<html>
<head>
    <title>Noroc</title>
    <style>
        .error{
            color: #ff2415;
        }
    </style>
</head>
<h1>Welcome!</h1>
<div>
    At this site you can register to our game named Noroc. The client is downloadable <a href="/game/build.zip">here</a>.
</div>
<?php
    if(!isset($message)) { ?>
        <form action="/" method="post">
            <input type="text" name="username" placeholder="Username"/>
            <input type="password" name="password" placeholder="Password"/>
            <input type="password" name="passwordConfirm" placeholder="Confirm"/>
            <input type="submit" value="Register"/>
        </form>
        <?php
        if(isset(Validation::$error)){
            ?>
            <p class="error"> <?php echo Validation::$error; ?> </p>
            <?php
        }
    }else{ ?>
        <p> <?php echo $message; ?> </p>
        <?php
    }

?>
</html>
