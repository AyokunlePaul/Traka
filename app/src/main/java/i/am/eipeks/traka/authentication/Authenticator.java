package i.am.eipeks.traka.authentication;


import android.content.Context;
import android.content.SharedPreferences;

public class Authenticator {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Authenticator(Context context){
        sharedPreferences = context.getSharedPreferences("Traka", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}