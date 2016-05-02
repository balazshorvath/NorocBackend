package hu.noroc.client;

import hu.noroc.client.message.MessageDatabase;
import hu.noroc.client.utils.TestClient;
import hu.noroc.client.utils.common.ArrayListBuilder;
import hu.noroc.common.communication.request.Request;
import hu.noroc.common.communication.request.ingame.PlayerAttackRequest;
import hu.noroc.common.communication.request.ingame.PlayerMoveRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Oryk on 4/28/2016.
 */
public class NorocClient {

    public static void main(String[] args) {
        boolean running = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] commands;
        TestClient client = new TestClient();

        while(running){
            try {
                commands = reader.readLine().split(" ");
                switch (commands[0].toLowerCase()) {
                    case "request":
                        doRequest(client, commands);
                        break;
                    case "show":
                        switch (commands[2].toLowerCase()){
                            case "visible":
                                client.printVisible();
                                break;
                            case "character":
                                client.printCharacter();
                                break;
                            case "logs":
                                break;
                        }
                        break;
                    case "connect":
                        client.init(commands[1], Integer.parseInt(commands[2]));
                        break;
                    case "reconnect":
                        client.pauseAndReconnect(commands[1], Integer.parseInt(commands[2]));
                        break;
                    case "log":
                        client.setMessageDatabase(new MessageDatabase(System.out, commands[1], commands[2]));
                        break;
                    case "disconnect":
                        client.disconnect();
                        break;
                    case "exit":
                        client.disconnect();
                        running = false;
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void doRequest(TestClient client, String[] command) throws Exception {
        Request request = null;
        switch (command[1]){
            case "login":
                client.login(command[2], command[3]);
                break;
            case "characterlist":
                client.listCharacters();
                break;
            case "worldlist":
                client.listWorlds();
                break;
            case "choosecharacter":
                client.chooseCharacter(command[2], command[3]);
                break;
            case "move":
                request = new PlayerMoveRequest();
                double[][] path = new double[2][2];

                path[0][0] = client.getCurrent().getX();
                path[0][1] = client.getCurrent().getY();

                path[1][0] = Double.valueOf(command[2]);
                path[1][1] = Double.valueOf(command[3]);

                ((PlayerMoveRequest) request).setPath(
                        path
                );
                break;
            case "attack":
                request = new PlayerAttackRequest();

                ((PlayerAttackRequest) request).setX(Double.valueOf(command[2]));
                ((PlayerAttackRequest) request).setY(Double.valueOf(command[3]));

                ((PlayerAttackRequest) request).setSpellId(command[4]);

                break;
            case "equip":
                System.out.println("Not supported.");
                break;
            case "interact":
                System.out.println("Not supported.");
                break;
        }
        if(request != null){
            client.sendIngameRequest(request);
        }
    }

}
