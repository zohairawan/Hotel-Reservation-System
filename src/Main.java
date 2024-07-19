/*
This program connects to a MySQL database
 */

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        // Reads data from music file
        Properties props = new Properties();
        try {
            props.load(Files.newInputStream(Path.of("src/hotel.properties"), StandardOpenOption.READ));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        var dataSource = new MysqlDataSource();
        dataSource.setServerName(props.getProperty("serverName"));
        dataSource.setPort(Integer.parseInt(props.getProperty("port")));
        dataSource.setDatabaseName(props.getProperty("databaseName"));

        // SQL Query
        String query = "SELECT * FROM reservations";

        try(var connection = dataSource.getConnection(
                props.getProperty("user"),
                System.getenv("MYSQL_PASS"));
            Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet = statement.executeQuery(query);

            //Returns column names and column data types
            var meta = resultSet.getMetaData();
            for(int i = 1; i < meta.getColumnCount(); i++) {
                System.out.printf("%d %s %s %n",
                        i,
                        meta.getColumnName(i),
                        meta.getColumnTypeName(i));
            }
            System.out.println("===============");

            //Iterates through and prints records of SQL table
            while(resultSet.next()) {
                System.out.printf("%s %s %d %s %n",
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getString(4)
                );

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //comment from UV
    }
}
