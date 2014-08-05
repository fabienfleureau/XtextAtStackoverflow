package fr.fabienfleureau.xtextatstackoverflow.twitter;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

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
		if (!twitterProperties.isEmpty()) {
			consumerKeyStr = twitterProperties.getProperty("consumerKeyStr");
			consumerSecretStr = twitterProperties
					.getProperty("consumerSecretStr");
			accessTokenStr = twitterProperties.getProperty("accessTokenStr");
			accessTokenSecretStr = twitterProperties
					.getProperty("accessTokenSecretStr");
		} else {
			consumerKeyStr = System.getenv("consumerKeyStr");
			consumerSecretStr = System.getenv("consumerSecretStr");
			accessTokenStr = System.getenv("accessTokenStr");
			accessTokenSecretStr = System.getenv("accessTokenSecretStr");
		}
		showVariables();
	}

	private String sixFirstCharacters(String string) {
		if (string == null) {
			return "";
		}
		int length = Integer.min(6, string.length());
		return string.substring(0, length - 1);
	}
	
	private void showVariables() {
		System.out.println("consumerKeyStr: " + sixFirstCharacters(consumerKeyStr));
		System.out.println("consumerSecretStr: " + sixFirstCharacters(consumerSecretStr));
		System.out.println("accessTokenStr: " + sixFirstCharacters(accessTokenStr));
		System.out.println("accessTokenSecretStr: " + sixFirstCharacters(accessTokenSecretStr));
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
		try {
			if (!twitter.getAuthorization().isEnabled()) {
				twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
				AccessToken accessToken = new AccessToken(accessTokenStr,
						accessTokenSecretStr);
				twitter.setOAuthAccessToken(accessToken);
			}
			twitter.updateStatus(content);

			System.out.println("Successfully updated the status in Twitter: "
					+ content);
		} catch (TwitterException te) {
			te.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TwitterWrapper testInstance = TwitterWrapper.getInstance();
		testInstance.tweet("test");
	}

}
