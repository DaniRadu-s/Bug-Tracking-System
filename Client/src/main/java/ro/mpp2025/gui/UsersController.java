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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UsersController implements Initializable,IObserver {
    @FXML
    public TableView<Tester> testerTable;
    @FXML
    public TableColumn<Tester,String> testerEmailColumn;
    @FXML
    public TableColumn<Tester,String> testerUsernameColumn;
    @FXML
    public TableView<Programmer> programmerTable;
    @FXML
    public TableColumn<Programmer,String> programmerEmailColumn;
    @FXML
    public TableColumn<Programmer,String> programmerUsernameColumn;
    @FXML
    public Button addButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button updateButton;
    @FXML
    public TextField emailField;
    @FXML
    public TextField usernameField;
    @FXML
    public TextField passwordField;
    @FXML
    public TextField nameField;
    @FXML
    public CheckBox testerCheck;
    @FXML
    public CheckBox programmerCheck;
    private IServices service;
    private Stage stage;
    ObservableList<Tester> testerModel = FXCollections.observableArrayList();
    ObservableList<Programmer> programmerModel = FXCollections.observableArrayList();
    public void setService(IServices service, Stage stage) {
        this.service = service;
        this.stage = stage;
        initModel();
    }

    public void initModel(){
        testerModel.clear();
        programmerModel.clear();
        Iterable<Tester> Itesters = service.getAllTesters();
        Iterable<Programmer> Iprogrammers = service.getAllProgrammers();
        List<Tester> testers = new ArrayList<>();
        for(Tester tester : Itesters){
            if(!tester.getEmail().equals("defaultTester"))
                testers.add(tester);
        }
        List<Programmer> programmers = new ArrayList<>();
        for(Programmer programmer : Iprogrammers){
            if(!programmer.getEmail().equals("defaultProgrammer"))
                programmers.add(programmer);
        }
        testerModel.addAll(testers);
        programmerModel.addAll(programmers);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        testerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        testerUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        testerTable.setItems(testerModel);
        programmerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        programmerUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        programmerTable.setItems(programmerModel);
    }

    public void handleAdd(ActionEvent actionEvent) throws Exception {
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = Hashing.hashPassword(passwordField.getText());
        String name = nameField.getText();
        if(testerCheck.isSelected() && !programmerCheck.isSelected()){
            Tester tester = service.findTesterByEmail(email);
            if(tester == null && emailValidation(email)){
                service.addTester(new Tester(username, password, email, name));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informatie");
                alert.setHeaderText(null);
                alert.setContentText("Testerul a fost adaugat!");
                alert.showAndWait();
            }
            else if(tester != null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informație");
                alert.setHeaderText(null);
                alert.setContentText("Testerul deja exista!");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informație");
                alert.setHeaderText(null);
                alert.setContentText("Adresa este invalida!");
                alert.showAndWait();
            }
        }
        else if(programmerCheck.isSelected() && !testerCheck.isSelected()){
            Programmer programmer = service.findProgrammerByEmail(email);
            if(programmer == null && emailValidation(email)){
                service.addProgrammer(new Programmer(username, password, email, name));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informatie");
                alert.setHeaderText(null);
                alert.setContentText("Programatorul a fost adaugat!");
                alert.showAndWait();
            }
            else if(programmer != null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informatie");
                alert.setHeaderText(null);
                alert.setContentText("Programmer deja exista!");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informatie");
                alert.setHeaderText(null);
                alert.setContentText("Adresa este invalida!");
                alert.showAndWait();
            }
        }
        else if(testerCheck.isSelected() && programmerCheck.isSelected()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informatie");
            alert.setHeaderText(null);
            alert.setContentText("Selecteaza doar o optiune!");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informatie");
            alert.setHeaderText(null);
            alert.setContentText("Selecteaza ceva!");
            alert.showAndWait();
        }
        initModel();
    }

    public void handleUpdate(ActionEvent actionEvent) throws Exception {
        Tester tester = testerTable.getSelectionModel().getSelectedItem();
        Programmer programmer = programmerTable.getSelectionModel().getSelectedItem();
        if(tester != null && programmer == null){
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = Hashing.hashPassword(passwordField.getText());
            String name = nameField.getText();
            Tester testez = service.findTesterByEmail(email);
            if((testez == null || email.equals(testez.getEmail()))&&emailValidation(email)) {
                Tester t = new Tester(username, password, email, name);
                t.setId(tester.getId());
                service.updateTester(t);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informatie");
                alert.setHeaderText(null);
                alert.setContentText("Testerul a fost actualizat");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informatie");
                alert.setHeaderText(null);
                alert.setContentText("Emailul exista deja la alt tester!");
                alert.showAndWait();
            }
        }
        else if(tester == null && programmer != null){
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = Hashing.hashPassword(passwordField.getText());
            String name = nameField.getText();
            Programmer programez = service.findProgrammerByEmail(email);
            if((programez == null || email.equals(programez.getEmail())) && emailValidation(email)) {
                Programmer p = new Programmer(username, password, email, name);
                p.setId(programmer.getId());
                service.updateProgrammer(p);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informatie");
                alert.setHeaderText(null);
                alert.setContentText("Programatorul a fost actualizat");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informatie");
                alert.setHeaderText(null);
                alert.setContentText("Emailul exista deja la alt programator!");
                alert.showAndWait();
            }
        }
        initModel();
    }

    public void handleDelete(ActionEvent actionEvent) {
        Tester tester = testerTable.getSelectionModel().getSelectedItem();
        Programmer programmer = programmerTable.getSelectionModel().getSelectedItem();
        boolean test = true;
        boolean program = true;
        Iterable<Bug> bugs = service.getAllBugs();
        for(Bug bug : bugs){
            if(tester != null && bug.getTester().getEmail().equals(tester.getEmail()) && !bug.getStatus().equals(BugStatus.SOLVED)){
                test = false;
            }
            if(programmer != null && bug.getProgrammer().getEmail().equals(programmer.getEmail()) && !bug.getStatus().equals(BugStatus.SOLVED)){
                program = false;
            }
        }
        if(tester != null && programmer == null&& test){
            Tester t = service.findTesterByEmail(tester.getEmail());
            service.deleteTester(t.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informatie");
            alert.setHeaderText(null);
            alert.setContentText("Testerul a fost sters");
            alert.showAndWait();
            }
        else if(tester == null && programmer != null && program){
            Programmer p = service.findProgrammerByEmail(programmer.getEmail());
            service.deleteProgrammer(p.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informatie");
            alert.setHeaderText(null);
            alert.setContentText("Programatorul a fost sters");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informatie");
            alert.setHeaderText(null);
            alert.setContentText("Testerul sau programatorul nu pot fi stersi deoarece inca mai au un bug care nu este rezolvat!");
            alert.showAndWait();
        }
        initModel();
    }

    private boolean emailValidation(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }


    @Override
    public void updatedBug(Bug data) {
        Platform.runLater(()->{
            initModel();
        });
    }

    @Override
    public void addedBug(Bug data) {

    }

    @Override
    public void deletedBug(Bug data) {

    }
}
