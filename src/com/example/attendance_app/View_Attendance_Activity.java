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
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class View_Attendance_Activity extends Activity {
	
	private ProgressDialog pDialog;
	private static String url = "http://attapp.pe.hu/authenticate.php";
	JSONArray json_array = null;
	Button show;
	Spinner classname;
	Spinner course;
	String responseServer;
	int atten[]=new int[56];
	TextView period[] =new TextView[58];
	TextView name[] =new TextView[58];
	EditText delivered;
	TextView del_text;
	TableLayout layout;
	TableRow row[]=new TableRow[58];
	ArrayList<String> names=new ArrayList<String>();
	int deliver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_attendance);
		show=(Button)findViewById(R.id.show);
		classname=(Spinner)findViewById(R.id.classes);
		course=(Spinner)findViewById(R.id.courses);
		delivered=(EditText)findViewById(R.id.deliver_edit);
		del_text=(TextView)findViewById(R.id.deliver);
		
		course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			course.setSelection(arg2);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		classname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			classname.setSelection(arg2);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		 
		show.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AsyncT asyncT = new AsyncT();
	            asyncT.execute();
	            try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            layout= (TableLayout) findViewById(R.id.layout);
	    		
	            for (int j = 1; j <= 57; j++) {
	    			row[j] = new TableRow(View_Attendance_Activity.this);
	    			row[j].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
	    			TextView dayOfWeek = new TextView(View_Attendance_Activity.this);
	    			dayOfWeek.setWidth(100);
	    			dayOfWeek.setHeight(120);
	    			if(j==1){
	    				dayOfWeek.setText("S No.");
	    				//dayOfWeek.setHeight(400);
	    			}
	    			else
	    			dayOfWeek.setText(""+(j-1));
	    			//dayOfWeek.setPadding(10, 10, 10, 10);
	    			row[j].addView(dayOfWeek);
	    			name[j] = new TextView(View_Attendance_Activity.this);
    				name[j].setWidth(350);
    				name[j].setHeight(120);
    				name[j].setGravity(Gravity.CENTER);
    				//name[j].setPadding(10, 10, 10, 10);
    				
    				if(j==1){
    					name[j].setText("Names");
    					//period.setLongClickable(false);
    					name[j].setTextSize(15);
    				}
    				else{
    					name[j].setText(names.get(j-2));
    					//period.setLongClickable(true);
    					name[j].setId((j-2));
    					if(names.get(j-2).length()>20)
        					name[j].setTextSize(8);
        				else
        				name[j].setTextSize(15);
        				
    				}
    				
    					row[j].addView(name[j]);
    					
    					if(j==1){
    						name[j].setBackgroundResource(R.drawable.color_non_empty);
    						name[j].setTextColor(0xffffffff);
    						}
    						else{
    							name[j].setBackgroundResource(R.drawable.color_empty);
    						name[j].setTextColor(0xffffffff);
    						}
	    			
	    				period[j] = new TextView(View_Attendance_Activity.this);
	    				period[j].setWidth(350);
	    				period[j].setHeight(120);
	    				period[j].setGravity(Gravity.CENTER);
	    				//period[j].setPadding(10, 10, 10, 10);
	    				
	    				if(j==1){
	    					period[j].setText("Attended");
	    					//period.setLongClickable(false);
	    				}
	    				else{
	    					period[j].setText(""+atten[j-2]);
	    					//period.setLongClickable(true);
	    					period[j].setId((j-2));
	    				}
	    		
	    					period[j].setTextSize(15);
	    					row[j].addView(period[j]);
	    					
	    					if(j==1){
	    						period[j].setBackgroundResource(R.drawable.color_non_empty);
	    						period[j].setTextColor(0xffffffff);
	    						}
	    						else{
	    						period[j].setBackgroundResource(R.drawable.color_empty);
	    						period[j].setTextColor(0xffffffff);
	    						}
	    					
	    					layout.addView(row[j], new TableLayout.LayoutParams(
	    	    					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1.0f));
	    	    					
		    				
	            		}
	            delivered.setText(""+deliver);
	            del_text.setText("Delivered");
	            delivered.setVisibility(View.VISIBLE);
	            del_text.setVisibility(View.VISIBLE);
	           
	            
			}
		});
		
	}
	
	
	
	/* Inner class to get response */
    class AsyncT extends AsyncTask<Void, Void, Void> {
    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(View_Attendance_Activity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}
    	
        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://attapp.pe.hu/fetch.php");
 
            try {
 
                JSONObject jsonobj = new JSONObject();
                
                //jsonobj.put("class_name",classname.getText().toString().toUpperCase());
                jsonobj.put("course_name",course.getSelectedItem().toString().toUpperCase());
                
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
                
                JSONObject c=json_array.getJSONObject(0);
                for (int i = 0; i < 56; i++) {
				atten[i]=Integer.parseInt(c.getString(""+(i+1)));
				names.add(i, c.getString("sn"+(i+1)));
                }
                deliver=Integer.parseInt(c.getString("delivered"));
                delivered.setText(""+deliver);
                
                //new GetAttendance().execute();
                
            	
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            
				//row.addView(deliver);
				//layout.addView(row, new TableLayout.LayoutParams(
				//LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

            
            return null;
        }
        
        
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing())
				pDialog.dismiss();
		
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
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

