package students.college.freefood;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Robert Bradshaw on 10/9/2017.
 */

public class User implements Serializable
{
    private final int LIKED = 0;
    private final int FLAGGED = 1;
    private int m_radius;
    public HashMap<String,boolean[]> eventThoughts;
    private ArrayList<Integer> filters;
    User()
    {
        m_radius = 1;
        eventThoughts = new HashMap<String,boolean[]>();
        filters = new ArrayList<>();
        for(int i = 0; i < 6; i++)
        {
            filters.add(0);
        }
    }
    public int getFilter(int index)
    {
        return filters.get(index);
    }
    public int getFilterLength()
    {
        return filters.size();
    }


    //getters
    public int getRadius()
    {
        return m_radius;
    }
    public boolean getLikedEvent(String event) {
        if (eventThoughts.containsKey(event)) {
            if (eventThoughts.get(event)[LIKED]) {
                return true;
            }
        }

        return false;
    }
    public boolean getFlaggedEvent(String event)
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

    //setters
    public void setRadius(int r)
    {
        m_radius = r;
    }

    public void setLikedEvent(String event, boolean thought)
    {
        if(eventThoughts.containsKey(event))
        {
            eventThoughts.get(event)[LIKED] = thought;
            saveSpace(event);
        }
        else
        {
            boolean[] thoughts = {thought,false};
            eventThoughts.put(event,thoughts);
        }
    }
    public void setFlaggedEvent(String event, boolean thought)
    {
        if(eventThoughts.containsKey(event))
        {
            eventThoughts.get(event)[FLAGGED] = thought;
            saveSpace(event);
        }
        else
        {
            boolean[] thoughts = {false, thought};
            eventThoughts.put(event,thoughts);
        }
    }
    public void setFilters(int index, int i)
    {
        filters.set(index,i);
    }

    private void saveSpace(String event)
    {
      if(!getFlaggedEvent(event) && !getLikedEvent(event))
      {
          eventThoughts.remove(event);
      }
    }



}
