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

@Table(name = "jenis_foto", database = AppDatabase.class)
public class JenisFoto extends BaseModel {

    @PrimaryKey
    public Integer jenis_foto_id;

    @Column
    public String nama_jenis_foto;

    @Column
    public String instruksi;

    @Column
    public Integer status_isian;


    public Integer getJenisFotoId(){
        return jenis_foto_id;
    }

    public void setJenisFotoId(Integer jenis_foto_id){
        this.jenis_foto_id = jenis_foto_id;
    }


    public String getNamaJenisFoto(){
        return nama_jenis_foto;
    }

    public void setNamaJenisFoto(String nama_jenis_foto){
        this.nama_jenis_foto = nama_jenis_foto;
    }


    public String getInstruksi(){
        return instruksi;
    }

    public void setInstruksi(String instruksi){
        this.instruksi = instruksi;
    }


    public Integer getStatusIsian(){
        return status_isian;
    }

    public void setStatusIsian(Integer status_isian){
        this.status_isian = status_isian;
    }


    public void fromJsonObject(JSONObject obj) {
        try {
            if (obj.has("jenis_foto_id") && !obj.isNull("jenis_foto_id")){
                this.jenis_foto_id = (Integer) Integer.valueOf(obj.get("jenis_foto_id").toString());

            }
            if (obj.has("nama_jenis_foto") && !obj.isNull("nama_jenis_foto")){
                this.nama_jenis_foto = (String) obj.get("nama_jenis_foto");

            }
            if (obj.has("instruksi") && !obj.isNull("instruksi")){
                this.instruksi = (String) obj.get("instruksi");

            }
            if (obj.has("status_isian") && !obj.isNull("status_isian")){
                this.status_isian = (Integer) Integer.valueOf(obj.get("status_isian").toString());

            }
        } catch (Exception e) {
            Log.e("Error JSON", "Error parsing JSON");
        }
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("jenis_foto_id", jenis_foto_id);
            obj.put("nama_jenis_foto", nama_jenis_foto);
            obj.put("instruksi", instruksi);
            obj.put("status_isian", status_isian);
        } catch (JSONException e) {
            Log.e("Error JSON", "Error creating JSON: " + e.getMessage());
        }
        return obj;
    }
}