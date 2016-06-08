<?php
    if(array_key_exists("username", $_POST)) {
        if(!array_key_exists("password", $_POST)
            || !array_key_exists("passwordConfirm", $_POST)){
            $error = "All the fields are required!";
        }

        if(strlen($_POST["username"]) <= 4
            && preg_match("^[a-zA-Z0-9]{4,10}$", $_POST["username"])){
            $error = "The username must be at least 4 characters long (maximum 10) and can not contain special characters.";
        }
        if(strlen($_POST["password"]) <= 4
            && preg_match("^.{4,32}", $_POST["username"])){
            $error = "The password must contain at least 4 characters (maximum 32).";
        }
        if(strcmp($_POST["passwordConfirm"], $_POST["password"])){
            $error = "The passwords are not identical.";
        }
//
//        if(strlen($_POST["email"]) <= 4
//            && preg_match("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\\b", $_POST["email"])){
//            $error = "The email address must be valid.";
//        }


        try{
            $mongo = new MongoClient();
            $norocDB = $mongo->selectDB("Noroc");

            $collection = $norocDB->User;
            $result = $collection->find(array("username" => $_POST["username"]));
            if($result->count() < 1){
                $collection->insert(array(
                    "username" => $_POST["username"],
                    "password" => $_POST["password"]
                ));
                $message = "Success!";
            }else{
                $error = "Username already taken.";
            }
        }catch (MongoConnectionException $e){
            $error = "Couldn't connect to database.<br>In case of this error, contact me please: <b>balazs.peter.horvath@gmail.com</b>";
        }

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
    }
    if(isset($error)){
        ?>
        <p class="error"> <?php echo $error; ?> </p>
        <?php
    }
?>
</html>
