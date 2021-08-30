import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Se encarga de establecer la conexión con el servidor, así como enviar mensajes y instanciar {@link ClientRunnable} para recibir mensajes.
 */
public class Client implements Runnable {
    /**
     * Almacena el Socket.
     */
    private Socket socket;
    /**
     * Almacena la información necesaria para el envio de mensajes.
     */
    private PrintWriter output;
    /**
     * Almacena el monto calculado enviado por el otro cliente.
     */
    public static double Monto;
    /**
     * Indica cuando el programa debe cerrar.
     */
    public boolean running = true;
    
    
    /** 
     * Inicia la conexión con el servidor.
     * @param ipAddress Recibe la dirección ip del servidor.
     * @param port Recibe el puerto del servidor.
     * @throws IOException
     */
    public void init(String ipAddress, int port) throws IOException {
        socket = new Socket(ipAddress, port);
        output = new PrintWriter(socket.getOutputStream(), true);
    }
    
    
    /** 
     * Finaliza la conexión con el servidor.
     * @throws IOException
     */
    public void kill() throws IOException {
        output.close();
        socket.close();
    }
    
    
    /** 
     * Facilita el envío de mensajes para la interfaz.
     * @param msg Mensaje a enviar.
     */
    public void sendMessage(String msg) {
        output.println(msg);
    }
    
    
    /** 
     * Retorna la variable Monto.
     * @return double
     */
    public double getMonto() {
        return Client.Monto;
    }
    
    
    /** 
     * Inicia la clase Client, así como la clase {@link ClientInterface}. Se mantiene en bucle esperando el Monto calculado por el otro cliente hasta cerrar el programa.
     * @param ipAddress Recibe la dirección ip del servidor.
     * @param port Recibe el puerto del servidor.
     * @param name Recibe el nombre que se le va a asignar a la instancia del cliente.
     * @throws IOException
     */
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

    /**
     * Inicia la instancia de la clase.
     */
	@Override
	public void run() {
        try {
            begin("localhost",8080, "cliente");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
