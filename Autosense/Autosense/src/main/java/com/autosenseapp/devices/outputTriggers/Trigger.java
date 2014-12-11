package com.autosenseapp.devices.outputTriggers;

import android.content.Context;
import android.os.Parcelable;
import com.autosenseapp.AutosenseApplication;
import com.autosenseapp.databases.ArduinoPin;
import com.autosenseapp.devices.actions.Action;

/**
 * Created by eric on 2014-09-05.
 */
public abstract class Trigger implements Parcelable {

	private static final String TAG = Trigger.class.getSimpleName();

	protected int id;
	protected Context context;
	protected Action action;
	protected String className;
	protected String name;
	protected ArduinoPin arduinoPin;
	protected String extraData;

	public Trigger(Context context) {
		this.context = context;
		// add the trigger to the inject list
		((AutosenseApplication)context.getApplicationContext()).inject(this);
	}

	public Trigger() {}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName(Context context) {
		return context.getResources().getString((context.getResources().getIdentifier(name, "string", context.getPackageName())));
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Action getAction() {
		return action;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setArduinoPin(ArduinoPin arduinoPin) {
		this.arduinoPin = arduinoPin;
	}

	public ArduinoPin getArduinoPin() {
		return arduinoPin;
	}

	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

	public String getExtraData() {
		return extraData;
	}

	@Override
	public String toString() {
		return name;
	}
}
