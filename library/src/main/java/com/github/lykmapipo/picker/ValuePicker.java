package com.github.lykmapipo.picker;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

/**
 * A pack of helpful helpers and uis to pick value from a given list of values.
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public class ValuePicker {
    // default color generator
    final ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

    // internal helpers

    /**
     * Parse color from pickable
     *
     * @param pickable
     * @return
     */
    private int colorFor(@NonNull Pickable pickable) {
        String color = pickable.getColor();
        // try parse pickable color
        try {
            int parsedColor = Color.parseColor(color);
            return parsedColor;
        }
        // return random material color
        catch (Exception e) {
            return colorGenerator.getRandomColor();
        }
    }

    // interface
    public interface Pickable {
        @NonNull
        String getId();

        @NonNull
        String getName();

        @Nullable
        String getDescription();

        @Nullable
        String getColor();
    }

    public interface OnClickListener {
        void onClick(Pickable pickable);
    }

    /**
     * Pickable {@link androidx.recyclerview.widget.RecyclerView.Adapter}
     *
     * @since 0.1.0
     */
    class PickablesAdapter extends RecyclerView.Adapter<PickablesAdapter.PickableViewHolder> {
        private List<Pickable> pickables;
        private OnClickListener listener;

        public PickablesAdapter(@NonNull List<Pickable> pickables, @NonNull OnClickListener listener) {
            this.pickables = pickables;
            this.listener = listener;
        }

        @NonNull
        @Override
        public PickableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View valueView = inflater.inflate(R.layout.item_value, parent, false);
            return new PickableViewHolder(valueView);
        }

        @Override
        public void onBindViewHolder(@NonNull PickableViewHolder pickableViewHolder, int position) {
            Pickable pickable = pickables.get(position);
            pickableViewHolder.bindView(pickable);
        }

        @Override
        public int getItemCount() {
            return pickables.size();
        }

        class PickableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            AppCompatImageView ivItemValueAvatar;
            AppCompatTextView tvItemValueName;
            AppCompatTextView tvItemValueDescription;

            PickableViewHolder(@NonNull View valueView) {
                super(valueView);
                valueView.setOnClickListener(this);
                ivItemValueAvatar = valueView.findViewById(R.id.ivItemValueAvatar);
                tvItemValueName = valueView.findViewById(R.id.tvItemValueName);
                tvItemValueDescription = valueView.findViewById(R.id.tvItemValueDescription);
            }

            void bindView(Pickable pickable) {
                String name = pickable.getName();
                String description = pickable.getDescription();
                String letter = String.valueOf(name.charAt(0));
                int color = colorFor(pickable);

                // set avatar
                TextDrawable drawable = TextDrawable.builder().buildRound(letter, color);
                ivItemValueAvatar.setImageDrawable(drawable);

                // set name
                tvItemValueName.setText(name);

                //set description
                description = TextUtils.isEmpty(description) ? name : description;
                tvItemValueDescription.setText(description);
            }

            @Override
            public void onClick(View v) {
                Pickable pickable = pickables.get(getAdapterPosition());
                if (listener != null) {
                    listener.onClick(pickable);
                }
            }
        }
    }
}
