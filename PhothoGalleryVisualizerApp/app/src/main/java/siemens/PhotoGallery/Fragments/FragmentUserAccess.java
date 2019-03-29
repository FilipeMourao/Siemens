package siemens.PhotoGallery.Fragments;
import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;
import siemens.PhotoGallery.R;
import siemens.PhotoGallery.helpfulStructures.AccountListRowAdapter;
import siemens.PhotoGallery.helpfulStructures.Database;
import siemens.PhotoGallery.helpfulStructures.FirebaseAppConnection;
import siemens.PhotoGallery.helpfulStructures.UserAccount;

import static android.Manifest.permission.READ_CONTACTS;
import static android.content.ContentValues.TAG;


public class FragmentUserAccess extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{
    private FloatingActionButton fab;
    private Database database;
    private ListView listView;
    private List<UserAccount> userAccounts;
    private FirebaseAuth firebaseAuth;
    private SwipeRefreshLayout swipeRefreshLayout;
    AutoCompleteTextView mEmailView ;
    EditText mPasswordView ;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the layout
        View view = inflater.inflate(R.layout.fragment_user_access, container, false);
        database = new Database(getActivity());
        //Get reference to FloatingActionButton, set correct icon and register OnClickListener
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this::onButtonPressed);
        listView = view.findViewById(R.id.list_view);
        listView.setOnItemClickListener(this::onitemClicked);
        firebaseAuth=FirebaseAuth.getInstance();
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);
        updateListView();
        return view;
    }

    public boolean checkPicturesPermission() { // check for the needed permissions to execute the application, if any permission is missing ask for the permissions
        if (
                (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.INTERNET)
                        != PackageManager.PERMISSION_GRANTED)
                )
        {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission
                    .WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET}, 0);
            return false;

        } else {
            return true;
            // Permission has already been granted
        }
    }
    public void onitemClicked(AdapterView<?> parent, View view, int position, long id){ // if the list view item was pressed ask if the user want to delete the element
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Are you sure do you want to delete this account from the device?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAppConnection firebaseAppConnection = new FirebaseAppConnection(getActivity());
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAppConnection  firebaseAppConnection = new FirebaseAppConnection(getActivity());
                        database.deleteAccount(userAccounts.get(position).getUserID());
                        firebaseAppConnection.getAllPhotos();
                        updateListView();
                    }
                })
                .show();
    }
    public void onButtonPressed(View v) { // if the fab button was pressed open the form to add a new account
        if (checkPicturesPermission()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setTitle("Add the new user email and password to add the gallery");
    //        builder.setMessage("AlertDialog");
            View dialogView = inflater.inflate(R.layout.add_new_user_layout, null);
            builder.setView(dialogView);
            //In case it gives you an error for setView(View) try
            //builder.setView(inflater.inflate(R.layout.add_new_user_layout, null));
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setPositiveButton("Confirm", null);
      //      builder.show();
            final AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    mEmailView = (AutoCompleteTextView) dialogView.findViewById(R.id.email);
                    mPasswordView =  (EditText) dialogView.findViewById(R.id.password);
                    if (validateForm()){ // if the information is validated, try to authentitcate the user
                        authenticateUser(mEmailView.getText().toString(),mPasswordView.getText().toString());
                        Toast.makeText(getActivity(), "Verifying the account...",
                                Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                }
            });
        }
    }
    public void updateListView(){// update the list view
        userAccounts = database.getAllUserAccounts();
        final AccountListRowAdapter adapter = new AccountListRowAdapter(getActivity(),userAccounts);
        listView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

    }

    //-----------------------------------------------------------------------------------------------//
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
  }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private boolean validateForm(){

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError("This email is not a valid email!");
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    private boolean isEmailValid(String email) {
//        Check if the tum emails have the same identification or if we want to use just tum emails
        return (email.contains("@")&email.contains(".com"));
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    /**
     * Authenticate the user via Firebase
     * @param usernameString The username
     * @param passwordString The corresponding PW
     */
    private void authenticateUser(final String usernameString, String passwordString) {
        //Sign in the user with the email and password
        firebaseAuth.signInWithEmailAndPassword(usernameString, passwordString)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    /**
                     * Is automatically called once the login task finished
                     * @param task The task that just completed
                     */
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Check if login was successful
                        if (task.isSuccessful()) {
                            //Login the user
                            if (firebaseAuth.getCurrentUser().isEmailVerified()){// add email in the database if the authentication is correct
                                UserAccount userAccount = new UserAccount(firebaseAuth.getUid(),usernameString);
                                database.addUserAccount(userAccount);
                                FirebaseAppConnection firebaseAppConnection = new FirebaseAppConnection(getActivity());
                                firebaseAppConnection.getAllPhotos();
                                updateListView();

                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onRefresh() { updateListView(); }

}
