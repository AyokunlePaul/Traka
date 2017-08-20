package i.am.eipeks.traka.authentication;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import i.am.eipeks.traka.R;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout emailTextInputLayout;
    private EditText resetEmail;
    private Button resetPassword;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        auth = FirebaseAuth.getInstance();

        emailTextInputLayout = (TextInputLayout) findViewById(R.id.forgot_password_text_input_layout);
        resetEmail = (EditText) findViewById(R.id.forgot_password_email);
        resetPassword = (Button) findViewById(R.id.reset_password);

        resetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reset_password:
                auth.sendPasswordResetEmail(resetEmail.getText().toString());
                break;
        }
    }
}
