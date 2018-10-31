package diidxa.itistmo.edu.mx.diidxa_itistmo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



public class DicZaFragment extends Fragment implements View.OnClickListener {
    private Button btnsugZa;


    public DicZaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_dic_za, container, false);
        btnsugZa = (Button) vista.findViewById(R.id.sugZapBtn);
        btnsugZa.setOnClickListener(this);
        return vista;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sugZapBtn:
                Intent intent = new Intent(getActivity(), Suggestions.class);
                getActivity().startActivity(intent);
                break;
        }
    }
}
