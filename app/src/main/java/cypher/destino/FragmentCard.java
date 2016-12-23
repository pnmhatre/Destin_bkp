package cypher.destino;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by karhack on 7/12/16.
 */
public class FragmentCard extends Fragment {
    private static final String TAG ="FragmentCard" ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try{
        View view = inflater.inflate(R.layout.adapter_recycler, container,false);
        return view;
    }catch (Exception e){
            Log.e(TAG, "onCreateView err : "+e.getLocalizedMessage());
            return null;
        }
    }


}
