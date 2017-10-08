package students.college.freefood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.Buffer;

/**
 * Created by Robert Bradshaw on 10/7/2017.
 */

public class NavigationMenu extends Activity
{
    private ImageView saveButton;
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
        Intent i = getIntent();
        radius = i.getIntExtra("radius",1);
        System.out.println("Im a stupid fuckw who thinks the radius is: "+radius);
        findTheViews();
    }

    public void findTheViews()
    {
        saveButton = (ImageView)findViewById(R.id.bSave);
        addEventButton = (Button)findViewById(R.id.bAddEvent);
        numMilesText = (TextView)findViewById(R.id.tvNumMiles);
        numMilesText.setText(""+radius+" miles");
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
        try
        {
            String string = Integer.toString(radius);
            FileOutputStream outputStream = openFileOutput("Radius.txt", Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        startActivity(i);
    }

}
