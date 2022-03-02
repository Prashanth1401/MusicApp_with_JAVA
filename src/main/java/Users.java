import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Users {
    String username;
    String password;

    public Users() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int createUser(Connection conn) throws SQLException {
        Scanner s = new Scanner(System.in);
        PreparedStatement psmt = conn.prepareStatement("Insert into Users values(?,?)");
        System.out.println("Enter your username = ");
        String username = s.next();
        psmt.setString(1, username);
        System.out.println("Enter your password = ");
        String password = s.next();
        psmt.setString(2, password);
        int roweff = psmt.executeUpdate();
        psmt.close();

        return roweff;
    }

    public boolean checkUsername(Connection conn, String username, String password) throws SQLException {

        Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = statement.executeQuery("select * from Users");

        boolean validation = true;
        while (rs.next()) {
            if (rs.getString(1).equals(username)) {
                if (rs.getString(2).equals(password)) {
                    validation = true;
                    System.out.println("\n\n________________\n");
                    System.out.println("            WELCOME  " + username + "            ");
                    System.out.println("________________");
                    break;
                }
            } else {
                validation = false;
            }
        }

        return validation;
    }
}
