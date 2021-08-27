import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ClientRunnable implements Runnable {

    private final BufferedReader input;
    private final PrintWriter output;
    private int datanum = 0;
    private int valorProducto;
    private int porcentaje;
    private int pesoProducto;
    public static double montoRecibido;
    public static double montoEnviado;

    public ClientRunnable(InputStream socketISR, OutputStream socketOPW) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(socketISR));
        this.output = new PrintWriter(socketOPW, true);
    }

    public void kill() {
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        output.close();
    }

    private boolean isInt(String message) {
        try{
            Integer.parseInt(message);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Error: " + message + " no es un número.");
            return false;
        }
    }

    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public double getMontoRecibido() {
        System.out.println(montoRecibido + " ClientRun monto recibido");
        return ClientRunnable.montoRecibido;
    }

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
                    ClientRunnable.montoRecibido = Double.valueOf(msg); //revisar el tipo de dato de todos los valores
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
