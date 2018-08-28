package ir.iot.smartremote.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.iot.smartremote.R;
import ir.iot.smartremote.RemoteFunction;


public class RemoteLayoutFragment extends Fragment {


    private onRemotePanelInteractionListener mListener;

    public RemoteLayoutFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RemoteLayoutFragment newInstance(String param1, String param2) {
        RemoteLayoutFragment fragment = new RemoteLayoutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.remotelayout, container, false);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onRemotePanelInteractionListener) {
            mListener = (onRemotePanelInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onRemotePanelInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface onRemotePanelInteractionListener {
        // TODO: Update argument type and name
        void onRemoteFunctionCLicked(RemoteFunction remoteFunction);
    }
}
