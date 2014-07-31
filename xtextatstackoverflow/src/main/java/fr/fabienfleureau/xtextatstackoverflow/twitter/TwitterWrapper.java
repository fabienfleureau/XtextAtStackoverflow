package fr.fabienfleureau.xtextatstackoverflow.twitter;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public class TwitterWrapper {

	private static TwitterWrapper instance;

	private String consumerKeyStr = "XXXXXXXXXXXXXXXXXXXXXXXXXXXX";
	private String consumerSecretStr = "XXXXXXXXXXXXXXXXXXXXXXXXXXXX";
	private String accessTokenStr = "XXXXXXXXXXXXXXXXXXXXXXXXXXXX";
	private String accessTokenSecretStr = "XXXXXXXXXXXXXXXXXXXXXXXXXXXX";

	private Twitter twitter;

	public static TwitterWrapper getInstance() {
		if (instance == null) {
			instance = new TwitterWrapper();
		}
		return instance;
	}

	private TwitterWrapper() {
		twitter = new TwitterFactory().getInstance();
		Properties twitterProperties = getPropertiesResource();
		consumerKeyStr = twitterProperties.getProperty("consumerKeyStr");
		consumerSecretStr = twitterProperties.getProperty("consumerSecretStr");
		accessTokenStr = twitterProperties.getProperty("accessTokenStr");
		accessTokenSecretStr = twitterProperties
				.getProperty("accessTokenSecretStr");
	}

	private static Properties getPropertiesResource() {
		Properties properties = new Properties();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		java.io.InputStream is = cl
				.getResourceAsStream("twittersecret.properties");
		if (is != null) {
			try {
				properties.load(is);
			} catch (IOException e) {
				System.err.println(e);
			}
		}
		return properties;
	}

	public void tweet(String content) {
		System.out.println(new Date() + " TWEET: " + content);
		// try {
		// twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
		// AccessToken accessToken = new AccessToken(accessTokenStr,
		// accessTokenSecretStr);
		//
		// twitter.setOAuthAccessToken(accessToken);
		//
		// twitter.updateStatus(content);

		// System.out.println("Successfully updated the status in Twitter: " +
		// content);
		// } catch (TwitterException te) {
		// te.printStackTrace();
		// }
	}

	public static void main(String[] args) {
		TwitterWrapper testInstance = TwitterWrapper.getInstance();
		testInstance.tweet("test");
	}

}
