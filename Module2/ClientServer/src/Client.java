import java.io.*;
import java.net.Socket;

public class Client {
    private Socket sock;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(String ip, int port) throws IOException {
        sock = new Socket(ip, port);
        in = new ObjectInputStream(sock.getInputStream());
        out = new ObjectOutputStream(sock.getOutputStream());
    }

    public Object sendMessage(Message message) {
        Object response = null;
        try {
            out.writeObject(message);
            response = in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
        sock.close();
    }
}
