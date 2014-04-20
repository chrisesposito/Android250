package com.fsq.homework251_chrisesp;


import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SummaryActivity extends Activity
{
	private TextView username;
	private TextView password;
	static final String STATE_EMAIL = "user_email";
	static final String STATE_PASSWORD = "user_password";

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) 
	{
	
		String userEmail = username.getText().toString();
		String userPassword = password.getText().toString();
		savedInstanceState.putString(STATE_EMAIL, userEmail);
	    savedInstanceState.putString(STATE_PASSWORD, userPassword);
	
	    super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) 
	{
		super.onRestoreInstanceState(savedInstanceState);
	
		// Restore state members from saved instance
		String userEmail = savedInstanceState.getString(STATE_EMAIL);
		String userPassword = savedInstanceState.getString(STATE_PASSWORD);

	     username.setText(userEmail);
	     password.setText(userPassword);
	}

	public SummaryActivity() 
	{
		// TODO Auto-generated constructor stub
	}
	

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        SharedUserData sharedData = SharedUserData.getInstance();
        
        username = (TextView)findViewById(R.id.email_value);
        username.setText(sharedData.getUserEmailAddress());
        
        password = (TextView)findViewById(R.id.password_value);	
        password.setText(sharedData.getUserPassword());
        
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        /*
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        */
    }


}
