package ro.mpp2025.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ro.mpp2025.domain.Bug;
import ro.mpp2025.domain.BugStatus;
import ro.mpp2025.domain.Programmer;
import ro.mpp2025.domain.Tester;

import ro.mpp2025.services.IObserver;
import ro.mpp2025.services.IServices;

import java.net.URL;
import java.util.ResourceBundle;

public class ProgrammerController implements Initializable,IObserver {
    @FXML
    public Button exitButton;
    @FXML
    public Text usernameLabel;
    @FXML
    public Button eliminateBugButton;
    @FXML
    public TableView<Bug> assignedTable;
    @FXML
    public TableView<Bug> bugsTable;
    @FXML
    public TableColumn<Bug,String> assignedNameColumn;
    @FXML
    public TableColumn<Bug,String> assignedStatusColumn;
    @FXML
    public TableColumn<Bug,String> nameColumn;
    @FXML
    public TableColumn<Bug,String> statusColumn;
    @FXML
    public TableColumn<Bug,String> descriptionColumn;
    @FXML
    public TableColumn<Bug,String> deadlineColumn;
    private IServices service;
    private Stage stage;
    private Programmer programmer;
    private LoginController loginController;
    ObservableList<Bug> assigned = FXCollections.observableArrayList();
    ObservableList<Bug> all = FXCollections.observableArrayList();

    public void setService(IServices service, Programmer programmer, Stage stage, LoginController loginController) {
        this.service = service;
        this.programmer = programmer;
        this.stage = stage;
        this.loginController = loginController;
        usernameLabel.setText(programmer.getUsername() + " " + programmer.getId());
        initModel();
    }

    public void handleClose(ActionEvent actionEvent) {
        service.logoutProgrammer(programmer,this);
        loginController.setProgrammerLoggedIn(false);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informatie");
        alert.setHeaderText(null);
        alert.setContentText(programmer.getUsername() + " a fost deconectat!");
        alert.showAndWait();
        this.stage.close();
    }

    public void handleEliminate(ActionEvent actionEvent) {
        Bug b = assignedTable.getSelectionModel().getSelectedItem();
        Bug bug = service.searchBugByName(b.getName());
        bug.setStatus(BugStatus.SOLVED);
        service.updateBug(bug);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informatie");
        alert.setHeaderText(null);
        alert.setContentText("Bug ul a fost rezolvat!");
        alert.showAndWait();
    }

    public void initModel(){
        all.clear();
        assigned.clear();
        Iterable<Bug> bugList = service.getAllBugs();
        for(Bug bug : bugList){
            if(bug.getStatus().equals(BugStatus.IN_PROGRESS))
            {
                all.add(bug);
                if(bug.getProgrammer().getEmail().equals(programmer.getEmail())){
                    assigned.add(bug);
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assignedNameColumn.setCellValueFactory(new PropertyValueFactory<Bug,String>("name"));
        assignedStatusColumn.setCellValueFactory(new PropertyValueFactory<Bug,String>("status"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Bug,String>("name"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Bug,String>("status"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Bug,String>("description"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<Bug,String>("date"));
        assignedTable.setItems(assigned);
        bugsTable.setItems(all);
    }

    @Override
    public void updatedBug(Bug data) {
        Platform.runLater(() -> {
            System.out.println("BUG actualizat: " + data.getName());
            initModel();
        });
    }

    @Override
    public void addedBug(Bug data) {
        Platform.runLater(() -> {
            System.out.println("BUG actualizat: " + data.getName());
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
