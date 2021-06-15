package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.justshare.R;
import com.example.justshare.models.ImageModel;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private ArrayList<ImageModel> imageList;

    private static OnItemClickListener onItemClickListener;

    public ImageAdapter(Context context, ArrayList<ImageModel> list){
        this.context = context;
        this.imageList = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item,parent,false);
        return new ImageListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final ImageListViewHolder viewHolder = (ImageListViewHolder)holder;

        Glide.with(context)
                .load(imageList.get(position).getImage())
                .placeholder(R.drawable.loading)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(viewHolder.imageView);

        if(imageList.get(position).isSelected()){
            viewHolder.checkBox.setChecked(true);
        }
        else{
            viewHolder.checkBox.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageListViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        CheckBox checkBox;

        public ImageListViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgThumb);
            checkBox = itemView.findViewById(R.id.circle);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(getAdapterPosition(),view);
                }
            });

        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

}


/*public class ImageAdapter extends BaseAdapter {

    Context context;
    private ArrayList<String> images;

    public ImageAdapter(Context context){
        this.context  = context;
        images = getAllImagePaths(context);
    }

    private ArrayList<String> getAllImagePaths(Context context) {
        Uri uri;

        int col_index_data, column_index_folder_name;

        ArrayList<String> imagePaths = new ArrayList<String>();

        *//*String absolutePath;
        uri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = context.getContentResolver().query(uri, projection, null,null,null);

        assert cursor != null;
        col_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()){
            absolutePath = cursor.getString(col_index_data);
            imagePaths.add(absolutePath);
        }
        *//*



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
        CheckBox checkBox ;

        if(view == null){
            picturesView = new ImageView(context);
            checkBox = new CheckBox(context);
            checkBox.setScaleX((float) 0.5);
            checkBox.setScaleY((float) 0.5);
            picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            checkBox.setLayoutParams(new GridView.LayoutParams(20,20));
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
}*/

