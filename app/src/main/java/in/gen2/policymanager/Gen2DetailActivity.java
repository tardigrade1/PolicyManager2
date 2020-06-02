package in.gen2.policymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Gen2DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen2_detail);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.gen2Weblink)
    public void onWebsiteClick(){
        String url = "https://www.gen2.in/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
