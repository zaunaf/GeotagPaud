package com.nufaza.geotagpaud.ui.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.nufaza.geotagpaud.MainActivity;
import com.nufaza.geotagpaud.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.nufaza.geotagpaud.MainActivity.SPKEY_SEKOLAH_ID;
import static com.nufaza.geotagpaud.MainActivity.SPKEY_SESSION;
import static com.nufaza.geotagpaud.MainActivity.STOREDATA;
import static com.nufaza.geotagpaud.R.id.agregatSiswaDapodik;
import static com.nufaza.geotagpaud.R.id.root;

public class DataFragment extends Fragment {

    private DataViewModel dataViewModel;
    TextView tv_agregat_dapodik_siswa;

    MainActivity mainActivity;

    //BUG : Untuk melihat Data perlu ke Geotag dulu

    // Deklarasi & Inisialisasi
    ListView listView;
    List<WebScrapResult> webScrapResults = new ArrayList<>();
    ArrayList<String> webSrapArray = new ArrayList<>();
    ListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dataViewModel =
                ViewModelProviders.of(this).get(DataViewModel.class);
        View root = inflater.inflate(R.layout.fragment_data_listview, container, false);

        new Scrap().execute();
        tv_agregat_dapodik_siswa = root.findViewById(R.id.agregatSiswaDapodik);

        listView = root.findViewById(R.id.listView);
        listView.setOnItemClickListener(null);

        adapter = new ListAdapter(getActivity().getApplicationContext() , R.layout.custom_list, webScrapResults );
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return root;
    }


    private class Scrap extends AsyncTask {

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Object[] objects) {
            String url = "https://manajemen.paud-dikmas.kemdikbud.go.id/Profil/Index/";
            mainActivity = (MainActivity) getActivity();
            String urlKey = mainActivity.getPreference(MainActivity.SPKEY_SEKOLAH_ID);
            url += urlKey;

            String statuslihat = mainActivity.getTKPreference(STOREDATA);

            if (statuslihat.equalsIgnoreCase("belum")){

                try {
                    // Konek ke  Web Dapodik
                    Document mBlogDocument = Jsoup.connect(url).get();

                    //Ini buat Bantu nanti Loop dibawah
                    Elements bantuLoop = mBlogDocument.select("table[class=data table table-striped table-hover no-margin]").select("tbody").eq(0).select("tr");
                    Elements isiData = mBlogDocument.select("table[class=data table table-striped table-hover no-margin]").select("tbody").eq(0).select("tr");
                    Elements sub = mBlogDocument.select("div[class=x_title]").eq(1).select("h2");

                    //Jumlah semua Tabel Data di Web Dapodik
                    int semuaTabel = mBlogDocument.select("table[class=data table table-striped table-hover no-margin]").select("tbody").size();

                    //Nge Loop semua Tabel lalu dimasukan ke dalam Array
                    for (int k = 0; k < semuaTabel; k++){

                        // k = Tabel ke -k

                        bantuLoop = mBlogDocument.select("table[class=data table table-striped table-hover no-margin]").select("tbody").eq(k).select("tr");

                        //Ini buat Judul Tabelnya
                        sub = mBlogDocument.select("div[class=x_title]").eq(k+1).select("h2");
                        String subteks = sub.text();

                        webSrapArray.add(subteks);
                        webSrapArray.add("");

                        for (int j = 0; j < bantuLoop.size(); j++){

                            String agregat = "";
                            String nama = "";

                            //Loop buat ngambil Nama
                            for (int i = 1; i <= 1; i++){
                                isiData = mBlogDocument.select("table[class=data table table-striped table-hover no-margin]").select("tbody").eq(k).select("tr").eq(j).select("td").eq(i);
                                nama = isiData.text();

                                //Loop buat ngambil Isi
                                for (int h = 2; h <= 2; h++){
                                    isiData = mBlogDocument.select("table[class=data table table-striped table-hover no-margin]").select("tbody").eq(k).select("tr").eq(j).select("td").eq(h);
                                    agregat = isiData.text();

                                    //webScrapResults.add(new WebScrapResult(0,(j+1) + "  " + nama,agregat));
                                    webSrapArray.add((j+1) + "  " + nama);
                                    webSrapArray.add(agregat);

                                }
                            }
                        }
                    }
                    //Simpen data Scraping ke dalam Preference
                    simpankePref();

                    //Ubah Status STOREDATA jadi sudah karena udah ngambil data dari Web
                    mainActivity.setTKPreference(STOREDATA,"sudah");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

                //Nampilin Data dari Preference
                //Kalau nggak ada Data nggak akan nampilin apa apa
                tampilkanDataPref();


            return null;
    }
    }

    public void simpankePref(){

        int d = 0;

        //Untuk setiap data di dalam webScrapArray (Isinya data Scraping) bakal dijadiin String terus
        //disimpen ke Preference satu satu kedalam bentuk variabel (SPKEY_DATA + angka)
        for (String dataTK : webSrapArray){

            if (d < webSrapArray.size()) {
                mainActivity.setTKPreference("SPKEY_DATATK" + d, dataTK);

            }

            d++;

        }

        mainActivity.setIntegerTKPreference("DATALIMIT",d);

    }

    public void tampilkanDataPref(){


        //Untuk setiap data TK di Preference bakal dimasukin ke webScrapResult
        //buat ditampilin
        for (int y = 0; y < mainActivity.getIntegerTKPreference("DATALIMIT"); y += 2 ){
            String prefnama = mainActivity.getTKPreference("SPKEY_DATATK"+y);
            String prefisi = mainActivity.getTKPreference("SPKEY_DATATK"+(y+1));

            int limit = mainActivity.getIntegerTKPreference("DATALIMIT");

            if (webScrapResults.size() < limit/2) {

                if (prefnama.contains("PTK")) {
                    webScrapResults.add(new WebScrapResult(R.drawable.ic_guru, prefnama, prefisi));
                } else if (prefnama.contains("Peserta Didik")) {
                    webScrapResults.add(new WebScrapResult(R.drawable.ic_siswa, prefnama, prefisi));
                } else if (prefnama.contains("Prasarana")) {
                    webScrapResults.add(new WebScrapResult(R.drawable.ic_ruang, prefnama, prefisi));
                } else if (prefnama.contains("Sarana")) {
                    webScrapResults.add(new WebScrapResult(R.drawable.ic_pras, prefnama, prefisi));
                } else {
                    webScrapResults.add(new WebScrapResult(0, prefnama, prefisi));
                }
            }
        }


    }
}