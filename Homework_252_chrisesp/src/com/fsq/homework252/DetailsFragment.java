// DetailsFragment.java
// Displays one contact's details
package com.fsq.homework252;

import com.fsq.homework252.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment
{
   // callback methods implemented by MainActivity  
   public interface DetailsFragmentListener
   {
      // called when a contact is deleted
      public void onContactDeleted();
      
      // called to pass Bundle of contact's info for editing
      public void onEditContact(Bundle arguments);
   }
   
   private DetailsFragmentListener listener;
   
   private long rowID = -1; 
   private TextView nameTextView;  
   private TextView emailTextView; 
   
   // set DetailsFragmentListener when fragment attached   
   @Override
   public void onAttach(Activity activity)
   {
      super.onAttach(activity);
      listener = (DetailsFragmentListener) activity;
   }
   
   // remove DetailsFragmentListener when fragment detached
   @Override
   public void onDetach()
   {
      super.onDetach();
      listener = null;
   }

   // called when DetailsFragmentListener's view needs to be created
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
   {
      super.onCreateView(inflater, container, savedInstanceState);  
      setRetainInstance(true); // save fragment across config changes

      // if DetailsFragment is being restored, get saved row ID
      if (savedInstanceState != null) 
      {
         rowID = savedInstanceState.getLong(MainActivity.ROW_ID);
      }
      else 
      {
         // get Bundle of arguments then extract the contact's row ID
         Bundle arguments = getArguments(); 
         
         if (arguments != null)
         {
            rowID = arguments.getLong(MainActivity.ROW_ID);
         }
      }
         
      // inflate DetailsFragment's layout
      View view = inflater.inflate(R.layout.fragment_details, container, false);               
      setHasOptionsMenu(true); // this fragment has menu items to display

      // get the EditTexts
      nameTextView = (TextView) view.findViewById(R.id.nameTextView);
      emailTextView = (TextView) view.findViewById(R.id.emailTextView);

      return view;
   }
   
   // called when the DetailsFragment resumes
   @Override
   public void onResume()
   {
      super.onResume();
      new LoadContactTask().execute(rowID); // load contact at rowID
   } 

   // save currently displayed contact's row ID
   @Override
   public void onSaveInstanceState(Bundle outState) 
   {
       super.onSaveInstanceState(outState);
       outState.putLong(MainActivity.ROW_ID, rowID);
   }

   // display this fragment's menu items
   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
   {
      super.onCreateOptionsMenu(menu, inflater);
      inflater.inflate(R.menu.fragment_details_menu, menu);
   }

   // handle menu item selections
   @Override
   public boolean onOptionsItemSelected(MenuItem item) 
   {
      switch (item.getItemId())
      {
         case R.id.action_edit: 
            // create Bundle containing contact data to edit
            Bundle arguments = new Bundle();
            arguments.putLong(MainActivity.ROW_ID, rowID);
            arguments.putCharSequence("name", nameTextView.getText());
            arguments.putCharSequence("email", emailTextView.getText());

            listener.onEditContact(arguments); // pass Bundle to listener
            return true;
         case R.id.action_delete:
            deleteContact();
            return true;
      }
      
      return super.onOptionsItemSelected(item);
   } 
   
   // performs database query outside GUI thread
   private class LoadContactTask extends AsyncTask<Long, Object, Cursor> 
   {
      DatabaseConnector databaseConnector = 
         new DatabaseConnector(getActivity());

      // open database & get Cursor representing specified contact's data
      @Override
      protected Cursor doInBackground(Long... params)
      {
         databaseConnector.open();
         return databaseConnector.getOneContact(params[0]);
      } 

      // use the Cursor returned from the doInBackground method
      @Override
      protected void onPostExecute(Cursor result)
      {
         super.onPostExecute(result);
         result.moveToFirst(); // move to the first item 
   
         // get the column index for each data item
         int nameIndex = result.getColumnIndex("name");
         int emailIndex = result.getColumnIndex("email");
   
         nameTextView.setText(result.getString(nameIndex));
         emailTextView.setText(result.getString(emailIndex));
   
         result.close(); 
         databaseConnector.close(); // close database connection
      } 
   } 

   // delete a contact
   private void deleteContact()
   {         
      // use FragmentManager to display the confirmDelete DialogFragment
      confirmDelete.show(getFragmentManager(), "confirm delete");
   } 

   // DialogFragment to confirm deletion of contact
   private DialogFragment confirmDelete = new DialogFragment()
      {
         // create an AlertDialog and return it
         @Override
         public Dialog onCreateDialog(Bundle bundle)
         {
            // create a new AlertDialog Builder
            AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
      
            builder.setTitle(R.string.confirm_title); 
            builder.setMessage(R.string.confirm_message);
      
            // provide an OK button that simply dismisses the dialog
            builder.setPositiveButton(R.string.button_delete,
               new DialogInterface.OnClickListener()
               {
                  @Override
                  public void onClick(
                     DialogInterface dialog, int button)
                  {
                     final DatabaseConnector databaseConnector = 
                        new DatabaseConnector(getActivity());
      
                     // AsyncTask deletes contact and notifies listener
                     AsyncTask<Long, Object, Object> deleteTask =
                        new AsyncTask<Long, Object, Object>()
                        {
                           @Override
                           protected Object doInBackground(Long... params)
                           {
                              databaseConnector.deleteContact(params[0]); 
                              return null;
                           } 
      
                           @Override
                           protected void onPostExecute(Object result)
                           {                                 
                              listener.onContactDeleted();
                           }
                        }; 
      
                     // execute the AsyncTask to delete contact at rowID
                     deleteTask.execute(new Long[] { rowID });               
                  } 
               } 
            ); 
            
            builder.setNegativeButton(R.string.button_cancel, null);
            return builder.create(); 
         }
      }; 
} 
