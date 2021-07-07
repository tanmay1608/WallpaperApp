package com.example.wallpaper;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class FullScreenWallpaper extends AppCompatActivity {
    String originalUrl="";
    PhotoView photoView;
    CircularProgressButton setWallpaperButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_wallpaper);
        final Intent intent=getIntent();
        originalUrl=intent.getStringExtra("originalUrl");
        final ProgressBar progressBar=findViewById(R.id.progress_bar);
        photoView=findViewById(R.id.photo_view);
        Glide.with(this).load(originalUrl).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(photoView);

        setWallpaperButton =findViewById(R.id.buttonSetWallpaper);
        setWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setWallpaperButton.startAnimation();
                AsyncTask<String ,String,String> demoSetWallpaper=new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {

                        try {
                            Thread.sleep(3000);
                            Log.e("t", "badiya h" );
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                            Log.e("c", "doInBackground:" );
                        }
                        return "done";
                    }

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    protected void onPostExecute(String s) {
                        if(s.equals("done"))
                        {
                            WallpaperManager wallpaperManager=WallpaperManager.getInstance(FullScreenWallpaper.this);
                            Bitmap bitmap=((BitmapDrawable) photoView.getDrawable()).getBitmap();
                            try {
                                wallpaperManager.setBitmap(bitmap);
                                Toast.makeText(FullScreenWallpaper.this, "Image set as your Wallpaper", Toast.LENGTH_SHORT).show();

                                setWallpaperButton.stopAnimation();
                                Intent intent1=new Intent(FullScreenWallpaper.this,MainActivity.class);
                                startActivity(intent1);
//                                setWallpaperButton.doneLoadingAnimation(Color.BLUE,BitmapFactory.decodeResource(getResources(),R.drawable.ic_baseline_done_24));

                            }catch (IOException e)
                            {
                                e.printStackTrace();
                                Log.e("te", "onPostExecute: ");
                            }
                        }

                    }

                };

                demoSetWallpaper.execute();

                //demoSetWallpaper.execute();

            }
        });
    }


}
