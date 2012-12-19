package com.exchangecam.ocr;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class CurrencyTextView extends TextView {
	private static final String TAG = CurrencyTextView.class.getSimpleName();
	
	public CurrencyTextView(Context context) {
		super(context);
	}
	
	public CurrencyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
	}
	
	public CurrencyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
	}
	
	private void setCustomFont(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CurrencyView);
		String customFont = a.getString(R.styleable.CurrencyView_customFont);
		setCustomFont(context, customFont);
		a.recycle();
	}
	
	public boolean setCustomFont(Context context, String asset) {
		Typeface tf = null;
		try {
			Log.d(TAG, "setCustomFont, asset: " + asset);
			tf = Typefaces.get(context, asset);
		} catch (Exception e) {
			Log.e(TAG, "Could not get typeface: " + e);
		}
		
		setTypeface(tf);
		return true;
	}
	
}
