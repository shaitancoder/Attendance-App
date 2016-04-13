package com.example.attendance_app;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.attendance_app.Mark_Attendance_Activity.AsyncT;
import com.example.attendance_app.Mark_Attendance_Activity.InputStreamToStringExample;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class View_Login extends Activity{

	Button login;
	EditText username,password;
	String responseServer;
	
	private ProgressDialog pDialog;
	private static String url = "http://attapp.pe.hu/authenticate.php";
	JSONArray json_array = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_login);
		login = (Button) findViewById(R.id.login);
		username=(EditText)findViewById(R.id.username);
		password=(EditText)findViewById(R.id.password);
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AsyncT asyncT = new AsyncT();
	            asyncT.execute();
	           //new GetAttendance().execute();
			}
		});
	}
	
	/* Inner class to get response */
    class AsyncT extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://attapp.pe.hu/authenticate.php");
 
            try {
 
                JSONObject jsonobj = new JSONObject();
                
                jsonobj.put("username",username.getText().toString());
                jsonobj.put("password",password.getText().toString());
                
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("req", jsonobj.toString()));
 
                Log.e("mainToPost", "mainToPost" + nameValuePairs.toString());
 
              // Use UrlEncodedFormEntity to send in proper format which we need
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                InputStream inputStream = response.getEntity().getContent();
                InputStreamToStringExample str = new InputStreamToStringExample();
                responseServer = str.getStringFromInputStream(inputStream);
                Log.e("response", "response -----" + responseServer);
                json_array = new JSONArray(responseServer);
                String ans="";
				//for (int i = 0; i < json_array.length(); i++) {
					JSONObject c = json_array.getJSONObject(0);
				ans = c.getString("answer");
				//}
				if(ans.equalsIgnoreCase("1")){
					Intent intent = new Intent(getApplicationContext(),
    						View_Attendance_Activity.class);
                	startActivity(intent);
				}
				else{
                	//Toast.makeText(getApplicationContext(), "Sorry Wrong Username or Password", Toast.LENGTH_LONG).show();
               }
                //new GetAttendance().execute();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    
	private class GetAttendance extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(View_Login.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
			//Toast.makeText(getApplicationContext(), jsonStr, Toast.LENGTH_LONG).show();
			Log.e("Response: ", "> " + jsonStr);
			
			if (jsonStr != null) {
				try {
					json_array = new JSONArray(jsonStr);
					
					// looping through All entries
					String ans="";
					for (int i = 0; i < json_array.length(); i++) {
						JSONObject c = json_array.getJSONObject(i);
					ans = c.getString("answer");
					}
					if(ans.equalsIgnoreCase("1")){
						Intent intent = new Intent(getApplicationContext(),
	    						View_Attendance_Activity.class);
	                	startActivity(intent);
					}
					else{
	                	//Toast.makeText(getApplicationContext(), "Sorry Wrong Username or Password", Toast.LENGTH_LONG).show();
	               }
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
		
			
		}

	}
    
    
    public static class InputStreamToStringExample {
   	 
        public static void main(String[] args) throws IOException {
 
            // intilize an InputStream
            InputStream is =
                    new ByteArrayInputStream("file content..blah blah".getBytes());
 
            String result = getStringFromInputStream(is);
 
            System.out.println(result);
            System.out.println("Done");
            
 
        }
 
        // convert InputStream to String
        private static String getStringFromInputStream(InputStream is) {
 
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();
 
            String line;
            try {
 
                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
 
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return sb.toString();
        }
 
    }

 
       
   
    

}
