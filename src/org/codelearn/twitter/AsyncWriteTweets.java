package org.codelearn.twitter;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

import org.codelearn.twitter.models.Tweet;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncWriteTweets extends AsyncTask<List<Tweet>,Void, Void>  {
TweetListActivity test=null;
private static final String TWEETS_CACHE_FILE = "tweet_cache.ser";
public List<Tweet> tweetsRead=new ArrayList<Tweet>();
public AsyncWriteTweets(TweetListActivity act) {
test=act;
}

	@Override
	protected Void doInBackground(List<Tweet>... tweets) {
		
		try
		{
			
			Thread.sleep(5000);
			Log.d("AsyncWriteTweets","Called()");
			test.getFileStreamPath(TWEETS_CACHE_FILE).delete();
			FileOutputStream fis = test.openFileOutput(TWEETS_CACHE_FILE, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fis);
			oos.writeObject(tweets);
			oos.reset();
			oos.close();
			Log.d("File Path",test.getFileStreamPath(TWEETS_CACHE_FILE).getAbsolutePath().toString());		
		}
		catch(Exception e)
		{
			Log.d("Error in Asynch Task",""+e.toString());
		}
		return null;
	}
	
	
}
