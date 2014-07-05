package org.codelearn.twitter;

import java.util.List;

import android.app.Activity;
import org.codelearn.twitter.models.Tweet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TweetAdapter extends ArrayAdapter<Tweet> {

	 private LayoutInflater inflater;
	 private List<Tweet> tweetsLocal;
     
     public TweetAdapter(Activity activity, List<Tweet> tweets){
         super(activity, R.layout.row_tweet, tweets);
         inflater = activity.getWindow().getLayoutInflater();
         tweetsLocal = tweets;
     }
     
     public TweetAdapter(Activity activity,String[] str){
         super(activity, R.layout.row_tweet);
         inflater = activity.getWindow().getLayoutInflater();
       
     }
     
     @Override
     public View getView(int position, View convertView, ViewGroup parent){
         View row = inflater.inflate(R.layout.row_tweet, parent, false);
         TextView title = (TextView) row.findViewById(R.id.tweetTitle);
         Tweet tweet = tweetsLocal.get(position);
         title.setText(tweet.getTitle());
         TextView body = (TextView) row.findViewById(R.id.textView2);
         body.setText(tweet.getBody());
         return row;
     }
	
}
