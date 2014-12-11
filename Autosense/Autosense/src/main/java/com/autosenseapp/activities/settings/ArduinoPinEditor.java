package com.autosenseapp.activities.settings;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.autosenseapp.R;
import com.autosenseapp.activities.BaseActivity;
import com.autosenseapp.activities.settings.ArduinoPinEditorModes.HighImpedance;
import com.autosenseapp.activities.settings.ArduinoPinEditorModes.Input;
import com.autosenseapp.activities.settings.ArduinoPinEditorModes.Output;
import com.autosenseapp.activities.settings.ArduinoPinEditorModes.PinMode;
import com.autosenseapp.adapters.PinsAdapter;
import com.autosenseapp.controllers.PinTriggerController;
import com.autosenseapp.databases.ArduinoPin;
import com.autosenseapp.devices.Arduino;
import com.autosenseapp.devices.outputTriggers.Trigger;
import java.util.ArrayList;
import javax.inject.Inject;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

/**
 * Created by eric on 2014-08-30.
 */
public class ArduinoPinEditor extends BaseActivity implements
		View.OnClickListener {

	private static final String TAG = ArduinoPinEditor.class.getSimpleName();

	@Inject PinTriggerController pinTriggerController;

	@InjectView(R.id.pin_edit_title) TextView pinEditTitle;
	@InjectView(R.id.pin_comment) EditText pinComment;
	@InjectView(R.id.pin_mode) Spinner pinModeList;
	@InjectView(R.id.arduino_pins_list) ListView pinList;

	private PinMode pinMode;
	private SparseArray<PinMode> pinModes = new SparseArray<PinMode>();
	private ArduinoPin selectedArduinoPin;

	@Override
	public void onCreate(Bundle saved) {
		super.onCreate(saved);

		setContentView(R.layout.arduino_pin_editor);
		// inject all the views
		ButterKnife.inject(this);

		pinModes.put(Arduino.HIGH_IMPEDANCE, new HighImpedance(this));
		pinModes.put(Arduino.INPUT, new Input(this));
		pinModes.put(Arduino.OUTPUT, new Output(this));
		pinMode = pinModes.get(Arduino.HIGH_IMPEDANCE);

		// get the map of pins that has been passed
		if (getIntent().hasExtra("pins")) {
			ArrayList<ArduinoPin> arduinoPins = getIntent().getParcelableArrayListExtra("pins");
			pinList.setAdapter(new PinsAdapter(this, arduinoPins));
			pinModeList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Arduino.getPinModes()));

			// Must be last
			// select the first item
			pinList.performItemClick(pinList, 0, pinList.getItemIdAtPosition(0));
		}
	}

	@OnTextChanged(R.id.pin_comment)
	void onTextChanged(CharSequence text) {
		// get the text and save it
		selectedArduinoPin.setComment(text.toString());
		pinTriggerController.updatePin(selectedArduinoPin);
	}

	// Called when an item in the list of pins, or trigger is clicked
	@OnItemClick(R.id.arduino_pins_list)
	public void pinListClick(AdapterView<?> adapter, View view, int position, long id) {
		selectedArduinoPin = (ArduinoPin) adapter.getItemAtPosition(position);
		updatePinMode(selectedArduinoPin.getMode());
		pinMode.updateTriggerList(selectedArduinoPin);

		pinEditTitle.setText(getString(R.string.pin) + " " + selectedArduinoPin.toString());
		pinComment.setText(selectedArduinoPin.getComment());
	}

	private void updatePinMode(int mode) {
		pinModeList.setSelection(mode);
		pinMode = pinModes.get(mode);
		// save in the db
		pinTriggerController.updatePin(selectedArduinoPin);
	}

	// called when pin mode is selected from the spinner
	@OnItemSelected(R.id.pin_mode)
	public void onPinModeSelected(AdapterView<?> parent, View view, int position, long id) {
		int mode = ((Long) id).intValue();
		selectedArduinoPin.setMode(mode);
		updatePinMode(mode);
		pinMode.updateTriggerList(selectedArduinoPin);
	}

	@OnItemClick(R.id.pin_triggers_list)
	public void pinTrigerClick(AdapterView<?> adapter, View view, int position, long id) {
		View v = adapter.getChildAt(position);
		CheckBox checkbox = (CheckBox) v.getTag(R.string.checkbox);
		Trigger trigger = (Trigger) view.getTag(R.string.triggers);
		// set the action title to include the trigger
		pinMode.updateActions(trigger, checkbox.isChecked());
	}

	// trigger checkbox callback
	@Override
	public void onClick(View v) {
		Trigger trigger = (Trigger) v.getTag(R.string.triggers);
		pinMode.updateActions(trigger, ((CheckBox)v).isChecked());
	}

}
