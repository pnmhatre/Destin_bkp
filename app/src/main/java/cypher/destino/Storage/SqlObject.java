package cypher.destino.Storage;

/**
 * Created by karhack on 28/11/16.
 */
public class SqlObject {
    public String name;
    public String address;
    public String latitude;
    public String longitude;
    public String radius;
    public String latitudeCurrent;
    public String longitudeCurrent;
    public String timestamp;
    public String status;
    public String action;
    public String hits;
    public  int id;

    public SqlObject(){

    }
    public SqlObject(int id){
        this.id = id;
    }

    public SqlObject(int id, String name, String address, String latitude, String longitude,String radius, String latitudeCurrent, String longitudeCurrent, String timestamp, String status, String action, String hits ) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.latitudeCurrent = latitudeCurrent;
        this.longitudeCurrent = longitudeCurrent;
        this.timestamp = timestamp;
        this.status = status;
        this.action = action;
        this.id = id;
        this.radius = radius;
        this.hits = hits;
    }
}
