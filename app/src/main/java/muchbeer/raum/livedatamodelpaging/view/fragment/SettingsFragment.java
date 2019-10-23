package muchbeer.raum.livedatamodelpaging.view.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import muchbeer.raum.livedatamodelpaging.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.settings, rootKey);
    }
}
