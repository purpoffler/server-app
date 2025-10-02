package layer.dto;

import layer.enums.ExpectedDataType;

public class UserPackage {
    private final String signature;
    private final String dataLength;
    private final ExpectedDataType dataType;
    private final String data;
    private final String clientCRC32;
    private final String ip;
    private final String date;

    public UserPackage(String signature, String dataLength, ExpectedDataType dataType, String data, String clientCRC32, String ip, String date) {
        this.signature = signature;
        this.dataLength = dataLength;
        this.dataType = dataType;
        this.data = data;
        this.clientCRC32 = clientCRC32;
        this.ip = ip;
        this.date = date;
    }

    public String getSignature() {
        return signature;
    }

    public String getDataLength() {
        return dataLength;
    }

    public ExpectedDataType getDataType() {
        return dataType;
    }

    public String getData() {
        return data;
    }

    public String getClientCRC32() {
        return clientCRC32;
    }

    public String getIp() {
        return ip;
    }

    public String getDate() {
        return date;
    }
}
