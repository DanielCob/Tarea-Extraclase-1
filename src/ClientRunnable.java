import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Se encarga de recibir los mensajes así como hacer el cálculo del monto con los datos recibidos y enviarlo devuelta al otro cliente.
 */
public class ClientRunnable implements Runnable {

    /**
     * Almacena la información necesaria para recibir mensajes.
     */
    private final BufferedReader input;
    /**
     * Almacena la información necesaria para enviar mensajes.
     */
    private final PrintWriter output;
    /**
     * Verifica la cantidad de datos recibidos para hacer el cálculo del monto.
     */
    private int datanum = 0;
    /**
     * Almacena el valor del producto para el cálculo.
     */
    private int valorProducto;
    /**
     * Almacena el porcentaje de impuestos para el cálculo.
     */
    private int porcentaje;
    /**
     * Almacena el peso del producto para el cálculo.
     */
    private int pesoProducto;
    /**
     * Almacena el monto calculado enviado por el otro cliente.
     */
    public static double montoRecibido;
    /**
     * Almacena el monto que se va a enviar al otro cliente.
     */
    public static double montoEnviado;

    /**
     * Construye la instancia de la clase.
     * @param socketISR Recibe el input de la clase {@link Client}.
     * @param socketOPW Recibe el output de la clase {@link Client}.
     * @throws IOException
     */
    public ClientRunnable(InputStream socketISR, OutputStream socketOPW) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(socketISR));
        this.output = new PrintWriter(socketOPW, true);
    }

    /**
     * Termina la conexión con el servidor.
     */
    public void kill() {
        try {
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        output.close();
    }

    
    /** 
     * Verifica si un string es un número entero.
     * @param message Mensaje a verificar.
     * @return boolean Retorna true si es entero o false si no lo es.
     */
    private boolean isInt(String message) {
        try{
            Integer.parseInt(message);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Error: " + message + " no es un número.");
            return false;
        }
    }

    
    /** 
     * Verifica si un string es un dato tipo double.
     * @param message Mensaje a verificar.
     * @return boolean Retorna true si es double o false si no lo es.
     */
    private boolean isDouble(String message) {
        try {
            Double.parseDouble(message);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    
    /** 
     * Retorna el monto calculado enviado por el otro cliente.
     * @return double
     */
    public double getMontoRecibido() {
        System.out.println(montoRecibido + " ClientRun monto recibido");
        return ClientRunnable.montoRecibido;
    }

    /**
     * Inicia la instancia de la clase, realiza las operaciones y verifica los mensajes recibidos.
     */
    @Override
    public void run() {
        try{
            while(true) {
                String msg = input.readLine();
                if(msg.equalsIgnoreCase("EXIT")) {
                    input.close();
                    break;
                } else if (isInt(msg)) {
                    if (datanum == 0) {
                        valorProducto = Integer.parseInt(msg);
                        datanum += 1;
                    } else if (datanum == 1) {
                        porcentaje = Integer.parseInt(msg);
                        datanum += 1;
                    } else if (datanum == 2) {
                        pesoProducto = Integer.parseInt(msg);
                        montoEnviado = (valorProducto*porcentaje/100.0) + (pesoProducto*0.15);
                        output.println(montoEnviado);
                        datanum = 0;
                    }
                } else if (isDouble(msg)) {
                    ClientRunnable.montoRecibido = Double.valueOf(msg);
                }
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
