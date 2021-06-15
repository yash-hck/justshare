package com.example.justshare.fragments;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justshare.R;
import com.example.justshare.models.ApkModel;

import java.util.ArrayList;
import java.util.List;

import adapters.ApksAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ApksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApksFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView apkView;

    ArrayList<ApkModel> apkList;
    ArrayList<ApplicationInfo> pakageList;
    ArrayList<String> selectedApks;

    ApksAdapter apksAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public ApksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ApksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ApksFragment newInstance(String param1, String param2) {
        ApksFragment fragment = new ApksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_apks, container, false);

        apkView = view.findViewById(R.id.apkView);
        apkList = new ArrayList<>();
        selectedApks = new ArrayList<>();
        pakageList = new ArrayList<>();

        getAllApks();

        apkView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        apksAdapter = new ApksAdapter(getContext(), apkList);
        apkView.setAdapter(apksAdapter);

        apksAdapter.setOnItemClickListener(new ApksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                try {
                    if(!apkList.get(position).isSelected()){
                        selectApk(position);
                    }
                    else{
                        unSelectApk(position);
                    }
                }
                catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }


    private void  selectApk(int position){
        if(!selectedApks.contains(apkList.get(position).getPackageName())){
            apkList.get(position).setSelected(true);
            selectedApks.add(0, apkList.get(position).getPackageName());
            apksAdapter.notifyDataSetChanged();
        }
    }

    private void unSelectApk(int position){
        for(int i = 0; i < selectedApks.size();i++){
            if(apkList.get(position).getPackageName() != null){
                if(selectedApks.get(i).equals(apkList.get(position).getPackageName())){
                    apkList.get(position).setSelected(false);
                    selectedApks.remove(i);
                    apksAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void getAllApks() {
        apkList.clear();
        final PackageManager pm = getContext().getPackageManager();
        List<PackageInfo> packages = getContext().getPackageManager().getInstalledPackages(0);

        for(PackageInfo info : packages){

            if((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                ApkModel model = new ApkModel();
                model.setPackageName(info.packageName);
                model.setApkPath(info.applicationInfo.sourceDir);
                model.setIcon(info.applicationInfo.loadIcon(getContext().getPackageManager()));
                model.setTitle(info.applicationInfo.loadLabel(getContext().getPackageManager()).toString());
                model.setVersionName(info.versionName);
                model.printInfo();
                apkList.add(model);
            }


        }

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
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
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
