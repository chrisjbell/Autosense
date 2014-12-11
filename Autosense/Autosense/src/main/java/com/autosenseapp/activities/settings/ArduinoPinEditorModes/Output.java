package com.autosenseapp.activities.settings.ArduinoPinEditorModes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.autosenseapp.R;
import com.autosenseapp.adapters.TriggerAdapter;
import com.autosenseapp.callbacks.ActionTimerCallback;
import com.autosenseapp.controllers.PinTriggerController;
import com.autosenseapp.databases.ArduinoPin;
import com.autosenseapp.devices.actions.Action;
import com.autosenseapp.devices.outputTriggers.Trigger;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by eric on 2014-11-26.
 */
public class Output extends HighImpedance implements
		RadioGroup.OnCheckedChangeListener,
		ActionTimerCallback {

	private static final String TAG = Output.class.getSimpleName();

	private Context context;
	@Inject	PinTriggerController pinTriggerController;

	@InjectView(R.id.arduino_action_title) TextView actionTitle;
	@InjectView(R.id.arduino_actions) RadioGroup actionRadioButtons;
	@InjectView(R.id.action_settings_button) View actionSettings;

	private List<Action> availableActions;
	private List<Trigger> availableTriggers;

	private Action action;
	private Trigger trigger;

	public Output(Context context) {
		super(context);
		this.context = context;

		// do this here so we aren't hitting the db more than necessary
		availableActions = pinTriggerController.getOutputActions();
		availableTriggers = pinTriggerController.getOutputTriggers();

		actionRadioButtons.setOnCheckedChangeListener(this);
	}

	@Override
	public void updateTriggerList(ArduinoPin selectedArduinoPin) {
		List<Trigger> selectedTriggers = pinTriggerController.getTriggers(selectedArduinoPin);
		// pass the full list of triggers and the selected triggers for the selected pin
		pinTriggers.setAdapter(new TriggerAdapter(context, availableTriggers, selectedTriggers, selectedArduinoPin));
		if (selectedTriggers.size() > 0) {
			// show the action for the top trigger
			updateActions(selectedTriggers.get(0), true);
		} else {
			updateActionTitle(null);
			actionRadioButtons.removeAllViews();
		}
	}

	@Override
	public void updateActions(Trigger trigger, boolean checked) {
		updateActionTitle(trigger);
		this.trigger = trigger;
		actionRadioButtons.removeAllViews();

		for (Action action : availableActions) {
			RadioButton radioButton = new RadioButton(context);

			radioButton.setId(action.getId());
			radioButton.setText(action.getName(context));
			// The reason we use R.string.action is we need a guaranteed unique id that is precompiled.  using already defined strings is easier than making new ids
			radioButton.setTag(R.string.action, action);
			radioButton.setTag(R.string.triggers, trigger);

			radioButton.setEnabled(checked);

			try {
				if (trigger.getAction().getId() == action.getId()) {
					radioButton.setChecked(true);
					showHideSettingsButton(action);
					this.action = action;
				}
			} catch (NullPointerException e) {}
			actionRadioButtons.addView(radioButton);
		}
	}

	private void updateActionTitle(Trigger trigger) {
		if (trigger != null) {
			actionTitle.setText(context.getString(R.string.action) + ": " + trigger.getName(context));
		} else {
			actionTitle.setText(context.getString(R.string.action));
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		RadioButton selectedRadioButton = ButterKnife.findById(group, checkedId);
		try {
			Action action = (Action) selectedRadioButton.getTag(R.string.action);
			Trigger trigger = (Trigger) selectedRadioButton.getTag(R.string.triggers);
			this.action = action;
			this.trigger = trigger;
			// get the stored action from the radio button
			showHideSettingsButton(action);
			if (selectedRadioButton.isChecked()) {
				pinTriggerController.editPinTrigger(trigger.getArduinoPin(), trigger, action);
			}
		} catch (NullPointerException e) {}
	}

	private void showHideSettingsButton(Action action) {
		if (action.hasExtra()) {
			actionSettings.setVisibility(View.VISIBLE);
		} else {
			actionSettings.setVisibility(View.INVISIBLE);
		}
	}

	@OnClick(R.id.action_settings_button)
	public void settingsButtonClick() {
		View radioButton = actionRadioButtons.findViewById(actionRadioButtons.getCheckedRadioButtonId());
		Trigger trigger = (Trigger) radioButton.getTag(R.string.triggers);
		((Action)radioButton.getTag(R.string.action)).getExtraDialog(context, trigger, this).show();
	}

	@Override
	public void actionPositive(Bundle info) {
		JSONObject jsonObject = new JSONObject();
		Set<String> keys = info.keySet();
		for (String key : keys) {
			try {
				jsonObject.put(key ,info.get(key));
			} catch (JSONException e) {}
		}
		this.trigger.setExtraData(jsonObject.toString());
		pinTriggerController.editPinTrigger(trigger.getArduinoPin(), trigger, action);
	}

	@Override
	public void actionNegative() {
		Log.d(TAG, "negative");
	}
}
