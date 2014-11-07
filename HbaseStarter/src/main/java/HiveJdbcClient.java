import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
 
public class HiveJdbcClient {
  private static String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
 
  public static void main(String[] args) throws SQLException {
    try {
      Class.forName(driverName);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }
    Connection con = DriverManager.getConnection("jdbc:hive://vmlab:10000/default", "", "");
    Statement stmt = con.createStatement();
    String tableName = "twitter";
    ResultSet res = stmt.executeQuery("select * from " + tableName);
    if (res.next()) {
      System.out.println(res.getString(1));
    }
    while (res.next()) {
    	int x = 17 ;
    	for (int i = 1; i <= x; i++) {
    		System.out.print(res.getString(i) + "\t");	
		}
    	System.out.println();
    }
  }
}