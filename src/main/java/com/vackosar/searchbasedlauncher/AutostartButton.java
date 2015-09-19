package com.vackosar.searchbasedlauncher;

import android.view.View;
import android.widget.TextView;

import com.google.inject.Inject;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class AutostartButton extends Colorful implements View.OnClickListener {

    @InjectView(R.id.autostartButton) TextView textView;
    @Inject private PreferencesAdapter preferencesAdapter;

    private boolean autostart;

    public void onCreate(@Observes OnCreateEvent onCreate) {
        textView.setOnClickListener(this);
        load();
    }

    @Override
    public void onClick(View v) {
        autostart=!autostart;
        setColor();
    }

    private void setColor() {
        setActivatedColor(autostart);
    }

    public boolean isOn() {
        return autostart;
    }

    public void save() {
        preferencesAdapter.save(getClass().getName(), new Boolean(autostart));
    }

    public void load() {
        autostart = preferencesAdapter.load(getClass().getName(), Boolean.class);
        setColor();
    }

    @Override
    View getView() {
        return textView;
    }
}
