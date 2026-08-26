import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DatabaseConnection {
    public static Connection getConnection(String configFilePath) throws Exception {
        try (FileReader reader = new FileReader(configFilePath)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject config = new JSONObject(tokener);

            String url = config.getString("url");
            String username = config.getString("username");
            String password = config.getString("password");

            return DriverManager.getConnection(url, username, password);
        }
    }
}