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
import com.example.justshare.models.ApkModel;

import java.util.ArrayList;

public class ApksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<ApkModel> apkList;

    private static OnItemClickListener onItemClickListener;

    public ApksAdapter(Context context, ArrayList<ApkModel> list){
        this.context = context;
        this.apkList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.apk_item, parent, false);

        return new ApkViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final ApkViewHolder viewHolder = (ApkViewHolder)holder;

        Glide.with(context)
                .load(apkList.get(position).getIcon())
                .placeholder(R.drawable.loading)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(viewHolder.apkThumbnail);

        viewHolder.apkTitle.setText(apkList.get(position).getTitle());

        if(apkList.get(position).isSelected()){
            viewHolder.apkCheckBox.setChecked(true);
        }else {
            viewHolder.apkCheckBox.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return apkList.size();
    }

    public class ApkViewHolder extends RecyclerView.ViewHolder{

        ImageView apkThumbnail;
        TextView apkTitle;
        CheckBox apkCheckBox;


        public ApkViewHolder(@NonNull View itemView) {
            super(itemView);

            apkThumbnail = itemView.findViewById(R.id.apkThumbnail);
            apkTitle = itemView.findViewById(R.id.apkTitle);
            apkCheckBox = itemView.findViewById(R.id.apkCheckBox);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(getAdapterPosition(),view);
                }
            });
        }

    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position, View v);
    }
}
