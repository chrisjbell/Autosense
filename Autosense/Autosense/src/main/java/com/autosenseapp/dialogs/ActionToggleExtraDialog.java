package com.autosenseapp.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import com.autosenseapp.R;
import com.autosenseapp.activities.settings.ArduinoPinEditorModes.PinMode;
import com.autosenseapp.databases.ArduinoPin;
import com.autosenseapp.devices.actions.ActionToggle;
import com.autosenseapp.devices.outputTriggers.Trigger;

import javax.inject.Inject;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.widget.AmbilWarnaPrefWidgetView;

/**
 * Created by eric on 2014-10-04.
 */
public class ActionToggleExtraDialog extends BaseDialog {

	@InjectView(R.id.high_color_view) AmbilWarnaPrefWidgetView highWidgetView;
	@InjectView(R.id.low_color_view) AmbilWarnaPrefWidgetView lowWidgetView;
	@Inject Context context;
	@Inject SharedPreferences sharedPreferences;
	private ArduinoPin arduinoPin;
	private int highColor;
	private int lowColor;

	public ActionToggleExtraDialog(Context context, Trigger trigger, PinMode pinMode) {
		super(context);
		this.arduinoPin = trigger.getArduinoPin();
	}

	@Override
	public void onCreate(Bundle saved) {
		super.onCreate(saved);
		setTitle(context.getString(R.string.pin_toggle_colors) + " " + arduinoPin.getPinNumber());
		setContentView(R.layout.dialog_toggle_extras);
		ButterKnife.inject(this);

		highColor = sharedPreferences.getInt(ActionToggle.PREFHIGH + arduinoPin.getId(), Color.WHITE);
		lowColor = sharedPreferences.getInt(ActionToggle.PREFLOW + arduinoPin.getId(), Color.WHITE);

		highWidgetView.setBackgroundColor(highColor);
		lowWidgetView.setBackgroundColor(lowColor);
	}

	@OnClick(R.id.high_row)
	public void clickHigh() {
		doClick(true);
	}

	@OnClick(R.id.low_row)
	public void clickLow() {
		doClick(false);
	}

	private void doClick(boolean isHigh) {
		int color;
		final String pref;
		final String title;
		final View widgetView;

		if (isHigh) {
			color = highColor;
			pref = ActionToggle.PREFHIGH;
			title = context.getString(R.string.high_color);
			widgetView = highWidgetView;
		} else {
			color = lowColor;
			pref = ActionToggle.PREFLOW;
			title = context.getString(R.string.low_color);
			widgetView = lowWidgetView;
		}

		AmbilWarnaDialog dialog = new AmbilWarnaDialog(getContext(), color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
			@Override
			public void onCancel(AmbilWarnaDialog dialog) { }

			@Override
			public void onOk(AmbilWarnaDialog dialog, int color) {
				sharedPreferences.edit().putInt(pref + arduinoPin.getId(), color).apply();
				widgetView.setBackgroundColor(color);
			}
		});
		dialog.getDialog().setTitle(title);
		dialog.show();
	}
}