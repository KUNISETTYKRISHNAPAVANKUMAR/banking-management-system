import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class User {
    Connection connection;
    Scanner scanner;
    public User(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }
    public void register(){
        System.out.println("Enter your full name");
        String name=scanner.next();
        System.out.println("Enter your Email");
        String email=scanner.next();
        System.out.println("Enter Password");
        String password=scanner.next();
        String query="INSERT INTO user(full_name,email,password) VALUES (?,?,?)";
        try{
            PreparedStatement stmt=connection.prepareStatement("SELECT * FROM user WHERE email=?");
            stmt.setString(1,email);
            ResultSet result=stmt.executeQuery();
            if(result.next()){
                System.out.println("Email already exists");
            }
            else {
                stmt = connection.prepareStatement(query);
                stmt.setString(1,name);
                stmt.setString(2,email);
                stmt.setString(3,password);
                int row = stmt.executeUpdate();
                if (row > 0) {
                    System.out.println("Registered Successfully!");
                }
                else{
                    System.out.println("Something happened.");
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public String login(){
        try {
            System.out.println("Enter your email");
            String email=scanner.next();
            System.out.println("enter password");
            String password=scanner.next();
            PreparedStatement stmt=connection.prepareStatement("SELECT * FROM user WHERE email=? and password=?");
            stmt.setString(1,email);
            stmt.setString(2,password);
            ResultSet resultSet=stmt.executeQuery();
            if(resultSet.next())
                return resultSet.getString("email");
        }
        catch (SQLException e){
            System.out.println("Here is error");
        }
        return null;
    }
}
