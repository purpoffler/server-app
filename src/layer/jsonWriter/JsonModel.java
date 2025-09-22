package layer.jsonWriter;

public class JsonModel {
    private String date;
    private String ip;
    private String data;

    public JsonModel(String date, String ip, String data) {
        this.date = date;
        this.ip = ip;
        this.data = data;
    }

    public String getDate() {
        return date;
    }

    public String getIp() {
        return ip;
    }

    public String getData() {
        return data;
    }
}
