package org.codelearn.twitter;

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
	
	int count = 5;
    
	
	
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
        }


	}
	
	public void renderTweet(List<Tweet> tweetList)
	{
		tweetItemArrayAdapter = new TweetAdapter(this, tweetList);
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
		List<Tweet> tweetList = new ArrayList<Tweet>();
		@Override
		protected Void doInBackground(String... params) {
			try {
			
				if(params[0].equals("") == false)
				{
	            	SharedPreferences prefs = getSharedPreferences(TwitterConstants.SHARED_PREFERENCE, MODE_PRIVATE);
	            	requesttoken = TwitterUtil.getInstance().getRequestToken();
	            	twitter = TwitterUtil.getInstance().getTwitter();
	            	Log.d(tag, "Reached inside try Resume");
	                accessToken = twitter.getOAuthAccessToken(requesttoken, params[0]); 
	                Log.d(tag, "Reached inside access token Resume");
	                
	                Editor e = prefs.edit();
	                e.putString(TwitterConstants.PREF_KEY_TOKEN, accessToken.getToken()); 
	                e.putString(TwitterConstants.PREF_KEY_SECRET, accessToken.getTokenSecret());
	                
	                e.commit();
				}
				
				
                Paging paging = new Paging(1,count);
                count = count+5;
            	statuses = twitter.getUserTimeline(paging);
				for(twitter4j.Status st: statuses)
				{
					Log.d(tag, st.getUser().getScreenName()+" : "+st.getText());
					Tweet tweet = new Tweet();
					tweet.setTitle(st.getUser().getScreenName()+" : "+st.getText());
					tweetList.add(tweet);
				}
                
              
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
			renderTweet(tweetList);
		}
		}
	

	
	}
