package com.example.miprimerproyectoandroid;

import android.app.Activity;
import android.app.LocaleManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Locale;

public final class MainActivity extends Activity {
    private static final String PREFERENCES = "app_preferences";
    private static final String LANGUAGE_KEY = "language";
    private String selectedLanguage;

    @Override
    protected void attachBaseContext(Context base) {
        SharedPreferences preferences = base.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        Configuration configuration = new Configuration(base.getResources().getConfiguration());
        Locale locale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            LocaleList applicationLocales = base.getSystemService(LocaleManager.class)
                    .getApplicationLocales();
            if (!applicationLocales.isEmpty()) {
                configuration.setLocales(applicationLocales);
                locale = applicationLocales.get(0);
            } else if (!preferences.contains(LANGUAGE_KEY)) {
                locale = Locale.forLanguageTag("es");
                configuration.setLocale(locale);
            } else {
                super.attachBaseContext(base);
                return;
            }
        } else {
            String language = preferences.getString(LANGUAGE_KEY, "es");
            locale = Locale.forLanguageTag(language);
            configuration.setLocale(locale);
        }

        Locale.setDefault(locale);
        super.attachBaseContext(base.createConfigurationContext(configuration));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        if (!preferences.contains(LANGUAGE_KEY)) {
            preferences.edit().putString(LANGUAGE_KEY, "es").apply();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getSystemService(LocaleManager.class)
                        .setApplicationLocales(LocaleList.forLanguageTags("es"));
            }
        }

        selectedLanguage = preferences.getString(LANGUAGE_KEY, "es");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            LocaleList applicationLocales = getSystemService(LocaleManager.class)
                    .getApplicationLocales();
            if (!applicationLocales.isEmpty()) {
                selectedLanguage = applicationLocales.get(0).getLanguage();
            } else {
                selectedLanguage = getResources().getConfiguration()
                        .getLocales().get(0).getLanguage();
            }
            preferences.edit().putString(LANGUAGE_KEY, selectedLanguage).apply();
        }
        setContentView(R.layout.activity_main);

        Button enterButton = findViewById(R.id.enter_button);
        enterButton.setOnClickListener(view ->
                Toast.makeText(this, R.string.entry_confirmation, Toast.LENGTH_SHORT).show());

        Spinner languageSelector = findViewById(R.id.language_selector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.language_names, R.layout.language_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSelector.setAdapter(adapter);

        String[] languageTags = getResources().getStringArray(R.array.language_tags);
        int selectedPosition = Arrays.asList(languageTags).indexOf(selectedLanguage);
        languageSelector.setSelection(Math.max(selectedPosition, 0), false);
        languageSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String language = languageTags[position];
                if (language.equals(selectedLanguage)) {
                    return;
                }

                selectedLanguage = language;
                preferences.edit().putString(LANGUAGE_KEY, language).apply();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getSystemService(LocaleManager.class)
                            .setApplicationLocales(LocaleList.forLanguageTags(language));
                } else {
                    recreate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
