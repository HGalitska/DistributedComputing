import dao.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    private final StudentDao studentDao = new StudentDaoImpl();
    private final GroupDao groupDao = new GroupDaoImpl();

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());

        Message inputMessage;
        try {
            while ((inputMessage = (Message) in.readObject()) != null) {
                if (inputMessage.type.equals("EXIT")) {
                    out.writeObject(new Message("EXITED", null));
                    break;
                }
                out.writeObject(handleMessage(inputMessage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Object handleMessage(Message message) {
        String type = message.type;

        Message response = new Message("RESULT", null);

        switch (type) {
            case "ALL_GROUPS":
                response.data = groupDao.getAllGroups();
                break;
            case "GET_GROUP":
                response.data = groupDao.getGroup((Integer) message.data);
                break;
            case "ADD_GROUP":
                groupDao.addGroup((Group) message.data);
                response.data = "Group added.";
                break;
            case "UPDATE_GROUP":
                groupDao.updateGroup((Group) message.data);
                response.data = "Group updated.";
                break;
            case "DELETE_GROUP":
                groupDao.deleteGroup((Integer) message.data);
                response.data = "Group deleted";
                break;

            case "ALL_STUDENTS":
                response.data = studentDao.getAllStudents();
                break;
            case "GET_STUDENT":
                response.data = studentDao.getStudent((Integer) message.data);
                break;
            case "ADD_STUDENT":
                studentDao.addStudent((Student) message.data);
                response.data = "Student added.";
                break;
            case "UPDATE_STUDENT":
                studentDao.updateStudent((Student) message.data);
                response.data = "Student updated.";
                break;
            case "DELETE_STUDENT":
                studentDao.deleteStudent((Integer) message.data);
                response.data = "Student deleted";
                break;
            default:
                response.type = "ERROR";
                response.data = "Unknown request.";
        }
        return response.data;
    }

    public static void main(String[] args) {
        try {
            Server srv = new Server();
            srv.start(12345);
        } catch (IOException e) {
            System.out.println("Error during starting server.");
        }
    }
}