package com.github.lykmapipo.picker.sample.ui;

import android.os.Bundle;
import android.os.Process;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.javafaker.Faker;
import com.github.lykmapipo.common.data.Query;
import com.github.lykmapipo.picker.ValuePicker;
import com.github.lykmapipo.picker.sample.R;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // show dialog value picker
        Button btnDialogPicker = findViewById(R.id.btnDialogPicker);
        btnDialogPicker.setOnClickListener(v -> ValuePicker.dialogPickerFor(this, new ValuePicker.Provider<Contact>() {
            @Override
            public String getTitle() {
                return "Select Contact";
            }

            @Override
            public String getSearchHint() {
                return "Search Contacts...";
            }

            @NonNull
            @Override
            public Task<List<Contact>> getValues(@NonNull Query query) {
                return getContactList();
            }

            @NonNull
            @Override
            public void onValueSelected(Contact picked) {
                Toast.makeText(MainActivity.this, "Picked: " + picked, Toast.LENGTH_SHORT).show();
            }
        }));

        // show bottom sheet value picker
        Button btnBottomSheetPicker = findViewById(R.id.btnBottomSheetPicker);
        btnBottomSheetPicker.setOnClickListener(v -> ValuePicker.bottomPickerFor(this, new ValuePicker.Provider<Contact>() {
            @Override
            public String getTitle() {
                return "Select Contact";
            }

            @Override
            public String getSearchHint() {
                return "Search Contacts...";
            }

            @NonNull
            @Override
            public Task<List<Contact>> getValues(@NonNull Query query) {
                return getContactList();
            }

            @NonNull
            @Override
            public void onValueSelected(Contact picked) {
                Toast.makeText(MainActivity.this, "Picked: " + picked, Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private Task<List<Contact>> getContactList() {
        final TaskCompletionSource<List<Contact>> source =
                new TaskCompletionSource<List<Contact>>();

        Thread fetch = new Thread(() -> {
            // moves the current Thread into the background
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            // execute task
            try {
                List<Contact> contacts = new ArrayList<Contact>();

                Faker faker = Faker.instance();
                for (int i = 0; i < 10; i++) {
                    String name = faker.name().fullName();
                    String phone = faker.phoneNumber().phoneNumber();
                    Contact contact = new Contact(name, phone);
                    contacts.add(contact);
                }
                source.setResult(contacts);
            } catch (Exception error) {
                source.setException(error);
            }
        });
        fetch.start();

        // create get value task
        Task<List<Contact>> task = source.getTask();
        return task;
    }

    class Contact implements ValuePicker.Pickable {
        String name;
        String phone;

        public Contact(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }

        @NonNull
        @Override
        public String getName() {
            return name;
        }

        @Nullable
        @Override
        public String getDescription() {
            return phone;
        }

        @Nullable
        @Override
        public String getColor() {
            return null;
        }

        @Override
        public String toString() {
            return "Contact{" +
                    "name='" + name + '\'' +
                    ", phone='" + phone + '\'' +
                    '}';
        }

        /**
         * Obtain valid stable id for the diffable item
         *
         * @return object id
         * @since 0.1.0
         */
        @NonNull
        @Override
        public String getObjectId() {
            return phone;
        }
    }
}
