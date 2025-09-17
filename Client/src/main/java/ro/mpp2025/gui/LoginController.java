package ro.mpp2025.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.mpp2025.domain.Admin;
import ro.mpp2025.domain.Hashing;
import ro.mpp2025.domain.Programmer;
import ro.mpp2025.domain.Tester;
import ro.mpp2025.services.IServices;

import java.io.IOException;

public class LoginController {
    @FXML
    public Button loginButton;
    @FXML
    public CheckBox testerCheck;
    @FXML
    public CheckBox programmerCheck;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField emailField;
    @FXML
    public Button exitButton;
    @FXML
    public CheckBox adminCheck;

    private IServices service;
    private Stage stage;
    private boolean testerLoggedIn = false;
    private boolean programmerLoggedIn = false;
    private boolean adminLoggedIn = false;


    public void setService(IServices service) {
        this.service = service;
    }

    public void setStage(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public void setTesterLoggedIn(boolean value) {
        this.testerLoggedIn = value;
    }

    public void setProgrammerLoggedIn(boolean value) {
        this.programmerLoggedIn = value;
    }

    public void setAdminLoggedIn(boolean value) {
        this.adminLoggedIn = value;
    }


    public void handleLogin(ActionEvent actionEvent) throws Exception {
        String email = emailField.getText();
        String password = Hashing.hashPassword(passwordField.getText());
        if(!testerCheck.isSelected() && !programmerCheck.isSelected() && adminCheck.isSelected()) {
            Admin admin = service.findAdminByEmail(email);
            if(admin == null) {
                MessageAlert.showErrorMessage(null,"Nu exista un admin!");
            }
            else {
                if(!password.equals(admin.getPassword())) {
                    MessageAlert.showErrorMessage(null,"Parola este incorecta!");
                }
                else {
                    if (adminLoggedIn) {
                        MessageAlert.showErrorMessage(null, "Admin already logged in!");
                        return;
                    }
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/views/AdminWindow.fxml"));

                        AnchorPane root = (AnchorPane) loader.load();
                        Stage dialogStage = new Stage();
                        dialogStage.setTitle("Admin");
                        dialogStage.initModality(Modality.WINDOW_MODAL);
                        Scene scene = new Scene(root);
                        dialogStage.setScene(scene);

                        AdminController registerController = loader.getController();
                        adminLoggedIn = true;
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Informatie");
                        alert.setHeaderText(null);
                        alert.setContentText("a@administrator.com a fost conectat!");
                        alert.showAndWait();
                        FXMLLoader loader1 = new FXMLLoader();
                        loader1.setLocation(getClass().getResource("/views/BugsWindow.fxml"));

                        AnchorPane root1 = (AnchorPane) loader1.load();

                        // Create the dialog Stage.
                        Stage dialogStage1 = new Stage();
                        dialogStage1.setTitle("Manage");
                        dialogStage1.initModality(Modality.WINDOW_MODAL);
                        //dialogStage.initOwner(primaryStage);
                        Scene scene1 = new Scene(root1);
                        dialogStage1.setScene(scene1);

                        BugsController registerController1 = loader1.getController();
                        this.service.loginAdmin(admin, registerController1);
                        registerController.setService(service, admin, dialogStage, registerController1, dialogStage1, this);

                        dialogStage.show();

                    }catch (RuntimeException e) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Eroare");
                        alert.setHeaderText("Authentication failure");
                        alert.setContentText(e.getMessage());
                        System.out.println(e.getMessage());
                        alert.showAndWait();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else
            if (testerCheck.isSelected() && programmerCheck.isSelected()) {
                MessageAlert.showErrorMessage(null, "Trebuie sa selectezi doar o optiune!");
            } else if (testerCheck.isSelected()) {
                Tester tester = service.findTesterByEmail(email);
                if (tester == null) {
                    MessageAlert.showErrorMessage(null, "Nu exista un tester!");
                } else {
                    if (!password.equals(tester.getPassword())) {
                        MessageAlert.showErrorMessage(null, "Parola este incorecta!");
                    } else {
                        if (testerLoggedIn) {
                            MessageAlert.showErrorMessage(null, "Tester already logged in!");
                            return;
                        }
                        try {

                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/views/TesterWindow.fxml"));
                            AnchorPane root = loader.load();


                            TesterController registerController = loader.getController();


                            service.loginTester(tester, registerController);
                            testerLoggedIn = true;
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Informatie");
                            alert.setHeaderText(null);
                            alert.setContentText(tester.getUsername() + " a fost conectat!");
                            alert.showAndWait();


                            Stage dialogStage = new Stage();
                            dialogStage.setTitle("Tester");
                            dialogStage.initModality(Modality.WINDOW_MODAL);
                            Scene scene = new Scene(root);
                            dialogStage.setScene(scene);

                            registerController.setService(service, tester, dialogStage, this);
                            dialogStage.show();

                        } catch (RuntimeException e) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Eroare");
                            alert.setHeaderText("Authentication failure");
                            alert.setContentText(e.getMessage());
                            System.out.println(e.getMessage());
                            alert.showAndWait();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            }
        else if(programmerCheck.isSelected()) {
            Programmer programmer= service.findProgrammerByEmail(email);
            if(programmer == null) {
                MessageAlert.showErrorMessage(null,"Nu exista un programmer!");
            }
            else {
                if(!password.equals(programmer.getPassword())) {
                    MessageAlert.showErrorMessage(null,"Parola este incorecta!");
                }
                else {
                    if(programmerCheck.isSelected() && programmerLoggedIn) {
                        MessageAlert.showErrorMessage(null, "Programmer already logged in!");
                        return;
                    }
                    try {

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/views/ProgrammerWindow.fxml"));
                        AnchorPane root = loader.load();


                        ProgrammerController registerController = loader.getController();


                        service.loginProgrammer(programmer, registerController);
                        programmerLoggedIn = true;
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Informatie");
                        alert.setHeaderText(null);
                        alert.setContentText(programmer.getUsername() + " a fost conectat!");
                        alert.showAndWait();


                        Stage dialogStage = new Stage();
                        dialogStage.setTitle("Programmer");
                        dialogStage.initModality(Modality.WINDOW_MODAL);
                        Scene scene = new Scene(root);
                        dialogStage.setScene(scene);

                        registerController.setService(service, programmer, dialogStage,this);
                        dialogStage.show();

                    } catch (RuntimeException e) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Eroare");
                        alert.setHeaderText("Authentication failure");
                        alert.setContentText(e.getMessage());
                        System.out.println(e.getMessage());
                        alert.showAndWait();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informație");
            alert.setHeaderText(null);
            alert.setContentText("Selectează ceva!");
            alert.showAndWait();
        }
    }


    public void handleClose(ActionEvent actionEvent) {
        this.stage.close();
    }
}
