import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Se encarga de comunicar los clientes y proveer un espacio en el que se puedan conectar.
 */
public class Server {
    /**
     * Almacena el Socket del servidor.
     */
    private ServerSocket serverSocket;
    /**
     * Almacena los clientes conectados.
     */
    private final ArrayList<ClientHandler> clientsList;
    
    /**
     * Crea el array de clientes.
     */
    public Server() {
        clientsList = new ArrayList<ClientHandler>();
    }

    
    /**
     * Inicia el servidor y acepta los clientes que soliciten unirse. 
     * @param port Puerto en el cual se inicia el servidor.
     * @throws IOException
     */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port: " + port + " ...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket, clientsList);
            clientsList.add(clientHandler);
            clientHandler.start();
            System.out.println("New client connected! ...");
        }
    }

    
    /** 
     * Cierra la conexión del servidor.
     * @throws IOException
     */
    public void stop() throws IOException {
        serverSocket.close();
    }
    
    /** 
     * Inicia la instancia de la clase servidor.
     * @param args
     */
    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
