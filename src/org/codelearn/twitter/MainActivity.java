package org.codelearn.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	protected static final String tag = "codelearn";
	Context context;
	Configuration configuration;
	ConfigurationBuilder config;
	TwitterFactory fact;
	Button _loginBtn;
	private static RequestToken requesttoken;
	private static Twitter twitter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SharedPreferences prefs = getSharedPreferences(TwitterConstants.SHARED_PREFERENCE, MODE_PRIVATE);
		_loginBtn = ( Button ) findViewById(R.id.btn_login);

		/**
		 * Handle OAuth Callback
		 */
		Uri uri = getIntent().getData();
		if (uri != null && uri.toString().startsWith(TwitterConstants.CALLBACK_URL)) {
			String verifier = uri.getQueryParameter(TwitterConstants.IEXTRA_OAUTH_VERIFIER);
            try { 
                AccessToken accessToken = twitter.getOAuthAccessToken(requesttoken, verifier); 
                Editor e = prefs.edit();
                e.putString(TwitterConstants.PREF_KEY_TOKEN, accessToken.getToken()); 
                e.putString(TwitterConstants.PREF_KEY_SECRET, accessToken.getTokenSecret()); 
                e.commit();
	        } catch (Exception e) { 
	                Log.e(tag, e.getMessage()); 
	                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show(); 
			}
        }
				 
		_loginBtn.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  //loginTweeter();
		    	  new TweetSync(MainActivity.this).execute();
		      }
		  });
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void loginTweeter()
	{
		ConfigurationBuilder config = new ConfigurationBuilder();
		config.setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY);
		config.setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET);
		configuration = config.build();
		
		TwitterFactory fact = new TwitterFactory(configuration);
		twitter = fact.getInstance();
		Log.d(tag, "outside try");
		try{
			requesttoken = twitter.getOAuthRequestToken(TwitterConstants.CALLBACK_URL);
			Log.d(tag, "inside try");
			//AccessToken accesstoken =null;
			this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requesttoken.getAuthenticationURL())));
			
			Log.d(tag, "end of try");
		}
		
		catch(TwitterException e)
		{

			e.printStackTrace();
			 Log.e(tag, "Error code : " + e.getErrorCode());
             Log.e(tag, "Error message : " + e.getErrorMessage());
             Log.e(tag, "Status code : " + e.getStatusCode());
             Log.e(tag, "Access level : " + e.getAccessLevel());
		}

	}
	
	private class TweetSync extends AsyncTask<Void, Void, Void>
	{
		MainActivity activity;
		public TweetSync(MainActivity activity) {
			this.activity = activity;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			config = new ConfigurationBuilder();
			config.setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY);
			config.setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET);
			configuration = config.build();
			
			fact = new TwitterFactory(configuration);
			twitter = fact.getInstance();
			Log.d(tag, "outside try");
			try{
				requesttoken = twitter.getOAuthRequestToken(TwitterConstants.CALLBACK_URL);
				Log.d(tag, "inside try");
				//AccessToken accesstoken =null;
					}
			
			catch(TwitterException e)
			{

				e.printStackTrace();
				 Log.e(tag, "Error code : " + e.getErrorCode());
	             Log.e(tag, "Error message : " + e.getErrorMessage());
	             Log.e(tag, "Status code : " + e.getStatusCode());
	             Log.e(tag, "Access level : " + e.getAccessLevel());
			}
			
			catch(Exception e)
			{
				 Log.e(tag, "Error : " + e.getMessage());
		            
			}


			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requesttoken.getAuthenticationURL())));
			

			Log.d(tag, "end of try");

		}
		
	}

}
