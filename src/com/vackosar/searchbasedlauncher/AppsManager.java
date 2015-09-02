package com.vackosar.searchbasedlauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("Convert2Lambda")
public class AppsManager {
    final MainActivity mainActivity;
    List<App> pkg = getEmptyAppList();
    Set<App> extra = new HashSet<>();
    Set<App> hidden = new HashSet<>();
    final private PreferencesAdapter preferencesAdapter;

    public AppsManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        preferencesAdapter = new PreferencesAdapter(mainActivity);
    }

    public void refreshView() {
        mainActivity.getAppsView().refeshView();
    }

    public void reload() {
        final Intent main = new Intent(Intent.ACTION_MAIN, null);
        final PackageManager pm = mainActivity.getPackageManager();
        switch (mainActivity.getAppTypeSelector().getSelected()) {
            case normal:
                pkg = getApplicationActivities(main, pm);
                pkg.removeAll(hidden);
                pkg.addAll(extra);
                break;
            case activity:
                pkg = getAllActivities(main, pm);
                pkg.removeAll(hidden);
                pkg.addAll(extra);
                break;
            case extra:
                pkg = getEmptyAppList();
                pkg.addAll(extra);
                break;
            case hidden:
                pkg = getEmptyAppList();
                pkg.addAll(hidden);
                break;
        }
        Collections.sort(pkg);
        mainActivity.getAppsView().refeshView();
    }

    private List<App> getAllActivities(Intent main, PackageManager pm) {
        final List<App> pkg = getEmptyAppList();
        final List<ResolveInfo> launchables = pm.queryIntentActivities(main, 0);
        for (ResolveInfo launchable : launchables) {
            App app = new App(launchable.activityInfo.packageName, deriveNick(launchable), launchable.activityInfo.name);
            pkg.add(app);
        }
        return pkg;
    }

    private static List<App> getEmptyAppList() {
        return new ArrayList<>();
    }

    private String deriveNick(ResolveInfo launchable) {
        String[] split = launchable.activityInfo.name.split("\\.");
        String nick = split[1];
        for (int j = 2; j < split.length; j++) {
            nick = nick + ":" + split[j];
        }
        return nick;
    }

    private List<App> getApplicationActivities(Intent main, PackageManager pm) {
        final List<App> pkg = getEmptyAppList();
        main.addCategory(Intent.CATEGORY_LAUNCHER); // will show only Regular AppsManager
        final List<ResolveInfo> launchables = pm.queryIntentActivities(main, 0);

        for (ResolveInfo launchable : launchables) {
            String nick = launchable.activityInfo.loadLabel(pm).toString();
            String name = launchable.activityInfo.packageName;
            String activity = launchable.activityInfo.name;
            final App app = new App(name, nick, activity);
            pkg.add(app);
        }
        return pkg;
    }

    public void load() {
        try {
            extra = preferencesAdapter.loadSet("extra");
            hidden = preferencesAdapter.loadSet("hidden");
        } catch (Exception e) {
            save();
        }
        reload();
    }

    public void save() {
        preferencesAdapter.saveSet(extra, "extra");
        preferencesAdapter.saveSet(hidden, "hidden");
        reload();
    }

    public Set<App> getExtra() {
        return extra;
    }

    public Set<App> getHidden() {
        return hidden;
    }

    public List<App> getPkg() {
        return pkg;
    }
}