package BankingManagmentSystem;

import java.sql.*;
import java.util.Scanner;
// the bankingapp is the main menu_driven class
//it is totally related to 3 other classes 1 users : to create a new user in our bank and login purpose
//2: Accounts:to create a new bankaccount or to get account number of existing user
//3:account manager for transaction purpose by getting account _number from above class
public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "Vishal@2002";


    public static void main(String[] args) throws ClassNotFoundException {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Drivers loaded successfully!!");
        } catch (ClassNotFoundException e) {
            System.out.println("connection Failed");
            System.out.println(e.getMessage());
        }
        try {
            Connection con_ins = DriverManager.getConnection(url, username, password);
            Scanner sc = new Scanner(System.in);
            User user_obj = new User(con_ins, sc);
            Accounts ac_obj= new Accounts(con_ins,sc);
            AccountManager acm_obj= new AccountManager(con_ins,sc);

            String email;
            long account_number;

            while (true) {
                System.out.println("*** Welcome To Banking System ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. exit");
                System.out.println("enter Your Choice");
                int choice1 = sc.nextInt();
                switch (choice1) {
                    case 1:
                        user_obj.register();
                        break;

                    case 2:
                        email = user_obj.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("User Logged In!");
                            //if they don't have account they will create
                            if (!ac_obj.account_exists(email)){
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                int choice= sc.nextInt();
                                if(choice==1){
                                    account_number=ac_obj.open_accoount(email);
                                    System.out.println("Account created successfully");
                                    System.out.println("Your account number is: "+account_number);
                                }
                                else{
                                    break;
                                }
                            }
                            // else
                            account_number=ac_obj.getAccountNumber(email);
                            int choice2=0;
                            while(choice2!=5){
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. check Balance");
                                System.out.println("5. Logout");
                                System.out.println("Enter Your choice: ");
                                choice2=sc.nextInt();
                                switch (choice2){
                                    case 1:
                                        acm_obj.debit_money(account_number);
                                        break;
                                    case 2:
                                        acm_obj.credit_money(account_number);
                                        break;
                                    case 3:
                                        acm_obj.transfer_money(account_number);
                                        break;
                                    case 4:
                                        acm_obj.check_balance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter a valid choice");
                                        break;
                                }
                            }

                        } else {
                            System.out.println("Incorrect Email or Password");
                        }
                    case 3:
                        System.out.println("Thank You For Using Banking System!!");
                        System.out.println("Exiting the system!!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice");
                        break;


                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
