package uk.ac.ncl.team2.qrquest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;

/**
 * A Registration screen that offers creates a new user.
 * Goes to LoginActivity once finished.
 */
public class RegistrationActivity extends Activity {

    //UI references
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private Button registrationButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Define UI References.
        nameEditText = (EditText) findViewById(R.id.registration_name);
        emailEditText = (EditText) findViewById(R.id.registration_email);
        passwordEditText = (EditText) findViewById(R.id.registration_password);
        passwordConfirmEditText = (EditText) findViewById(R.id.registration_password_confirm);
        registrationButton = (Button) findViewById(R.id.registration_button);
        backButton = (Button) findViewById(R.id.registration_back_button);

        //Add event listeners for buttons.
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegistration();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

    }

    /**
     * Checks if the given name string is in a correct format.
     * @param name:String
     * @return Validity:Boolean
     */
    private boolean isNameValid(String name){
        return true;
    }

    /**
     * Checks if the given email string is in the correct format
     * @param email:String
     * @return Validity:Boolean
     */
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Checks if the given password string is in the correct format
     * @param password:String
     * @return Validity:Boolean
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Check if the strings entered are valid do not yet exist
     * and if, register the user and go back to login screen.
     */
    private void attemptRegistration() {

        // Reset errors.
        nameEditText.setError(null);
        emailEditText.setError(null);
        passwordEditText.setError(null);
        passwordConfirmEditText.setError(null);

        // Store values at the time of the registration attempt.
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordConfirmEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.error_field_required));
            focusView = passwordEditText;
            cancel = true;
        }
        else if(TextUtils.isEmpty(passwordConfirm)) {
            passwordConfirmEditText.setError(getString(R.string.error_field_required));
            focusView = passwordConfirmEditText;
            cancel = true;
        }
        else if(!password.equals(passwordConfirm)) {
            passwordEditText.setError(getString(R.string.error_unmatching_passwords));
            passwordConfirmEditText.setError(getString(R.string.error_unmatching_passwords));
            focusView = passwordEditText;
            cancel = true;
        }
        else if(!isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.error_field_required));
            focusView = emailEditText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailEditText.setError(getString(R.string.error_invalid_email));
            focusView = emailEditText;
            cancel = true;
        }

        // Check for existing email address
        for(User user: DummyDatabase.users)
        {
            if( user.getEmail().equals(email) ){
                emailEditText.setError(getString(R.string.error_existing_email));
                focusView = emailEditText;
                cancel = true;
            }
        }

        // Check for a valid name
        if(TextUtils.isEmpty(name)) {
            nameEditText.setError(getString(R.string.error_field_required));
            focusView = nameEditText;
            cancel = true;
        }
        else if(!isNameValid(name)) {
            nameEditText.setError(getString(R.string.error_invalid_name));
            focusView = nameEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus(); //Error. Focus the first invalid form field.
        } else {
            User newUser = new User(name, email, password);
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            intent.putExtra("NEW_REGISTERED_USER", newUser);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }

    }
}
