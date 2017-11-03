package students.college.freefood;

/**
 * Created by Robert Bradshaw on 10/7/2017.
 */

import android.app.backup.FullBackupDataOutput;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * This class will be deleted and moved later, but for now it is where I will play with
 * EventList and populate it.
 */
public class EventList extends UserActivity
{
    private Button likeButton,allButton;
    private ArrayList<FreeFoodEvent> fullList;
    private ArrayList<FreeFoodEvent> eventList;
    private int filterSelected; //0 if all, 1 if like (Could be more later)
    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_detail);
        likeButton = findViewById(R.id.likeFilter);
        allButton = findViewById(R.id.allFilter);
        filterSelected = 0;
        Intent intent = getIntent();
        fullList = (ArrayList<FreeFoodEvent>)intent.getExtras().getSerializable("event");
        eventList = new ArrayList<>();

        MobileAds.initialize(this, "ca-app-pub-6153564065949295~3246609206");
        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("57BAA04D839C980E2F9720252A22323C")
                .build();
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);

        buildAll();
        buildList();



    }

    /**
     * Builds the event list out of the full list of events
     */
    public void buildList()
    {
        //the encompassing layout of this screen
        LinearLayout layoutSpace = (LinearLayout) findViewById(R.id.eventListLayout);

        layoutSpace.setDividerPadding(5);

        layoutSpace.removeAllViews();
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
                if(tv.getText().length() >= 15)
                {
                    tv.setText(tv.getText().toString().substring(0,12)+"...");
                }
            }
            else
            {
                tv.setWidth(600);
                //Depending on likes, we have room for 11 character names
                if(tv.getText().length() >= 30)
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
                    startActivityForResult(i,view.getId());
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

    /**
     * filters the events to show liked events, new events, or all of the events
     * @param view - one of the three buttons
     */
    public void filterList(View view)
    {

        likeButton.setBackgroundColor(Color.GRAY);
        allButton.setBackgroundColor(Color.GRAY);
        view.setBackgroundColor(Color.GREEN);

        eventList.clear();
        if(((Button)view).getText().equals("ALL"))
        {
          buildAll();
        }
        else if(((Button)view).getText().equals("LIKED"))
        {
           buildLikes();
        }

        buildList();
    }

    /**
     * Build the event list with all events
     */
    private void buildAll()
    {
        for(int i = 0; i < fullList.size(); i++)
        {
            eventList.add(fullList.get(i));
        }
    }

    /**
     * Build the event list, only keep events the user liked
     */
    public void buildLikes()
    {
        for(int i = 0; i < fullList.size(); i++)
        {
            if(m_user.hasEvent(fullList.get(i).getHash()) && m_user.getLikedEvent(fullList.get(i).getHash()))
            {
                eventList.add(fullList.get(i));
            }
        }
    }

    /**
     * when we get back here from EventDetails, there is some work needed to update the like values
     * @param requestCode - the code sent to event details (the button id that sent us there)
     * @param resultCode - just checks that everythign went ok in event details
     * @param data - data taht event details sends back (number of likes)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //get updated like data for filtering
        readUser();

        //update like here, without having to query
        fullList.get(requestCode).setLikes(data.getIntExtra("Likes",fullList.get(requestCode).getLikes()));
        eventList.clear();

        //show the user what they wanted to filter
        if(filterSelected == 0)
        {
            buildAll();
        }
        else
        {
            buildLikes();
        }

        //rebuild the event list
        buildList();
    }
}
