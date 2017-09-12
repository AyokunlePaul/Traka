package i.am.eipeks.traka.util.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import i.am.eipeks.traka.R;
import i.am.eipeks.traka.adapters.ContactListAdapter;
import i.am.eipeks.traka.util.Contact;

public class Contacts extends Fragment {

    private ContentResolver resolver;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contacts = inflater.inflate(R.layout.fragment_contacts, container, false);
        resolver = getContext().getContentResolver();

        recyclerView = (RecyclerView) contacts.findViewById(R.id.contacts_list);

        getContactsList();

        return contacts;
    }

    private void getContactsList(){
        final ArrayList<Contact> contacts = new ArrayList<>();
        final Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        AsyncTask<Void, Void, ArrayList<Contact>> task = new AsyncTask<Void, Void, ArrayList<Contact>>() {

            @Override
            protected void onPreExecute() {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            }

            @Override
            protected ArrayList<Contact> doInBackground(Void... params) {
                if (cursor != null){
                    if (cursor.getCount() > 0){
                        while (cursor.moveToFirst()){
                            try{
                                String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                                String phoneNumber = "";

                                if (cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0){
                                    Cursor phoneNumberCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                            new String[]{id}, null);
                                    if (phoneNumberCursor != null){
                                        while (phoneNumberCursor.moveToFirst()){
                                            phoneNumber = phoneNumberCursor.getString(phoneNumberCursor
                                                    .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                        }
                                        phoneNumberCursor.close();
                                    }
                                }
                                contacts.add(new Contact(id, name, phoneNumber));
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        cursor.close();
                    }
                }
                return contacts;
            }

            @Override
            protected void onPostExecute(ArrayList<Contact> contacts) {
                Toast.makeText(getContext(), contacts.size(), Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(new ContactListAdapter(getContext(), contacts));
            }
        };
        task.execute();
    }

}
