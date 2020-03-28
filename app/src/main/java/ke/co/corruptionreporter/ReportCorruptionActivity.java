package ke.co.corruptionreporter;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ReportCorruptionActivity extends AppCompatActivity {

    private AppCompatSpinner locationsSpinner;
    private AppCompatSpinner corruptionsSpinner;
    private EditText title;
    private EditText description;
    private Button saveCorruption;

    private Realm realm;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView imageView;
    private String userChoosenTask;
    private static String image_path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_corruption);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm = Realm.getDefaultInstance();

        locationsSpinner = findViewById(R.id.corruption_location);
        corruptionsSpinner = findViewById(R.id.corruption_type);
        title = findViewById(R.id.corruption_title);
        description = findViewById(R.id.description);
        saveCorruption = findViewById(R.id.save);
        imageView = findViewById(R.id.image_captured);

        // Spinner Drop down elements
        List<String> counties = new ArrayList<>();
        counties.add("Select Location");
        counties.add("MOMBASA");
        counties.add("KWALE");
        counties.add("KILIFI");
        counties.add("TANA");
        counties.add("LAMU");
        counties.add("TAITA");
        counties.add("GARISSA");
        counties.add("WAJIR");
        counties.add("MANDERA");
        counties.add("MARSABIT");
        counties.add("ISIOLO");
        counties.add("MERU");
        counties.add("THARAKA-NITHI");
        counties.add("EMBU");
        counties.add("KITUI");
        counties.add("MACHAKOS");
        counties.add("MAKUENI");
        counties.add("NYANDARUA");
        counties.add("NYERI");
        counties.add("KIRINYAGA");
        counties.add("MURANGA");
        counties.add("KIAMBU");
        counties.add("TURKANA");
        counties.add("WEST POKOT");
        counties.add("SAMBURU");
        counties.add("TRANSZOIA");
        counties.add("UASIN GISHU");
        counties.add("ELGEYO");
        counties.add("NANDI");
        counties.add("BARINGO");
        counties.add("LAIKIPIA");
        counties.add("NAKURU");
        counties.add("NAROK");
        counties.add("KAJIADO");
        counties.add("KERICHO");
        counties.add("BOMET");
        counties.add("VIHIGA");
        counties.add("BUNGOMA");
        counties.add("BUSIA");
        counties.add("SIAYA");
        counties.add("KISUMU");
        counties.add("HOMABAY");
        counties.add("MIGORI");
        counties.add("KISII");
        counties.add("NYAMIRA");
        counties.add("NAIROBI");

        // Creating adapter for spinner
        ArrayAdapter<String> locationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, counties);

        // Drop down layout style - list view with radio button
        locationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        locationsSpinner.setAdapter(locationsAdapter);

        locationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> corruptionsList = new ArrayList<>();
        corruptionsList.add("Select Corruption");
        corruptionsList.add("Bribery");
        corruptionsList.add("Cronyism");
        corruptionsList.add("Electoral Fraud");
        corruptionsList.add("Traffic Corruption");
        corruptionsList.add("Nepotism");

        // Creating adapter for spinner
        final ArrayAdapter<String> corruptionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, corruptionsList);

        // Drop down layout style - list view with radio button
        corruptionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        corruptionsSpinner.setAdapter(corruptionsAdapter);

        corruptionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveCorruption.setOnClickListener(v -> {

            String locationValue = locationsSpinner.getSelectedItem().toString();

            String corruptionValue = corruptionsSpinner.getSelectedItem().toString();

            String titleOfCorruption = title.getText().toString().trim();

            String descriptionValue = description.getText().toString().trim();

            if (locationValue.equals("Select Location")){
                setSpinnerError(locationsSpinner);
            }else if (corruptionValue.equals("Select Corruption")){
                setSpinnerError(corruptionsSpinner);
            }else if (titleOfCorruption.equals("")){
                title.setError("Subject field cannot be empty");
            }else if (descriptionValue.equals("")){
                description.setError("Description  field cannot be empty");
            }else{
                saveRecord(locationValue, corruptionValue, titleOfCorruption, descriptionValue);
            }
        });
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReportCorruptionActivity.this);
        builder.setTitle("Grant Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.camera) {
            selectImage();
            return true;
        } return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(ReportCorruptionActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utils.checkPermission(ReportCorruptionActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bm, "Title", null);
                image_path = Uri.parse(path).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imageView.setImageBitmap(bm);
    }

    private void setSpinnerError(AppCompatSpinner spinner){

        TextView errorText = (TextView)spinner.getSelectedView();
        errorText.setError("");
        errorText.setTextColor(Color.RED);
        errorText.setText(R.string.spinner_error_warning);
    }

    private void saveRecord(String location, String corruption, String title, String description){

        RealmResults<Corruption> all = realm.where(Corruption.class).findAll().sort("id", Sort.ASCENDING);

        long lastId;

        final Corruption corr = new Corruption();

        if (all.isEmpty()){

            corr.setId(1);
        }else{
            lastId = all.last().getId();

            corr.setId(lastId + 1);
        }

        corr.setCorruption_type(corruption);
        corr.setLocation(location);
        corr.setTitle(title);
        corr.setDescription(description);
        corr.setImage_path(image_path);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(corr);
            }
        });

        startActivity(new Intent(this, ReportedCorruptionsActivity.class));
        finish();
    }

}
