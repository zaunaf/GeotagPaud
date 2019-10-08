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

@Table(name = "geotag", database = AppDatabase.class)
public class Geotag extends BaseModel {

    @PrimaryKey
    public UUID geotag_id;

    @Column
    public UUID sekolah_id;

    @Column
    public Integer status_geotag_id;

    @Column
    public UUID pengguna_id;

    @Column
    public Date tgl_pengambilan;

    @Column
    public String lintang;

    @Column
    public String bujur;

    @Column
    public String petugas_link;

    @Column
    public String sekolah_link;

    @Column
    public Date tgl_pengiriman;

    @Column
    public Integer status_data;


    public UUID getGeotagId(){
        return geotag_id;
    }

    public void setGeotagId(UUID geotag_id){
        this.geotag_id = geotag_id;
    }


    public UUID getSekolahId(){
        return sekolah_id;
    }

    public void setSekolahId(UUID sekolah_id){
        this.sekolah_id = sekolah_id;
    }

    public Sekolah getSekolah(){
        return SQLite.select().from(Sekolah.class).where(Sekolah_Table.sekolah_id.eq(this.getSekolahId())).querySingle();
    }


    public Integer getStatusGeotagId(){
        return status_geotag_id;
    }

    public void setStatusGeotagId(Integer status_geotag_id){
        this.status_geotag_id = status_geotag_id;
    }

    public StatusGeotag getStatusGeotag(){
        return SQLite.select().from(StatusGeotag.class).where(StatusGeotag_Table.status_geotag_id.eq(this.getStatusGeotagId())).querySingle();
    }


    public UUID getPenggunaId(){
        return pengguna_id;
    }

    public void setPenggunaId(UUID pengguna_id){
        this.pengguna_id = pengguna_id;
    }

    public Pengguna getPengguna(){
        return SQLite.select().from(Pengguna.class).where(Pengguna_Table.pengguna_id.eq(this.getPenggunaId())).querySingle();
    }


    public Date getTglPengambilan(){
        return tgl_pengambilan;
    }

    public void setTglPengambilan(Date tgl_pengambilan){
        this.tgl_pengambilan = tgl_pengambilan;
    }


    public String getLintang(){
        return lintang;
    }

    public void setLintang(String lintang){
        this.lintang = lintang;
    }


    public String getBujur(){
        return bujur;
    }

    public void setBujur(String bujur){
        this.bujur = bujur;
    }


    public String getPetugasLink(){
        return petugas_link;
    }

    public void setPetugasLink(String petugas_link){
        this.petugas_link = petugas_link;
    }


    public String getSekolahLink(){
        return sekolah_link;
    }

    public void setSekolahLink(String sekolah_link){
        this.sekolah_link = sekolah_link;
    }


    public Date getTglPengiriman(){
        return tgl_pengiriman;
    }

    public void setTglPengiriman(Date tgl_pengiriman){
        this.tgl_pengiriman = tgl_pengiriman;
    }


    public Integer getStatusData(){
        return status_data;
    }

    public void setStatusData(Integer status_data){
        this.status_data = status_data;
    }


    public void fromJsonObject(JSONObject obj) {
        try {
            if (obj.has("geotag_id") && !obj.isNull("geotag_id")){
                this.geotag_id = (UUID) obj.get("geotag_id");

            }
            if (obj.has("sekolah_id") && !obj.isNull("sekolah_id")){
                this.sekolah_id = (UUID) obj.get("sekolah_id");

            }
            if (obj.has("status_geotag_id") && !obj.isNull("status_geotag_id")){
                this.status_geotag_id = (Integer) Integer.valueOf(obj.get("status_geotag_id").toString());

            }
            if (obj.has("pengguna_id") && !obj.isNull("pengguna_id")){
                this.pengguna_id = (UUID) obj.get("pengguna_id");

            }
            if (obj.has("tgl_pengambilan") && !obj.isNull("tgl_pengambilan")){
                this.tgl_pengambilan = (Date) obj.get("tgl_pengambilan");

            }
            if (obj.has("lintang") && !obj.isNull("lintang")){
                this.lintang = (String) obj.get("lintang");

            }
            if (obj.has("bujur") && !obj.isNull("bujur")){
                this.bujur = (String) obj.get("bujur");

            }
            if (obj.has("petugas_link") && !obj.isNull("petugas_link")){
                this.petugas_link = (String) obj.get("petugas_link");

            }
            if (obj.has("sekolah_link") && !obj.isNull("sekolah_link")){
                this.sekolah_link = (String) obj.get("sekolah_link");

            }
            if (obj.has("tgl_pengiriman") && !obj.isNull("tgl_pengiriman")){
                this.tgl_pengiriman = (Date) obj.get("tgl_pengiriman");

            }
            if (obj.has("status_data") && !obj.isNull("status_data")){
                this.status_data = (Integer) Integer.valueOf(obj.get("status_data").toString());

            }
        } catch (Exception e) {
            Log.e("Error JSON", "Error parsing JSON");
        }
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("geotag_id", geotag_id);
            obj.put("sekolah_id", sekolah_id);
            obj.put("status_geotag_id", status_geotag_id);
            obj.put("pengguna_id", pengguna_id);
            obj.put("tgl_pengambilan", tgl_pengambilan);
            obj.put("lintang", lintang);
            obj.put("bujur", bujur);
            obj.put("petugas_link", petugas_link);
            obj.put("sekolah_link", sekolah_link);
            obj.put("tgl_pengiriman", tgl_pengiriman);
            obj.put("status_data", status_data);
        } catch (JSONException e) {
            Log.e("Error JSON", "Error creating JSON: " + e.getMessage());
        }
        return obj;
    }
}