package in.gen2.policymanager.Fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.gen2.policymanager.Helpers.FileDownloader;
import in.gen2.policymanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LicPremiumOnlinePaymentFragment extends Fragment {


    private View view;
    private Unbinder unbinder;
    @BindView(R.id.AutoPaymentStepText)
    TextView autoPaymentText;
    @BindView(R.id.tvLicIndiaLink)
            TextView licIndiaLink;
    @BindView(R.id.btnFormDownload)
    Button downloadFile;
    String fileUrl="http://www.insuranceportal.in/download/lic-premium-payment-form.pdf";
    String fileNameText="Lic_premium_paymentRegister_form.pdf";
    private String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Online premium payment");
        view = inflater.inflate(R.layout.fragment_lic_premium_online_payment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        unbinder = ButterKnife.bind(this,view);
        licIndiaLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://licindia.in/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        path = Environment.getExternalStorageDirectory() + File.separator + "LicCardData";
        checkFile();
        String licLinkColor="<span style=color:#006A85>liccards@axisbank.com</span>";
        autoPaymentText.setText(Html.fromHtml("<ul>" +
                "<li>Premium can be auto paid by card directly.</li>" +
                "<li>This payment is made after 25 days from due date.</li>" +
                "<li>Applicant has to send LIC Premium Registration form to "+licLinkColor+" from his registered email.</li>" +
                "<li>Message will be send by bank.</li>" +
                "</ul>"));
    }
public void checkFile(){
    File file = new File(path);
    String fileName = file + "/"+fileNameText;
    File myFile = new File(fileName);
    if (myFile.exists()) {
        downloadFile.setText("Open Form");
        downloadFile.setBackgroundColor(getResources().getColor(R.color.DownloadColor));
    } else {
        downloadFile.setText("Download Form");
    }
}
    @OnClick(R.id.btnFormDownload)
    public void  onAutoSubmittionfprmDownloadClick(){
        File file = new File(path);
        String fileName = file + "/"+fileNameText;
        File myFile = new File(fileName);
        if (myFile.exists()) {
            Intent pdfIntent = new Intent();
            Uri fileUri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", myFile);
            pdfIntent.setAction(android.content.Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(fileUri, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getContext(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }

        } else {
            new DownloadFile().execute(fileUrl, fileNameText);
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
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
