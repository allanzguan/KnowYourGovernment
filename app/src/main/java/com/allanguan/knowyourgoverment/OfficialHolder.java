package com.allanguan.knowyourgoverment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfficialHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView name;


    public OfficialHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.titleView);
        name = itemView.findViewById(R.id.nameView);
    }
}
