import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
 
public class JsonHbaseLoader {
 
    private static org.apache.hadoop.conf.Configuration conf = null;
    /**
     * Initialization
     */
    static {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "vmlab");
        conf.set("hbase.zookeeper.property.clientPort","2181");
        conf.set("hbase.master", "vmlab:60000");
    }
 
    /**
     * Create a table
     */
    public static void creatTable(String tableName, String[] familys)
            throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        if (admin.tableExists(tableName)) {
            System.out.println("table already exists!");
        } else {
            HTableDescriptor tableDesc = new HTableDescriptor(tableName);
            for (int i = 0; i < familys.length; i++) {
                tableDesc.addFamily(new HColumnDescriptor(familys[i]));
            }
            admin.createTable(tableDesc);
            System.out.println("create table " + tableName + " ok.");
        }
    }
 
    /**
     * Delete a table
     */
    public static void deleteTable(String tableName) throws Exception {
        try {
            HBaseAdmin admin = new HBaseAdmin(conf);
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
            System.out.println("delete table " + tableName + " ok.");
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * Put (or insert) a row
     */
    public static void addRecord(String tableName, String rowKey,
            String family, String qualifier, String value) throws Exception {
        try {
            HTable table = new HTable(conf, tableName);
            Put put = new Put(Bytes.toBytes(rowKey));
            put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes
                    .toBytes(value));
            table.put(put);
            System.out.println("insert recored " + rowKey + " to table "
                    + tableName + " ok.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * Delete a row
     */
    public static void delRecord(String tableName, String rowKey)
            throws IOException {
        HTable table = new HTable(conf, tableName);
        List<Delete> list = new ArrayList<Delete>();
        Delete del = new Delete(rowKey.getBytes());
        list.add(del);
        table.delete(list);
        System.out.println("del recored " + rowKey + " ok.");
    }
 
    /**
     * Get a row
     */
    public static void getOneRecord (String tableName, String rowKey) throws IOException{
        HTable table = new HTable(conf, tableName);
        Get get = new Get(rowKey.getBytes());
        Result rs = table.get(get);
        for(KeyValue kv : rs.raw()){
            System.out.print(new String(kv.getRow()) + " " );
            System.out.print(new String(kv.getFamily()) + ":" );
            System.out.print(new String(kv.getQualifier()) + " " );
            System.out.print(kv.getTimestamp() + " " );
            System.out.println(new String(kv.getValue()));
        }
    }
    /**
     * Scan (or list) a table
     */
    public static void getAllRecord (String tableName) {
        try{
             HTable table = new HTable(conf, tableName);
             Scan s = new Scan();
             ResultScanner ss = table.getScanner(s);
             for(Result r:ss){
                 for(KeyValue kv : r.raw()){
                    System.out.print(new String(kv.getRow()) + " ");
                    System.out.print(new String(kv.getFamily()) + ":");
                    System.out.print(new String(kv.getQualifier()) + " ");
                    System.out.print(kv.getTimestamp() + " ");
                    System.out.println(new String(kv.getValue()));
                 }
             }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args) {
        try {
            String tablename = "test1234";
            String[] familys = { "grade", "course" };
            //JsonHbaseLoader.creatTable(tablename, familys);
            
            //read Json data from file and parse Json to get required data.
            File file = new File("sampletweets");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String tweet;
            //read each tweet from file
            while( (tweet=br.readLine())!=null){
            	
            	if(tweet!=null || !tweet.isEmpty()){
            		JSONParser jp = new JSONParser();
            		JSONObject tweetO = (JSONObject) jp.parse(tweet);
            		//check if tweet is created or deleted
            		//if will process all the created tweets
            		if(tweetO.get("created_at")!=null){
            			
            			//get tweet details
            			//start
            			Long id = (Long)tweetO.get("id");
            			String time = (String) tweetO.get("created_at");
            			String tweetText = (String)tweetO.get("text");
            			Long retweetCount = (Long)tweetO.get("retweet_count");
            			String language = (String)tweetO.get("lang");
            			String source = (String)tweetO.get("source");
            			
            			//this needs more parsing
            			//String coordinates = (String)tweetO.get("coordinates");
            			//String place = (String)tweetO.get("place");
            			
            			//get user data
            			JSONObject user = (JSONObject) tweetO.get("user");
            			String user_name = (String)user.get("name");
            			Long followers_count = (Long)user.get("followers_count");
            			Long friends_count = (Long)user.get("friends_count");
            			Long favourites_count = (Long)user.get("favourites_count");
            			Long statuses_count = (Long)user.get("statuses_count");
            			String user_location = (String)user.get("location");	
            			String timezone = (String)user.get("time_zone");
            			
            			//end
            			
            			
            			//parsing the retweeted tweet data 
            			// start
            			
            			JSONObject rt = (JSONObject)tweetO.get("retweeted_status");
            			if(rt!=null){
            			Long idrt = (Long)rt.get("id");
            			String timert = (String) rt.get("created_at");
            			String tweetTextrt = (String)rt.get("text");
            			Long retweetCountrt = (Long)rt.get("retweet_count");
            			String languagert = (String)rt.get("lang");
            			String sourcert = (String)rt.get("source");
            			
            			//this needs more parsing
            			//String coordinates = (String)tweetO.get("coordinates");
            			//String place = (String)tweetO.get("place");
            			
            			//get user data for retweet
            			JSONObject userrt = (JSONObject) rt.get("user");
            			String user_namert = (String)userrt.get("name");
            			Long followers_countrt = (Long)userrt.get("followers_count");
            			Long friends_countrt = (Long)userrt.get("friends_count");
            			Long favourites_countrt = (Long)userrt.get("favourites_count");
            			Long statuses_countrt = (Long)userrt.get("statuses_count");
            			String user_locationrt = (String)userrt.get("location");	
            			String timezonert = (String)userrt.get("time_zone");
            			
            			//end
            			System.out.println(timezonert);
            			}
            			
            		}else{
            			//System.out.println("deleted tweet");
            		}
            		
            		
            	}
            	
            }
            
            
            
            
            // add record zkb
       //     JsonHbaseLoader.addRecord(tablename, "zkb", "grade", "", "5");
          } catch (Exception e) {
            e.printStackTrace();
        }
    }
}