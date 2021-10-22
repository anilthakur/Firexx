package com.example.fireex;

import static com.example.fireex.util.Constants.CAMERA_IMAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fireex.adapter.ImageAdapter;
import com.example.fireex.util.DataProcessor;
import com.google.android.material.textfield.TextInputLayout;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class FormActivity extends AppCompatActivity {
    DBHelper dbHelper;
    TextView saveButton, viewButton;
    TextInputLayout slNo, capacity;
    Spinner typeofExtinguisher, exteriorType, paintData, identificationData, operatingData,
            capData, dischargeData, controlData, washerData, plungerData,
            interiorData, intierData, gasData, contentsData, powderData, safetyData,
            carryingData, inspectionData;
    ImageButton captureImage;
    public static final int REEQUEST_CODE_VIEW_PDF = 120;
    private final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    File mPhotoFile;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklist);
        dbHelper = new DBHelper(this);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Fire control Panel");
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> finish());
        recyclerView = findViewById(R.id.list);
        captureImage = findViewById(R.id.capture_image);
        saveButton = findViewById(R.id.submitButton);
        viewButton = findViewById(R.id.viewButton);
        capacity = findViewById(R.id.textCapacityInput);
        slNo = findViewById(R.id.slNum);
        typeofExtinguisher = findViewById(R.id.TypeofExtinguisherSpinner);
        exteriorType = findViewById(R.id.ExteriorSpinner);
        paintData = findViewById(R.id.PaintSpinner);
        identificationData = findViewById(R.id.Identification_Spinner);
        operatingData = findViewById(R.id.Operating_Spinner);
        capData = findViewById(R.id.cap_Spinner);
        dischargeData = findViewById(R.id.Discharge_Spinner);
        controlData = findViewById(R.id.Control_Spinner);
        washerData = findViewById(R.id.Washer_Spinner);
        plungerData = findViewById(R.id.Plunger_Spinner);
        interiorData = findViewById(R.id.Interior_Spinner);
        intierData = findViewById(R.id.Intier_Spinner);
        gasData = findViewById(R.id.Gas_Spinner);
        contentsData = findViewById(R.id.contents_Spinner);
        powderData = findViewById(R.id.powder_Spinner);
        safetyData = findViewById(R.id.Safety_Spinner);
        carryingData = findViewById(R.id.Carrying_Spinner);
        inspectionData = findViewById(R.id.Inspection_Spinner);
        File file = createFile();
        saveButton.setOnClickListener(view -> {
            try {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FormActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
                } else {
                    generatePDF(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        viewButton.setOnClickListener(view -> {
            try {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FormActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 103);
                } else {
                    readFile(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        captureImage.setOnClickListener(view -> {
            onCameraClick(view);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DataProcessor.getImage(CAMERA_IMAGE) != null && !DataProcessor.getImage(CAMERA_IMAGE).isEmpty()) {
            ImageAdapter imageAdapter = new ImageAdapter(this, DataProcessor.getImage(CAMERA_IMAGE));
            RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(imageAdapter);
            recyclerView.setNestedScrollingEnabled(false);
        }
    }

    private void generatePDF(File file) throws FileNotFoundException {

        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        pdfDocument.setDefaultPageSize(PageSize.A4);
        document.setMargins(0, 0, 0, 0);

        Paragraph paragraph = new Paragraph("Check List info").setBold().setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER);

        float[] width = {100f, 100f};
        Table table = new Table(width);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd-yyyy-MM HHmm");
        Date getDate = new Date();
        String dateStr = timeStampFormat.format(getDate);

        table.addCell(new Cell().add(new Paragraph("Date")));
        table.addCell(new Cell().add(new Paragraph(dateStr)));

        table.addCell(new Cell().add(new Paragraph("Sl.No")));
        if (slNo.getEditText() != null && slNo.getEditText().getText() != null)
            table.addCell(new Cell().add(new Paragraph(slNo.getEditText().getText().toString())));

        table.addCell(new Cell().add(new Paragraph("Type of Extinguisher")));
        if (typeofExtinguisher != null && typeofExtinguisher.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(typeofExtinguisher.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("Capacity")));
        if (capacity != null)
            table.addCell(new Cell().add(new Paragraph(Objects.requireNonNull(capacity.getEditText()).getText().toString())));

        table.addCell(new Cell().add(new Paragraph("Exterior (No damage/corrosion)")));
        if (exteriorType != null && exteriorType.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(exteriorType.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("Paint in Good condition")));
        if (paintData != null && paintData.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(paintData.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("Identification Number clear")));
        if (identificationData != null && inspectionData.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(identificationData.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("Operating instruction clear")));
        if (operatingData != null && operatingData.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(operatingData.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("Cap(no cracks,vent hole free,polish)")));
        if (capData != null && capData.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(capData.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("Discharge hose/horn/nozzle")));
        if (dischargeData != null && dischargeData.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(dischargeData.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("Control valve")));
        if (controlData != null && controlData.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(controlData.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("Washer")));
        if (washerData != null && washerData.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(washerData.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("Plunger & piercer free movement")));
        if (plungerData != null && plungerData.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(plungerData.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("Interior - No corrosion,damages")));
        if (interiorData != null && interiorData.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(interiorData.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("Intier discarge tube,siphon tube")));
        if (intierData != null && intierData.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(intierData.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("Gas cartridge-weight,seal")));
        if (gasData != null && gasData.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(gasData.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("contents-weight,liquid level")));
        if (contentsData != null && contentsData.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(contentsData.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("powder(Free following,no cakeing)")));
        if (powderData != null && powderData.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(powderData.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("Safety pin/clip properly fixed")));
        if (safetyData != null && safetyData.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(safetyData.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("Carrying handle squeesing")));
        if (carryingData != null && carryingData.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(carryingData.getSelectedItem().toString())));

        table.addCell(new Cell().add(new Paragraph("Inspection tag in place")));
        if (inspectionData != null && inspectionData.getSelectedItem() != null)
            table.addCell(new Cell().add(new Paragraph(inspectionData.getSelectedItem().toString())));
        document.add(paragraph);
        document.add(table);
        ArrayList<String> images = DataProcessor.getImage(CAMERA_IMAGE);
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                Image image = null;
                try {
                    ImageData data = ImageDataFactory.create(images.get(i));
                    image = new Image(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (image != null) {
                    document.add(image);
                }
            }
        }
        document.close();
        DataProcessor.clearImages(CAMERA_IMAGE);
        Toast.makeText(this, "Pdf Created", Toast.LENGTH_LONG).show();
        dbHelper.deleteAllFireData();
        dbHelper.deleteAllFireExData();
    }

    public void readFile(File file) {
        try {
            Uri uri = FileProvider.getUriForFile(FormActivity.this, BuildConfig.APPLICATION_ID +
                    ".provider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(MimeTypeMap.getFileExtensionFromUrl(file.getPath()));
            intent.setDataAndType(uri, mimeType);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent chooser = Intent.createChooser(intent, "Open with");
            chooser.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(chooser, REEQUEST_CODE_VIEW_PDF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REEQUEST_CODE_VIEW_PDF) {
            Intent intent = new Intent(this, LandingPageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            ArrayList<String> imageList = DataProcessor.getImage(CAMERA_IMAGE);
            if (imageList == null) {
                imageList = new ArrayList<>();
            }
            imageList.add(mPhotoFile.toString());
            DataProcessor.saveImage(imageList, CAMERA_IMAGE);
            Log.d("FILEPATH", "Director" + mPhotoFile);
        }
    }

    public File createFile() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";
        File directory = new File(path);
        if (!directory.exists())
            directory.mkdir();
        File file = new File(directory, "/survey_data.pdf");
        return file;
    }

    public void onSaveClick(View view) {
    }

    public void onCameraClick(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (getFromPref(this, ALLOW_KEY)) {
                showSettingsAlert();
            } else if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean
                            showRationale =
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                    this, permission);

                    if (showRationale) {
                        showAlert();
                    } else {
                        saveToPreferences(this, ALLOW_KEY, true);
                    }
                }

            }
        }
    }


    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
                Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }

    public static void saveToPreferences(Context context, String key, Boolean allowed) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, allowed);
        prefsEditor.apply();
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                (dialog, which) -> {
                    dialog.dismiss();
                    ActivityCompat.requestPermissions(FormActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                });
        alertDialog.show();
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(FormActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                (dialog, which) -> {
                    dialog.dismiss();
                    //finish();
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                (dialog, which) -> {
                    dialog.dismiss();
                    startInstalledAppDetailsActivity(FormActivity.this);
                });

        alertDialog.show();
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }

        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, MY_PERMISSIONS_REQUEST_CAMERA);

            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }
}
