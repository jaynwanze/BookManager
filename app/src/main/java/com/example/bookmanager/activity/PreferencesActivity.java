package com.example.bookmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookmanager.R;

public class PreferencesActivity extends AppCompatActivity {
    final String PREFS_NAME = "Preferences"; // Change to "Preferences"
    final String SILENT_MODE = "silent_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_preferences);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_preferences), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Get intent
        Intent intent = getIntent();
        boolean currentSilentMode = intent.getBooleanExtra("currentSilentMode", false);
        Switch toggle = findViewById(R.id.silent_mode_switch);

        //Set with current silent mode
        toggle.setChecked(currentSilentMode);
        toggle.setOnClickListener(v -> {
            updatePref(toggle.isChecked());
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void updatePref(boolean silent) {
        Context context = getApplicationContext();
        SharedPreferences preferences =
                context.getSharedPreferences(PREFS_NAME, 0);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SILENT_MODE, silent).apply();
        editor.commit();
        // Set phone to silent mode if enabled
        if (silent) {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            if (audioManager != null) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }
            Toast.makeText(this, "Silent Mode " + (silent ? "Enabled" : "Disabled"),
                    Toast.LENGTH_LONG).show();

        }
    }
}

