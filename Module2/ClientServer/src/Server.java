import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) throws RemoteException, MalformedURLException{
        StudyDepartmentImpl studyDepartment = new StudyDepartmentImpl();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("StudyDepartment", studyDepartment);
        System.out.println("🚀 Server started!");
    }
}