package i.am.eipeks.traka.util.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import i.am.eipeks.traka.R;
import i.am.eipeks.traka.adapters.ContactListAdapter;
import i.am.eipeks.traka.util.Contact;

public class Contacts extends Fragment implements
        ContactListAdapter.CardViewClickListener{

    private ContentResolver resolver;
    private RecyclerView recyclerView;
    private static final int PERMISSION_CODE = 1010;
    private boolean isRequestGranted = false;
    private ArrayList<Contact> contactsList;
    private AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contacts = inflater.inflate(R.layout.fragment_contacts, container, false);
        resolver = getContext().getContentResolver();

        recyclerView = contacts.findViewById(R.id.contacts_list);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
            isRequestGranted = true;
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_CODE);
        }
        getContactsList();

        return contacts;
    }

    private void getContactsList(){
        if (isRequestGranted){
            final Cursor contactsCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    contactsList = new ArrayList<>();
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(new ContactListAdapter(getContext(), contactsList));
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    if (contactsCursor != null){
                        if (contactsCursor.getCount() > 0 && contactsCursor.moveToFirst()){
                            do {
                                String id = contactsCursor.getString(contactsCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone._ID));
                                String contactName = contactsCursor.getString(contactsCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                String contactPhone = contactsCursor.getString(contactsCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                contactsList.add(new Contact(id, contactName, contactPhone));
                            } while (contactsCursor.moveToNext());
                            contactsCursor.close();
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Collections.sort(contactsList, new Comparator<Contact>() {
                        @Override
                        public int compare(Contact first, Contact second) {
                            return first.getContactName().compareTo(second.getContactName());
                        }
                    });
                    ((ContactListAdapter)recyclerView.getAdapter()).setOnCardViewClickListener(Contacts.this);
                    (recyclerView.getAdapter()).notifyDataSetChanged();
                }
            }.execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    isRequestGranted = true;
                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
        }
    }

    @Override
    public void onCardViewClick(Contact contact, int position) {

    }
}