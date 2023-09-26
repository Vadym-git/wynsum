package com.wynsumart.wynsum.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wynsumart.wynsum.R;
import com.wynsumart.wynsum.databinding.TargetsSingleItemLayourBinding;
import com.wynsumart.wynsum.models.MeditationTargetContainer;

import java.util.List;

public class TargetsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // Adapter for main fragment - targets list

    private List<MeditationTargetContainer> data;
    private View.OnClickListener targetClickListener;

    public TargetsListAdapter(View.OnClickListener listener){
        targetClickListener = listener;
    }

    public void targets(List<MeditationTargetContainer> targetsList){
        data = targetsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TargetsSingleItemLayourBinding binding = TargetsSingleItemLayourBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.getRoot().setOnClickListener(targetClickListener);
        binding.startTargetSessionButton.setOnClickListener(targetClickListener);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        MeditationTargetContainer target = (MeditationTargetContainer) data.get(position);
        viewHolder.name.setText(target.name);
        viewHolder.description.setText(target.short_description);
        viewHolder.itemView.setTag(target);
        viewHolder.startTargetSession.setTag(target);
        if (target.icon != null) {
            Glide.with(viewHolder.icon)
                    .load(target.icon)
//                    .circleCrop()
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(viewHolder.icon);
        } else {
            viewHolder.icon.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, description;
        ImageView startTargetSession, icon;
        public ViewHolder(@NonNull TargetsSingleItemLayourBinding itemView) {
            super(itemView.getRoot());
            name = itemView.targetName;
            description = itemView.targetDescription;
            startTargetSession = itemView.startTargetSessionButton;
            icon = itemView.targetIcon;
        }
    }

}
