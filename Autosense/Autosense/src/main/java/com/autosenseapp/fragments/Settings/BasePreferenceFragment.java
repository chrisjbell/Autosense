package com.autosenseapp.fragments.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autosenseapp.AutosenseApplication;
import com.autosenseapp.R;

import javax.inject.Inject;

/**
 * Created by eric on 2014-09-16.
 */
public abstract class BasePreferenceFragment extends PreferenceFragment {

	@Inject
	SharedPreferences sharedPreferences;

	@Override
	public void onCreate(Bundle saved) {
		super.onCreate(saved);

		((AutosenseApplication)getActivity().getApplication()).inject(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved) {
		View view = super.onCreateView(inflater, group, saved);
		if (sharedPreferences.getBoolean("setListviewBg", false) && view != null) {
			view.setBackgroundResource(R.drawable.button_bg);
		}
		return view;
	}
}
