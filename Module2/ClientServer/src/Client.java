import java.io.*;
import java.net.Socket;

public class Client {
    private Socket sock;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(String ip, int port) throws IOException {
        sock = new Socket(ip, port);
        out = new ObjectOutputStream(sock.getOutputStream());
        in = new ObjectInputStream(sock.getInputStream());
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
