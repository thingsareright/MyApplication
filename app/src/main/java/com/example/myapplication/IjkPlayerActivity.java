package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class IjkPlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {

    SurfaceView surfaceView;
    Button startIjk;
    Button pauseIJk;
    private IjkMediaPlayer ijkMediaPlayer;

    boolean isPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ijk_player);

        surfaceView = findViewById(R.id.video_ijk);
        startIjk = findViewById(R.id.start_ijk);
        pauseIJk = findViewById(R.id.pause_ijk);
        //注册监听器事件
        startIjk.setOnClickListener(this);
        pauseIJk.setOnClickListener(this);
        isPlay = false;

        pauseIJk.setEnabled(false);
        surfaceView.getHolder().addCallback(this);

        initPlayer();
    }

    private void initPlayer() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        ijkMediaPlayer = new IjkMediaPlayer();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        ijkMediaPlayer.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_ijk:
                ijkMediaPlayer.reset();
                try {
                    //读取视频文件地址，可以远程
                    ijkMediaPlayer.setDataSource("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
                    ijkMediaPlayer.prepareAsync(); //预加载视频
                    ijkMediaPlayer.setDisplay(surfaceView.getHolder());   //将视频画面输出到surface上
                    ijkMediaPlayer.start(); //开始播放
                    pauseIJk.setEnabled(true);
                    pauseIJk.setText("暂停");
                    isPlay = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(IjkPlayerActivity.this, "ijkPlayer播放发生错误", Toast.LENGTH_LONG).show();
                }
                break;
            case  R.id.pause_ijk:
                if (true == isPlay){
                    pauseIJk.setText("继续");
                    ijkMediaPlayer.pause();
                    isPlay = false;
                } else {
                    ijkMediaPlayer.start();
                    pauseIJk.setText("暂停");
                    isPlay = true;
                }
                break;
             default:
                 Toast.makeText(IjkPlayerActivity.this, "您已进入默认情况", Toast.LENGTH_LONG).show();
                 break;
        }
    }
}
