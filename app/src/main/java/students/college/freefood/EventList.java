package students.college.freefood;

/**
 * Created by Robert Bradshaw on 10/7/2017.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * This class will be deleted and moved later, but for now it is where I will play with
 * EventList and populate it.
 */
public class EventList extends UserActivity
{
    private ArrayList<FreeFoodEvent> eventList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_detail);

        //the encompassing layout of this screen
        LinearLayout layoutSpace = (LinearLayout) findViewById(R.id.eventListLayout);

        layoutSpace.setDividerPadding(5);

        Intent intent = getIntent();
        eventList = (ArrayList<FreeFoodEvent>)intent.getExtras().getSerializable("event");

        for(int i = 0; i < eventList.size(); i++ )
        {
            //create a dummy layout that will hold info about this event
            LinearLayout a = new LinearLayout(this);
            a.setOrientation(LinearLayout.HORIZONTAL);
            a.setMinimumHeight(150);

            //create an image for each event
            ImageView imv = new ImageView(this);
            imv.setMinimumWidth(50);
            imv.setMinimumHeight(75);
            imv.setMaxHeight(75);
            imv.setImageResource(eventList.get(i).getCategoryInt());
            a.addView(imv);

            // the name of the event (as a text view)
            TextView tv = new TextView(this);
            tv.setText(eventList.get(i).getName());


            //check if we are showing likes
            if(eventList.get(i).getLikes() > 0)
            {
                tv.setWidth(300);
                //Depending on likes, we have room for 11 character names
                if(tv.getText().length() > 15)
                {
                    tv.setText(tv.getText().toString().substring(0,12)+"...");
                }
            }
            else
            {
                tv.setWidth(600);
                //Depending on likes, we have room for 11 character names
                if(tv.getText().length() > 30)
                {
                    tv.setText(tv.getText().toString().substring(0,25)+"...");
                }
            }
            tv.setHeight(75);
            a.addView(tv);

            //if it had likes
            if(eventList.get(i).getLikes() > 0) {
                // how many likes it has
                TextView numLikes = new TextView(this);
                numLikes.setText("Likes:" + eventList.get(i).getLikes());
                numLikes.setWidth(300);
                numLikes.setHeight(75);
                a.addView(numLikes);
            }

            //add a button here to get more details
            final Button eventButton = new Button(this);
            eventButton.setId(i);
            eventButton.setText("Details");
            eventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                //This should be changed in the future to link to the event with description
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), EventDetails.class);
                    System.out.println(eventList.get(eventButton.getId()).getName());
                    FreeFoodEvent ffe = eventList.get(eventButton.getId());
                    i.putExtra("event", ffe);
                    startActivity(i);
                }
            });
            a.addView(eventButton);

            //add this layout to the full layout
            layoutSpace.addView(a);

            //this bufferes each dummy layout
            TextView tv2 = new TextView(this);
            tv2.setHeight(75);
            layoutSpace.addView(tv2);
        }
    }

    /**
     * sends user to the mapActivity screen
     * @param view - down arrow
     */
    public void BackToMap(View view)
    {
        Intent i = new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(i);
    }
}
