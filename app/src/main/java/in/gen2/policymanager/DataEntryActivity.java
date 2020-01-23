package in.gen2.policymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.opencsv.CSVWriter;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
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

//
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

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // special intent for Samsung file manager
        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        // if you want any file type, you can skip next line
        sIntent.putExtra("CONTENT_TYPE", mimeType);
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent chooserIntent;
        if (getPackageManager().resolveActivity(sIntent, 0) != null) {
            // it is device with Samsung file manager
            chooserIntent = Intent.createChooser(sIntent, "Open file");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
        } else {
            chooserIntent = Intent.createChooser(intent, "Open file");
        }

        try {
            startActivityForResult(chooserIntent, requestcode);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 1:

                if (resultCode == RESULT_OK) {
                    File file = new File(data.getData().getPath());
                    String pathApplication = data.getData().getPath();
                    String filename = pathApplication.substring(pathApplication.indexOf("/", 2) + 1);
                    imgChoose.setImageResource(R.drawable.csv);
                    tvFileName.setText(file.getName());
                    btnUploadData.setText("DONE");
                    btnUploadData.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                    ReadApplicationForm(filename);
                    readExcelData(filename);
                    Toast.makeText(this, "Application file successfully", Toast.LENGTH_LONG).show();

                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {

                    File fileDecision = new File(data.getData().getPath());
                    String PathDecision = data.getData().getPath();
                    String filename = PathDecision.substring(PathDecision.indexOf("/", 2) + 1);
                    tvFileName.setText(fileDecision.getName());
                    imgChoose.setImageResource(R.drawable.csv);
                    btnUploadData.setText("DONE");
                    btnUploadData.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    ReadApprovalDeclineStatus(filename);
                    Toast.makeText(this, "Decision file successfully", Toast.LENGTH_LONG).show();
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    String pathQueries = data.getData().getPath();
                    String filename = pathQueries.substring(pathQueries.indexOf("/", 2) + 1);
                    File fileQuesries = new File(data.getData().getPath());
                    imgChoose.setImageResource(R.drawable.csv);
                    tvFileName.setText(fileQuesries.getName());
                    btnUploadData.setText("DONE");
                    btnUploadData.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    ReadQueriesList(filename);
                    Toast.makeText(this, "Queries successfully", Toast.LENGTH_LONG).show();
                }
                break;
            case 4:
                if (resultCode == RESULT_OK) {
                    String pathQueries = data.getData().getPath();
                    String filename = pathQueries.substring(pathQueries.indexOf("/", 2) + 1);
                    File fileQuesries = new File(data.getData().getPath());
                    imgChoose.setImageResource(R.drawable.csv);
                    tvFileName.setText(fileQuesries.getName());
                    btnUploadData.setText("DONE");
                    btnUploadData.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    ReadCommissionData(filename);
                    Toast.makeText(this, "Commission List Successfully fetched", Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }

    private void ReadSRList() {
        list_sales_representatives.clear();
//        dataUsersAdapter = null;
        String path = Environment.getExternalStorageDirectory()
                + File.separator + "ReadQrData/SRList.csv";
        if (new File(path).exists()) {
            parserSettings = new CsvParserSettings();
            parserSettings.getFormat().setLineSeparator(CSVWriter.DEFAULT_LINE_END);
            parserSettings.selectFields("Date", "SR No", "Name", "Branch", "Residence", "Mob No", "Email");
            try {
                for (String[] strArr : parseWithSettings(this.parserSettings, "ReadQrData/SRList.csv")) {

                    this.list_sales_representatives.add(new EmpData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5], strArr[6]));
                    try {
                    } catch (Exception unused) {
                        this.list_sales_representatives.add(new EmpData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5], strArr[6]));
                    }

                }

                if (this.list_sales_representatives.size() > 0) {
                    for (int i = 0; i < list_sales_representatives.size(); i++) {
                        EmpData UserData = list_sales_representatives.get(i);
                        Log.d(TAG, "ReadUsersList: " + i + ", " + UserData.getName() + ", " + UserData.getSrNo());
                    }
                    return;
                }
                Toast.makeText(this, "No users found. Please register user", Toast.LENGTH_SHORT).show();
//                this.lvUsers.setAdapter(null);
                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "No users found. Please register user", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(this, "No users found. Please register user", Toast.LENGTH_SHORT).show();
    }

    //    reading data from Application forms CSV file
    private void ReadApplicationForm(String fileLocationName) {
        list_application_form.clear();
//        dataUsersAdapter = null;
        String path = Environment.getExternalStorageDirectory()
                + File.separator + fileLocationName;
        Log.d(TAG, "ReadApplicationForm: " + path);
        if (new File(path).exists()) {
            parserSettings = new CsvParserSettings();
            parserSettings.getFormat().setLineSeparator(CSVWriter.DEFAULT_LINE_END);
            parserSettings.selectFields("Application No", "Name", "Mobile No", "PAN No", "SR No", "Date");
            try {
                for (String[] strArr : parseWithSettings(this.parserSettings, path)) {

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
                    return;
                }
                Toast.makeText(this, "No application list found.", Toast.LENGTH_SHORT).show();
//                this.lvUsers.setAdapter(null);
                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d(TAG, "ReadApplicationForm: " + e);
                Toast.makeText(this, "No application list found.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    //    reading data from Application forms CSV file
    private void ReadApprovalDeclineStatus(String fileLocationName) {
        list_application_decision.clear();

        String path = Environment.getExternalStorageDirectory()
                + File.separator + fileLocationName;
        if (new File(path).exists()) {
            parserSettings = new CsvParserSettings();
            parserSettings.getFormat().setLineSeparator(CSVWriter.DEFAULT_LINE_END);

            parserSettings.selectFields("ApplicationNo", "Bank", "SR No", "Decision", "DecisionDate", "Comment");
            try {
                for (String[] strArr : parseWithSettings(this.parserSettings, path)) {

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
                    return;
                }
                Toast.makeText(this, "No Decision list found.", Toast.LENGTH_SHORT).show();
//                this.lvUsers.setAdapter(null);
                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    //    reading data from Commissions CSV file
    private void ReadCommissionData(String fileLocationName) {
        list_application_Commission.clear();

        String path = Environment.getExternalStorageDirectory()
                + File.separator + fileLocationName;
        if (new File(path).exists()) {
            parserSettings = new CsvParserSettings();
            parserSettings.getFormat().setLineSeparator(CSVWriter.DEFAULT_LINE_END);

            parserSettings.selectFields("Application No", "SR No", "Commission");
            try {
                for (String[] strArr : parseWithSettings(this.parserSettings, path)) {

                    this.list_application_Commission.add(new CommissionData(strArr[0], strArr[1], strArr[2]));
                    try {
                    } catch (Exception unused) {
                        this.list_application_Commission.add(new CommissionData(strArr[0], strArr[1], strArr[2]));
                    }

                }
                if (this.list_application_Commission.size() > 0) {
                    for (int i = 0; i < list_application_Commission.size(); i++) {
                        CommissionData commissionData = list_application_Commission.get(i);
                        Log.d(TAG, "Commission List: " + i + ", " + commissionData.getApplicationNo() + "'s commission is" + commissionData.getCommission());

                    }
                    return;
                }
                Toast.makeText(this, "No commission list found.", Toast.LENGTH_SHORT).show();

                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    //    reading data from Application forms CSV file
    private void ReadQueriesList(String fileLocationName) {
        list_queries.clear();

        String path = Environment.getExternalStorageDirectory()
                + File.separator + fileLocationName;
        if (new File(path).exists()) {
            parserSettings = new CsvParserSettings();
            parserSettings.getFormat().setLineSeparator(CSVWriter.DEFAULT_LINE_END);
            parserSettings.selectFields("ApplicationNo", "Name", "Mobile No", "SR No", "Requirements", "Status");
            try {
                for (String[] strArr : parseWithSettings(this.parserSettings, path)) {
                    this.list_queries.add(new QueriesData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5]));
                    try {
                    } catch (Exception unused) {
                        this.list_queries.add(new QueriesData(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5]));
                    }

                }
                if (this.list_queries.size() > 0) {
                    for (int i = 0; i < list_queries.size(); i++) {
                        QueriesData formData = list_queries.get(i);
                        Log.d(TAG, "Queries List: " + i + ", " + formData.getApplicationNo());

                    }
                    return;
                }
                Toast.makeText(this, "No Decision list found.", Toast.LENGTH_SHORT).show();
//                this.lvUsers.setAdapter(null);
                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
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
        if (radioActionGroup.getCheckedRadioButtonId() == -1) {
            // no radio buttons are checked
            Toast.makeText(DataEntryActivity.this, "please choose any one action", Toast.LENGTH_SHORT).show();
        } else {

//
            if (rbAddApplication.isChecked()) {
                new uploadApplicaitonFormData().execute();
            } else if (rbDecision.isChecked()) {
                new uploadApplicaitoDecisionStatusData().execute();
            } else if (rbQueries.isChecked()) {
                new uploadQueriesData().execute();

            } else if (rbCommission.isChecked()) {
                new uploadCommissionData().execute();
            }
        }
    }

    class uploadSRData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(DataEntryActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setTitle("Please Wait");
            Dialog.setMessage("entering data..");
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

            if (list_sales_representatives.size() > 0) {
                for (int i = 0; i < list_sales_representatives.size(); i++) {
                    final EmpData srData = list_sales_representatives.get(i);
                    final HashMap<String, Object> hm = new HashMap<>();
                    hm.put("name", srData.getName());
                    hm.put("srNo", srData.getSrNo());
                    hm.put("doj", srData.getDoj());
                    hm.put("branch", srData.getBranch());
                    hm.put("residence", srData.getResidence());
                    hm.put("mobileNo", srData.getMobileNo());
                    hm.put("email", srData.getEmail());
                    firestore.collection("SalesRepresentatives").document(srData.getSrNo()).update(hm)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "onButtonClick: " + srData.getName() + " data is successfully submit");
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
                Toast.makeText(DataEntryActivity.this, "No user list found", Toast.LENGTH_SHORT).show();
            }
            return 0;
        }
    }


    class uploadApplicaitonFormData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(DataEntryActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setTitle("Please Wait");
            Dialog.setMessage("entering data..");
            Dialog.setCancelable(false);
            Dialog.setIndeterminate(false);
            Dialog.show();

        }


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (uploadData.size() > 0) {
                for (int i = 0; i < uploadData.size(); i++) {

                    final XYValue formData = uploadData.get(i);
                    final HashMap<String, Object> hmApplication = new HashMap<>();
                    hmApplication.put("applicationNo", formData.getAppNo());
                    hmApplication.put("applicantName", formData.getName());
                    hmApplication.put("panNo", formData.getPan());
                    hmApplication.put("srNo", formData.getSrNo());
                    hmApplication.put("contactNo", formData.getContact());
                    hmApplication.put("CSM Code", "LM216305CS");

                    firestore
                            .collection("PolicyForms")
                            .document(formData.getAppNo())
                            .set(hmApplication)

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "On form data submit: " + formData.getAppNo() + " data is successfully submit");
                                    Dialog.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                    Dialog.dismiss();
                                }
                            });
                }
            } else {
                Toast.makeText(DataEntryActivity.this, "No user list found", Toast.LENGTH_SHORT).show();

            }

//            if (list_application_form.size() > 0) {
//
//                for (int i = 0; i < list_application_form.size(); i++) {
//                    final ApplicationFormData formData = list_application_form.get(i);
//                    final HashMap<String, Object> hmApplication = new HashMap<>();
//                hmApplication.put("applicationNo", formData.getApplicationNo());
//                hmApplication.put("applicantName", formData.getApplicantName());
//                hmApplication.put("panNo", formData.getPanNo());
//                hmApplication.put("srNo", formData.getSrNo());
//                hmApplication.put("purchaseDate", formData.getDateOfPolicy());
//                hmApplication.put("contactNo", formData.getContactNo());
//                hmApplication.put("CSM Code", "LM216305CS");
//
//                    firestore
//                            .collection("SalesRepresentatives")
//                            .document(formData.getSrNo())
//                            .collection("PolicyForms")
//                            .document(formData.getApplicationNo())
//                            .update(hmApplication)
//
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Log.d(TAG, "On form data submit: " + formData.getApplicantName() + " data is successfully submit");
//                                    Dialog.dismiss();
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.w(TAG, "Error adding document", e);
//                                    Dialog.dismiss();
//                                }
//                            });
//                }
//            } else {
//                Toast.makeText(DataEntryActivity.this, "No user list found", Toast.LENGTH_SHORT).show();
//            }
            return 0;
        }
    }

    class uploadApplicaitoDecisionStatusData extends AsyncTask<Void, Void, Integer> {
        ProgressDialog Dialog = new ProgressDialog(DataEntryActivity.this);

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

            if (list_application_decision.size() > 0) {

                for (int i = 0; i < list_application_decision.size(); i++) {
                    final ApproveDeclineData formData = list_application_decision.get(i);
                    final HashMap<String, Object> hmDecision = new HashMap<>();
                    hmDecision.put("bankName", formData.getBankName());
                    hmDecision.put("decisionDate", formData.getDecisionDate());
                    hmDecision.put("PolicyStatus", formData.getStatus());
                    hmDecision.put("comment", formData.getComment());


                    firestore
                            .collection("SalesRepresentatives")
                            .document(formData.getSrNo())
                            .collection("PolicyForms")
                            .document(formData.getApplicationNo())
                            .update(hmDecision)

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "On form data submit: " + formData.getApplicationNo() + " data is successfully updated");
                                    Dialog.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                    Dialog.dismiss();
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setTitle("Please Wait");
            Dialog.setMessage("entering data..");
            Dialog.setIndeterminate(false);
            Dialog.show();


        }


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            if (list_queries.size() > 0) {

                for (int i = 0; i < list_queries.size(); i++) {
                    final QueriesData formData = list_queries.get(i);
                    final HashMap<String, Object> hmQueries = new HashMap<>();

                    hmQueries.put("applicationNo", formData.getApplicationNo());
                    hmQueries.put("applicantName", formData.getName());
                    hmQueries.put("srNo", formData.getSrNo());
                    hmQueries.put("contactNo", formData.getContactNo());
                    hmQueries.put("PolicyStatus", formData.getStatus());
                    hmQueries.put("Requirements", formData.getRequirements());

                    firestore
                            .collection("SalesRepresentatives")
                            .document(formData.getSrNo())
                            .collection("PolicyForms")
                            .document(formData.getApplicationNo())
                            .set(hmQueries, SetOptions.merge())

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "On form data submit: " + formData.getApplicationNo() + " data is successfully updated");
                                    Dialog.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                    Dialog.dismiss();
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog.setTitle("Please Wait");
            Dialog.setMessage("entering data..");
            Dialog.setIndeterminate(false);
            Dialog.show();


        }


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            if (list_application_Commission.size() > 0) {

                for (int i = 0; i < list_application_Commission.size(); i++) {
                    final CommissionData commissionData = list_application_Commission.get(i);
                    final HashMap<String, Object> hmQueries = new HashMap<>();


                    hmQueries.put("applicationNo", commissionData.getApplicationNo());
                    hmQueries.put("commission", commissionData.getCommission());

                    firestore
                            .collection("Commission")
                            .document(commissionData.getSrNo())
                            .collection("PolicyForms")
                            .document(commissionData.getApplicationNo())
                            .set(hmQueries)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "On form data submit: " + commissionData.getApplicationNo() + " data is successfully updated");
                                    Dialog.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                    Dialog.dismiss();
                                }
                            });
                }
            } else {
                Toast.makeText(DataEntryActivity.this, "No user list found", Toast.LENGTH_SHORT).show();
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


    //excel sheet data read
    private void readExcelData(String filePath) {

        Log.d(TAG, "readExcelData: Reading Excel File.");
        String path = Environment.getExternalStorageDirectory()
                + File.separator + filePath;
        //decarle input file
        File inputFile = new File(path);
        if (inputFile.exists()) {
            try {
                InputStream inputStream = new FileInputStream(inputFile);
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                int rowsCount = sheet.getPhysicalNumberOfRows();
                FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                StringBuilder sb = new StringBuilder();

                //outter loop, loops through rows
                for (int r = 1; r < rowsCount; r++) {
                    Row row = sheet.getRow(r);
                    int cellsCount = row.getPhysicalNumberOfCells();
                    //inner loop, loops through columns
                    for (int c = 0; c < cellsCount; c++) {
                        //handles if there are to many columns on the excel sheet.
                        if (c > 6) {
                            Log.e(TAG, "readExcelData: ERROR. Excel File Format is incorrect! ");
                            toastMessage("ERROR: Excel File Format is incorrect!");
                            break;
                        } else {
                            String value = getCellAsString(row, c, formulaEvaluator);
                            String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                            Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
                            sb.append(value + ", ");
                        }
                    }
                    sb.append(":");
                }
                Log.d(TAG, "readExcelData: STRINGBUILDER: " + sb.toString());

                parseStringBuilder(sb);

            } catch (FileNotFoundException e) {
                Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "readExcelData: Error reading inputstream. " + e.getMessage());
            }
        }
    }

    /**
     * Method for parsing imported data and storing in ArrayList<XYValue>
     */
    public void parseStringBuilder(StringBuilder mStringBuilder) {
        Log.d(TAG, "parseStringBuilder: Started parsing.");

        // splits the sb into rows.
        String[] rows = mStringBuilder.toString().split(":");

        //Add to the ArrayList<XYValue> row by row
        for (int i = 0; i < rows.length; i++) {
            //Split the columns of the rows
            String[] columns = rows[i].split(",");

            //use try catch to make sure there are no "" that try to parse into doubles.
            try {
                String id = columns[0];
                String appNo = columns[1];
                String name = columns[2];
                String mobile = columns[3];
                String pan = columns[4];
                String srNo = columns[5];

                String cellInfo = "(x,y): (" + id + "," + appNo + "," + name + "," + mobile + "," + pan + "," + srNo + ")";
                Log.d(TAG, "ParseStringBuilder: Data from row: " + cellInfo);

                //add the the uploadData ArrayList
                uploadData.add(new XYValue(id, appNo, name, mobile, pan, srNo));

            } catch (NumberFormatException e) {

                Log.e(TAG, "parseStringBuilder: NumberFormatException: " + e.getMessage());

            }
        }

        printDataToLog();
    }

    private void printDataToLog() {
        Log.d(TAG, "printDataToLog: Printing data to log...");

        if (uploadData.size() > 0) {
            for (int i = 0; i < uploadData.size(); i++) {
                String name = uploadData.get(i).getName();
                String appNo = uploadData.get(i).getAppNo();
                Log.d(TAG, "printDataToLog: (x,y): (" + appNo + "," + name + ")");
            }
        } else {
            Log.d(TAG, "printDataToLog: array list is empty");
        }
    }


    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {

            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage());
        }
        return value;
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
