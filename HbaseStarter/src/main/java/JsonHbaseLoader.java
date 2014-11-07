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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
 
public class JsonHbaseLoader {
 
    private static org.apache.hadoop.conf.Configuration conf = null;
    private static HTable table = null;
    private static String tablename = null;
    /**
     * Initialization
     */
    static {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "vmlab");
        conf.set("hbase.zookeeper.property.clientPort","2181");
        conf.set("hbase.master", "vmlab:60000");
        tablename = "twittertable";
        try {
			 table = new HTable(conf, tablename);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
            
            Put put = new Put(Bytes.toBytes(rowKey));
            put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes
                    .toBytes(value));
            table.put(put);
            //System.out.println("insert recored " + rowKey + " to table "+ tableName + " ok.");
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
            
            String[] familys = { "td", "ld" };
            //JsonHbaseLoader.creatTable(tablename, familys);
            
            //read Json data from file and parse Json to get required data.
            File file = new File("/Users/prakash/Desktop/1018");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String tweet;
            Tweet tw=null;
            //read each tweet from file
            while( (tweet=br.readLine())!=null){
            	
            	if(tweet!=null || !tweet.isEmpty()){
            		JSONParser jp = new JSONParser();
            		JSONObject tweetO = (JSONObject) jp.parse(tweet);
            		//check if tweet is created or deleted
            		//Condition will process all the created tweets
            		if(tweetO.get("created_at")!=null){
            			 tw = (Tweet)parseTweet(tweetO);
            			/************************************ retweet start ********************************/
            			JSONObject rt = (JSONObject)tweetO.get("retweeted_status");
            			if(rt !=null){
		            			Tweet retw = (Tweet)parseTweet(rt);
            			}
            		//load data into table
            			if(tw.getCountry()!=null)
            		JsonHbaseLoader.addRecord(tablename, String.valueOf(tw.getId()), "ld", "country", tw.getCountry());
            			if(tw.getPlace()!=null)
            		JsonHbaseLoader.addRecord(tablename, String.valueOf(tw.getId()), "ld", "place", tw.getPlace());
            			if(tw.getTime()!=null)
            		JsonHbaseLoader.addRecord(tablename, String.valueOf(tw.getId()), "ld", "time", tw.getTime());
            			if(tw.getTimezone()!=null)
            		JsonHbaseLoader.addRecord(tablename, String.valueOf(tw.getId()), "ld", "timezone", tw.getTimezone());
            			if(tw.getUser_location()!=null)
            		JsonHbaseLoader.addRecord(tablename, String.valueOf(tw.getId()), "ld", "user_location", tw.getUser_location());
            			if(tw.getLongitude()!=null && tw.getLatitude()!=null)
            		JsonHbaseLoader.addRecord(tablename, String.valueOf(tw.getId()), "ld", "coordinates", String.valueOf(tw.getLatitude()) + "," + String.valueOf(tw.getLongitude()));
            			if(tw.getSource()!=null)
            		JsonHbaseLoader.addRecord(tablename, String.valueOf(tw.getId()), "td", "source", tw.getSource());
            			if(tw.getTweetText()!=null)
            		JsonHbaseLoader.addRecord(tablename, String.valueOf(tw.getId()), "td", "text", tw.getTweetText());
            			if(tw.getUser_name()!=null)
            		JsonHbaseLoader.addRecord(tablename, String.valueOf(tw.getId()), "td", "user_name", tw.getUser_name());
            			if(tw.getFavourites_count()!=null)
            		JsonHbaseLoader.addRecord(tablename, String.valueOf(tw.getId()), "td", "favourites_count", String.valueOf(tw.getFavourites_count()));
            			if(tw.getFollowers_count()!=null)
            		JsonHbaseLoader.addRecord(tablename, String.valueOf(tw.getId()), "td", "followers_count", String.valueOf(tw.getFollowers_count()));
            			if(tw.getFriends_count()!=null)
            		JsonHbaseLoader.addRecord(tablename, String.valueOf(tw.getId()), "td", "friends_count", String.valueOf(tw.getFriends_count()));
            			if(tw.getRetweetCount()!=null)
            		JsonHbaseLoader.addRecord(tablename, String.valueOf(tw.getId()), "td", "retweetCounnt", String.valueOf(tw.getRetweetCount()));
            			if(tw.getStatuses_count()!=null)
            		JsonHbaseLoader.addRecord(tablename, String.valueOf(tw.getId()), "td", "statuses_count", String.valueOf(tw.getStatuses_count()));
            			if(tw.getLanguage()!=null)
            		JsonHbaseLoader.addRecord(tablename, String.valueOf(tw.getId()), "td", "language", tw.getLanguage());
            		//System.out.println(tw.getSource()+"\t"+tw.getTimezone());
            		}
            	}else{
            				
            		}
            }
            System.out.println("done!");
            	
            // add record zkb
       
       }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static Tweet parseTweet(JSONObject tweet){
    	Tweet tw = new Tweet();
		//get tweet details
		//start
    	JSONObject tweetO = (JSONObject)tweet;
		tw.setId((Long)tweetO.get("id"));
		tw.setTime( (String) tweetO.get("created_at"));
		tw.setTweetText((String)tweetO.get("text"));
		tw.setRetweetCount((Long)tweetO.get("retweet_count"));
		tw.setLanguage((String)tweetO.get("lang"));
		tw.setSource((String)tweetO.get("source"));
		
		//get location data
		JSONObject coordinates = (JSONObject)tweetO.get("coordinates");
		if((coordinates!=null)){
			JSONArray latandlong = (JSONArray) coordinates.get("coordinates");
			tw.setLatitude((Double)latandlong.get(0));
			tw.setLongitude((Double)latandlong.get(1));
		}
		JSONObject place = (JSONObject)tweetO.get("place");
		if(place!=null){
			tw.setPlace((String)place.get("full_name"));
			tw.setCountry((String)place.get("country"));
		}
		
		//get user data
		JSONObject user = (JSONObject) tweetO.get("user");
		tw.setUser_name((String)user.get("name"));
		tw.setFollowers_count((Long)user.get("followers_count"));
		tw.setFriends_count((Long)user.get("friends_count"));
		tw.setFavourites_count((Long)user.get("favourites_count"));
		tw.setStatuses_count((Long)user.get("statuses_count"));
		tw.setUser_location((String)user.get("location"));	
		tw.setTimezone((String)user.get("time_zone"));
    	return tw;
    }
    
}