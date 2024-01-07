package com.example.gevs.ui.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gevs.R;
import com.example.gevs.data.pojo.DistrictVote;

import java.util.List;

public class ConstituencyAdapter extends RecyclerView.Adapter<ConstituencyAdapter.ConstituencyViewHolder> {

    final private ListItemClickListener mOnclickListener;
    List<DistrictVote> districtVoteList;

    public ConstituencyAdapter(ListItemClickListener mOnclickListener) {
        this.mOnclickListener = mOnclickListener;
    }

    @NonNull
    @Override
    public ConstituencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.list_item_district_vote, parent, false);
        return new ConstituencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConstituencyViewHolder holder, int position) {
        if (districtVoteList != null) {
            DistrictVote districtVote = districtVoteList.get(position);
            holder.constituencyName.setText(districtVote.getConstituency());
        }
    }

    @Override
    public int getItemCount() {
        if (districtVoteList != null)
            return districtVoteList.size();
        else return 0;
    }

    public void setList(List<DistrictVote> districtVoteList) {
        this.districtVoteList = districtVoteList;
        notifyDataSetChanged();
    }


    public interface ListItemClickListener {
        void onListItemClick(String key);
    }

    class ConstituencyViewHolder extends RecyclerView.ViewHolder {
        TextView constituencyName;

        public ConstituencyViewHolder(@NonNull View itemView) {
            super(itemView);
            constituencyName = itemView.findViewById(R.id.district_result_constituency_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    //long id = getItemId();
                    mOnclickListener.onListItemClick(districtVoteList.get(index).getConstituency());
                }
            });

        }
    }


}
