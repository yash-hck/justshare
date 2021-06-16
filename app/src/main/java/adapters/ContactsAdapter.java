package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justshare.R;
import com.example.justshare.models.ContactsModel;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private static OnItemClickListener onItemClickListener;
    ArrayList<ContactsModel> contactsList;

    public ContactsAdapter(Context context, ArrayList<ContactsModel> list){
        this.context = context;
        this.contactsList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);

        return new ContactsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContactsViewHolder viewHolder = (ContactsViewHolder) holder;

        viewHolder.contactName.setText(contactsList.get(position).getName());
        viewHolder.contactNumber.setText(contactsList.get(position).getNumber());

        if(contactsList.get(position).isSelected()){
            viewHolder.contactsCheckBox.setChecked(true);
        }
        else{
            viewHolder.contactsCheckBox.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder{

        TextView contactName,contactNumber;
        CheckBox contactsCheckBox;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            contactName = itemView.findViewById(R.id.contactName);
            contactNumber = itemView.findViewById(R.id.contactNumber);
            contactsCheckBox = itemView.findViewById(R.id.contactCheckbox);

            itemView.setOnClickListener(new View.OnClickListener() {
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
