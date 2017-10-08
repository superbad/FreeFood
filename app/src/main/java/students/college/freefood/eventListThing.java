package students.college.freefood;

/**
 * Created by Robert Bradshaw on 10/7/2017.
 */

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.EventListener;

/**
 * This class will be deleted and moved later, but for now it is where I will play with
 * EventList and populate it.
 */
public class eventListThing extends Activity
{
    private FreeFoodEvent[] eventList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_detail);
        //make 5 events (for testing)

        LinearLayout layoutSpace = (LinearLayout) findViewById(R.id.eventListLayout);

        /*Uncomment this part when you are passing the array from MapsActivity
        Intent i = getIntent();
        FreeFoodEvent[] eventList = (FreeFoodEvent[]) intent.getExtras().getSerializable("event");

        */
         eventList = new FreeFoodEvent[5];
        for(int i = 0; i < eventList.length; i++ )
        {
            eventList[i] = new FreeFoodEvent();
            eventList[i].setName("HackUmbc, but its " + i + " times better!");

            LinearLayout a = new LinearLayout(this);
            a.setOrientation(LinearLayout.HORIZONTAL);
            a.setMinimumHeight(150);

            //create an image for each event

            // the name of the event (as a text view)
            TextView tv = new TextView(this);
            tv.setText(eventList[i].getName());
            tv.setWidth(900);
            tv.setHeight(50);
            a.addView(tv);

            //add a button here to get more details
            final Button eventButton = new Button(this);
            eventButton.setText(Integer.toString(i));
            eventButton.setTextSize(0);
            eventButton.setHeight(100);
            eventButton.setWidth(100);
            eventButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                //This should be changed in the future to link to the event with description
                public void onClick(View view)
                {
                    Intent i = new Intent(getApplicationContext(),EventDetails.class);
                    System.out.println(eventList[Integer.parseInt(eventButton.getText().toString())].getName());
                    FreeFoodEvent ffe = eventList[Integer.parseInt(eventButton.getText().toString())];
                    i.putExtra("event",ffe);
                    startActivity(i);
                }
            });
            a.addView(eventButton);

            //add this layout to the full layout
            layoutSpace.addView(a);
        }
    }
}
