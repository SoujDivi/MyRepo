package com.example.souji.handinhand.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.souji.handinhand.R;
import com.example.souji.handinhand.pojo.ContactsPojo;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by souji on 30/4/16.
 */
public class ContactsAdapter extends BaseAdapter {


   // List<ContactsPojo> contactsList;
    ArrayList<ContactsPojo> contactsList;
    Context mContext;
    LayoutInflater inflater;

    public ContactsAdapter(Context context,List<ContactsPojo> list){
        mContext = context;
        this.contactsList = new ArrayList<ContactsPojo>();
        this.contactsList.addAll(list);
        inflater = LayoutInflater.from(mContext);

    }

    static class ViewHolder {
        protected TextView name;
        protected TextView number;
        protected CheckBox check;
        protected ImageView image;
    }


    @Override
    public int getCount() {
        Log.d("contactslist"," size"+contactsList.size());
        return contactsList.size();

    }

    @Override
    public Object getItem(int position) {
        return contactsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.contacts_row, null);

            holder.name = (TextView) convertView.findViewById(R.id.contactname);
            holder.number = (TextView) convertView.findViewById(R.id.contactno);

            holder.check = (CheckBox) convertView.findViewById(R.id.contactcheck);

            holder.image = (ImageView) convertView.findViewById(R.id.contactimage);

            convertView.setTag(holder);
          /*  convertView.setTag(R.id.contactname, holder.name);
            convertView.setTag(R.id.contactno, holder.number);
            convertView.setTag(R.id.contactcheck, holder.check);
*/
            holder.check
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton vw,
                                                     boolean isChecked) {

                            int getPosition = (Integer) vw.getTag();
                            contactsList.get(getPosition).setSelected(
                                    vw.isChecked());

                        }
                    });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       holder.check.setTag(position);


        holder.name.setText(contactsList.get(position).getName());
        Log.d("pos", " " + position+"name in adapert "+holder.name.getText());
        holder.number.setText(contactsList.get(position).getNumber());

       // holder.image.setImageURI(Uri.parse(contactsList.get(position).getImage()));
      //  holder.image.setImageBitmap();

        if(getByteContactPhoto(contactsList.get(position).getImage())==null){

            holder.image.setImageResource(R.drawable.ic_launcher);
        }else{
            holder.image.setImageBitmap(getByteContactPhoto(contactsList.get(position).getImage()));
        }



        holder.check.setChecked(contactsList.get(position).isSelected());

        return convertView;
    }


    public Bitmap getByteContactPhoto(String contactId) {
        Log.d("getbyte contact"," "+contactId);
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = mContext.getContentResolver().query(photoUri,
                new String[] {ContactsContract.Contacts.Photo.DATA15}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
                }
            }
        } finally {
            cursor.close();
        }

        return null;
    }


}
