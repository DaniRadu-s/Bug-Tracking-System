package ro.mpp2025.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.mpp2025.domain.Admin;
import ro.mpp2025.domain.Bug;
import ro.mpp2025.services.IObserver;
import ro.mpp2025.services.IServices;

import java.io.IOException;

public class AdminController{
    @FXML
    public Button usersButton;
    @FXML
    public Button manageBugs;
    @FXML
    public Button logoutButton;
    private IServices service;
    private Stage stage;
    private BugsController bugsController;
    private Admin admin;
    private Stage dialogStage;
    private LoginController loginController;
    public void setService(IServices service, Admin admin, Stage stage, BugsController bugsController, Stage dialogStage, LoginController loginController) {
        this.service = service;
        this.stage = stage;
        this.admin = admin;
        this.bugsController = bugsController;
        this.dialogStage = dialogStage;
        this.loginController = loginController;
    }

    public void handleUsers(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/UserWindow.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Manage");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            UsersController registerController = loader.getController();
            registerController.setService(service, dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleBugs(ActionEvent actionEvent) {
        bugsController.setService(service,admin,dialogStage);
        dialogStage.show();
    }

    public void handleLogout(ActionEvent actionEvent) {
        this.service.logoutAdmin(admin, bugsController);
        loginController.setAdminLoggedIn(false);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informatie");
        alert.setHeaderText(null);
        alert.setContentText(admin.getEmail() + " a fost deconectat!");
        alert.showAndWait();
        this.stage.close();
    }
}
