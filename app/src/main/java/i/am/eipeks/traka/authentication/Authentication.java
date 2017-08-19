package i.am.eipeks.traka.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import i.am.eipeks.traka.R;
import i.am.eipeks.traka.activities.LocationActivity;


public class Authentication extends AppCompatActivity implements
        View.OnClickListener {

    private Button signInButton;
    private Button forgotPasswordButton;
    private TextInputLayout emailTextInputLayout, passwordTextInputLayout;
    private EditText email, password;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication);

        forgotPasswordButton = (Button) findViewById(R.id.forgot_password);
        signInButton = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        emailTextInputLayout = (TextInputLayout) findViewById(R.id.email_text_input_layout);
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.password_text_input_layout);

        forgotPasswordButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null){
                    startActivity(new Intent(Authentication.this, LocationActivity.class));
                } else {
                    Toast.makeText(Authentication.this, "Please sign in", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.login:

                break;
            case R.id.forgot_password:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (auth != null){
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
