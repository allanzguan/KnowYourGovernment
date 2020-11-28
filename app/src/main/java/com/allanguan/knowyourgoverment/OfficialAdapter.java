package com.allanguan.knowyourgoverment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialHolder> {

    private List<Official> officialList;
    private MainActivity mainActivity;

    OfficialAdapter(List<Official> officialList, MainActivity mainActivity){
        this.officialList = officialList;
        this.mainActivity = mainActivity;
    }




    @NonNull
    @Override
    public OfficialHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.official_list_row, parent, false);

        itemView.setOnClickListener(mainActivity);
        return new OfficialHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialHolder holder, int position) {
        Official official = officialList.get(position);

        holder.title.setText(official.getTitle());
        holder.name.setText(official.getName() + " (" + official.getParty() + ") ");
    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}
