package com.example.justshare.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justshare.R;
import com.example.justshare.models.VideoModel;

import java.util.ArrayList;
import java.util.Collections;

import adapters.VideoAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    RecyclerView videosView;
    ArrayList<VideoModel> videoList;
    ArrayList<String> selectedideosList;
    VideoAdapter videoAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public VideosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideosFragment newInstance(String param1, String param2) {
        VideosFragment fragment = new VideosFragment();
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
        View view = inflater.inflate(R.layout.fragment_videos, container, false);

        videosView = view.findViewById(R.id.videosView);
        videoList = new ArrayList<>();
        selectedideosList = new ArrayList<>();

        getAllVideos();

        videosView.setLayoutManager(new LinearLayoutManager(getContext()));
        videoAdapter = new VideoAdapter(getContext(), videoList);
        videosView.setAdapter(videoAdapter);

        videoAdapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                try {
                    if(!videoList.get(position).isSelected()){
                        selectImage(position);
                    }
                    else{
                        unselectImage(position);
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
        });

        return view;

    }

    private void unselectImage(int position) {
        for(int i = 0; i < selectedideosList.size();i++){
            if(videoList.get(position).getVideo() != null){
                if(selectedideosList.get(i).equals(videoList.get(position).getVideo())){
                    videoList.get(position).setSelected(false);
                    selectedideosList.remove(i);
                    videoAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void selectImage(int position) {

        if(!selectedideosList.contains(videoList.get(position).getVideo())){
            videoList.get(position).setSelected(true);
            selectedideosList.add(0, videoList.get(position).getVideo());
            videoAdapter.notifyDataSetChanged();
        }
    }

    private void getAllVideos() {
        videoList.clear();

        final String[] columns = { MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID , MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DURATION};
        final String orderBy = MediaStore.Video.Media.DATE_ADDED;
//Stores all the images from the gallery in Cursor
        Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int image_column_index = cursor.getColumnIndex(MediaStore.Video.Media._ID);
//Total number of images
        int count = cursor.getCount();


//Create an array to store path to all the images

        while (cursor.moveToNext()){
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE));
            String absolutePath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            String duration = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DURATION));

            VideoModel model = new VideoModel();
            model.setTitle(title);
            model.setSubTitle(timeConversion(Long.parseLong(duration)));
            model.setVideo(absolutePath);
            videoList.add(model);
        }
        Collections.reverse(videoList);
        Log.d("List Length", String.valueOf(videoList.size()));
        /*for (int i = 0; i < this.count; i++) {
            cursor.moveToPosition(i);
            ids[i] = cursor.getInt(image_column_index);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            arrPath[i] = cursor.getString(dataColumnIndex);
        }*/
        // The cursor should be freed up after use with close()
        cursor.close();

    }

    public String timeConversion(long value) {
        String videoTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            videoTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            videoTime = String.format("%02d:%02d", mns, scs);
        }
        return videoTime;
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
