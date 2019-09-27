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

@Table(name = "foto", database = AppDatabase.class)
public class Foto extends BaseModel {

    @PrimaryKey
    public UUID foto_id;

    @Column
    public Integer jenis_foto_id;

    @Column
    public UUID sekolah_id;

    @Column
    public UUID pengguna_id;

    @Column
    public String judul;

    @Column
    public Date tgl_pengambilan;

    @Column
    public Integer tinggi_pixel;

    @Column
    public Integer lebar_pixel;

    @Column
    public Integer ukuran;

    @Column
    public String lintang;

    @Column
    public String bujur;

    @Column
    public Date tgl_pengiriman;


    public UUID getFotoId(){
        return foto_id;
    }

    public void setFotoId(UUID foto_id){
        this.foto_id = foto_id;
    }


    public Integer getJenisFotoId(){
        return jenis_foto_id;
    }

    public void setJenisFotoId(Integer jenis_foto_id){
        this.jenis_foto_id = jenis_foto_id;
    }

    public JenisFoto getJenisFoto(){
        return SQLite.select().from(JenisFoto.class).where(JenisFoto_Table.jenis_foto_id.eq(this.getJenisFotoId())).querySingle();
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


    public UUID getPenggunaId(){
        return pengguna_id;
    }

    public void setPenggunaId(UUID pengguna_id){
        this.pengguna_id = pengguna_id;
    }

    public Pengguna getPengguna(){
        return SQLite.select().from(Pengguna.class).where(Pengguna_Table.pengguna_id.eq(this.getPenggunaId())).querySingle();
    }


    public String getJudul(){
        return judul;
    }

    public void setJudul(String judul){
        this.judul = judul;
    }


    public Date getTglPengambilan(){
        return tgl_pengambilan;
    }

    public void setTglPengambilan(Date tgl_pengambilan){
        this.tgl_pengambilan = tgl_pengambilan;
    }


    public Integer getTinggiPixel(){
        return tinggi_pixel;
    }

    public void setTinggiPixel(Integer tinggi_pixel){
        this.tinggi_pixel = tinggi_pixel;
    }


    public Integer getLebarPixel(){
        return lebar_pixel;
    }

    public void setLebarPixel(Integer lebar_pixel){
        this.lebar_pixel = lebar_pixel;
    }


    public Integer getUkuran(){
        return ukuran;
    }

    public void setUkuran(Integer ukuran){
        this.ukuran = ukuran;
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


    public Date getTglPengiriman(){
        return tgl_pengiriman;
    }

    public void setTglPengiriman(Date tgl_pengiriman){
        this.tgl_pengiriman = tgl_pengiriman;
    }


    public void fromJsonObject(JSONObject obj) {
        try {
            if (obj.has("foto_id") && !obj.isNull("foto_id")){
                this.foto_id = (UUID) obj.get("foto_id");

            }
            if (obj.has("jenis_foto_id") && !obj.isNull("jenis_foto_id")){
                this.jenis_foto_id = (Integer) Integer.valueOf(obj.get("jenis_foto_id").toString());

            }
            if (obj.has("sekolah_id") && !obj.isNull("sekolah_id")){
                this.sekolah_id = (UUID) obj.get("sekolah_id");

            }
            if (obj.has("pengguna_id") && !obj.isNull("pengguna_id")){
                this.pengguna_id = (UUID) obj.get("pengguna_id");

            }
            if (obj.has("judul") && !obj.isNull("judul")){
                this.judul = (String) obj.get("judul");

            }
            if (obj.has("tgl_pengambilan") && !obj.isNull("tgl_pengambilan")){
                this.tgl_pengambilan = (Date) obj.get("tgl_pengambilan");

            }
            if (obj.has("tinggi_pixel") && !obj.isNull("tinggi_pixel")){
                this.tinggi_pixel = (Integer) Integer.valueOf(obj.get("tinggi_pixel").toString());

            }
            if (obj.has("lebar_pixel") && !obj.isNull("lebar_pixel")){
                this.lebar_pixel = (Integer) Integer.valueOf(obj.get("lebar_pixel").toString());

            }
            if (obj.has("ukuran") && !obj.isNull("ukuran")){
                this.ukuran = (Integer) Integer.valueOf(obj.get("ukuran").toString());

            }
            if (obj.has("lintang") && !obj.isNull("lintang")){
                this.lintang = (String) obj.get("lintang");

            }
            if (obj.has("bujur") && !obj.isNull("bujur")){
                this.bujur = (String) obj.get("bujur");

            }
            if (obj.has("tgl_pengiriman") && !obj.isNull("tgl_pengiriman")){
                this.tgl_pengiriman = (Date) obj.get("tgl_pengiriman");

            }
        } catch (Exception e) {
            Log.e("Error JSON", "Error parsing JSON");
        }
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("foto_id", foto_id);
            obj.put("jenis_foto_id", jenis_foto_id);
            obj.put("sekolah_id", sekolah_id);
            obj.put("pengguna_id", pengguna_id);
            obj.put("judul", judul);
            obj.put("tgl_pengambilan", tgl_pengambilan);
            obj.put("tinggi_pixel", tinggi_pixel);
            obj.put("lebar_pixel", lebar_pixel);
            obj.put("ukuran", ukuran);
            obj.put("lintang", lintang);
            obj.put("bujur", bujur);
            obj.put("tgl_pengiriman", tgl_pengiriman);
        } catch (JSONException e) {
            Log.e("Error JSON", "Error creating JSON: " + e.getMessage());
        }
        return obj;
    }
}