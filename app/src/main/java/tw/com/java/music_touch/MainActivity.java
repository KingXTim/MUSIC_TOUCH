package tw.com.java.music_touch;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity{

    GifView gifView;
    MediaPlayer mPlayer;
    private static Boolean isExit = false;
    private static Boolean hasTask = false;
    Timer timerExit = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        gifView = (GifView) findViewById(R.id.gif_view);
        if (dm.widthPixels >= 1000) gifView.set(R.raw.music0);
        else gifView.set(R.raw.music);

        try {
            mPlayer = MediaPlayer.create(this, R.raw.giligili_eye);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setLooping(true);
            //重複播放
            //mPlayer.prepare();
            //特別使用批註的方式, 是為了提醒大家, 由於我們先前使用create method建立MediaPlayer
            //create method會自動的call prepare(), 所以我們再call prepare() method會發生 prepareAsync called in state 8的錯誤
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        mPlayer.start();
    }

    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
        mPlayer.pause();
    }

    public void gamestart(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, main_page.class);
        startActivity(intent);    //觸發換頁
        finish();   //結束本頁
    }

    TimerTask task = new TimerTask() {
            @Override
             public void run() {
                 isExit = false;
                 hasTask = true;
             }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判斷是否按下Back
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否要退出
                     if(isExit == false ) {
                         isExit = true; //記錄下一次要退出
                         Toast.makeText(this, "再按一次Back退出APP"
                          , Toast.LENGTH_SHORT).show();
                // 如果超過兩秒則恢復預設值
                         if(!hasTask) {
                             timerExit.schedule(task, 2000);
                         }
                     } else {
                         finish(); // 離開程式
                         System.exit(0);
                     }
        }
        return false;
    }
}

