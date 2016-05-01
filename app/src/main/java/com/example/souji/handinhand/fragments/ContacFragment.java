package com.example.souji.handinhand.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.souji.handinhand.R;
import com.example.souji.handinhand.adapter.ContactsAdapter;
import com.example.souji.handinhand.pojo.ContactsPojo;

import java.util.ArrayList;

/**
 * Created by souji on 1/5/16.
 */
@SuppressLint("ValidFragment")
public class ContacFragment extends Fragment {

    Context context;
    ProgressDialog pd;
    public ContactsPojo c;

    @SuppressLint("ValidFragment")
    public ContacFragment(Context signContext){

       this.context = signContext;
    }

    public ArrayList<ContactsPojo> contactObject = new ArrayList<ContactsPojo>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

         pd = new ProgressDialog(context);

        View rootView = inflater.inflate(R.layout.contacts_list,container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.contacts_list);
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute()
            {
                pd = ProgressDialog.show(context,
                        "Loading..", "Please Wait", true, false);
            }// End of onPreExecute method

            @Override
            protected Void doInBackground(Void... params)
            {

                getContacts();

                return null;
            }// End of doInBackground method

         @Override
            protected void onPostExecute(Void result)
            {

               // Log.d("on post execute"," "+result.toString());
                pd.dismiss();
                ContactsAdapter cus = new ContactsAdapter(context,contactObject);
                // ArrayAdapter<String>   arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,aa);
                listView.setAdapter(cus);

            }//End of onPostExecute method
        }.execute((Void[]) null);
    return rootView;
    }
    private void getContacts()
    {


        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,null, null, null);
        if (cur.getCount() > 0)
        {
            while (cur.moveToNext())
            {
                c = new ContactsPojo();
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //    String url = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));


            //    Log.d("id ","name"+name+" "+url);

               // aa.add(name);
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    String phoneNumber = null;
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    while (pCur.moveToNext())
                    {
                         phoneNumber = pCur.getString(pCur.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));


                        c.setNumber(phoneNumber);





                        //  num.add(phoneNumber);
                    }
                    c.setName(name);
                    c.setImage(id);
                    contactObject.add(c);
                    Log.d("phn number ", " " + phoneNumber + "name " + name);
                    pCur.close();
                }

            }
        }



    }
}
