package com.mediaplayer;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RecoverySystem.ProgressListener;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MusicPlay extends Activity {

    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");  
    private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();  
    private static Bitmap mCachedBit = null;  
    
	private final int NO_LOOP  = 1;
	private final int LOOP_ONE = 2;
	private final int LOOP_ALL = 3;
	private int playstate = NO_LOOP;
	private boolean isrand = false;
	//使用MediaPlayer控件，播放歌曲
	private MediaPlayer mMediaPlayer = new MediaPlayer();
	//播放按钮
	private ImageButton mPlayButton = null;
	//停止按钮
	private ImageButton mStopButton = null;
	//快退按钮
	private ImageButton mRewindButton = null;
	//快进按钮
	private ImageButton mForwardButton = null;
	//上一曲按钮
	private ImageButton mPreButton = null;
	//下一曲按钮
	private ImageButton mNextButton = null;
	//使用SeekBar显示进度
	private SeekBar mSeekBar;
	//使用TextView显示播放进度
	private TextView mProgressTextView = null;
	//使用TextView显示歌曲总时长
	private TextView mDurationTextView = null;
	//歌名显示
	private TextView mMusicTextView = null;
	//歌手/艺术家显示
	private TextView mArtistTextView = null;
	//专辑显示
	private TextView mAlbumTextView = null;
	//歌曲年份
	private TextView mYearTextView = null;
	//歌手画面显示
	private ImageView mArtistImage = null;
	//循环播放
	private TextView mLoopPlay = null;
	//随机播放
	private TextView mRandPlay = null;
	//歌曲id列表
	private int[] _ids;
	//当前播放歌曲的id
	private int _id;
	//歌曲uri
	private Uri uri;
	//当前进度
	private int curprogress;
	//进度时长字符串
	private String progressstr;
	//歌曲时长
	private int duration;
	//时长字符串
	private String durationstr;
	//歌曲名
	private String music;
	//歌手/艺术家
	private String artist;
	//专辑
	private String album;
	//年份
	private int year;
//	private String[] _titles;
//	private String[] _artists;
//	private String[] _durations;
	//歌曲列表界面得到的歌曲位置信息
	private int position;
	
	Handler handler,proghandler,fhandler,rhandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//显示歌曲播放界面
		setContentView(R.layout.musicplay);
		
		init();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		setup();
		play();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//setup();
		//play();
	}
/*
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Intent intent = getIntent();
		//获取歌曲位置
		int pos = intent.getIntExtra("position", -1);
		if(pos != position) {
			stop();
			position = pos;
			_id = _ids[position];
			if(_id >= 0) {
				uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + _id);
			}
			//若位置小于0，表示歌曲位置出错，播放第一首歌
			else {
				uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + 0);
			}
			setup();
			play();
		}
		//if(_id >= 0) {
			//uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + _id);
		//}
		//若位置小于0，表示歌曲位置出错，播放第一首歌
		//else {
			//uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + 0);
		//}
	}
*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 初始化
	 */
	protected void init() {
		//mMediaPlayer = new MediaPlayer();
		//PlayButton = (ImageButton) findViewById(R.id.btnPlay);
		//注册播放按钮的点击事件
		mPlayButton = (ImageButton)findViewById(R.id.btnPlay);
		mPlayButton.setOnClickListener(new myPlayButtonListener());
		//注册停止播放按钮事件
		mStopButton = (ImageButton) findViewById(R.id.btnStop);
		mStopButton.setOnClickListener(new myStopButtonListener());
		//注册上一曲按钮的事件
		mPreButton = (ImageButton) findViewById(R.id.btnPre);
		mPreButton.setOnClickListener(new myPreButtonListener());
		//注册下一曲的事件
		mNextButton = (ImageButton) findViewById(R.id.btnNext);
		mNextButton.setOnClickListener(new myNextButtonListener());
		//快进按钮事件
		mForwardButton = (ImageButton) findViewById(R.id.btnForward);
		mForwardButton.setOnTouchListener(new myForwardButtonListerner());
		//快退按钮事件
		mRewindButton = (ImageButton) findViewById(R.id.btnRewind);
		mRewindButton.setOnTouchListener(new myRewindButtonListerner());
		//注册进度条显示事件
		mSeekBar = (SeekBar) findViewById(R.id.seekBar);
		//循环播放事件
		mLoopPlay = (TextView) findViewById(R.id.loopplay);
		mLoopPlay.setText(R.string.noloop);
		mLoopPlay.setTextColor(0xffffffff);
		mLoopPlay.setOnClickListener(new myLoopTextListener());
		//随机播放事件
		mRandPlay = (TextView) findViewById(R.id.randplay);
		mRandPlay.setText(R.string.randplay);
		mRandPlay.setTextColor(0x66ffc6d4);
		mRandPlay.setOnClickListener(new myRandTextListener());
		mSeekBar.setOnSeekBarChangeListener(new mySeekBarListener());
		mMusicTextView = (TextView) findViewById(R.id.musicplay);
		mArtistTextView = (TextView) findViewById(R.id.artistplay);
		mAlbumTextView = (TextView) findViewById(R.id.albumplay);
		mYearTextView = (TextView) findViewById(R.id.yearplay);
		mArtistImage = (ImageView) findViewById(R.id.imgartist);
		mDurationTextView = (TextView) findViewById(R.id.durationplay);
		mProgressTextView = (TextView) findViewById(R.id.progress);
		
		handler = new Handler();
		curprogress = 0;
		duration = 0;
		//注册广播接收事件
		//IntentFilter filter = new IntentFilter();
		//filter.addAction(Intent.ACTION_EDIT);
		//this.registerReceiver(MusicReceiver, filter);
		
		getsong();
		
		//myPlayButton.performClick();
		
	}
	
	/*×
	 *获取要播放的歌曲 
	 */
	protected void getsong() {
		//获取歌曲列表界面传递的信息
		Intent intent = getIntent();
		//获取歌曲id列表
		if(intent.getIntArrayExtra("ids") != null) {
			_ids = intent.getIntArrayExtra("ids");
		}
		//获取歌曲位置
		int pos = intent.getIntExtra("position", -1);
		if(pos != -1) {
			position = pos;
			_id = _ids[position];
		}
		if(_id >= 0) {
			uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + _id);
		}
		//若位置小于0，表示歌曲位置出错，播放第一首歌
		else {
			uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + 0);
		}
		
		//myPlayButton.performClick();
	}
	
	/**
	 * 播放前设置
	 */
	protected void setup() {
		try {
			//if (mMediaPlayer.isPlaying()) {
				//mMediaPlayer.stop();
			//}
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(this, uri);
			mMediaPlayer.prepare();
			duration = 0;
			durationstr = null;
			curprogress = 0;
			
			rhandler = new Handler();
			rhandler.removeCallbacks(RewindPlay);
			fhandler = new Handler();
			fhandler.removeCallbacks(ForwardPlay);
			
			getmusicinfo();
			getalbum();
			
			mSeekBar.setProgress(curprogress);
			mSeekBar.setMax(duration);
			
			if (duration != 0) {
				handler.post(UpdateMusicInfo);
				//handler.sendEmptyMessage(1);
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			
		}
	}
	
	/**
	 * 返回
	 */
	protected void back() {
		stop();
	}
	
	/**
	 * 获取歌曲信息
	 */
	protected void getmusicinfo() {
		Cursor c = this.getContentResolver()
						.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
								new String[]{MediaStore.Audio.Media._ID,
											MediaStore.Audio.Media.TITLE,
											MediaStore.Audio.Media.ARTIST,
											MediaStore.Audio.Media.DURATION,
											MediaStore.Audio.Media.ALBUM,
											MediaStore.Audio.Media.YEAR},
								MediaStore.Audio.Media._ID + "=?", 
								new String[]{String.valueOf(_id)}, 
								null);
		
		c.moveToFirst();
		for (int i = 0;i < c.getCount() - 1;i++) {
			c.moveToNext();
		}
		if (c != null && (c.getCount() != 0)) {
		//	startManagingCursor(c);
			music = c.getString(1);
			artist = c.getString(2);
			if (artist.equals("<unknown>")) {
				artist = "未知艺术家";
			}
			duration = c.getInt(3);
			album = c.getString(4);
			year = c.getInt(5);
			durationstr = totime(duration);
		}
	}
	
	/**
	 *获取专辑封面 
	 */
	
	private void getalbum() {
		Cursor	myCur = getContentResolver().query(  
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,  
                new String[] { MediaStore.Audio.Media.TITLE,  
                        MediaStore.Audio.Media._ID,  
                        MediaStore.Audio.Media.ALBUM_ID}, null,null, null);  
        myCur.moveToPosition(position);

        long songid = myCur.getLong(1);  
        long albumid = myCur.getLong(2);  
        Bitmap bm = MusicPlay.getArtwork(this, songid, albumid,true);
        if(bm != null){  
            //Log.d(TAG,"bm is not null==========================");  
            mArtistImage.setImageBitmap(bm);  
        }else{  
            //Log.d(TAG,"bm is null============================");
        	mArtistImage.setImageResource(R.drawable.bg1);
        }  
	}
    public static Bitmap getArtwork(Context context, long song_id, long album_id,  
            boolean allowdefault) {  
        if (album_id < 0) {  
            // This is something that is not in the database, so get the album art directly  
            // from the file.  
            if (song_id >= 0) {  
                Bitmap bm = getArtworkFromFile(context, song_id, -1);  
                if (bm != null) {  
                    return bm;  
                }  
            }  
            if (allowdefault) {  
                return getDefaultArtwork(context);  
            }  
            return null;  
        }  
        ContentResolver res = context.getContentResolver();  
        Uri uri1 = ContentUris.withAppendedId(sArtworkUri, album_id);  
        if (uri1 != null) {  
            InputStream in = null;  
            try {  
                in = res.openInputStream(uri1);  
                return BitmapFactory.decodeStream(in, null, sBitmapOptions);  
            } catch (FileNotFoundException ex) {  
                // The album art thumbnail does not actually exist. Maybe the user deleted it, or  
                // maybe it never existed to begin with.  
                Bitmap bm = getArtworkFromFile(context, song_id, album_id);  
                if (bm != null) {  
                    if (bm.getConfig() == null) {  
                        bm = bm.copy(Bitmap.Config.RGB_565, false);  
                        if (bm == null && allowdefault) {  
                            return getDefaultArtwork(context);  
                        }  
                    }  
                } else if (allowdefault) {  
                    bm = getDefaultArtwork(context);  
                }  
                return bm;  
            } finally {  
                try {  
                    if (in != null) {  
                        in.close();  
                    }  
                } catch (IOException ex) {  
                }  
            }  
        }  
          
        return null;  
    }  
      
    private static Bitmap getArtworkFromFile(Context context, long songid, long albumid) {  
        Bitmap bm = null;  
        byte [] art = null;  
        String path = null;  
        if (albumid < 0 && songid < 0) {  
            throw new IllegalArgumentException("Must specify an album or a song id");  
        }  
        try {  
            if (albumid < 0) {  
                Uri uri1 = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");  
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri1, "r");  
                if (pfd != null) {  
                    FileDescriptor fd = pfd.getFileDescriptor();  
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }  
            } else {  
                Uri uri1 = ContentUris.withAppendedId(sArtworkUri, albumid);  
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri1, "r");  
                if (pfd != null) {  
                    FileDescriptor fd = pfd.getFileDescriptor();  
                    bm = BitmapFactory.decodeFileDescriptor(fd);  
                }  
            }  
        } catch (FileNotFoundException ex) {  
   
        }  
        if (bm != null) {  
            mCachedBit = bm;  
        }  
        return bm;  
    }  
      
    private static Bitmap getDefaultArtwork(Context context) {  
        BitmapFactory.Options opts = new BitmapFactory.Options();  
        opts.inPreferredConfig = Bitmap.Config.RGB_565;          
        return BitmapFactory.decodeStream(  
                context.getResources().openRawResource(R.drawable.bg1), null, opts);                 
    }  
    

	/*
	private void getalbum() {
		String filepath = "//mnt/sdcard/";
		Cursor c = this.getCursorfromPath(filepath);
	    int album_id = c.getInt(c  
                .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                
        Bitmap bm = null;  
	    String albumArt = getAlbumArt(album_id);  
	    if (albumArt == null) {  
	    	mArtistImage.setBackgroundResource(R.drawable.bg1);  
	    } else {  
	        bm = BitmapFactory.decodeFile(albumArt);  
	        BitmapDrawable bmpDraw = new BitmapDrawable(bm);  
	        mArtistImage.setImageDrawable(bmpDraw);  
	    }  
                
	}
	
	private String getAlbumArt(int album_id) {  
	    String mUriAlbums = "content://media/external/audio/albums";  
	    String[] projection = new String[] { "album_art" };  
	    Cursor cur = this.getContentResolver().query(  
	            Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),  
	            projection, null, null, null);  
	    String album_art = null;  
	    if (cur.getCount() > 0 && cur.getColumnCount() > 0) {  
	        cur.moveToNext();  
	        album_art = cur.getString(0);  
	    }  
	    cur.close();  
	    cur = null;  
	    return album_art;  
	}  
	
	private Cursor getCursorfromPath(String filePath) {  
	    String path = null;  
	    Cursor c = getContentResolver().query(  
	            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,  
	            MediaStore.Audio.Media.DEFAULT_SORT_ORDER);  
	    // System.out.println(c.getString(c.getColumnIndex("_data")));  
	    if (c.moveToFirst()) {  
	        do {  
	            // 通过Cursor 获取路径，如果路径相同则break；  
	            path = c.getString(c  
	                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));  
	            // 查找到相同的路径则返回，此时cursorPosition 便是指向路径所指向的Cursor 便可以返回了  
	            if (path.equals(filePath)) {  
	                // System.out.println("audioPath = " + path);  
	                // System.out.println("filePath = " + filePath);  
	                // cursorPosition = c.getPosition();  
	                break;  
	            }  
	        } while (c.moveToNext());  
	    }  
	    // 这两个没有什么作用，调试的时候用  
	    // String audioPath = c.getString(c  
	    // .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));  
	    //  
	    // System.out.println("audioPath = " + audioPath);  
	    return c;  
	}  
	*/
	
	/**
	 * 将整型的歌曲时长转换为时间格式
	 */
    private String totime(int time) {
		int hour,min,sec;
		time /= 1000;
		sec = time % 60;
		min = time /60;
		hour = min /60;
		min %= 60;
		
		return String.format("%02d:%02d", min, sec);
	}

	/**
	 * 播放歌曲
	 */
	protected void play() {
		if (mMediaPlayer != null && (!mMediaPlayer.isPlaying())) {
			
			fhandler.removeCallbacks(ForwardPlay);
			rhandler.removeCallbacks(RewindPlay);
			
			//mMediaPlayer.seekTo(curprogress);
		
			proghandler = new Handler();
			proghandler.post(UpdateProgress);
				
			mMediaPlayer.start();
			
			//final Intent intent = new Intent();
			//intent.setAction(Intent.ACTION_EDIT);
			if (proghandler == null) {
				proghandler = new Handler() {
					
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						//if(msg.what == 1) {
						curprogress = mMediaPlayer.getCurrentPosition();
							//System.out.println("play-----" +String.valueOf(curprogress));
							//intent.putExtra("process", curprogress);
							//sendBroadcast(intent);
							//handler.sendEmptyMessageDelayed(1, 600);
						}
					//}
				};
			}
		}
	}
	
	/**
	 * 暂停播放
	 */
	protected void pause() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			mMediaPlayer.pause();
		}	
	}
	
	/**
	 * 停止播放
	 */
	protected void stop() {
		try {
			if(mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			handler.post(SetPlayButton);
			handler = null;
			proghandler.removeCallbacks(UpdateProgress);
			fhandler.removeCallbacks(ForwardPlay);
			rhandler.removeCallbacks(RewindPlay);
			curprogress = 0;
			mSeekBar.setProgress(curprogress);
			progressstr = "";
			mProgressTextView.setText(progressstr);
			//init();
			//setup();
			
			//Intent intent = new Intent();
			//intent.setClass(this, MediaPlayerActivity.class);
			//startActivity(intent);
			//finish();
			
		}
		catch (Exception e) {
			
		}
		finally {
			
		}
		//unregisterReceiver(MusicReceiver);
	}
	
	/**
	 * 更新播放进度
	 */
	Runnable UpdateProgress = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			fhandler.removeCallbacks(ForwardPlay);
			rhandler.removeCallbacks(RewindPlay);
			curprogress = mMediaPlayer.getCurrentPosition();
			//System.out.println("updateprogress----" + String.valueOf(curprogress));
			progressstr = totime(curprogress);
			if (curprogress <= duration - 300) {
				mProgressTextView.setText(progressstr);
				mSeekBar.setProgress(curprogress);
				proghandler.postDelayed(UpdateProgress, 500);
			}
			else if(curprogress >= duration - 300) {
				//stop();
				if (isrand == true) {
					randsong();
					setup();
					play();
				}
				else {
					switch (playstate) {
					case NO_LOOP:
						stop();
						break;
					case LOOP_ONE:
						setup();
						play();
						//mMediaPlayer.setLooping(true);
						break;
					case LOOP_ALL:
						//if (mMediaPlayer.isLooping()) {
						//	mMediaPlayer.setLooping(false);
						//}
						nextone();
						break;
					}
				}
				
			}
			else {
				proghandler.removeCallbacks(UpdateProgress);
			}
		}
	};
	
	/**
	 * 随机播放歌曲
	 */
	protected void randsong() {
		Random rand = new Random();
		int newpos = rand.nextInt(_ids.length);
		if (newpos >= 0 && newpos <= _ids.length - 1) {
			position = newpos;
			_id = _ids[position];
			uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + _id);
		}
	}
	
	/**
	 * 快进
	 */
	Runnable ForwardPlay = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//mMediaPlayer.pause();
			proghandler.removeCallbacks(UpdateProgress);
			curprogress = mMediaPlayer.getCurrentPosition() + 3000;
			if (curprogress <= duration) {
				progressstr = totime(curprogress);
				mProgressTextView.setText(progressstr);
				mSeekBar.setProgress(curprogress);
				mMediaPlayer.seekTo(curprogress);
				//System.out.println("forward---" + String.valueOf(curprogress));
				//mMediaPlayer.start();
				fhandler.postDelayed(ForwardPlay, 500);
			}
			else {
				fhandler.removeCallbacks(ForwardPlay);
			}
		}
	};
	
	/**
	 * 快退
	 */
	Runnable RewindPlay = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			//mMediaPlayer.pause();
			proghandler.removeCallbacks(UpdateProgress);
			curprogress = mMediaPlayer.getCurrentPosition() - 3000;
			if (curprogress >= 0) {
				progressstr = totime(curprogress);
				mProgressTextView.setText(progressstr);
				mSeekBar.setProgress(curprogress);
				mMediaPlayer.seekTo(curprogress);
				//mMediaPlayer.start();
				rhandler.postDelayed(RewindPlay, 500);
			}
			else {
				rhandler.removeCallbacks(RewindPlay);
			}
		}
	};
	
	/**
	 * 点击播放按钮
	 */
	Runnable SetPlayButton = new Runnable(){
		
		@Override
		public void run(){
			
			//更改播放按钮，显示“暂停”
			mPlayButton.setBackgroundResource(R.drawable.play_selector);
		}
	};
	
	Runnable SetPauseButton = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			mPlayButton.setBackgroundResource(R.drawable.pause_selector);
		}
	};
	
	/**
	 * 显示歌曲信息
	 */
	Runnable UpdateMusicInfo = new Runnable() {
		
		@Override
		public void run() {
			mMusicTextView.setText(music);
			mArtistTextView.setText(artist);
			mAlbumTextView.setText(album);
			//YearTextView.setText(String.valueOf(year));
			if (year == 0) {
				mYearTextView.setText("");
			}
			else {
				mYearTextView.setText(String.valueOf(year));
			}
			mDurationTextView.setText(durationstr);
			mProgressTextView.setText("00:00");
		}
	} ;
	
	/**
	 * 播放按钮事件
	 */
	class myPlayButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			try {
				//若当前正在播放歌曲，则暂停播放
				if(mMediaPlayer.isPlaying()) {
					//handler.post(SetStopButton);
					pause();
					handler.post(SetPlayButton);
				}
				//若当前没有播放歌曲，则播放
				else {
					//handler.post(SetPlayButton);
					play();
					handler.post(SetPauseButton);
				}
			}
			catch(Exception e) {
				System.out.println(e.toString());
			}
		}
	}
	
	/**
	 * 停止播放按钮事件
	 */
	class myStopButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			stop();
		}
	}
	
	/**
	 * 上一曲按钮事件
	 */
	class myPreButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			preone();
		}
	}
	
	/**
	 *下一曲按钮事件
	 */
	class myNextButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			nextone();
		}
	}
	
	/**
	 * 快进按钮
	 */
	class myForwardButtonListerner implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					pause();
					fhandler.post(ForwardPlay);
					break;
				case MotionEvent.ACTION_UP:
					play();
					//mMediaPlayer.seekTo(curprogress);
					//handlerprogress.post(UpdateProgress);
					break;
			}
			return true;
		}
		
	}
	/**
	 * 快退按钮
	 */
	class myRewindButtonListerner implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					pause();
					rhandler.post(RewindPlay);
					break;
				case MotionEvent.ACTION_UP:
					play();
					//handlerprogress.post(UpdateProgress);
					break;
			}
			return true;
		}
	}
	
	/**
	 * 循环播放事件
	 */
	class myLoopTextListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (playstate) {
			case NO_LOOP:
				playstate = LOOP_ONE;
				mLoopPlay.setText(R.string.loopone);
				//mLoopPlay.setTextColor(0x55ffc6d4);
				break;
			case LOOP_ONE:
				playstate = LOOP_ALL;
				mLoopPlay.setText(R.string.loopall);
				//mLoopPlay.setTextColor(0x55ffc6d4);
				break;
			case LOOP_ALL:
				playstate = NO_LOOP;
				mLoopPlay.setText(R.string.noloop);
				//mLoopPlay.setTextColor(0x55ffc6d4);
				break;
			}
			isrand = false;
		}
	}
	
	/**
	 * 随机播放事件
	 */
	class myRandTextListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (isrand == false) {
				isrand = true;
				//if (mMediaPlayer.isLooping()) {
				//	mMediaPlayer.setLooping(false);
				//}
				mRandPlay.setTextColor(0xffffffff);
				randsong();
				//setup();
				//play();
			}
			else {
				isrand = false;
				mRandPlay.setTextColor(0x66ffc6d4);
			}
		}
	}
	
	/**
	 * 进度条事件
	 */
	class mySeekBarListener implements OnSeekBarChangeListener{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			if (fromUser == true) {
				seekbarchange(progress);
			}
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			pause();
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			play();
		}
	}
	
	/**
	 * 上一曲
	 */
	protected void preone() {
		try {
			if(_ids.length == 1) {
				position = _ids.length - 1;
				_id = _ids[position];
			}
			else if(position == 0) {
				position = _ids.length - 1;
				_id = _ids[position];
			}
			else if(position > 0 && position <= _ids.length - 1){
				position--;
				_id = _ids[position];
			}
			else {
				position = 0;
				_id = _ids[position];
			}
			uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + _id);
			setup();
			play();
		}
		catch (Exception e) {
			
		}
		finally {
			
		}
	}
	
	/**
	 * 下一曲
	 */
	protected void nextone() {
		try {
			if(_ids.length == 1) {
				position = _ids.length - 1;
				_id = _ids[position];
			}
			else if(position >= (_ids.length - 1)) {
				position = 0;
				_id = _ids[position];
			}
			else if(position < _ids.length - 1) {
				position++;
				_id = _ids[position];
			}
			else {
				position = 0;
				_id = _ids[position];
			}
			
			uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,"" + _id);
			setup();
			play();
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
		finally {
			
		}
	}
	
	/**
	 * 进度改变
	 */
	protected void seekbarchange(int progress) {
		mMediaPlayer.pause();
		proghandler.removeCallbacks(UpdateProgress);
		mMediaPlayer.seekTo(progress);
		//seekbar.setProgress(progress);
		curprogress = progress;
		proghandler.post(UpdateProgress);
	}
	
	/**
	 * 定义musicReceiver，接收广播
	 */
	/*
	protected BroadcastReceiver MusicReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			System.out.println("reveive msg from musicreceive");
			String action = intent.getAction();
			if (action.equals(intent.ACTION_EDIT)) {
				int curtime = 0;
				curtime = intent.getExtras().getInt("progress");
				if (curtime >= 0) {
					curprogress = curtime;
					progressstr = totime(curprogress);
					ProgressTextView.setText(curprogress);
					SeekBar.setProgress(curprogress);
				}
				
			}
		}
	};
	*/
}
