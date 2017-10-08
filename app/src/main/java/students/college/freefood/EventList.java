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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.EventListener;

/**
 * This class will be deleted and moved later, but for now it is where I will play with
 * EventList and populate it.
 */
public class EventList extends Activity
{
    private ArrayList<FreeFoodEvent> eventList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_detail);
        //make 5 events (for testing)

        LinearLayout layoutSpace = (LinearLayout) findViewById(R.id.eventListLayout);

        layoutSpace.setDividerPadding(5);
       // Uncomment this part when you are passing the array from MapsActivity

        Intent intent = getIntent();
        eventList = (ArrayList<FreeFoodEvent>)intent.getExtras().getSerializable("event");

        for(int i = 0; i < eventList.size(); i++ ) {
            LinearLayout a = new LinearLayout(this);
            a.setOrientation(LinearLayout.HORIZONTAL);
            a.setMinimumHeight(150);

            //create an image for each event
            //create an image for each event
            ImageView imv = new ImageView(this);
            imv.setMinimumWidth(50);
            imv.setMinimumHeight(50);
            imv.setMaxHeight(50);
            imv.setImageResource(eventList.get(i).getCategoryInt());
            a.addView(imv);

            // the name of the event (as a text view)
            TextView tv = new TextView(this);
            tv.setText(eventList.get(i).getName());
            tv.setWidth(600);
            tv.setHeight(50);
            a.addView(tv);

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

            TextView tv2 = new TextView(this);
            tv2.setHeight(50);
            layoutSpace.addView(tv2);
        }
    }
    public void BackToMap(View view)
    {
        Intent i = new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(i);
    }
}
