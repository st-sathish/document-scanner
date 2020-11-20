package com.daypay_technologies.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.daypay_technologies.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    private List<File> data;

    public FolderAdapter(ArrayList<File> data){
        this.data = data;
    }

    @NonNull
    @Override
    public FolderAdapter.FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View deviceView = inflater.inflate(R.layout.item_folder, parent, false);
        return new FolderAdapter.FolderViewHolder(deviceView);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderAdapter.FolderViewHolder holder, int position) {
        File imageFile = data.get(position);
        holder.folderName.setText(imageFile.getName());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder  {

        private TextView folderName;

        private LinearLayout layout;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.item_folder_name);
            layout = itemView.findViewById(R.id.item_root);
        }
    }
}
