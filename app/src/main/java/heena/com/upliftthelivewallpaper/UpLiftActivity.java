package heena.com.upliftthelivewallpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.io.File;

public class UpLiftActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_up_lift);
    }
    public void play(View v){
        Intent intent=new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(this, GIFWallpaperService.class));
        startActivity(intent);
    }

    public void share(View v){
        try{
            ApplicationInfo app=getApplicationContext().getApplicationInfo();
            String sdImg=app.sourceDir;
            Intent in = new Intent(Intent.ACTION_SEND);
            in.setType("*/*");
            in.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            in.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(sdImg)));
            startActivity(Intent.createChooser(in, "Share  With..."));
        } catch (Exception e1) {
            e1.printStackTrace();
            Toast.makeText(getApplicationContext(), "Please Try Again Later", Toast.LENGTH_LONG).show();
        }

    }
}
