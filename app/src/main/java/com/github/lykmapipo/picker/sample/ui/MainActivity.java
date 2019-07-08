package com.github.lykmapipo.picker.sample.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.javafaker.Faker;
import com.github.lykmapipo.picker.ValuePicker;
import com.github.lykmapipo.picker.sample.R;

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
        btnDialogPicker.setOnClickListener(v -> ValuePicker.dialogPickerFor(this, new ValuePicker.Provider() {
            @Override
            public String getTitle() {
                return "Select Contact";
            }

            @Override
            public String getSearchHint() {
                return "Search...";
            }

            @Override
            public List<? extends ValuePicker.Pickable> getValues() {
                return getContactList();
            }

            @Override
            public void onValueSelected(ValuePicker.Pickable pickable) {
                Toast.makeText(MainActivity.this, "Picked: " + pickable, Toast.LENGTH_SHORT).show();
            }
        }));

        // show bottom sheet value picker
        Button btnBottomSheetPicker = findViewById(R.id.btnBottomSheetPicker);
        btnBottomSheetPicker.setOnClickListener(v -> {
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private List<Contact> getContactList() {
        List<Contact> contacts = new ArrayList<Contact>();

        for (int i = 0; i < 10; i++) {
            Faker faker = new Faker();
            String name = faker.name().fullName();
            String phone = faker.phoneNumber().phoneNumber();
            Contact contact = new Contact(name, phone);
            contacts.add(contact);
        }

        return contacts;
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
        public String getId() {
            return phone;
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
    }
}
