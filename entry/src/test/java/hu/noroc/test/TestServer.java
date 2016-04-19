package hu.noroc.test;

import hu.noroc.entry.NorocEntry;
import hu.noroc.test.utils.common.ArrayListBuilder;

/**
 * Created by Oryk on 4/19/2016.
 */
public class TestServer {
    public void startGame() throws InterruptedException {
        NorocEntry.main(
                (String[]) new ArrayListBuilder<String>()
                        .add("src/test/resources/Entry.json")
                        //TODO itt voltam legutóbb
                        .add("src/test/resources/World.json")
                        .add("src/test/resources/WorldNPC.json")
                        .get().toArray()
        );
    }
}
