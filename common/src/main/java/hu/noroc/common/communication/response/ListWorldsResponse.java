package hu.noroc.common.communication.response;

import hu.noroc.common.communication.response.standard.SimpleResponse;

import java.util.List;

/**
 * Created by Oryk on 4/14/2016.
 */
public class ListWorldsResponse extends SimpleResponse {
    List<WorldData> worlds;

    public ListWorldsResponse() {
        super(SUCCESS);
    }

    public ListWorldsResponse(List<WorldData> worlds) {
        super(SUCCESS);
        this.worlds = worlds;
    }

    public static class WorldData{
        private String id;
        private String name;
        private int players;
        private int maxPlayers;

        public WorldData() {
        }

        public WorldData(String id, String name, int players, int maxPlayers) {
            this.id = id;
            this.name = name;
            this.players = players;
            this.maxPlayers = maxPlayers;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPlayers() {
            return players;
        }

        public void setPlayers(int players) {
            this.players = players;
        }

        public String getId() {

            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
