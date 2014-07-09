package org.codelearn.twitter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import org.codelearn.twitter.models.Tweet;

import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class TweetListActivity extends ListActivity{



    private ArrayAdapter tweetItemArrayAdapter;
    public List<Tweet> tweets = new ArrayList<Tweet>();
 
    public static String tag="codelearn";
	
    Twitter twitter;
	RequestToken requesttoken;
	AccessToken accessToken;
	Paging paging;
	
	int count = 5;
	boolean isRefresh = false;
	long latest_tweet_id , last_tweet_id;
    
	FileOutputStream fos;
	ObjectOutputStream oos;
	FileInputStream fis;
	ObjectInputStream ois;
	
	Editor e;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_tweet_list);
		
		/**
		 * Handle OAuth Callback
		 */
		Uri uri = getIntent().getData();
		if (uri != null && uri.toString().startsWith(TwitterConstants.CALLBACK_URL)) {
			String verifier = uri.getQueryParameter(TwitterConstants.IEXTRA_OAUTH_VERIFIER);
			new TweetList().execute(verifier);

			renderTweet();
        }
		
		else
		{
			isRefresh = true;
			
			try {
				fis = openFileInput(TwitterConstants.TWEET_CACHE_FILE);
				ois = new ObjectInputStream(fis);
				tweets = (List<Tweet>) ois.readObject();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			new TweetList().execute("");

			renderTweet();
		}
		


	}
	
	public void renderTweet()
	{
		tweetItemArrayAdapter = new TweetAdapter(this, tweets);
		setListAdapter(tweetItemArrayAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet_list, menu);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, TweetDetailActivity.class);
	
		startActivity(intent);
		
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.refresh:
			
			//Toast.makeText(getApplicationContext(), "Refresh", Toast.LENGTH_SHORT).show();
			isRefresh = true;
			new TweetList().execute("");
			Log.d(tag, "Refresh Item!!");
			return true;
			
		case R.id.composeTweet:
			
			Intent intent = new Intent(this , ComposeTweetActivity.class);
			startActivity(intent);
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	private class TweetList extends AsyncTask<String, Void, Void>
	{
		List<twitter4j.Status> statuses;
		SharedPreferences prefs;
		List<Tweet> tempTweets = new ArrayList<Tweet>();

		@Override
		protected Void doInBackground(String... params) {
			try {
				tempTweets = new ArrayList<Tweet>();
				prefs = getSharedPreferences(TwitterConstants.SHARED_PREFERENCE, MODE_PRIVATE);
            	
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
						//Log.d(tag, st.getUser().getScreenName()+" : "+st.getText());
						Tweet tweet = new Tweet();
						
						tweet.setTitle(st.getUser().getScreenName());
						tweet.setBody(st.getText());
						tweets.add(tweet);
						
						//last_tweet_id = st.getId();
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
							//Log.d(tag, st.getUser().getScreenName()+" : "+st.getText());
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
	            	tempTweets.addAll(tweets);
	            	tweets = tempTweets;
	            	//paging = new Paging(1,count).sinceId(prefs.getLong(TwitterConstants.LAST_FETCH_TWEET , 0L));
					
				}
				
				//count = count+5;
            	
				/*Editor e = prefs.edit();
				
				//e.putLong(TwitterConstants.LAST_FETCH_TWEET, statuses.get(statuses.size()-1).getId());
				e.putLong(TwitterConstants.LAST_FETCH_TWEET, last_tweet_id);
				e.commit();
				*/
            	Log.d(tag, "Writing to file");
            	
            	fos = openFileOutput(TwitterConstants.TWEET_CACHE_FILE, MODE_PRIVATE);
            	oos = new ObjectOutputStream(fos);
            	oos.writeObject(tweets);
            	Log.d(tag, "Written to file success");
            	oos.close();
            	fos.close();
                
              
	        } catch (TwitterException e) { 
	                e.printStackTrace();
			}
            
            catch (Exception e) {
            	Log.d(tag, "other exception");
            	e.printStackTrace();
            }
			return null;
			}
		
		@Override
		protected void onPostExecute(Void result) {
			renderTweet();
		}
		}
	

	
	}
