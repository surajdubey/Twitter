package org.codelearn.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	protected static final String tag = "codelearn";
	Context context;
	Configuration configuration;
	Button _loginBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SharedPreferences prefs = getSharedPreferences(TwitterConstants.SHARED_PREFERENCE, MODE_PRIVATE);
		_loginBtn = ( Button ) findViewById(R.id.btn_login);
		
		ConfigurationBuilder config = new ConfigurationBuilder();
		config.setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY);
		config.setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET);
		configuration = config.build();
				 
		_loginBtn.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  loginTweeter();
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
	  	TwitterFactory fact = new TwitterFactory(configuration);
	    		Twitter twitter = fact.getInstance();
	    		try{
	    			RequestToken requesttoken = twitter.getOAuthRequestToken("oauth://t4jsample");
	    			//AccessToken accesstoken =null;
	    			this.startActivity(Intent.ACTION_VIEW, Uri.parse(requesttoken.getAuthenticationURL()));
	    		}
	    		
	    		catch(Exception e)
	    		{
	    			Log.d(tag, e.getMessage());
	    		}
	
	}

}
