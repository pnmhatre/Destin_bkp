package cypher.destino;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityOptionsCompat;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by karhack on 22/12/16.
 */
public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        TextView textView = (TextView)findViewById(R.id.splash_tv_version);
        final ImageView imgSplash = (ImageView) findViewById(R.id.imgSplash);
        textView.setText("v" + version);

        new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(Splash.this,imgSplash,"imgSplash");
                Intent intent = new Intent(Splash.this, DashBoard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent,activityOptionsCompat.toBundle());
                finish();
            }
        }.start();

    }



}
