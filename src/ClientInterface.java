import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Esta clase se encarga de manejar la parte gráfica del cliente. De modo que también debe ordenarle a la clase {@link Client} cúando enviar un mensaje y que datos tendrá.
 */
public class ClientInterface extends Application {
    /**
     * Contiene la información necesaria para la ventana de la aplicación.
     */
    private Stage window;
    /**
     * Almacena la información del botón "Enviar".
     */
    private Button button0;
    /**
     * Almacena la instancia de la clase {@link Client}.
     */
    private static Client client = new Client();
    
    
    /** 
     * Contiene todos los elementos gráficos de la ventana, además indica cuando se deba enviar la información.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Cliente");
        window.setOnCloseRequest(e -> {
            window.close();
            try {
                client.running = false;
                client.kill();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        
        //Form
        TextField num0 = new TextField();
        num0.setPromptText("Valor del producto");
        TextField num1 = new TextField();
        num1.setPromptText("Porcentaje");
        TextField num2 = new TextField();
        num2.setPromptText("Peso del producto");
        
        TextArea textArea = new TextArea();

        Label label = new Label("Digite los datos del producto");
        label.setFont(new Font("Arial", 24));
        
        button0 = new Button("Enviar");
        button0.setOnAction(e -> {
            if(isInt(num0, num0.getText()) && isInt(num1, num1.getText()) && isInt(num2, num2.getText())){
                label.setFont(new Font("Arial", 24));
                label.setText("Enviando datos al otro cliente...");
                client.sendMessage(num0.getText());
                client.sendMessage(num1.getText());
                client.sendMessage(num2.getText());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                textArea.setText("El resultado calculado es: " + String.valueOf(client.getMonto()));
                System.out.println(client.getMonto() + "Interface");
                label.setFont(new Font("Arial", 24));
                label.setText("¡Resultado recibido!");
            } else {
                label.setFont(new Font("Arial", 18));
                label.setText("No digitaste números en al menos una de las casillas");
            }
            
        });

        //Layout
        HBox bottomMenu = new HBox();
        bottomMenu.getChildren().addAll(num0, num1, num2, button0);
        
        VBox centerMenu = new VBox();
        centerMenu.getChildren().addAll(textArea, label);

        BorderPane borderPane = new BorderPane();
        borderPane.setBottom(bottomMenu);
        borderPane.setCenter(centerMenu);
        
        Scene scene = new Scene(borderPane, 500, 250);
        window.setScene(scene);
        window.show();
    }

    
    /** 
     * Verifica si un string es un número entero.
     * @param input Indica la caja de texto que contiene el mensaje.
     * @param message Indica el mensaje en sí.
     * @return boolean Retorna true si es entero o false si no lo es.
     */
    private boolean isInt(TextField input, String message) {
        try{
            Integer.parseInt(input.getText());
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Error: " + message + " no es un número.");
            return false;
        }
    }
 
    /** 
     * Inicia la aplicación así como la instancia de la clase {@link Client}.
     * @param args
     */
    public static void main(String[] args) {
        new Thread(client).start();
        launch(ClientInterface.class);
    }
}