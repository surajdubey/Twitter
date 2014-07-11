package org.codelearn.twitter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.codelearn.twitter.models.Tweet;

import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

public class TweetListAsync extends AsyncTask<String, Void, Void> {
	List<twitter4j.Status> statuses;
	SharedPreferences prefs;
	List<Tweet> tempTweets = new ArrayList<Tweet>();
	RequestToken requesttoken;
	Twitter twitter;
	
	int count = 5;
	boolean isRefresh = false;
	long latest_tweet_id , last_tweet_id;
	public static String tag="codelearn";
    
	FileOutputStream fos;
	ObjectOutputStream oos;
	FileInputStream fis;
	ObjectInputStream ois;
	
	Editor e;
	Paging paging;
	AccessToken accessToken;
	
	TweetListActivity activity;
	
	public TweetListAsync(TweetListActivity activity) {
		this.activity = activity;
	}


	@Override
	protected Void doInBackground(String... params) {
		
		try {
			tempTweets = new ArrayList<Tweet>();
			prefs = activity.getSharedPreferences(TwitterConstants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        	
			if(params[0].equals("") == false)
			{
            	requesttoken = TwitterUtil.getInstance().getRequestToken();
            	twitter = TwitterUtil.getInstance().getTwitter();
            	Log.d(tag, "Reached inside try Resume");
                accessToken = twitter.getOAuthAccessToken(requesttoken, params[0]); 
            	Log.d(tag, "Reached inside access token Resume");
                
                e = prefs.edit();
                e.putString(TwitterConstants.PREF_KEY_TOKEN, accessToken.getToken()); 
                e.putString(TwitterConstants.PREF_KEY_SECRET, accessToken.getTokenSecret());
                e.commit();

                paging = new Paging(1,count);
                statuses = twitter.getUserTimeline(paging);
                latest_tweet_id = statuses.get(0).getId();
				
              //save latest tweet id
				e = prefs.edit();
				e.putLong(TwitterConstants.LATEST_TWEET_ID, latest_tweet_id);
				e.commit();
				
                
				for(twitter4j.Status st: statuses)
				{
					Tweet tweet = new Tweet();
					
					tweet.setTitle(st.getUser().getScreenName());
					tweet.setBody(st.getText());
					//tweets.add(tweet);

				}
            	
			}
			
			else{
				
				Log.d(tag,"Else in back");
				String keyToken = prefs.getString(TwitterConstants.PREF_KEY_TOKEN, "");
				String keyTokenSecret = prefs.getString(TwitterConstants.PREF_KEY_SECRET,"");
				accessToken = new AccessToken(keyToken, keyTokenSecret);
				TwitterUtil.getInstance().setTwitterFactory(accessToken);
				twitter = TwitterUtil.getInstance().getTwitter();
				try{
					paging = new Paging(1,count);
					paging.sinceId(prefs.getLong(TwitterConstants.LATEST_TWEET_ID, 0L));
	                
					statuses = twitter.getUserTimeline(paging);
					latest_tweet_id = statuses.get(0).getId();
					
					//save latest tweet id
					Editor e = prefs.edit();
					e.putLong(TwitterConstants.LATEST_TWEET_ID, latest_tweet_id);
					e.commit();
					
					Log.d(tag, String.valueOf(latest_tweet_id));
	            	for(twitter4j.Status st: statuses)
					{
						Tweet tweet = new Tweet();
						
						tweet.setTitle(st.getUser().getScreenName());
						tweet.setBody(st.getText());
						tempTweets.add(tweet);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
            	/*tempTweets.addAll(tweets);
            	tweets = tempTweets;
            	*/
			}
			
			Log.d(tag, "Writing to file");
        	
        	/*fos = openFileOutput(TwitterConstants.TWEET_CACHE_FILE, MODE_PRIVATE);
        	oos = new ObjectOutputStream(fos);
        	//oos.writeObject(tweets);
        	Log.d(tag, "Written to file success");
        	oos.reset();
        	oos.close();*/
        	
            
          
        } catch (TwitterException e) { 
                e.printStackTrace();
		}
        
        catch (Exception e) {
        	Log.d(tag, "other exception");
        	e.printStackTrace();
        }

		
		return null;
	}

}
