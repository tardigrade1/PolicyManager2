package in.gen2.policymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.opencsv.CSVWriter;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;


import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.gen2.policymanager.Helpers.RealFilePathUtil;
import in.gen2.policymanager.models.ApplicationFormData;
import in.gen2.policymanager.models.ApproveDeclineData;
import in.gen2.policymanager.models.CommissionData;
import in.gen2.policymanager.models.EmpData;
import in.gen2.policymanager.models.QueriesData;
import in.gen2.policymanager.models.XYValue;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DataEntryActivity extends AppCompatActivity {
    String TAG = "main";
    private String mimeType = "text/csv";
    int applicationRequestCode = 1;
    int decisionRequestCode = 2;
    int quesriesRequestCode = 3;
    int commissionRequestCode = 4;
    Unbinder unbinder;
    @BindView(R.id.radioActionGroup)
    RadioGroup radioActionGroup;
    @BindView(R.id.chooseFile)
    LinearLayout chooseFile;
    @BindView(R.id.imgChoose)
    ImageView imgChoose;
    @BindView(R.id.tvFileName)
    TextView tvFileName;
    @BindView(R.id.btnUploadData)
    Button btnUploadData;
    @BindView(R.id.rbAddApplication)
    RadioButton rbAddApplication;
    @BindView(R.id.rbDecision)
    RadioButton rbDecision;
    @BindView(R.id.rbQueries)
    RadioButton rbQueries;
    @BindView(R.id.rbCommission)
    RadioButton rbCommission;
    private ArrayList<EmpData> list_sales_representatives = new ArrayList();
    CsvParserSettings parserSettings;
    private ArrayList<ApplicationFormData> list_application_form = new ArrayList();
    private ArrayList<ApproveDeclineData> list_application_decision = new ArrayList();
    private ArrayList<CommissionData> list_application_Commission = new ArrayList();
    private ArrayList<QueriesData> list_queries = new ArrayList();
    public static final int RequestPermissionCode = 7;
    private FirebaseFirestore firestore;
    private NetworkInfo activeNetwork;
    Intent intent;
    ArrayList<XYValue> uploadData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        unbinder = ButterKnife.bind(this);
        if (!CheckingPermissionIsEnabledOrNot()) {
            RequestMultiplePermission();
        }
//        ReadQueriesList();
//        ReadSRList();
//        ReadApplicationForm();
//        ReadApprovalDeclineStatus();
        variableControl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void variableControl() {

        firestore = FirebaseFirestore.getInstance();
        ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = conn.getActiveNetworkInfo();
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioActionGroup.getCheckedRadioButtonId() == -1) {
                    // no radio buttons are checked
                    Toast.makeText(DataEntryActivity.this, "please choose any one action", Toast.LENGTH_SHORT).show();
                } else {
                    if (rbAddApplication.isChecked()) {
                        openFile(applicationRequestCode);
                        Toast.makeText(DataEntryActivity.this, "application form", Toast.LENGTH_SHORT).show();
                    } else if (rbDecision.isChecked()) {
                        openFile(decisionRequestCode);
                        Toast.makeText(DataEntryActivity.this, "Decision list", Toast.LENGTH_SHORT).show();
                    } else if (rbQueries.isChecked()) {

                        openFile(quesriesRequestCode);
                        Toast.makeText(DataEntryActivity.this, "find queries list", Toast.LENGTH_SHORT).show();
                    } else if (rbCommission.isChecked()) {

                        openFile(commissionRequestCode);
                        Toast.makeText(DataEntryActivity.this, "find commissions list", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


    }


    public void openFile(int requestcode) {


        Intent intent;
        if (android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            intent.putExtra("CONTENT_TYPE", mimeType);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                intent = new Intent(Intent.ACTION_GET_CONTENT); // or ACTION_OPEN_DOCUMENT
                intent.setType("*/*");
//            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            } else {
                intent = new Intent(Intent.ACTION_GET_CONTENT); // or ACTION_OPEN_DOCUMENT
                intent.setType(mimeType);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            }
            String[] mimeTypes =
                    {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                            "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                            "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                            "text/plain",
                            "text/csv",
                            "application/pdf",
                            "application/zip", "application/vnd.android.package-archive"};


        }


        try {
            startActivityForResult(intent, requestcode);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 1:

                if (resultCode == RESULT_OK) {
                    Uri selectedImageURI = data.getData();
                    String imageFile = RealFilePathUtil.getPath(this, selectedImageURI);

                    String fileType = imageFile.substring(imageFile.indexOf(".", 1) + 1);
                    File fileAppForm = new File(imageFile);
//                    ReadSRList(filename);
                    if (fileType.equals("csv") && ReadApplicationForm(imageFile)) {
                        imgChoose.setImageResource(R.drawable.csv);
                        tvFileName.setText(fileAppForm.getName());
                        btnUploadData.setText("DONE");
                        btnUploadData.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        tvFileName.setText("Choose correct file");
                    }


                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {

                    Uri selectedImageURI = data.getData();
                    String imageFile = RealFilePathUtil.getPath(this, selectedImageURI);

                    String fileType = imageFile.substring(imageFile.indexOf(".", 1) + 1);
                    File fileDecision = new File(imageFile);

                    if (fileType.equals("csv") && ReadApprovalDeclineStatus(imageFile)) {
                        tvFileName.setText(fileDecision.getName());
                        imgChoose.setImageResource(R.drawable.csv);
                        btnUploadData.setText("DONE");
                        btnUploadData.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        tvFileName.setText("Choose correct file");
                    }

                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageURI = data.getData();
                    String imageFile = RealFilePathUtil.getPath(this, selectedImageURI);

                    String fileType = imageFile.substring(imageFile.indexOf(".", 1) + 1);
                    File fileQuesries = new File(imageFile);

                    if (fileType.equals("csv") && ReadQueriesList(imageFile)) {
                        imgChoose.setImageResource(R.drawable.csv);
                        tvFileName.setText(fileQuesries.getName());
                        btnUploadData.setText("DONE");
                        btnUploadData.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                    } else {
                        tvFileName.setText("Choose correct file");
                    }


                }
                break;
            case 4:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageURI = data.getData();
                    String imageFile = RealFilePathUtil.getPath(this, selectedImageURI);

                    String fileType = imageFile.substring(imageFile.indexOf(".", 1) + 1);
                    Log.d(TAG, "onActivityResult: " + imageFile);
                    File fileQuesries = new File(imageFile);

//                    if (fileType.equals("csv") && ReadSRList(imageFile)) {
                    if (fileType.equals("csv") && ReadCommissionData(imageFile)) {
                        imgChoose.setImageResource(R.drawable.csv);
                        tvFileName.setText(fileQuesries.getName());
                        btnUploadData.setText("DONE");
                        btnUploadData.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        tvFileName.setText("Choose correct file");
                    }

                }
                break;


        }
    }

    private Boolean ReadSRList(String fileLocationName) {
        list_sales_representatives.clear();
//        dataUsersAdapter = null;
//        String path = Environment.getExternalStorageDirectory()
//                + File.separator + fileLocationName;
//        Log.d(TAG, "ReadSrList: " + path);
        if (new File(fileLocationName).exists()) {
            parserSettings = new CsvParserSettings();
            parserSettings.getFormat().setLineSeparator(CSVWriter.DEFAULT_LINE_END);
            parserSettings.selectFields("Date", "SR No", "Name", "Branch", "Residence", "Mob No","Mktg Code");
            try {
                for (String[] strArr : parseWithSettings(this.parserSettings, fileLocationName)) {

                    this.list_sales_representatives.add(new EmpData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5], strArr[6]));
                    try {
                    } catch (Exception unused) {
                        this.list_sales_representatives.add(new EmpData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5], strArr[6]));
                    }

                }

                if (this.list_sales_representatives.size() > 0) {
                    for (int i = 0; i < list_sales_representatives.size(); i++) {
                        EmpData UserData = list_sales_representatives.get(i);
                        Log.d(TAG, "ReadUsersList: " + i + ", " + UserData.getName() + ", " + UserData.getSrNo() + ", " + UserData.getMobileNo() + ", " + UserData.getEmail());
                    }
                    return true;
                }
                Toast.makeText(this, "No users found. Please register user", Toast.LENGTH_SHORT).show();
//                this.lvUsers.setAdapter(null);
                return false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "No users found. Please register user", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Toast.makeText(this, "No users found. Please register user", Toast.LENGTH_SHORT).show();
        return false;
    }

    //    reading data from Application forms CSV file
    private boolean ReadApplicationForm(String fileLocationName) {
        list_application_form.clear();
//        dataUsersAdapter = null;
//        String path = Environment.getExternalStorageDirectory()
//                + File.separator + fileLocationName;
//        Log.d(TAG, "ReadApplicationForm: " + path);
        if (new File(fileLocationName).exists()) {
            parserSettings = new CsvParserSettings();
            parserSettings.getFormat().setLineSeparator(CSVWriter.DEFAULT_LINE_END);
            parserSettings.selectFields("Application No", "Name", "Mobile No", "PAN No", "SR No", "Date");
            try {
                for (String[] strArr : parseWithSettings(this.parserSettings, fileLocationName)) {

                    this.list_application_form.add(new ApplicationFormData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5]));
                    try {
                    } catch (Exception unused) {
                        this.list_application_form.add(new ApplicationFormData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5]));
                    }

                }

                if (this.list_application_form.size() > 0) {
                    for (int i = 0; i < list_application_form.size(); i++) {
                        ApplicationFormData formData = list_application_form.get(i);
                        Log.d(TAG, "ApplictionFormsList: " + i + ", " + formData.getApplicantName() + ", " + formData.getApplicationNo());

                    }
                    return true;
                }
                Toast.makeText(this, "No application list found.", Toast.LENGTH_SHORT).show();
//                this.lvUsers.setAdapter(null);
                return false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d(TAG, "ReadApplicationForm: " + e);
                Toast.makeText(this, "No application list found.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false;
    }

    //    reading data from Application forms CSV file
    private boolean ReadApprovalDeclineStatus(String fileLocationName) {
        list_application_decision.clear();

//        String path = Environment.getExternalStorageDirectory()
//                + File.separator + fileLocationName;
        if (new File(fileLocationName).exists()) {
            parserSettings = new CsvParserSettings();
            parserSettings.getFormat().setLineSeparator(CSVWriter.DEFAULT_LINE_END);

            parserSettings.selectFields("ApplicationNo", "Name", "SR No", "Decision", "Dt of Decision", "Reason of Rejection");
            try {
                for (String[] strArr : parseWithSettings(this.parserSettings, fileLocationName)) {

                    this.list_application_decision.add(new ApproveDeclineData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5]));
                    try {
                    } catch (Exception unused) {
                        this.list_application_decision.add(new ApproveDeclineData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5]));
                    }

                }
                if (this.list_application_decision.size() > 0) {
                    for (int i = 0; i < list_application_decision.size(); i++) {
                        ApproveDeclineData formData = list_application_decision.get(i);
                        Log.d(TAG, "Decision List: " + i + ", " + formData.getApplicationNo());

                    }
                    return true;
                }
                Toast.makeText(this, "No Decision list found.", Toast.LENGTH_SHORT).show();
//                this.lvUsers.setAdapter(null);
                return false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    //    reading data from Commissions CSV file
    private boolean ReadCommissionData(String fileLocationName) {
        list_application_Commission.clear();

//        String path = Environment.getExternalStorageDirectory()
//                + File.separator + fileLocationName;
        if (new File(fileLocationName).exists()) {
            parserSettings = new CsvParserSettings();
            parserSettings.getFormat().setLineSeparator(CSVWriter.DEFAULT_LINE_END);

            parserSettings.selectFields("SR Code", "Application No", "Name", "Dt of Decision", "Comm Paid", "DecisionMonth");
            try {
                for (String[] strArr : parseWithSettings(this.parserSettings, fileLocationName)) {

                    this.list_application_Commission.add(new CommissionData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5]));
                    try {
                    } catch (Exception unused) {
                        this.list_application_Commission.add(new CommissionData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5]));
                    }

                }
                if (this.list_application_Commission.size() > 0) {
                    for (int i = 0; i < list_application_Commission.size(); i++) {
                        CommissionData commissionData = list_application_Commission.get(i);
                        Log.d(TAG, "Commission List: " + i + ", " + commissionData.getApplicationNo() + "'s commission is" + commissionData.getCommission());

                    }
                    return true;
                }
                Toast.makeText(this, "No commission list found.", Toast.LENGTH_SHORT).show();

                return false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    //    reading data from Application forms CSV file
    private boolean ReadQueriesList(String fileLocationName) {
        list_queries.clear();

//        String path = Environment.getExternalStorageDirectory()
//                + File.separator + fileLocationName;
        if (new File(fileLocationName).exists()) {
            parserSettings = new CsvParserSettings();
            parserSettings.getFormat().setLineSeparator(CSVWriter.DEFAULT_LINE_END);
            parserSettings.selectFields("ApplicationNo", "Name", "Mobile No", "SR No", "Requirements");
            try {
                for (String[] strArr : parseWithSettings(this.parserSettings, fileLocationName)) {
                    this.list_queries.add(new QueriesData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4]));
                    try {
                    } catch (Exception unused) {
                        this.list_queries.add(new QueriesData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4]));
                    }

                }
                if (this.list_queries.size() > 0) {
                    for (int i = 0; i < list_queries.size(); i++) {
                        QueriesData formData = list_queries.get(i);
                        Log.d(TAG, "Queries List: " + i + ", " + formData.getApplicationNo());

                    }
                    return true;
                }
                Toast.makeText(this, "No Decision list found.", Toast.LENGTH_SHORT).show();
//                this.lvUsers.setAdapter(null);
                return false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private List<String[]> parseWithSettings(CsvParserSettings csvParserSettings, String path) throws FileNotFoundException {
        RowListProcessor rowListProcessor = new RowListProcessor();
        csvParserSettings.setProcessor(rowListProcessor);
        csvParserSettings.setHeaderExtractionEnabled(true);
        CsvParser csvParser = new CsvParser(csvParserSettings);
        csvParser.parse(new FileReader(path));
        return rowListProcessor.getRows();
    }

    public void btnUploadData(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DataEntryActivity.this);
        if (radioActionGroup.getCheckedRadioButtonId() == -1) {
            // no radio buttons are checked
            Toast.makeText(DataEntryActivity.this, "please choose any one action", Toast.LENGTH_SHORT).show();
        } else {

//

            if (rbAddApplication.isChecked()) {
                alertDialog.setMessage("Please checkout file is correct or not!");
                new uploadApplicaitonFormData().execute();
            } else if (rbDecision.isChecked()) {
                new uploadApplicaitoDecisionStatusData().execute();
            } else if (rbQueries.isChecked()) {
                new uploadQueriesData().execute();

            } else if (rbCommission.isChecked()) {
                new uploadCommissionData().execute();
//                                new uploadSRData().execute();

            }
        }
    }

    class uploadSRData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(DataEntryActivity.this);
        int dataSize = list_sales_representatives.size();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setTitle("Please Wait");
            Dialog.setMessage("Exporting data from file..");
            Dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            Dialog.setIndeterminate(false);
            Dialog.setProgress(0);
            Dialog.setCancelable(false);
            Dialog.setMax(dataSize);
            Dialog.show();
            isInternetOn();

        }

        protected void isInternetOn() {

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

            if (dataSize > 0) {
                for (int i = 0; i <dataSize; i++) {
                    final EmpData srData = list_sales_representatives.get(i);
                    final HashMap<String, Object> hm = new HashMap<>();
//                    hm.put("name", srData.getName());
//                    hm.put("srNo", srData.getSrNo());
//                    hm.put("doj", srData.getDoj());
//                    hm.put("branch", srData.getBranch());
//                    hm.put("residence", srData.getResidence());
//                    hm.put("mobileNo", srData.getMobileNo());
//                    hm.put("email", srData.getEmail());
                    hm.put("supervisorCode", srData.getEmail());
                    hm.put("active", true);
                    int finalI = i;
                    firestore
                            .collection("SalesRepresentatives")
                            .document(srData.getSrNo())
                            .set(hm, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Dialog.setMessage("Entering data...");
                                    Dialog.setProgress(finalI);
                                    if (finalI==dataSize-1) {
                                        Dialog.dismiss();

                                    }
                                }
                            });
                }
            } else {
                Toast.makeText(DataEntryActivity.this, "No user list found", Toast.LENGTH_SHORT).show();
            }
            return 0;
        }
    }


    class uploadApplicaitonFormData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(DataEntryActivity.this);
        int dataSize = list_application_form.size();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setTitle("Please Wait");
            Dialog.setMessage("Exporting data from file..");
            Dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            Dialog.setIndeterminate(false);
            Dialog.setProgress(0);
            Dialog.setCancelable(false);
            Dialog.setMax(dataSize);
            Dialog.show();

        }


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            if (dataSize > 0) {
                for (int i = 0; i < dataSize; i++) {
                    final ApplicationFormData formData = list_application_form.get(i);
                    final HashMap<String, Object> hmApplication = new HashMap<>();
                    hmApplication.put("applicationNo", formData.getApplicationNo());
                    hmApplication.put("applicantName", formData.getApplicantName());
                    hmApplication.put("panNo", formData.getPanNo());
                    hmApplication.put("srNo", formData.getSrNo());
                    hmApplication.put("purchaseDate", formData.getDateOfPolicy());
                    hmApplication.put("contactNo", formData.getContactNo());
                    hmApplication.put("CSM Code", "LM216305CS");
                    DocumentReference docRef = firestore
                            .collection("SalesRepresentatives")
                            .document(formData.getSrNo())
                            .collection("ApplicationForms")
                            .document(formData.getApplicationNo());
                    int finalI = i;
                    docRef.set(hmApplication, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Dialog.setMessage("Entering data...");
                                    Dialog.setProgress(finalI);
                                    if (finalI==dataSize-1) {
                                        Dialog.dismiss();

                                    }
                                }
                            });

                }
            } else {
                Toast.makeText(DataEntryActivity.this, "No user list found", Toast.LENGTH_SHORT).show();
            }
            return 0;
        }
    }

    class uploadApplicaitoDecisionStatusData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(DataEntryActivity.this);
        int dataSize = list_application_decision.size();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setTitle("Please Wait");
            Dialog.setMessage("entering data..");
            Dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            Dialog.setIndeterminate(false);
            Dialog.setProgress(0);
            Dialog.setCancelable(false);
            Dialog.setMax(dataSize);
            Dialog.show();
            isInternetOn();

        }

        protected void isInternetOn() {
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

            if (dataSize > 0) {

                for (int i = 0; i < dataSize; i++) {

                    final ApproveDeclineData formData = list_application_decision.get(i);
                    final HashMap<String, Object> hmDecision = new HashMap<>();
                    hmDecision.put("bankName", "Axis");
                    hmDecision.put("decisionDate", formData.getDecisionDate());
                    hmDecision.put("PolicyStatus", formData.getStatus());
                    hmDecision.put("comment", formData.getComment());
                    int finalI = i;
                    firestore
                            .collection("SalesRepresentatives")
                            .document(formData.getSrNo())
                            .collection("ApplicationForms")
                            .document(formData.getApplicationNo())
                            .set(hmDecision, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Dialog.setMessage("Entering data...");
                                    Dialog.setProgress(finalI);
                                    if (finalI==dataSize-1) {
                                        Dialog.dismiss();

                                    }
                                }
                            });
                }
            } else {
                Toast.makeText(DataEntryActivity.this, "No user list found", Toast.LENGTH_SHORT).show();
            }
            return 0;
        }
    }

    class uploadQueriesData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(DataEntryActivity.this);
        int dataSize = list_queries.size();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setTitle("Please Wait");
            Dialog.setMessage("Exporting data from file..");
            Dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            Dialog.setIndeterminate(false);
            Dialog.setProgress(0);
            Dialog.setCancelable(false);
            Dialog.setMax(dataSize);
            Dialog.show();


        }


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            if (dataSize > 0) {

                for (int i = 0; i < dataSize; i++) {
                    final QueriesData formData = list_queries.get(i);
                    final HashMap<String, Object> hmQueries = new HashMap<>();


                    hmQueries.put("PolicyStatus", "Pending");
                    hmQueries.put("requirements", formData.getRequirements());
                    Log.d(TAG, "doInBackground: "+i+", "+hmQueries.entrySet());
//                    hmQueries.put("comment", FieldValue.delete());
                    int finalI = i;
                    firestore
                            .collection("SalesRepresentatives")
                            .document(formData.getSrNo())
                            .collection("ApplicationForms")
                            .document(formData.getApplicationNo())
                            .set(hmQueries, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Dialog.setMessage("Entering data...");
                                    Dialog.setProgress(finalI);
                                    if (finalI==dataSize-1) {
                                        Dialog.dismiss();

                                    }
                                }
                            });

                }
            } else {
                Toast.makeText(DataEntryActivity.this, "No user list found", Toast.LENGTH_SHORT).show();
            }
            return 1;
        }
    }

    class uploadCommissionData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(DataEntryActivity.this);
        final String NEW_FORMAT = "yyyyMM";
        final String OLD_FORMAT = "MMMM, yyyy";
        int dataSize = list_application_Commission.size();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setTitle("Please Wait");
            Dialog.setMessage("Exporting data from file..");
            Dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            Dialog.setIndeterminate(false);
            Dialog.setProgress(0);
            Dialog.setCancelable(false);
            Dialog.setMax(dataSize);
            Dialog.show();


        }


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
//            Dialog.dismiss();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            if (dataSize > 0) {
                for (int i = 0; i < dataSize; i++) {

                    final CommissionData commissionData = list_application_Commission.get(i);
                    String monthId = null;
                    String monthText = commissionData.getDecisionMonth();
                    final HashMap<String, Object> hmQueries = new HashMap<>();

////                    String dateSubstring = splitToNChar(dateText, 6);
                    SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
                    Date d = null;
                    try {
                        d = sdf.parse(monthText);
                        sdf.applyPattern(NEW_FORMAT);
                        monthId = sdf.format(d);
                        hmQueries.put("monthId", monthId);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
//                    hmQueries.put("monthId", monthText);
                    hmQueries.put("monthName", monthText);
                    int finalI = i;
                    DocumentReference docRef = firestore
                            .collection("Commissions")
                            .document(commissionData.getSrNo())
                            .collection("months")
                            .document(monthId);
                    if (commissionData.getApplicationNo() != null) {
                        Map<String, Object> nestedData = new HashMap<>();
                        nestedData.put("ApplicationId", commissionData.getApplicationNo());
                        nestedData.put("Commission", commissionData.getCommission());
                        nestedData.put("ApplicantName", commissionData.getName());
                        nestedData.put("decisionDate", commissionData.getDecisionDate());
                        Log.d(TAG, "doInBackground: "+nestedData.entrySet());
                        docRef.set(hmQueries, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        docRef.collection("ApplicationForms")
                                                .document(commissionData.getApplicationNo())
                                                .set(nestedData, SetOptions.merge());
                                        Dialog.setMessage("Entering data...");
                                        Dialog.setProgress(finalI);
                                        if (finalI==dataSize-1) {
                                            Dialog.dismiss();

                                        }
                                    }
                                });
                    } else {
                        Map<String, Object> nestedData = new HashMap<>();
                        nestedData.put("Commission", commissionData.getCommission());
                        nestedData.put("ApplicantName", commissionData.getName());
                        Log.d(TAG, "doInBackground: "+nestedData.entrySet());
                        docRef.set(hmQueries, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        docRef.collection("ApplicationForms")
                                                .document()
                                                .set(nestedData, SetOptions.merge());
                                        Dialog.setProgress(finalI);
                                        if (finalI==dataSize-1) {
                                            Dialog.dismiss();

                                        }
                                    }
                                });

                    }

                }

            } else {
                Toast.makeText(DataEntryActivity.this, "No user list found", Toast.LENGTH_SHORT).show();
                Dialog.dismiss();
            }
            return 0;
        }
    }


    //Permission function starts from here
    private void RequestMultiplePermission() {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(DataEntryActivity.this, new String[]
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
                        Toast.makeText(DataEntryActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(DataEntryActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }

    // Checking permission is enabled or not using function starts from here.
    public boolean CheckingPermissionIsEnabledOrNot() {
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;
    }


    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private static String splitToNChar(String text, int size) {
        String subString = null;
        int length = text.length();
//        for (int i = 0; i < length; i += size) {
        subString = text.substring(0, Math.min(length, size));
//        }
        return subString;
    }
    private void compareApplicationNo() {
        if (list_application_decision.size() > 0) {
            for (int i = 0; i < list_application_decision.size(); i++) {
                final ApproveDeclineData formData = list_application_decision.get(i);
                for (ApplicationFormData applicationFormData : list_application_form) {
                    if (formData.getApplicationNo().equals(applicationFormData.getApplicationNo())) {
                        Log.d(TAG, "compareApplicationNo: " + i + ", " + formData.getApplicationNo());
                    }
                }
            }
        }
    }
}
