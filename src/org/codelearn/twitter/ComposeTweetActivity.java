package org.codelearn.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ComposeTweetActivity extends Activity {
	
	Twitter twitter;
	String tweetToPost;
	public String tag = "codelearn";
	Context context = this;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		
		final EditText fld_compose = (EditText) findViewById(R.id.fld_compose);
		Button btn_submit = (Button) findViewById(R.id.btn_submit);
		
		btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tweetToPost = fld_compose.getText().toString();
				new TweetAsync().execute();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class TweetAsync extends AsyncTask<Void, Void, Void>
	{
		
		ProgressDialog pd;
		
		
		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(context, "Please Wait", "Posting Tweet!!" , true);
		}
		@Override
		protected Void doInBackground(Void... params) {
			
			twitter = TwitterUtil.getInstance().getTwitter();
			try {
				twitter4j.Status response = twitter.updateStatus(tweetToPost);
				Log.d(tag, "Status : "+response.getText());
			} catch (TwitterException e) {
				e.printStackTrace();
				Log.d(tag, "Error!!");
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			pd.dismiss();
		}
		
		
		
	}


}
