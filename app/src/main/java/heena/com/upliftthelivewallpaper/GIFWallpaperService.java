package heena.com.upliftthelivewallpaper;

import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by fdsh on 18/07/2016.
 */
public class GIFWallpaperService extends WallpaperService {
    public void onCreate(){
        super.onCreate();
    }
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public WallpaperService.Engine onCreateEngine() {
        // TODO Auto-generated method stub
        try{
            //Movie movie=Movie.decodeStream(getResources().getMovie(R.drawable.endless_staircase));
            Movie movie=getResources().getMovie(R.raw.endless_staircase);
            return new GIFWallpaperEngine(movie);
        }
        catch (Exception e) {
            // TODO: handle exception
            Log.d("GIF", "Could Not load Asset");
            return null;
        }
    }

    private class GIFWallpaperEngine extends Engine{
        private final int frameDuration=20;
        private SurfaceHolder holder;
        private Movie movie;
        private boolean visible;
        private Handler handler;
        int mWhen;
        long mStart;
        float mScaleX,mScaleY;
        public GIFWallpaperEngine(Movie movie){
            this.movie=movie;
            handler=new Handler();
            mWhen=-1;
        }
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            // TODO Auto-generated method stub
            super.onCreate(surfaceHolder);//tick();
            this.holder=surfaceHolder;
        }
        private void draw()
        {
            tick();
            Canvas canvas=null;
            try{
                canvas=holder.lockCanvas();
                canvas.save();
                canvas.scale(mScaleX,mScaleY);
                if(canvas!=null){
                    movie.draw(canvas, 0, 0);
                }
                canvas.restore();
                //		holder.unlockCanvasAndPost(canvas);
                //movie.setTime(((int)(SystemClock.uptimeMillis()-SystemClock.uptimeMillis())%(movie.duration())));
                //movie.setTime((int)((System.currentTimeMillis()%movie.duration())));
                movie.setTime(mWhen);
            }
            finally
            {
                if(canvas!=null)
                    holder.unlockCanvasAndPost(canvas);
            }
            handler.removeCallbacks(drawGIF);
            if(visible)handler.postDelayed(drawGIF, 1000L/25L);
        }
        void tick(){
            int dur=movie.duration();
            if(mWhen==-1L)
            {
                mWhen=0;
                mStart=SystemClock.uptimeMillis();
            }
            else
            {
                long mDiff= SystemClock.uptimeMillis()-mStart;
                mWhen=(int)(mDiff%dur);
            }

        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            // TODO Auto-generated method stub
            super.onSurfaceChanged(holder, format, width, height);
            mScaleX=width/(1f*movie.width());
            mScaleY=height/(1f*movie.height());
            draw();
        }

        private Runnable drawGIF=new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                draw();
            }
        };
        @Override
        public void onDestroy() {
            // TODO Auto-generated method stub
            super.onDestroy();
            handler.removeCallbacks(drawGIF);
        }
        public void onVisibilityChanged(boolean visible) {
            // TODO Auto-generated method stub
            //super.onVisibilityChanged(visible);
            this.visible=visible;
            if(visible){
                handler.post(drawGIF);
            }
            else
            {
                handler.removeCallbacks(drawGIF);
            }
        }
    }

}
