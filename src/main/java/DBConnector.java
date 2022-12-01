import java.sql.*;

public class DBConnector {
    private String db_connection_url;

    public DBConnector(DatabaseAuthInfo db_info) {
        this.db_connection_url = String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s",
                db_info.getHost(),
                db_info.getPort(),
                db_info.getDatabase_name(),
                db_info.getUsername(),
                db_info.getPassword());
    }

    public ResultSet getResultSet(String queryString) {
        try {
            Connection db_connection = DriverManager.getConnection(db_connection_url);
            PreparedStatement db_statement = db_connection.prepareStatement(queryString);
            return db_statement.executeQuery();
        } catch (SQLException se) {
            System.out.println("DB와 연결할 수 없습니다. db.auth의 정보를 확인해주세요.");
            return null;
        }
    }
    public void upDateDB(String queryString) {
        try {
            Connection db_connection = DriverManager.getConnection(db_connection_url);
            PreparedStatement db_statement = db_connection.prepareStatement(queryString);
            db_statement.executeUpdate();
        } catch (SQLException se) {
            System.out.println("DB와 연결할 수 없습니다. db.auth의 정보를 확인해주세요.");
        }
    }
}
