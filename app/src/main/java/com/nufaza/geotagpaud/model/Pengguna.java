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

@Table(name = "pengguna", database = AppDatabase.class)
public class Pengguna extends BaseModel {

    @PrimaryKey
    public UUID pengguna_id;

    @Column
    public UUID sekolah_id;

    @Column
    public String username;

    @Column
    public String password;

    @Column
    public String nama;

    @Column
    public String nip_nim;

    @Column
    public String jabatan_lembaga;

    @Column
    public String ym;

    @Column
    public String skype;

    @Column
    public String alamat;

    @Column
    public String kode_wilayah;

    @Column
    public String no_telepon;

    @Column
    public String no_hp;

    @Column
    public String aktif;

    @Column
    public String ptk_id;

    @Column
    public Integer peran_id;

    @Column
    public String lembaga_id;

    @Column
    public String yayasan_id;

    @Column
    public String la_id;

    @Column
    public String dudi_id;

    @Column
    public Date create_date;

    @Column
    public String roles;

    @Column
    public Date last_update;

    @Column
    public String soft_delete;

    @Column
    public Date last_sync;

    @Column
    public String updater_id;


    public UUID getPenggunaId(){
        return pengguna_id;
    }

    public void setPenggunaId(UUID pengguna_id){
        this.pengguna_id = pengguna_id;
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


    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }


    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }


    public String getNama(){
        return nama;
    }

    public void setNama(String nama){
        this.nama = nama;
    }


    public String getNipNim(){
        return nip_nim;
    }

    public void setNipNim(String nip_nim){
        this.nip_nim = nip_nim;
    }


    public String getJabatanLembaga(){
        return jabatan_lembaga;
    }

    public void setJabatanLembaga(String jabatan_lembaga){
        this.jabatan_lembaga = jabatan_lembaga;
    }


    public String getYm(){
        return ym;
    }

    public void setYm(String ym){
        this.ym = ym;
    }


    public String getSkype(){
        return skype;
    }

    public void setSkype(String skype){
        this.skype = skype;
    }


    public String getAlamat(){
        return alamat;
    }

    public void setAlamat(String alamat){
        this.alamat = alamat;
    }


    public String getKodeWilayah(){
        return kode_wilayah;
    }

    public void setKodeWilayah(String kode_wilayah){
        this.kode_wilayah = kode_wilayah;
    }


    public String getNoTelepon(){
        return no_telepon;
    }

    public void setNoTelepon(String no_telepon){
        this.no_telepon = no_telepon;
    }


    public String getNoHp(){
        return no_hp;
    }

    public void setNoHp(String no_hp){
        this.no_hp = no_hp;
    }


    public String getAktif(){
        return aktif;
    }

    public void setAktif(String aktif){
        this.aktif = aktif;
    }


    public String getPtkId(){
        return ptk_id;
    }

    public void setPtkId(String ptk_id){
        this.ptk_id = ptk_id;
    }


    public Integer getPeranId(){
        return peran_id;
    }

    public void setPeranId(Integer peran_id){
        this.peran_id = peran_id;
    }


    public String getLembagaId(){
        return lembaga_id;
    }

    public void setLembagaId(String lembaga_id){
        this.lembaga_id = lembaga_id;
    }


    public String getYayasanId(){
        return yayasan_id;
    }

    public void setYayasanId(String yayasan_id){
        this.yayasan_id = yayasan_id;
    }


    public String getLaId(){
        return la_id;
    }

    public void setLaId(String la_id){
        this.la_id = la_id;
    }


    public String getDudiId(){
        return dudi_id;
    }

    public void setDudiId(String dudi_id){
        this.dudi_id = dudi_id;
    }


    public Date getCreateDate(){
        return create_date;
    }

    public void setCreateDate(Date create_date){
        this.create_date = create_date;
    }


    public String getRoles(){
        return roles;
    }

    public void setRoles(String roles){
        this.roles = roles;
    }


    public Date getLastUpdate(){
        return last_update;
    }

    public void setLastUpdate(Date last_update){
        this.last_update = last_update;
    }


    public String getSoftDelete(){
        return soft_delete;
    }

    public void setSoftDelete(String soft_delete){
        this.soft_delete = soft_delete;
    }


    public Date getLastSync(){
        return last_sync;
    }

    public void setLastSync(Date last_sync){
        this.last_sync = last_sync;
    }


    public String getUpdaterId(){
        return updater_id;
    }

    public void setUpdaterId(String updater_id){
        this.updater_id = updater_id;
    }


    public void fromJsonObject(JSONObject obj) {
        try {
            if (obj.has("pengguna_id") && !obj.isNull("pengguna_id")){
                this.pengguna_id = (UUID) obj.get("pengguna_id");

            }
            if (obj.has("sekolah_id") && !obj.isNull("sekolah_id")){
                this.sekolah_id = (UUID) obj.get("sekolah_id");

            }
            if (obj.has("username") && !obj.isNull("username")){
                this.username = (String) obj.get("username");

            }
            if (obj.has("password") && !obj.isNull("password")){
                this.password = (String) obj.get("password");

            }
            if (obj.has("nama") && !obj.isNull("nama")){
                this.nama = (String) obj.get("nama");

            }
            if (obj.has("nip_nim") && !obj.isNull("nip_nim")){
                this.nip_nim = (String) obj.get("nip_nim");

            }
            if (obj.has("jabatan_lembaga") && !obj.isNull("jabatan_lembaga")){
                this.jabatan_lembaga = (String) obj.get("jabatan_lembaga");

            }
            if (obj.has("ym") && !obj.isNull("ym")){
                this.ym = (String) obj.get("ym");

            }
            if (obj.has("skype") && !obj.isNull("skype")){
                this.skype = (String) obj.get("skype");

            }
            if (obj.has("alamat") && !obj.isNull("alamat")){
                this.alamat = (String) obj.get("alamat");

            }
            if (obj.has("kode_wilayah") && !obj.isNull("kode_wilayah")){
                this.kode_wilayah = (String) obj.get("kode_wilayah");

            }
            if (obj.has("no_telepon") && !obj.isNull("no_telepon")){
                this.no_telepon = (String) obj.get("no_telepon");

            }
            if (obj.has("no_hp") && !obj.isNull("no_hp")){
                this.no_hp = (String) obj.get("no_hp");

            }
            if (obj.has("aktif") && !obj.isNull("aktif")){
                this.aktif = (String) obj.get("aktif");

            }
            if (obj.has("ptk_id") && !obj.isNull("ptk_id")){
                this.ptk_id = (String) obj.get("ptk_id");

            }
            if (obj.has("peran_id") && !obj.isNull("peran_id")){
                this.peran_id = (Integer) Integer.valueOf(obj.get("peran_id").toString());

            }
            if (obj.has("lembaga_id") && !obj.isNull("lembaga_id")){
                this.lembaga_id = (String) obj.get("lembaga_id");

            }
            if (obj.has("yayasan_id") && !obj.isNull("yayasan_id")){
                this.yayasan_id = (String) obj.get("yayasan_id");

            }
            if (obj.has("la_id") && !obj.isNull("la_id")){
                this.la_id = (String) obj.get("la_id");

            }
            if (obj.has("dudi_id") && !obj.isNull("dudi_id")){
                this.dudi_id = (String) obj.get("dudi_id");

            }
            if (obj.has("create_date") && !obj.isNull("create_date")){
                this.create_date = (Date) obj.get("create_date");

            }
            if (obj.has("roles") && !obj.isNull("roles")){
                this.roles = (String) obj.get("roles");

            }
            if (obj.has("last_update") && !obj.isNull("last_update")){
                this.last_update = (Date) obj.get("last_update");

            }
            if (obj.has("soft_delete") && !obj.isNull("soft_delete")){
                this.soft_delete = (String) obj.get("soft_delete");

            }
            if (obj.has("last_sync") && !obj.isNull("last_sync")){
                this.last_sync = (Date) obj.get("last_sync");

            }
            if (obj.has("updater_id") && !obj.isNull("updater_id")){
                this.updater_id = (String) obj.get("updater_id");

            }
        } catch (Exception e) {
            Log.e("Error JSON", "Error parsing JSON");
        }
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("pengguna_id", pengguna_id);
            obj.put("sekolah", sekolah_id);
            obj.put("username", username);
            obj.put("password", password);
            obj.put("nama", nama);
            obj.put("nip_nim", nip_nim);
            obj.put("jabatan_lembaga", jabatan_lembaga);
            obj.put("ym", ym);
            obj.put("skype", skype);
            obj.put("alamat", alamat);
            obj.put("kode_wilayah", kode_wilayah);
            obj.put("no_telepon", no_telepon);
            obj.put("no_hp", no_hp);
            obj.put("aktif", aktif);
            obj.put("ptk_id", ptk_id);
            obj.put("peran_id", peran_id);
            obj.put("lembaga_id", lembaga_id);
            obj.put("yayasan_id", yayasan_id);
            obj.put("la_id", la_id);
            obj.put("dudi_id", dudi_id);
            obj.put("create_date", create_date);
            obj.put("roles", roles);
            obj.put("last_update", last_update);
            obj.put("soft_delete", soft_delete);
            obj.put("last_sync", last_sync);
            obj.put("updater_id", updater_id);
        } catch (JSONException e) {
            Log.e("Error JSON", "Error creating JSON: " + e.getMessage());
        }
        return obj;
    }
}