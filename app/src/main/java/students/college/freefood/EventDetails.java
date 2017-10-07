package students.college.freefood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Robert Bradshaw on 10/7/2017.
 */

public class EventDetails extends Activity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        System.out.println("We got here!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);
        Intent intent = getIntent();
        //FreeFoodEvent ffe = (FreeFoodEvent) intent.getExtras().getSerializable("event");
        //System.out.println(ffe.getName());
    }
}
