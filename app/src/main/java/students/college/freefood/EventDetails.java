package students.college.freefood;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);
        Intent intent = getIntent();
        returnCode = intent.getIntExtra("returnCode",0);
        ffe = (FreeFoodEvent) intent.getExtras().getSerializable("event");
        returnCode = intent.getIntExtra("return",0);

        liked = m_user.getLikedEvent(ffe.getHash());
        flagged = m_user.getFlaggedEvent(ffe.getHash());

        likedButton = (Button)findViewById(R.id.likedButton);
        flaggedButton = (Button)findViewById(R.id.flaggedButton);

        updateLayout();
    }

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
            flaggedButton.setBackgroundColor(Color.GREEN);
        }
        else
        {
            flaggedButton.setBackgroundColor(Color.LTGRAY);
        }

    }

    //buttons
    public void returnToLast(View view)
    {
        finish();
    }

    public void clickedGreatEvent(View view)
    {
        liked = !liked;
        if(liked)
        {
            likedButton.setBackgroundColor(Color.GREEN);
            new API.hitPage().execute("http://ec2-54-226-112-134.compute-1.amazonaws.com/" +
                    "like.php?hash=" + ffe.getHash());
        }
        else
        {
            likedButton.setBackgroundColor(Color.LTGRAY);
            new API.hitPage().execute("http://ec2-54-226-112-134.compute-1.amazonaws.com/" +
                    "unlike.php?hash=" + ffe.getHash());
        }
        m_user.setLikedEvent(ffe.getHash(),liked);
        writeUser();
    }

    public void flagEvent(View view)
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
