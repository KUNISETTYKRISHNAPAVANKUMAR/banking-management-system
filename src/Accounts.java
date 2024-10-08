import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner scanner;
    private static int count=0;
    public Accounts(Connection connection, Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }
    public long create_account(String email){
        if(!account_exist(email)){
            System.out.println("Enter Full Name");
            String name=scanner.next();
            System.out.println("Enter initial Amount");
            Double amount=scanner.nextDouble();
            System.out.println("Create Security Pin");
            String pin=scanner.next();
            long a_number=generateAccountNumber();
            String query="INSERT INTO accounts(account_number,full_name,email,balance,security_pin) VALUES (?,?,?,?,?)";
            try {
                PreparedStatement stmt=connection.prepareStatement(query);
                stmt.setLong(1,a_number);
                stmt.setString(2,name);
                stmt.setString(3,email);
                stmt.setDouble(4,amount);
                stmt.setString(5,pin);
                int row=stmt.executeUpdate();
                if(row>0){
                    return a_number;
                }
            }
            catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
        throw new RuntimeException("Email already exist");
    }
    public long getAccountNumber(String email){
        try{
            PreparedStatement stmt=connection.prepareStatement("SELECT account_number FROM accounts WHERE email=?");
            stmt.setString(1,email);
            ResultSet resultSet=stmt.executeQuery();
            if(resultSet.next())
                return resultSet.getLong("account_number");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Email not exist");
    }

    public boolean account_exist(String email){
        try{
            PreparedStatement stmt=connection.prepareStatement("SELECT * FROM accounts WHERE email=?");
            stmt.setString(1,email);
            ResultSet resultSet=stmt.executeQuery();
            if(resultSet.next()){
                return true;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());

        }
        return false;
    }
    private long generateAccountNumber(){
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM accounts ORDER BY account_number DESC LIMIT 1");
            if (resultSet.next()) {
                return resultSet.getLong("account_number")+1;
            }
            else{
                return 10000+1;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 1;
    }
}
