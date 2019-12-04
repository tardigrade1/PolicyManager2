package in.gen2.policymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
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

import in.gen2.policymanager.authActivities.PhoneAuthActivity;
import in.gen2.policymanager.models.EmpData;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class ImportEmployeesActivity extends AppCompatActivity {
    String TAG = "main";
    private ArrayList<EmpData> list_show_users = new ArrayList();
    CsvParserSettings parserSettings;
    int id = 0;
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
        FileControl();
    }

    private void FileControl() {


        docRef = FirebaseFirestore.getInstance();
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
            parserSettings.selectFields("S No", "Gr", "Date", "SR No", "Name", "Agency No", "Branch", "Br No", "Residence", "Mob No", "Email");
            try {
                for (String[] strArr : parseWithSettings(this.parserSettings, "ReadQrData/SRList.csv")) {

                    this.list_show_users.add(new EmpData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5], strArr[6], strArr[7], strArr[8], strArr[9], strArr[10]));
                    try {
                    } catch (Exception unused) {
                        this.list_show_users.add(new EmpData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5], strArr[6], strArr[7], strArr[8], strArr[9], strArr[10]));
                    }

                }

                if (this.list_show_users.size() > 0) {
                    for (int i = 0; i < list_show_users.size(); i++) {
                        EmpData UserData = list_show_users.get(i);
                        Log.d(TAG, "ReadUsersList: " + UserData.getName() + "," + UserData.getSrno());

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
        this.id = 0;
    }

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
                        CAMERA,
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

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && WriteExternalStorage && ReadExternalStorage) {

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
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    public void uploadDataOnFiresStore(View view) {
        if (list_show_users.size() > 0) {

            for (int i = 0; i < list_show_users.size(); i++) {
                final EmpData UserData = list_show_users.get(i);
                final HashMap<String, Object> hm = new HashMap<>();
                hm.put("grade", UserData.getGrade());
                hm.put("name", UserData.getName());
                hm.put("srNo", UserData.getSrno());
                hm.put("doj", UserData.getDate());
                hm.put("agencyNo", UserData.getAgencyNo());
                hm.put("branch", UserData.getBranch());
                hm.put("branchNo", UserData.getBranchNo());
                hm.put("residence", UserData.getResidence());
                hm.put("mobileNo", UserData.getMobNo());
                hm.put("email", UserData.getEmail());
                Log.d(TAG, "onButtonClick: " + UserData.getName() + " data is successfully submit");
                docRef.collection("SalesRepresentatives").document(UserData.getSrno()).set(hm)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

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
    }

    public void logoutUser(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(ImportEmployeesActivity.this, PhoneAuthActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
