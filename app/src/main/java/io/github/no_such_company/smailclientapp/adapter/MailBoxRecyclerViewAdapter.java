package io.github.no_such_company.smailclientapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.github.no_such_company.smailclientapp.MailsActivity;
import io.github.no_such_company.smailclientapp.R;
import io.github.no_such_company.smailclientapp.pojo.mailList.MailFolder;

public class MailBoxRecyclerViewAdapter extends RecyclerView.Adapter<MailBoxRecyclerViewAdapter.ViewHolder> {

    private List<MailFolder> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public MailBoxRecyclerViewAdapter(Context context, List<MailFolder> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.mail_list_card, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String mailBoxName = mData.get(position).getFolderName();
        holder.myTextView.setText(mailBoxName);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.folderName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public String getItem(int id) {
        return mData.get(id).getFolderName();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}