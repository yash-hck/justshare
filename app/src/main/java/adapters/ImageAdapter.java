package adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.justshare.R;

import java.util.ArrayList;
import java.util.Collections;

public class ImageAdapter extends BaseAdapter {

    Context context;
    private ArrayList<String> images;

    public ImageAdapter(Context context){
        this.context  = context;
        images =  getAllImagePaths(context);
    }

    private ArrayList<String> getAllImagePaths(Context context) {
        Uri uri;

        int col_index_data, column_index_folder_name;

        ArrayList<String> imagePaths = new ArrayList<String>();

        
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_ADDED;
//Stores all the images from the gallery in Cursor
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
//Total number of images
        int count = cursor.getCount();

//Create an array to store path to all the images
        String[] arrPath = new String[count];

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            imagePaths.add(cursor.getString(dataColumnIndex));
            Log.i("PATH",cursor.getString(dataColumnIndex));
        }
// The cursor should be freed up after use with close()
        cursor.close();
        Log.d("imagesa", String.valueOf(imagePaths.size()));
        Collections.reverse(imagePaths);
        return imagePaths;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView picturesView;

        if(view == null){
            picturesView = new ImageView(context);
            picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            picturesView.setLayoutParams(new GridView.LayoutParams(260,260));
        }
        else {
            picturesView = (ImageView)view;
        }

        Glide.with(context).load(images.get(i))
                .placeholder(R.drawable.loading).centerCrop()
                .into(picturesView);

        Log.d("ktpu", String.valueOf(images.size()));

        return picturesView;
    }
}
