import org.json.simple.JSONObject;


public class Tweet {

	private Long id;
	private String time;
	private String tweetText ;
	private Long retweetCount ;
	private String language ;
	private String source ;
	private String user_name ;
	private Long followers_count ;
	private Long friends_count ;
	private Long favourites_count ;
	private Long statuses_count ;
	private String user_location ;	
	private String timezone ;
	private Double latitude;
	private Double longitude;
	private String place;
	private String country;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTweetText() {
		return tweetText;
	}
	public void setTweetText(String tweetText) {
		this.tweetText = tweetText;
	}
	public Long getRetweetCount() {
		return retweetCount;
	}
	public void setRetweetCount(Long retweetCount) {
		this.retweetCount = retweetCount;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public Long getFollowers_count() {
		return followers_count;
	}
	public void setFollowers_count(Long followers_count) {
		this.followers_count = followers_count;
	}
	public Long getFriends_count() {
		return friends_count;
	}
	public void setFriends_count(Long friends_count) {
		this.friends_count = friends_count;
	}
	public Long getFavourites_count() {
		return favourites_count;
	}
	public void setFavourites_count(Long favourites_count) {
		this.favourites_count = favourites_count;
	}
	public Long getStatuses_count() {
		return statuses_count;
	}
	public void setStatuses_count(Long statuses_count) {
		this.statuses_count = statuses_count;
	}
	public String getUser_location() {
		return user_location;
	}
	public void setUser_location(String user_location) {
		this.user_location = user_location;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	
	
}
