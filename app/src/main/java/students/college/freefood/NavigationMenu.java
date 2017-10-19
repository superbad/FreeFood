package students.college.freefood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Robert Bradshaw on 10/7/2017.
 */

public class NavigationMenu extends UserActivity
{
    private ImageView saveButton;
    private Button addEventButton;
    private TextView numMilesText;
    private SeekBar numMilesBar;

    private CheckBox cbNone;
    private CheckBox cbRR;
    private CheckBox cbCE;
    private CheckBox cbHH;
    private CheckBox cbGL;
    private CheckBox cbP;

    private int radius;
    private final int MAX_MILES = 20;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_menu);
        Intent i = getIntent();
        radius = m_user.getRadius();
        findTheViews();
        if(i.getIntExtra("cbNone", 0) == 1){
            cbNone.setChecked(true);
        }
        if(i.getIntExtra("cbRR", 0) == 1){
            cbRR.setChecked(true);
        }
        if(i.getIntExtra("cbCE", 0) == 1){
            cbCE.setChecked(true);
        }
        if(i.getIntExtra("cbHH", 0) == 1){
            cbHH.setChecked(true);
        }
        if(i.getIntExtra("cbGL", 0) == 1){
            cbGL.setChecked(true);
        }
        if(i.getIntExtra("cbP", 0) == 1){
            cbP.setChecked(true);
        }
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
                if(radius < 1)
                {
                    radius = 1;
                }
            }
        });

        cbNone = (CheckBox)findViewById(R.id.cbNone);
        cbCE = (CheckBox)findViewById(R.id.cbCampus);
        cbRR = (CheckBox)findViewById(R.id.cbRR);
        cbGL = (CheckBox)findViewById(R.id.cbGreek);
        cbHH = (CheckBox)findViewById(R.id.cbHappy);
        cbP = (CheckBox)findViewById(R.id.cbPizza);
    }

    public void SaveClick(View view)
    {
        m_user.setRadius(radius);
        //initialize the new set to 0
        for(int i = 0;i < m_user.getFilterLength(); i++)
        {
            m_user.setFilters(i,0);
        }
        //let the object know which ones were enabled
        if(cbNone.isChecked())
        {
            m_user.setFilters(0,1);
        }
        if(cbRR.isChecked())
        {
            m_user.setFilters(1,1);
        }
        if(cbCE.isChecked())
        {
            m_user.setFilters(2,1);
        }
        if(cbHH.isChecked())
        {
            m_user.setFilters(3,1);
        }
        if(cbGL.isChecked())
        {
            m_user.setFilters(4,1);
        }
        if(cbP.isChecked())
        {
            m_user.setFilters(5,1);
        }

        writeUser();
        /*
        //System.out.println("I totally saved.");

        try
        {
            cbNone = (CheckBox)findViewById(R.id.cbNone);
            cbRR = (CheckBox)findViewById(R.id.cbRR);
            cbCE = (CheckBox)findViewById(R.id.cbCampus);
            cbGL = (CheckBox)findViewById(R.id.cbGreek);
            cbHH = (CheckBox)findViewById(R.id.cbHappy);
            cbP = (CheckBox)findViewById(R.id.cbPizza);

            String string = Integer.toString(radius);
            if(cbNone.isChecked()){
                string = string + "\n1";
            }
            else{
                string = string + "\n0";
            }
            if(cbRR.isChecked()){
                string = string + "\n1";
            }
            else{
                string = string + "\n0";
            }
            if(cbCE.isChecked()){
                string = string + "\n1";
            }
            else{
                string = string + "\n0";
            }
            if(cbHH.isChecked()){
                string = string + "\n1";
            }
            else{
                string = string + "\n0";
            }
            if(cbGL.isChecked()){
                string = string + "\n1";
            }
            else{
                string = string + "\n0";
            }
            if(cbP.isChecked()){
                string = string + "\n1";
            }
            else{
                string = string + "\n0";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        */
        Intent i = new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(i);
    }
    public void onCheckboxClicked(View view)
    {

    }

}
