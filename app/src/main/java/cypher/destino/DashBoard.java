package cypher.destino;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cypher.destino.Adapters.NotYetConfig;
import cypher.destino.Storage.Helperss;
import cypher.destino.Storage.SqlObject;


/**
 * Created by karhack on 2/12/16.
 */
public class DashBoard extends AppCompatActivity {

    private static final String TAG = "DashBoard";
    public ArrayList<SqlObject> arrayList;
    public static ViewPager viewPager;
    public static TextView counterText;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        try {

                counterText = (TextView) findViewById(R.id.tv_task_counter);
                viewPager = (ViewPager) findViewById(R.id.viewpager_main);

                ImageView btnSettings = (ImageView) findViewById(R.id.btnSetting);
            btnSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(DashBoard.this,SettingsActivity.class));
                }
            });
                ImageView btnRating = (ImageView) findViewById(R.id.btnRating);

               btnRating.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Uri uri = Uri.parse("market://details?id=" + getPackageName());
                       Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                       goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                               Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                               Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                       try {
                           startActivity(goToMarket);
                       } catch (ActivityNotFoundException e) {
                           startActivity(new Intent(Intent.ACTION_VIEW,
                                   Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                       }
                   }
               });

                viewPager = (ViewPager) findViewById(R.id.viewpager_main);

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DashBoard.this, MapActivity.class);
                        startActivity(intent);
                    }
                });

            fab.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new Helperss(DashBoard.this).deletAll();
                    return true;
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "onCreate err : " + e.getLocalizedMessage());
        }
    }


    public class mViewpagerAdapter extends PagerAdapter {
        ArrayList<SqlObject> arrayList;
        public mViewpagerAdapter(ArrayList<SqlObject> arrayList){
            this.arrayList = arrayList;
        }
        TextView tvTitle, tvAddress, tvDate, tvHits, tvId;
        ImageView imgType, imgStatus, btnEdit;
        ImageView image1, image2, image3;
        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            try {
                View view = View.inflate(DashBoard.this, R.layout.adapter_recycler, null);
                tvId = (TextView) view.findViewById(R.id.tvId);
                tvTitle = (TextView) view.findViewById(R.id.tv_name);
                tvAddress = (TextView) view.findViewById(R.id.tv_address);
                tvDate = (TextView) view.findViewById(R.id.dashboard_tv_date);
                tvHits = (TextView) view.findViewById(R.id.dashboard_tv_hits);
                imgType = (ImageView) view.findViewById(R.id.img_type);
                imgStatus = (ImageView) view.findViewById(R.id.img_status);
                btnEdit = (ImageView) view.findViewById(R.id.btn_edit);
                tvId.setText(String.valueOf(arrayList.get(position).id));

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //showEditSheet(Integer.valueOf(tvId.getText().toString()));
                        showEditSheet(Integer.valueOf(((TextView) container.getChildAt(position)
                                .findViewById(R.id.tvId)).getText().toString()));
                    }
                });

                image1 = (ImageView) view.findViewById(R.id.img_1);
                image2 = (ImageView) view.findViewById(R.id.img_2);
                image3 = (ImageView) view.findViewById(R.id.img_3);

                if (arrayList.get(position).action.equalsIgnoreCase(NotYetConfig.actionChangeProfile)){
                    image2.setImageResource(R.mipmap.ic_audio_profile);
                    image1.setImageResource(R.mipmap.ic_sms);
                    image3.setImageResource(R.mipmap.ic_notification);
                }else if (arrayList.get(position).action.equalsIgnoreCase(NotYetConfig.actionSMS)){
                    image2.setImageResource(R.mipmap.ic_sms);
                    image1.setImageResource(R.mipmap.ic_notification);
                    image3.setImageResource(R.mipmap.ic_audio_profile);
                }else if (arrayList.get(position).action.equalsIgnoreCase(NotYetConfig.actionNotify)){
                    image2.setImageResource(R.mipmap.ic_notification);
                    image1.setImageResource(R.mipmap.ic_sms);
                    image3.setImageResource(R.mipmap.ic_audio_profile);

                }



                if (arrayList.get(position).name != null){
                    tvTitle.setText(arrayList.get(position).name);
                }else {
                    tvTitle.setText("");
                }

                if (arrayList.get(position).hits !=null){

                    tvHits.setText(arrayList.get(position).hits);
                }else {
                    tvHits.setText("0");
                }

                Date date = new Date(Long.valueOf(arrayList.get(position).timestamp)*1000);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
                tvDate.setText(dateFormat.format(date));


                tvAddress.setText(arrayList.get(position).address);
/*
                if (arrayList.get(position).status.equalsIgnoreCase("on")) {
                    imgStatus.setColorFilter(getResources().getColor(R.color.green));
                } else {
                    imgStatus.setColorFilter(getResources().getColor(R.color.white_t));
                }*/


                if (arrayList.get(position).radius.equalsIgnoreCase(NotYetConfig.radiusWalk)) {
                    imgType.setImageResource(R.drawable.ic_walk);
                } else if (arrayList.get(position).radius.equalsIgnoreCase(NotYetConfig.radiusCar)) {
                    imgType.setImageResource(R.drawable.ic_car);
                } else if (arrayList.get(position).radius.equalsIgnoreCase(NotYetConfig.radiusTrain)) {
                    imgType.setImageResource(R.drawable.ic_train);
                }

                container.addView(view);
                return view;
            }catch (Exception e){
                Log.e(TAG,"instantiateItem err : "+e.getLocalizedMessage());
                e.printStackTrace();
                return null;
            }
        }


        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public float getPageWidth(int position) {
            return .6f;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout)object);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            populateViewPager();
        } catch (Exception e){
            Log.e(TAG, "OnResume Err : "+e.getLocalizedMessage());

        }

    }

    private void populateViewPager() {
        Helperss helperss = new Helperss(DashBoard.this);
        if (helperss.getAllRecords()!=null) {
            counterText.setText(helperss.getAllRecords().size() + " tasks found");
            viewPager.setAdapter(new mViewpagerAdapter(helperss.getAllRecords()));
        }
    }


    String radius = null;

    public void showEditSheet(final int id){
        try {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            View view = View.inflate(this, R.layout.bottomsheet_add_geo, null);
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();


            ImageView imgAdd = (ImageView) view.findViewById(R.id.fabAdd);
            imgAdd.setVisibility(View.INVISIBLE);
            final ImageView btnWalk = (ImageView)view.findViewById(R.id.btnWalk);
            final ImageView btnCar = (ImageView)view.findViewById(R.id.btnCar);
            final ImageView btnTrain = (ImageView)view.findViewById(R.id.btnTrain);

            final TextView txtTitle = (TextView) view.findViewById(R.id.edtext_title);
            final TextView txtAddress= (TextView)view.findViewById(R.id.tv_bm_address);
            TextView txtDistance= (TextView)view.findViewById(R.id.tv_bm_distance);
            txtDistance.setVisibility(View.INVISIBLE);

            RadioButton radioNotify = (RadioButton)view.findViewById(R.id.checkbox_notify);
            RadioButton radioChangeProfile = (RadioButton)view.findViewById(R.id.checkbox_change_profile);
            Button btnSave = (Button) view.findViewById(R.id.btn_bm_add);
            Button btnDelete = (Button) view.findViewById(R.id.btn_bm_delete);
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DashBoard.this).setMessage("Do you really want to delete this location from your list?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Helperss helperss = new Helperss(DashBoard.this);
                                        helperss.deleteRecord(id);
                                        Toast.makeText(DashBoard.this, "Location has been deleted successfully", Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                        populateViewPager();
                                        bottomSheetDialog.dismiss();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                        alertBuilder.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            final Helperss helperss = new Helperss(this);
            final SqlObject sqlObject = helperss.getSingleRow(id);
            txtTitle.setText(sqlObject.name);
            txtAddress.setText(sqlObject.address);
            if (sqlObject.action.equalsIgnoreCase(NotYetConfig.actionChangeProfile)){
                action = NotYetConfig.actionChangeProfile;
                radioChangeProfile.setChecked(true);
            }else if (sqlObject.action.equalsIgnoreCase(NotYetConfig.actionNotify)){
                radioNotify.setChecked(true);
                action = NotYetConfig.actionNotify;
            }

            if (sqlObject.radius.equalsIgnoreCase(NotYetConfig.radiusWalk)){

                btnWalk.setBackground(getResources().getDrawable(R.drawable.media_back_one_pressed));
                btnCar.setBackground(getResources().getDrawable(R.drawable.media_back_two));
                btnTrain.setBackground(getResources().getDrawable(R.drawable.media_back_three));

                radius = NotYetConfig.radiusWalk;
            }else if (sqlObject.radius.equalsIgnoreCase(NotYetConfig.radiusCar)){

                btnWalk.setBackground(getResources().getDrawable(R.drawable.media_back_one));
                btnCar.setBackground(getResources().getDrawable(R.drawable.media_back_two_pressed));
                btnTrain.setBackground(getResources().getDrawable(R.drawable.media_back_three));
                radius = NotYetConfig.radiusCar;

            }else if (sqlObject.radius.equalsIgnoreCase(NotYetConfig.radiusTrain)){
                btnWalk.setBackground(getResources().getDrawable(R.drawable.media_back_one));
                btnCar.setBackground(getResources().getDrawable(R.drawable.media_back_two));
                btnTrain.setBackground(getResources().getDrawable(R.drawable.media_back_three_pressed));
                radius = NotYetConfig.radiusTrain;
            }


            btnWalk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    radius = NotYetConfig.radiusWalk;
                    btnWalk.setBackground(getResources().getDrawable(R.drawable.media_back_one_pressed));
                    btnCar.setBackground(getResources().getDrawable(R.drawable.media_back_two));
                    btnTrain.setBackground(getResources().getDrawable(R.drawable.media_back_three));
                }
            });
            btnCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    radius = NotYetConfig.radiusCar;
                    btnWalk.setBackground(getResources().getDrawable(R.drawable.media_back_one));
                    btnCar.setBackground(getResources().getDrawable(R.drawable.media_back_two_pressed));
                    btnTrain.setBackground(getResources().getDrawable(R.drawable.media_back_three));
                }
            });
            btnTrain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    radius = NotYetConfig.radiusTrain;
                    btnWalk.setBackground(getResources().getDrawable(R.drawable.media_back_one));
                    btnCar.setBackground(getResources().getDrawable(R.drawable.media_back_two));
                    btnTrain.setBackground(getResources().getDrawable(R.drawable.media_back_three_pressed));
                }
            });


            if (radioNotify.isChecked()) {
                action = NotYetConfig.actionNotify;
            } else if (radioChangeProfile.isChecked()) {
                action = NotYetConfig.actionChangeProfile;
            }

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        if (txtTitle.getText().toString().length()<3){
                            Toast.makeText(DashBoard.this, "Please enter a unique title for this location", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SqlObject sqlObject1 = new SqlObject();
                        sqlObject1.id = id;
                        sqlObject1.name = txtTitle.getText().toString();
                        sqlObject1.radius = radius;
                        sqlObject1.action = action;
                        helperss.updateRecord(sqlObject1);
                        populateViewPager();
                        bottomSheetDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }


}
