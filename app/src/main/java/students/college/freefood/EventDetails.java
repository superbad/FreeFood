package students.college.freefood;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


/**
 * Created by Robert Bradshaw on 10/7/2017.
 */

public class EventDetails extends UserActivity
{
    private int returnCode;
    private FreeFoodEvent ffe;
    Button likedButton;
    Button flaggedButton;
    boolean liked;
    boolean flagged;
    private AdView mAdView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);
        Intent intent = getIntent();

        ffe = (FreeFoodEvent) intent.getExtras().getSerializable("event");
        liked = m_user.getLikedEvent(ffe.getHash());
        flagged = m_user.getFlaggedEvent(ffe.getHash());

        likedButton = (Button)findViewById(R.id.likedButton);
        flaggedButton = (Button)findViewById(R.id.flaggedButton);

        MobileAds.initialize(this, "ca-app-pub-6153564065949295~3246609206");
        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("246CAC462C6CC8310A2951206014F34F")
                .build();
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);

        updateLayout();
    }

    /**
     * This will take info we were passed about the event
     * and populate the various text views with that info
     */
    public void updateLayout()
    {
        TextView eventName = (TextView)findViewById(R.id.eventTextViewName);
        TextView eventDesc = (TextView)findViewById(R.id.eventTextViewDescription);
        TextView eventLoc = (TextView) findViewById(R.id.eventLocation);
        TextView eventTime = (TextView)findViewById(R.id.eventTime);

        eventName.setText(ffe.getName());
        eventDesc.setText(ffe.getDescription());
        eventLoc.setText(ffe.getAddress());
        eventTime.setText(ffe.getStartTime()+"\nto\n "+ffe.getEndTime());

        if(liked)
        {
            likedButton.setBackgroundColor(Color.GREEN);
        }
        else
        {
            likedButton.setBackgroundColor(Color.LTGRAY);
        }

        if(flagged)
        {
            flaggedButton.setBackgroundColor(Color.RED);
        }
        else
        {
            flaggedButton.setBackgroundColor(Color.LTGRAY);
        }

    }

    //buttons

    /**
     * returns user to the mapActivity or EventList screen, depending on where they where before
     * @param view - the back arrow
     */
    public void returnToLast(View view)
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed()
    {
        int likes = ffe.getLikes();
        Intent i = new Intent(getApplicationContext(),EventList.class);
        i.putExtra("Likes",likes);
        setResult(0,i);
        finish();
    }

        /**
         * Will increment the number of likes this event has in the db
         * @param view - like button
         */
    public void clickedGreatEvent(View view)
    {
        liked = !liked;
        if(liked)
        {
            ffe.setLikes(ffe.getLikes()+1);
            likedButton.setBackgroundColor(Color.GREEN);
            new API.hitPage().execute("http://ec2-54-226-112-134.compute-1.amazonaws.com/" +
                    "like.php?hash=" + ffe.getHash());
        }
        else
        {
            ffe.setLikes(ffe.getLikes()-1);
            likedButton.setBackgroundColor(Color.LTGRAY);
            new API.hitPage().execute("http://ec2-54-226-112-134.compute-1.amazonaws.com/" +
                    "unlike.php?hash=" + ffe.getHash());
        }

        m_user.setLikedEvent(ffe.getHash(),liked);
        writeUser();
    }

    /**
     * will ask the user to confirm this choice, then it will increment the flag value in db
     * @param view - the flag button
     */
    public void clickedFlagEvent(View view)
    {
        if(!flagged) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Flagging Event")
                    .setMessage("Are you sure you want to flag this event?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            flagEvent();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else
        {
            flagEvent();
        }
    }

    /**
     *
     * @param view - The location icon
     */
    public void clickLocation(View view){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/search/?api=1&query="+ffe.getLat()+"," + ffe.getLon()));
        startActivity(intent);
    }



    private void flagEvent()
    {
        flagged = !flagged;
        if(flagged)
        {
            flaggedButton.setBackgroundColor(Color.RED);
            new API.hitPage().execute("http://ec2-54-226-112-134.compute-1.amazonaws.com/" +
                    "report.php?hash=" + ffe.getHash());
        }
        else
        {
            flaggedButton.setBackgroundColor(Color.LTGRAY);
            new API.hitPage().execute("http://ec2-54-226-112-134.compute-1.amazonaws.com/" +
                    "unreport.php?hash=" + ffe.getHash());
        }
        m_user.setFlaggedEvent(ffe.getHash(),flagged);
        writeUser();
    }
}
