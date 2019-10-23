package muchbeer.raum.livedatamodelpaging.info;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SharedPreferenceHelper {

    private static final String PREF_TIME= "Pref_time";
    private static SharedPreferenceHelper instance;
    private SharedPreferences prefs;

    private SharedPreferenceHelper(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferenceHelper getInstance(Context context) {
        if(instance ==null) {
            instance=new SharedPreferenceHelper(context);
        }
        return instance;
    }

    public void saveUpdateTime(long time) {
        prefs.edit().putLong(PREF_TIME, time).apply();
    }

    public long getUpdatedTime() {
        return prefs.getLong(PREF_TIME, 0);
    }

    public String getChachefromPreference() {
        return prefs.getString("pref_key_duration", "");
    }
}
