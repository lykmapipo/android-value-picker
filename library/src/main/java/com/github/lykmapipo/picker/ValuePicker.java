package com.github.lykmapipo.picker;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
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
    /**
     * Default color generator
     *
     * @since 0.1.0
     */
    private static final ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

    public static synchronized void dialogPickerFor(
            @NonNull FragmentActivity activity,
            @NonNull Provider provider) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        PickableDialogFragment picker = new PickableDialogFragment(provider);
        picker.show(fragmentManager, PickableDialogFragment.TAG);
    }

    public static synchronized void bottomPickerFor(
            @NonNull FragmentActivity activity,
            @NonNull Provider provider) {
    }

    // TODO provide picker for fragments usage
    // TODO handle async value providers

    /**
     * Parse color from pickable
     *
     * @param pickable
     * @return
     */
    private static int colorFor(@NonNull Pickable pickable) {
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

    /**
     * Interface definition for a pickable value
     *
     * @since 0.1.0
     */
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

    /**
     * Interface definition for a callback to be invoked when
     * a {@link Pickable} is clicked.
     *
     * @since 0.1.0
     */
    public interface OnClickListener {
        void onClick(Pickable pickable);
    }

    /**
     * Interface definition for {@link Pickable} provider
     *
     * @since 0.1.0
     */
    public interface Provider {
        /**
         * {@link Pickable} header
         *
         * @return
         */
        String getTitle();

        /**
         * {@link Pickable} search hint
         *
         * @return
         */
        String getSearchHint();

        /**
         * {@link Pickable} values
         *
         * @return
         */
        List<? extends Pickable> getValues();

        /**
         * {@link Pickable} selection listener
         *
         * @param pickable
         */
        void onValueSelected(Pickable pickable);

        // TODO add searchValues(String searchTerm)
    }

    /**
     * A {@link androidx.recyclerview.widget.RecyclerView.Adapter} for {@link Pickable} values
     *
     * @since 0.1.0
     */
    static class PickableAdapter extends RecyclerView.Adapter<PickableAdapter.PickableViewHolder> {
        private List<? extends Pickable> pickables;
        private OnClickListener listener;

        public PickableAdapter(@NonNull List<? extends Pickable> pickables, @NonNull OnClickListener listener) {
            this.pickables = pickables;
            this.listener = listener;
        }

        @NonNull
        @Override
        public PickableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View valueView = inflater.inflate(R.layout.pickable_item, parent, false);
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

        /**
         * A ViewHolder for a {@link Pickable}
         */
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

    /**
     * {@link DialogFragment} for {@link Pickable} values
     */
    static class PickableDialogFragment extends DialogFragment implements OnClickListener {
        public static final String TAG = PickableDialogFragment.class.getSimpleName();

        private AppCompatEditText etListValuesSearch;
        private RecyclerView rvListValues;
        private Provider provider;

        public PickableDialogFragment(@NonNull Provider provider) {
            this.provider = provider;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.pickable_list, container, false);
            etListValuesSearch = view.findViewById(R.id.etListValuesSearch);
            rvListValues = view.findViewById(R.id.rvListValues);
            return view;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            // TODO set title
            // TODO bind search listener

            // bind recycler adapter & values
            // TODO obtain value copy and handle emptiness
            PickableAdapter adapter = new PickableAdapter(provider.getValues(), this);
            rvListValues.setAdapter(adapter);
        }

        @Override
        public void onStart() {
            super.onStart();
            try {
                Window window = getDialog().getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            } catch (Exception e) {
                // ignore
            }
        }

        @Override
        public void onClick(Pickable pickable) {
            dismiss();
            if (provider != null) {
                provider.onValueSelected(pickable);
            }
        }
    }
}
