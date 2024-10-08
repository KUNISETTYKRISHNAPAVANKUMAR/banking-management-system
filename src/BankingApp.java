import java.sql.*;
import java.util.Scanner;

public class BankingApp {
    private static final String url="jdbc:mysql://localhost:3306/banking_system";
    private static final String username="root";
    private static final String password="Abcdeabcd@2000";
    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        try{
            Connection connection=DriverManager.getConnection(url,username,password);
            Scanner scanner=new Scanner(System.in);
            User user=new User(connection,scanner);
            AccountManager accountManager=new AccountManager(connection,scanner);
            Accounts accounts=new Accounts(connection,scanner);
            String email;
            long account_number;

        System.out.println("**********Welcome to Banking Management System*************");
        while(true){
            System.out.println("1.Register User");
            System.out.println("2.Login User");
            System.out.println("3.Exit");
            int choice=scanner.nextInt();
            switch (choice){
                case 1:
                    user.register();
                    break;
                case 2:
                    email=user.login();
//                    System.out.println(email);
                    if(email!=null){
                        if(!accounts.account_exist(email)){
                            System.out.println();
                            System.out.println("1. Open a new Bank Account");
                            System.out.println("2. Exit");
                            int choice2=scanner.nextInt();
                            switch (choice2){
                                case 1:
                                    account_number=accounts.create_account(email);
                                    System.out.println("Account is created successfully");
                                    System.out.println("Your account number is : "+account_number);
                                    break;
                                case 2:
                                    break;
                            }
                        }
                            account_number=accounts.getAccountNumber(email);
                            int choice3=0;
                            while (choice3!=5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Logout");
                                System.out.println("Enter your choice : ");
                                choice3=scanner.nextInt();
                                switch (choice3){
                                    case 1:
                                        accountManager.debit_amount(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_amount(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice");
                                        break;
                                }
                            }
                    }
                    else{
                        System.out.println("Email and Password doesn't match");
                        break;
                    }
                    break;
                case 3:
                    System.out.println("Thanks for using BANKING SYSTEM");
                    System.out.println("Exiting");
                    return;
                default:
                    System.out.println("Enter valid choice");
                    break;
            }
        }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        catch (RuntimeException e){
            System.out.println("Email exist");
        }
    }
}