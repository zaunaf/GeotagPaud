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
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.faltenreich.skeletonlayout.SkeletonLayout;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;
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
    private TextView tv_agregat_dapodik_siswa;
    private MainActivity mainActivity;
    private ListView listView;
    private ListAdapter adapter;
    private SkeletonScreen skeletonScreen;
    private com.faltenreich.skeletonlayout.Skeleton skeleton;
    private View skel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.mainActivity = (MainActivity) getActivity();
        dataViewModel =
                ViewModelProviders.of(this).get(DataViewModel.class);
        View root = inflater.inflate(R.layout.fragment_data_listview, container, false);

        // new Scrap().execute();
        tv_agregat_dapodik_siswa = root.findViewById(R.id.agregatSiswaDapodik);

        skeleton = root.findViewById(R.id.skeletonLayout);
        skel = root.findViewById(R.id.skeletonLayout);
        skeleton.showSkeleton();
        skeleton.setShimmerDurationInMillis(1500);

        listView = root.findViewById(R.id.listView);
        listView.setOnItemClickListener(null);

        return root;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.onHiddenChanged(isVisibleToUser);

        if (isVisibleToUser) {
            skeleton.showOriginal();
            skel.setVisibility(View.GONE);
            List<WebScrapResult> webScrapResults = mainActivity.dataScrappingManager.getWebScrapResults();
            if (webScrapResults.size() > 4) {
                adapter = new ListAdapter(getActivity().getApplicationContext(), R.layout.custom_list, webScrapResults);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {

            }
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {

        super.onHiddenChanged(hidden);

        if (!hidden) {
            skeleton.showOriginal();
            skel.setVisibility(View.GONE);
            List<WebScrapResult> webScrapResults = mainActivity.dataScrappingManager.getWebScrapResults();
            if (webScrapResults.size() > 4) {
                adapter = new ListAdapter(getActivity().getApplicationContext(), R.layout.custom_list, webScrapResults);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {

            }
        }
    }
}