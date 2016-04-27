package hu.noroc.test.utils;

import hu.noroc.entry.NorocEntry;
import hu.noroc.test.utils.common.ArrayListBuilder;

/**
 * Created by Oryk on 4/19/2016.
 */
public class TestServer {
    private Thread gameServer;

    public void startGame() throws InterruptedException {
        gameServer = new Thread(() -> {
            String[] args = new String[3];
            new ArrayListBuilder<String>()
                    .add("src/test/resources/Entry.json")
                    .add("src/test/resources/World.json")
                    .add("src/test/resources/WorldNPC.json")
                    .get().toArray(args);
            try {
                NorocEntry.main(args);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        gameServer.start();
    }

    public boolean isRunning(){
        return gameServer.isAlive();
    }

    public void stopServer() throws InterruptedException {
        gentleStop();
        Thread.sleep(1000);
        if(!gameServer.getState().equals(Thread.State.TERMINATED))
            forceStop();
    }

    private void gentleStop() throws InterruptedException {
        NorocEntry.stopServer();
    }
    private void forceStop(){
        gameServer.interrupt();
    }
}
