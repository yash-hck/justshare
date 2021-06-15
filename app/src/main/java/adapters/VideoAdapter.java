package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.justshare.R;
import com.example.justshare.models.VideoModel;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<VideoModel> videoList;
    private static OnItemClickListener onItemClickListener;

    public VideoAdapter(Context context, ArrayList<VideoModel> list){
        this.context = context;
        this.videoList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.video_item,parent,false);
        return new VideoListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final VideoListViewHolder viewHolder = (VideoListViewHolder)holder;

        Glide.with(context)
                .load(videoList.get(position).getVideo())
                .placeholder(R.drawable.video_background)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(viewHolder.videoThumbnail);

        viewHolder.videoTitle.setText(videoList.get(position).getTitle());
        viewHolder.videoSubtitle.setText(videoList.get(position).getSubTitle());

        if(videoList.get(position).isSelected()){
            viewHolder.videoChaeckBox.setChecked(true);
        }
        else {
            viewHolder.videoChaeckBox.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoListViewHolder extends RecyclerView.ViewHolder{

        ImageView videoThumbnail;
        TextView videoTitle, videoSubtitle;
        CheckBox videoChaeckBox;

        public VideoListViewHolder(@NonNull View itemView) {
            super(itemView);
            videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            videoSubtitle = itemView.findViewById(R.id.videoSubtitle);
            videoChaeckBox = itemView.findViewById(R.id.videoCheckBox);

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

    public interface OnItemClickListener{
        void onItemClick(int position, View view);
    }

}
