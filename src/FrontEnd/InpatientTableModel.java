package FrontEnd;

import Controller.InpatientDAO;
import DataModel.Patient;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Optional;

public class InpatientTableModel {

    private static TableView<Patient> tableView = new TableView<>();
    private static int id = 0;//if it is non static what happened

    private static String firstName;
    private static String lastName;
    private static Date birthDate;
    private static Date admissionDate;
    private static String gender;
    private static String address;
    private static int roomNumber;
    private static String phoneNumber;

    public static void setId(int id) {
        InpatientTableModel.id = id;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        InpatientTableModel.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        InpatientTableModel.lastName = lastName;
    }

    public static String getBirthDate() {
        String date = birthDate.toString();
        return date;
    }

    public static void setBirthDate(java.sql.Date birthDate) {
        InpatientTableModel.birthDate = birthDate;
    }

    public static String getAdmissionDate() {
        String date = admissionDate.toString();
        return date;
    }

    public static void setAdmissionDate(Date admissionDate) {
        InpatientTableModel.admissionDate = admissionDate;
    }

    public static String getGender() {
        return gender;
    }

    public static void setGender(String gender) {
        InpatientTableModel.gender = gender;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        InpatientTableModel.address = address;
    }

    public static int getRoomNumber() {
        return roomNumber;
    }

    public static void setRoomNumber(int roomNumber) {
        InpatientTableModel.roomNumber = roomNumber;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static void setPhoneNumber(String phoneNumber) {
        InpatientTableModel.phoneNumber = phoneNumber;
    }

    public static int getId() {
        return id;
    }

    public TableView<Patient> table() {
        tableView.setPrefHeight(Integer.MAX_VALUE);
        tableView.setPlaceholder(Menu_scene.formattingLabel("Empty List", Color.rgb(255, 0, 128), 35));

        TableColumn<Patient, Integer> id = new TableColumn<>("ID");
        id.setPrefWidth(60);
        id.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Patient, String> firstName = new TableColumn<>("First Name");
        firstName.setPrefWidth(200);
        firstName.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());

        TableColumn<Patient, String> lastName = new TableColumn<>("Last Name");
        lastName.setPrefWidth(200);
        lastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        TableColumn<Patient, String> gender = new TableColumn<>("Gender");
        gender.setPrefWidth(90);
        gender.setCellValueFactory(cellData -> cellData.getValue().genderProperty());


        TableColumn<Patient, String> phoneNumber = new TableColumn<>("Phone Number");
        phoneNumber.setPrefWidth(150);
        phoneNumber.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());

        TableColumn<Patient, Date> birthDate = new TableColumn<>("Birth Date");
        birthDate.setPrefWidth(150);
        birthDate.setCellValueFactory(cellData -> cellData.getValue().ageProperty());

        TableColumn<Patient, Date> admission = new TableColumn<>("Date of Admission");
        admission.setPrefWidth(200);
        admission.setCellValueFactory(cellData -> cellData.getValue().admissionDateProperty());

        TableColumn<Patient, String> address = new TableColumn<>("Address");
        address.setPrefWidth(150);
        address.setCellValueFactory(cellData -> cellData.getValue().addressProperty());

        TableColumn<Patient, Integer> room = new TableColumn<>("Room");
        room.setPrefWidth(100);
        room.setCellValueFactory(c -> c.getValue().roomProperty().asObject());
        tableView.setOnMouseClicked(e -> handleMouseClicked());
        // tableView.setOnMouseClicked(e -> deleteReccored());
        tableView.setContextMenu(context_menu());
        tableView.setEditable(true);

        tableView.getColumns().addAll(id, firstName, lastName, gender, phoneNumber, birthDate, admission, address, room);
        return tableView;
    }

    private ContextMenu context_menu() {
        PtScene ptScene = new PtScene();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem add_menu = new MenuItem("_Add New Patient");
        add_menu.setOnAction(e -> ptScene.pt_form_pane(1));
        MenuItem edit_menu = new MenuItem("_Edit Selected Row");
        MenuItem delete_menu = new MenuItem("_Delete Selected Row");
        delete_menu.setOnAction(e -> {
            try {
                deleteReccored();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        MenuItem refresh = new MenuItem("_Refresh");
        refresh.setAccelerator(KeyCombination.keyCombination("Shortcut+r"));
        refresh.setOnAction(e -> {
            try {
                showPatientS();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        Menu sort_menu = new Menu("_Sort By");
        ToggleGroup toggles = new ToggleGroup();
        RadioMenuItem name_menu = new RadioMenuItem("_Name");
        name_menu.setToggleGroup(toggles);
        RadioMenuItem id_menu = new RadioMenuItem("Patient _ID");
        id_menu.setToggleGroup(toggles);
        RadioMenuItem date_menu = new RadioMenuItem("_Date Modified");
        date_menu.setToggleGroup(toggles);
        sort_menu.getItems().addAll(name_menu, id_menu, date_menu);
        id_menu.setSelected(true);
        contextMenu.getItems().addAll(add_menu, edit_menu, delete_menu, sort_menu, refresh);
        return contextMenu;
    }

    public static void showPatientS() throws SQLException {
        tableView.setItems(InpatientDAO.getInpatientList());
    }

    public void showSpecificpatient(int id, String name) throws SQLException {
        tableView.setItems(InpatientDAO.getSpecificPatient(id, name));
    }

    public int handleMouseClicked() {

        Patient patient = tableView.getSelectionModel().getSelectedItem();
        if (patient == null) {

            return 0;
        }

        id = patient.getId();
        setFirstName(patient.getFirstName());
        setLastName(patient.getLastName());
        setAddress(patient.getAddress());
        setAdmissionDate((Date) patient.getAdmissionDate());
        setBirthDate((patient.getAge()));
        setPhoneNumber(patient.getPhone());
        setRoomNumber(patient.getRoom());
        setGender(patient.getGender());
        setAdmissionDate((Date) patient.getAdmissionDate());

        return id;
    }

    public Patient deletePatient() {
        Patient patient = tableView.getSelectionModel().getSelectedItem();
        return patient;
    }

    public void deleteReccored() throws SQLException {
        System.out.println(id);
        if (id == 0) {
            AlertBox.selectionChecker("Please select the record  that you want to delete");
            return;
        } else {
            deleteConfirmationAlert("Are you sure you want to delete with id=" + id);

        }

    }

    public static void deleteConfirmationAlert(String text) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setContentText(text);
        alert.setHeaderText(null);

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");

        alert.getButtonTypes().addAll(yes, no);
        alert.getButtonTypes().removeAll(ButtonType.OK);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yes) {
            InpatientDAO.deleteSelectedRecorred(id);
            showPatientS();
            id = 0;
        }
        if (result.isPresent() && result.get() == no) {
            id = 0;
        }
    }


    public void deleteRecored() {

       /* System.out.println(id);
       int i=table
        InpatientDAO.deleteSelectedRecorred(id);*/
//       int i=tableView.getSelectionModel().getSelectedIndex();
        // System.out.println("rt  "+i);
        System.out.println(" key " + deletePatient());
        ObservableList<Patient> allPatient, selectedPatient;
        allPatient = tableView.getItems();
        selectedPatient = tableView.getSelectionModel().getSelectedItems();
        selectedPatient.forEach(allPatient::remove);
        System.out.println(id);
    }
}
