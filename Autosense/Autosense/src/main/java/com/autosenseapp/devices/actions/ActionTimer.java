package com.autosenseapp.devices.actions;

import android.app.Dialog;
import android.content.Context;
import android.os.Parcel;
import android.view.View;

import com.autosenseapp.activities.settings.ArduinoPinEditorModes.PinMode;
import com.autosenseapp.databases.ArduinoPin;
import com.autosenseapp.devices.outputTriggers.Trigger;
import com.autosenseapp.dialogs.ActionTimerExtraDialog;

/**
 * Created by eric on 2014-09-04.
 */
public class ActionTimer extends Action {

	private static final String TAG = ActionTimer.class.getSimpleName();
	private String extraData;

	public ActionTimer() {
		super();
	}

	@Override
	public boolean hasExtra() {
		return true;
	}

	@Override
	public Dialog getExtraDialog(final Context context, Trigger trigger, PinMode pinMode) {
		return new ActionTimerExtraDialog(context, trigger, pinMode);
	}

	@Override
	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

	@Override
	public String getExtraData() {
		return extraData;
	}

	@Override
	public String getExtraString() {
		if (extraData != null && !extraData.equalsIgnoreCase("")) {
			try {
				Long seconds = Long.parseLong(extraData);

				int minutes = (int) ((seconds / 60) % 60);
				int hours = (int) ((seconds / (60 * 60)) % 24);
				seconds = (seconds % 60);

				return String.format("%02d:%02d:%02d", hours, minutes, seconds);
			} catch (Exception e) {	}
		}
		return "";
	}

	@Override
	public void setView(Context context, View view, ArduinoPin pin) {}

	@Override
	public void doAction(Context context, ArduinoPin pin) {
//		super.doAction(context, pin, Master.TIMER);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(extraData);
		dest.writeString(className);
	}

	public ActionTimer(Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
		this.extraData = in.readString();
		this.className = in.readString();
	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public Object createFromParcel(Parcel source) {
			return new ActionTimer(source);
		}

		@Override
		public Action[] newArray(int size) {
			return new Action[0];
		}
	};

}
