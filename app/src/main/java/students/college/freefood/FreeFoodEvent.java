package students.college.freefood;

import android.location.Location;
import android.util.StringBuilderPrinter;

import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by Robert Bradshaw on 10/7/2017.
 */

public class FreeFoodEvent {
    private String m_name;
    private Location m_location;
    private String m_description;
    private Date m_startTime;
    private Date m_endTime;

    FreeFoodEvent() {
        m_name = "HackUMBC";
        m_description = "We made an app";
        m_startTime = new Date(System.currentTimeMillis());
        m_endTime =m_startTime;
        m_location = new Location("");
    }

    FreeFoodEvent(String name, String descritpion, Location location, Date startTime, Date endTime) {
        m_name = name;
        m_location = location;
        m_description = descritpion;
        m_endTime = endTime;
        m_startTime = startTime;
    }

    //setters
    public void setName(String name) {
        m_name = name;
    }

    public void setDescription(String des) {
        m_description = des;
    }

    public void setLocation(Location loc) {
        m_location = loc;
    }

    public void setStartTime(Date startTime) {
        m_startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        m_endTime = endTime;
    }

    //getters
    public String getName() {
        return m_name;
    }

    public String getDescription() {
        return m_description;
    }

    public Location getLocation() {
        return m_location;
    }

    public Date getStartTime() {
        return m_startTime;
    }

    public Date getEndTime() {
        return m_endTime;
    }

    public String getStartTimeString()
    {
        return dateStringFixer(m_startTime.toString());
    }
    public String getEndTimeString()
    {
        return dateStringFixer(m_endTime.toString());
    }

    private String dateStringFixer(String date)
    {
        String ret = "";
        StringTokenizer tok = new StringTokenizer(date," :");
        int i = 0;
        while(tok.hasMoreElements())
        {
            if(i < 5) {
                ret += tok.nextToken().toString();
                if (i == 3) {
                    ret += ":";
                } else {
                    ret += " ";
                }
            }
            else
            {
                tok.nextToken();
            }
            i++;
        }
        System.out.println("The date is: "+ret);
        return ret;
    }
}
