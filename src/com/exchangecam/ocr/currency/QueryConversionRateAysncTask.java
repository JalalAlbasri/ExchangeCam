package com.exchangecam.ocr.currency;

import com.exchangecam.ocr.CaptureActivity;
import com.exchangecam.ocr.PreferencesActivity;
import com.exchangecam.ocr.currency.QueryConversionRate;

import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class QueryConversionRateAysncTask extends
		AsyncTask<String, String, Boolean> {
	private static final String TAG = QueryConversionRateAysncTask.class
			.getSimpleName();

	private CaptureActivity activity;
	private Context context;
	private ProgressDialog dialog;
	private String sourceCurrencyCode;
	private String targetCurrencyCode;
	private String conversionRate;

	/*
	 * AsyncTask to asynchronously query the currency conversion rate from
	 * Google.
	 */

	public QueryConversionRateAysncTask(CaptureActivity activity, ProgressDialog dialog, String sourceCurrencyCode,
			String targetCurrencyCode) {
		this.activity = activity;
		this.context = activity.getBaseContext();
		this.dialog = dialog;
		this.sourceCurrencyCode = sourceCurrencyCode;
		this.targetCurrencyCode = targetCurrencyCode;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog.setTitle("Please wait...");
		dialog.setMessage("Retrieving Exchange Rate...");
		dialog.setIndeterminate(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(false);
		dialog.show();
	}
	
	@Override
	protected Boolean doInBackground(String... arg0) {
		this.conversionRate = QueryConversionRate
				.getConversionRate(sourceCurrencyCode, targetCurrencyCode);
		
		if (conversionRate.equals(QueryConversionRate.BAD_CONVERSION_MSG)){
			return false;
		}
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		//If the result is good set the shared preference with the new conversion rate
		//If the result is bad show an error message
		
		super.onPostExecute(result);
		
		try {
			dialog.dismiss();
		} catch (IllegalArgumentException e) {
			// Catch "View not attached to window manager" error, and continue
		}
		
		if (result) {
			//Update exchange rate in shared preferences.
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	        sharedPreferences.edit().putString(PreferencesActivity.KEY_EXCHANGE_RATE_PREFERENCE, conversionRate).commit();
		} else {
			activity.showErrorMessage("Error", "Unable to retrieve exchange rate", false);
		}	
	}
}