package cypher.destino;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import cypher.destino.Adapters.AdapterRecyclerManager;
import cypher.destino.Storage.Helperss;
import cypher.destino.Storage.SqlObject;

/**
 * Created by karhack on 29/11/16.
 */
public class GeoListActivity extends AppCompatActivity {
    private static final String TAG = "GeoListActivity";
    public static RecyclerView recyclerGeoList;
    public static AdapterRecyclerManager adapterRecyclerManager;
    public static ArrayList<SqlObject> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.layout_geolist);
        recyclerGeoList = (RecyclerView) findViewById(R.id.recyclerGeoList);
        recyclerGeoList.setLayoutManager(new LinearLayoutManager(this));
            arrayList = new ArrayList<>();
            Helperss helperss = new Helperss(this);
            arrayList = helperss.getAllRecords();

            adapterRecyclerManager = new AdapterRecyclerManager(this,arrayList);

            recyclerGeoList.setAdapter(adapterRecyclerManager);


    }catch (Exception e){
            Log.e(TAG, "onCreate err : "+e.getLocalizedMessage());
        }
    }

    public void notifyChange(int position){
        try{
        arrayList.remove(position);
        recyclerGeoList.removeViewAt(position);
        adapterRecyclerManager.notifyItemRemoved(position);
    }catch (Exception e){
            Log.e(TAG, "notifyChange Err "+e.getLocalizedMessage());
        }}
}
