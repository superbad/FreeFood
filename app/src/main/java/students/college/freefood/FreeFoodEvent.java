package students.college.freefood;

import java.io.InterruptedIOException;
import java.io.Serializable;
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
    private String m_address;
    private String m_category;
    private String m_hash;

    private int m_numLikes;


    FreeFoodEvent(String name, String descritpion, String lat, String lon, String startTime, String endTime, String address, String category, int likes, String hash) {
        m_name = name;
        m_lat = lat;
        m_lon = lon;
        m_description = descritpion;
       // m_endTime = endTime;
        //m_startTime = startTime;
        m_address = address;
        m_category = category;
        m_numLikes = likes;
        m_hash = hash;

        m_startTime =  convertTime(startTime);
        m_endTime = convertTime(endTime);
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

    public void setAddress(String address){m_address = address;}

    public void setCategory(String cat){m_category = cat;}

    public void setLikes(int likes) {m_numLikes = likes;}

    public void setHash(String m_hash){this.m_hash = m_hash;}

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

    public String getAddress(){return m_address;}

    public String getCategory(){return m_category;}

    public int getCategoryInt()
    {
        switch (m_category)
        {
            case("Registration Required"):
                return R.mipmap.registration;
            case("Campus Event"):
                return R.mipmap.campus;
            case("Happy Hour"):
                return R.mipmap.happyhour;
            case("Greek Lyfe"):
                return R.mipmap.greek;
            case("Pizza"):
                return R.mipmap.pizza;
            default:
                return R.mipmap.free;
        }
    }

    public int getLikes(){return m_numLikes;}
    public String getHash(){return m_hash;}

    /**
     * checks if this ffe is equal to another ffe, based on their hash
     * @param other - another object
     * @return - returns true if this shares its hash with another object
     */
    @Override
    public boolean equals(Object other)
    {
        if(other instanceof FreeFoodEvent)
        {
            if ((m_hash).equals(((FreeFoodEvent)other).m_hash))
                return true;
        }
        return false;
    }

    /**
     * Takes a given time string and converts the hours to 12 times, with am and pm at end
     * @param time - a military time
     * @return - a normal am/pm time
     */
    private String convertTime(String time)
    {
        StringTokenizer tok = new StringTokenizer(time,"- :");
        String year = tok.nextToken();//year
        String month = tok.nextToken();//month
        String day = tok.nextToken();//day
        int hours = Integer.parseInt(tok.nextToken());
        String min = tok.nextToken();

        String ampm = "am";
        if(hours > 12)
        {
            hours -= 12;
            ampm = "pm";
        }
        if(hours == 12)
        {
            ampm = "pm";
        }
        if(hours < 1)
        {
            hours = 12;
        }
        time = year+"-"+month+"-"+day+" "+hours+":"+min+" "+ampm;
        return time;
    }

}
