package org.codelearn.twitter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codelearn.twitter.models.Tweet;
import org.json.JSONArray;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class TweetListActivity extends ListActivity{



    private ArrayAdapter tweetItemArrayAdapter;
    private List<Tweet> tweets = new ArrayList<Tweet>();
 
    public static String tag="codelearn";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_tweet_list);
		
		/*tweetItemArrayAdapter = new TweetAdapter(this, tweets);
		setListAdapter(tweetItemArrayAdapter);
		*/	
		new TweetSync().execute();
		
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
		
		//intent.putExtra("MyClass",(Tweet) getListAdapter().getItem(position));
		startActivity(intent);
		
	}
	
	private class TweetSync extends AsyncTask<Void, Void, Void>
	{
		String url="http://app-dev-challenge-endpoint.herokuapp.com/tweets";
		JSONArray jarray;
		@Override
		protected Void doInBackground(Void... params) {
			
			try{
			DefaultHttpClient htppclient = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse response = htppclient.execute(get);
			
			Log.d(tag,"Response Received");
			HttpEntity entity = response.getEntity();
			Log.d(tag,"http Entity");
			
			if(entity!=null)
			{
				Log.d(tag,"Entity not null");
				
				String retStr  = EntityUtils.toString(entity);
				Log.d(tag,"Response is "+retStr);
				
				jarray = new JSONArray(retStr);
			}//if
			
			
			}//try
			
			catch(Exception e)
			{
				Log.d("codelearn",e.getMessage());
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
		
		}
		
	}
}
