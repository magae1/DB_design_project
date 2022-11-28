import java.io.*;

public class DatabaseAuthInfo {
    private String host;
    private String port;
    private String databaseName;
    private String userName;
    private String password;

    public DatabaseAuthInfo() {
        this.host = "";
        this.port = "3306";
        this.databaseName = "";
        this.userName = "";
        this.password = "";
    }

    boolean parse_auth_info(String fileName) {

        File file = new File("./src/" + fileName);
        if (!file.isFile() || !file.exists() || file.isDirectory()) {
            System.out.println("It's not a file.");
        }

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String str;
            while ((str = br.readLine()) != null) {
                if (str.startsWith("#") || str.length() == 0)
                    continue;

                String[] chunks = str.split("=");
                if (chunks.length != 2)
                    throw new IOException("Wrong auth input.");

                String paramsName = chunks[0];
                String paramsValue = chunks[1];
                switch (paramsName) {
                    case "host" -> this.host = paramsValue;
                    case "port" -> this.port = paramsValue;
                    case "database" -> this.databaseName = paramsValue;
                    case "username" -> this.userName = paramsValue;
                    case "password" -> this.password = paramsValue;
                    default -> {}
                }
            }
            br.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return false;
        }
        System.out.println(fileName + " read successfully!");
        return true;
    }

    String getHost() {
        return host;
    }
    String getPort() {
        return port;
    }
    String getDatabase_name() {
        return databaseName;
    }
    String getPassword() {
        return password;
    }
    String getUsername() {
        return userName;
    }
}