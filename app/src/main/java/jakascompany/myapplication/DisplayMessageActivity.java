package jakascompany.myapplication;


import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;



public class DisplayMessageActivity extends AppCompatActivity {
    GestureDetector gestureDetector;
    BackgroundSound bg = new BackgroundSound();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageView);

        imageButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                if (gestureDetector.onTouchEvent(arg1)) {
                    bg.doInBackground();
                    return true;
                } else {
                    bg.stop();
                }
                return false;
            }
        });

    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }


    private class BackgroundSound extends AsyncTask<Void, Void, Void> {
        MediaPlayer player;

        @Override
        protected Void doInBackground(Void... params) {
            if(player==null) {
                player = MediaPlayer.create(DisplayMessageActivity.this, R.raw.kalinka);
                player.setLooping(false);
                player.setVolume(100, 100);
                player.start();
            }
            return null;
        }

        private void stop() {
            if (player != null) {
                player.stop();
                player.release();
                player = null;
            }
        }

    }
    public void onDestroy() {
        super.onDestroy();
        bg.stop();
    }
}

