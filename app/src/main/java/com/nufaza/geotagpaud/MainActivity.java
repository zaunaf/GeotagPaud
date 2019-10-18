package com.nufaza.geotagpaud;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
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
import com.nufaza.geotagpaud.managers.DataScrappingManager;
import com.nufaza.geotagpaud.managers.DataTransportManager;
import com.nufaza.geotagpaud.model.Foto;
import com.nufaza.geotagpaud.model.Foto_Table;
import com.nufaza.geotagpaud.model.Geotag;
import com.nufaza.geotagpaud.model.JenisFoto;
import com.nufaza.geotagpaud.model.JenisFoto_Table;
import com.nufaza.geotagpaud.model.Pengguna;
import com.nufaza.geotagpaud.model.Pengguna_Table;
import com.nufaza.geotagpaud.model.Sekolah;
import com.nufaza.geotagpaud.model.Sekolah_Table;
import com.nufaza.geotagpaud.ui.data.DataFragment;
import com.nufaza.geotagpaud.ui.data.ListAdapter;
import com.nufaza.geotagpaud.ui.data.WebScrapResult;
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
import okhttp3.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public static final String STOREDATA ="STOREDATA";

    public HomeFragment homeFragment;
    public DataFragment dataFragment;
    public GalleryFragment galleryFragment;
    public GeotagFragment geotagFragment;

    private SharedPreferences sharedPreferences;

    //Buat SharedPreferences untuk nyimpen Data TK
    private SharedPreferences dataTKPref;

    private String thumbnailPath;
    private String sekolahId;

    // Folders And Paths
    public static final String THUMBNAIL_FOLDER = "thumbs";
    public static final String TEMP_IMAGE_FOLDER = "temp";
    public static final String EXTERNAL_IMAGE_FOLDER = "images";
    public static final String JSON_FOLDER = "json";

    public DataScrappingManager dataScrappingManager;

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
        dataTKPref = getSharedPreferences("TK_PREF",Context.MODE_PRIVATE);

        // Toggle login menu
        toggleLogin();

        if (!checkLogin()){
            loginDialog();
        }

        // Callighrapy
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/quicksand_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        // Scrap manager
        dataScrappingManager = new DataScrappingManager(MainActivity.this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
        final Typeface faceMed = ResourcesCompat.getFont(this, R.font.quicksand_semibold);
        final Typeface face = ResourcesCompat.getFont(this, R.font.quicksand_regular);

        int id = item.getItemId();

        switch (id) {

            case R.id.nav_login:

                if (checkLogin()) {
                    logout();
                    break;
                }

                loginDialog();
                break;

            case R.id.nav_upload:
                DataTransportManager.sendData(MainActivity.this);
                DataTransportManager.sendDataFoto(MainActivity.this);
                break;

            case R.id.nav_help:

                String url = "https://geotag.paud.nufaza.com/help";
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
                            .typeface(faceMed,face)
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
            null,
            HttpCaller.RETURN_TYPE_JSON,
            new HttpCallback() {
                @Override
                public void onSuccess(JSONObject responseJSO, Response response) {

                    try {

                        // String token = responseJSO.getString("token");
                        // String id = responseJSO.getString("id");
                        // Snackbar.make(mainView, "Login berhasil. ID ditemukan = " + id, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        // setPreference(SPKEY_TOKEN, token);

                        String name = responseJSO.getString("nama");
                        setPreference(SPKEY_NAME, name);

                        String penggunaId = responseJSO.getString("pengguna_id");
                        setPreference(SPKEY_PENGGUNA_ID, penggunaId);

                        JSONObject sekolahObj = responseJSO.getJSONObject("sekolah");
                        String sekolahId = sekolahObj.getString("sekolah_id");
                        setPreference(SPKEY_SEKOLAH_ID, sekolahId);


                        // Save intial data to database
                        saveInitialData(responseJSO);

                        // Load scraping data
                        dataScrappingManager.flushScrapResults();
                        dataScrappingManager.executeScrap();

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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Refresh Menu and Identity
                                toggleLogin();
                                homeFragment.updateView();
                            }
                        });
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

            penggunaId = UUID.fromString(responseJSO.getString("pengguna_id"));
            JSONObject sekolahObj = responseJSO.getJSONObject("sekolah");
            sekolahId = UUID.fromString(sekolahObj.getString("sekolah_id"));

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
                    geotag.setStatusData(1);
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

    //Buat Bikin Preference tipe data String disimpen ke Preference TK
    public void setTKPreference (String key, String value){
        SharedPreferences.Editor editor = dataTKPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    //Buat Bikin Preference tipe data Integer disimpen ke Preference TK
    public void setIntegerTKPreference (String key, int value){
        SharedPreferences.Editor editor =  dataTKPref.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public String getPreference (String key){
        return sharedPreferences.getString(key, "");
    }

    //Buat Ambil Preference tipe data Integer dari Preference TK
    public int getIntegerTKPreference (String key){
        return dataTKPref.getInt(key, 0);
    }

    //Buat Ambil Preference tipe data String dari Preference TK
    public String getTKPreference (String key){
        return dataTKPref.getString(key, "");
    }


    public void logout() {
        setPreference(SPKEY_TOKEN, "");
        setPreference(SPKEY_PENGGUNA_ID, "");
        setPreference(SPKEY_SEKOLAH_ID, "");

        //Ngehapus lagi Preference Data TK pas Logout
        // BUG : Data baru hilang di DataFragment ketika keluar Aplikasi
        SharedPreferences.Editor editor = dataTKPref.edit();
        editor.clear();
        editor.apply();

        Snackbar.make(mainView, "Anda telah logout.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        toggleLogin();
        homeFragment.updateView();
        loginDialog();
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

        // set iv_profile
        // External Memory
        thumbnailPath = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/" + MainActivity.THUMBNAIL_FOLDER + "/";

        sekolahId = this.getPreference(MainActivity.SPKEY_SEKOLAH_ID);

        Foto foto = SQLite.select()
                .from(Foto.class)
                .where(Foto_Table.jenis_foto_id.eq(8))
                .and(Foto_Table.status_data.greaterThanOrEq(1))
                .querySingle();

        ImageView imgProfile = navigationView.getHeaderView(0).findViewById(R.id.imageView);

        if (foto != null){
            if (imgProfile != null) {
                String fotoUri = foto.getFotoId().toString()+".jpg";
                File imageFile = new File(thumbnailPath+ sekolahId+ "/"   + fotoUri);
                Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", imageFile);

                if (imageFile.isFile()) {
                    Bitmap profileBitmap = null;
                    try {
                        profileBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Round the profileBitmap
                    profileBitmap = getRoundedBitmap(profileBitmap);
                    imgProfile.setImageBitmap(profileBitmap);
                } else {
                    imgProfile.setImageResource(R.mipmap.ic_launcher);
                }
            }else {
                imgProfile.setImageResource(R.mipmap.ic_launcher);
            }
        }else {
            imgProfile.setImageResource(R.mipmap.ic_launcher);
        }

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

    public void loginDialog(){
        Typeface faceMed = ResourcesCompat.getFont(this, R.font.quicksand_semibold);
        Typeface face = ResourcesCompat.getFont(this, R.font.quicksand_regular);
        final MaterialDialog loginDialog = new MaterialDialog.Builder(this)
                .title("Login")
                .customView(R.layout.form_login, true)
                .positiveText("OK")
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .icon(getResources().getDrawable(R.mipmap.ic_launcher))
                .typeface(faceMed,face)
                .autoDismiss(true)
                .show();

        View loginForm = loginDialog.getCustomView();
        EditText usernameField = loginForm.findViewById(R.id.username);
        EditText passwordField = loginForm.findViewById(R.id.password);
        passwordField.setTypeface(face);
        passwordField.setTransformationMethod(new PasswordTransformationMethod());

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

                //Buat set Limit Loop di Data Fragment
                setIntegerTKPreference("DATALIMIT",0);

                //Buat cek apakah User udah ambil data dari Web
                setTKPreference(STOREDATA,"belum");

                // Close loginDialog
                loginDialog.dismiss();

                // Create map for JSON
                HashMap<String, String> params = new HashMap<>();
                params.put("username", usernameStr);
                params.put("password", passwordStr);

                JSONObject jsonObject = new JSONObject(params);

                String token = getPreference(SPKEY_TOKEN);

                // Create call to backend
                HttpCaller hc = new HttpCaller(
                        MainActivity.this,
                        HttpCaller.POST,
                        "/api/login_check",
                        null,
                        jsonObject,
                        HttpCaller.RETURN_TYPE_JSON,
                        new HttpCallback() {
                            @Override
                            public void onSuccess(JSONObject responseJSO, Response response) {

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
    public static Bitmap getRoundedBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    
}