import java.sql.*;


public class Main {
    public static void main(String[] args) {
        DatabaseAuthInfo db_info = new DatabaseAuthInfo();

        if (!db_info.parse_auth_info("db.auth")) {
            System.out.println("Failed to connect with DB. Check \".auth\" file.");
            return;
        }


        String db_connection_url = String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s",
                db_info.getHost(),
                db_info.getPort(),
                db_info.getDatabase_name(),
                db_info.getUsername(),
                db_info.getPassword());

        String query_string = "SELECT ID, name FROM student;";

        try {
            Connection db_connection = DriverManager.getConnection(db_connection_url);
            PreparedStatement db_statement = db_connection.prepareStatement(query_string);

            ResultSet result = db_statement.executeQuery(query_string);
            System.out.println("Results..");
            while(result.next()) {
                System.out.println(result.getString("ID") + "\t" + result.getString("name"));
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

    }
}