package com.nufaza.geotagpaud.ui.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

        WebScrapResult hero = webScrapList.get(position);


        imageView.setImageResource(hero.getImage());
        textViewName.setText(hero.getName());
        textViewAgregat.setText(hero.getAgregat());


        return view;
    }

}
