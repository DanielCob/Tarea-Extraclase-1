import java.lang.reflect.Field;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {

    private Socket socket;
    private PrintWriter output;
    private Scanner scanner;
    public double Monto=4;
    
    public void init(String ipAddress, int port) throws IOException {
        socket = new Socket(ipAddress, port);
        output = new PrintWriter(socket.getOutputStream(), true);
    }
    
    public void kill() throws IOException {
        output.close();
        socket.close();
    }
    
    public void sendMessage(String msg) {
        output.println(msg);
    }

    public double getMonto() {
        return Client.Monto;
    }

    public void begin(String ipAddress, int port) throws IOException {
        init(ipAddress, port);
        ClientRunnable clientRun = new ClientRunnable(socket.getInputStream());
        new Thread(clientRun).start();
        
        scanner = new Scanner(System.in);
        String usrInput;
        String name = ("Cliente");
        String msg = String.format("[%s] : ", name);
        
        while(true) {
            Monto = clientRun.getMonto();
            
            usrInput = scanner.nextLine();
            output.println(msg + usrInput);
            

            if(usrInput.equalsIgnoreCase("EXIT")) {
                output.println(String.format("[%s] left chat ...", name));
                output.println("EXIT");
                break;
            }
        }
        
        kill();
    }

	@Override
	public void run() {
        try {
            begin("localhost",8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
