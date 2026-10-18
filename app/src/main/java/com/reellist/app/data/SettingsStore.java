package com.reellist.app.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.reellist.app.logic.SortOption;

/** Small key-value store for user preferences (Settings screen). */
public class SettingsStore {
    private static final String KEY_SORT = "default_sort";
    private static final String KEY_CONFIRM = "confirm_delete";

    private final SharedPreferences prefs;

    public SettingsStore(Context context) {
        prefs = context.getSharedPreferences("reellist_settings", Context.MODE_PRIVATE);
    }

    public SortOption getDefaultSort() {
        String saved = prefs.getString(KEY_SORT, null);
        for (SortOption o : SortOption.values()) {
            if (o.name().equals(saved)) return o;
        }
        return SortOption.DATE_ADDED;
    }

    public void setDefaultSort(SortOption value) {
        prefs.edit().putString(KEY_SORT, value.name()).apply();
    }

    public boolean getConfirmBeforeDelete() {
        return prefs.getBoolean(KEY_CONFIRM, true);
    }

    public void setConfirmBeforeDelete(boolean value) {
        prefs.edit().putBoolean(KEY_CONFIRM, value).apply();
    }
}
