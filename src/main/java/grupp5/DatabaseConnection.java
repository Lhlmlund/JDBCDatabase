package grupp5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection connect() {
        String url = "jdbc:sqlite:E:\\JavaUtveckling2024\\SQLiteDatabase\\identifier.sqlite";

        try {

        Connection connection = DriverManager.getConnection(url);
            System.out.println("Connection established");
            return connection;

        } catch (SQLException e) {
                 e.printStackTrace();

                 return null;
            }
        }
    }


