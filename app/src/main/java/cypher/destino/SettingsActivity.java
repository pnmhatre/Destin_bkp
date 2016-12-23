package cypher.destino;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * Created by karhack on 22/12/16.
 */
public class SettingsActivity extends AppCompatActivity {
    private SwitchCompat switchCompat;
    private TextView titleLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);
        try {
            Toolbar toolbar = (Toolbar)findViewById(R.id.toolBar);
            toolbar.setTitle("Settings");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.mipmap.ic_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            titleLocation = (TextView)findViewById(R.id.tv_location_title);
            switchCompat = (SwitchCompat)findViewById(R.id.switch_location);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        try {
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch(Exception ex) {}

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch(Exception ex) {}

            if(!gps_enabled && !network_enabled) {
                // notify user
                switchCompat.setChecked(false);
                titleLocation.setText("Location Service Disabled");
            }else {
                switchCompat.setChecked(true);
                titleLocation.setText("Location Service Enabled");
            }

            if (!switchCompat.isChecked()){
                switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(SettingsActivity.this);
                        dialog.setMessage("Location service is disabled");
                        dialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                // TODO Auto-generated method stub
                                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(myIntent);
                                //get gps
                            }
                        });
                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                // TODO Auto-generated method stub

                            }
                        });
                        dialog.show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
