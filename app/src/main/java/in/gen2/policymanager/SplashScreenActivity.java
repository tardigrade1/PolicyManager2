package in.gen2.policymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AppCompatActivity {
    @BindView(R.id.splashLogo)
    ImageView splashImg;
    @BindAnim(R.anim.splash_animation)
    Animation myanim;
    private FirebaseAuth mAuth;
    private SharedPreferences prefs = null;
    private Boolean admin;
    private Intent intent = null;
    private Boolean supervisor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // bind the view using butterknife
        ButterKnife.bind(this);
        splashImg.startAnimation(myanim);
        mAuth = FirebaseAuth.getInstance();
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        admin = prefs.getBoolean("admin", false);
        supervisor = prefs.getBoolean("supervisor", false);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

            if (firebaseUser != null) {
                //verification successful we will start the profile activity

                if (admin) {
                    intent = new Intent(this, MainActivity.class);
                } else if (supervisor) {
                    intent = new Intent(this, MainActivity.class);
                } else {
                    intent = new Intent(this, SrDashboardActivity.class);

                }
            }
            else {
            intent = new Intent(this, WelcomeInformationActivity.class);
        }

        Thread timer = new Thread() {
            @Override
            public void run() {

                try {
                    sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}
