import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {

    private final Socket clientSocket;
    private final ArrayList<ClientHandler> clientsList;
    private PrintWriter output;
    private BufferedReader input;

    public ClientHandler(
            Socket socket,
            ArrayList<ClientHandler> clientsList
    ) {
        this.clientSocket = socket;
        this.clientsList = clientsList;
    }

    private void init() throws IOException {
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        output = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    private void notifyAllClients(String msg) {
        if (msg.equalsIgnoreCase("EXIT")) {
            this.output.println("EXIT");
        } else {
            for (ClientHandler client : clientsList) {
                if (!client.equals(this))
                    client.output.println(msg);
            }
        }
    }

    private void kill() throws IOException {
        input.close();
        output.close();
        clientSocket.close();
    }

    @Override
    public void run() {
        try {
            init();
            while (true) {
                String msg = input.readLine();
                if (msg.equalsIgnoreCase("EXIT")) {
                    break;
                }
                // Your logic
                System.out.println("log: "+msg);
                notifyAllClients(msg);
            }
            kill();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
