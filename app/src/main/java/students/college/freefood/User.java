package students.college.freefood;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Robert Bradshaw on 10/9/2017.
 */

public class User implements Serializable
{
    private final int LIKED = 0;
    private final int HATED = 1;
    private final int FLAGGED = 2;
    private int m_radius;
    public HashMap<FreeFoodEvent,boolean[]> eventThoughts;
    private ArrayList<Integer> filters;
    User()
    {
        m_radius = 1;
        eventThoughts = new HashMap<FreeFoodEvent,boolean[]>();
        filters = new ArrayList<>();
        for(int i = 0; i < 6; i++)
        {
            filters.add(0);
        }
    }
    public int getRadius()
    {
        return m_radius;
    }
    public boolean getLikedEvent(FreeFoodEvent event)
    {
        if(eventThoughts.containsKey(event))
        {
            if(eventThoughts.get(event)[LIKED])
            {
                return true;
            }
            System.out.println("THE EVENT: "+event.getName()+" DOES EXIST.");

        }
        System.out.println("THE EVENT: "+event.getName()+" DID NOT EXIST.");

        return false;
    }
    public boolean getHatedEvent(FreeFoodEvent event)
    {
        if(eventThoughts.containsKey(event))
        {
            if(eventThoughts.get(event)[HATED])
            {
                return true;
            }
        }
        return false;
    }
    public boolean getFlaggedEvent(FreeFoodEvent event)
    {
        if(eventThoughts.containsKey(event))
        {
            if(eventThoughts.get(event)[FLAGGED])
            {
                return true;
            }
        }
        return false;
    }
    public void setRadius(int r)
    {
        m_radius = r;
    }

    public void setLikedEvent(FreeFoodEvent event, boolean thought)
    {
        if(eventThoughts.containsKey(event))
        {
            eventThoughts.get(event)[LIKED] = thought;
            saveSpace(event);
        }
        else
        {
            boolean[] thoughts = {thought,false,false};
            eventThoughts.put(event,thoughts);
        }
    }
    public void setHatedEvent(FreeFoodEvent event, boolean thought)
    {
        if(eventThoughts.containsKey(event))
        {
            eventThoughts.get(event)[HATED] = thought;
            saveSpace(event);
        }
        else
        {
            boolean[] thoughts = {thought,false,false};
            eventThoughts.put(event,thoughts);
            System.out.println(eventThoughts.containsKey(event));
        }
    }
    public void setFlaggedEvent(FreeFoodEvent event, boolean thought)
    {
        if(eventThoughts.containsKey(event))
        {
            eventThoughts.get(event)[FLAGGED] = thought;
            saveSpace(event);
        }
        else
        {
            boolean[] thoughts = {thought,false,false};
            eventThoughts.put(event,thoughts);
        }
    }
    public void setFilters(int index, int i)
    {
        filters.set(index,i);
    }
    public int getFilter(int index)
    {
        return filters.get(index);
    }
    public int getFilterLength()
    {
        return filters.size();
    }

    private void saveSpace(FreeFoodEvent event)
    {
      if(!getFlaggedEvent(event) && !getLikedEvent(event) && !getHatedEvent(event))
      {
         // eventThoughts.remove(event);
      }
    }



}
