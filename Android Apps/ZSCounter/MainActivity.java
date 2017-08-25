package com.numetriclabz.youtubedemo;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayerView;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private YouTubePlayerView playerView;
    private TextView tvDay, tvHour, tvMinute, tvSecond, tvEvent;
    private LinearLayout linearLayout1, linearLayout2;
    private Handler handler;
    private Runnable runnable;
    NotificationCompat.Builder notification;
    private static final int uniqueID = 45612;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
        // Called to modify the window feature and resize to full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        initUI();
        countDownStart();
        onFinish();

        playerView = (YouTubePlayerView) findViewById(R.id.player_view);

        // initializes the YouTube player view
        playerView.initialize(Config.API_KEY, this);

    }
    @SuppressLint("SimpleDateFormat")
    private void initUI() {
        linearLayout1 = (LinearLayout) findViewById(R.id.ll1);
        linearLayout2 = (LinearLayout) findViewById(R.id.ll2);
        tvDay = (TextView) findViewById(R.id.txtTimerDay);
        tvHour = (TextView) findViewById(R.id.txtTimerHour);
        tvMinute = (TextView) findViewById(R.id.txtTimerMinute);
        tvSecond = (TextView) findViewById(R.id.txtTimerSecond);
        tvEvent = (TextView) findViewById(R.id.tvevent);
    }
    public void onFinish(){

        notification.setSmallIcon(R.drawable.ic_launcher);
        notification.setTicker("This is the ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("ZS-Counter");
        notification.setContentText("Click here to watch Dragon Ball Super Episode!!!");

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        //Builds notification and issues it
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());
    }
    // //////////////COUNT DOWN START/////////////////////////
    public void countDownStart() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd");
                    // Here Set your Event Date
                    Date eventDate = dateFormat.parse("2017-04-09");
                    Date currentDate = new Date();
                    if (!currentDate.after(eventDate)) {
                        long diff = eventDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        tvDay.setText("" + String.format("%02d", days));
                        tvHour.setText("" + String.format("%02d", hours));
                        tvMinute.setText("" + String.format("%02d", minutes));
                        tvSecond.setText("" + String.format("%02d", seconds));
                    } else {
                        linearLayout1.setVisibility(View.VISIBLE);
                        linearLayout2.setVisibility(View.GONE);
                        tvEvent.setText("Android Event Start");
                        handler.removeCallbacks(runnable);
                        // handler.removeMessages(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }


    // Called when YouTube Player initialization is failed
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorResult) {

        // shows dialog if user interaction may fix the error
        if (errorResult.isUserRecoverableError()) {
            errorResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        }
        else {
            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Called when initialization of Player is successful
     * @param provider Provider used to initialize the Player
     * @param player Player instance used to control the video playback
     * @param wasRestored Depicts whether the video is restored from a previous
     *                    state. Returns true if video is resumed from the last
     *                    paused state, else returns false
     */
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

            player.cueVideo(Config.VIDEO_CODE);
            player.setPlayerStyle(PlayerStyle.DEFAULT);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {

            // initializes the YouTube player view
            getYouTubePlayerProvider().initialize(Config.API_KEY, this);
        }
    }


    // Returns Player view defined in xml file
    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.player_view);
    }


    /**
     * 
     */
    private final class EventListener implements YouTubePlayer.PlaybackEventListener {


        /**
         * Called when video starts playing
         */
        @Override
        public void onPlaying() {
            Log.e("Status", "Playing");
        }


        /**
         * Called when video stops playing
         */
        @Override
        public void onPaused() {
            Log.e("Status", "Paused");
        }


        /**
         * Called when video stopped for a reason other than paused
         */
        @Override
        public void onStopped() {
            Log.e("Status", "Stopped");
        }


        /**
         * Called when buffering of video starts or ends
         * @param b True if buffering is on, else false
         */
        @Override
        public void onBuffering(boolean b) {
        }


        /**
         * Called when jump in video happens. Reason can be either user scrubbing
         * or seek method is called explicitely
         * @param i
         */
        @Override
        public void onSeekTo(int i) {
        }
    }

    private final class StateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        /**
         * Called when player begins loading a video. During this duration, player
         * won't accept any command that may affect the video playback
         */
        @Override
        public void onLoading() {
        }

        /**
         * Called when video is loaded. After this player can accept
         * the playback affecting commands
         * @param s Video Id String
         */
        @Override
        public void onLoaded(String s) {
        }


        /**
         * Called when YouTube ad is started
         */
        @Override
        public void onAdStarted() {
        }


        /**
         * Called when video starts playing
         */
        @Override
        public void onVideoStarted() {
        }


        /**
         * Called when video is ended
         */
        @Override
        public void onVideoEnded() {
        }


        /**
         * Called when any kind of error occurs
         * @param errorReason Error string showing the reason behind it
         */
        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
        }
    }
}
