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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import i.am.eipeks.traka.R;
import i.am.eipeks.traka.activities.Home;
import i.am.eipeks.traka.activities.LocationActivity;
import i.am.eipeks.traka.util.User;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout firstNameTextInputLayout, lastNameTextInputLayout,
            emailTextInputLayout, passwordTextInputLayout, confirmPasswordTextInputLayout, phoneNumberTextInputLayout;
    private EditText firstName, lastName, email, password, confirmPassword, phoneNumber;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        Button signUpButton = findViewById(R.id.sign_up_button);

        firstNameTextInputLayout = findViewById(R.id.first_name_text_input_layout);
        lastNameTextInputLayout = findViewById(R.id.last_name_text_input_layout);
        emailTextInputLayout = findViewById(R.id.email_text_input_layout);
        passwordTextInputLayout = findViewById(R.id.password_text_input_layout);
        confirmPasswordTextInputLayout = findViewById(R.id.confirm_password_text_input_layout);
        phoneNumberTextInputLayout = findViewById(R.id.phone_number_text_input_layout);

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email_sign_up);
        password = findViewById(R.id.password_sign_up);
        confirmPassword = findViewById(R.id.confirm_password_sign_up);
        phoneNumber = findViewById(R.id.phone_number);

        signUpButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_up_button:
                if (TextUtils.isEmpty(firstName.getText()) || TextUtils.isEmpty(lastName.getText()) || TextUtils.isEmpty(email.getText()) ||
                        TextUtils.isEmpty(password.getText()) || TextUtils.isEmpty(confirmPassword.getText())){
                    Snackbar.make(view, "One or more fields are empty", Snackbar.LENGTH_LONG).show();
                } else {
                    createUser(view, email.getText().toString(), password.getText().toString());
                }
                break;
        }
    }

    private void createUser(final View view, final String email, final String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Snackbar.make(view, "Couldn't complete task", Snackbar.LENGTH_INDEFINITE)
                                    .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                                    .setAction("Retry", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            createUser(view, email, password);
                                        }
                                    }).show();
//                            Toast.makeText(SignUp.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            String fullName = String.format("%s %s", firstName.getText().toString(), lastName.getText().toString());
                            User currentUser = new User(fullName, email, phoneNumber.getText().toString());

                            reference.child("user").child(task.getResult().getUser().getUid()).setValue(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SignUp.this, "Configured", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignUp.this, "An error occurred", Toast.LENGTH_SHORT).show();
                                    }
                                    login(view, email, password);
                                }
                            });

                        }
                    }
                });
    }

    private void login(final View view, final String email, final String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try{
                            if (task.isSuccessful()){
                                startActivity(new Intent(SignUp.this, Home.class));
                            } else {
                                Snackbar.make(view, "Login failed", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("Retry", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                login(view, email, password);
                                            }
                                        }).setActionTextColor(getResources().getColor(R.color.colorPrimary))
                                        .show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

}