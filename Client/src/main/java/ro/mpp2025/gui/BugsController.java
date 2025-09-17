package ro.mpp2025.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ro.mpp2025.domain.*;
import ro.mpp2025.services.IObserver;
import ro.mpp2025.services.IServices;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class BugsController implements Initializable, IObserver {
    @FXML
    public TableView<Bug> newBugsTable;
    @FXML
    public TableColumn<Bug, String> nameNewColumn;
    @FXML
    public TableColumn<Bug,String> statusNewColumn;
    @FXML
    public TableView<Bug> progressBugsTable;
    @FXML
    public TableColumn<Bug, String> nameProgressColumn;
    @FXML
    public TableColumn<Bug,String> progressStatusColumn;
    @FXML
    public TableView<Bug> solvedBugsTable;
    @FXML
    public TableColumn<Bug, String> solvedNameColumn;
    @FXML
    public TableColumn<Bug,String> solvedStatusColumn;
    @FXML
    public TextField nameField;
    @FXML
    public TextField descriptionField;
    @FXML
    public TextField testerField;
    @FXML
    public TextField programmerField;
    @FXML
    public DatePicker datePickerField;
    @FXML
    public Button addButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button updateButton;
    @FXML
    public Button logoutButton;
    @FXML
    public Button solvedButton;
    private Admin admin;


    private IServices services;
    private Stage stage;
    ObservableList<Bug> newModel = FXCollections.observableArrayList();
    ObservableList<Bug> progressModel = FXCollections.observableArrayList();
    ObservableList<Bug> solvedModel = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameNewColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        statusNewColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        newBugsTable.setItems(newModel);
        nameProgressColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        progressStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        progressBugsTable.setItems(progressModel);
        solvedNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        solvedStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        solvedBugsTable.setItems(solvedModel);
    }

    public void setService(IServices service, Admin admin, Stage dialogStage) {
        this.services = service;
        this.admin = admin;
        this.stage = dialogStage;
        initModel();
    }

    public void initModel(){
        newModel.clear();
        progressModel.clear();
        solvedModel.clear();

        Iterable<Bug> bugs = services.getAllBugs();
        for (Bug bug : bugs) {
            if (bug.getStatus().equals(BugStatus.NEW)) {
                newModel.add(bug);
            } else if (bug.getStatus().equals(BugStatus.IN_PROGRESS)) {
                progressModel.add(bug);
            } else if (bug.getStatus().equals(BugStatus.SOLVED)) {
                solvedModel.add(bug);
            }
        }
    }

    public void handleAdd(ActionEvent actionEvent) {
        String name = nameField.getText();
        String description = descriptionField.getText();
        LocalDateTime date = datePickerField.getValue().atStartOfDay();
        Tester tester = services.findTesterByEmail("defaultTester");
        Programmer programmer = services.findProgrammerByEmail("defaultProgrammer");
        Bug bug = services.searchBugByName(name);
        if(tester!=null && programmer!=null && bug==null){
            services.addBug(name, description, tester, programmer, date);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informatie");
            alert.setHeaderText(null);
            alert.setContentText("Bug-ul a fost adaugat cu succes!");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informatie");
            alert.setHeaderText(null);
            alert.setContentText("Bug-ul deja exista!");
            alert.showAndWait();
        }
        nameField.clear();
        descriptionField.clear();
        initModel();
    }

    public void handleDelete(ActionEvent actionEvent) {
        Bug b = solvedBugsTable.getSelectionModel().getSelectedItem();
        if(b!=null){
            Bug bug = services.searchBugByName(b.getName());
            services.deleteBug(bug.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informatie");
            alert.setHeaderText(null);
            alert.setContentText("Bug-ul a fost sters cu succes!");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informatie");
            alert.setHeaderText(null);
            alert.setContentText("Trebuie sa selectezi un bug din cele rezolvate!");
            alert.showAndWait();
        }
        initModel();
    }

    public void handleUpdate(ActionEvent actionEvent) {
        Bug b = newBugsTable.getSelectionModel().getSelectedItem();
        if(b!=null) {
            Bug bug = services.searchBugByName(b.getName());
            String emailTester = "";
            if (bug.getTester().getEmail().equals("defaultTester"))
                emailTester = testerField.getText();
            else {
                emailTester = bug.getTester().getEmail();
            }
            bug.setStatus(BugStatus.IN_PROGRESS);
            if (bug.getProgrammer().getId() == 1) {
                Tester tester = services.findTesterByEmail(emailTester);
                Programmer programmer = services.findProgrammerByEmail(programmerField.getText());
                bug.setTester(tester);
                bug.setProgrammer(programmer);
            }
            services.updateBug(bug);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informatie");
            alert.setHeaderText(null);
            alert.setContentText("Bug-ul a fost atribuit unui programator si unui tester!");
            alert.showAndWait();
            testerField.clear();
            programmerField.clear();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informatie");
            alert.setHeaderText(null);
            alert.setContentText("Trebuie sa selectezi un bug nou aparut!");
            alert.showAndWait();
        }
        initModel();
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

    public void handleLogout(ActionEvent actionEvent) {
        this.stage.close();
    }

    public void handleSolved(ActionEvent actionEvent) {
        Bug b = progressBugsTable.getSelectionModel().getSelectedItem();
        Bug bug = services.searchBugByName(b.getName());
        bug.setStatus(BugStatus.SOLVED);
        services.updateBug(bug);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informatie");
        alert.setHeaderText(null);
        alert.setContentText("Bug-ul a fost rezolvat!");
        alert.showAndWait();
        initModel();
    }
}
