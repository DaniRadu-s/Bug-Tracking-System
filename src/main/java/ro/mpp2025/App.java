package ro.mpp2025;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ro.mpp2025.gui.LoginController;
import ro.mpp2025.repository.database.RepoDBBug;
import ro.mpp2025.repository.database.RepoDBProgrammer;
import ro.mpp2025.repository.database.RepoDBTester;
import ro.mpp2025.repository.interfaces.RepoInterfaceBug;
import ro.mpp2025.repository.interfaces.RepoInterfaceProgrammer;
import ro.mpp2025.repository.interfaces.RepoInterfaceTester;
import ro.mpp2025.server.Service;

import java.io.IOException;

public class App extends Application {

    private Service service;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            String url = "jdbc:postgresql://localhost:5432/BugProblem";
            String username = "postgres";
            String password = "Dani";

            RepoInterfaceTester testerRepository = new RepoDBTester(url, username, password);
            RepoInterfaceProgrammer programmerRepository = new RepoDBProgrammer(url, username, password);
            RepoInterfaceBug bugRepository = new RepoDBBug(url, username, password);
            service = new Service(testerRepository, programmerRepository, bugRepository);

            initView(stage);
            stage.setTitle("Hello!");
            stage.setWidth(800);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/views/LoginWindow.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        LoginController userController = fxmlLoader.getController();
        userController.setService(service);
        userController.setStage(primaryStage);

    }
}
