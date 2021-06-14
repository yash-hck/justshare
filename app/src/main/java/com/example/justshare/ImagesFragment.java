package com.example.justshare;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justshare.models.ImageModel;

import java.util.ArrayList;

import adapters.ImageAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImagesFragment extends Fragment {

    RecyclerView recyclerView;

    private String[] arrPath;
    private boolean[] thumbnailsselection;

    ArrayList<ImageModel> imageList;
    ArrayList<String> selectedImageList;
    private int ids[];
    private int count;

    ImageAdapter imageAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ImagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImagesFragment newInstance(String param1, String param2) {
        ImagesFragment fragment = new ImagesFragment();
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



        View imageFragmentView = inflater.inflate(R.layout.fragment_images, container, false);

        recyclerView = imageFragmentView.findViewById(R.id.recycler_view);

        imageList = new ArrayList<>();
        selectedImageList = new ArrayList<>();
        getAllImages();

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        imageAdapter = new ImageAdapter(getContext(), imageList);
        recyclerView.setAdapter(imageAdapter);

        imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                try {
                    if(!imageList.get(position).isSelected()){
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

        return imageFragmentView;
    }

    private void getAllImages() {

        imageList.clear();

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_ADDED;
//Stores all the images from the gallery in Cursor
        Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int image_column_index = cursor.getColumnIndex(MediaStore.Images.Media._ID);
//Total number of images
        int count = cursor.getCount();
        this.arrPath = new String[this.count];
        ids = new int[count];
        this.thumbnailsselection = new boolean[this.count];
//Create an array to store path to all the images
        String[] arrPath = new String[count];

        while (cursor.moveToNext()){
            String absolutePath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            ImageModel model = new ImageModel();
            model.setImage(absolutePath);
            imageList.add(model);
        }
        Log.d("List Length", String.valueOf(imageList.size()));
        /*for (int i = 0; i < this.count; i++) {
            cursor.moveToPosition(i);
            ids[i] = cursor.getInt(image_column_index);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            arrPath[i] = cursor.getString(dataColumnIndex);
        }*/
        // The cursor should be freed up after use with close()
        cursor.close();

    }

    public void selectImage(int position){
        if(!selectedImageList.contains(imageList.get(position).getImage())){
            imageList.get(position).setSelected(true);
            selectedImageList.add(0, imageList.get(position).getImage());
            imageAdapter.notifyDataSetChanged();
        }
    }

    public void unselectImage(int position){
        for(int i = 0; i < selectedImageList.size();i++){
            if(imageList.get(position).getImage() != null){
                if(selectedImageList.get(i).equals(imageList.get(position).getImage())){
                    imageList.get(position).setSelected(false);
                    selectedImageList.remove(i);
                    imageAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private boolean isStoragePremissionGranted() {
        return true;
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

