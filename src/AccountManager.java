import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    Connection connection;
    Scanner scanner;
    public AccountManager(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }
    public void debit_amount(long account_number){
        try {
            System.out.println("Enter an amount to debit");
            double amount=scanner.nextDouble();
            System.out.println("Enter Security Pin");
            String pin=scanner.next();
            PreparedStatement stmt=connection.prepareStatement("SELECT balance FROM accounts WHERE account_number=? and security_pin=?");
            stmt.setLong(1,account_number);
            stmt.setString(2,pin);
            ResultSet resultSet =stmt.executeQuery();
            double cash;
            if(resultSet.next()){
                cash=resultSet.getDouble("balance");

            if(cash>=amount) {
                stmt = connection.prepareStatement("UPDATE accounts SET balance=balance-?");
                stmt.setDouble(1, amount);
                int row = stmt.executeUpdate();
                if (row > 0) {
                    System.out.println("Debited Successfully");
                } else {
                    System.out.println("Debut Unsuccess!");
                }
            }
            else {
                System.out.println("Insufficient Balance");
            }
            }
            else{
                System.out.println("Account Number and Password is not matching");
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void credit_amount(long account_number) {
        try {
            System.out.println("Enter an amount to credit");
            double amount = scanner.nextDouble();
            System.out.println("Enter Security Pin");
            String pin = scanner.next();
            PreparedStatement stmt = connection.prepareStatement("SELECT balance FROM accounts WHERE account_number=? and security_pin=?");
            stmt.setLong(1, account_number);
            stmt.setString(2, pin);
            ResultSet resultSet = stmt.executeQuery();
            double cash;
            if (resultSet.next()) {
                cash = resultSet.getDouble("balance");
                stmt = connection.prepareStatement("UPDATE accounts SET balance=balance+?");
                stmt.setDouble(1, amount);
                int row = stmt.executeUpdate();
                if (row > 0) {
                    System.out.println("Credited Successfully");
                } else {
                    System.out.println("Credit Unsuccess!");
                }
            }
            else {
                System.out.println("Email and Pin is not matching");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void transfer_money(long account_number){
        try{
            connection.setAutoCommit(false);
            System.out.println("Enter the account number to transfer Money");
            long receipent=scanner.nextLong();
            System.out.println("Enter the amount to transfer");
            double money=scanner.nextDouble();
            System.out.println("Enter Security Pin");
            String pin=scanner.next();
            PreparedStatement stmt=connection.prepareStatement("SELECT account_number FROM accounts WHERE account_number=? and security_pin=?");
            stmt.setLong(1,account_number);
            stmt.setString(2,pin);
            ResultSet resultSet=stmt.executeQuery();
            if(resultSet.next()) {
                stmt = connection.prepareStatement("SELECT account_number FROM accounts WHERE account_number=?");
                stmt.setLong(1, receipent);
                resultSet = stmt.executeQuery();
                if (resultSet.next()) {
                    stmt = connection.prepareStatement("SELECT balance FROM accounts where account_number=?");
                    stmt.setLong(1, account_number);
                    resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        if (resultSet.getDouble("balance") >= money) {
                            stmt = connection.prepareStatement("UPDATE accounts SET balance=balance-? WHERE account_number=?");
                            stmt.setDouble(1, money);
                            stmt.setLong(2, account_number);
                            int row = stmt.executeUpdate();
                            if (row > 0) {
                                stmt = connection.prepareStatement("UPDATE accounts SET balance=balance+? WHERE account_number=?");
                                stmt.setDouble(1, money);
                                stmt.setLong(2, receipent);
                                row = stmt.executeUpdate();
                                if (row > 0) {
                                    connection.commit();
                                    connection.setAutoCommit(true);
                                    System.out.println("Amount Transferred Successfully!");
                                } else {
                                    connection.rollback();;
                                    connection.setAutoCommit(true);
                                    System.out.println("Transaction Unsuccessful!");
                                }
                            }
                        } else {
                            System.out.println("Insufficient Balance");
                        }
                    }
                } else {
                    System.out.println("Account with the number " + receipent + " doesn't exist");
                }
            }
            else {
                System.out.println("Account Number and Security Pin is not matching");
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void getBalance(long account_number){
        try {
            System.out.println("Enter Security Pin");
            String pin=scanner.next();
            PreparedStatement stmt=connection.prepareStatement("SELECT balance FROM accounts WHERE account_number=? and security_pin=?");
            stmt.setLong(1,account_number);
            stmt.setString(2,pin);
            ResultSet resultSet =stmt.executeQuery();
            if(resultSet.next()){
                System.out.println("Your Balance is : "+resultSet.getDouble("balance"));
            }
            else{
                System.out.println("Account Number and Security Pin is not matching");
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }


}
