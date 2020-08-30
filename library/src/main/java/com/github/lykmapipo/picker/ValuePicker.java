package com.github.lykmapipo.picker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.lykmapipo.common.Common;
import com.github.lykmapipo.common.data.Diffable;
import com.github.lykmapipo.common.data.Query;
import com.github.lykmapipo.common.widget.recyclerview.DiffableListAdapter;
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
        // TODO: support empty state icon, title and description
        dialogPickerFor(fragment.requireActivity(), provider);
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
        // TODO: support empty state icon, title and description
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
        bottomPickerFor(fragment.requireActivity(), provider);
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
     * Interface definition for a pickable value
     *
     * @since 0.1.0
     */
    public interface Pickable extends Diffable {
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
    public interface Provider<T extends Pickable> {
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
        @NonNull
        Task<List<T>> getValues(@NonNull Query query);

        /**
         * {@link Pickable} selection listener
         *
         * @param pickable
         */
        @NonNull
        void onValueSelected(T pickable);
    }

    /**
     * A {@link DiffableListAdapter} for {@link Pickable} values
     *
     * @since 0.1.0
     */
    public static class PickableAdapter extends DiffableListAdapter<Pickable, PickableAdapter.PickableViewHolder> {
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
                Integer color = Common.Colors.parseColor(pickable.getColor());

                // set avatar
                TextDrawable drawable = Common.Drawables.letterAvatarFor(letter, color);
                ivItemValueAvatar.setImageDrawable(drawable);

                // set name
                tvItemValueName.setText(name);

                //set description
                description = Common.Strings.valueOr(description, name);
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
    public static class PickableDialogFragment extends DialogFragment
            implements OnClickListener {
        public static final String TAG = PickableDialogFragment.class.getSimpleName();

        private StateLayout slPickableList;
        private SearchView svPickableListSearch;
        private AppCompatTextView etPickableListTitle;
        private RecyclerView rvPickableListValues;
        private Query query = Query.create();
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
            // inflate
            View view = inflater.inflate(R.layout.list_pickable, container, false);

            // setup state layout
            slPickableList = view.findViewById(R.id.slPickableList);

            // obtain required views
            svPickableListSearch = view.findViewById(R.id.svPickableListSearch);
            rvPickableListValues = view.findViewById(R.id.content_pickable);
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

            // set search hint
            String searchHint = provider.getSearchHint();
            if (Common.Strings.isEmpty(searchHint)) {
                searchHint = getString(R.string.hint_list_values_search);
            }
            svPickableListSearch.setQueryHint(searchHint);

            // Set SearchView QueryTextListener
            svPickableListSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String q) {
                    dismissKeyboard(svPickableListSearch);
                    query = Common.Strings.isEmpty(q) ? Query.create() : Query.create(q);
                    search();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String q) {
                    query = Common.Strings.isEmpty(q) ? Query.create() : Query.create(q);
                    search();
                    return true;
                }
            });

            // Set focus on the SearchView and open the keyboard
            svPickableListSearch.setOnQueryTextFocusChangeListener((view, hasFocus) -> {
                if (hasFocus) {
                    showKeyboard(view.findFocus());
                }
            });

            // set title
            String title = provider.getTitle();
            if (Common.Strings.isEmpty(title)) {
                title = getString(R.string.text_pickable_list_title);
            }
            etPickableListTitle.setText(title);

            // bind recycler adapter & values
            search();
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
        public void onPause() {
            dismissKeyboard(svPickableListSearch);
            super.onPause();
        }

        @Override
        public void onClick(Pickable pickable) {
            dismissKeyboard(svPickableListSearch);
            dismiss();
            if (provider != null) {
                provider.onValueSelected(pickable);
            }
        }

        private void search() {
            // show loading
            slPickableList.showLoading();

            // bind recycler adapter & values
            Task<List<Pickable>> task = provider.getValues(query);

            // handle loading states
            task.addOnSuccessListener(requireActivity(), pickables -> {
                if (pickables.isEmpty()) {
                    slPickableList.showEmpty();
                } else {
                    adapter.submitList(pickables);
                    slPickableList.showContent();
                }
            });

            // handle load error
            task.addOnFailureListener(requireActivity(), e -> slPickableList.showError());
        }

        public void setProvider(Provider provider) {
            this.provider = provider;
        }

        private void showKeyboard(View view) {
            InputMethodManager imm =
                    (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }

        private void dismissKeyboard(View view) {
            InputMethodManager imm =
                    (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * {@link BottomSheetDialogFragment} for {@link Pickable} values
     */
    public static class PickableBottomSheetDialogFragment extends BottomSheetDialogFragment
            implements OnClickListener {
        public static final String TAG = PickableBottomSheetDialogFragment.class.getSimpleName();

        private StateLayout slPickableList;
        private SearchView svPickableListSearch;
        private AppCompatTextView etPickableListTitle;
        private RecyclerView rvPickableListValues;
        private Query query = Query.create();
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
        public View onCreateView(
                @NonNull LayoutInflater inflater,
                @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            // inflate
            View view = inflater.inflate(R.layout.list_pickable, container, false);

            // setup state layout
            slPickableList = view.findViewById(R.id.slPickableList);

            // obtain required views
            svPickableListSearch = view.findViewById(R.id.svPickableListSearch);
            rvPickableListValues = view.findViewById(R.id.content_pickable);
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

            // set search hint
            String searchHint = provider.getSearchHint();
            if (Common.Strings.isEmpty(searchHint)) {
                searchHint = getString(R.string.hint_list_values_search);
            }
            svPickableListSearch.setQueryHint(searchHint);

            // Set SearchView QueryTextListener
            svPickableListSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String q) {
                    dismissKeyboard(svPickableListSearch);
                    query = Common.Strings.isEmpty(q) ? Query.create() : Query.create(q);
                    search();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String q) {
                    query = Common.Strings.isEmpty(q) ? Query.create() : Query.create(q);
                    search();
                    return true;
                }
            });

            // Set focus on the SearchView and open the keyboard
            svPickableListSearch.setOnQueryTextFocusChangeListener((view, hasFocus) -> {
                if (hasFocus) {
                    showKeyboard(view.findFocus());
                }
            });

            // set title
            String title = provider.getTitle();
            if (Common.Strings.isEmpty(title)) {
                title = getString(R.string.text_pickable_list_title);
            }
            etPickableListTitle.setText(title);

            // bind recycler adapter & values
            search();
        }

        @Override
        public void onPause() {
            dismissKeyboard(svPickableListSearch);
            super.onPause();
        }

        @Override
        public void onClick(Pickable pickable) {
            dismissKeyboard(svPickableListSearch);
            dismiss();
            if (provider != null) {
                provider.onValueSelected(pickable);
            }
        }

        private void search() {
            // show loading
            slPickableList.showLoading();

            // bind recycler adapter & values
            Task<List<Pickable>> task = provider.getValues(query);

            // handle loading states
            task.addOnSuccessListener(requireActivity(), pickables -> {
                if (pickables.isEmpty()) {
                    slPickableList.showEmpty();
                } else {
                    adapter.submitList(pickables);
                    slPickableList.showContent();
                }
            });

            // handle load error
            task.addOnFailureListener(requireActivity(), e -> slPickableList.showError());
        }

        public void setProvider(Provider provider) {
            this.provider = provider;
        }

        private void showKeyboard(View view) {
            InputMethodManager imm =
                    (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }

        private void dismissKeyboard(View view) {
            InputMethodManager imm =
                    (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

    }
}
