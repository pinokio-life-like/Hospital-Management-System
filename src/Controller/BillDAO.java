package Controller;

import DataModel.HighLevelDB.DBUtil;

import java.sql.*;

public class BillDAO {
    private final String SELECT_QUERY = "SELECT * FROM bill";
    private static final String INSERT_QUERY = "INSERT INTO bill (bill_no, pt_id, Date, total_bill) VALUES (?,?,?,?)";

    public static void insertBillInfo(int bill_id, int id, Date date, double tot_bill) {
        try {
            Connection connection = DBUtil.dbConnect();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            preparedStatement.setInt(1, bill_id);
            preparedStatement.setInt(2, id);
            preparedStatement.setDate(3, date);
            preparedStatement.setDouble(4, tot_bill);
            preparedStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static String columnSelector(String column, String table, int id) {
        final String columnSelector = "SELECT " + column + " FROM " + table + " WHERE pt_id=?";
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

}
