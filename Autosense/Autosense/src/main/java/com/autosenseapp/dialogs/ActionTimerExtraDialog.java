package com.autosenseapp.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.autosenseapp.R;
import com.autosenseapp.activities.settings.ArduinoPinEditorModes.PinMode;
import com.autosenseapp.callbacks.ActionTimerCallback;
import com.autosenseapp.databases.ArduinoPin;
import com.autosenseapp.devices.outputTriggers.Trigger;
import com.ikovac.timepickerwithseconds.view.MyTimePickerDialog;
import com.ikovac.timepickerwithseconds.view.TimePicker;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by eric on 2014-12-09.
 */
public class ActionTimerExtraDialog extends BaseDialog implements
		MyTimePickerDialog.OnTimeSetListener, RadioGroup.OnCheckedChangeListener {

	private static final String TAG = ActionTimerExtraDialog.class.getSimpleName();

	@InjectView(R.id.pin_number) TextView pinNumber;
	@InjectView(R.id.pin_status) RadioGroup pinStatus;
	@InjectView(R.id.pin_status_after) RadioGroup pinStatusAfter;
	@InjectView(R.id.timer_duration) EditText duration;

	private int hours, minutes, seconds, totalTime;

	@Inject Context context;
	private ActionTimerCallback callback;
	private ArduinoPin arduinoPin;
	private Trigger trigger;

	public ActionTimerExtraDialog(Context context, Trigger trigger, PinMode pinMode) {
		super(context);
		if (pinMode instanceof ActionTimerCallback) {
			callback = (ActionTimerCallback) pinMode;
		}
		this.context = context;
		this.arduinoPin = trigger.getArduinoPin();
		this.trigger = trigger;
	}

	@Override
	public void onCreate(Bundle saved) {
		super.onCreate(saved);
		setTitle(context.getString(R.string.pin_timer) + " " + arduinoPin.getPinNumber());
		setContentView(R.layout.dialog_timer_extras);
		ButterKnife.inject(this);

		duration.setInputType(InputType.TYPE_NULL);
		pinStatus.setOnCheckedChangeListener(this);
		pinStatusAfter.setOnCheckedChangeListener(this);
		pinNumber.setText(String.valueOf(arduinoPin.getPinNumber()));

		if (trigger.getExtraData() != null) {
			parseExtraData(trigger.getExtraData());
		}
	}

	@OnClick(R.id.timer_duration)
	public void timerClick() {
		MyTimePickerDialog timePickerDialog = new MyTimePickerDialog(getContext(), this, hours, minutes, seconds);
		timePickerDialog.show();
	}

	@Override
	public void onTimeSet(TimePicker view, int hours, int minutes, int seconds) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		totalTime = 0;
		totalTime += TimeUnit.HOURS.toSeconds(hours);
		totalTime += TimeUnit.MINUTES.toSeconds(minutes);
		totalTime += seconds;

		duration.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		RadioButton selectedRadioButton = ButterKnife.findById(group, checkedId);
		switch (group.getId()) {
			case  R.id.pin_status: {
				if (selectedRadioButton.getId() == R.id.timer_high) {
					((RadioButton)pinStatusAfter.getChildAt(1)).setChecked(true);
				} else {
					((RadioButton)pinStatusAfter.getChildAt(0)).setChecked(true);
				}
				break;
			}
			case R.id.pin_status_after: {
				if (selectedRadioButton.getId() == R.id.timer_high_after) {
					((RadioButton)pinStatus.getChildAt(1)).setChecked(true);
				} else {
					((RadioButton)pinStatus.getChildAt(0)).setChecked(true);
				}
				break;
			}
		}
	}

	@OnClick(R.id.button_edit_ok)
	public void okClick() {
		int buttonId = pinStatus.getCheckedRadioButtonId();
		Bundle info = new Bundle();
		info.putInt("time", totalTime);
		info.putBoolean("highThenLow", buttonId == R.id.timer_high);
		callback.actionPositive(info);
		dismiss();
	}

	@OnClick(R.id.button_edit_cancel)
	public void cancelClick() {
		callback.actionNegative();
		dismiss();
	}

	private void parseExtraData(String json) {
		Boolean highThenLow;
		int time;
		try {
			time = new JSONObject(json).getInt("time");
			highThenLow = new JSONObject(json).getBoolean("highThenLow");
			if (highThenLow) {
				((RadioButton) pinStatus.getChildAt(0)).setChecked(true);
				((RadioButton) pinStatusAfter.getChildAt(1)).setChecked(true);
			} else {
				((RadioButton) pinStatus.getChildAt(1)).setChecked(true);
				((RadioButton) pinStatusAfter.getChildAt(0)).setChecked(true);
			}
			seconds = (time % 60);
			minutes = ((time / 60) % 60);
			hours   = ((time / (60*60)) % 24);
			totalTime = time;
			duration.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
		} catch (JSONException e) {}
	}
}