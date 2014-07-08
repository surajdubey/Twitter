package org.codelearn.twitter;

import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	protected static final String tag = "codelearn";
	Context context;
	Configuration configuration;
	ConfigurationBuilder config;
	
	Button _loginBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		_loginBtn = ( Button ) findViewById(R.id.btn_login);
		_loginBtn.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  //loginTweeter();
		    	  new TweetSync().execute();
		      }
		  });
		
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		
		return true;
	}
	
	private class TweetSync extends AsyncTask<Void, Void, RequestToken>
	{
		@Override
		protected RequestToken doInBackground(Void... params) {
			Log.d(tag, "outside try");
			
			return TwitterUtil.getInstance().getRequestToken();
			
		}
		
		@Override
		protected void onPostExecute(RequestToken requesttoken) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requesttoken.getAuthenticationURL()));
			startActivity(intent);
			Log.d(tag, "end of try");

		}
		
	}

}
