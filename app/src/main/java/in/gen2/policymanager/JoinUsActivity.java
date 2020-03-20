package in.gen2.policymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.gen2.policymanager.Helpers.FileDownloader;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class JoinUsActivity extends AppCompatActivity {
    public static final int RequestPermissionCode = 7;
    private String path;
    @BindView(R.id.documentsListText)
    TextView needDocs;
    @BindView(R.id.btnDownloadSpecimen)
    Button downloadSpecimen;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_us);
        unbinder = ButterKnife.bind(this);
        path = Environment.getExternalStorageDirectory() + File.separator + "LicCardData";
        if (!CheckingPermissionIsEnabledOrNot()) {
            RequestMultiplePermission();
        }
        checkFile();
        needDocs.setText(Html.fromHtml("<ul>\n" +
                "  <li>Fill Form-Specimen of Application form</li>" +
                "  <li>One passpost size photo</li>" +
                "  <li>Copy of PAN card" +
                "<li>Copy of Address Proof</li><li>Cancelled cheque</li><li>Copy of LIC Agency letter or Agency status from sales deptt. of the Branch</li>" +
                "  <li>Attend free brief training session to know the details of LIC card." +
                "</ul>"));
    }

    private void checkFile() {
        File file = new File(path);
        String fileName = file + "/SR_Application_Form.pdf";
        File myFile = new File(fileName);
        if (myFile.exists()) {
            downloadSpecimen.setText("Open Form");
            downloadSpecimen.setBackgroundColor(getResources().getColor(R.color.DownloadColor));
        } else {
            downloadSpecimen.setText("Download Specimen Form");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.btnDownloadSpecimen)
    public void DownloadForm() {
        new DownloadFile().execute("http://www.liccards.co.in/images/SR_Application_Form.pdf", "SR_Application_Form.pdf");
        File file = new File(path);
        String fileName = file + "/SR_Application_Form.pdf";
        File myFile = new File(fileName);
        if (myFile.exists()) {
            Intent pdfIntent = new Intent();
            Uri fileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", myFile);
            pdfIntent.setAction(android.content.Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(fileUri, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(JoinUsActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }

        } else {
            if (!CheckingPermissionIsEnabledOrNot()) {
                RequestMultiplePermission();

            }
            else {
                downloadSpecimen.setEnabled(false);
                downloadSpecimen.setText("please Wait");
                downloadSpecimen.setBackgroundColor(getResources().getColor(R.color.pleaseWaitColor));
                Toast.makeText(this,"Please Wait!!",Toast.LENGTH_SHORT).show();
                new DownloadFile().execute("http://www.liccards.co.in/images/SR_Application_Form.pdf", "SR_Application_Form.pdf");
            }

        }


    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {


            String fileUrl = strings[0];   // -> link
            String fileName = strings[1];  // -> pdf name


            String path = Environment.getExternalStorageDirectory() + File.separator + "LicCardData";
            File file = new File(path);
            String fileNameText = file + "/" + fileName;

            if (!file.exists()) {
                file.mkdir();
            }
            if (!new File(fileNameText).exists()) {
                File pdfFile = new File(fileNameText);
                FileDownloader.downloadFile(fileUrl, pdfFile);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            downloadSpecimen.setEnabled(true);
            Toast.makeText(JoinUsActivity.this,"Download successfully!",Toast.LENGTH_SHORT).show();
            checkFile();

        }
    }

    //Permission function starts from here
    private void RequestMultiplePermission() {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(JoinUsActivity.this, new String[]
                {

                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE
                }, RequestPermissionCode);

    }

    // Calling override method.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {


                    boolean WriteExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (WriteExternalStorage && ReadExternalStorage) {

                        Toast.makeText(JoinUsActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(JoinUsActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    // Checking permission is enabled or not using function starts from here.
    public boolean CheckingPermissionIsEnabledOrNot() {
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                        ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

}