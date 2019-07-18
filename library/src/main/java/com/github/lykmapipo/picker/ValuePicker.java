package com.github.lykmapipo.picker;

import android.app.Dialog;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.github.lykmapipo.listview.adapter.DiffableListAdapter;
import com.github.lykmapipo.listview.data.Diffable;
import com.github.lykmapipo.listview.view.StateLayout;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

/**
 * A pack of helpful helpers and uis to pick value from a given list of values.
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public class ValuePicker {
    // TODO explore material dialog layout to improve UI/UX

    /**
     * Default color generator
     *
     * @since 0.1.0
     */
    private static final ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

    /**
     * Launch dialog picker
     *
     * @param fragment
     * @param provider
     * @since 0.1.0
     */
    public static synchronized void dialogPickerFor(
            @NonNull Fragment fragment,
            @NonNull Provider provider) {
        dialogPickerFor(fragment.getActivity(), provider);
    }

    /**
     * Launch dialog picker
     *
     * @param activity
     * @param provider
     * @since 0.1.0
     */
    public static synchronized void dialogPickerFor(
            @NonNull FragmentActivity activity,
            @NonNull Provider provider) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        PickableDialogFragment picker =
                (PickableDialogFragment) fragmentManager.findFragmentByTag(PickableDialogFragment.TAG);
        if (null == picker) {
            picker = PickableDialogFragment.newInstance();
        }
        picker.setProvider(provider);
        picker.show(fragmentManager, PickableDialogFragment.TAG);
    }

    /**
     * Launch bottom sheet picker
     *
     * @param fragment
     * @param provider
     * @since 0.1.0
     */
    public static synchronized void bottomPickerFor(
            @NonNull Fragment fragment,
            @NonNull Provider provider) {
        bottomPickerFor(fragment.getActivity(), provider);
    }

    /**
     * Launch bottom sheet picker
     *
     * @param activity
     * @param provider
     * @since 0.1.0
     */
    public static synchronized void bottomPickerFor(
            @NonNull FragmentActivity activity,
            @NonNull Provider provider) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        PickableBottomSheetDialogFragment picker =
                (PickableBottomSheetDialogFragment) fragmentManager.findFragmentByTag(PickableBottomSheetDialogFragment.TAG);
        if (null == picker) {
            picker = PickableBottomSheetDialogFragment.newInstance();
        }
        picker.setProvider(provider);
        picker.show(fragmentManager, PickableBottomSheetDialogFragment.TAG);
    }

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
    public interface Pickable extends Diffable {
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
        Task<List<? extends Pickable>> getValues(); // TODO use Task<List<? extends Pickable>> load(Query query) with search and paging

        /**
         * {@link Pickable} selection listener
         *
         * @param pickable
         */
        void onValueSelected(Pickable pickable);
    }

    /**
     * A {@link DiffableListAdapter} for {@link Pickable} values
     *
     * @since 0.1.0
     */
    static class PickableAdapter extends DiffableListAdapter<Pickable, PickableAdapter.PickableViewHolder> {
        private OnClickListener listener;

        public PickableAdapter(@NonNull OnClickListener listener) {
            super();
            this.listener = listener;
        }

        @NonNull
        @Override
        public PickableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View valueView = inflater.inflate(R.layout.item_pickable, parent, false);
            return new PickableViewHolder(valueView);
        }

        @Override
        public void onBindViewHolder(@NonNull PickableViewHolder pickableViewHolder, int position) {
            Pickable pickable = getItem(position);
            pickableViewHolder.bind(pickable);
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
                ivItemValueAvatar = valueView.findViewById(R.id.ivPickableItemAvatar);
                tvItemValueName = valueView.findViewById(R.id.tvPickableItemName);
                tvItemValueDescription = valueView.findViewById(R.id.tvPickableItemDescription);
            }

            void bind(Pickable pickable) {
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
                Pickable pickable = getItem(getAdapterPosition());
                if (listener != null) {
                    listener.onClick(pickable);
                }
            }
        }
    }

    /**
     * {@link DialogFragment} for {@link Pickable} values
     */
    static class PickableDialogFragment extends DialogFragment
            implements OnClickListener {
        public static final String TAG = PickableDialogFragment.class.getSimpleName();

        private StateLayout llPickableList;
        private AppCompatEditText etPickableListSearch;
        private AppCompatTextView etPickableListTitle;
        private RecyclerView rvPickableListValues;
        private Provider provider;
        private PickableAdapter adapter;

        public PickableDialogFragment() {
            setRetainInstance(true);
        }

        /**
         * Instantiate new dialog picker in not exists
         *
         * @return
         * @since 0.1.0
         */
        public static PickableDialogFragment newInstance() {
            // TODO improve picker intent
            final Bundle args = new Bundle();
            args.getString("ID", PickableDialogFragment.TAG);
            final PickableDialogFragment fragment = new PickableDialogFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.list_pickable, container, false);
            // setup state layout
            llPickableList = (StateLayout) view;

            // obtain required views
            etPickableListSearch = view.findViewById(R.id.etPickableListSearch);
            rvPickableListValues = view.findViewById(R.id.rvPickableListValues);
            etPickableListTitle = view.findViewById(R.id.etPickableListTitle);

            // bind adapters
            adapter = new PickableAdapter(this);
            rvPickableListValues.setAdapter(adapter);

            // return views
            return view;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            // TODO bind search listener

            // set title
            String title = provider.getTitle();
            if (TextUtils.isEmpty(title)) {
                title = getString(R.string.text_pickable_list_title);
            }
            etPickableListTitle.setText(title);

            // bind recycler adapter & values
            Task<List<? extends Pickable>> task = provider.getValues();

            // handle loading states
            task.addOnSuccessListener(getActivity(), pickables -> {
                if (pickables.isEmpty()) {
                    llPickableList.showEmpty();
                } else {
                    adapter.submitList((List<Pickable>) pickables);
                    llPickableList.showContent();
                }
            });

            // handle load error
            task.addOnFailureListener(getActivity(), e -> llPickableList.showError());
        }

        @Override
        public void onStart() {
            super.onStart();
            try {
                Dialog dialog = getDialog();
                Window window = dialog.getWindow();
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

        public void setProvider(Provider provider) {
            this.provider = provider;
        }
    }

    /**
     * {@link BottomSheetDialogFragment} for {@link Pickable} values
     */
    static class PickableBottomSheetDialogFragment extends BottomSheetDialogFragment
            implements OnClickListener {
        public static final String TAG = PickableBottomSheetDialogFragment.class.getSimpleName();

        private StateLayout llPickableList;
        private AppCompatEditText etPickableListSearch;
        private AppCompatTextView etPickableListTitle;
        private RecyclerView rvPickableListValues;
        private Provider provider;
        private PickableAdapter adapter;

        public PickableBottomSheetDialogFragment() {
            setRetainInstance(true);
        }

        public PickableBottomSheetDialogFragment(@NonNull Provider provider) {
            this.provider = provider;
        }

        /**
         * Instantiate new bottom sheet picker in not exists
         *
         * @return
         * @since 0.1.0
         */
        public static PickableBottomSheetDialogFragment newInstance() {
            // TODO improve picker intent
            final Bundle args = new Bundle();
            args.getString("ID", PickableBottomSheetDialogFragment.TAG);
            final PickableBottomSheetDialogFragment fragment = new PickableBottomSheetDialogFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.list_pickable, container, false);
            // setup state layout
            llPickableList = (StateLayout) view;

            // obtain required views
            etPickableListSearch = view.findViewById(R.id.etPickableListSearch);
            rvPickableListValues = view.findViewById(R.id.rvPickableListValues);
            etPickableListTitle = view.findViewById(R.id.etPickableListTitle);

            // bind adapters
            adapter = new PickableAdapter(this);
            rvPickableListValues.setAdapter(adapter);

            // return view
            return view;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            // TODO bind search listener

            // set title
            String title = provider.getTitle();
            if (TextUtils.isEmpty(title)) {
                title = getString(R.string.text_pickable_list_title);
            }
            etPickableListTitle.setText(title);

            // bind recycler adapter & values
            Task<List<? extends Pickable>> task = provider.getValues();

            // handle loading states
            task.addOnSuccessListener(getActivity(), pickables -> {
                if (pickables.isEmpty()) {
                    llPickableList.showEmpty();
                } else {
                    adapter.submitList((List<Pickable>) pickables);
                    llPickableList.showContent();
                }
            });

            // handle load error
            task.addOnFailureListener(getActivity(), e -> llPickableList.showError());
        }

        @Override
        public void onClick(Pickable pickable) {
            dismiss();
            if (provider != null) {
                provider.onValueSelected(pickable);
            }
        }

        public void setProvider(Provider provider) {
            this.provider = provider;
        }

    }
}
