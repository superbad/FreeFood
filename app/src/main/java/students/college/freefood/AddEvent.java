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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

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

public class AddEvent extends UserActivity
{
    private FreeFoodEvent ffe;
    TextView tvStartDate,tvEndDate;
    Calendar mCal;

    int startDay,startMonth,startYear,endDay,endMonth,endYear;

    TextView tvStartTime, tvEndTime;

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

        //Set Time Spinner
        Spinner spinner = (Spinner) findViewById(R.id.spCategory);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        setUpCalendarVars();

        doTheDateAndTimeStuff();

    }

    public void addThisEvent(View view)
    {

        name = ((EditText)findViewById(R.id.etName)).getText().toString();
        description = ((EditText)findViewById(R.id.etDescription)).getText().toString();
        location =  ((EditText)findViewById(R.id.etLocation)).getText().toString();

        String startTime = startYear+"-"+(startMonth+1)+"-"+ startDay +"%20"+ startHour +":"+ startMin +":00";
        String endTime = endYear +"-"+ (endMonth+1) +"-"+endDay+"%20"+ endHour +":"+ endMin +":00";
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

        if(location.equalsIgnoreCase("My Current Location")) {
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            String fullAddress = "";
            try {
                addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                fullAddress += address + ", " + city + ", " + state;
            } catch (Exception e) {
                fullAddress = "UMBC";
            }
            location = fullAddress;
        }
        new API.hitPage().execute("http://ec2-54-226-112-134.compute-1.amazonaws.com/" +
                "add.php?name=%22" + name + "%22&lat=" + lat + "&long=" + lng + "&description=%22" + description +
                "%22&" + "start=%22" +startTime+ "%22&end=%22" + endTime+ "%22&category=%22"+category+"%22&" +
                "image=%22%22&address=%22" + location + "%22");

        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("Toast","Your event has been added!");
        startActivity(intent);
    }
    private void setUpCalendarVars(){
        mCal = Calendar.getInstance();
        startDay = mCal.get(Calendar.DAY_OF_MONTH);
        startMonth = mCal.get(Calendar.MONTH);
        startYear = mCal.get(Calendar.YEAR);
        startHour = mCal.get(Calendar.HOUR_OF_DAY);
        startMin = mCal.get(Calendar.MINUTE);

        endDay = mCal.get(Calendar.DAY_OF_MONTH);
        endMonth = mCal.get(Calendar.MONTH);
        endYear = mCal.get(Calendar.YEAR);
        endHour = mCal.get(Calendar.HOUR_OF_DAY);
        endMin = mCal.get(Calendar.MINUTE);

        tvStartDate = (TextView) findViewById(R.id.buttonDate1);
        tvStartTime = (TextView) findViewById(R.id.buttonTime1);

        tvEndDate = (TextView) findViewById(R.id.buttonDate2);
        tvEndTime = (TextView) findViewById(R.id.buttonTime2);
    }

    private void doTheDateAndTimeStuff() {
        //set the text that's displayed initially
        System.out.println((startMonth+1)+"/"+startDay +"/"+startYear);
        tvStartDate.setText((startMonth+1)+"/"+startDay +"/"+startYear);
        tvStartTime.setText(startHour +":"+ startMin);
        System.out.println((endMonth+1) +"/" + endDay+"/"+ endYear);
        tvEndDate.setText((endMonth+1) +"/" + endDay+"/"+ endYear);
        tvEndTime.setText(endHour +":"+ endMin);

        //do this when the start date is clicked
        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEvent.this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view1, int year1, int monthOfYear1, int dayOfMonth1) {
                        //Change the text shown
                        tvStartDate.setText((monthOfYear1+1)+"/"+dayOfMonth1+"/"+year1);
                        //change the variables
                        startDay = dayOfMonth1;
                        startMonth = monthOfYear1;
                        startYear = year1;
                    }
                }, startYear, startMonth, startDay);
                datePickerDialog.show();
            }
        });

        //do this when the end date is clicked
        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEvent.this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view2, int year2, int monthOfYear2, int dayOfMonth2) {
                        //Change the text shown
                        tvEndDate.setText((monthOfYear2+1)+"/"+dayOfMonth2+"/"+year2);
                        //change the variables
                        endDay = dayOfMonth2;
                        endMonth = monthOfYear2;
                        endYear = year2;
                    }
                }, endYear, endMonth, endDay);
                datePickerDialog.show();
            }
        });

        //do this when the start time is clicked
        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v3) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEvent.this,new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view1, int hour1, int min1) {
                        //change the text shown
                        String showMin;
                        if(min1 < 10) {
                            showMin = "0" + Integer.toString(min1);
                        }
                        else{
                            showMin = Integer.toString(min1);
                        }
                        tvStartTime.setText(hour1+":"+showMin);
                        //change the variables
                        startHour = hour1;
                        startMin = min1;
                    }
                }, startHour, startMin, true);
                timePickerDialog.show();
            }
        });

        //do this when the end time is clicked
        tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v4) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEvent.this,new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view2, int hour2, int min2) {
                        //change the text shown
                        String showMin;
                        if(min2 < 10) {
                            showMin = "0" + Integer.toString(min2);
                        }
                        else{
                            showMin = Integer.toString(min2);
                        }
                        tvEndTime.setText(hour2+":"+showMin);
                        //change the variables
                        endHour = hour2;
                        endMin = min2;
                    }
                }, endHour, endMin, true);
                timePickerDialog.show();
            }
        });
    }
}
