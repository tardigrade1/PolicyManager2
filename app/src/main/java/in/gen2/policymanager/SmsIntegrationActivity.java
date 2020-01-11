package in.gen2.policymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SmsIntegrationActivity extends AppCompatActivity {

    Button btnSend;
    EditText etNumber,etMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_integration);
        etNumber=findViewById(R.id.etNumber);
        etMessage=findViewById(R.id.etMessage);
        btnSend=findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numText=etNumber.getText().toString().trim();
                String msgText=etMessage.getText().toString().trim();
                if(!numText.isEmpty()|| !msgText.isEmpty())
                {
                    try {
                        // Construct data
                        String apiKey = "apikey=" + "RmXMWfq4+xU-yGjR31DRuRR6zAKJVPEqo09zuCaoR7";
                        String message = "&message=" + msgText;
                        String sender = "&sender=" + "TXTLCL";
                        String numbers = "&numbers=" + numText;

                        // Send data
                        HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
                        String data = apiKey + numbers + message + sender;
                        conn.setDoOutput(true);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                        conn.getOutputStream().write(data.getBytes("UTF-8"));
                        final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        final StringBuffer stringBuffer = new StringBuffer();
                        String line;
                        while ((line = rd.readLine()) != null) {
                            stringBuffer.append(line);
                            Toast.makeText(SmsIntegrationActivity.this,"SMS sent successfully "+line,Toast.LENGTH_SHORT).show();
                        }
                        rd.close();


                    } catch (Exception e) {
                        Toast.makeText(SmsIntegrationActivity.this,"Sending Error "+e,Toast.LENGTH_SHORT).show();
                        System.out.println("Error SMS "+e);

                    }
                }
            }
        });
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }
}
