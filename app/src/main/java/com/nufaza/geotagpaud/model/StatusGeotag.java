package com.nufaza.geotagpaud.model;

import com.nufaza.geotagpaud.AppDatabase;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.math.BigInteger;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.text.SimpleDateFormat;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

@Table(name = "status_geotag", database = AppDatabase.class)
public class StatusGeotag extends BaseModel {

    @PrimaryKey
    public Integer status_geotag_id;

    @Column
    public String nama_status_geotag;


    public Integer getStatusGeotagId(){
        return status_geotag_id;
    }

    public void setStatusGeotagId(Integer status_geotag_id){
        this.status_geotag_id = status_geotag_id;
    }


    public String getNamaStatusGeotag(){
        return nama_status_geotag;
    }

    public void setNamaStatusGeotag(String nama_status_geotag){
        this.nama_status_geotag = nama_status_geotag;
    }


    public void fromJsonObject(JSONObject obj) {
        try {
            if (obj.has("status_geotag_id") && !obj.isNull("status_geotag_id")){
                this.status_geotag_id = (Integer) Integer.valueOf(obj.get("status_geotag_id").toString());

            }
            if (obj.has("nama_status_geotag") && !obj.isNull("nama_status_geotag")){
                this.nama_status_geotag = (String) obj.get("nama_status_geotag");

            }
        } catch (Exception e) {
            Log.e("Error JSON", "Error parsing JSON");
        }
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("status_geotag_id", status_geotag_id);
            obj.put("nama_status_geotag", nama_status_geotag);
        } catch (JSONException e) {
            Log.e("Error JSON", "Error creating JSON: " + e.getMessage());
        }
        return obj;
    }
}