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
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Mark_Attendance_Activity extends Activity {
	String class_name="";
	ArrayList<Integer> attendance=new ArrayList<Integer>();
	int array[]=new int[60];
	String responseServer;
	EditText classname,course;
	private ProgressDialog pDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mark_attendance);
		Mark_login.activityMain.finish();
		//setClass();
		classname=(EditText)findViewById(R.id.class_name);
		course=(EditText)findViewById(R.id.course);
		TableLayout layout = (TableLayout) findViewById(R.id.layout);
		
		for (int j = 1; j <= 57; j++) {
			TableRow row = new TableRow(this);
			row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			TextView dayOfWeek = new TextView(this);
			dayOfWeek.setWidth(200);
			if(j==1){
				dayOfWeek.setText("Serial No.");
			}
			else
			dayOfWeek.setText(""+(j-1));
			dayOfWeek.setPadding(10, 10, 10, 10);
			row.addView(dayOfWeek);
				final TextView period = new TextView(this);
				period.setWidth(400);
				period.setGravity(Gravity.CENTER);
				period.setPadding(10, 10, 10, 10);
				
				if(j==1){
					period.setText("Attendance");
					//period.setLongClickable(false);
				}
				else{
					period.setText("Present");
					//period.setLongClickable(true);
					period.setId(j-2);
					array[j-2]=1;
				}
				period.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(period.getText().toString()==""||period.getText().toString()=="Absent"){
							period.setText("Present");
							//attendance.add(period.getId(), 1);
							array[period.getId()]=1;
						}
						else if(period.getText().toString()=="Present"){
							period.setText("Absent");
							//attendance.add(period.getId(), 0);
							array[period.getId()]=0;
						}
						
						period.setBackgroundResource(R.drawable.color_non_empty);
						period.setTextColor(0xffffffff);
						
						
					}
				});
			
					if(j==1){
					period.setBackgroundResource(R.drawable.color_non_empty);
					period.setTextColor(0xffffffff);
					}
					else{
					period.setBackgroundResource(R.drawable.color_empty);
					period.setTextColor(0xffffffff);}
				period.setTextSize(20);
				row.addView(period);
			layout.addView(row, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
		
		
	final Button load=new Button(this);
	load.setClickable(true);
	load.setText("Upload");
	//load.setPadding(40, 40, 40, 40);
	load.setBackgroundColor(Color.LTGRAY);
	
	//load.setBackgroundColor();
	//row2.addView(load);
	//row2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
	layout.addView(load, new TableLayout.LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	load.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			load.setBackgroundResource(R.drawable.color_non_empty);
			load.setTextColor(Color.WHITE);
			AsyncT asyncT = new AsyncT();
            asyncT.execute();
            
		}
	});
	
	}
	
	/* Inner class to get response */
    class AsyncT extends AsyncTask<Void, Void, Void> {
    	
    	
        @Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(Mark_Attendance_Activity.this);
			pDialog.setMessage("Uploading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@SuppressLint("DefaultLocale")
		@Override
        protected Void doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://attapp.pe.hu/upload.php");
 
            try {
 
                JSONObject jsonobj = new JSONObject();
                
                jsonobj.put("class_name",classname.getText().toString().toUpperCase());
                jsonobj.put("course_name",course.getText().toString().toUpperCase());
                for(int i=0;i<56;i++)
                jsonobj.put(""+(i+1),array[i]);
                
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
                pDialog.setMessage("Uploaded Successfully...");
 
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
 
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
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
	public void setClass(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Set Class");
		alertDialog.setMessage("Enter Class Name: ");
		final EditText input = new EditText(this);
		input.requestFocus();
		alertDialog.setView(input);

		alertDialog.setPositiveButton("Set",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						class_name = input.getText().toString();	
					}

				});
		alertDialog.setNeutralButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
					}
				});
		alertDialog.show();


	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
}

