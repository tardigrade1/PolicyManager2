package in.gen2.policymanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.opencsv.CSVWriter;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.gen2.policymanager.models.ApplicationFormData;
import in.gen2.policymanager.models.ApproveDeclineData;
import in.gen2.policymanager.models.EmpData;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class ImportEmployeesActivity extends AppCompatActivity {
    String TAG = "main";

    private ArrayList<ApplicationFormData> list_application_form = new ArrayList();


    public static final int RequestPermissionCode = 7;

    //    firestore
    FirebaseFirestore docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_employees);
        if (!CheckingPermissionIsEnabledOrNot()) {
            RequestMultiplePermission();
        }

        FileControl();
    }

    private void FileControl() {


        docRef = FirebaseFirestore.getInstance();
        Button btnSRDataSubmit=findViewById(R.id.btnSrData);

        btnSRDataSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new uploadSRData().execute();
            }
        });

    }



    //Permission function starts from here
    private void RequestMultiplePermission() {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(ImportEmployeesActivity.this, new String[]
                {
                        SEND_SMS,
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

                    boolean SmsPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (SmsPermission && WriteExternalStorage && ReadExternalStorage) {

                        Toast.makeText(ImportEmployeesActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ImportEmployeesActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    // Checking permission is enabled or not using function starts from here.
    public boolean CheckingPermissionIsEnabledOrNot() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;
    }
    class uploadSRData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(ImportEmployeesActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setTitle("Please Wait");
            Dialog.setMessage("entering data..");
            Dialog.setIndeterminate(false);
            Dialog.show();
            isInternetOn();

        }

        protected void isInternetOn() {
            ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = conn.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected() == true) {
            } else {
                Dialog.setMessage("please check your internet connection...");
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            if (list_application_form.size() > 0) {
                for (int i = 0; i < list_application_form.size(); i++) {
                    final ApplicationFormData UserData = list_application_form.get(i);
                    final HashMap<String, Object> hm = new HashMap<>();
//                    hm.put("name", UserData.getName());
//                    hm.put("srNo", UserData.getSrNo());
//                    hm.put("doj", UserData.getDoj());
//                    hm.put("branch", UserData.getBranch());
//                    hm.put("residence", UserData.getResidence());
//                    hm.put("mobileNo", UserData.getMobileNo());
//                    hm.put("email", UserData.getEmail());
                    docRef.collection("SalesRepresentatives").document(UserData.getSrNo()).set(hm)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
//                                    Log.d(TAG, "onButtonClick: " + UserData.getName() + " data is successfully submit");
                                    Dialog.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                }
            } else {
                Toast.makeText(ImportEmployeesActivity.this, "No user list found", Toast.LENGTH_SHORT).show();
            }
            return 0;
        }
    }

}
