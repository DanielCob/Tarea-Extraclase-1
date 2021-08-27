import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ClientRunnable implements Runnable {

    private final BufferedReader input;
    private int datanum = 0;
    private int valorProducto;
    private int porcentaje;
    private int pesoProducto;
    public double Monto;

    public ClientRunnable(InputStream socketISR) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(socketISR));
    }

    private boolean isInt(String message) {
        try{
            int num = Integer.parseInt(message);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Error: " + message + " no es un número.");
            return false;
        }
    }

    public double getMonto() {
        return this.Monto;
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
                        this.Monto = (valorProducto * porcentaje/100) + (pesoProducto * 0.15);
                        datanum = 0;
                    }
                }
                System.out.println(msg);
                System.out.println(Monto);
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
