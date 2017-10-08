package students.college.freefood;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import android.util.Log;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * Created by Robert Bradshaw on 10/7/2017.
 */

public class AddEvent extends Activity
{
    private FreeFoodEvent ffe;
    TextView tvStartDate,tvEndDate;
    Calendar mCurrentDate1,mCurrentDate2;

    int startDay,startMonth,startYear,endDay,endMonth,endYear;

    TextView tvStartTime, tvEndTime;
    Calendar mCurrentTime1,mCurrentTime2;

    int startHour,startMin, endHour, endMin;

    private String name;
    private String description;
    private String location;
    private String category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_event);

        Spinner spinner = (Spinner) findViewById(R.id.spCategory);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        tvStartDate = (TextView) findViewById(R.id.buttonDate1);
        mCurrentDate1 = Calendar.getInstance();
        startDay = mCurrentDate1.get(Calendar.DAY_OF_MONTH);
        startMonth = mCurrentDate1.get(Calendar.MONTH);
        startMonth = startMonth;
        startYear = mCurrentDate1.get(Calendar.YEAR);
        System.out.println((startMonth+1)+"/"+startDay +"/"+startYear);
        tvStartDate.setText((startMonth+1)+"/"+startDay +"/"+startYear);
        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEvent.this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view1, int year1, int monthOfYear1, int dayOfMonth1) {
                        monthOfYear1 = monthOfYear1 + 1;
                        tvStartDate.setText(monthOfYear1+"/"+dayOfMonth1+"/"+year1);
                        startDay = dayOfMonth1;
                        startMonth = monthOfYear1;
                        startYear = year1;
                    }
                }, startYear, startMonth, startDay);
                datePickerDialog.show();
            }
        });



        tvStartTime = (TextView) findViewById(R.id.buttonTime1);
        mCurrentTime1 = Calendar.getInstance();
        startHour = mCurrentTime1.get(Calendar.HOUR_OF_DAY);
        startMin = mCurrentTime1.get(Calendar.MINUTE);
        tvStartTime.setText(startHour +":"+ startMin);
        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v3) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEvent.this,new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view1, int hour1, int min1) {
                        tvStartTime.setText(hour1+":"+min1);
                        startHour = hour1;
                        startMin = min1;
                    }
                }, startHour, startMin, true);
                timePickerDialog.show();
            }
        });

        tvEndDate = (TextView) findViewById(R.id.buttonDate2);
        mCurrentDate2 = Calendar.getInstance();
        endDay = mCurrentDate2.get(Calendar.DAY_OF_MONTH);
        endMonth = mCurrentDate2.get(Calendar.MONTH);
        endYear = mCurrentDate2.get(Calendar.YEAR);
        System.out.println((endMonth+1) +"/" + endDay+"/"+ endYear);
        tvEndDate.setText((endMonth+1) +"/" + endDay+"/"+ endYear);
        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEvent.this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view2, int year2, int monthOfYear2, int dayOfMonth2) {
                        monthOfYear2 = monthOfYear2 + 1;
                        tvEndDate.setText(monthOfYear2+"/"+dayOfMonth2+"/"+year2);
                        endDay = dayOfMonth2;
                        endMonth = monthOfYear2;
                        endYear = year2;
                    }
                }, endYear, endMonth, endDay);
                datePickerDialog.show();
            }
        });


        tvEndTime = (TextView) findViewById(R.id.buttonTime2);
        mCurrentTime2 = Calendar.getInstance();
        endHour = mCurrentTime2.get(Calendar.HOUR_OF_DAY);
        endMin = mCurrentTime2.get(Calendar.MINUTE);
        tvEndTime.setText(endHour +":"+ endMin);
        tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v4) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEvent.this,new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view2, int hour2, int min2) {
                        tvEndTime.setText(hour2+":"+min2);
                        endHour = hour2;
                        endMin = min2;
                    }
                }, endHour, endMin, true);
                timePickerDialog.show();
            }
        });
    }

    public void addThisEvent(View view)
    {

        name = ((EditText)findViewById(R.id.etName)).getText().toString();
        description = ((EditText)findViewById(R.id.etDescription)).getText().toString();
        location =  ((EditText)findViewById(R.id.etLocation)).getText().toString();
        String startTime = startYear+"-"+startMonth+"-"+ startDay +"%20"+ startHour +":"+ startMin +":00";
        String endTime = endYear +"-"+ endMonth +"-"+endDay+"%20"+ endHour +":"+ endMin +":00";
        category = ((Spinner)findViewById(R.id.spCategory)).getSelectedItem().toString();

        String response = "";
        double lat = 39.25;
        double lng = -76.69;

        Geocoder geocoder = new Geocoder(AddEvent.this, Locale.getDefault());
        try {
            List<Address> geoResults = geocoder.getFromLocationName(location, 1);
            int counter = 0;
            while (geoResults.size()==0 && counter< 10) {
                geoResults = geocoder.getFromLocationName(location, 1);
                counter++;
            }
            if (geoResults.size()>0) {
                Address addr = geoResults.get(0);
                lat = addr.getLatitude();
                lng = addr.getLongitude();
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        new addEvent().execute("http://ec2-54-226-112-134.compute-1.amazonaws.com/" +
                "add.php?name=%22" + name + "%22&lat=" + lat + "&long=" + lng + "&description=%22" + description +
                "%22&" + "start=%22" +startTime+ "%22&end=%22" + endTime+ "%22&category=%22"+category+"%22&" +
                "image=%22%22&address=%22" + location + "%22");

        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
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
