package students.college.freefood;

import android.location.Location;
import android.util.StringBuilderPrinter;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by Robert Bradshaw on 10/7/2017.
 *
 * Events are made here, and should be found from Json
 */

public class FreeFoodEvent implements Serializable{
    private String m_name;
    private String m_lat;
    private String m_lon;
    private String m_description;
    private String m_startTime;
    private String m_endTime;

    FreeFoodEvent() {
        m_name = "HackUMBC";
        m_description = "We made an app";
        m_startTime = "10/07/2017 10:00";
        m_endTime =m_startTime;
        m_lon = "SOME";
        m_lat = "WHERE";
    }

    FreeFoodEvent(String name, String descritpion, String lat, String lon, String startTime, String endTime) {
        m_name = name;
        m_lat = lat;
        m_lon = lon;
        m_description = descritpion;
        m_endTime = endTime;
        m_startTime = startTime;
    }

    //FreeFoodEvent() //Make this one work with json data

    //setters
    public void setName(String name) {
        m_name = name;
    }

    public void setDescription(String des) {
        m_description = des;
    }

    public void setLat(String lat){m_lat = lat;}

    public void setLon(String lon){m_lon = lon;}

    public void setStartTime(String startTime) {
        m_startTime = startTime;
    }

    public void setEndTime(String endTime) {
        m_endTime = endTime;
    }

    //getters
    public String getName() {
        return m_name;
    }

    public String getDescription() {
        return m_description;
    }

    public String getLat(){return m_lat;}

    public String getLon(){return m_lon;}

    public String getStartTime() {
        return m_startTime;
    }

    public String getEndTime() {
        return m_endTime;
    }


/*
    public String jSonTimeToJava(String date)
    {
        String formattedDate = "";
        try {
            SimpleDateFormat readFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:SS", java.util.Locale.getDefault());
            SimpleDateFormat writeFormat = new SimpleDateFormat( "HH:mm MMM/dd/yyyy", java.util.Locale.getDefault());

            java.util.Date convertedDate = readFormat.parse( date );

            formattedDate = writeFormat.format( convertedDate );

        }
        catch (ParseException e) {

            e.printStackTrace();
        }
        return formattedDate;
    }

    public String javaTimeToJson(String date)
    {
        String formattedDate = "";
        try
        {
            SimpleDateFormat readFormat = new SimpleDateFormat( "HH:mm MMM/dd/yyyy", java.util.Locale.getDefault());
            SimpleDateFormat writeFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:SS", java.util.Locale.getDefault());

            java.util.Date convertedDate = readFormat.parse( date );
            formattedDate = writeFormat.format( convertedDate );
        }
        catch (ParseException e) {

            e.printStackTrace();
        }
        return formattedDate;
    }
*/
}
