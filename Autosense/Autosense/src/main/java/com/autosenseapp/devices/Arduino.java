package com.autosenseapp.devices;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import com.autosenseapp.R;
import com.autosenseapp.interfaces.ArduinoListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by eric on 2013-05-28.
 */
public class Arduino {
	// debug tag
	private static final String TAG = Arduino.class.getSimpleName();

	public static final String PIN_STATE = "arduinoPinState";
	public static final String PIN_VALUE = "arduinoPinValue";

	public static final int HIGH_IMPEDANCE = 0;
	public static final int INPUT= 1;
	public static final int OUTPUT = 2;
	private static List<String> pinModes;

	// array list of devices passed in
	private SparseArray<Device> devices = new SparseArray<Device>();

	public Arduino(Context context) {
		pinModes = new ArrayList<String>();
		pinModes.add(HIGH_IMPEDANCE, context.getString(R.string.high_impedance));
		pinModes.add(INPUT, context.getString(R.string.input));
		pinModes.add(OUTPUT, context.getString(R.string.output));
	}

	// take the list of devices from the hardware manager, add them to our own list, and set the listener
	public void setDevices(SparseArray<Device> devices, ArduinoListener listener) {
		this.devices = devices;
		for (int i=0, size=devices.size(); i<size; i++) {
			devices.valueAt(i).setListener(listener);
		}
	}

	// get the device we have data for, and hand off the info for it to deal with
	public void parseData(int sender, int length, int[] data, int checksum) {
		// get the device that sent the data.  they'll process it and see if it's worth keeping or not
		Device device = devices.get(sender);
		try {
			device.parseData(sender, length, data, checksum);
		} catch (Exception e) {
			Log.d(TAG, "Device missing ", e);
		}
	}

	public static List<String> getPinModes() {
		return Collections.unmodifiableList(pinModes);
	}

}
