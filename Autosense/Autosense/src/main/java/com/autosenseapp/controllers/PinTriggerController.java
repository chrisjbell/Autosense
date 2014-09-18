package com.autosenseapp.controllers;

import android.content.Context;
import com.autosenseapp.R;
import com.autosenseapp.databases.ArduinoPinsDataSource;
import com.autosenseapp.databases.ArduinoPin;
import com.autosenseapp.devices.actions.Action;
import com.autosenseapp.devices.triggers.Trigger;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by eric on 2014-09-02.
 */
@Singleton
public class PinTriggerController {

	private static final String TAG = PinTriggerController.class.getSimpleName();

	private Context context;
	public static final int HIGH_IMPEDANCE = 0;
	public static final int INPUT= 1;
	public static final int OUTPUT = 2;

	private final ArduinoPinsDataSource arduinoPinsDataSource;
	private List<String> pinModes;

	@Inject
	public PinTriggerController(Context context) {
		this.context = context;

		arduinoPinsDataSource = new ArduinoPinsDataSource(context);
		arduinoPinsDataSource.open();

		pinModes = new ArrayList<String>();
		pinModes.add(HIGH_IMPEDANCE, this.context.getString(R.string.high_impedance));
		pinModes.add(INPUT, this.context.getString(R.string.input));
		pinModes.add(OUTPUT, this.context.getString(R.string.output));

		setupTriggers();
	}

	public void onDestroy() {
		arduinoPinsDataSource.close();
	}

	private void setupTriggers() {
		// get the list of triggers
		List<Trigger> triggers = getTriggers();
		// loop over them
		for (Trigger trigger : triggers) {
			// get the class name
			String className = trigger.getClassName();
			// if it's not null
			if (className != null) {
				try {
					// create a new classname for the object
					Class<?> clazz = Class.forName(context.getPackageName() + ".devices.triggers." + className);
					// get the constructor
					Constructor<?> constructor = clazz.getConstructor(Context.class);
					// create the object
					constructor.newInstance(context);
				} catch (Exception e) {}
			}
		}
	}

	public List<ArduinoPin> getAllTriggersByClassName(String name) {
		return arduinoPinsDataSource.getAllTriggersByClassName(name);
	}

	public List<String> getPinModes() {
		return Collections.unmodifiableList(pinModes);
	}

	public List<Action> getActions() {
		return arduinoPinsDataSource.getActions();
	}

	public List<Trigger> getTriggers(ArduinoPin pin) {
		return arduinoPinsDataSource.getTriggers(pin);
	}

	public List<Trigger> getTriggers() {
		return arduinoPinsDataSource.getTriggers();
	}

	public void updatePin(ArduinoPin arduinoPin) {
		arduinoPinsDataSource.updatePin(arduinoPin);
	}

	public void addPinTriggers(ArduinoPin arduinoPin, Trigger trigger, Action action) {
		arduinoPinsDataSource.addPinTrigger(arduinoPin, trigger, action);
	}

	public void editPinTrigger(ArduinoPin arduinoPin, Trigger trigger, Action action) {
		arduinoPinsDataSource.editPinTrigger(arduinoPin, trigger, action);
	}

	public void removePinTrigger(ArduinoPin arduinoPin, Trigger trigger) {
		arduinoPinsDataSource.removePinTrigger(arduinoPin, trigger);
	}

	public List<ArduinoPin> getPins(int type) {
		return arduinoPinsDataSource.getPins(type);
	}
}