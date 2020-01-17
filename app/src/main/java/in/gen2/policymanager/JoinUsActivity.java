package in.gen2.policymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
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
import in.gen2.policymanager.Helpers.FileDownloader;

public class JoinUsActivity extends AppCompatActivity {

    private String path;
    @BindView(R.id.documentsListText)
    TextView needDocs;
    @BindView(R.id.btnDownloadSpecimen)
    Button downloadSpecimen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_us);
        ButterKnife.bind(this);

        path = Environment.getExternalStorageDirectory() + File.separator + "LicCardData";
        checkFile();
        needDocs.setText(Html.fromHtml("<ul>\n" +
                "  <li>Fill Form -specimen if Application form</li>" +
                "  <li>One passpost size photo</li>" +
                "  <li>Copy of PAN card" +
                "<li>Copy of Address Proof</li><li>Cancelled cheque</li><li>Copy of LIC Agency letter or Agency status from sales deptt. of the Branch</li>" +
                "  <li>Attend free brief training session to know the details of LIC card." +
                "</ul>"));
    }
private void checkFile(){
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
    public void DownloadForm(View view) {

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
            new DownloadFile().execute("http://www.liccards.co.in/images/SR_Application_Form.pdf", "SR_Application_Form.pdf");
        }


    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> link
            String fileName = strings[1];  // -> pdf name


            String path = Environment.getExternalStorageDirectory() + File.separator + "LicCardData";
            File file = new File(path);
            String fileNameText = file + "/"+fileName;

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
            checkFile();
        }
    }
}