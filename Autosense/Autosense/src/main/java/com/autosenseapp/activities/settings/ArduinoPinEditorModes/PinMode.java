package com.autosenseapp.activities.settings.ArduinoPinEditorModes;

import android.widget.RadioGroup;
import com.autosenseapp.databases.ArduinoPin;
import com.autosenseapp.devices.outputTriggers.Trigger;

/**
 * Created by eric on 2014-11-26.
 */
public interface PinMode {

	public void updateTriggerList(ArduinoPin pin);
	public void updateActions(Trigger trigger, boolean checked);
	public void onCheckedChanged(RadioGroup group, int checkedId);
}
