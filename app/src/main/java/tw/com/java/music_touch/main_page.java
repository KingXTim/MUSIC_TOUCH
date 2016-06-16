package tw.com.java.music_touch;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class main_page extends AppCompatActivity {
    MediaPlayer mPlayer;
    public static int flag = 0;
    SQLiteDB music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        View im = findViewById(R.id.ffss);
        im.setAlpha(0);
        music = new SQLiteDB(this, "music", null, 1);
        try
        {
            mPlayer = MediaPlayer.create(this, R.raw.giligili_eye);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setLooping(true);
            mPlayer.start();
            //重複播放
            //mPlayer.prepare();
            //特別使用批註的方式, 是為了提醒大家, 由於我們先前使用create method建立MediaPlayer
            //create method會自動的call prepare(), 所以我們再call prepare() method會發生 prepareAsync called in state 8的錯誤
        }catch (IllegalStateException e)
        {
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
    @Override
    protected void onStart()
    {
        super.onStart();
        mPlayer.start();
    }

    class SQLiteDB extends SQLiteOpenHelper {

        final String tablename = "music";
        public SQLiteDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            final String create = "CREATE TABLE IF NOT EXISTS "
                    + tablename + "("
                    + "id" + " INTEGER PRIMARY KEY, "
                    + "name" + " TEXT NOT NULL, "
                    + "score" + " INTRGER DEFAULT 0"
                    + ")";
            db.execSQL(create);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            final String drop = "DROP TABLE " + tablename;
            db.execSQL(drop);
            onCreate(db);
        }
    }
    private String getdata(int id){
        final SQLiteDatabase db  = music.getReadableDatabase();
        try {
            Cursor c = db.query("music", new String[]{"score"}, "id==?", new String[]{String.valueOf(id)}, null, null, "id");
            c.moveToFirst();
            String score = null;
            for(int i=0; i<c.getColumnCount(); i++) {
                score = c.getString(i);
            }
            c.close();
            return score;
        } catch (CursorIndexOutOfBoundsException e) {
            return "0";
        }
    }

    public void music(View v){
        if (flag == 0) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = MediaPlayer.create(this, R.raw.sad);
            mPlayer.setLooping(true);
            mPlayer.start();
            flag = 1;
            String sc = getdata(1);
            TextView txv=(TextView)findViewById(R.id.edit);
            sc="\n歌曲資訊\n 歌曲:我難過\n 長度:100s\n 按鍵:201鍵\n best score:\n "+sc+"\n 請再點一下";
            txv.setText(sc);
            View im = findViewById(R.id.ffss);
            im.setAlpha(1);
        } else {
            // 第二次单击buttont改变触发的事件
            flag = 0;
            Intent intent = new Intent();
            intent.setClass(main_page.this, game_page.class);
            startActivity(intent);    //觸發換頁
            finish();   //結束本頁
        }
    }

    public void menu(View v){
        if (flag!=0) {
            mPlayer.stop();
            mPlayer = MediaPlayer.create(this, R.raw.giligili_eye);
            mPlayer.setLooping(true);
            mPlayer.start();
            flag = 0;
            TextView txv = (TextView) findViewById(R.id.edit);
            txv.setText("");
            View im = findViewById(R.id.ffss);
            im.setAlpha(0);
        }
    }
    private static Boolean isExit = false;
    private static Boolean hasTask = false;

    Timer timerExit = new Timer();
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
