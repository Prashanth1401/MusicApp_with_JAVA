import java.sql.DriverManager;
import java.sql.Connection;

public class Connect {
    public static Connection connection;

    public static Connection getConnection() throws Exception
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/MajorJukeBox","root","1401");
        return connection;
    }

}
