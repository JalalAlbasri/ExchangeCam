/*
 * Copyright (C) 2008 ZXing authors
 * Copyright 2011 Robert Theis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exchangecam.ocr;

import com.exchangecam.ocr.currency.QueryConversionRateAysncTask;
//import com.exchangecam.language.LanguageCodeHelper;
//import com.exchangecam.language.TranslatorBing;
//import com.exchangecam.language.TranslatorGoogle;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.PreferenceCategory;

/**
 * Class to handle preferences that are saved across sessions of the app. Shows
 * a hierarchy of preferences to the user, organized into sections. These
 * preferences are displayed in the options menu that is shown when the user
 * presses the MENU button.
 * 
 * The code for this class was adapted from the ZXing project:
 * http://code.google.com/p/zxing
 */
public class PreferencesActivity extends PreferenceActivity implements
                OnSharedPreferenceChangeListener {

        // Exchange Preferences
        public static final String KEY_SOURCE_CURRENCY_PREFERENCE = "sourceCurrencyCodeExchangePref";
        public static final String KEY_TARGET_CURRENCY_PREFERENCE = "targetCurrencyCodeExchangePref";
        public static final String KEY_AUTO_EXCHANGE_RATE_PREFERENCE = "toggleAutoExchangeRateExchangePref";
        public static final String KEY_EXCHANGE_RATE_PREFERENCE = "exchangeRateExchangePref";

        // Preference keys not carried over from ZXing project
//        public static final String KEY_SOURCE_LANGUAGE_PREFERENCE = "sourceLanguageCodeOcrPref";
//        public static final String KEY_TARGET_LANGUAGE_PREFERENCE = "targetLanguageCodeTranslationPref";
//        public static final String KEY_TOGGLE_TRANSLATION = "preference_translation_toggle_translation";

//        public static final String KEY_CONTINUOUS_PREVIEW = "preference_capture_continuous";
//        public static final String KEY_PAGE_SEGMENTATION_MODE = "preference_page_segmentation_mode";
//        public static final String KEY_OCR_ENGINE_MODE = "preference_ocr_engine_mode";
        
//        public static final String KEY_CHARACTER_BLACKLIST = "preference_character_blacklist";
//        public static final String KEY_CHARACTER_WHITELIST = "preference_character_whitelist";
//        public static final String KEY_TOGGLE_LIGHT = "preference_toggle_light";
//        public static final String KEY_TRANSLATOR = "preference_translator";

        // Preference keys carried over from ZXing project
//        public static final String KEY_AUTO_FOCUS = "preferences_auto_focus";
        public static final String KEY_HELP_VERSION_SHOWN = "preferences_help_version_shown";
        public static final String KEY_NOT_OUR_RESULTS_SHOWN = "preferences_not_our_results_shown";
        public static final String KEY_REVERSE_IMAGE = "preferences_reverse_image";
//        public static final String KEY_PLAY_BEEP = "preferences_play_beep";
        public static final String KEY_VIBRATE = "preferences_vibrate";

//        public static final String TRANSLATOR_BING = "Bing Translator";
//        public static final String TRANSLATOR_GOOGLE = "Google Translate";

        private ListPreference listPreferenceSourceCurrency;
        private ListPreference listPreferenceTargetCurrency;
        private EditTextPreference editTextPreferenceExchangeRate;
        private CheckBoxPreference checkBoxPreferenceAutoExchangeRate;
//        private CheckBoxPreference checkBoxPreferenceContinuousPreview;
//        private ListPreference listPreferenceSourceLanguage;
        // private CheckBoxPreference checkBoxTranslate;
//        private ListPreference listPreferenceTargetLanguage;
//        private ListPreference listPreferenceTranslator;
//        private ListPreference listPreferenceOcrEngineMode;
        // private CheckBoxPreference checkBoxBeep;
//        private EditTextPreference editTextPreferenceCharacterBlacklist;
//        private EditTextPreference editTextPreferenceCharacterWhitelist;
//        private ListPreference listPreferencePageSegmentationMode;
//        // private CheckBoxPreference checkBoxReversedImage;

        private static SharedPreferences sharedPreferences;

        /**
         * Set the default preference values.
         * 
         * @param Bundle
         *            savedInstanceState the current Activity's state, as passed by
         *            Android
         */
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                
                addPreferencesFromResource(R.xml.preferences);

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

                listPreferenceSourceCurrency = (ListPreference) getPreferenceScreen()
                                .findPreference(KEY_SOURCE_CURRENCY_PREFERENCE);
                listPreferenceTargetCurrency = (ListPreference) getPreferenceScreen()
                                .findPreference(KEY_TARGET_CURRENCY_PREFERENCE);
                editTextPreferenceExchangeRate = (EditTextPreference) getPreferenceScreen()
                                .findPreference(KEY_EXCHANGE_RATE_PREFERENCE);
                checkBoxPreferenceAutoExchangeRate = (CheckBoxPreference) getPreferenceScreen()
                		.findPreference(KEY_AUTO_EXCHANGE_RATE_PREFERENCE);
//                checkBoxPreferenceContinuousPreview = (CheckBoxPreference) getPreferenceScreen().findPreference(KEY_CONTINUOUS_PREVIEW);
//                PreferenceCategory generalPreferences = (PreferenceCategory) findPreference("General settings");
//                generalPreferences.removePreference(checkBoxPreferenceContinuousPreview);
//                listPreferenceSourceLanguage = (ListPreference) getPreferenceScreen()
//                                .findPreference(KEY_SOURCE_LANGUAGE_PREFERENCE);
                // checkBoxTranslate = (CheckBoxPreference)
                // getPreferenceScreen().findPreference(KEY_TOGGLE_TRANSLATION);
//                listPreferenceTargetLanguage = (ListPreference) getPreferenceScreen()
//                                .findPreference(KEY_TARGET_LANGUAGE_PREFERENCE);
//                listPreferenceTranslator = (ListPreference) getPreferenceScreen()
//                                .findPreference(KEY_TRANSLATOR);
//                listPreferenceOcrEngineMode = (ListPreference) getPreferenceScreen()
//                                .findPreference(KEY_OCR_ENGINE_MODE);

                // checkBoxBeep = (CheckBoxPreference)
                // getPreferenceScreen().findPreference(KEY_PLAY_BEEP);
//                editTextPreferenceCharacterBlacklist = (EditTextPreference) getPreferenceScreen()
//                                .findPreference(KEY_CHARACTER_BLACKLIST);
//
//                editTextPreferenceCharacterWhitelist = (EditTextPreference) getPreferenceScreen()
//                                .findPreference(KEY_CHARACTER_WHITELIST);

//                listPreferencePageSegmentationMode = (ListPreference) getPreferenceScreen()
//                                .findPreference(KEY_PAGE_SEGMENTATION_MODE);

//                removePreferences();
                // checkBoxReversedImage = (CheckBoxPreference)
                // getPreferenceScreen().findPreference(KEY_REVERSE_IMAGE);

                // Create the entries/entryvalues for the translation target language
                // list.
//                initTranslationTargetList();

        }

        /**
         * Interface definition for a callback to be invoked when a shared
         * preference is changed. Sets summary text for the app's preferences.
         * Summary text values show the current settings for the values.
         * 
         * @param sharedPreferences
         *            the Android.content.SharedPreferences that received the change
         * @param key
         *            the key of the preference that was changed, added, or removed
         */
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                        String key) {
                // Update preference summary values to show current preferences
//                if (key.equals(KEY_TRANSLATOR)) {
//                        listPreferenceTranslator.setSummary(sharedPreferences.getString(
//                                        key, CaptureActivity.DEFAULT_TRANSLATOR));
//                } else 
//                	if (key.equals(KEY_SOURCE_LANGUAGE_PREFERENCE)) {
//
//                        // Set the summary text for the source language name
//                        listPreferenceSourceLanguage
//                                        .setSummary("foo");
//
//                        // Retrieve the character blacklist/whitelist for the new language
//                        String blacklist = OcrCharacterHelper.getBlacklist(
//                                        sharedPreferences, listPreferenceSourceLanguage.getValue());
//                        String whitelist = OcrCharacterHelper.getWhitelist(
//                                        sharedPreferences, listPreferenceSourceLanguage.getValue());
//
//                        // Save the character blacklist/whitelist to preferences
//                        sharedPreferences.edit()
//                                        .putString(KEY_CHARACTER_BLACKLIST, blacklist).commit();
//                        sharedPreferences.edit()
//                                        .putString(KEY_CHARACTER_WHITELIST, whitelist).commit();
//
//                        // Set the blacklist/whitelist summary text
//                        editTextPreferenceCharacterBlacklist.setSummary(blacklist);
//                        editTextPreferenceCharacterWhitelist.setSummary(whitelist);
//
//                } else if (key.equals(KEY_TARGET_LANGUAGE_PREFERENCE)) {
//                        listPreferenceTargetLanguage.setSummary("foo");
//                } else 
//        		if (key.equals(KEY_PAGE_SEGMENTATION_MODE)) {
//                        listPreferencePageSegmentationMode.setSummary(sharedPreferences
//                                        .getString(key,
//                                                        CaptureActivity.DEFAULT_PAGE_SEGMENTATION_MODE));
//                } else if (key.equals(KEY_OCR_ENGINE_MODE)) {
//                        listPreferenceOcrEngineMode.setSummary(sharedPreferences.getString(
//                                        key, CaptureActivity.DEFAULT_OCR_ENGINE_MODE));
//                } else if (key.equals(KEY_CHARACTER_BLACKLIST)) {
//
//                        // Save a separate, language-specific character blacklist for this
//                        // language
//                        OcrCharacterHelper.setBlacklist(sharedPreferences,
//                                        "eng",
//                                        sharedPreferences.getString(key, OcrCharacterHelper
//                                                        .getDefaultBlacklist("eng")));
//
//                        // Set the summary text
//                        editTextPreferenceCharacterBlacklist.setSummary(sharedPreferences
//                                        .getString(key, OcrCharacterHelper
//                                                        .getDefaultBlacklist("eng")));
//
//                } else if (key.equals(KEY_CHARACTER_WHITELIST)) {
//
//                        // Save a separate, language-specific character blacklist for this
//                        // language
//                        OcrCharacterHelper.setWhitelist(sharedPreferences,
//                        		"eng",
//                                        sharedPreferences.getString(key, OcrCharacterHelper
//                                                        .getDefaultWhitelist("eng")));
//
//                        // Set the summary text
//                        editTextPreferenceCharacterWhitelist.setSummary(sharedPreferences
//                                        .getString(key, OcrCharacterHelper
//                                                        .getDefaultWhitelist("eng")));
//
//                } else 
        	if (key.equals(KEY_SOURCE_CURRENCY_PREFERENCE)) {
                        // Set the summary text for the source currency preference
                        listPreferenceSourceCurrency.setSummary(sharedPreferences
                                        .getString(key, CaptureActivity.DEFAULT_SOURCE_CURRENCY));
                        
                        // Update the exchange rate
                        
                        
                        // add the currency symbol to the character whitelist
                        // remove the previous currency symbol from the character whitelist
                        // Retrieve the character blacklist/whitelist for the new language
//                        String blacklist = OcrCharacterHelper.getBlacklist(
//                                        sharedPreferences, "eng");
//                        String whitelist = OcrCharacterHelper.getWhitelist(
//                                        sharedPreferences, "eng");
//
//                        // Save the character blacklist/whitelist to preferences
//                        sharedPreferences.edit()
//                                        .putString(KEY_CHARACTER_BLACKLIST, blacklist).commit();
//                        sharedPreferences.edit()
//                                        .putString(KEY_CHARACTER_WHITELIST, whitelist).commit();
//
//                        // Set the blacklist/whitelist summary text
//                        editTextPreferenceCharacterBlacklist.setSummary(blacklist);
//                        editTextPreferenceCharacterWhitelist.setSummary(whitelist);

                        
                } else if (key.equals(KEY_TARGET_CURRENCY_PREFERENCE)) {
                        // Set the summary text
                        listPreferenceTargetCurrency.setSummary(sharedPreferences
                                        .getString(key, CaptureActivity.DEFAULT_TARGET_CURRENCY));

                        // update the exchange rate
                } else if (key.equals(KEY_EXCHANGE_RATE_PREFERENCE)) {
                        // Set the summary text
                        editTextPreferenceExchangeRate.setSummary(sharedPreferences
                                        .getString(key, CaptureActivity.DEFAULT_EXCHANGE_RATE));
                }

                // Update the languages available for translation based on the current
                // translator selected.
//                if (key.equals(KEY_TRANSLATOR)) {
//                        initTranslationTargetList();
//                }

        }

//        /**
//         * Sets the list of available languages and the current target language for
//         * translation. Called when the key for the current translator is changed.
//         */
//        void initTranslationTargetList() {
//                // Set the preference for the target language code, in case we've just
//                // switched from Google
//                // to Bing, or Bing to Google.
//                String currentLanguageCode = sharedPreferences.getString(
//                                KEY_TARGET_LANGUAGE_PREFERENCE,
//                                CaptureActivity.DEFAULT_TARGET_LANGUAGE_CODE);
//
//                // Get the name of our language
//                String currentLanguage = LanguageCodeHelper.getTranslationLanguageName(
//                                getBaseContext(), currentLanguageCode);
//                String[] translators = getResources().getStringArray(
//                                R.array.translators);
//                String translator = sharedPreferences.getString(KEY_TRANSLATOR,
//                                CaptureActivity.DEFAULT_TRANSLATOR);
//                String newLanguageCode = "";
//                if (translator.equals(translators[0])) { // Bing
//                        // Update the list of available languages for the currently-chosen
//                        // translation API.
//                        listPreferenceTargetLanguage
//                                        .setEntries(R.array.translationtargetlanguagenames_microsoft);
//                        listPreferenceTargetLanguage
//                                        .setEntryValues(R.array.translationtargetiso6391_microsoft);
//
//                        // Get the corresponding code for our language name
//                        newLanguageCode = TranslatorBing.toLanguage(currentLanguage);
//                } else if (translator.equals(translators[1])) { // Google
//                        // Update the list of available languages for the currently-chosen
//                        // translation API.
//                        listPreferenceTargetLanguage
//                                        .setEntries(R.array.translationtargetlanguagenames_google);
//                        listPreferenceTargetLanguage
//                                        .setEntryValues(R.array.translationtargetiso6391_google);
//
//                        // Get the corresponding code for our language name
//                        newLanguageCode = TranslatorGoogle.toLanguage(currentLanguage);
//                }
//
//                // Store the code as the target language preference
//                String newLanguageName = LanguageCodeHelper.getTranslationLanguageName(
//                                getBaseContext(), newLanguageCode);
//                listPreferenceTargetLanguage.setValue(newLanguageName); // Set the radio
//                                                                                                                                // button in the
//                                                                                                                                // list
//                sharedPreferences
//                                .edit()
//                                .putString(PreferencesActivity.KEY_TARGET_LANGUAGE_PREFERENCE,
//                                                newLanguageCode).commit();
//                listPreferenceTargetLanguage.setSummary(newLanguageName);
//        }

        /**
         * Sets up initial preference summary text values and registers the
         * OnSharedPreferenceChangeListener.
         */
        @Override
        protected void onResume() {
                super.onResume();
                // Set up the initial summary values
//                listPreferenceTranslator.setSummary(sharedPreferences.getString(
//                                KEY_TRANSLATOR, CaptureActivity.DEFAULT_TRANSLATOR));
//                listPreferenceSourceLanguage.setSummary("foo");
//                listPreferenceTargetLanguage.setSummary("foo");
//                listPreferencePageSegmentationMode.setSummary(sharedPreferences
//                                .getString(KEY_PAGE_SEGMENTATION_MODE,
//                                                CaptureActivity.DEFAULT_PAGE_SEGMENTATION_MODE));
//                listPreferenceOcrEngineMode.setSummary(sharedPreferences.getString(
//                                KEY_OCR_ENGINE_MODE, CaptureActivity.DEFAULT_OCR_ENGINE_MODE));
//                editTextPreferenceCharacterBlacklist.setSummary(sharedPreferences
//                                .getString(KEY_CHARACTER_BLACKLIST, OcrCharacterHelper
//                                                .getDefaultBlacklist("eng")));
//                editTextPreferenceCharacterWhitelist.setSummary(sharedPreferences
//                                .getString(KEY_CHARACTER_WHITELIST, OcrCharacterHelper
//                                                .getDefaultWhitelist("eng")));
                listPreferenceSourceCurrency.setSummary(sharedPreferences.getString(
                                KEY_SOURCE_CURRENCY_PREFERENCE,
                                CaptureActivity.DEFAULT_SOURCE_CURRENCY));
                listPreferenceTargetCurrency.setSummary(sharedPreferences.getString(
                                KEY_TARGET_CURRENCY_PREFERENCE,
                                CaptureActivity.DEFAULT_TARGET_CURRENCY));
                editTextPreferenceExchangeRate.setSummary(sharedPreferences.getString(
                                KEY_EXCHANGE_RATE_PREFERENCE, CaptureActivity.DEFAULT_EXCHANGE_RATE));

                // Set up a listener whenever a key changes
                getPreferenceScreen().getSharedPreferences()
                                .registerOnSharedPreferenceChangeListener(this);
//                removePreferences();
        }

        /**
         * Called when Activity is about to lose focus. Unregisters the
         * OnSharedPreferenceChangeListener.
         */
        @Override
        protected void onPause() {
                super.onPause();
                getPreferenceScreen().getSharedPreferences()
                                .unregisterOnSharedPreferenceChangeListener(this);
        }
        
        
//        /*
//         * Does not seems to work
//         */
//        protected void removePreferences(){
//        	PreferenceScreen preferenceScreen = getPreferenceScreen();
//           
//            preferenceScreen.removePreference(listPreferenceOcrEngineMode);
//           
//            preferenceScreen.removePreference(editTextPreferenceCharacterBlacklist);
//           
//            preferenceScreen.removePreference(editTextPreferenceCharacterWhitelist);
//       
//            preferenceScreen.removePreference(listPreferencePageSegmentationMode);
//        }
}