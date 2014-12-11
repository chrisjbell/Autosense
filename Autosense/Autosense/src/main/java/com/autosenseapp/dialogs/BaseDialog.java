package com.autosenseapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import com.autosenseapp.AutosenseApplication;

/**
 * Created by eric on 2014-12-09.
 */
public class BaseDialog extends Dialog {
	public BaseDialog(Context context) {
		super(context);
	}

	@Override
	public void onCreate(Bundle saved) {
		super.onCreate(saved);
		((AutosenseApplication)getContext().getApplicationContext()).inject(this);
	}
}
