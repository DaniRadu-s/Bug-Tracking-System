package ro.mpp2025.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ro.mpp2025.domain.Bug;
import ro.mpp2025.domain.Programmer;
import ro.mpp2025.domain.Tester;
import ro.mpp2025.services.IObserver;
import ro.mpp2025.services.IServices;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class TesterController implements Initializable, IObserver {

    @FXML
    public Text usernameLabel;
    @FXML
    public Button testerClose;
    @FXML
    public TextField nameField;
    @FXML
    public TextField descriptionField;
    @FXML
    public TextField statusField;
    @FXML
    public Button reportButton;
    @FXML
    public TableView<Bug> bugsTable;
    @FXML
    public TableColumn<Bug, String> nameColumn;
    @FXML
    public TableColumn<Bug, String> descriptionColumn;
    @FXML
    public TableColumn<Bug, String> deadlineColumn;
    @FXML
    public TableColumn<Bug, String> statusColumn;
    @FXML
    public DatePicker datePicker;
    private IServices service;
    private Stage stage;
    private Tester tester;
    private LoginController loginController;
    ObservableList<Bug> all = FXCollections.observableArrayList();

    public void setService(IServices service, Tester tester, Stage stage, LoginController loginController) {
        this.service = service;
        this.tester = tester;
        this.stage = stage;
        this.loginController = loginController;
        usernameLabel.setText(tester.getUsername() + " " + tester.getId());
        initModel();
    }

    public void handleClose(ActionEvent actionEvent) {
        service.logoutTester(tester,this);
        loginController.setTesterLoggedIn(false);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informatie");
        alert.setHeaderText(null);
        alert.setContentText(tester.getUsername() + " a fost deconectat!");
        alert.showAndWait();
        this.stage.close();
    }

    public void initModel(){
        all.clear();
        Iterable<Bug> bugList = service.getAllBugs();
        for(Bug bug : bugList){
            all.add(bug);
        }
    }

    public void handleReport(ActionEvent actionEvent) {
        String name = nameField.getText();
        String description = descriptionField.getText();
        LocalDateTime date = datePicker.getValue().atStartOfDay();
        Bug bug = service.searchBugByName(name);
        if(bug == null) {
            Programmer p = service.findProgrammerByEmail("defaultProgrammer");
            service.addBug(name, description, tester, p, date);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informatie");
            alert.setHeaderText(null);
            alert.setContentText("Bug ul a fost inregistrat cu succes!");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informatie");
            alert.setHeaderText(null);
            alert.setContentText("Bug ul deja este inregistrat!");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<Bug, String>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Bug, String>("description"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<Bug, String>("date"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Bug, String>("status"));
        bugsTable.setItems(all);
    }

    @Override
    public void updatedBug(Bug data) {
        Platform.runLater(() -> {
            System.out.println("Bug actualizat: " + data.getName());
            initModel();
        });
    }

    @Override
    public void addedBug(Bug data) {
        Platform.runLater(() -> {
            System.out.println("Bug actualizat: " + data.getName());
            initModel();
        });
    }

    @Override
    public void deletedBug(Bug data) {
        Platform.runLater(() -> {
            System.out.println("BUG actualizat: " + data.getName());
            initModel();
        });
    }
}
