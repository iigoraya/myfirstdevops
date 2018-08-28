package sunfly.tv2u.com.karaoke2u.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import sunfly.tv2u.com.karaoke2u.R;
import sunfly.tv2u.com.karaoke2u.fragments.AboutUsFragment;
import sunfly.tv2u.com.karaoke2u.fragments.PlaybackControlsFragment;


/**
 * Created by Envy on 4/26/2016.
 */
public class AboutActivity extends AppCompatActivity {

    public PlaybackControlsFragment mControlsFragment;
    private SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*if (!(getResources().getBoolean(R.bool.portrait_only))) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }*/
        setContentView(R.layout.about_layout_activity);

        mControlsFragment = (PlaybackControlsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_playback_controls);
        shared = PreferenceManager.getDefaultSharedPreferences(AboutActivity.this);

        if(shared.getBoolean("isPlayingRadio",false)) {
            mControlsFragment.setAlbumArtImage();
            mControlsFragment.pulseEffect.setVisibility(View.VISIBLE);
            mControlsFragment.pulseEffect.startRippleAnimation();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("pulse-animator"));

        if (savedInstanceState == null) {
            AboutUsFragment fragment = new AboutUsFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.about_container , fragment).commit();
        }
    }

    protected BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("isPlayingRadio", shared.getBoolean("isPlayingRadio",false) + "");
                    if(shared.getBoolean("isPlayingRadio",false)){
                        mControlsFragment.setAlbumArtImage();
                        mControlsFragment.pulseEffect.setVisibility(View.VISIBLE);
                        mControlsFragment.pulseEffect.startRippleAnimation();
                    }else{
                        mControlsFragment.pulseEffect.setVisibility(View.INVISIBLE);
                        mControlsFragment.pulseEffect.stopRippleAnimation();
                    }
                }
            });

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }
}
