package fr.fabienfleureau.xtextatstackoverflow.twitter;

import java.io.IOException;
import java.util.Properties;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterService {

	private String consumerKeyStr = "XXXXXXXXXXXXXXXXXXXXXXXXXXXX";
	private String consumerSecretStr = "XXXXXXXXXXXXXXXXXXXXXXXXXXXX";
	private String accessTokenStr = "XXXXXXXXXXXXXXXXXXXXXXXXXXXX";
	private String accessTokenSecretStr = "XXXXXXXXXXXXXXXXXXXXXXXXXXXX";
	  
	
	private static TwitterService instance;
	private Twitter twitter;

	private TwitterService() {
		twitter = new TwitterFactory().getInstance();
		Properties twitterProperties = getPropertiesResource();
		consumerKeyStr = twitterProperties.getProperty("consumerKeyStr");
		consumerSecretStr = twitterProperties.getProperty("consumerSecretStr");
		accessTokenStr = twitterProperties.getProperty("accessTokenStr");
		accessTokenSecretStr = twitterProperties.getProperty("accessTokenSecretStr");
	}
	
	public static TwitterService getInstance() {
		if (instance == null) {
			instance = new TwitterService();
		}
		return instance;
	}
	
	public void tweet(String content) {
		try {
			twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
			AccessToken accessToken = new AccessToken(accessTokenStr, accessTokenSecretStr);

			twitter.setOAuthAccessToken(accessToken);

			twitter.updateStatus(content);

			System.out.println("Successfully updated the status in Twitter: " + content);
		} catch (TwitterException te) {
			te.printStackTrace();
		}
		System.out.println("TWEET: " + content);
	}
	    
	    public static Properties getPropertiesResource() {
	    	Properties properties = new Properties();
	            ClassLoader cl = Thread.currentThread().getContextClassLoader();
	            java.io.InputStream is = cl.getResourceAsStream("twittersecret.properties");
	            if (is != null) {
	                try {
	                	properties.load(is);
	                } catch (IOException e) {
	                	System.err.println(e);
	                }
	            }
	 
	        return properties;
	    }
	    
	    public static void main(String[] args) {
			TwitterService testInstance = TwitterService.getInstance();
			testInstance.tweet("test");
		}
}
