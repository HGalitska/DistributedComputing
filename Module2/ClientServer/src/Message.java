import java.io.Serializable;

public class Message implements Serializable {
    String type;
    Object data;

    public Message(String type, Object data) {
        this.type = type;
        this.data = data;
    }
    public Message(String type) {
        this.type = type;
    }
}
