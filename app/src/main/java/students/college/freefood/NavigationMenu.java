package students.college.freefood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Robert Bradshaw on 10/7/2017.
 */

public class NavigationMenu extends Activity
{
    private ImageButton saveButton;
    private Button addEventButton;
    private TextView numMilesText;
    private SeekBar numMilesBar;

    private int radius;
    private final int MAX_MILES = 20;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_menu);

        radius = 1;
        findTheViews();
    }

    public void findTheViews()
    {
        saveButton = (ImageButton)findViewById(R.id.bSave);
        addEventButton = (Button)findViewById(R.id.bAddEvent);
        numMilesText = (TextView)findViewById(R.id.tvNumMiles);
        numMilesBar = (SeekBar) findViewById(R.id.sbNumMiles);
        numMilesBar.setProgress(radius);
        numMilesBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                if(i < 1)
                    radius = 1;
                else
                    radius = i;
                numMilesText.setText(radius+" miles");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                radius = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                radius = seekBar.getProgress();
            }
        });
    }

    public void SaveClick(View view)
    {
        //System.out.println("I totally saved.");
        Intent i = new Intent(getApplicationContext(),MapsActivity.class);
        i.putExtra("radius",radius);
        startActivity(i);
    }

}
