package siemens.PhotoGallery;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import siemens.PhotoGallery.Fragments.FragmentCloud;
import siemens.PhotoGallery.Fragments.FragmentPhotos;

public class MainActivity extends AppCompatActivity implements   NavigationView.OnNavigationItemSelectedListener {
    // public class MainActivity extends Fragment implements NavigationView.OnNavigationItemSelectedListener,  AdapterView.OnItemClickListener {
    private Context context;
    private DrawerLayout drawer;
    private TextView username;
    public MainActivity() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_menu);
        loadFragment(new FragmentPhotos());
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Load the fragment in the container
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //Get the navigation view and set a listener on it to react if a navigation item was clicked
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            FragmentPhotos fragmentYourMeetings = new FragmentPhotos();
            getFragmentManager().beginTransaction().replace(R.id.container,fragmentYourMeetings).commit();

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    //@Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == R.id.nav_photo) {
            item.setChecked(true);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            fragment = new FragmentPhotos();
        }else if (id == R.id.nav_cloud) {
            item.setChecked(true);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            fragment = new FragmentCloud();
        }else if (id == R.id.nav_logout) {
            item.setChecked(true);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
//            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//            firebaseAuth.signOut();
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(intent);
        }
        return loadFragment(fragment);

    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment).commit();
        }

        return false;

    }

}
