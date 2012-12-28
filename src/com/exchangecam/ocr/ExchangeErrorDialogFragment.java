package com.exchangecam.ocr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

public class ExchangeErrorDialogFragment extends DialogFragment {

	public interface ExchangeErrorDialogListener {
		public void onDialogClickSetManually(DialogFragment dialog);
		public void onDialogClickCancel(DialogFragment dialog);
	}

	ExchangeErrorDialogListener mListener;
	//	DashboardFragment dashboardFragment;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			FragmentManager fm = activity.getFragmentManager();
			mListener = (ExchangeErrorDialogListener) fm.findFragmentByTag("DASHBOARD_FRAGMENT");
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement ExchangeErrorDialogListener Interface");
		} catch (NullPointerException e) {
			throw new NullPointerException(activity.toString() + "DashboardFragment is not initialized");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(getActivity(), R.style.exchangeErrorDialog));
		builder.setTitle(R.string.dialog_exchange_error_title);
		builder.setMessage(R.string.dialog_exchange_error)
		.setPositiveButton(R.string.dialog_exchange_manual, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mListener.onDialogClickSetManually(ExchangeErrorDialogFragment.this);
			}
		})
		.setNegativeButton(R.string.dialog_exchange_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mListener.onDialogClickCancel(ExchangeErrorDialogFragment.this);
			}
		});
		// Create the AlertDialog object and return it
				return builder.create();
	}
}
