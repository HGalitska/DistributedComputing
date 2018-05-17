import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket sock = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public Client(String ip, int port) throws IOException {
        sock = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new PrintWriter(sock.getOutputStream(), true);
    }

    private int sendQuery(int operation, int value1, int value2) throws IOException {
        String query = operation + "#" + value1 + "#" + value2;
        out.println(query);
        String response = in.readLine();
        String[] fields = response.split("#");
        if (fields.length != 2)
            throw new IOException("Invalid response from server");
        try {
            // Код завершения
            int comp_code = Integer.valueOf(fields[0]);
            // Результат операции
            int result = Integer.valueOf(fields[1]);
            if (comp_code == 0)
                return result;
            else
                throw new IOException(
                        "Error while processing query");
        } catch (NumberFormatException e) {
            throw new IOException("Invalid response from server");
        }
    }

    public void disconnect() throws IOException {
        sock.close();
    }

    // главный метод
    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 12345);

            client.disconnect();
        } catch (IOException e) {
            System.out.println("Возникла ошибка");
            e.printStackTrace();
        }
    }
}
