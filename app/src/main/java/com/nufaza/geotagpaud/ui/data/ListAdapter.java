package com.nufaza.geotagpaud.ui.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.nufaza.geotagpaud.R;
import com.nufaza.geotagpaud.ui.data.WebScrapResult;

import java.util.List;


public class ListAdapter extends ArrayAdapter<WebScrapResult> {

    List<WebScrapResult> webScrapList;

    Context context;

    int resource;

    public ListAdapter(Context context, int resource, List<WebScrapResult>  scrapResults) {
        super(context, resource, scrapResults);
        this.context = context;
        this.resource = resource;
        this.webScrapList = scrapResults;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);

        ImageView imageView = view.findViewById(R.id.iconList);
        TextView textViewName = view.findViewById(R.id.labelList);
        TextView textViewAgregat = view.findViewById(R.id.agregatList);


        final Typeface faceMed = ResourcesCompat.getFont(context, R.font.quicksand_semibold);
        final Typeface face = ResourcesCompat.getFont(context, R.font.quicksand_regular);
        textViewName.setTypeface(face);

        WebScrapResult hero = webScrapList.get(position);

        String prefnama = hero.getName();
        if (prefnama.contains("PTK")) {
            prefnama = "PTK";
            textViewName.setTypeface(faceMed);
        } else if (prefnama.contains("Peserta Didik")) {
            prefnama = "Peserta Didik";
            textViewName.setTypeface(faceMed);
        } else if (prefnama.contains("Prasarana")) {
            prefnama = "Prasarana";
            textViewName.setTypeface(faceMed);
        } else if (prefnama.contains("Sarana")) {
            prefnama = "Sarana";
            textViewName.setTypeface(faceMed);
        }

        imageView.setImageResource(hero.getImage());
        textViewName.setText(prefnama);
        textViewAgregat.setText(hero.getAgregat());


        return view;
    }

}
