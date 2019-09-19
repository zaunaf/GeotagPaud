package com.nufaza.geotagpaud;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.nufaza.geotagpaud.ui.data.DataFragment;
import com.nufaza.geotagpaud.ui.gallery.GalleryFragment;
import com.nufaza.geotagpaud.ui.geotag.GeotagFragment;
import com.nufaza.geotagpaud.ui.home.HomeFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Main Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer & Navigation
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ViewPager
        ViewPager viewPager = findViewById(R.id.viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        Fragment homeFragment = new HomeFragment();
        viewPagerAdapter.addFragment(homeFragment, "Home");

        Fragment dataFragment = new DataFragment();
        viewPagerAdapter.addFragment(dataFragment, "Data");

        Fragment galleryFragment = new GalleryFragment();
        viewPagerAdapter.addFragment(galleryFragment, "Gallery");

        Fragment geotagFragment = new GeotagFragment();
        viewPagerAdapter.addFragment(geotagFragment, "Geotag");

        // adapter.addFragment(gedungFragment, getResources().getString(R.string.text_gedung));
        viewPager.setAdapter(viewPagerAdapter);

        // TabLayout and Pager
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.nav_help:
                String url = "http://takola.ditpsd.net/faq";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

            case R.id.nav_about:

                try {

                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    // String packageName = pInfo.packageName;

                    int stringId = getApplicationContext().getApplicationInfo().labelRes;
                    String appName = getApplicationContext().getString(stringId);

                    String version = pInfo.versionName;
//                    String databaseVersion = String.valueOf(RehabDatabase.VERSION);


                    MaterialDialog dialog = new MaterialDialog.Builder(this)
                            .title("Tentang Aplikasi")
                            .content(appName + "\r\nVersi App: " + version + "\r\nVersi DB: 0.0.1" )
                            //.content("Aplikasi Verifikasi Sarana Prasarana\r\nEdisi Custom (Takola SD)" + "\r\nVersi App: 1.0.0\r\nVersi DB: " + databaseVersion)
                            .positiveText("OK")
                            .icon(getResources().getDrawable(R.mipmap.ic_launcher))
                            .autoDismiss(true)
                            .show();

                    //dialog.on

                }
                catch (Exception e)
                {
                    // Do nothing lah.
                }
                break;
        }
        return false;
    }
}


/**
 * Still View Pager shit
 */
class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
        //return null;
    }


}