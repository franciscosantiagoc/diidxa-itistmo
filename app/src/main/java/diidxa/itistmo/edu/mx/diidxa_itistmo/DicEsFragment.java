package diidxa.itistmo.edu.mx.diidxa_itistmo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DicEsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DicEsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DicEsFragment extends Fragment implements View.OnClickListener {

    //private OnFragmentInteractionListener mListener;

    public DicEsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button btnsuges, search;
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_dic_es, container, false);
        btnsuges = (Button) view.findViewById(R.id.sugEspBtn);
        search = (Button) view.findViewById(R.id.BTNTextEspañolSearch);
        btnsuges.setOnClickListener(this);
        search.setOnClickListener(this);

        return view;
    }

    public void onClick(View v) {

        switch (v.getId()) {
           case R.id.sugEspBtn:

                Toast.makeText(getActivity(),"Evento detectado", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), Suggestions.class);
                getActivity().startActivity(intent);

                break;

            case R.id.BTNTextEspañolSearch:
                Toast.makeText(getActivity(),"Boton buscar pesionado",Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
