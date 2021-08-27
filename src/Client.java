import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.PublicKey;

public class Client implements Runnable {

    private Socket socket;
    private PrintWriter output;
    public static double Monto;
    public boolean running = true;
    
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
    
    public void begin(String ipAddress, int port, String name) throws IOException {
        init(ipAddress, port);
        ClientRunnable clientRun = new ClientRunnable(socket.getInputStream(), socket.getOutputStream());
        new Thread(clientRun).start();
        
        String msg = String.format("[%s] : ", name);
        
        while(running) {
            Client.Monto = clientRun.getMontoRecibido();
        }
        clientRun.kill();
    }

	@Override
	public void run() {
        try {
            begin("localhost",8080, "cliente");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
