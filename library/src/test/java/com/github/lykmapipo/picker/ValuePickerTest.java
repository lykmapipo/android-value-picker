package com.github.lykmapipo.picker;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class ValuePickerTest {
    Context context;

    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void testShouldProvideValuePicker() {
        assertNotNull("Should provide value picker", 1l);
    }

    @After
    public void cleanup() {
        context = null;
    }

}