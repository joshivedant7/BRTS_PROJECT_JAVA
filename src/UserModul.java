import java.security.spec.ECField;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
public class UserModul {
    static String fname,lname,address,email,phone,city,password;
    static int userID;
    static Scanner sc;
    static String time;
    static  DBConnector dbc;

    public UserModul(ResultSet rs) throws Exception{
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
        new TimeSpeeder(2000).start();
        userFunctions();
    } // Completed

    void userFunctions() throws Exception{
        do {
            System.out.println("1.Book seat");
            System.out.println("2.See Bus Timetable");
            System.out.println("3.See all Bus");
            System.out.println("4.See My bookings");
            System.out.println("5.Change info");
            System.out.println("6.Change password");
            System.out.println("9.Exit");
            System.out.println();
            System.out.print("Enter your choice : ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice){
                case 1 -> new BookingModule(userID);
                case 2 -> UserModul.nextFiveBus();
                case 3 -> UserModul.showAllBus();
                case 4 -> UserModul.showAllMyBooking();
                case 5 -> UserModul.changeUserInfo();
                case 6 -> UserModul.changeUserPassword();
                case 9 -> {return;}
                default -> System.out.println("Invalid Operation");
            }
        }while (true);
    }

    static void changeUserPassword() throws Exception {
        System.out.print("Enter old password : ");
        String oldpass = sc.next();
        if (oldpass.equals(password)){
            System.out.print("Enter new password : ");
            String newPass = sc.next();
            PreparedStatement pst = dbc.fireExecuteQueryPrepare("UPDATE user SET password = ? where UserID = ?");
            pst.setString(1,newPass);
            pst.setInt(2,userID);
            int r = pst.executeUpdate();
            if (r > 0){
                UserModul.phone = newPass;
                System.out.println("Password change successfully");
            }
            else System.out.println("Fail to change password");
        }
    } // Completed

    static void changeUserInfo() throws Exception{
        System.out.print("Enter first name(-1 for old first name) : ");
        String fname = sc.next(); if (fname.equals("-1")) fname= UserModul.fname;
        System.out.print("Enter last name(-1 for old name) : ");
        String lname = sc.next();sc.nextLine(); if (lname.equals("-1")) lname= UserModul.lname;
        System.out.print("Enter address(-1 for old address) : ");
        String address = sc.nextLine();if (address.equals("-1")) address= UserModul.address;
        System.out.print("Enter city(-1 for old city) : ");
        String city = sc.nextLine();if (city.equals("-1")) city= UserModul.city;
        System.out.print("Enter phone number(-1 for phone number) : ");
        String phone = sc.next();if (phone.equals("-1")) phone= UserModul.phone;

        PreparedStatement pst = dbc.fireExecuteQueryPrepare("UPDATE `user`set `FirstName`= ?, `LastName`= ?, `Address`= ?, `City`= ?, `PhoneNumber` = ? where email = ?;");
        pst.setString(1, fname);
        pst.setString(2, lname);
        pst.setString(3, address);
        pst.setString(4, city);
        pst.setString(5, phone);
        pst.setString(6, email);
        pst.execute();
    } // Completed

    public static void showAllBus() throws Exception{
        ResultSet rs = dbc.fireExecuteQuery("SELECT * FROM bus");
        System.out.println(" ID | Bus Name | Bus Type |   Time   |  DepartureCity  |  ArrivalCity  | Cost");
        int i =1;
        while (rs.next()){
            System.out.printf(" %2d | %-8s | %-8s | %-8s | %-15s | %-13s | %4.1f\n",
                    i++,
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(7),
                    rs.getString(8),
                    rs.getDouble(9)
            );
        }
    }

    public static void showAllMyBooking() throws Exception{
        CallableStatement cast = dbc.fireCallableStatement("{call myBooking(?)}");
        cast.setInt(1,userID);
        ResultSet rs = cast.executeQuery();
        System.out.println(" ID | Booking ID | Bus name | Total seat | Cost | Total Amt |    Date ");
        int i = 1;
        while (rs.next()){
            System.out.printf(" %2d | %10d | %8s | %10d | %4.1f | %9.1f | %s\n",
                    i++,rs.getInt(6),
                    rs.getString(1),
                    rs.getInt(2),
                    rs.getDouble(3),
                    rs.getDouble(4),
                    rs.getDate(5).toString()
                    );
        }
    } // Completed

    public static void nextFiveBus() throws Exception{
        String time = UserModul.time;
        ResultSet rs = dbc.fireExecuteQuery("SELECT * FROM BUS where DepartureTime > '"+time+"' limit 5");
        System.out.println(" ID | Bus Name | Bus Type |   Time   |  DepartureCity  |  ArrivalCity  | Cost");
        int i =1;
        while (rs.next()){
            System.out.printf(" %2d | %-8s | %-8s | %-8s | %-15s | %-13s | %4.1f\n",
                    i++,
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(7),
                    rs.getString(8),
                    rs.getDouble(9)
            );
        }
    }

}