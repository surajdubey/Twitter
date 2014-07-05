package org.codelearn.twitter;

import java.util.List;

import org.codelearn.twitter.models.Tweet;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class TweetDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_detail);
	Tweet  value = (Tweet) getIntent().getSerializableExtra("MyClass");
		TextView u1=(		TextView) findViewById(R.id.tweetTitle);
		
		TextView u2=(		TextView) findViewById(R.id.tweetBody);
		u1.setText(value.getTitle().toString());
		u2.setText(value.getBody().toString());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet_detail, menu);
		return true;
	}

}
