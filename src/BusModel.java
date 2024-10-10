import java.util.*;
import java.sql.*;
import DataStructure.HashMap;

class Bus {
    int busID;
    String busName;
    String busType;
    String departureTime;
    String travelTime;
    String departureDate;
    String sourceCity;
    String destinationCity;
    double cost =0;
    int operatorID;
    int totalSeats;

    public Bus(int busID, String busName, String busType, String departureTime, String travelTime, String departureDate, String sourceCity, String destinationCity, double cost, int operatorID, int totalSeats) {
        this.busID = busID;
        this.busName = busName;
        this.busType = busType;
        this.departureTime = departureTime;
        this.travelTime = travelTime;
        this.departureDate = departureDate;
        this.sourceCity = sourceCity;
        this.destinationCity = destinationCity;
        this.cost = cost;
        this.operatorID = operatorID;
        this.totalSeats = totalSeats;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "busID=" + busID +
                ", busName='" + busName + '\'' +
                ", busType='" + busType + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", travelTime='" + travelTime + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", sourceCity='" + sourceCity + '\'' +
                ", destinationCity='" + destinationCity + '\'' +
                ", cost=" + cost +
                ", operatorID=" + operatorID +
                ", totalSeats=" + totalSeats +
                '}';
    }
}
public class BusModel{
    DBConnector dbc = new DBConnector();
    HashMap<Integer,Bus>bus = new HashMap<>();
    Scanner sc = new Scanner(System.in);

    BusModel() throws Exception{
        setBus();
        busModelFuction();
    }

    BusModel(int i){
        setBus();
    }

    void busModelFuction() throws Exception{
        do{
            System.out.println("1.Add Bus");
            System.out.println("2.Remove Bus");
            System.out.println("3.Change Bus info");
            System.out.println("4.See all bus");
            System.out.println("5.Exit");
            System.out.println("Enter choice : ");
            int choice = sc.nextInt();
            switch (choice){
                case 1 -> addBus();
                case 2 -> removeBus();
                case 3 -> {}
                case 4 -> showAllBus();
                case 5 -> {return;}
                default -> System.out.println("Invalid Operation");
            }
        }while (true);
    }

    void setBus() {
        ResultSet rs = dbc.fireExecuteQuery("SELECT * from bus");
        try {
        while(rs.next()){
            Bus b = new Bus(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getTime(4).toString(),
                    rs.getTime(5).toString(),
                    rs.getDate(6).toString(),
                    rs.getString(7),
                    rs.getString(8),
                    rs.getDouble(9),
                    rs.getInt(10),
                    rs.getInt(11)
                    );
            bus.put(rs.getInt(1),b);
            }
        }catch (Exception e){}
    }

    public void showAllBus() {
        for (Bus b : bus.values()){
            System.out.println(b);
        }
    }

    void addBus() throws Exception{
        System.out.print("Enter bus id : ");
        int busId = sc.nextInt();
        if (bus.containsKey(busId)){
            System.out.println("Id already Exist");
            return;
        }
        sc.nextLine(); // Consume newline left-over
        System.out.print("Enter bus name: ");
        String busName = sc.nextLine();
        System.out.print("Enter bus type: ");
        String busType = sc.nextLine();
        System.out.print("Enter departure time (e.g., 10:30 AM): ");
        String departureTime = sc.nextLine();
        System.out.print("Enter travel time (e.g., 5 hours): ");
        String travelTime = sc.nextLine();
        System.out.print("Enter departure date (e.g., 2024-08-09): ");
        String departureDate = sc.nextLine();
        System.out.print("Enter source city: ");
        String sourceCity = sc.nextLine();
        System.out.print("Enter destination city: ");
        String destinationCity = sc.nextLine();
        System.out.print("Enter cost: ");
        double cost = sc.nextDouble();
        System.out.print("Enter operator ID: ");
        int operatorID = sc.nextInt();
        System.out.print("Enter total seats: ");
        int totalSeats = sc.nextInt();
        Bus b = new Bus(busId,busName,busType,departureTime,travelTime,departureDate,sourceCity,destinationCity,cost,operatorID,totalSeats);
        PreparedStatement pst = dbc.fireExecuteQueryPrepare("INSERT into bus values(?,?,?,?,?,?,?,?,?,?,?)");
        pst.setInt(1,busId);
        pst.setString(2,busName);
        pst.setString(3,busType);
        pst.setString(4,departureTime);
        pst.setString(5,travelTime);
        pst.setString(6,departureDate);
        pst.setString(7,sourceCity);
        pst.setString(8,destinationCity);
        pst.setDouble(9,cost);
        pst.setInt(10,operatorID);
        pst.setInt(11,totalSeats);
        int r = pst.executeUpdate();
        if (r > 0){
            bus.put(busId,b);
            System.out.println("Successfully entered!");
        }else System.out.println("Fail");
    }

    void removeBus(){
        System.out.println("Enter BusID : ");
        int busId = sc.nextInt();
        int r = dbc.fireExecuteUpdate("DELETE from bus where BusId = "+busId);
        if (r > 0){
            System.out.println("Deleted successfully");
        }else System.out.println("Fail to delete");
    }

    void editBusInfo() throws Exception{
        System.out.print("Enter bus id : ");
        int busId = sc.nextInt();
        Bus b =bus.get(busId);
        if (b == null){
            System.out.println("No bus with :"+busId);
            return;
        }
        System.out.print("Enter bus name(-1 for old name): ");
        String busName = sc.nextLine();if (busName.equals("-1")) busName = b.busName;
        System.out.print("Enter bus type(-1 for old type): ");
        String busType = sc.nextLine();if (busType.equals("-1")) busType = b.busType;
        System.out.print("Enter departure time(-1 for old time)(e.g., HH:MM:SS): ");
        String departureTime = sc.nextLine();if (departureTime.equals("-1")) departureTime = b.departureTime;
        System.out.print("Enter travel time(-1 for old time)(e.g., HH : MM : SS): ");
        String travelTime = sc.nextLine();if (travelTime.equals("-1")) travelTime = b.travelTime;
        System.out.print("Enter departure date(-1 for old Date) (e.g., 2024-08-09): ");
        String departureDate = sc.nextLine();if (departureDate.equals("-1")) departureDate = b.departureDate;
        System.out.print("Enter source city(-1 for old city): ");
        String sourceCity = sc.nextLine();if (sourceCity.equals("-1")) sourceCity = b.sourceCity;
        System.out.print("Enter destination city(-1 for old city): ");
        String destinationCity = sc.nextLine();if (destinationCity.equals("-1")) destinationCity = b.destinationCity;
        System.out.print("Enter cost(-1 for old cost): ");
        double cost = sc.nextDouble();if (cost ==-1) cost = b.cost;
        System.out.print("Enter operator ID(-1 for old operatorID): ");
        int operatorID = sc.nextInt();if (operatorID ==- 1) operatorID = b.operatorID;
        System.out.print("Enter total seats(-1 for old count): ");
        int totalSeats = sc.nextInt();if (totalSeats == -1) totalSeats = b.totalSeats;
        b = new Bus(busId,busName,busType,departureTime,travelTime,departureDate,sourceCity,destinationCity,cost,operatorID,totalSeats);
        PreparedStatement pst = dbc.fireExecuteQueryPrepare("UPDATE `bus` SET `BusName`= ?,`BusType`=?,`DepartureTime`=?,`TravelTime`= ?,`departureDate`=?,`DepartureCity`=?,`ArrivalCity`=?,`cost`=?,`OperatorId`=?,`TotalSeats`=? WHERE BusId = ?");
        pst.setString(1,busName);
        pst.setString(2,busType);
        pst.setString(3,departureTime);
        pst.setString(4,travelTime);
        pst.setString(5,departureDate);
        pst.setString(6,sourceCity);
        pst.setString(7,destinationCity);
        pst.setDouble(8,cost);
        pst.setInt(9,operatorID);
        pst.setInt(10,totalSeats);
        pst.setInt(11,busId);
        int r = pst.executeUpdate();
        if (r > 0){
            bus.put(busId,b);
            System.out.println("Successfully Changed Info!");
        }else System.out.println("Fail");
    }
}