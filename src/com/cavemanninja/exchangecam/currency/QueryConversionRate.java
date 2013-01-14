package com.cavemanninja.exchangecam.currency;

import java.io.*;
import java.net.*;
import java.util.regex.*;
import java.math.*;

import com.cavemanninja.exchangecam.CaptureActivity;
import com.cavemanninja.exchangecam.PreferencesActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;


/*
 * Uses Google calculator service to convert the currency.
 */

public class QueryConversionRate {
	public static final String TAG = "QueryConversionRate.class.getSimpleName";
	public static final String BAD_CONVERSION_MSG = "[Conversion failed]";
	
	private static final String error = "error:";
	private static final String noErrorFound = "\"\"";
	private static final String regExp = "-?\\d+(.\\d+)?";

	private QueryConversionRate(Activity activity) {
		// Private Constructor to enforce noninstantiability.
	}
	
	static String getConversionRate(String sourceCurrencyCode,
			String targetCurrencyCode) {
		try {

			String urlString = String.valueOf("/ig/calculator?h1=en&q=" + "1"
					+ "" + sourceCurrencyCode + "=?" + targetCurrencyCode);
			
			URL url = new URL ("http://www.google.com" + urlString);
			URLConnection urlConnection = url.openConnection();
			
			InputStream inputStream = urlConnection.getInputStream();
		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		 
		    String conversionResult = bufferedReader.readLine();
		    bufferedReader.close();
		    inputStream.close();
		    urlConnection = null;
						
			String conversionRate = extractConvertedValue(conversionResult);
			
			/* 
			 * Check that the conversion rate is numeric before saving it.
			 */
			
			if (conversionRate != null && CurrencyHelper.isNumeric(conversionRate)) {
		        BigDecimal roundVal = new BigDecimal(conversionRate);
		        roundVal.round(new MathContext(4, RoundingMode.HALF_UP));
		        //Log.d(TAG, "getConversionRate.roundVal: " + roundVal);
		        return roundVal.toString();
		    }
			return conversionRate;
		}
		catch (Exception ex)
	    {
	      //Log.e(TAG, "Caught exceptioin in conversion request.");
	      return QueryConversionRate.BAD_CONVERSION_MSG;
	    }

	}



	/**
	 * If error is found within the response string, throw runtime exception to
	 * report, else parse the result for extraction
	 **/
	private static String extractConvertedValue(String convertedResult)
			throws Exception {
		String[] convertedResStrings = convertedResult.split(",");
		for (int i = 0; i < convertedResStrings.length; i++) {
			if ((convertedResStrings[i].contains(error))
					&& convertedResStrings[i].split(" ")[1]
							.equals(noErrorFound)) {
				String convertedValue = extract(convertedResStrings[i - 1]);
				if (!(convertedValue.equals(""))) {
					return convertedValue;
				}
			} else if ((convertedResStrings[i].contains(error))
					&& !convertedResStrings[i].split(" ")[1]
							.equals(noErrorFound)) {
				throw new RuntimeException(
						"Error occured while converting amount: "
								+ convertedResStrings[i].split(" ")[1]);
			}
		}
		return null;
	}

	private static String extract(String str) {
		StringBuffer sBuffer = new StringBuffer();
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		if (m.find()) {
			sBuffer.append(m.group());
		}
		return sBuffer.toString();
	}
	
}
