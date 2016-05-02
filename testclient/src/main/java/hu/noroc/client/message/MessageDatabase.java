package hu.noroc.client.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import hu.noroc.client.message.log.MessageLog;
import hu.noroc.common.communication.message.Message;
import hu.noroc.common.communication.request.Request;
import hu.noroc.common.communication.response.standard.SimpleResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Oryk on 4/29/2016.
 */
public class MessageDatabase {
    private BufferedWriter writer;
    private RequestLogRepository requestLogRepository;
    private ResponseLogRepository responseLogRepository;
    private MessageLogRepository messageLogRepository;

    private boolean logToConsole;
    private boolean logToMongo;

    private List<RequestLog> outgoing = new ArrayList<>();
    private List<ResponseLog> incoming = new ArrayList<>();
    private List<MessageLog> incomingSync = new ArrayList<>();

    private ObjectMapper mapper = new ObjectMapper();

    public MessageDatabase(OutputStream logTo, String dbName, String host){
        if(logTo != null) {
            writer = new BufferedWriter(new OutputStreamWriter(logTo));
            this.logToConsole = true;
        }
        if(dbName != null && !dbName.isEmpty()){
            if(host == null || host.isEmpty())
                host = "localhost";
            DB database = new DB(new MongoClient(host), dbName);

            responseLogRepository = new ResponseLogRepository(database);
            requestLogRepository = new RequestLogRepository(database);
            messageLogRepository = new MessageLogRepository(database);

            this.logToMongo = true;
        }
    }

    public void logOutgoing(Request request) throws IOException {
        RequestLog log = new RequestLog(Calendar.getInstance().getTime(), request);
        outgoing.add(log);
        if(logToConsole) writer.write(mapper.writeValueAsString(log) + "\n");
        if(logToMongo) requestLogRepository.insert(log);
    }

    public void logIncoming(SimpleResponse response) throws IOException {
        ResponseLog log = new ResponseLog(Calendar.getInstance().getTime(), response);
        incoming.add(log);
        if(logToConsole) writer.write(mapper.writeValueAsString(log) + "\n");
        if(logToMongo) responseLogRepository.insert(log);
    }
    public void logIncoming(Message response) throws IOException {
        MessageLog log = new MessageLog(Calendar.getInstance().getTime(), response);
        incomingSync.add(log);
        if(logToConsole) writer.write(mapper.writeValueAsString(log) + "\n");
        if(logToMongo) messageLogRepository.insert(log);
    }

    public List<RequestLog> getOutgoing() {
        return outgoing;
    }

    public List<ResponseLog> getIncoming() {
        return incoming;
    }

    public List<MessageLog> getIncomingSync() {
        return incomingSync;
    }
}
