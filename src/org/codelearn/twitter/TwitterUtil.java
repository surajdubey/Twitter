package org.codelearn.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtil {

	private Twitter twitter;
	private RequestToken requesttoken;
	private TwitterFactory fact;
	Configuration config;
	ConfigurationBuilder cb;
	
	private static TwitterUtil twitterutil = new TwitterUtil();
	
	public TwitterUtil() {
		
		cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY);
		cb.setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET);
		config = cb.build();
		
		fact = new TwitterFactory(config);
		twitter = fact.getInstance();
		
	}
	
	public RequestToken getRequestToken()
	{
		if(requesttoken == null)
		{
			try {
				requesttoken = twitter.getOAuthRequestToken(TwitterConstants.CALLBACK_URL);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
		return requesttoken;
	}
	
	
	public static TwitterUtil getInstance() {
		return twitterutil;
	}
	
	public Twitter getTwitter() {
		return twitter;
	}
	
	public TwitterFactory getFact() {
		return fact;
	}

}
