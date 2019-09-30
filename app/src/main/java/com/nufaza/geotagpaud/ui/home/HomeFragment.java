package com.nufaza.geotagpaud.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.nufaza.geotagpaud.MainActivity;
import com.nufaza.geotagpaud.R;
import com.nufaza.geotagpaud.model.Sekolah;
import com.nufaza.geotagpaud.model.Sekolah_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.LinkedHashMap;
import java.util.UUID;

import static com.nufaza.geotagpaud.MainActivity.SPKEY_SEKOLAH_ID;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View root;
    private MainActivity mainActivity;

    private LinkedHashMap<String,String> bentukpendidikanMap;
    private LinkedHashMap<String,String> kabkotaMap;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        mainActivity = (MainActivity) getActivity();

//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });



        // Load Maps Array
        loadMaps();
        updateView();

        return root;
    }

    public void updateView() {

        String key = MainActivity.SPKEY_SEKOLAH_ID;
        String sekolahIdStr = null;
        sekolahIdStr = mainActivity.getPreference(key);

        TextView namaTextView = ((TextView) root.findViewById(R.id.item_nama_sekolah));
        TextView npsnTextView = ((TextView) root.findViewById(R.id.npsn));
        TextView bentukPendidikanStrTextView = ((TextView) root.findViewById(R.id.bentuk_pendidikan_str));
        TextView alamatJalanTextView = ((TextView) root.findViewById(R.id.alamat_jalan));
        TextView namaKabkotaTextView = ((TextView) root.findViewById(R.id.nama_kabkota));
        TextView noTelpTextView = ((TextView) root.findViewById(R.id.no_telp));

        if (sekolahIdStr != "") {

            UUID sekolahId = UUID.fromString(sekolahIdStr);
            Sekolah sekolah = SQLite.select().from(Sekolah.class).where(Sekolah_Table.sekolah_id.eq(sekolahId)).querySingle();


            if (sekolah != null) {

                if (namaTextView != null) namaTextView.setText(String.valueOf(sekolah.getNama()));
                if (npsnTextView != null) npsnTextView.setText(String.valueOf(sekolah.getNpsn()));

                String bentukPendidikanId = sekolah.getBentukPendidikanId().toString();
                if (bentukPendidikanStrTextView != null)
                    bentukPendidikanStrTextView.setText(bentukpendidikanMap.get(bentukPendidikanId));
                if (alamatJalanTextView != null)
                    alamatJalanTextView.setText(sekolah.getAlamatJalan());

                String kodeKabkota = sekolah.getKodeWilayah();
                kodeKabkota = (kodeKabkota != null) ? kodeKabkota.substring(0, 4) + "00" : "";
                if (namaKabkotaTextView != null)
                    namaKabkotaTextView.setText(kabkotaMap.get(kodeKabkota));
                if (noTelpTextView != null) noTelpTextView.setText(sekolah.getNomorTelepon());
            }

        } else {

            if (namaTextView != null) namaTextView.setText("");
            if (npsnTextView != null) npsnTextView.setText("-");
            if (bentukPendidikanStrTextView != null) bentukPendidikanStrTextView.setText("-");
            if (alamatJalanTextView != null) alamatJalanTextView.setText("-");
            if (namaKabkotaTextView != null) namaKabkotaTextView.setText("");
            if (noTelpTextView != null) noTelpTextView.setText("-");

        }
    }

    private void loadMaps() {

        String[] keys = this.getResources().getStringArray(R.array.bentukpendidikanArray);
        String[] values = this.getResources().getStringArray(R.array.bentukpendidikanValues);
        bentukpendidikanMap = new LinkedHashMap<String,String>();
        for (int i = 0; i < Math.min(keys.length, values.length); ++i) {
            bentukpendidikanMap.put(keys[i], values[i]);
        }

        keys = this.getResources().getStringArray(R.array.kabkotaArray);
        values = this.getResources().getStringArray(R.array.kabkotaValues);
        kabkotaMap = new LinkedHashMap<String,String>();
        for (int i = 0; i < Math.min(keys.length, values.length); ++i) {
            kabkotaMap.put(keys[i], values[i]);
        }

    }
}