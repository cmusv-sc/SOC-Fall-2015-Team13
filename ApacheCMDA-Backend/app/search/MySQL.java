package search;


import java.sql.*;

/**
 * Created by bluebyte60 on 9/8/15.
 */
public class MySQL {
    Connection conn = null;

    public MySQL(String account, String password) {
        try {
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/climate",account,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ResultSet select(String sql) {
        try {
            Statement stmt = (Statement) conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        new MySQL("climate","climate");

    }
}
