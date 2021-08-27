import java.io.IOException;

public class App {

    public static void main(String[] args) {
	// write your code here
    Client client = new Client();
        try {
            client.begin("localhost",8080);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
