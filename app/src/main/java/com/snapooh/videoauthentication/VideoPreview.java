package com.snapooh.videoauthentication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoPreview extends AppCompatActivity implements
        TextureView.SurfaceTextureListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener,
        View.OnClickListener,SeekBar.OnSeekBarChangeListener  {
    private static final String TAG = VideoPreview.class.getSimpleName();
    private TextureView mTextureView;
    private MediaPlayer mMediaPlayer;
    private String videoUri;
  //  private SeekBar mSeekBar;
    private Handler mHandler = new Handler();
  //  private ImageView mTextureImageView;
   // private ImageView mplayView;
    private boolean isPlaying;
   // private RecyclerView recylerView;
    private LinearLayoutManager mLayoutManager;
    private List<byte[]> mDataset;
    private CustomAdapter mAdapter;
    private ImageView thumbnail;
    //private SeekBar mSeekBar;
    private Utilities utils;
    private float mVideoHeight;
    private float mVideoWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_preview);

        utils = new Utilities();
        thumbnail=(ImageView)findViewById(R.id.thumbnail);
        videoUri=getIntent().getStringExtra("videoUri");

        mTextureView = (TextureView) findViewById(R.id.textureView);
      //  mTextureImageView=(ImageView)findViewById(R.id.textureImageView);
      //  mplayView=(ImageView)findViewById(R.id.playView);
       // recylerView=(RecyclerView)findViewById(R.id.recyclerView);
      //  mSeekBar=(SeekBar)findViewById(R.id.songProgressBar);

        calculateVideoSize();
        mTextureView.setLayoutParams(new RelativeLayout.LayoutParams((int)mVideoWidth,(int)mVideoHeight));
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

      //  recylerView.setLayoutManager(mLayoutManager);

      //  mplayView.setOnClickListener(this);
        Bitmap bmFrame = getBitmap(1);
      //  mTextureImageView.setImageBitmap(bmFrame);

        mTextureView.setSurfaceTextureListener(this);
        mTextureView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    playVideo();
                }
                return false;
            }
        });
        mTextureView.requestLayout();
        mTextureView.invalidate();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
       // mSeekBar.setOnSeekBarChangeListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnCompletionListener(this);

        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);

        mMediaPlayer.setOnPreparedListener(this);


     //   Toast.makeText(this,videoUri,Toast.LENGTH_SHORT).show();

    }


    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 1000);
    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mMediaPlayer.getDuration();
            long currentDuration = mMediaPlayer.getCurrentPosition();

           /* // Displaying Total Duration time
            songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));*/

            // Updating progress bar
            int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
         //   mSeekBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    private Bitmap getBitmap(int sec) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

        mediaMetadataRetriever.setDataSource(this, Uri.parse(videoUri));
         return mediaMetadataRetriever.getFrameAtTime(sec * 1000 * 1000);
    }
    public byte[] getBitmapByteArray(Bitmap bitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video_preview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
      //  Toast.makeText(this,"onSurfaceAvailable()",Toast.LENGTH_SHORT).show();
        if (videoUri == null) {
            return;
        }
        Surface s = new Surface(surface);

        try {
            if(mMediaPlayer!=null) {
                mMediaPlayer.setSurface(s);
                mMediaPlayer.setDataSource(getRealPathFromURI(this, Uri.parse(videoUri)));
                mMediaPlayer.prepare();
            }


        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();

    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Video.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
         //  Toast.makeText(this,"onSurfaceChanged()",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
       // Toast.makeText(this,"onSurfaceDestroyed()",Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
     //   Toast.makeText(this,"onSurfaceUpdated()",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
       // Toast.makeText(this,"onMediaPrepared()",Toast.LENGTH_SHORT).show();
        mp.start();

        mDataset=new ArrayList<>();
        for(int i=0;i<((mp.getDuration()/1000)%15);i++){
            mDataset.add(getBitmapByteArray(getBitmap(i)));
        }
        mAdapter=new CustomAdapter(this,mDataset);
        mAdapter.setmItemClickListener(new CustomAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v,final int position) {

               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       thumbnail.setImageBitmap(getBitmap(position));
                       Toast.makeText(VideoPreview.this, "pos" + position,Toast.LENGTH_SHORT).show();

                   }
               });


            }
        });

       // recylerView.setAdapter(mAdapter);
    }

    public void forwardVideo(int seekForwardTime) {
        if (mMediaPlayer != null) {
            int currentPosition = mMediaPlayer.getCurrentPosition();
            if (currentPosition + seekForwardTime <= mMediaPlayer.getDuration()) {
                mMediaPlayer.seekTo(currentPosition + seekForwardTime);
            } else {
                mMediaPlayer.seekTo(mMediaPlayer.getDuration());
            }
        }
    }
    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
      //  Toast.makeText(this,"onVideoSizeChanged()",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
      //  Toast.makeText(this,"onSeekComplete()",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
       // Toast.makeText(this,"onCompletion()",Toast.LENGTH_SHORT).show();
       // mplayView.setVisibility(View.VISIBLE);
        mMediaPlayer.start();
        mMediaPlayer.setLooping(true);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
       // Toast.makeText(this,"onBufferingUpdate()",Toast.LENGTH_SHORT).show();
    }

    private void playVideo() {
        if(mMediaPlayer!=null&& isPlaying){
           // mTextureImageView.setVisibility(View.VISIBLE);
        //    mplayView.setVisibility(View.VISIBLE);
            isPlaying=false;
            mMediaPlayer.pause();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int sec=mMediaPlayer.getCurrentPosition()/100;
                    Log.i(TAG,String.valueOf(sec) );

                    thumbnail.setImageBitmap(getBitmap(sec));
                }
            });


        }else if(mMediaPlayer!=null&& !isPlaying){
            isPlaying=true;
        //    mTextureImageView.setVisibility(View.GONE);
            //mplayView.setVisibility(View.GONE);
            mMediaPlayer.start();
        }
    }

    @Override
    public void onClick(View v) {
        playVideo();
    }

    public void confirm(View view) {
       /* Intent i=new Intent(this,GridPreview.class);
        i.putExtra("videoUri",videoUri);
            startActivity(i);*/
        ArrayList<Media> mediaList=new ArrayList<>();
        String p=getRealPathFromURI(this,Uri.parse(videoUri));
        Media media=new Media(0,p,p);
        mediaList.add(media);
        media=new Media(1,p,p);
        mediaList.add(media);
        Intent intent=new Intent(this,GridPreview.class);
        Bundle bundle=new Bundle();
        bundle.putParcelableArrayList("media", mediaList);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void createBitmap(){
        try {
            Bitmap bmp = getFrametAtTime(0);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream("/sdcard/test.jpg"));
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

     public Bitmap getFrametAtTime(long time){
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();


            mediaMetadataRetriever.setDataSource(getRealPathFromURI(Uri.parse(videoUri)));
            Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(10000000); //unit in microsecond
            //s  frameThumbnail.setImageBitmap(bmFrame);

            return bmFrame;


    }
    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA );
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
    public void cancel(View view) {
        finish();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser) {
            mMediaPlayer.seekTo(progress);
            //   mSeekBar.setProgress(progress);}
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int totalDuration = mMediaPlayer.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mMediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    private void calculateVideoSize() {
        try {
          //  AssetFileDescriptor afd = getAssets().openFd(FILE_NAME);
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            /*metaRetriever.setDataSource(
                    afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());*/
            metaRetriever.setDataSource(this, Uri.parse(videoUri));
            String height = metaRetriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            String width = metaRetriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            mVideoHeight = Float.parseFloat(height);
            mVideoWidth = Float.parseFloat(width);

        }  catch (NumberFormatException e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
