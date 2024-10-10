import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class BookingModule {
    DBConnector dbc ;
    Scanner sc ;
    int userID;
    BookingModule(int userID) throws Exception{
        dbc = new DBConnector();
        sc = new Scanner(System.in);
        this.userID = userID;
        dbc.setAutoCommitFalse();
        bookingModelFunction();

    }
    void bookingModelFunction() throws Exception{
        do {
            System.out.println("1.Book ticket");
            System.out.println("2.cancel ticket");
            System.out.println("3.exit");
            System.out.println();
            System.out.print("Enter choice :");
            int choiec = sc.nextInt();
            switch (choiec){
                case 1 -> bookTicket();
                case 2 -> cancelTicket();
                case 3 -> {
                    dbc.setAutoCommitTrue();
                    return;
                }
                default -> System.out.println("Invalid operation");
            }


        }while (true);
   }
   void bookTicket() throws Exception{
       System.out.print("Enter busId (-1 to see bus info) : ");
       int id = sc.nextInt();
       Bus bus = null;
       if (id == -1){
           BusModel b = new BusModel(1);
           b.showAllBus();
           System.out.print("Enter busId (-1 to see bus info) : ");
           id = sc.nextInt();
           if (!b.bus.containsKey(id)){
               System.out.println("No bus with budID :"+id);
               return;
           }else bus = b.bus.get(id);
       }
       System.out.print("Enter seat count :");
       int seatCount = sc.nextInt();
       CallableStatement cst = dbc.fireCallableStatement("{call bookingTicket(?,?,?,?,?)}");
       cst.setInt(1,userID);
       cst.setInt(2,bus.busID);
       cst.setInt(3,seatCount);
       cst.setDouble(4,bus.cost);
       cst.setInt(5,bus.operatorID);
       int rs =cst.executeUpdate();
       System.out.println("Total cost is : "+(seatCount*bus.cost));
       System.out.print("Type 'CONFIRM' to book : ");
       String confirm = sc.next().toUpperCase();
       if (confirm.equals("CONFIRM")){
           dbc.commit();
           if (rs > 0){
               System.out.println("Booked successfully");
           }
       }else dbc.rollback();
   }

   void cancelTicket() throws  Exception{
       System.out.print("Enter booking ID(-1 to see all booking) :");
       int bookingID = sc.nextInt();
       if (bookingID == -1){
           UserModul.showAllMyBooking();
           System.out.print("Enter booking ID(-1 to see all booking) :");
           bookingID = sc.nextInt();
       }
       int rs = dbc.fireCallableStatement("{call cancelTicket("+bookingID+")}").executeUpdate();
       System.out.print("Type 'CONFIRM' to cancel : ");
       String confirm = sc.next().toUpperCase();
       if (confirm.equals("CONFIRM")){
           dbc.commit();
           if (rs > 0){
               System.out.println("Canceled successfully");
           }else System.out.println("No booking ID with :"+bookingID);
       }else dbc.rollback();
   }
}
