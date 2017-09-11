package i.am.eipeks.traka.util.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import i.am.eipeks.traka.R;

public class Contacts extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contacts = inflater.inflate(R.layout.fragment_contacts, container, false);
        return contacts;
    }
}
