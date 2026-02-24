package controller.adminController;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.User;
import model.tableModel.SupplierTM;
import model.tableModel.UserTM;
import service.ServiceFactory;
import service.custom.UserService;
import util.ServiceType;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXComboBox cmbRole;

    @FXML
    private JFXComboBox cmbStatus;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colPassword;

    @FXML
    private TableColumn colRole;

    @FXML
    private TableColumn colStatus;

    @FXML
    private TableColumn colUsername;

    @FXML
    private TableView tblUser;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtUserId;

    @FXML
    private TextField txtUsername;

    private final UserService userService = ServiceFactory.getInstance().getServiceType(ServiceType.USER);

    private ObservableList<UserTM> masterData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueForTable();
        loadStatusFromDb();
        loadRolesFromDb();
        setUserID();
        loadTable();
        addTableSelectionListener();
        initSearchListener();
    }

    private void setCellValueForTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("userType"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadStatusFromDb() {
        try {
            List<String> status = userService.getUserStatus();
            ObservableList<String> obList = FXCollections.observableArrayList(status);
            cmbStatus.setItems(obList);

            if (!obList.isEmpty()) {
                cmbStatus.getSelectionModel().select("Active");
                cmbStatus.setDisable(true);
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Status Load Error !").show();
            throw new RuntimeException(e);
        }

    }

    private void initSearchListener() {

        FilteredList<UserTM> filteredData = new FilteredList<>(masterData, p -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredData.setPredicate(user -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String filter = newValue.toLowerCase();

                return String.valueOf(user.getId()).contains(filter) ||
                        user.getUsername().toLowerCase().contains(filter) ||
                        user.getUserType().toLowerCase().contains(filter) ||
                        user.getStatus().toLowerCase().contains(filter);
            });
        });

        SortedList<UserTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblUser.comparatorProperty());

        tblUser.setItems(sortedData);
    }

    private void loadRolesFromDb() {
        try {
            List<String> roles = userService.getUserRole();
            ObservableList<String> obList = FXCollections.observableArrayList(roles);
            cmbRole.setItems(obList);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Role Load Error!").show();
        }
    }

    private void setUserID() {
        try {
            txtUserId.setText(userService.getUserId());
            txtUserId.setStyle("-fx-text-fill: black; -fx-opacity: 0.8;");
            txtUserId.setDisable(true);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "ID Generate Error !").show();
            e.printStackTrace();
        }
    }

    private boolean isValid() {
        if (txtUsername.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Username is required!").show();
            txtUsername.requestFocus();
            return false;
        } else if (txtUsername.getText().trim().length() < 3) {
            new Alert(Alert.AlertType.WARNING, "Username must be at least 3 characters long!").show();
            txtUsername.requestFocus();
            return false;
        }

        if (txtPassword.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Password is required!").show();
            txtPassword.requestFocus();
            return false;
        }

        if (cmbRole.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a User Role (Admin/Cashier)!").show();
            cmbRole.requestFocus();
            return false;
        }

        if (cmbStatus.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a Status!").show();
            cmbStatus.requestFocus();
            return false;
        }

        return true;
    }

    private void loadTable() {

        Task<List<User>> loadTask = new Task<>() {
            @Override
            protected List<User> call() throws Exception {
                return userService.getAllUser();
            }
        };

        loadTask.setOnSucceeded(event -> {

            List<User> allUsers = loadTask.getValue();

            masterData.clear();

            for (User user : allUsers) {
                masterData.add(new UserTM(
                        user.getId(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getUserType(),
                        user.getStatus()
                ));
            }

        });

        loadTask.setOnFailed(event -> {
            Throwable e = loadTask.getException();
            new Alert(Alert.AlertType.ERROR,
                    "Error Loading Table: " + e.getMessage()).show();
        });

        new Thread(loadTask).start();
    }

    private boolean isDataNotChanged(UserTM tm) {

        return tm.getUsername().equals(txtUsername.getText().trim()) &&
                tm.getPassword().equals(txtPassword.getText().trim()) &&
                tm.getUserType().equals(cmbRole.getValue().toString()) &&
                tm.getStatus().equals(cmbStatus.getValue().toString());
    }


    private void clear() {
        txtUsername.clear();
        txtPassword.clear();
        cmbRole.getSelectionModel().clearSelection();
        cmbStatus.getSelectionModel().clearSelection();
        cmbStatus.getSelectionModel().select("Active");
        cmbStatus.setDisable(true);
        txtPassword.setDisable(false);
        tblUser.getSelectionModel().clearSelection();

        setUserID();
        btnAdd.setDisable(false);
    }

    private void addTableSelectionListener() {

        tblUser.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {

                    if (newValue != null) {
                        setDataToFields((UserTM) newValue);
                    }
                }
        );
    }

    private void setDataToFields(UserTM userTM) {

        txtUserId.setText(String.valueOf(userTM.getId()));
        txtUsername.setText(userTM.getUsername());
        txtPassword.setText(userTM.getPassword());

        cmbRole.setValue(userTM.getUserType());
        cmbStatus.setValue(userTM.getStatus());

        cmbStatus.setDisable(false);
        btnAdd.setDisable(true);
        txtPassword.setDisable(true);
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {

        if (!isValid()) {
            return;
        }

        int id = Integer.parseInt(txtUserId.getText());
        String userName = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String userRole = cmbRole.getSelectionModel().getSelectedItem().toString();
        String status = (cmbStatus.getSelectionModel().getSelectedItem() != null) ? cmbStatus.getSelectionModel().getSelectedItem().toString() : "Active";

        User user = new User(id, userName, password, userRole, status);

        try {
            if (userService.addUser(user)) {
                new Alert(Alert.AlertType.INFORMATION, "User Added Successfully").show();
                loadTable();
                setUserID();
                clear();
            } else {
                new Alert(Alert.AlertType.ERROR, "User Added Failed").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            throw new RuntimeException(e);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            throw new RuntimeException(e);
        }

    }


    @FXML
    void btnClearOnAction(ActionEvent event) {
        clear();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!isValid()) return;

        UserTM selectedItem = (UserTM) tblUser.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a user to update!").show();
            return;
        }

        if (isDataNotChanged(selectedItem)) {
            new Alert(Alert.AlertType.INFORMATION, "No changes detected!").show();
            return;
        }

        User user = new User(
                Integer.parseInt(txtUserId.getText()),
                txtUsername.getText().trim(),
                txtPassword.getText().trim(),
                cmbRole.getValue().toString(),
                cmbStatus.getValue().toString()
        );

        try {
            if (userService.updateUser(user)) {

                new Alert(Alert.AlertType.INFORMATION, "User Updated Successfully!").show();

                loadTable();
                clear();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

}

