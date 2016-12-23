package cypher.destino.Adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cypher.destino.GeoListActivity;
import cypher.destino.R;
import cypher.destino.Storage.Helperss;
import cypher.destino.Storage.SqlObject;

/**
 * Created by karhack on 29/11/16.
 */
public class AdapterRecyclerManager extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG ="AdapterRecyclerManager";
    private final AppCompatActivity mActivity;
    public ArrayList<SqlObject> arraylist;
    public AdapterRecyclerManager(AppCompatActivity mActivity, ArrayList<SqlObject> arraylist){
        this.arraylist = arraylist;
        this.mActivity = mActivity;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recycler, parent, false);
        return new ManagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try{
            ManagerViewHolder managerViewHolder = (ManagerViewHolder) holder;
            managerViewHolder.tvId.setText("id : "+arraylist.get(position).id);
            managerViewHolder.tvName.setText("name : "+arraylist.get(position).name);
            managerViewHolder.tvAddress.setText("address : "+arraylist.get(position).address);
            managerViewHolder.tvLat.setText("lat : "+arraylist.get(position).latitude);
            managerViewHolder.tvLon.setText("lon : " + arraylist.get(position).longitude);

            Date date = new Date(Long.valueOf(arraylist.get(position).timestamp));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            managerViewHolder.tvDate.setText(dateFormat.format(date));

            String string = arraylist.get(position).radius;
            if (string.equalsIgnoreCase(NotYetConfig.radiusWalk)){
                managerViewHolder.typeImage.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_walk));
            }else if (string.equalsIgnoreCase(NotYetConfig.radiusCar)){
                managerViewHolder.typeImage.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_car));
            }else if (string.equalsIgnoreCase(NotYetConfig.radiusTrain)){
                managerViewHolder.typeImage.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_train));
            }


            if (arraylist.get(position).status.equalsIgnoreCase("ON")){
                managerViewHolder.statusSwitch.setChecked(true);
            }

        }catch (Exception e){
            Log.e(TAG, "onBindViewHolder : "+e.getLocalizedMessage());
        }
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }


    public class ManagerViewHolder extends RecyclerView.ViewHolder {
        ImageView btnDelete;
        TextView tvId, tvName, tvAddress, tvLat, tvLon, tvDate;
        SwitchCompat statusSwitch;
        ImageView typeImage;
        public ManagerViewHolder(View itemView) {
            super(itemView);
            try{
            tvId = (TextView)itemView.findViewById(R.id.tv_id_1);
            tvName = (TextView)itemView.findViewById(R.id.tv_name_1);
            tvAddress = (TextView)itemView.findViewById(R.id.tv_address_1);
            tvLat = (TextView)itemView.findViewById(R.id.tv_lat_1);
            tvLon = (TextView)itemView.findViewById(R.id.tv_lon_1);
            tvDate = (TextView)itemView.findViewById(R.id.dashboard_tv_date);

            statusSwitch = (SwitchCompat) itemView.findViewById(R.id.switch_status);
            typeImage = (ImageView) itemView.findViewById(R.id.imgType);
            btnDelete = (ImageView) itemView.findViewById(R.id.btn_delete);

                statusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        SqlObject sqlObject = new SqlObject();

                        String[] strings = tvId.getText().toString().split(": ");
                        sqlObject.id = Integer.valueOf(strings[1]);
                        if (b){
                            sqlObject.status = "ON";
                        }else {
                            sqlObject.status = "OFF";
                        }
                        new Helperss(mActivity).updateRecord(sqlObject);
                    }
                });


                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Helperss(mActivity).deleteRecord(arraylist.get(getAdapterPosition()).id);
                        new GeoListActivity().notifyChange(getAdapterPosition());
                    }
                });
        }catch (Exception e){
                Log.e(TAG, "ManagerViewHolder : "+e.getLocalizedMessage());
            }
        }
    }

}
