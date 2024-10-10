import java.sql.*;

public class DBConnector {
    private Connection conn;
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String uname = "root";
    private String pwd = "";
    private String url = "jdbc:mysql://localhost:3306/brts";
    private Statement stmt;
    private PreparedStatement pstmt;
    private CallableStatement cast;
    private ResultSet rs;

    public void setAutoCommitFalse() throws Exception{
        conn.setAutoCommit(false);
    }
    public void setAutoCommitTrue() throws Exception{
        conn.setAutoCommit(true);
    }
    public void commit() throws Exception{
        conn.commit();
    }
    public void rollback() throws Exception{
        conn.rollback();
    }


    public DBConnector() {
        try {
            Class.forName(this.driver);
            this.conn = DriverManager.getConnection(this.url, this.uname, this.pwd);
        } catch (Exception var2) {
            System.out.println(var2.getMessage());
        }

    }

    public CallableStatement fireCallableStatement(String query){
        try {
            this.cast = this.conn.prepareCall(query);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return this.cast;
    }

    public int fireExecuteUpdate(String query) {
        int n = 0;

        try {
            this.stmt = this.conn.createStatement();
            n = this.stmt.executeUpdate(query);
        } catch (SQLException var4) {
            System.out.println(var4.getMessage());
        }

        return n;
    }

    public ResultSet fireExecuteQuery(String query) {
        this.rs = null;

        try {
            this.stmt = this.conn.createStatement();
            this.rs = this.stmt.executeQuery(query);
        } catch (Exception var3) {
            System.out.println(var3.getMessage());
        }

        return this.rs;
    }

    public PreparedStatement fireExecuteQueryPrepare(String query) {
        try {
            this.pstmt = this.conn.prepareStatement(query);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return this.pstmt;
    }

    public void close() {
        try {
            if(cast != null)
                this.cast.close();
            if(pstmt != null)
                this.pstmt.close();
            if(stmt!= null)
                this.stmt.close();
            if(rs != null)
                this.rs.close();
            this.conn.close();
        } catch (SQLException var2) {
            var2.printStackTrace();
        }

    }
}
