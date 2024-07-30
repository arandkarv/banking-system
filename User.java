package BankingManagmentSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class User {
    Connection conn;
    Scanner scan;
    User(Connection con_ins, Scanner sc){
        this.conn=con_ins;
        this.scan=sc;
    }
    public  void register(){
        scan.nextLine();
        System.out.println("Full Name: ");
        String name=scan.nextLine();
        System.out.println("email: ");
        String email=scan.nextLine();
        System.out.println("password: ");
        String password=scan.nextLine();
        if(user_exist(email)){
            System.out.println("User Already Exists for this Email Address!!");
            return;
        }
        String register_query= "Insert into User(full_name,email,password) values (?,?,?);";
        try {
            PreparedStatement prep_stmt=conn.prepareStatement(register_query);
            prep_stmt.setString(1,name);
            prep_stmt.setString(2,email);
            prep_stmt.setString(3,password);
            int rowsAffected= prep_stmt.executeUpdate();
            if (rowsAffected>0){
                System.out.println("Registration Successfully");
            }
            else{
                System.out.println("Registration Failed");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
    public String login(){
        scan.nextLine();
        System.out.println("Email: ");
        String email=scan.nextLine();
        System.out.println("password: ");
        String password=scan.nextLine();
        String login_query="Select * from User where email=? and password=?;";
        try{
            PreparedStatement prep_stmt= conn.prepareStatement(login_query);
            prep_stmt.setString(1,email);
            prep_stmt.setString(2,password);
            ResultSet res=prep_stmt.executeQuery();
            if (res.next()){
                return email;
            }
            else{
                return null;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;


    }
    public boolean user_exist(String email){
        String query="select * from user where email=?;";
        try{
            PreparedStatement prep_stmt = conn.prepareStatement(query);
            prep_stmt.setString(1,email);
            ResultSet res = prep_stmt.executeQuery();
            if (res.next()){
                return true;
            }
            else{
                return  false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return  false;
    }

}
