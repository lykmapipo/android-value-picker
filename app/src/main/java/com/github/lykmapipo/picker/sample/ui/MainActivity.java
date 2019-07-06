package com.github.lykmapipo.picker.sample.ui;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.lykmapipo.picker.sample.R;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // show dialog value picker
        Button btnDialogPicker = findViewById(R.id.btnDialogPicker);
        btnDialogPicker.setOnClickListener(v -> {
        });

        // show bottom sheet value picker
        Button btnBottomSheetPicker = findViewById(R.id.btnBottomSheetPicker);
        btnBottomSheetPicker.setOnClickListener(v -> {
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private String format(@NonNull String label, @NonNull double value) {
        return String.format(Locale.ENGLISH, "%s: %f", label, value);
    }
}
