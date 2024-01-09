package com.example.gevs.ui.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gevs.R;
import com.example.gevs.data.pojo.DistrictPartyWinner;
import com.example.gevs.util.Constants;

import java.util.List;

public class DistrictWinnerAdapter extends RecyclerView.Adapter<DistrictWinnerAdapter.DistrictWinnerViewHolder> {

    List<DistrictPartyWinner> districtPartyWinnerList;

    public void setList(List<DistrictPartyWinner> districtPartyWinnerList) {
        this.districtPartyWinnerList = districtPartyWinnerList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DistrictWinnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_district_winners, parent, false);
        return new DistrictWinnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistrictWinnerViewHolder holder, int position) {
        if (districtPartyWinnerList != null) {
            DistrictPartyWinner districtPartyWinner = districtPartyWinnerList.get(position);
            holder.name.setText(districtPartyWinner.getName());
            holder.constituency.setText(districtPartyWinner.getConstituency());

            if (districtPartyWinner.getParty().equals(Constants.PARTY_BLUE)) {
                holder.party.setImageResource(R.drawable.blue);
            } else if (districtPartyWinner.getParty().equals(Constants.PARTY_RED)) {
                holder.party.setImageResource(R.drawable.red);
            } else if (districtPartyWinner.getParty().equals(Constants.PARTY_YELLOW)) {
                holder.party.setImageResource(R.drawable.yellow);
            } else {
                holder.party.setImageResource(R.drawable.independent);
            }

        }
    }

    @Override
    public int getItemCount() {
        if (districtPartyWinnerList != null) {
            return districtPartyWinnerList.size();
        } else {
            return 0;
        }
    }

    class DistrictWinnerViewHolder extends RecyclerView.ViewHolder {

        TextView name, constituency;
        ImageView party;

        public DistrictWinnerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.candidate_name_textview);
            constituency = itemView.findViewById(R.id.candidate_constituency_textview);
            party = itemView.findViewById(R.id.candidate_party_imageView);
        }
    }
}
