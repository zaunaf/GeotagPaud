package com.nufaza.geotagpaud.managers;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.nufaza.geotagpaud.MainActivity;
import com.nufaza.geotagpaud.R;
import com.nufaza.geotagpaud.ui.data.WebScrapResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.nufaza.geotagpaud.MainActivity.STOREDATA;

public class DataScrappingManager {

    private final MainActivity mainActivity;
    private ArrayList<String> webScrapArray = new ArrayList<>();
    private List<WebScrapResult> webScrapResults = new ArrayList<>();

    public DataScrappingManager(MainActivity mainActivity) {
        super();
        this.mainActivity = mainActivity;
    }

    // Only called after login
    public void executeScrap(){
        new Scrap().execute();
    }

    private class Scrap extends AsyncTask {

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Object[] objects) {
            String url = "https://manajemen.paud-dikmas.kemdikbud.go.id/Profil/Index/";
            // mainActivity = (MainActivity) getActivity();

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

                        webScrapArray.add(subteks);
                        webScrapArray.add("");

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
                                    // webScrapArray.add((j+1) + "  " + nama);
                                    webScrapArray.add(nama);
                                    webScrapArray.add(agregat);

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

            // Nampilin Data dari Preference
            // Kalau nggak ada Data nggak akan nampilin apa apa
            // tampilkanDataPref();


            return null;
        }
    }

    public void simpankePref(){

        int d = 0;

        //Untuk setiap data di dalam webScrapArray (Isinya data Scraping) bakal dijadiin String terus
        //disimpen ke Preference satu satu kedalam bentuk variabel (SPKEY_DATA + angka)
        for (String dataTK : webScrapArray){

            if (d < webScrapArray.size()) {
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

    // Called after login and on application bootstrap
    public List<WebScrapResult> getWebScrapResults(){
        tampilkanDataPref();
        return this.webScrapResults;
    }

    // To reset scrap results
    public void flushScrapResults() {
        webScrapArray.clear();
        webScrapResults.clear();
        webScrapArray.clear();
    }

}
