package com.nufaza.geotagpaud.ui.gallery;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.samples.zoomable.ZoomableDraweeView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nufaza.geotagpaud.App;
import com.nufaza.geotagpaud.MainActivity;
import com.nufaza.geotagpaud.R;
import com.nufaza.geotagpaud.model.Foto;
import com.nufaza.geotagpaud.model.Foto_Table;
import com.nufaza.geotagpaud.model.JenisFoto;
import com.nufaza.geotagpaud.model.JenisFoto_Table;
import com.nufaza.geotagpaud.util.PermissionUtils;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class GalleryFragment extends Fragment {

    private static final int FOTO_REQUEST_CODE = 1;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 313;
    private static final int CAMERA_REQUEST_CODE = 354;

    private GalleryViewModel galleryViewModel;
    private View root;
    private MainActivity mainActivity;
    private FloatingActionButton fab;


    // References
    private List<Integer> jenisFotoValues;
    private List<String> jenisFotoLabels;
    private List<JenisFoto> jenisFotoList;
    private List<Foto> listFoto = null;
    private HashMap<String, String> listFotoObyek = new HashMap<>();

    // Image things
    private String imageId = "";
    private String imageFileName;
    private App app;
    private int imageSize = 0;
    private int imageHeight = 0;
    private int imageWidth = 0;
    private float imageLintang = 0;
    private float imageBujur = 0;

    // Auth vars
    private String sekolahId;
    private String penggunaId;

    // Path vars
    private String thumbnailPath;
    private String tempPath;
    private String imagesPath;
    private String jsonPath;

    private boolean mStoragePermissionDenied = false;
    private boolean mCameraPermissionDenied = false;
    private Integer jenisFotoId;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mainActivity = (MainActivity) getActivity();

//        final TextView textView = root.findViewById(R.id.text_gallery);
//        galleryViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        prepareAuth();
        prepareReferences();
        prepareFab();
        preparePaths();
        return root;
    }

    private void prepareAuth() {
        sekolahId = mainActivity.getPreference(MainActivity.SPKEY_SEKOLAH_ID);
        penggunaId = mainActivity.getPreference(MainActivity.SPKEY_PENGGUNA_ID);
    }

    private void preparePaths() {


        // Thumbnails
        thumbnailPath = mainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/" + MainActivity.THUMBNAIL_FOLDER + "/";
        // Temp Memory
        tempPath = mainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/" + MainActivity.TEMP_IMAGE_FOLDER + "/";
        // External Memory
        imagesPath = mainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/" + MainActivity.EXTERNAL_IMAGE_FOLDER + "/";
        // JSON Path
        jsonPath = mainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/" + MainActivity.JSON_FOLDER + "/";

        File f = new File(thumbnailPath);
        if (!f.exists()) {
            if (!f.mkdirs()) {
                System.out.println("Fail to create thumbnail path");
            } else {
                System.out.println("Thumbnail path created");
            }
        }

        f = new File(tempPath);
        if (!f.exists()) {
            if (!f.mkdirs()) {
                System.out.println("Fail to create temp path");
            } else {
                System.out.println("Temp path created");
            }
        }

        f = new File(imagesPath);
        if (!f.exists()) {
            if (!f.mkdirs()) {
                System.out.println("Fail to create imagesPath");
            } else {
                System.out.println("ImagesPath created");
            }
        }

        f = new File(jsonPath);
        if (!f.exists()) {
            if (!f.mkdirs()) {
                System.out.println("Fail to create jsonPath");
            } else {
                System.out.println("JsonPath created");
            }
        }
    }

    private void prepareReferences() {

        // Isi referensi untuk dropdown
        jenisFotoList = SQLite.select().from(JenisFoto.class).queryList();
        jenisFotoValues = new ArrayList<>();
        jenisFotoLabels = new ArrayList<>();

        if (jenisFotoList.size() > 0) {
            for (int i = 0; i < jenisFotoList.size(); i++) {
                jenisFotoValues.add(jenisFotoList.get(i).getJenisFotoId());
                jenisFotoLabels.add(jenisFotoList.get(i).getNamaJenisFoto());
            }
        }
    }

    private void prepareFab() {

        // Here, thisActivity is the current activity
        // Check write permission
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(mainActivity, WRITE_EXTERNAL_STORAGE_REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
        }
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(mainActivity, CAMERA_REQUEST_CODE, Manifest.permission.CAMERA, true);
        }

        fab = root.findViewById(R.id.fabTambahFoto);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final MaterialDialog fotoDialog = new MaterialDialog.Builder(mainActivity)
                        .title("Menyimpan Lokasi")
                        .customView(R.layout.form_foto, true)
                        .positiveText("Ambil Foto")
                        .negativeText("Batal")
                        .autoDismiss(true)
                        .show();

                View dialogView = fotoDialog.getCustomView();

                // Isi dropdown dengan data
                final Spinner inputJenisFotoId = dialogView.findViewById(R.id.jenis_foto_id);
                ArrayAdapter<String> jenisFotoDataAdapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_spinner_item, jenisFotoLabels);
                jenisFotoDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                inputJenisFotoId.setAdapter(jenisFotoDataAdapter);

                // Persiapan EditText
                final EditText inputNamaObyek = view.findViewById(R.id.nama_obyek);

                fotoDialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        jenisFotoId = jenisFotoValues.get(inputJenisFotoId.getSelectedItemPosition());

                        dispatchTakePictureIntent();
                        fotoDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                        Manifest.permission.CAMERA)) {
                } else {
                    new MaterialDialog.Builder(mainActivity)
                            .title("Permisi Ditolak")
                            .content("Aplikasi tidak diperbolehkan mengambil gambar.")
                            .positiveText("OK")
                            .autoDismiss(true)
                            .show();

                    // Display the missing permission error dialog when the fragments resume.
                    mCameraPermissionDenied = true;
                }
                break;
            case WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                    new MaterialDialog.Builder(mainActivity)
                            .title("Permisi Ditolak")
                            .content("Aplikasi tidak diperbolehkan menulis ke storage.")
                            .positiveText("OK")
                            .autoDismiss(true)
                            .show();
                    // Display the missing permission error dialog when the fragments resume.
                    mStoragePermissionDenied = true;
                }
                break;
        }
    }

    /**
     * Method ini untuk menjalankan fungsi pengambilan gambar oleh aplikasi default kamera.
     * Di sini digunakan MediaStore.EXTRA_OUTPUT, sehingga logikanya sbb:
     * - Dibuat dulu imageFile kosong, caranya memanggil method createImageFile()
     * - Tendang activity untuk picture intent
     * - Setelah camera selesai, gambar langsung tersimpan di output file,
     * tapi nama filenya bertambah panjang sedikit (belum diketahui kenapa).
     * - Method <pre>onActivityResult</pre> membantu pemanggilan penyimpanan thumbnail dan database
     */
    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mainActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("dispatchTakePicture", ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Toast.makeText(mainActivity.getApplicationContext(), "Writing to: " + photoFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                Uri photoURI = FileProvider.getUriForFile(mainActivity, mainActivity.getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, FOTO_REQUEST_CODE);
            }
        }

    }


    private void dispatchRetakePictureIntent(String fotoId) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mainActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;

            try {
                photoFile = retakeImageFile(fotoId);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("dispatchRetakePicture", ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Toast.makeText(mainActivity.getApplicationContext(), "Writing to: " + photoFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                Uri photoURI = FileProvider.getUriForFile(mainActivity, mainActivity.getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, FOTO_REQUEST_CODE);
            }
        }
    }

    /**
     * Method yang digunakan untuk membuat folder storage di external storage
     * untuk kemudian membuat file kosongan untuk menangkap hasil kamera.
     * Penyimpanan dipisah per sekolah_id.
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // imageId = "sekolah_" + timeStamp;

        // Major change: set imageId with UUID
            imageId = UUID.randomUUID().toString();


        // Compose storage path, create the folders if it's not exist yet
        String storagePath = tempPath + sekolahId;
        File storageDir = new File(storagePath);
        if (!storageDir.isDirectory()) {
            storageDir.mkdirs();
        }

        // Debugging purposes
        // Toast.makeText(getApplicationContext(), "Storage dir: " + storageDir.toString(), Toast.LENGTH_LONG).show();

        // Create destination file
        File image = File.createTempFile(
                imageId,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        // mCurrentPhotoPath = "file:" + image.getAbsolutePath();

        //Debugging purposes
        //Toast.makeText(getApplicationContext(), "Image path: " + mCurrentPhotoPath, Toast.LENGTH_LONG).show();

        return image;
    }

    private File retakeImageFile(String fotoId) throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // imageId = "sekolah_" + timeStamp;

        // Major change: set imageId with UUID

            imageId = fotoId;

        // Compose storage path, create the folders if it's not exist yet
        String storagePath = tempPath + sekolahId;
        File storageDir = new File(storagePath);
        if (!storageDir.isDirectory()) {
            storageDir.mkdirs();
        }

        // Debugging purposes
        // Toast.makeText(getApplicationContext(), "Storage dir: " + storageDir.toString(), Toast.LENGTH_LONG).show();

        // Create destination file
        File image = File.createTempFile(
                imageId,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        // mCurrentPhotoPath = "file:" + image.getAbsolutePath();

        //Debugging purposes
        //Toast.makeText(getApplicationContext(), "Image path: " + mCurrentPhotoPath, Toast.LENGTH_LONG).show();

        return image;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == FOTO_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                String message = "";

                try {

                    validatePhoto();

                    saveThumbnailToInternalStorage();
                    message += "Menyimpan thumbnail berhasil. ";

                    resizeImage();
                    message += "Mengubah ukuran image berhasil. ";

                    attachImageToDatabase();
                    message += "Menyimpan data image ke database berhasil. ";

                    Toast.makeText(mainActivity.getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    populateListFoto();
                    populateListViewFoto();

                    ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    imagePipeline.clearMemoryCaches();
                    imagePipeline.clearDiskCaches();
                    imagePipeline.clearCaches();

                } catch (Exception e) {
                    message += "Terjadi error: " + e.getMessage();
                    Toast.makeText(mainActivity.getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }

                Toast.makeText(mainActivity.getApplicationContext(), message, Toast.LENGTH_LONG).show();

                // Update
                // updateBannerImage();
            }
        }
    }

    private boolean validatePhoto() throws Exception {

        // Get External Storage
        String storagePath = tempPath + sekolahId;

        // Find the image
        imageFileName = findFileBeginsWith(imageId);

        // Load the image, with options set
        File oldImagePath = new File(storagePath + "/" + imageFileName);
        File imagePath = new File(storagePath + "/" + imageId + ".jpg");
        oldImagePath.renameTo(imagePath);

        // Load the image
        final Bitmap source = BitmapFactory.decodeFile(imagePath.toString());

        // Validate orientation
        imageHeight = source.getHeight();
        imageWidth = source.getWidth();

        if (imageHeight > imageWidth) {
            throw new Exception("Salah orientasi foto. Ambillah foto dengan posisi landscape, jangan potrait.");
        }

        // Validate position
        ExifInterface exif = new ExifInterface(imagePath.toString());
        float[] latLong = new float[2];
        boolean hasLatLong = exif.getLatLong(latLong);


        if (hasLatLong) {
            imageLintang = latLong[0];
            imageBujur = latLong[1];
        } else {
            Toast.makeText(mainActivity.getApplicationContext(), "WARNING: Latitude/longitude tidak diset.", Toast.LENGTH_LONG).show();
        }

        // Validate ...
        return true;
    }


    /**
     * Method bantu untuk mencari nama file picture yang tersimpan di sdcard
     *
     * @param str
     * @return
     */
    private String findFileBeginsWith(String str) {

        String storagePath = tempPath + sekolahId;
        File path = new File(storagePath);
        File files[] = path.listFiles();
        String foundFileName = "";

        for (int i = 0; i < files.length; i++) {

            int strLength = str.length();
            String fileName = files[i].getName();

            // Mysterious bug for million years ago finally found!
            if (fileName.length() < strLength) {
                continue;
            }

            String beginningFileName = fileName.substring(0, strLength);

            // if (Objects.equals(beginningFileName, str)) {
            if (beginningFileName.equals(str)) {
                foundFileName = fileName;
                break;
            }
        }
        return foundFileName;
    }

    /**
     * Menyimpan thumbnail ke internal storage
     *
     * @return boolean berhasil atau tidaknya fungsi
     */
    private void saveThumbnailToInternalStorage() throws Exception {

        // Get External Storage
        String storagePath = tempPath + sekolahId;

        // Find the image
        imageFileName = findFileBeginsWith(imageId);

        // Load the image, with options set
        File imagePath = new File(storagePath + "/" + imageFileName);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outHeight = 267;
        options.outWidth = 267;
        options.inSampleSize = 0;

        // Create the thumbnail
        final Bitmap source = BitmapFactory.decodeFile(imagePath.toString(), options);
        Bitmap thumbnailBitmap = ThumbnailUtils.extractThumbnail(source, 267, 267);

        // Get internal memory directory, create if it's not there
        File directory = new File(thumbnailPath + "/" + sekolahId);

        // File directory = new File(thumbnailPath);
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        // Create thumbnailpath
        File thumbnailpath = new File(directory, imageFileName);

        // Use the compress method on the Bitmap object to write thumbnail to
        // the OutputStream
        FileOutputStream fos = new FileOutputStream(thumbnailpath);

        // Writing the bitmap to the output stream
        thumbnailBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();

        // Log.e("saveToInternalStorage()", e.getMessage());
        // Log.e("saveToInternalStorage", e.toString());

    }

    public static void copyExif(String oldPath, String newPath) throws IOException {
        ExifInterface oldExif = new ExifInterface(oldPath);

        String[] attributes = new String[]
                {
                        ExifInterface.TAG_APERTURE,
                        ExifInterface.TAG_DATETIME,
                        ExifInterface.TAG_DATETIME_DIGITIZED,
                        ExifInterface.TAG_EXPOSURE_TIME,
                        ExifInterface.TAG_FLASH,
                        ExifInterface.TAG_FOCAL_LENGTH,
                        ExifInterface.TAG_GPS_ALTITUDE,
                        ExifInterface.TAG_GPS_ALTITUDE_REF,
                        ExifInterface.TAG_GPS_DATESTAMP,
                        ExifInterface.TAG_GPS_LATITUDE,
                        ExifInterface.TAG_GPS_LATITUDE_REF,
                        ExifInterface.TAG_GPS_LONGITUDE,
                        ExifInterface.TAG_GPS_LONGITUDE_REF,
                        ExifInterface.TAG_GPS_PROCESSING_METHOD,
                        ExifInterface.TAG_GPS_TIMESTAMP,
                        ExifInterface.TAG_IMAGE_LENGTH,
                        ExifInterface.TAG_IMAGE_WIDTH,
                        ExifInterface.TAG_ISO,
                        ExifInterface.TAG_MAKE,
                        ExifInterface.TAG_MODEL,
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.TAG_SUBSEC_TIME,
                        ExifInterface.TAG_SUBSEC_TIME_DIG,
                        ExifInterface.TAG_SUBSEC_TIME_ORIG,
                        ExifInterface.TAG_WHITE_BALANCE
                };

        ExifInterface newExif = new ExifInterface(newPath);
        for (int i = 0; i < attributes.length; i++) {
            String value = oldExif.getAttribute(attributes[i]);
            if (value != null)
                newExif.setAttribute(attributes[i], value);
        }
        newExif.saveAttributes();
    }


    /**
     * Resize, crop, dan menyimpan image ke posisi final storage
     *
     * @return
     */
    private void resizeImage() throws Exception {

        // Get External Storage
        String storagePath = tempPath + sekolahId;

        // Find the image
        imageFileName = findFileBeginsWith(imageId);

        // Load the image, with options set
        File imagePath = new File(storagePath + "/" + imageFileName);

        // Load the image
        final Bitmap source = BitmapFactory.decodeFile(imagePath.toString());
        Bitmap dstBmp = null;

        // NEW: limit by height: 1080p
        // Make it smaller first
        // public static Bitmap createScaledBitmap (Bitmap src, int dstWidth, int dstHeight, boolean filter)
        //dstBmp = Bitmap.createScaledBitmap(
        //        source,
        //        1920,
        //        Math.round(1920 * source.getHeight() / source.getWidth()),
        //        true
        //);

        Integer height = 1080;
        Integer width = Math.round(1080 * source.getWidth() / source.getHeight());
        dstBmp = Bitmap.createScaledBitmap(
                source,
                width,
                height,
                true
        );

        // NEW: Skip cropping. It's bad.
        // Then crop it
        // public static Bitmap createBitmap (Bitmap source, int x, int y, int width, int height)
        //dstBmp = Bitmap.createBitmap(
        //        dstBmp,
        //        0,
        //        (dstBmp.getHeight()-1080)/2,
        //        1920,
        //        1080
        //);
        dstBmp = Bitmap.createBitmap(
                dstBmp,
                0,
                0,
                width,
                height
        );

        imageSize = dstBmp.getByteCount();
        imageHeight = dstBmp.getHeight();
        imageWidth = dstBmp.getWidth();

        // Get external memory directory, create if it's not there
        File directory = new File(imagesPath + "/" + sekolahId);

        // File directory = new File(thumbnailPath);
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        File imagefilepath = new File(directory, imageFileName);

        // try {
        // Use the compress method on the Bitmap object to write thumbnail to
        // the OutputStream
        FileOutputStream fos = new FileOutputStream(imagefilepath);

        // Writing the bitmap to the output stream
        dstBmp.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        fos.flush();
        fos.close();

        imageSize = (int) imagefilepath.length();

        // Normalize EXIF shit
        copyExif(imagePath.toString(), imagefilepath.toString());

    }

    /**
     * Menyimpan kolom nama file ke database
     *
     * @return boolean berhasil atau tidak
     */

    private void attachImageToDatabase() throws Exception {

        // Also major change: insert to foto table along with its shit

        Foto foto = null;
        UUID fotoId = UUID.fromString(imageId);
        foto = SQLite.select().from(Foto.class).where(Foto_Table.foto_id.eq(fotoId)).querySingle();

        if (foto == null){
            foto = new Foto();
        }

        foto.setFotoId(UUID.fromString(imageId));
        foto.setJenisFotoId(jenisFotoId);
        foto.setTglPengambilan(new Date());
        foto.setUkuran(imageSize);
        foto.setTinggiPixel(imageHeight);
        foto.setLebarPixel(imageWidth);
        foto.setLintang(String.valueOf(imageLintang));
        foto.setBujur(String.valueOf(imageBujur));
        foto.setPenggunaId(UUID.fromString(penggunaId));
        foto.setSekolahId(UUID.fromString(sekolahId));
        foto.save();

    }

    /**
     * Avoid loss of data during Intent processing
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("sekolahId", sekolahId);
        savedInstanceState.putString("penggunaId", penggunaId);
        savedInstanceState.putInt("jenisFotoId", jenisFotoId);
        savedInstanceState.putString("imageId", imageId);
        savedInstanceState.putString("imageFileName", imageFileName);
        savedInstanceState.putString("thumbnailPath", thumbnailPath);
        savedInstanceState.putString("tempPath", tempPath);
        savedInstanceState.putString("imagesPath", imagesPath);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            sekolahId = savedInstanceState.getString("sekolahId");
            penggunaId = savedInstanceState.getString("penggunaId");
            jenisFotoId = savedInstanceState.getInt("jenisFotoId");
            imageId = savedInstanceState.getString("imageId");
            imageFileName = savedInstanceState.getString("imageFileName");
            thumbnailPath = savedInstanceState.getString("thumbnailPath");
            tempPath = savedInstanceState.getString("tempPath");
            imagesPath = savedInstanceState.getString("imagesPath");
        }
    }

    /**
     * Method ini untuk mengisi data listFoto untuk listViewFoto di data pengamatan umum
     */
    private void populateListFoto() {
        listFoto = SQLite.select()
                .from(Foto.class)
                .where(Foto_Table.status_data.greaterThanOrEq(1))
                .queryList();

        if (listFoto != null) {
            for (int i = 0; i < listFoto.size(); i++) {
                listFotoObyek.put(listFoto.get(i).getFotoId().toString(), listFoto.get(i).getFotoId() + ".jpg");
            }
        }
    }

    /**
     * Method ini untuk mengisi tampilan listViewFoto di data pengamatan umum
     */
    private void populateListViewFoto() {

        // Buat adapter
        ArrayAdapter<Foto> adapter = new FotoListAdapter();

        // Panggil listviewnya
        final ListView listViewFoto = (ListView) root.findViewById(R.id.listview_foto);
        listViewFoto.setAdapter(adapter);
        listViewFoto.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        listViewFoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Foto selectedFoto = listFoto.get(position);
                openViewImageIntent(selectedFoto);
            }
        });
        // listViewFoto.setOnTouchListener(new View.OnTouchListener() {
        //     @Override
        //     public boolean onTouch(View v, MotionEvent event) {
        //         v.getParent().requestDisallowInterceptTouchEvent(true);
        //         return false;
        //     }
        // });

    }


    public void openViewImageIntent(final Foto currentFoto) {

        String fotoUri = listFotoObyek.get(currentFoto.getFotoId().toString());

        // Intent imageViewIntent = new Intent(Intent.ACTION_VIEW);
        // imageViewIntent.setDataAndType(Uri.parse("file://" + imagesPath + sekolahId + "/" + fotoUri), "image/*");
        File imageFile = new File(imagesPath + sekolahId + "/" + fotoUri);
        Uri photoURI = FileProvider.getUriForFile(mainActivity, mainActivity.getApplicationContext().getPackageName() + ".provider", imageFile);
        // imageViewIntent.setDataAndType(photoURI, "image/*");
        // startActivity(imageViewIntent);

        final MaterialDialog fotoDialog = new MaterialDialog.Builder(mainActivity)
                .title("Foto")
                .customView(R.layout.fresco_viewer, true)
                .positiveText("OK")
                .neutralText("Delete")
                .negativeText("Retake")
                .autoDismiss(true)
                .show();

        View dialogView = fotoDialog.getCustomView();
        ZoomableDraweeView imageViewer = null;

        fotoDialog.getActionButton(DialogAction.NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity().getApplicationContext(),"Anda memilih Retake",Toast.LENGTH_LONG).show();
                jenisFotoId = currentFoto.getJenisFotoId();
                String fotoId =  currentFoto.getFotoId().toString();

                dispatchRetakePictureIntent(fotoId);
                fotoDialog.dismiss();
                populateListFoto();
                populateListViewFoto();
            }
        });


        if (dialogView != null) {

            imageViewer = dialogView.findViewById(R.id.simple_drawee_view);

            DraweeController ctrl = Fresco.newDraweeControllerBuilder()
                    .setUri(photoURI)
                    .setTapToRetryEnabled(true)
                    .build();

            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                    .setProgressBarImage(new ProgressBarDrawable())
                    .build();

            imageViewer.setController(ctrl);
            imageViewer.setHierarchy(hierarchy);

            // imageViewer.setImageURI(photoURI);
        }
    }


    private class FotoListAdapter extends ArrayAdapter<Foto> {

        public FotoListAdapter() {
            super(mainActivity, R.layout.gallery_item, listFoto);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Pastikan ada view kalau null
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.gallery_item, parent, false);
            }

            // Find the item
            Foto currentFoto = listFoto.get(position);
            int jenisFotoId = currentFoto.getJenisFotoId();

            // jenisFotoList contains list of jenisFotos.
            // But beware, if using "get", you'll get position with 0 as first index.
            // So, add - 1
            // Correct: we're using hashmap now, so it shouldn't be a prblem
            int idx = jenisFotoValues.indexOf(jenisFotoId);
            JenisFoto jenisFotoObj = jenisFotoList.get(idx);
            String jenisFotoTitle = jenisFotoObj.getNamaJenisFoto();

            // Attach strings
            TextView namaFotoText = (TextView) itemView.findViewById(R.id.item_judul_foto);
            namaFotoText.setText(jenisFotoTitle); // Ganti ini

            TextView jenisFotoText = (TextView) itemView.findViewById(R.id.item_jenis_foto);
            jenisFotoText.setText(jenisFotoTitle); // Ganti ini

            Date tglFoto = currentFoto.getTglPengambilan();
            String tglFotoStr;

            if (tglFoto == null) {
                tglFotoStr = "-";
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:ss", Locale.getDefault());
                tglFotoStr = sdf.format(tglFoto);
            }
            ((TextView) itemView.findViewById(R.id.item_tgl_foto)).setText(tglFotoStr);


            // Attach foto
            File directory = new File(thumbnailPath + "/" + sekolahId);
            File imagePath = new File(directory.toString() + "/" + currentFoto.foto_id + ".jpg");

            // Render if it's found

            if (imagePath.isFile()) {

                // Convert to bitmap
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.outHeight = 60;
                options.outWidth = 60;
                final Bitmap thumbnailBitmap = BitmapFactory.decodeFile(imagePath.toString(), options);

                // Attach to view
                ImageView fotoFotoView = (ImageView) itemView.findViewById(R.id.foto_obyek);
                fotoFotoView.setImageBitmap(thumbnailBitmap);

                // Create listener for the ImageView (fotoSekolahView)
                // final String sekolahId = sekolahId;
                // final String namaSekolah = currentSekolah.nama_sekolah;
                final String fotoUri = currentFoto.foto_id + ".jpg";
                final File imagePathUri = imagePath;

                fotoFotoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "Anda memencet image "+ namaImage + " ..", Toast.LENGTH_LONG).show();

                        if (imagePathUri.isFile()) {

                            // Intent imageViewIntent = new Intent(Intent.ACTION_VIEW);
                            // File imageFile = new File(imagesPath + sekolahId + "/" + fotoUri);
                            // Uri photoURI = FileProvider.getUriForFile(mainActivity, mainActivity.getApplicationContext().getPackageName() + ".provider", imageFile);
                            //
                            // imageViewIntent.setDataAndType(photoURI, "image/*");
                            // // imageViewIntent.setDataAndType(Uri.parse("file://" + imagesPath + sekolahId + "/" + fotoUri), "image/*");
                            // startActivity(imageViewIntent);

                        }
                    }
                });

            } else {
                ImageView fotoFotoView = (ImageView) itemView.findViewById(R.id.foto_obyek);
                fotoFotoView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_image));
            }

            // Return the view
            return itemView;
        }

    }

    @Override
    public void onResume() {
        populateListFoto();
        populateListViewFoto();

        super.onResume();
    }

}


