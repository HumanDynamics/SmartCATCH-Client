package edu.mit.media.hellopds;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.util.AttributeSet;
import android.widget.TextView;
import android.view.*;

public class ButtonPreference extends Preference {
    private TextView mTextView;

    public ButtonPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setWidgetLayoutResource(R.layout.pref_button);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
//        mTextView = (TextView) view.findViewById(R.id.pr);
    }

    public void setImage(Intent imageData) {
    }
}