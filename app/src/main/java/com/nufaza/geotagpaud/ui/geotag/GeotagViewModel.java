package com.nufaza.geotagpaud.ui.geotag;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GeotagViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GeotagViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Geotag fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}