package org.codelearn.twitter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	Button _loginBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SharedPreferences prefs = getSharedPreferences("codelearn_twitter", MODE_PRIVATE);
		_loginBtn = ( Button ) findViewById(R.id.btn_login);
		
		String s=prefs.getString("user", null);
		String s1=prefs.getString("pass", null);
		if(s!=null && s1!=(null))
		{
			  Intent intent = new Intent(MainActivity.this, TweetListActivity.class);
			  startActivity(intent);
			  finish();
		}
				 
		_loginBtn.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	 
		    	  Log.d("codelearn", "button clicked!!");
          		  EditText username = ( EditText ) findViewById(R.id.fld_username);
    			  EditText password = ( EditText ) findViewById(R.id.fld_pwd);		  
    			  SharedPreferences prefs = getSharedPreferences("codelearn_twitter", MODE_PRIVATE);
    			  Editor edit = prefs.edit();
    			  edit.putString("user",username.getText().toString());
    			  Log.d("user",username.getText().toString());
    			  Log.d("pass",password.getText().toString());
    			  edit.commit();
    			  edit.putString("pass",password.getText().toString());
    			  edit.commit();
    			  
    			  /*
    			  String url = "http://app-dev-challenge-endpoint.herokuapp.com/login";
    				JSONObject json = new JSONObject();
    				String token="";
    				
    				try {
    					 Log.d("codelearn", "reached clicked!!");
    	          		 
    					json.put("username", username.getText().toString());
    					json.put("password", password.getText().toString());
    					
    					
    					DefaultHttpClient httpclient = new DefaultHttpClient();
    					HttpPost httppost = new HttpPost(url);
    					
    					StringEntity se = null;
    					se = new StringEntity(json.toString());
    					httppost.setEntity(se);
    					httppost.setHeader("Accept", "application/json");
    					httppost.setHeader("Content-type","application/json");
    					
    					long t = System.currentTimeMillis();
    					
    					HttpResponse response = (HttpResponse)httpclient.execute(httppost);
    					Log.d("codelearn", "HTTPResponse received in [" + (System.currentTimeMillis()-t) + "ms]");
    					
    					HttpEntity entity = response.getEntity();
    					
    					if(entity!=null)
    					{
    						String retStr = EntityUtils.toString(entity);
    						JSONObject jobj = new JSONObject(retStr);
    						
    						Log.d("codelearn", jobj.toString());
    						token=jobj.getString("token");
    						
    					  SharedPreferences prefs = getSharedPreferences("codelearn_twitter", MODE_PRIVATE);
    					  Editor edit = prefs.edit();
    					  
    					  edit.putString("token", token);
    					  edit.commit();
    					  
    					  Log.d("codelearn", "Data written");
    					  

    	    				Intent intent = new Intent(MainActivity.this, TweetListActivity.class);
    						  startActivity(intent);
    						  finish();
    					}
    					
    					
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}*/
    			
    				Intent intent = new Intent(MainActivity.this, TweetListActivity.class);
					  startActivity(intent);
					  finish();
			
    			  
		    			  
		      }
		  });
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

}
