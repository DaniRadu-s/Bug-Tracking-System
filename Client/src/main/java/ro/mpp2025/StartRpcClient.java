package ro.mpp2025;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.mpp2025.gui.LoginController;
import ro.mpp2025.network.rpcprotocol.TransportServicesRpcProxy;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClient extends Application {
    private static final int DEFAULT_PORT = 55555;
    private static final String DEFAULT_SERVER = "localhost";

    @Override
    public void start(Stage primaryStage) throws IOException {
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClient.class.getResourceAsStream("/transportclient.properties"));
        } catch (IOException e) {
            System.err.println("Cannot load client properties: " + e.getMessage());
            return;
        }

        String serverIP = clientProps.getProperty("transport.server.host", DEFAULT_SERVER);
        int serverPort = DEFAULT_PORT;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("transport.server.port"));
        } catch (NumberFormatException ex) {
            System.out.println("Invalid port number. Using default: " + DEFAULT_PORT);
        }

        System.out.println("Using server IP: " + serverIP);
        System.out.println("Using server port: " + serverPort);

        var server = new TransportServicesRpcProxy(serverIP, serverPort);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/LoginWindow.fxml"));
        Parent root = fxmlLoader.load();
        LoginController controller = fxmlLoader.getController();
        controller.setService(server);
        controller.setStage(primaryStage);

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 590, 400));
        primaryStage.show();
    }
}
