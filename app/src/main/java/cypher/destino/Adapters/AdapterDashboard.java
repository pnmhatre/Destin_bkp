package cypher.destino.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cypher.destino.GeoListActivity;
import cypher.destino.MapActivity;
import cypher.destino.R;


/**
 * Created by karhack on 29/11/16.
 */
public class AdapterDashboard extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "AdapterDashboard";
    private View view;
    AppCompatActivity appCompatActivity;
    ArrayList<String> arrayList;
    public AdapterDashboard(AppCompatActivity appCompatActivity,ArrayList<String> arrayList){
        this.appCompatActivity = appCompatActivity;
        this.arrayList = arrayList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recycler_dashboard, parent, false);
            return new DashboardViewHolder(view);
        }catch (Exception e){
            Log.e(TAG, "OnCreateViewHolder Err : "+e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try{
            DashboardViewHolder dHolder = (DashboardViewHolder) holder;
            dHolder.titleTv.setText(arrayList.get(position).toString());
        }catch (Exception e){Log.e(TAG, "onBindViewHolder  Err : "+e.getLocalizedMessage());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class DashboardViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        public DashboardViewHolder(View itemView) {
            super(itemView);
            try{
                titleTv = (TextView)itemView.findViewById(R.id.ad_dash_text);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            Class aClass = null;
                            switch (getAdapterPosition()){
                                case 0: aClass = MapActivity.class; break;
                                case 1: aClass = GeoListActivity.class; break;
                                default: aClass = MapActivity.class; break;
                            }
                            Intent intent = new Intent(appCompatActivity, aClass);
                            appCompatActivity.startActivity(intent);
                        }catch (ClassCastException e){
                            Log.e(TAG, "Class not found : "+e.getLocalizedMessage());
                        }catch (ActivityNotFoundException e){
                            Log.e(TAG, "ActivityNotFoundException : "+e.getLocalizedMessage());
                        }
                    }
                });
            }catch (Exception e){
                Log.e(TAG, "ViewHolder Err : "+e.getLocalizedMessage());
            }
        }
    }
}
