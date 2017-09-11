package i.am.eipeks.traka.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import i.am.eipeks.traka.util.fragments.Activities;
import i.am.eipeks.traka.util.fragments.Contacts;


public class PagerAdapter extends FragmentStatePagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Activities();
            case 1:
                return new Contacts();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Activities";
            case 1:
                return "Contacts";
        }
        return null;
    }
}
