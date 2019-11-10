android-value-picker
====================

[![](https://jitpack.io/v/lykmapipo/android-value-picker.svg)](https://jitpack.io/#lykmapipo/android-value-picker)

A pack of helpful helpers and uis to pick value from a given list of values.

## Installation
Add [https://jitpack.io](https://jitpack.io) to your build.gradle with:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
add `android-value-picker` dependency into your project

```gradle
dependencies {
    implementation 'com.github.lykmapipo:android-value-picker:v0.5.0'
}
```

## Usage

In activity(or other component) request for dialog picker

```java
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
                return "Search...";
            }

            @Override
            public Task<List<Contact>> getValues() {
                return getContactList();
            }

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
}
```


## Test
```sh
./gradlew test
```

## Contribute
It will be nice, if you open an issue first so that we can know what is going on, then, fork this repo and push in your ideas.
Do not forget to add a bit of test(s) of what value you adding.

## License

(The MIT License)

Copyright (c) lykmapipo && Contributors

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
'Software'), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
