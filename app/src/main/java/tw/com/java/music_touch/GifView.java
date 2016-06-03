package tw.com.java.music_touch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import java.io.InputStream;

public class GifView extends View{

    public InputStream gifInputStream = this.getResources().openRawResource(R.raw.music0);
    private Movie gifMovie;
    private int movieWidth, movieHeight;
    private long movieDuration;
    private long movieStart;


    public GifView(Context context) {
        super(context);
        if(!isInEditMode())
        init(context);
    }

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode())
        init(context);
    }

    public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!isInEditMode())
        init(context);
    }

    private void init(Context context) {
        setFocusable(true);

        gifMovie = Movie.decodeStream(gifInputStream);
        movieWidth = gifMovie.width();
        movieHeight = gifMovie.height();
        movieDuration = gifMovie.duration();
    }
    public void set(int a) {
        gifInputStream = this.getResources().openRawResource(a);
        init(getContext());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(movieWidth,movieHeight);
    }

    public int getMovieWidth(){
        return movieWidth;
    }

    public int getMovieHeight(){
        return movieHeight;
    }

    public long getMovieDuration() {
        return movieDuration;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long now = SystemClock.uptimeMillis();

        if(movieStart == 0) {
            movieStart = now;
        }

        if(gifMovie != null) {
            int dur = gifMovie.duration();
            if(dur ==0) {
                dur = 1000;
            }

            int relTime = (int)((now - movieStart) % dur);

            gifMovie.setTime(relTime);

            gifMovie.draw(canvas,0,0);
            invalidate();
        }
    }


}
