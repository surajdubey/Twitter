package org.codelearn.twitter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.AsynchronousCloseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

import org.codelearn.twitter.models.Tweet;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncFetchTweets extends AsyncTask<Void,Void, List<Tweet>> {
	 private List<Tweet> tweets = new ArrayList<Tweet>();
	private TweetListActivity test=null; 
	 private static final String TWEETS_CACHE_FILE = "tweet_cache.ser";
	
	
	 public AsyncFetchTweets(TweetListActivity act) {
		// TODO Auto-generated constructor stub
		 test=act;
	}
	 
	@Override
	protected List<Tweet> doInBackground(Void... params) {
		
		try
		{
		Thread.sleep(5000);
		for ( int i = 0; i < 30; i++ ) {
		    Tweet tweet = new Tweet();
		    tweet.setTitle("A nice header for Tweet # " +i);
		    tweet.setBody("Some random body text for the tweet # " +i);
		    tweets.add(tweet);
		}
		
		Log.d("Calling AsynchWriteTweets", "Call()");
		AsyncWriteTweets test1= new AsyncWriteTweets(test);
		test1.execute(tweets);
		Log.d("AsyncWriteTweets Finished","Finished");
		
		}
		catch(Exception e)
		{
			Log.d("Error in Asynch Task",""+e.toString());	
		}
		return tweets;
	}
	
	
	 protected void onPostExecute(List<Tweet> TweetRead) {
		 Log.d("Call From on Prefetch","Check");
	         test.renderTweets(TweetRead);
	    }
	
}
