package uk.ac.ncl.team2.qrquest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A login screen that offers login via email/password.
 * Can go to RegistrationActivity if user isn't registered.
 * Can go to MainMenuActivity by typing in details.
 */
public class LoginActivity extends Activity {

    //Request code for registering a new user.
    public final int REGISTRATION_REQUEST = 1;

    // Keep track of the login task to ensure we can cancel it if requested.
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText emailEditText;
    private EditText passwordEditText;
    private View processView;
    private View formView;
    private Button signInButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Setup dummy database. (Temporary Measure)
        DummyDatabase.init();

        // Define UI References
        emailEditText = (EditText) findViewById(R.id.login_email);
        passwordEditText = (EditText) findViewById(R.id.login_password);
        formView = findViewById(R.id.login_form);
        processView = findViewById(R.id.login_progress);
        signInButton = (Button) findViewById(R.id.login_sign_in_button);
        registerButton = (Button) findViewById(R.id.login_register_button);

        //Add event listeners for passwordEditText and buttons.
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivityForResult(intent, REGISTRATION_REQUEST);
            }
        });

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        emailEditText.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the login attempt.
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.error_field_required));
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

        if (cancel) {
            focusView.requestFocus(); //Error. Focus the last invalid form field.
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Checks if the given email string is in a correct format.
     * @param email:String
     * @return validity:Boolean
     */
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Checks if the given password string is in a correct format.
     * @param password:String
     * @return validity:Boolean
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     * @param show:Boolean True if you want ot display spinner and hide UI
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            formView.setVisibility(show ? View.GONE : View.VISIBLE);
            formView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    formView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            processView.setVisibility(show ? View.VISIBLE : View.GONE);
            processView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    processView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            processView.setVisibility(show ? View.VISIBLE : View.GONE);
            formView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String emailEditText;
        private final String passwordEditText;
        private User selectedUser;

        UserLoginTask(String email, String password) {
            emailEditText = email;
            passwordEditText = password;
            selectedUser = null;
        }

        /**
         * Method to simulate network access. This will eventually attempt
         * authentication against a network service.
         * @param params
         * @return success:Boolean
         */
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (User user : DummyDatabase.users) {
                // If the user name and password matches, store user and return true.
                if (user.getEmail().equals(emailEditText)
                        && user.getPassword().equals(passwordEditText)) {
                    selectedUser = user;
                    return true;
                }
            }
            return false;
        }

        /**
         * If, successfully connected to network, logs user in.
         * Else, displays UI and error.
         * @param success:Boolean states if system has connected to network.
         */
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;


            if (success) {
                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                intent.putExtra("USER", selectedUser);
                startActivity(intent);
            } else {
                LoginActivity.this.passwordEditText.setError(getString(R.string.error_incorrect_password));
                LoginActivity.this.passwordEditText.requestFocus();
            }
            showProgress(false);
        }

        /**
         * If network access has been canceled, will stop and display UI.
         */
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Method executed when recieving result from RegistrationActivity.
     * @param requestCode:int Code to specify the request you're making (REGISTRATION_REQUEST).
     * @param resultCode:int Code to specify result: RESULT_OK or RESULT_CANCELLED.
     * @param data:Intent Data containing the new User.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Check which request we're responding to
        if (requestCode == REGISTRATION_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                User newUser = (User) data.getSerializableExtra("NEW_REGISTERED_USER");
                DummyDatabase.users.add(newUser);
            }
        }
    }

}

