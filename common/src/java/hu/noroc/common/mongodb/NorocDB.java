package hu.noroc.common.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import hu.noroc.common.data.repository.*;

/**
 * Created by Oryk on 12/24/2015.
 */
public class NorocDB {

    private String url;
    private String db;
    private String user;
    private String pw;

    private MongoClient client;
    private MongoDatabase database;

    private static NorocDB instance;

    /*REPOSITORIES*/

    private UserRepo userRepo;
    private CharacterRepo characterRepo;
    private CharacterClassRepo characterClassRepo;
    private ItemRepo itemRepo;
    private NPCRepo npcRepo;
    private SpellRepo spellRepo;

    private NorocDB(String url, String db, String user, String pw) {
        this.url = url;
        this.db = db;
        this.user = user;
        this.pw = pw;
    }

    private NorocDB(String url, String db) {
        this.url = url;
        this.db = db;
    }

    public static NorocDB getInstance(String url, String db, String user, String pw){
        if(instance == null){
            instance = new NorocDB(url, db, user, pw);
            instance.initialize();
        }
        return instance;
    }

    public static NorocDB getInstance(String url, String db){
        if(instance == null){
            instance = new NorocDB(url, db);
            instance.initialize();
        }
        return instance;
    }
    public static NorocDB getInstance(){
        return instance;
    }

    private void initialize(){
        client = new MongoClient(url);
        database = client.getDatabase(db);

        userRepo = new UserRepo(database);
        characterRepo = new CharacterRepo(database);
        characterClassRepo = new CharacterClassRepo(database);
        itemRepo = new ItemRepo(database);
        npcRepo = new NPCRepo(database);
        spellRepo = new SpellRepo(database);
    }

    public UserRepo getUserRepo() {
        return userRepo;
    }

    public CharacterRepo getCharacterRepo() {
        return characterRepo;
    }

    public ItemRepo getItemRepo() {
        return itemRepo;
    }

    public CharacterClassRepo getCharacterClassRepo() {
        return characterClassRepo;
    }

    public NPCRepo getNpcRepo() {
        return npcRepo;
    }

    public SpellRepo getSpellRepo() {
        return spellRepo;
    }
}
