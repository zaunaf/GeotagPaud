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

@Table(name = "sekolah", database = AppDatabase.class)
public class Sekolah extends BaseModel {

    @PrimaryKey
    public UUID sekolah_id;

    @Column
    public String nama;

    @Column
    public String nama_nomenklatur;

    @Column
    public String nss;

    @Column
    public String npsn;

    @Column
    public Integer bentuk_pendidikan_id;

    @Column
    public String alamat_jalan;

    @Column
    public String rt;

    @Column
    public String rw;

    @Column
    public String nama_dusun;

    @Column
    public String desa_kelurahan;

    @Column
    public String kode_wilayah;

    @Column
    public String kode_pos;

    @Column
    public String lintang;

    @Column
    public String bujur;

    @Column
    public String nomor_telepon;

    @Column
    public String nomor_fax;

    @Column
    public String email;

    @Column
    public String website;

    @Column
    public Integer kebutuhan_khusus_id;

    @Column
    public String status_sekolah;

    @Column
    public String sk_pendirian_sekolah;

    @Column
    public Date tanggal_sk_pendirian;

    @Column
    public String status_kepemilikan_id;

    @Column
    public String yayasan_id;

    @Column
    public String sk_izin_operasional;

    @Column
    public Date tanggal_sk_izin_operasional;

    @Column
    public String no_rekening;

    @Column
    public String nama_bank;

    @Column
    public String cabang_kcp_unit;

    @Column
    public String rekening_atas_nama;

    @Column
    public String mbs;

    @Column
    public String luas_tanah_milik;

    @Column
    public String luas_tanah_bukan_milik;

    @Column
    public BigInteger kode_registrasi;

    @Column
    public String npwp;

    @Column
    public String nm_wp;

    @Column
    public String flag;

    @Column
    public Date create_date;

    @Column
    public Date last_update;

    @Column
    public String soft_delete;

    @Column
    public Date last_sync;

    @Column
    public String updater_id;


    public UUID getSekolahId(){
        return sekolah_id;
    }

    public void setSekolahId(UUID sekolah_id){
        this.sekolah_id = sekolah_id;
    }


    public String getNama(){
        return nama;
    }

    public void setNama(String nama){
        this.nama = nama;
    }


    public String getNamaNomenklatur(){
        return nama_nomenklatur;
    }

    public void setNamaNomenklatur(String nama_nomenklatur){
        this.nama_nomenklatur = nama_nomenklatur;
    }


    public String getNss(){
        return nss;
    }

    public void setNss(String nss){
        this.nss = nss;
    }


    public String getNpsn(){
        return npsn;
    }

    public void setNpsn(String npsn){
        this.npsn = npsn;
    }


    public Integer getBentukPendidikanId(){
        return bentuk_pendidikan_id;
    }

    public void setBentukPendidikanId(Integer bentuk_pendidikan_id){
        this.bentuk_pendidikan_id = bentuk_pendidikan_id;
    }


    public String getAlamatJalan(){
        return alamat_jalan;
    }

    public void setAlamatJalan(String alamat_jalan){
        this.alamat_jalan = alamat_jalan;
    }


    public String getRt(){
        return rt;
    }

    public void setRt(String rt){
        this.rt = rt;
    }


    public String getRw(){
        return rw;
    }

    public void setRw(String rw){
        this.rw = rw;
    }


    public String getNamaDusun(){
        return nama_dusun;
    }

    public void setNamaDusun(String nama_dusun){
        this.nama_dusun = nama_dusun;
    }


    public String getDesaKelurahan(){
        return desa_kelurahan;
    }

    public void setDesaKelurahan(String desa_kelurahan){
        this.desa_kelurahan = desa_kelurahan;
    }


    public String getKodeWilayah(){
        return kode_wilayah;
    }

    public void setKodeWilayah(String kode_wilayah){
        this.kode_wilayah = kode_wilayah;
    }


    public String getKodePos(){
        return kode_pos;
    }

    public void setKodePos(String kode_pos){
        this.kode_pos = kode_pos;
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


    public String getNomorTelepon(){
        return nomor_telepon;
    }

    public void setNomorTelepon(String nomor_telepon){
        this.nomor_telepon = nomor_telepon;
    }


    public String getNomorFax(){
        return nomor_fax;
    }

    public void setNomorFax(String nomor_fax){
        this.nomor_fax = nomor_fax;
    }


    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }


    public String getWebsite(){
        return website;
    }

    public void setWebsite(String website){
        this.website = website;
    }


    public Integer getKebutuhanKhususId(){
        return kebutuhan_khusus_id;
    }

    public void setKebutuhanKhususId(Integer kebutuhan_khusus_id){
        this.kebutuhan_khusus_id = kebutuhan_khusus_id;
    }


    public String getStatusSekolah(){
        return status_sekolah;
    }

    public void setStatusSekolah(String status_sekolah){
        this.status_sekolah = status_sekolah;
    }


    public String getSkPendirianSekolah(){
        return sk_pendirian_sekolah;
    }

    public void setSkPendirianSekolah(String sk_pendirian_sekolah){
        this.sk_pendirian_sekolah = sk_pendirian_sekolah;
    }


    public Date getTanggalSkPendirian(){
        return tanggal_sk_pendirian;
    }

    public void setTanggalSkPendirian(Date tanggal_sk_pendirian){
        this.tanggal_sk_pendirian = tanggal_sk_pendirian;
    }


    public String getStatusKepemilikanId(){
        return status_kepemilikan_id;
    }

    public void setStatusKepemilikanId(String status_kepemilikan_id){
        this.status_kepemilikan_id = status_kepemilikan_id;
    }


    public String getYayasanId(){
        return yayasan_id;
    }

    public void setYayasanId(String yayasan_id){
        this.yayasan_id = yayasan_id;
    }


    public String getSkIzinOperasional(){
        return sk_izin_operasional;
    }

    public void setSkIzinOperasional(String sk_izin_operasional){
        this.sk_izin_operasional = sk_izin_operasional;
    }


    public Date getTanggalSkIzinOperasional(){
        return tanggal_sk_izin_operasional;
    }

    public void setTanggalSkIzinOperasional(Date tanggal_sk_izin_operasional){
        this.tanggal_sk_izin_operasional = tanggal_sk_izin_operasional;
    }


    public String getNoRekening(){
        return no_rekening;
    }

    public void setNoRekening(String no_rekening){
        this.no_rekening = no_rekening;
    }


    public String getNamaBank(){
        return nama_bank;
    }

    public void setNamaBank(String nama_bank){
        this.nama_bank = nama_bank;
    }


    public String getCabangKcpUnit(){
        return cabang_kcp_unit;
    }

    public void setCabangKcpUnit(String cabang_kcp_unit){
        this.cabang_kcp_unit = cabang_kcp_unit;
    }


    public String getRekeningAtasNama(){
        return rekening_atas_nama;
    }

    public void setRekeningAtasNama(String rekening_atas_nama){
        this.rekening_atas_nama = rekening_atas_nama;
    }


    public String getMbs(){
        return mbs;
    }

    public void setMbs(String mbs){
        this.mbs = mbs;
    }


    public String getLuasTanahMilik(){
        return luas_tanah_milik;
    }

    public void setLuasTanahMilik(String luas_tanah_milik){
        this.luas_tanah_milik = luas_tanah_milik;
    }


    public String getLuasTanahBukanMilik(){
        return luas_tanah_bukan_milik;
    }

    public void setLuasTanahBukanMilik(String luas_tanah_bukan_milik){
        this.luas_tanah_bukan_milik = luas_tanah_bukan_milik;
    }


    public BigInteger getKodeRegistrasi(){
        return kode_registrasi;
    }

    public void setKodeRegistrasi(BigInteger kode_registrasi){
        this.kode_registrasi = kode_registrasi;
    }


    public String getNpwp(){
        return npwp;
    }

    public void setNpwp(String npwp){
        this.npwp = npwp;
    }


    public String getNmWp(){
        return nm_wp;
    }

    public void setNmWp(String nm_wp){
        this.nm_wp = nm_wp;
    }


    public String getFlag(){
        return flag;
    }

    public void setFlag(String flag){
        this.flag = flag;
    }


    public Date getCreateDate(){
        return create_date;
    }

    public void setCreateDate(Date create_date){
        this.create_date = create_date;
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
            if (obj.has("sekolah_id") && !obj.isNull("sekolah_id")){
                this.sekolah_id = (UUID) obj.get("sekolah_id");

            }
            if (obj.has("nama") && !obj.isNull("nama")){
                this.nama = (String) obj.get("nama");

            }
            if (obj.has("nama_nomenklatur") && !obj.isNull("nama_nomenklatur")){
                this.nama_nomenklatur = (String) obj.get("nama_nomenklatur");

            }
            if (obj.has("nss") && !obj.isNull("nss")){
                this.nss = (String) obj.get("nss");

            }
            if (obj.has("npsn") && !obj.isNull("npsn")){
                this.npsn = (String) obj.get("npsn");

            }
            if (obj.has("bentuk_pendidikan_id") && !obj.isNull("bentuk_pendidikan_id")){
                this.bentuk_pendidikan_id = (Integer) Integer.valueOf(obj.get("bentuk_pendidikan_id").toString());

            }
            if (obj.has("alamat_jalan") && !obj.isNull("alamat_jalan")){
                this.alamat_jalan = (String) obj.get("alamat_jalan");

            }
            if (obj.has("rt") && !obj.isNull("rt")){
                this.rt = (String) obj.get("rt");

            }
            if (obj.has("rw") && !obj.isNull("rw")){
                this.rw = (String) obj.get("rw");

            }
            if (obj.has("nama_dusun") && !obj.isNull("nama_dusun")){
                this.nama_dusun = (String) obj.get("nama_dusun");

            }
            if (obj.has("desa_kelurahan") && !obj.isNull("desa_kelurahan")){
                this.desa_kelurahan = (String) obj.get("desa_kelurahan");

            }
            if (obj.has("kode_wilayah") && !obj.isNull("kode_wilayah")){
                this.kode_wilayah = (String) obj.get("kode_wilayah");

            }
            if (obj.has("kode_pos") && !obj.isNull("kode_pos")){
                this.kode_pos = (String) obj.get("kode_pos");

            }
            if (obj.has("lintang") && !obj.isNull("lintang")){
                this.lintang = (String) obj.get("lintang");

            }
            if (obj.has("bujur") && !obj.isNull("bujur")){
                this.bujur = (String) obj.get("bujur");

            }
            if (obj.has("nomor_telepon") && !obj.isNull("nomor_telepon")){
                this.nomor_telepon = (String) obj.get("nomor_telepon");

            }
            if (obj.has("nomor_fax") && !obj.isNull("nomor_fax")){
                this.nomor_fax = (String) obj.get("nomor_fax");

            }
            if (obj.has("email") && !obj.isNull("email")){
                this.email = (String) obj.get("email");

            }
            if (obj.has("website") && !obj.isNull("website")){
                this.website = (String) obj.get("website");

            }
            if (obj.has("kebutuhan_khusus_id") && !obj.isNull("kebutuhan_khusus_id")){
                this.kebutuhan_khusus_id = (Integer) Integer.valueOf(obj.get("kebutuhan_khusus_id").toString());

            }
            if (obj.has("status_sekolah") && !obj.isNull("status_sekolah")){
                this.status_sekolah = (String) obj.get("status_sekolah");

            }
            if (obj.has("sk_pendirian_sekolah") && !obj.isNull("sk_pendirian_sekolah")){
                this.sk_pendirian_sekolah = (String) obj.get("sk_pendirian_sekolah");

            }
            if (obj.has("tanggal_sk_pendirian") && !obj.isNull("tanggal_sk_pendirian")){
                this.tanggal_sk_pendirian = (Date) obj.get("tanggal_sk_pendirian");

            }
            if (obj.has("status_kepemilikan_id") && !obj.isNull("status_kepemilikan_id")){
                this.status_kepemilikan_id = (String) obj.get("status_kepemilikan_id");

            }
            if (obj.has("yayasan_id") && !obj.isNull("yayasan_id")){
                this.yayasan_id = (String) obj.get("yayasan_id");

            }
            if (obj.has("sk_izin_operasional") && !obj.isNull("sk_izin_operasional")){
                this.sk_izin_operasional = (String) obj.get("sk_izin_operasional");

            }
            if (obj.has("tanggal_sk_izin_operasional") && !obj.isNull("tanggal_sk_izin_operasional")){
                this.tanggal_sk_izin_operasional = (Date) obj.get("tanggal_sk_izin_operasional");

            }
            if (obj.has("no_rekening") && !obj.isNull("no_rekening")){
                this.no_rekening = (String) obj.get("no_rekening");

            }
            if (obj.has("nama_bank") && !obj.isNull("nama_bank")){
                this.nama_bank = (String) obj.get("nama_bank");

            }
            if (obj.has("cabang_kcp_unit") && !obj.isNull("cabang_kcp_unit")){
                this.cabang_kcp_unit = (String) obj.get("cabang_kcp_unit");

            }
            if (obj.has("rekening_atas_nama") && !obj.isNull("rekening_atas_nama")){
                this.rekening_atas_nama = (String) obj.get("rekening_atas_nama");

            }
            if (obj.has("mbs") && !obj.isNull("mbs")){
                this.mbs = (String) obj.get("mbs");

            }
            if (obj.has("luas_tanah_milik") && !obj.isNull("luas_tanah_milik")){
                this.luas_tanah_milik = (String) obj.get("luas_tanah_milik");

            }
            if (obj.has("luas_tanah_bukan_milik") && !obj.isNull("luas_tanah_bukan_milik")){
                this.luas_tanah_bukan_milik = (String) obj.get("luas_tanah_bukan_milik");

            }
            if (obj.has("kode_registrasi") && !obj.isNull("kode_registrasi")){
                this.kode_registrasi = (BigInteger) obj.get("kode_registrasi");

            }
            if (obj.has("npwp") && !obj.isNull("npwp")){
                this.npwp = (String) obj.get("npwp");

            }
            if (obj.has("nm_wp") && !obj.isNull("nm_wp")){
                this.nm_wp = (String) obj.get("nm_wp");

            }
            if (obj.has("flag") && !obj.isNull("flag")){
                this.flag = (String) obj.get("flag");

            }
            if (obj.has("create_date") && !obj.isNull("create_date")){
                this.create_date = (Date) obj.get("create_date");

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
            obj.put("sekolah_id", sekolah_id);
            obj.put("nama", nama);
            obj.put("nama_nomenklatur", nama_nomenklatur);
            obj.put("nss", nss);
            obj.put("npsn", npsn);
            obj.put("bentuk_pendidikan_id", bentuk_pendidikan_id);
            obj.put("alamat_jalan", alamat_jalan);
            obj.put("rt", rt);
            obj.put("rw", rw);
            obj.put("nama_dusun", nama_dusun);
            obj.put("desa_kelurahan", desa_kelurahan);
            obj.put("kode_wilayah", kode_wilayah);
            obj.put("kode_pos", kode_pos);
            obj.put("lintang", lintang);
            obj.put("bujur", bujur);
            obj.put("nomor_telepon", nomor_telepon);
            obj.put("nomor_fax", nomor_fax);
            obj.put("email", email);
            obj.put("website", website);
            obj.put("kebutuhan_khusus_id", kebutuhan_khusus_id);
            obj.put("status_sekolah", status_sekolah);
            obj.put("sk_pendirian_sekolah", sk_pendirian_sekolah);
            obj.put("tanggal_sk_pendirian", tanggal_sk_pendirian);
            obj.put("status_kepemilikan_id", status_kepemilikan_id);
            obj.put("yayasan_id", yayasan_id);
            obj.put("sk_izin_operasional", sk_izin_operasional);
            obj.put("tanggal_sk_izin_operasional", tanggal_sk_izin_operasional);
            obj.put("no_rekening", no_rekening);
            obj.put("nama_bank", nama_bank);
            obj.put("cabang_kcp_unit", cabang_kcp_unit);
            obj.put("rekening_atas_nama", rekening_atas_nama);
            obj.put("mbs", mbs);
            obj.put("luas_tanah_milik", luas_tanah_milik);
            obj.put("luas_tanah_bukan_milik", luas_tanah_bukan_milik);
            obj.put("kode_registrasi", kode_registrasi);
            obj.put("npwp", npwp);
            obj.put("nm_wp", nm_wp);
            obj.put("flag", flag);
            obj.put("create_date", create_date);
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