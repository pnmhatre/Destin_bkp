package cypher.destino;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import cypher.destino.Adapters.AdapterDashboard;


/**
 * Created by karhack on 29/11/16.
 */
public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "DashboardActivity";
    String[] strings = {"Add location","Manage previous locations", "Help"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*try {

            int port = Integer.parseInt("6066");
            try {
                Thread t = new SocketServer(port);
                t.start();

            } catch (IOException e) {
                System.out.println("Err : " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.e(TAG,"SOcket Errr : "+ e.getLocalizedMessage());
        }*/

        try{
            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_dashboard);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i<strings.length; i++){
                arrayList.add(strings[i]);
            }
            recyclerView.setAdapter(new AdapterDashboard(this, arrayList));

        }catch (Exception e){
            Log.e(TAG, "OnCreate Err : "+e.getLocalizedMessage());
        }
    }
}





