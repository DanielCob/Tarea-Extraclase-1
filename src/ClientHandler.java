import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * La clase se encarga de manejar la comunicación entre las instancias de clientes y almacenar un record de los mensajes.
 */
public class ClientHandler extends Thread {
    /**
     * Almacena el Socket de los clientes.
     */
    private final Socket clientSocket;
    /**
     * Almacena los clientes conectados al servidor.
     */
    private final ArrayList<ClientHandler> clientsList;
    /**
     * Almacena la información necesaria para enviar mensajes.
     */
    private PrintWriter output;
    /**
     * Almacena la información necesaria para recibir mensajes.
     */
    private BufferedReader input;
    /**
     * Constructor de la clase
     * @param socket Recibe el socket del cliente.
     * @param clientsList Recibe la lista de clientes.
     */
    public ClientHandler(
            Socket socket,
            ArrayList<ClientHandler> clientsList
    ) {
        this.clientSocket = socket;
        this.clientsList = clientsList;
    }

    
    /** 
     * Inicia la conexión con los clientes.
     * @throws IOException
     */
    private void init() throws IOException {
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        output = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    
    /** 
     * Envía el mensaje del cliente a los demás clientes conectados al servidor.
     * @param msg Mensaje a enviar.
     */
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

    
    /** 
     * Finaliza la conexión con el cliente.
     * @throws IOException
     */
    private void kill() throws IOException {
        input.close();
        output.close();
        clientSocket.close();
    }

    /**
     * Inicia la instancia de la clase.
     */
    @Override
    public void run() {
        try {
            init();
            while (true) {
                String msg = input.readLine();
                if (msg.equalsIgnoreCase("EXIT")) {
                    break;
                }
                System.out.println("log: " + msg);
                notifyAllClients(msg);
            }
            kill();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
