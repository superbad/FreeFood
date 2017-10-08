package students.college.freefood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Robert Bradshaw on 10/7/2017.
 */

public class EventDetails extends Activity
{
    private int returnCode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        System.out.println("We got here!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);
        Intent intent = getIntent();
        FreeFoodEvent ffe = (FreeFoodEvent) intent.getExtras().getSerializable("event");
        returnCode = intent.getIntExtra("return",0);
        System.out.println(ffe.getName());

        updateLayout(ffe);
    }

    public void updateLayout(FreeFoodEvent ffe)
    {
        TextView eventName = (TextView)findViewById(R.id.eventTextViewName);
        TextView eventDesc = (TextView)findViewById(R.id.eventTextViewDescription);
        TextView eventLoc = (TextView) findViewById(R.id.eventLocation);
        TextView eventTime = (TextView)findViewById(R.id.eventTime);

        eventName.setText(ffe.getName());
        eventDesc.setText(ffe.getDescription());
        eventLoc.setText(ffe.getAddress());
        eventTime.setText(ffe.getStartTime()+" - "+ffe.getEndTime());
    }

    public void returnToLast(View view)
    {
        finishActivity(0);
    }
}
