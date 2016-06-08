package hu.noroc.common.communication.response.standard;

/**
 * Created by Oryk on 2016. 06. 05..
 */
public class PingResponse extends SimpleResponse {
    private Long clientTime;
    private Long serverTime;
    private int protocolType;

    public PingResponse(Long clientTime, Long serverTime, int protocolType) {
        super(SUCCESS);
        this.clientTime = clientTime;
        this.serverTime = serverTime;
        this.protocolType = protocolType;
    }

    public Long getClientTime() {
        return clientTime;
    }

    public void setClientTime(Long clientTime) {
        this.clientTime = clientTime;
    }

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }

    public int getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(int protocolType) {
        this.protocolType = protocolType;
    }
}