package students.college.freefood;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Robert Bradshaw on 10/9/2017.
 */

/**
 * Super class of all of our non-fragment activities. It includes a User object
 * and ways to read and write info to that User object.
 */
public class UserActivity extends Activity
{
    User m_user;
    UserActivity() {
        super();
    }
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        readUser();
    }

    /**
     * reads information about the user from a .data file
     */
    public void readUser()
    {
        try
        {
            FileInputStream fin = new FileInputStream(getFilesDir()+"userData.data");
            ObjectInputStream ois = new ObjectInputStream(fin);
            m_user = (User) ois.readObject();
            fin.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            m_user = new User();

        }
    }

    /**
     * Saves information about the User object into a .data file
     */
    public void writeUser()
    {
        // Write to disk with FileOutputStream
        try
        {
            FileOutputStream fout = new FileOutputStream(getFilesDir()+"userData.data");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(m_user);
            fout.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
