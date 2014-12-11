package com.autosenseapp.activities.settings.ArduinoPinEditorModes;

import android.widget.RadioGroup;
import com.autosenseapp.databases.ArduinoPin;
import com.autosenseapp.devices.outputTriggers.Trigger;

/**
 * Created by eric on 2014-11-26.
 */
public abstract class PinMode {

	public abstract void updateTriggerList(ArduinoPin pin);
	public abstract void updateActions(Trigger trigger, boolean checked);
	public abstract void onCheckedChanged(RadioGroup group, int checkedId);
}
