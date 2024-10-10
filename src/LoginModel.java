import java.sql.*;
import java.util.*;

public class LoginModel {
    static DBConnector dbc = new DBConnector();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try{
            do {
                System.out.println("1.Sign up");
                System.out.println("2.Sign in");
                System.out.println("3.Exit");
                System.out.println();
                System.out.print("Enter choice : ");
                int choice = sc.nextInt(); sc.nextLine();
                switch (choice){
                    case 1 -> loginAction();
                    case 2 -> resistorAction();
                    case 3 -> {
                        dbc.close();
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid Operation");
                }

            }while (true);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    static void loginAction() throws Exception{
        System.out.print("E-mail: ");
        String email = sc.next();
        System.out.print("Password : ");
        String password = sc.next();

        ResultSet rs = dbc.fireExecuteQuery("SELECT * from user where EMail = '"+email+"' AND password = '"+password+"'");
        if (rs.next()){
            String type = rs.getString(9);
            if (type.equalsIgnoreCase("admin")){
                new AdminModel(rs);
            } else if (type.equalsIgnoreCase("user")) {
                new UserModul(rs);
            }
        }else System.out.println("Wrong e-mail or password");
    }

    static void resistorAction() throws Exception {
        System.out.print("E-mail: ");
        String email = sc.next();
        if (!email.contains("@")){
            System.out.println("Invalid e-mail");
            return;
        }
        System.out.print("Password : ");
        String password = sc.next();
        DBConnector dbc = new DBConnector();
        int rs = dbc.fireExecuteUpdate("INSERT INTO user (email,password) values('" + email + "','" + password + "');");
        if (rs > 0) {
            System.out.println("Registration successful!");
            System.out.println();
            System.out.print("Enter first name : ");
            String fname = sc.next();
            System.out.print("Enter last name : ");
            String lname = sc.next();sc.nextLine();
            System.out.print("Enter address : ");
            String address = sc.nextLine();
            System.out.print("Enter city : ");
            String city = sc.nextLine();
            String phone;
            do {
                System.out.print("Enter phone number : ");
                phone = sc.next();
            }while (LoginModel.checkValidPhone(phone));
            PreparedStatement pst = dbc.fireExecuteQueryPrepare("UPDATE `user`set `FirstName`= ?, `LastName`= ?, `Address`= ?, `City`= ?, `PhoneNumber` = ? where email = ?;");
            pst.setString(1, fname);
            pst.setString(2, lname);
            pst.setString(3, address);
            pst.setString(4, city);
            pst.setString(5, phone);
            pst.setString(6, email);
            pst.execute();
        }
    }

    static  boolean checkValidPhone(String s){
        boolean b = s.matches("\\d{10}");
        if (!b) System.out.println("\nEnter number Properly");
        return !b;
    }

}