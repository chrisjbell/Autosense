package com.autosenseapp.activities.settings.ArduinoPinEditorModes;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.autosenseapp.AutosenseApplication;
import com.autosenseapp.R;
import com.autosenseapp.databases.ArduinoPin;
import com.autosenseapp.devices.outputTriggers.Trigger;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by eric on 2014-11-26.
 */
public class HighImpedance implements PinMode {

	private Context context;
	@InjectView(R.id.pin_triggers_list) ListView pinTriggers;
	@InjectView(R.id.arduino_action_title) TextView actionTitle;
	@InjectView(R.id.arduino_actions) RadioGroup actionGroup;

	public HighImpedance(Context context) {
		this.context = context;
		ButterKnife.inject(this, (Activity)context);
		((AutosenseApplication)context.getApplicationContext()).inject(this);
	}

	public void updateActions() {
	}

	@Override
	public void updateTriggerList(ArduinoPin pin) {
		pinTriggers.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, new String[]{}));
		updateActions(null, false);
	}

	@Override
	public void updateActions(Trigger trigger, boolean checked) {
		actionTitle.setText(context.getString(R.string.action));
		actionGroup.removeAllViews();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

	}
}
