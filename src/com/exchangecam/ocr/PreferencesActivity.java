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


public class PreferencesActivity extends PreferenceActivity implements
                OnSharedPreferenceChangeListener {

        // Exchange Preferences
        public static final String KEY_SOURCE_CURRENCY_PREFERENCE = "sourceCurrencyCodeExchangePref";
        public static final String KEY_TARGET_CURRENCY_PREFERENCE = "targetCurrencyCodeExchangePref";
        public static final String KEY_AUTO_EXCHANGE_RATE_PREFERENCE = "toggleAutoExchangeRateExchangePref";
        public static final String KEY_EXCHANGE_RATE_PREFERENCE = "exchangeRateExchangePref";
        public static final String KEY_HELP_VERSION_SHOWN = "preferences_help_version_shown";
        public static final String KEY_NOT_OUR_RESULTS_SHOWN = "preferences_not_our_results_shown";
        public static final String KEY_REVERSE_IMAGE = "preferences_reverse_image";
        public static final String KEY_VIBRATE = "preferences_vibrate";

        private ListPreference listPreferenceSourceCurrency;
        private ListPreference listPreferenceTargetCurrency;
        private EditTextPreference editTextPreferenceExchangeRate;
        private CheckBoxPreference checkBoxPreferenceAutoExchangeRate;

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
        	if (key.equals(KEY_SOURCE_CURRENCY_PREFERENCE)) {
                        // Set the summary text for the source currency preference
                        listPreferenceSourceCurrency.setSummary(sharedPreferences
                                        .getString(key, CaptureActivity.DEFAULT_SOURCE_CURRENCY));
                        
                        
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
        }

        /**
         * Sets up initial preference summary text values and registers the
         * OnSharedPreferenceChangeListener.
         */
        @Override
        protected void onResume() {
                super.onResume();
                
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

}