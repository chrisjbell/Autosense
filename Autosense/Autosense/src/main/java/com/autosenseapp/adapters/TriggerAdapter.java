package com.autosenseapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.autosenseapp.R;
import com.autosenseapp.databases.ArduinoPin;
import com.autosenseapp.devices.outputTriggers.Trigger;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by eric on 2014-09-02.
 */
public class TriggerAdapter extends ArrayAdapter<Trigger> {

	private static final String TAG = TriggerAdapter.class.getSimpleName();

	private Context context;
	private LayoutInflater inflater;
	private ArduinoPin arduinoPin;
	private List<Trigger> allTriggers;
	private List<Trigger> selectedTriggers;

	public TriggerAdapter(Context context, List<Trigger> allTriggers, List<Trigger> selectedTriggers, ArduinoPin arduinoPin) {
		super(context, android.R.layout.simple_list_item_1, allTriggers);
		this.context = context;
		this.allTriggers = allTriggers;
		this.selectedTriggers = selectedTriggers;
		this.arduinoPin = arduinoPin;

		inflater = LayoutInflater.from(context);
	}

	@Override
	public Trigger getItem(int position) {
		if (allTriggers.size() > 0) {
			return allTriggers.get(position);
		} else {
			return null;
		}
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view != null) {
			holder = (ViewHolder) view.getTag(R.string.view_holder);
		} else {
			view = inflater.inflate(R.layout.pin_trigger_row, parent, false);
			holder = new ViewHolder(view);
			view.setTag(R.string.view_holder, holder);
		}
		Trigger currentTrigger = getItem(position);

		try {
			holder.textView.setText(currentTrigger.getName(context));
		} catch (Exception e) {}
		try {
			holder.checkBox.setOnClickListener((View.OnClickListener) context);
		} catch (Exception e) {}

		for (Trigger t : selectedTriggers) {
			holder.checkBox.setChecked(false);
			if (currentTrigger.getId() == t.getId()) {
				// if we've found a matching id, check it
				holder.checkBox.setChecked(true);
				currentTrigger.setAction(t.getAction());
				// and break the loop
				break;
			}
		}
		currentTrigger.setArduinoPin(arduinoPin);
		holder.checkBox.setTag(R.string.triggers, currentTrigger);
		view.setTag(R.string.triggers, currentTrigger);
		view.setTag(R.string.checkbox, holder.checkBox);
		return view;
	}

	static class ViewHolder {
		@InjectView(R.id.TextView) TextView textView;
		@InjectView(R.id.CheckBox) CheckBox checkBox;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}
}
