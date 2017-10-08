package students.college.freefood;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Robert Bradshaw on 10/7/2017.
 */

public class AddEvent extends Activity
{
    private FreeFoodEvent ffe;
    TextView tv1,tv2;
    Calendar mCurrentDate1,mCurrentDate2;
    int day1,month1,year1,day2,month2,year2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        tv1 = (TextView) findViewById(R.id.buttonDate1);
        mCurrentDate1 = Calendar.getInstance();
        day1 = mCurrentDate1.get(Calendar.DAY_OF_MONTH);
        month1 = mCurrentDate1.get(Calendar.MONTH);
        year1 = mCurrentDate1.get(Calendar.YEAR);
        System.out.println(day1+"/"+month1+"/"+year1);

        month1 = month1 + 1;

        tv1.setText(day1+"/"+month1+"/"+year1);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEvent.this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view1, int year1, int monthOfYear1, int dayOfMonth1) {
                        monthOfYear1 = monthOfYear1 + 1;
                        tv1.setText(dayOfMonth1+"/"+monthOfYear1+"/"+year1);
                    }
                }, year1, month1, day1);
                datePickerDialog.show();
            }
        });

        tv2 = (TextView) findViewById(R.id.buttonDate2);
        mCurrentDate2 = Calendar.getInstance();
        day2 = mCurrentDate2.get(Calendar.DAY_OF_MONTH);
        month2 = mCurrentDate2.get(Calendar.MONTH);
        year2 = mCurrentDate2.get(Calendar.YEAR);
        System.out.println(day2+"/"+month2+"/"+year2);

        month2 = month2 + 1;

        tv2.setText(day2+"/"+month2+"/"+year2);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEvent.this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view2, int year2, int monthOfYear2, int dayOfMonth2) {
                        monthOfYear2 = monthOfYear2 + 1;
                        tv2.setText(dayOfMonth2+"/"+monthOfYear2+"/"+year2);
                    }
                }, year2, month2, day2);
                datePickerDialog.show();
            }
        });
    }

    private class addEvent extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }
}
