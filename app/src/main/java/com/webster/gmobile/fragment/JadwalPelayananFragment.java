package com.webster.gmobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webster.gmobile.gmobile.R;

import com.webster.gmobile.activity.JadwalPelayananActivity;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this com.webster.gmobile.fragment must implement the
 * {@link JadwalPelayananFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JadwalPelayananFragment#newInstance} factory method to
 * create an instance of this com.webster.gmobile.fragment.
 */
public class JadwalPelayananFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the com.webster.gmobile.fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    OnFragmentInteractionListener mCallback;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this com.webster.gmobile.fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of com.webster.gmobile.fragment JadwalPelayananFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JadwalPelayananFragment newInstance(String param1, String param2) {
        JadwalPelayananFragment fragment = new JadwalPelayananFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public JadwalPelayananFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.webster.gmobile.fragment
        return inflater.inflate(R.layout.fragment_jadwal_pelayanan, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri, "a");
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnFragmentInteractionListener) activity;
            Intent i= new Intent(getActivity(), JadwalPelayananActivity.class);
            getActivity().startActivity(i);
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * com.webster.gmobile.fragment to allow an interaction in this com.webster.gmobile.fragment to be communicated
     * to the com.webster.gmobile.activity and potentially other fragments contained in that
     * com.webster.gmobile.activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri, String str);
    }

}
