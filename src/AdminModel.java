import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
public class AdminModel {
    static String fname,lname,address,email,phone,city,password;
    static int userID;
    static Scanner sc;
    static  DBConnector dbc;

    public AdminModel(ResultSet rs) throws Exception{
        userID = rs.getInt(1);
        fname = rs.getString(2);
        lname = rs.getString(3);
        address = rs.getString(4);
        city = rs.getString(5);
        email = rs.getString(6);
        phone = rs.getString(7);
        password = rs.getString(8);
        sc = new Scanner(System.in);
        dbc = new DBConnector();
        adminFunctions();
    } // Completed

    void adminFunctions() throws Exception{
        do {
        System.out.println("1.See all booking");
        System.out.println("2.See all bus operator");
        System.out.println("3.Edit bus operator");
        System.out.println("4.See all user");
        System.out.println("5.Modify bus");
        System.out.println("6.Change info");
        System.out.println("7.Change password");
        System.out.println("9.Exit");
        System.out.println();
        System.out.print("Enter your choice : ");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice){
            case 1 -> AdminModel.showAllBooking();
            case 2 -> AdminModel.showAllBusOperator();
            case 3 -> AdminModel.editBusOperator();
            case 4 -> AdminModel.showAllUser();
            case 5 -> new BusModel();
            case 6 -> AdminModel.changeAdminInfo();
            case 7 -> AdminModel.changeAdminPassword();
            case 9 -> {return;}
            default -> System.out.println("Invalid Operation");
        }
        }while (true);
    } // Completed

    static void changeAdminPassword() throws Exception {
        System.out.print("Enter old password : ");
        String oldpass = sc.next();
        if (oldpass.equals(password)){
            System.out.print("Enter new password : ");
            String newPass = sc.next();
            PreparedStatement pst = dbc.fireExecuteQueryPrepare("UPDATE user SET password = ? where UserID = ?");
                pst.setString(1,newPass);
                pst.setInt(2,userID);
                int r = pst.executeUpdate();
                if (r > 0)
                    System.out.println("Password change successfully");
                else System.out.println("Fail to change password");
        }
    } // Completed

    static void changeAdminInfo() throws Exception{
        System.out.print("Enter first name(-1 for old first name) : ");
        String fname = sc.next(); if (fname.equals("-1")) fname= AdminModel.fname;
        System.out.print("Enter last name(-1 for old name) : ");
        String lname = sc.next();sc.nextLine(); if (lname.equals("-1")) lname= AdminModel.lname;
        System.out.print("Enter address(-1 for old address) : ");
        String address = sc.nextLine();if (address.equals("-1")) address= AdminModel.address;
        System.out.print("Enter city(-1 for old city) : ");
        String city = sc.nextLine();if (city.equals("-1")) city= AdminModel.city;
        System.out.print("Enter phone number(-1 for phone number) : ");
        String phone = sc.next();if (phone.equals("-1")) phone= AdminModel.phone;

        PreparedStatement pst = dbc.fireExecuteQueryPrepare("UPDATE `user`set `FirstName`= ?, `LastName`= ?, `Address`= ?, `City`= ?, `PhoneNumber` = ? where email = ?;");
        pst.setString(1, fname);
        pst.setString(2, lname);
        pst.setString(3, address);
        pst.setString(4, city);
        pst.setString(5, phone);
        pst.setString(6, email);
        pst.execute();
    } // Completed

    static void editBusOperator() throws Exception{
        System.out.println("Enter ID : ");
        int id = sc.nextInt();
        ResultSet rs = dbc.fireExecuteQuery("SELECT * FROM busoperator where operatorid = "+id);
        if (!rs.next()){
            System.out.println("No Id found");
            return;
        }
        System.out.print("Enter name name(-1 for old name) : ");
        String name = sc.next(); if (name.equals("-1")) name=rs.getString(2);
        System.out.print("Enter email(-1 for old name) : ");
        String email = sc.next();sc.nextLine(); if (email.equals("-1")) email= rs.getString(4);
        System.out.print("Enter address(-1 for old address) : ");
        String address = sc.nextLine();if (address.equals("-1")) address=rs.getString(3);
        System.out.print("Enter city(-1 for old city) : ");
        String city = sc.nextLine();if (city.equals("-1")) city= rs.getString(5);
        System.out.print("Enter phone number(-1 for phone number) : ");
        String phone = sc.next();if (phone.equals("-1")) phone= rs.getString(6);

        PreparedStatement pst = dbc.fireExecuteQueryPrepare("UPDATE `busoperator`set `OperatorName`= ?, `Address`= ?, `EMail`= ?, `City`= ?, `PhoneNumber` = ? where OperatorId = ?;");
        pst.setString(1, name);
        pst.setString(2, address);
        pst.setString(3, email);
        pst.setString(4, city);
        pst.setString(5, phone);
        pst.setInt(6, id);
        pst.execute();
    }// Completed

    public static void showAllBusOperator() throws Exception{
        ResultSet rs = dbc.fireExecuteQuery("SELECT * FROM busoperator");
        System.out.println(" ID |      Name      |       E-mail      |  PhoneNo.  |    City    |     Address     ");
        while (rs.next()){
            System.out.printf(" %2d | %-14s | %-17s | %-10s | %-10s | %s\n",
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(4),
                    rs.getString(6),
                    rs.getString(5),
                    rs.getString(3)
                    );
        }
    }

    public static void showAllUser() throws Exception{
        ResultSet rs = dbc.fireExecuteQuery("SELECT * FROM user where type ='user'");
        System.out.println(" ID |        Name        |       E-mail      |  PhoneNo.  |    City    |     Address     ");
        while (rs.next()){
            System.out.printf(" %2d | %-18s | %-17s | %-10s | %-10s | %s\n",
                    rs.getInt(1),
                    (rs.getString(2)+" "+rs.getString(3)),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getString(5),
                    rs.getString(4)
                    );
        }
    } // Completed

    public static void showAllBooking() throws Exception{
        ResultSet rs = dbc.fireExecuteQuery("SELECT * FROM booking");
        System.out.println(" ID | User ID | Bus Id | DepartureDate | DepartureTime ");
        while (rs.next()){
            System.out.printf(" %2d | %7d | %6d | %-13s | %s\n",
                    rs.getInt(1),
                    rs.getInt(2),
                    rs.getInt(3),
                    rs.getDate(4).toString(),
                    rs.getTime(5)
                    );
        }
    } // Completed

}