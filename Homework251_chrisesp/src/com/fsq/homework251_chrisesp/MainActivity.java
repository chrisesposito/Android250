package com.fsq.homework251_chrisesp;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.fsq.homework251_chrisesp.R;;

public class MainActivity extends Activity 
{
	private EditText username;
	private EditText password;
	
	// property names use for UI state save/restore in rotation
	static final String STATE_EMAIL = "user_email";
	static final String STATE_PASSWORD = "user_password";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        username = (EditText)findViewById(R.id.email_address);
        password = (EditText)findViewById(R.id.password_text);	
        
        //hide activity bar, per requirements
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        // code block to load fragment added by ADT as boilerplate part of new project 
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }
    
    // event handler for login button
    // only case not quite correctly handled is if both username and password
    // are missing. In that case, just the first problem is noted in the error message
    public void login(View view) 
    {
    	String user = username.getText().toString();
    	String pw = password.getText().toString();
    	    	
    	if(user.length() > 0) 
    	{
    		if(pw.length() > 0)
    		{
    			String userEmail = username.getText().toString();
    			String userPassword = password.getText().toString();
    			SharedUserData sharedData = SharedUserData.getInstance();
    			sharedData.setUserEmailAddress(userEmail);
    			sharedData.setUserPassword(userPassword);
    			
    			Intent intent = new Intent(this, SummaryActivity.class);
    			startActivity(intent);
    		}
    		else
    		{
    			//missing data - print error
       	     Toast.makeText(getApplicationContext(), "Missing password",
       	    	      Toast.LENGTH_SHORT).show();
    		}
    	}
    	else
    	{
    		//missing data - print error
    	     Toast.makeText(getApplicationContext(), "Missing email address",
    	    	      Toast.LENGTH_SHORT).show();
    	}
    }
    
    // save UI state on rotation so it can be restored later
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) 
	{
		String userEmail = username.getText().toString();
		String userPassword = password.getText().toString();
		savedInstanceState.putString(STATE_EMAIL, userEmail);
	    savedInstanceState.putString(STATE_PASSWORD, userPassword);
	
	    super.onSaveInstanceState(savedInstanceState);
	}

	// restore UI state after rotation
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) 
	{
		super.onRestoreInstanceState(savedInstanceState);

	     username.setText(savedInstanceState.getString(STATE_EMAIL));
	     password.setText(savedInstanceState.getString(STATE_PASSWORD));
	}

	// method added by ADT as boilerplate part of new project
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

 // method added by ADT as boilerplate part of new project
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view. 
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
