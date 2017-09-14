package i.am.eipeks.traka.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import i.am.eipeks.traka.R;
import i.am.eipeks.traka.activities.Home;
import i.am.eipeks.traka.activities.LocationActivity;


public class Authentication extends AppCompatActivity implements
        View.OnClickListener {

    public Button signInButton, forgotPasswordButton, signUpButton;
    private TextInputLayout emailTextInputLayout, passwordTextInputLayout;
    private EditText email, password;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication);

        forgotPasswordButton = (Button) findViewById(R.id.forgot_password);
        signInButton = (Button) findViewById(R.id.login);
        signUpButton = (Button) findViewById(R.id.sign_up);

        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        emailTextInputLayout = (TextInputLayout) findViewById(R.id.email_text_input_layout);
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.password_text_input_layout);

        forgotPasswordButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null){
                    startActivity(new Intent(Authentication.this, Home.class));
                } else {
                    Toast.makeText(Authentication.this, "Please sign in", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onClick(final View view) {
        emailTextInputLayout.setErrorEnabled(false);
        passwordTextInputLayout.setErrorEnabled(false);
        switch(view.getId()){
            case R.id.login:
                if (TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(password.getText())){
                    emailTextInputLayout.setErrorEnabled(true);
                    passwordTextInputLayout.setErrorEnabled(true);
                    if (TextUtils.isEmpty(email.getText())){
                        passwordTextInputLayout.setErrorEnabled(false);
                        emailTextInputLayout.setError("Field is empty");
                    } else {
                        emailTextInputLayout.setErrorEnabled(false);
                        passwordTextInputLayout.setError("Field is empty");
                    }
                } else {
                    if (!validate(email.getText().toString())){
                        emailTextInputLayout.setErrorEnabled(true);
                        emailTextInputLayout.setError("Invalid email type");
                    } else {
                        signInUser(view, email.getText().toString(), password.getText().toString());
                    }
                }
                break;
            case R.id.forgot_password:
                startActivity(new Intent(Authentication.this, ForgotPassword.class));
                break;
            case R.id.sign_up:
                startActivity(new Intent(Authentication.this, SignUp.class));
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

    public void signInUser(final View view, final String username, final String password){
        auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Snackbar.make(view, "Couldn't complete task. Please try again", Snackbar.LENGTH_INDEFINITE)
                                    .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                                    .setAction("Retry", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            signInUser(view, username, password);
                                        }
                                    }).show();
                        } else {
                            startActivity(new Intent(Authentication.this, Home.class));
                        }
                    }
                });
    }

    public boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

}
