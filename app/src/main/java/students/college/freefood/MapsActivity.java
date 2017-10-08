package students.college.freefood;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener { //implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;
    private ArrayList<FreeFoodEvent> ffeArray = new ArrayList<FreeFoodEvent>();
    private int mdistance = 20;
    private LatLng mlatLng = new LatLng(38.9783, -76.4925);


    /**
     * When this view is loaded (when a user clicks on our app for the first time, and it wasnt already open)
     * @param savedInstanceState - if there is some cache for the app this is it.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        int r = (int)intent.getIntExtra("radius",20);
        System.out.println("THE RADIUS IS: "+r);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }
        //There needs to be a dynamic way to set this array later
        //ffeArray = new FreeFoodEvent[5];

        //set up array here:
        //for(int i = 0 ; i < 5; i++)
        //{
        //    ffeArray[i] = new FreeFoodEvent();
        //    ffeArray[i].setName("Rick and Morty Season "+i);
        //}
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        keyPrivateVariablesToLayout();
        //generateEventList();
        new getEvents().execute("http://ec2-54-226-112-134.compute-1.amazonaws.com/get.php?lat=" +
                mlatLng.latitude + "&long=" + mlatLng.longitude + "&distance=" + mdistance);
    }

    /**
     * When the app is ready to display the map (immediately after onCreate)
     * @param googleMap some variable for the map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mlatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    /**
     * This is part of letting google know we are allowed to use their map
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    /**
     * What to do when the app is connected to the internet. (connects them to gooogle maps)
     * @param bundle - Im not sure what this is.
     */
    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    /**
     * What to do if the user suspends the app (if we want to do anything?
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * What to do when the user's location is updated
     * @param location - the user's current location
     */
    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        mlatLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mlatLng);
        markerOptions.title("New Marker!");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        new getEvents().execute("http://ec2-54-226-112-134.compute-1.amazonaws.com/get.php?lat=" +
                mlatLng.latitude + "&long=" + mlatLng.longitude + "&distance=" + mdistance);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mlatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    /**
     * What to do if the user has no internect connection?
     * @param connectionResult - the result of checking their connection status
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /*
        Asks the user if we can use their current location.
     */
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Gets the results from asking the user for permission to use their current location
     * @param requestCode - what type of request was asked (in this case it will be a request for location permission)
     * @param permissions - what permissions were given
     * @param grantResults - the results from the user. 1 is allow, -1 is don't allow.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    /**
     * handle marker click event
     * This function should show the user some information about the event going down at
     * the clicked location.
     */
    @Override
    public boolean onMarkerClick(Marker marker) {

        //Intent intnet;//Should link to a layout that gives info about the event
        //getIntent().putExtra();//grab the party info from the database based on the location clicked
        //startActivity(intent);
        Log.w("Click", "test");
        return false;

    }

    /**
     * This is a quick function for initializing the variables of this class to the
     * IDs of different views on the layout.
     */
    public void keyPrivateVariablesToLayout()
    {
    }

    public void onEventsClick(View view)
    {
        Intent i = new Intent(getApplicationContext(),EventList.class);
        i.putExtra("event",ffeArray);
        startActivity(i);
    }

    public void goToOptions(View view)
    {
        Intent i = new Intent(getApplicationContext(),NavigationMenu.class);
        startActivity(i);
    }

    public void generateEventList()
    {
        LinearLayout layoutSpace = (LinearLayout)findViewById(R.id.mainEventListLayout);

        for(int i = 0; i < ffeArray.size(); i++)
        {
            LinearLayout a = new LinearLayout(this);
            a.setOrientation(LinearLayout.HORIZONTAL);
            //a.setMinimumHeight(100);

            //create an image for each event
            ImageView imv = new ImageView(this);
            imv.setMinimumWidth(50);
            imv.setMinimumHeight(50);
            imv.setMaxHeight(50);
            imv.setImageResource(R.mipmap.ic_launcher);
            a.addView(imv);

            // the name of the event (as a text view)
            TextView tv = new TextView(this);
            tv.setText(ffeArray.get(i).getName());
            Log.d("i: "+ i, " array: "+ ffeArray.get(i).toString());
            tv.setWidth(600);
            tv.setHeight(50);
            a.addView(tv);

            //add a button here to get more details
            final Button eventButton = new Button(this);
            eventButton.setText(Integer.toString(i));
            eventButton.setTextSize(0);
            eventButton.setHeight(100);
            eventButton.setWidth(100);
            eventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                //This should be changed in the future to link to the event with description
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), EventDetails.class);
                    System.out.println(ffeArray.get(Integer.parseInt(eventButton.getText().toString())).getName());
                    FreeFoodEvent ffe = ffeArray.get(Integer.parseInt(eventButton.getText().toString()));
                    i.putExtra("event", ffe);
                    startActivity(i);
                }
            });
            a.addView(eventButton);

            //add this layout to the full layout
            layoutSpace.addView(a);
        }
    }

    private class getEvents extends AsyncTask<String, String, String> {

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

        protected void onPostExecute(String jsonStr)
        {
            try {
                JSONArray jsonarray = new JSONArray(jsonStr);
                ffeArray.clear();
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    //"Lon":"-76.7131987","StartTime":"2017-10-07 11:00:00","EndTime":"2017-10-07 23:59:00","Category":"registration","Image":null,"Address":""
                    ffeArray.add(new FreeFoodEvent(jsonobject.getString("EventName"), jsonobject.getString("Description"), jsonobject.getString("Lat"), jsonobject.getString("Lon"), jsonobject.getString("StartTime"), jsonobject.getString("EndTime"), jsonobject.getString("Address")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("freeFoodEvents: ", "> " + ffeArray.toString());
            generateEventList();
        }
    }

}
