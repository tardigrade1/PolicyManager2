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

    private ArrayList<EmpData> list_show_users = new ArrayList();
    private ArrayList<ApplicationFormData> list_application_form = new ArrayList();
    private ArrayList<ApproveDeclineData> list_application_approve= new ArrayList();
    CsvParserSettings parserSettings;

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
        ReadUsersList();
//        ReadApplicationForm();
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

    //    reading data from exiting CSV file
    private void ReadUsersList() {
        list_show_users.clear();
//        dataUsersAdapter = null;
        String path = Environment.getExternalStorageDirectory()
                + File.separator + "ReadQrData/SRList.csv";
        if (new File(path).exists()) {
            parserSettings = new CsvParserSettings();
            parserSettings.getFormat().setLineSeparator(CSVWriter.DEFAULT_LINE_END);
            parserSettings.selectFields("Date", "SR No", "Name","Branch", "Residence", "Mob No", "Email");
            try {
                for (String[] strArr : parseWithSettings(this.parserSettings, "ReadQrData/SRList.csv")) {

                    this.list_show_users.add(new EmpData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5], strArr[6]));
                    try {
                    } catch (Exception unused) {
                        this.list_show_users.add(new EmpData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5], strArr[6]));
                    }

                }

                if (this.list_show_users.size() > 0) {
                    for (int i = 0; i < list_show_users.size(); i++) {
                        EmpData UserData = list_show_users.get(i);
                        Log.d(TAG, "ReadUsersList: "+i+", " + UserData.getName() + ", " + UserData.getSrNo());
                    }
                    return;
                }
                Toast.makeText(this, "No users found. Please register user", Toast.LENGTH_SHORT).show();
//                this.lvUsers.setAdapter(null);
                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }

    }
    //    reading data from Application forms CSV file
//    private void ReadApplicationForm() {
//        list_application_form.clear();
////        dataUsersAdapter = null;
//        String path = Environment.getExternalStorageDirectory()
//                + File.separator + "ReadQrData/ApplictionFormsList.csv";
//        if (new File(path).exists()) {
//            parserSettings = new CsvParserSettings();
//            parserSettings.getFormat().setLineSeparator(CSVWriter.DEFAULT_LINE_END);
//            parserSettings.selectFields("Application No","Name","Mobile No","PAN No","SR No","Date");
//            try {
//                for (String[] strArr : parseWithSettings(this.parserSettings, "ReadQrData/ApplictionFormsList.csv")) {
//
//                    this.list_application_form.add(new ApplicationFormData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5]));
//                    try {
//                    } catch (Exception unused) {
//                        this.list_application_form.add(new ApplicationFormData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5]));
//                    }
//
//                }
//
//                if (this.list_application_form.size() > 0) {
//                    for (int i = 0; i < list_application_form.size(); i++) {
//                        ApplicationFormData formData = list_application_form.get(i);
//                        Log.d(TAG, "ApplictionFormsList: "+i+", " + formData.getApplicantName() + ", " + formData.getApplicationNo());
//
//                    }
//                    return;
//                }
//                Toast.makeText(this, "No application list found.", Toast.LENGTH_SHORT).show();
////                this.lvUsers.setAdapter(null);
//                return;
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                return;
//            }
//        }
//        this.id = 0;
//    }
    //    reading data from Application forms CSV file
//    private void ReadApprovalDeclineStatus() {
//        list_application_approve.clear();
////        dataUsersAdapter = null;
//        String path = Environment.getExternalStorageDirectory()
//                + File.separator + "ReadQrData/ApprovalDecline.csv";
//        if (new File(path).exists()) {
//            parserSettings = new CsvParserSettings();
//            parserSettings.getFormat().setLineSeparator(CSVWriter.DEFAULT_LINE_END);
//            parserSettings.selectFields("ApplicationNo","Bank","Status","Comment");
//            try {
//                for (String[] strArr : parseWithSettings(this.parserSettings, "ReadQrData/ApprovalDecline.csv")) {
//
//                    this.list_application_approve.add(new ApproveDeclineData(strArr[0], strArr[1], strArr[2], strArr[3]));
//                    try {
//                    } catch (Exception unused) {
//                        this.list_application_approve.add(new ApproveDeclineData(strArr[0], strArr[1], strArr[2], strArr[3]));
//                    }
//
//                }
//                Toast.makeText(this, "No application list found.", Toast.LENGTH_SHORT).show();
////                this.lvUsers.setAdapter(null);
//                return;
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                return;
//            }
//        }
//        this.id = 0;
//    }

    private List<String[]> parseWithSettings(CsvParserSettings csvParserSettings, String str) throws FileNotFoundException {
        RowListProcessor rowListProcessor = new RowListProcessor();
        csvParserSettings.setProcessor(rowListProcessor);
        csvParserSettings.setHeaderExtractionEnabled(true);
        CsvParser csvParser = new CsvParser(csvParserSettings);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory());
        stringBuilder.append(File.separator);
        stringBuilder.append(str);
        csvParser.parse(new FileReader(stringBuilder.toString()));
        return rowListProcessor.getRows();
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

            if (list_show_users.size() > 0) {
                for (int i = 0; i < list_show_users.size(); i++) {
                    final EmpData UserData = list_show_users.get(i);
                    final HashMap<String, Object> hm = new HashMap<>();
                    hm.put("name", UserData.getName());
                    hm.put("srNo", UserData.getSrNo());
                    hm.put("doj", UserData.getDoj());
                    hm.put("branch", UserData.getBranch());
                    hm.put("residence", UserData.getResidence());
                    hm.put("mobileNo", UserData.getMobileNo());
                    hm.put("email", UserData.getEmail());
                    docRef.collection("SalesRepresentatives").document(UserData.getSrNo()).set(hm)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "onButtonClick: " + UserData.getName() + " data is successfully submit");
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

//    private void uploadApplicationDataOnFiresStore() {
//        if (list_application_form.size() > 0) {
//
//            for (int i = 0; i < list_application_form.size(); i++) {
//                final ApplicationFormData formData = list_application_form.get(i);
//                final HashMap<String, Object> hmApplication = new HashMap<>();
//                hmApplication.put("PolicyStatus", "pending");
//                hmApplication.put("applicationNo", formData.getApplicationNo());
//                hmApplication.put("applicantName", formData.getApplicantName());
//                hmApplication.put("panNo", formData.getPanNo());
//                hmApplication.put("srNo", formData.getSrNo());
//                hmApplication.put("purchaseDate", formData.getDateOfPolicy());
//                hmApplication.put("contactNo", formData.getDateOfPolicy());
//                hmApplication.put("CSM Code", "LM216305CS");
//                docRef
//                        .collection("NewCollection")
//                        .document(formData.getSrNo())
////                        .collection("PolicyForms")
////                        .document(formData.getApplicationNo())
//                        .set(hmApplication)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Log.d(TAG, "On form data submit: " + formData.getApplicantName() + " data is completely submit");
//                            }
//                        })
//                        .addOnCanceledListener(new OnCanceledListener() {
//                            @Override
//                            public void onCanceled() {
//                                Log.d(TAG, "cancel adding document");
//                            }
//                        })
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Log.d(TAG, "On form data submit: " + formData.getApplicantName() + " data is successfully submit");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w(TAG, "Error adding document", e);
//                            }
//                        });
//            }
//        } else {
//            Toast.makeText(ImportEmployeesActivity.this, "No user list found", Toast.LENGTH_SHORT).show();
//        }
//    }


}
