package com.nufaza.geotagpaud;

import com.nufaza.geotagpaud.model.Geotag;
import com.nufaza.geotagpaud.model.JenisFoto;
import com.nufaza.geotagpaud.model.StatusGeotag;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import androidx.annotation.NonNull;

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)

public class AppDatabase {

    public static final String NAME = "AppDatabase";

    public static final int VERSION = 1;

    @Migration(version = 0, database = AppDatabase.class)
    public static class Migration000 extends BaseMigration{

        @Override
        public void migrate(@NonNull DatabaseWrapper database) {

            JenisFoto jenisFoto = new JenisFoto();
            jenisFoto.setJenisFotoId(1);
            jenisFoto.setNamaJenisFoto("Plang Sekolah");
            jenisFoto.setInstruksi("Berdirilah di depan plang sekolah sehingga tampak plang terbaca dan gedung sekolah di belakangnya terlihat");
            jenisFoto.setStatusIsian(1);     // 1 Wajib  2 Opsional
            jenisFoto.save(database);

            jenisFoto = new JenisFoto();
            jenisFoto.setJenisFotoId(2);
            jenisFoto.setNamaJenisFoto("Tampak Depan");
            jenisFoto.setInstruksi("Berdirilah di depan sekolah sehingga sebagian besar (minimal 80%) gedung sekolah terlihat berikut plangnya.");
            jenisFoto.setStatusIsian(1);     // 1 Wajib  2 Opsional
            jenisFoto.save(database);

            jenisFoto = new JenisFoto();
            jenisFoto.setJenisFotoId(3);
            jenisFoto.setNamaJenisFoto("Aktiﬁtas Peserta Didik");
            jenisFoto.setInstruksi("Ambillah foto saat ada aktifitas peserta didik, jangan lupa beri judul.");
            jenisFoto.setStatusIsian(2);     // 1 Wajib  2 Opsional
            jenisFoto.save(database);

            jenisFoto = new JenisFoto();
            jenisFoto.setJenisFotoId(4);
            jenisFoto.setNamaJenisFoto("Aktiﬁtas PTK");
            jenisFoto.setInstruksi("Ambillah foto saat ada aktifitas Pendidik/Tenaga Kependidikan, jangan lupa beri judul.");
            jenisFoto.setStatusIsian(2);     // 1 Wajib  2 Opsional
            jenisFoto.save(database);

            jenisFoto = new JenisFoto();
            jenisFoto.setJenisFotoId(5);
            jenisFoto.setNamaJenisFoto("Prestasi Sekolah");
            jenisFoto.setInstruksi("Ambillah foto saat penyerahan penghargaan atas prestasi sekolah.");
            jenisFoto.setStatusIsian(2);     // 1 Wajib  2 Opsional
            jenisFoto.save(database);

            jenisFoto = new JenisFoto();
            jenisFoto.setJenisFotoId(6);
            jenisFoto.setNamaJenisFoto("Lomba Sekolah");
            jenisFoto.setInstruksi("Ambillah foto saat kegiatan lomba di sekolah.");
            jenisFoto.setStatusIsian(2);     // 1 Wajib  2 Opsional
            jenisFoto.save(database);

            jenisFoto = new JenisFoto();
            jenisFoto.setJenisFotoId(7);
            jenisFoto.setNamaJenisFoto("Program Pembangunan");
            jenisFoto.setInstruksi("Ambillah foto saat program pembangunan. Jelaskan pada judul.");
            jenisFoto.setStatusIsian(2);     // 1 Wajib  2 Opsional
            jenisFoto.save(database);

            jenisFoto = new JenisFoto();
            jenisFoto.setJenisFotoId(8);
            jenisFoto.setNamaJenisFoto("Foto Operator");
            jenisFoto.setInstruksi("Ambillah Pas Foto Operator");
            jenisFoto.setStatusIsian(2);
            jenisFoto.save(database);

            StatusGeotag statusGeotag = new StatusGeotag();
            statusGeotag.setStatusGeotagId(1);
            statusGeotag.setNamaStatusGeotag("Geotag Lama");
            statusGeotag.save(database);

            statusGeotag = new StatusGeotag();
            statusGeotag.setStatusGeotagId(2);
            statusGeotag.setNamaStatusGeotag("Geotag Baru Koreksi");
            statusGeotag.save(database);

            statusGeotag = new StatusGeotag();
            statusGeotag.setStatusGeotagId(3);
            statusGeotag.setNamaStatusGeotag("Geotag Baru Perpindahan Posisi Sekolah");
            statusGeotag.save(database);

        }
    }
}
