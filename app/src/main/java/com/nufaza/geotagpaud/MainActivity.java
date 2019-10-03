package com.nufaza.geotagpaud;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.nufaza.geotagpaud.model.Geotag;
import com.nufaza.geotagpaud.model.JenisFoto;
import com.nufaza.geotagpaud.model.JenisFoto_Table;
import com.nufaza.geotagpaud.model.Pengguna;
import com.nufaza.geotagpaud.model.Pengguna_Table;
import com.nufaza.geotagpaud.model.Sekolah;
import com.nufaza.geotagpaud.model.Sekolah_Table;
import com.nufaza.geotagpaud.ui.data.DataFragment;
import com.nufaza.geotagpaud.ui.gallery.GalleryFragment;
import com.nufaza.geotagpaud.ui.geotag.GeotagFragment;
import com.nufaza.geotagpaud.ui.home.HomeFragment;
import com.nufaza.geotagpaud.util.HttpCallback;
import com.nufaza.geotagpaud.util.HttpCaller;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private AppBarConfiguration mAppBarConfiguration;
    private View mainView;

    public static final String SPKEY_SESSION = "SPKEY_SESSION";
    public static final String SPKEY_NAME = "SPKEY_NAME";
    public static final String SPKEY_USERNAME = "SPKEY_USERNAME";
    public static final String SPKEY_PASSWORD = "SPKEY_PASSWORD";
    public static final String SPKEY_PENGGUNA_ID = "SPKEY_PENGGUNA_ID";
    public static final String SPKEY_SEKOLAH_ID = "SPKEY_SEKOLAH_ID";
    public static final String SPKEY_TOKEN = "SPKEY_TOKEN";

    public HomeFragment homeFragment;
    public DataFragment dataFragment;
    public GalleryFragment galleryFragment;
    public GeotagFragment geotagFragment;

    private SharedPreferences sharedPreferences;

    // Folders And Paths
    public static final String THUMBNAIL_FOLDER = "thumbs";
    public static final String TEMP_IMAGE_FOLDER = "temp";
    public static final String EXTERNAL_IMAGE_FOLDER = "images";
    public static final String JSON_FOLDER = "json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Main Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mainView = getWindow().getDecorView().findViewById(android.R.id.content);

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

        homeFragment = new HomeFragment();
        viewPagerAdapter.addFragment(homeFragment, "Home");

        dataFragment = new DataFragment();
        viewPagerAdapter.addFragment(dataFragment, "Data");

        galleryFragment = new GalleryFragment();
        viewPagerAdapter.addFragment(galleryFragment, "Gallery");

        geotagFragment = new GeotagFragment();
        viewPagerAdapter.addFragment(geotagFragment, "Geotag");

        // adapter.addFragment(gedungFragment, getResources().getString(R.string.text_gedung));
        viewPager.setAdapter(viewPagerAdapter);

        // TabLayout and Pager
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        // Shared Preferences
        sharedPreferences = getSharedPreferences(SPKEY_SESSION, Context.MODE_PRIVATE);

        // Toggle login menu
        toggleLogin();
    }

    @Override
    protected void onStart() {

        super.onStart();

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

            case R.id.nav_login:

                if (checkLogin()) {
                    logout();
                    break;
                }

                final MaterialDialog loginDialog = new MaterialDialog.Builder(this)
                    .title("Login")
                    .customView(R.layout.form_login, true)
                    .positiveText("OK")
                    .icon(getResources().getDrawable(R.mipmap.ic_launcher))
                    .autoDismiss(true)
                    .show();

                View loginForm = loginDialog.getCustomView();
                EditText usernameField = loginForm.findViewById(R.id.username);
                EditText passwordField = loginForm.findViewById(R.id.password);
                usernameField.setText(getPreference(SPKEY_USERNAME));
                passwordField.setText(getPreference(SPKEY_PASSWORD));

                View submitButton = loginDialog.getActionButton(DialogAction.POSITIVE);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final View theView = view;
                        View loginForm = loginDialog.getCustomView();

                        // Get The Field Values
                        EditText usernameField = loginForm.findViewById(R.id.username);
                        EditText passwordField = loginForm.findViewById(R.id.password);
                        String usernameStr = usernameField.getText().toString();
                        String passwordStr = passwordField.getText().toString();

                        // Save to session
                        setPreference(SPKEY_USERNAME, usernameStr);
                        setPreference(SPKEY_PASSWORD, passwordStr);

                        // Close loginDialog
                        loginDialog.dismiss();

                        // Create map for JSON
                        HashMap<String, String> params = new HashMap<>();
                        params.put("username", usernameStr);
                        params.put("password", passwordStr);

                        String token = getPreference(SPKEY_TOKEN);

                        // Create call to backend
                        HttpCaller hc = new HttpCaller (
                                MainActivity.this,
                                HttpCaller.POST,
                                "/api/login_check",
                                params,
                                HttpCaller.RETURN_TYPE_JSON,
                                new HttpCallback() {
                            @Override
                            public void onSuccess(JSONObject responseJSO) {

                                try {

                                    String token = responseJSO.getString("token");
                                    String id = responseJSO.getString("id");
                                    Snackbar.make(mainView, "Login berhasil. ID ditemukan = " + id, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    setPreference(SPKEY_TOKEN, token);

                                    getInitialData(id, token);

                                } catch (JSONException e) {

                                    Snackbar.make(mainView, "Login gagal", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    // e.printStackTrace();
                                }

                            }
                        }, token);

                    }
                });

                break;

            case R.id.nav_help:

                String url = "http://geotagpaud.nufaza.com/faq";
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
                    // String databaseVersion = String.valueOf(RehabDatabase.VERSION);

                    //long jumlahJenisFoto = SQLite.select().from(JenisFoto.class).count();
                    long jumlahSekolah = SQLite.selectCountOf().from(Sekolah.class).count();
                    long jumlahPengguna = SQLite.selectCountOf().from(Pengguna.class).count();

                    MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title("Tentang Aplikasi")
                        .content(appName + "\r\nJumlah Pengguna: " + Long.toString(jumlahPengguna) + "\r\nJumlah Sekolah: " + Long.toString(jumlahSekolah) + "\r\nVersi App: " + version + "\r\nVersi DB: " + AppDatabase.VERSION )
                        //.content("Aplikasi Verifikasi Sarana Prasarana\r\nEdisi Custom (Takola SD)" + "\r\nVersi App: 1.0.0\r\nVersi DB: " + databaseVersion)
                        .positiveText("OK")
                        .icon(getResources().getDrawable(R.mipmap.ic_launcher))
                        .autoDismiss(true)
                        .show();

                }
                catch (Exception e)
                {
                    // Do nothing lah.
                    System.out.println(e.getMessage());
                }
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getInitialData(String id, String token) {

        // Create call to backend
        HttpCaller hc = new HttpCaller (
            MainActivity.this,
            HttpCaller.GET,
            "/api/penggunas/" + id,
            null,
            HttpCaller.RETURN_TYPE_JSON,
            new HttpCallback() {
                @Override
                public void onSuccess(JSONObject responseJSO) {

                    try {

                        // String token = responseJSO.getString("token");
                        // String id = responseJSO.getString("id");
                        // Snackbar.make(mainView, "Login berhasil. ID ditemukan = " + id, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        // setPreference(SPKEY_TOKEN, token);

                        String name = responseJSO.getString("nama");
                        setPreference(SPKEY_NAME, name);

                        String penggunaId = responseJSO.getString("id");
                        setPreference(SPKEY_PENGGUNA_ID, penggunaId);

                        JSONObject sekolahObj = responseJSO.getJSONObject("sekolah");
                        String sekolahId = sekolahObj.getString("id");
                        setPreference(SPKEY_SEKOLAH_ID, sekolahId);


                        // Save intial data to database
                        saveInitialData(responseJSO);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Refresh Menu and Identity
                                toggleLogin();
                                homeFragment.updateView();
                            }
                        });


                    } catch (JSONException e) {

                        // Snackbar.make(mainView, "Login gagal", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        // e.printStackTrace();
                    }

                }
            }, token);

    }

    private void saveInitialData(JSONObject responseJSO) {

        Pengguna pengguna;
        Sekolah sekolah;
        UUID penggunaId;
        UUID sekolahId;

        try {

            penggunaId = UUID.fromString(responseJSO.getString("id"));
            JSONObject sekolahObj = responseJSO.getJSONObject("sekolah");
            sekolahId = UUID.fromString(sekolahObj.getString("id"));

            // Cek pengguna di lokal, if not exists then save
            pengguna = SQLite.select().from(Pengguna.class).where(Pengguna_Table.pengguna_id.eq(penggunaId)).querySingle();
            if (pengguna == null) {
                pengguna = new Pengguna();
                pengguna.fromJsonObject(responseJSO);
                pengguna.setPenggunaId(penggunaId);
                pengguna.save();
            }

            // Cek sekolah di lokal, if not exists then save
            sekolah = SQLite.select().from(Sekolah.class).where(Sekolah_Table.sekolah_id.eq(sekolahId)).querySingle();
            if (sekolah == null) {

                sekolah = new Sekolah();
                sekolah.fromJsonObject(sekolahObj);
                sekolah.setSekolahId(sekolahId);
                sekolah.save();

                if (sekolah.getLintang() != ".0000000") {
                    Geotag geotag = new Geotag();
                    geotag.setGeotagId(UUID.randomUUID());
                    geotag.setSekolahId(sekolahId);
                    geotag.setPenggunaId(penggunaId);
                    geotag.setLintang(String.valueOf(sekolah.getLintang()));
                    geotag.setBujur(String.valueOf(sekolah.getBujur()));
                    geotag.setStatusGeotagId(1);
                    geotag.setStatusTag(1);
                    geotag.save();
                }
            }

            Snackbar.make(mainView, "Data pengguna dan sekolah tersimpan di database.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } catch (Exception e) {

            Snackbar.make(mainView, "Error saat save data.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            e.printStackTrace();
        }
    }

    public void setPreference (String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getPreference (String key){
        return sharedPreferences.getString(key, "");
    }

    public void logout() {
        setPreference(SPKEY_TOKEN, "");
        setPreference(SPKEY_PENGGUNA_ID, "");
        setPreference(SPKEY_SEKOLAH_ID, "");
        Snackbar.make(mainView, "Anda telah logout.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        toggleLogin();
        homeFragment.updateView();
    }

    public boolean checkLogin() {
        String token = getPreference(SPKEY_TOKEN);
        return !token.equals("");
    }

    /**
     * Toggle login by cheking token
     */
    public void toggleLogin() {

        // Stuff that updates the UI
        updateNavigation(checkLogin());
    }

    /**
     * Change title of login menu item in the Navigation Drawer
     * @param loggedIn
     */
    public void updateNavigation(boolean loggedIn) {

        String title = (loggedIn) ? "Logout" : "Login";
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem loginMenuItem = menu.findItem(R.id.nav_login);
        loginMenuItem.setTitle(title);

        String name = loggedIn ? getPreference(SPKEY_NAME) : "Not Authenticated";
        String email = loggedIn ? getPreference(SPKEY_USERNAME) : "Please login first";

        TextView nameTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_name);
        TextView emailTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_email);

        if (nameTextView != null) {
            nameTextView.setText(name);
        }

        if (emailTextView != null) {
            emailTextView.setText(email);
        }

        // ImageView profilePicImgView = (ImageView) findViewById(R.id.profilePic);
        // if (profilePicImgView != null) {
        //
        //     File profileImagePath = new File(imagesPath, "profile_" + socmedId + ".jpg");
        //
        //     if (profileImagePath.isFile()) {
        //         Bitmap profileBitmap = BitmapFactory.decodeFile(profileImagePath.toString());
        //
        //         // Round the profileBitmap
        //         profileBitmap = getRoundedBitmap(profileBitmap);
        //         profilePicImgView.setImageBitmap(profileBitmap);
        //     } else {
        //         profilePicImgView.setImageResource(R.mipmap.ic_launcher);
        //     }
        //
        // } else {
        //     // profilePicImgView.setImageResource(R.mipmap.ic_launcher);
        // }

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