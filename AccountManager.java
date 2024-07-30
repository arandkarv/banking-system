package BankingManagmentSystem;

import com.mysql.cj.exceptions.ConnectionIsClosedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection conn;
    private Scanner scan;
    AccountManager(Connection con_ins,Scanner sc){
        this.conn=con_ins;
        this.scan=sc;
    }

    public void credit_money(long account_number) throws SQLException{
        scan.nextLine();
        System.out.println("Enter Amount: ");
        double amount=scan.nextDouble();
        scan.nextLine();
        System.out.println("Enter Security Pin: ");
        String security_pin=scan.nextLine();

        try{
            conn.setAutoCommit(false);
            if(account_number!=0){
                String query="select * from accounts where account_number=? and security_pin=?;";
                PreparedStatement prep_stmt= conn.prepareStatement(query);
                prep_stmt.setLong(1,account_number);
                prep_stmt.setString(2,security_pin);
                ResultSet res= prep_stmt.executeQuery();
                if(res.next()) {
                    String credit_query = "update accounts set balance = balance +? where account_number=?;";
                    PreparedStatement prep_stmt1 = conn.prepareStatement(credit_query);
                    prep_stmt1.setDouble(1, amount);
                    prep_stmt1.setLong(2, account_number);
                    System.out.println("executed-------");
                    int rowsAffected = prep_stmt1.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Rs. " + amount + " credited Successfully");
                        conn.commit();
                        conn.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed");
                        conn.rollback();
                        conn.setAutoCommit(true);
                    }
                }
                else{
                    System.out.println("Invalid Security Pin!");
                }
            }


        }
        catch (SQLException e){
            e.printStackTrace();
        }
        conn.setAutoCommit(true);
    }

    public  void  debit_money(long account_number) throws SQLException{
        scan.nextLine();
        System.out.println("enter amount: ");
        double amount=scan.nextDouble();
        scan.nextLine();
        System.out.println("enter security pin: ");
        String security_pin= scan.nextLine();
        try{
            conn.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement prep_stmt= conn.prepareStatement("select * from accounts where account_number=? and security_pin=?;");
                prep_stmt.setLong(1,account_number);
                prep_stmt.setString(2,security_pin);
                ResultSet res=prep_stmt.executeQuery();
                if(res.next()){
                    double current_balance=res.getDouble("balance");
                    if(amount<=current_balance){
                        String debit_query= "Update accounts set balance = balance -? where account_number=?";
                        PreparedStatement prep_stmt1=conn.prepareStatement(debit_query);
                        prep_stmt1.setDouble(1,amount);
                        prep_stmt1.setLong(2,account_number);
                        int rowsAffected =prep_stmt1.executeUpdate();
                        if(rowsAffected>0){
                            System.out.println("Rs. "+ amount+"Debited Successfully");
                            conn.commit();
                            conn.setAutoCommit(true);
                        }
                        else{
                            System.out.println("Insufficient Balance!");
                        }

                    }
                    else{
                        System.out.println("invalid pin !");
                    }
                }
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        conn.setAutoCommit(true);

    }

    public void transfer_money(long sender_account_number) throws SQLException{
        scan.nextLine();
        System.out.println("enter receiver account number: ");
        long receiver_account_number=scan.nextLong();
        System.out.println("Enter Amount: ");
        double amount =scan.nextDouble();
        scan.nextLine();
        System.out.println("Enter Security Pin: ");
        String security_pin=scan.nextLine();
        //System.out.println("my account number:"+ sender_account_number);
        try{
            conn.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0){
                PreparedStatement prep_stmt=conn.prepareStatement("Select * from accounts where account_number=? and security_pin= ?");
                prep_stmt.setLong(1,sender_account_number);
                prep_stmt.setString(2,security_pin);
                ResultSet res =prep_stmt.executeQuery();

                if(res.next()){
                    double current_balance=res.getDouble("balance");
                    if(amount<=current_balance){
                        String debit_query="update accounts set balance = balance -? where account_number= ?;";
                        String credit_query= "Update accounts set balance = balance + ? where account_number = ?;";
                        PreparedStatement credit_stmt=conn.prepareStatement(credit_query);
                        PreparedStatement debit_stmt=conn.prepareStatement(debit_query);

                        credit_stmt.setDouble(1,amount);
                        credit_stmt.setLong(2,receiver_account_number);
                        debit_stmt.setDouble(1,amount);
                        debit_stmt.setLong(2,sender_account_number);
                        int rowsAffected1= debit_stmt.executeUpdate();
                        int rowsAffected2= credit_stmt.executeUpdate();

                        if(rowsAffected1>0 && rowsAffected2>0){
                            System.out.println("Transaction Successful");
                            System.out.println("RS."+ amount+"Transferred Successfully");
                            conn.commit();
                            conn.setAutoCommit(true);
                            return;
                        }
                        else{
                            System.out.println("Transaction Failed");
                            conn.rollback();
                            conn.setAutoCommit(true);
                        }
                    }
                    else{
                        System.out.println("Insufficient Balance");
                    }
                }
                else{
                    System.out.println("Invalid Security Pin!");
                }
            }
            else{
                System.out.println("Invalid Account Number");
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        conn.setAutoCommit(true);
    }


    public  void  check_balance(long  account_number){
        scan.nextLine();
        System.out.println("enter Security pin:");
        String security_pin=scan.nextLine();
        try{
            PreparedStatement prep_stmt= conn.prepareStatement("select balance from accounts where account_number=? and security_pin=?; ");
            prep_stmt.setLong(1,account_number);
            prep_stmt.setString(2,security_pin);
            ResultSet res = prep_stmt.executeQuery();
            if(res.next()){
                double balance=res.getDouble("balance");
                System.out.println("balance in your account is: "+balance);

            }
            else {
                System.out.println("Invalid pin !");
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }


}
