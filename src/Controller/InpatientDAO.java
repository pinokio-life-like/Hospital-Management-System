package Controller;

import DataModel.HighLevelDB.DBUtil;
import DataModel.Patient;
import FrontEnd.InpatientTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class InpatientDAO {
    private static final String INSERT_QUERY = "INSERT INTO Inpatient (id, first_name, last_name, gender," +
            " phone_no, birth_date,admission_date, address, room) VALUES (?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE inpatient SET first_name=?,last_name=?," +
            "gender=?," +
            "phone_no=?,birth_date=?,admission_date=?,address=?," +
            "room=? WHERE id=?";

    private static final String SELECT_QUERY = "SELECT * FROM Inpatient";
    private static final String DELETE_QUERY = "DELETE FROM inpatient WHERE id=?";
    private static final String SEARCH_OPERATION = "SELECT * FROM inpatient WHERE id=?";
    // private static final String SEARCH_OPERATION = "SELECT * FROM inpatient WHERE id LIKE '"+;

    public static void insertInpatientInfo(int id, String fName, String lName, String gender, String phone_no,
                                           Date date,
                                           Date admission, String address, int room,
                                           int state) throws SQLException {
        if (state == 0) {
            try {
                Connection connection = DBUtil.dbConnect();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, fName);
                preparedStatement.setString(3, lName);
                preparedStatement.setString(4, gender);
                preparedStatement.setString(5, phone_no);
                preparedStatement.setDate(6, date);
                preparedStatement.setDate(7, admission);
                preparedStatement.setString(8, address);
                preparedStatement.setInt(9, room);
                preparedStatement.executeUpdate();

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            InpatientTableModel in_table = new InpatientTableModel();
        } else {
            try {
                Connection connection = DBUtil.dbConnect();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
                preparedStatement.setString(1, fName);
                preparedStatement.setString(2, lName);
                preparedStatement.setString(3, gender);
                preparedStatement.setString(4, phone_no);
                preparedStatement.setDate(5, date);
                preparedStatement.setDate(6, admission);
                preparedStatement.setString(7, address);
                preparedStatement.setInt(8, room);
                preparedStatement.setInt(9, id);
                preparedStatement.executeUpdate();

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        // in_table.showPatientS(); this leads to bussiy.
    }

    public static Patient createInpatient(ResultSet resultSet) {
        Patient patient = new Patient();
        try {
            patient.setId(resultSet.getInt("id"));
            patient.setFirstName(resultSet.getString("first_name"));
            patient.setLastName(resultSet.getString("last_name"));
            patient.setGender(resultSet.getString("gender"));
            patient.setPhone(resultSet.getString("phone_no"));
            patient.setAge(resultSet.getDate("birth_date"));
            patient.setAdmissionDate(resultSet.getDate("admission_date"));
            patient.setAddress(resultSet.getString("address"));
            patient.setRoom(resultSet.getInt("room"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;//
    }

    public static ObservableList<Patient> getInpatientList() throws SQLException {
        ObservableList<Patient> patientList = FXCollections.observableArrayList();
        try {
            Connection connection = DBUtil.dbConnect();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Patient patient = createInpatient(resultSet);
                patientList.add(patient);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return patientList;/// This return final value set to tables.
    }

    public static ObservableList<Patient> getSpecificPatient(int id, String name) throws SQLException {
        final String SEARCH_BYID = "SELECT * FROM inpatient WHERE id LIKE '%" + id + "%'";
        final String SEARCH_NAME = "SELECT * FROM inpatient WHERE  first_name LIKE '%" + name + "%'";

        ObservableList<Patient> patientList = FXCollections.observableArrayList();
        try {
            Connection connection = DBUtil.dbConnect();
            PreparedStatement preparedStatement;
            if (id != -1) {
                preparedStatement = connection.prepareStatement(SEARCH_BYID);
            } else
                preparedStatement = connection.prepareStatement(SEARCH_NAME);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Patient patient = createInpatient(resultSet);
                patientList.add(patient);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return patientList;/// This return final value set to tables.
    }

    public static int lastIdGetter(String tableName, String idColNmae) {
        final String idSelector = "SELECT " + idColNmae + " from " + tableName + "";
        int m = 0;
        int id = 0;
        //int h=0;
        try {
            Connection connection = DBUtil.dbConnect();
            PreparedStatement preparedStatement = connection.prepareStatement(idSelector);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt(idColNmae);
               /* if (id - 1 != 0)//enables the pointer start from 1.
                    return 1;*/
                if (resultSet.next()) {
                    int h = resultSet.getInt(idColNmae);
                    System.out.println("h==  " + h);
                    if (id + 1 != h) {
                        return id + 1;
                    }
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return id + 2;
    }

    public static String columnSelector(String column, String table, String IdColName, int id) {
        final String columnSelector = "SELECT " + column + " FROM " + table + " WHERE " + IdColName + "=?";
        String columnValue = "";
        try {
            Connection connection = DBUtil.dbConnect();
            PreparedStatement preparedStatement = connection.prepareStatement(columnSelector);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                columnValue = resultSet.getString(column);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return columnValue;
    }

    public static boolean idFounderChecker(int id) {
         final String id_checker = "SELECT id from inpatient where id=?";
        try {
            Connection connection = DBUtil.dbConnect();
            PreparedStatement preparedStatement = connection.prepareStatement(id_checker);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void deleteSelectedRecorred(int id) {
        InpatientTableModel inpatientTableModel = new InpatientTableModel();
        try {
            Connection connection = DBUtil.dbConnect();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            /// inpatientTableModel.showPatientS();
            System.out.println("You avae to do something ");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
