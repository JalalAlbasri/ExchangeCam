package com.exchangecam.ocr;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.util.Log;
import android.graphics.Rect;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.exchangecam.ocr.ExchangeErrorDialogFragment.ExchangeErrorDialogListener;
import com.exchangecam.ocr.currency.CurrencyHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ToggleButton;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;



public class DashboardFragment extends Fragment implements ExchangeErrorDialogListener {
	private static final String TAG = DashboardFragment.class.getSimpleName();
	private CaptureActivity mActivity;

	private ToggleButton mAutoExchangeRateToggleButton;
	private EditText mExchangeRateEditText;
	private Spinner mSourceCurrencySpinner;
	private Spinner mTargetCurrencySpinner;
	private TextView mSourceCurrencyTextView;
	private TextView mTargetCurrencyTextView;
	private String[] mCurrencyCodes;

	String mSourceCurrency;
	String mTargetCurrency;

	private SharedPreferences prefs;


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (CaptureActivity) activity;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Apply Holo theme to the layout
		Context context = new ContextThemeWrapper(mActivity, R.style.dashboardFragmentStyle);
		LayoutInflater holoInflater = inflater.cloneInContext(context);
		View dashboardView = holoInflater.inflate(R.layout.dashboard_land, container, false);

		//Retrieve layout widgets from View
		mAutoExchangeRateToggleButton = (ToggleButton) dashboardView.findViewById(R.id.query_rate_toggle_button);
		mExchangeRateEditText = (EditText) dashboardView.findViewById(R.id.exchange_rate_edit_text);
		mSourceCurrencySpinner = (Spinner) dashboardView.findViewById(R.id.source_currency_spinner);
		mTargetCurrencySpinner = (Spinner) dashboardView.findViewById(R.id.target_currency_spinner);
		mSourceCurrencyTextView = (TextView) dashboardView.findViewById(R.id.source_currency_text_view);
		mTargetCurrencyTextView = (TextView) dashboardView.findViewById(R.id.target_currency_text_view);

		//Register Listeners for widget input events
		mAutoExchangeRateToggleButton.setOnClickListener(mToggleButtonListener);
		mSourceCurrencySpinner.setOnItemSelectedListener(mSourceCurrencySpinnerListener);
		mTargetCurrencySpinner.setOnItemSelectedListener(mTargetCurrencySpinnerListener);

		//Array of currency codes.
		mCurrencyCodes = getResources().getStringArray(R.array.currencycodes);
		//Populate the Spinners with currency codes
		//		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mActivity, R.array.currencycodes, R.layout.currency_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mActivity, R.array.currencynames, R.layout.currency_spinner);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
		mSourceCurrencySpinner.setAdapter(adapter);
		mTargetCurrencySpinner.setAdapter(adapter);

		mSourceCurrencyTextView.setText("0");
		mTargetCurrencyTextView.setText("0");

		retrievePreferences();

		return dashboardView;
	}

	private void retrievePreferences() {
		//Retrieve SharedPreferences
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

		//Set Toggle Button status from preferences
		mAutoExchangeRateToggleButton.setChecked(
				prefs.getBoolean(PreferencesActivity.KEY_AUTO_EXCHANGE_RATE_PREFERENCE, CaptureActivity.DEFAULT_AUTO_EXCHANGE_RATE_PREFERENCE));
		//Retrive excange rate from preferences
		mExchangeRateEditText.setText(
				prefs.getString(PreferencesActivity.KEY_EXCHANGE_RATE_PREFERENCE, CaptureActivity.DEFAULT_EXCHANGE_RATE));
		//Set exchange rate edit text enabled only if auto exchange rate is false
		mExchangeRateEditText.setEnabled(!mAutoExchangeRateToggleButton.isChecked());

		//Set currently selected currencies from preferences
		mSourceCurrency = prefs.getString(PreferencesActivity.KEY_SOURCE_CURRENCY_PREFERENCE, CaptureActivity.DEFAULT_SOURCE_CURRENCY);
		//		ArrayAdapter sourceAdapter = (ArrayAdapter) mSourceCurrencySpinner.getAdapter();
		//		int sourcePos = sourceAdapter.getPosition(mSourceCurrency);
		int sourcePos = Arrays.asList(mCurrencyCodes).indexOf(mSourceCurrency);
		mSourceCurrencySpinner.setSelection(sourcePos);

		mTargetCurrency = prefs.getString(PreferencesActivity.KEY_TARGET_CURRENCY_PREFERENCE, CaptureActivity.DEFAULT_TARGET_CURRENCY);
		//		ArrayAdapter targetAdapter = (ArrayAdapter) mSourceCurrencySpinner.getAdapter();
		//		int targetPos = targetAdapter.getPosition(mTargetCurrency);
		int targetPos = Arrays.asList(mCurrencyCodes).indexOf(mTargetCurrency);
		mTargetCurrencySpinner.setSelection(targetPos);
	}

	public void updateExchangeRateEditText() {
		//Retrive excange rate from preferences
		mExchangeRateEditText.setText(
				prefs.getString(PreferencesActivity.KEY_EXCHANGE_RATE_PREFERENCE, CaptureActivity.DEFAULT_EXCHANGE_RATE));
	}

	public void updateTextViews(OcrResultText resultText, int priceIndex) {
		Log.d(TAG, "updateTextViews");

		String[] words = resultText.getText().replace("\n"," ").split(" ");
		String price = "0";
		String convertedPrice = "0";

		if (priceIndex < words.length) {
			price = words[priceIndex];
			convertedPrice = CurrencyHelper.convert(mActivity, words[priceIndex]);				
		}
		Log.d(TAG, "format, price: " + price);
		price = CurrencyHelper.formatPrice(price, mSourceCurrency);
		mSourceCurrencyTextView.setText(price);
		mTargetCurrencyTextView.setText(convertedPrice);
	}

	public void removeResultText() {
		//		Log.d(TAG, "removeResultText()");
		if (mSourceCurrencyTextView != null) {
			mSourceCurrencyTextView.setText("0.00");
		}
		if (mTargetCurrencyTextView != null) {
			mTargetCurrencyTextView.setText("0.00");
		}
	}


	private OnClickListener mToggleButtonListener = new OnClickListener() {
		public void onClick(View v) {
			Log.d(TAG, "onClick AutoButton isChecked?: " + mAutoExchangeRateToggleButton.isChecked());
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean(PreferencesActivity.KEY_AUTO_EXCHANGE_RATE_PREFERENCE, mAutoExchangeRateToggleButton.isChecked());
			editor.commit();
			mExchangeRateEditText.setEnabled(!mAutoExchangeRateToggleButton.isChecked());
			if (mAutoExchangeRateToggleButton.isChecked()) {

			}
		}
	};

	private OnItemSelectedListener mSourceCurrencySpinnerListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			//			String item = (String)parent.getItemAtPosition(pos);
			mSourceCurrency = mCurrencyCodes[pos]; 
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(PreferencesActivity.KEY_SOURCE_CURRENCY_PREFERENCE, mSourceCurrency);
			editor.putLong(PreferencesActivity.KEY_EXCHANGE_RATE_TIMESTAMP, (new Date()).getTime());
			editor.commit();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			// Do Nothing.
		}	
	};

	private OnItemSelectedListener mTargetCurrencySpinnerListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			//			String item = (String)parent.getItemAtPosition(pos);
			mTargetCurrency = mCurrencyCodes[pos];
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(PreferencesActivity.KEY_TARGET_CURRENCY_PREFERENCE, mTargetCurrency);
			editor.putLong(PreferencesActivity.KEY_EXCHANGE_RATE_TIMESTAMP, (new Date()).getTime());
			editor.commit();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			// Do Nothing.
		}
	};

	@Override
	public void onDialogClickSetManually(DialogFragment dialog) {
		mAutoExchangeRateToggleButton.setChecked(false);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(PreferencesActivity.KEY_AUTO_EXCHANGE_RATE_PREFERENCE, mAutoExchangeRateToggleButton.isChecked());
		editor.commit();
		mExchangeRateEditText.setEnabled(!mAutoExchangeRateToggleButton.isChecked());
		mExchangeRateEditText.requestFocus();
	}

	@Override
	public void onDialogClickCancel(DialogFragment dialog) {
		//revert....
	}



}
