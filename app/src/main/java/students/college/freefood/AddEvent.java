package students.college.freefood;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.support.annotation.Nullable;

import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import android.util.Log;
import android.widget.TimePicker;

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
    TextView tvStartDate,tvEndDate;
    Calendar mCurrentDate1,mCurrentDate2;
    int day1,month1,year1,day2,month2,year2;

    TextView tvStartTime, tvEndTime;
    Calendar mCurrentTime1,mCurrentTime2;
    int hour1,min1,hour2,min2;

    private String name;
    private String description;
    private String location;
    private String category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        tvStartDate = (TextView) findViewById(R.id.buttonDate1);
        mCurrentDate1 = Calendar.getInstance();
        day1 = mCurrentDate1.get(Calendar.DAY_OF_MONTH);
        month1 = mCurrentDate1.get(Calendar.MONTH);
        year1 = mCurrentDate1.get(Calendar.YEAR);
        System.out.println(day1+"/"+month1+"/"+year1);

        tvStartDate.setText(day1+"/"+month1+"/"+year1);
        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEvent.this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view1, int year1, int monthOfYear1, int dayOfMonth1) {
                        monthOfYear1 = monthOfYear1 + 1;
                        tvStartDate.setText(dayOfMonth1+"/"+monthOfYear1+"/"+year1);
                    }
                }, year1, month1, day1);
                datePickerDialog.show();
            }
        });

        tvEndDate = (TextView) findViewById(R.id.buttonDate2);
        mCurrentDate2 = Calendar.getInstance();
        day2 = mCurrentDate2.get(Calendar.DAY_OF_MONTH);
        month2 = mCurrentDate2.get(Calendar.MONTH);
        year2 = mCurrentDate2.get(Calendar.YEAR);
        System.out.println(day2+"/"+month2+"/"+year2);


        tvStartTime = (TextView) findViewById(R.id.buttonTime1);
        mCurrentTime1 = Calendar.getInstance();
        hour1 = mCurrentTime1.get(Calendar.HOUR_OF_DAY);
        min1 = mCurrentTime1.get(Calendar.MINUTE);
        tvStartTime.setText(hour1+":"+min1);
        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v3) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEvent.this,new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view1, int hour1, int min1) {
                        tvStartTime.setText(hour1+":"+min1);
                    }
                }, hour1, min1, true);
                timePickerDialog.show();
            }
        });

        tvEndDate.setText(day2+"/"+month2+"/"+year2);
        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEvent.this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view2, int year2, int monthOfYear2, int dayOfMonth2) {
                        monthOfYear2 = monthOfYear2 + 1;
                        tvEndDate.setText(dayOfMonth2+"/"+monthOfYear2+"/"+year2);
                    }
                }, year2, month2, day2);
                datePickerDialog.show();
            }
        });


        tvEndTime = (TextView) findViewById(R.id.buttonTime2);
        mCurrentTime2 = Calendar.getInstance();
        hour2 = mCurrentTime2.get(Calendar.HOUR_OF_DAY);
        min2 = mCurrentTime2.get(Calendar.MINUTE);
        tvEndTime.setText(hour2+":"+min2);
        tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v4) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEvent.this,new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view2, int hour2, int min2) {
                        tvEndTime.setText(hour2+":"+min2);
                    }
                }, hour2, min2, true);
                timePickerDialog.show();
            }
        });
    }

    public void addThisEvent(View view)
    {

        name = ((EditText)findViewById(R.id.etName)).getText().toString();
        description = ((EditText)findViewById(R.id.etDescription)).getText().toString();
        location =  ((EditText)findViewById(R.id.etLocation)).getText().toString();
        String startTime = year1+"-"+month1+"-"+day1+"%20"+hour1+":"+min1+":00";
        String endTime = year2+"-"+month2+"-"+day2+"%20"+hour2+":"+min2+":00";

        new addEvent().execute("http://ec2-54-226-112-134.compute-1.amazonaws.com/" +
                "add.php?name=%22" + name + "%22&lat=" + 39.25 + "&long=" + -76.69 + "&description=%22" + description +
                "%22&" + "start=%22" +startTime+ "%22&end=%22" + endTime+ "%22&category=%22%22&" +
                "image=%22%22&address=%22" + location + "%22");
    }
    private class addEvent extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                System.out.println("LOOK BELOW!");
                System.out.println(params[0]);
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
