package BankingManagmentSystem;

import java.security.PrivateKey;
import java.sql.*;
import java.util.PropertyResourceBundle;
import java.util.Scanner;

public class Accounts {
    private Connection conn;
    private Scanner scan;
    public Accounts(Connection con_ins,Scanner sc){
        this.conn=con_ins;
        this.scan=sc;
    }

    public long open_accoount(String email){
        if(!account_exists(email)){
            String open_account_query="Insert into Accounts(account_number,full_name,email,balance,security_pin) values (?,?,?,?,?);";
            scan.nextLine();
            System.out.print("Enter Full name: ");
            String full_name=scan.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance=scan.nextDouble();
            scan.nextLine();
            System.out.print("Enter Security pin: ");
            String security_pin=scan.nextLine();
            try{

                long account_number=generateAccountNumber();

                PreparedStatement prep_stmt=conn.prepareStatement(open_account_query);
                prep_stmt.setLong(1,account_number);
                prep_stmt.setString(2,full_name);
                prep_stmt.setString(3,email);
                prep_stmt.setDouble(4,balance);
                prep_stmt.setString(5,security_pin);

                int rowsAffected=prep_stmt.executeUpdate();
                System.out.println(rowsAffected);
                if(rowsAffected>0){
                    System.out.println("account created successfully");
                    return account_number;
                }
                else {
                    throw new RuntimeException("Account Creation Failed");
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        // instead of throwing you can say else { account already exists;}
        throw new RuntimeException("Account Already Exists");
    }

    public long getAccountNumber(String email) {
        String query = "Select account_number from accounts where email=?;";
        try {
            PreparedStatement prep_stmt = conn.prepareStatement(query);
            prep_stmt.setString(1, email);
            ResultSet res=prep_stmt.executeQuery();
            if(res.next()){
                return res.getLong("account_number");
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account Number Doesn't Exists");
    }

    private long generateAccountNumber(){
        try{
            Statement stmt=conn.createStatement();
            ResultSet res=stmt.executeQuery("Select account_number from Accounts order by account_number desc limit 1;");
            if(res.next()){
                long acc_numb=res.getLong("account_number");
                return acc_numb+1;
            }
            else{
                return 10000100;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }

    public boolean account_exists(String email){
        String query="Select account_number from accounts where email= ?;";
        try {
            PreparedStatement prep_stmt = conn.prepareStatement(query);
            prep_stmt.setString(1,email);
            ResultSet res=prep_stmt.executeQuery();
            if(res.next()){
                return true;
            }
            else{
            return  false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;

    }


}
