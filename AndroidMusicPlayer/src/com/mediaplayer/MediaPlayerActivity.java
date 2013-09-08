package com.mediaplayer;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MediaPlayerActivity extends ListActivity {
    /** Called when the activity is first created. */
	
	//歌曲列表显示
	private ListView mListView;
	//歌曲图片
	private ImageView mImageView;
	//歌曲id列表
	private int[] _ids;
	//歌曲名称列表
	private String[] _titles;
	//歌曲歌手列表
	private String[] _artists;
	//歌曲时长列表
	private String[] _durations;
	//歌曲信息映射
	private ArrayList<HashMap<String,String>> songlist = new ArrayList<HashMap<String,String>>();
	//ListView adapter
	private SimpleAdapter adapter = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示歌曲列表界面
        setContentView(R.layout.listsong);
        
        mListView = new ListView(this);
        mImageView = (ImageView) findViewById(R.id.imageitem);
        
        try {
        	//歌曲列表显示
        	ListSongs();
        }
        catch (Exception e) {
        	System.out.println(e.getMessage());
        }
        finally {
        	
        }
    }

	/**
     * 歌曲浏览
     */
    private void ListSongs() {
		//listview = new ListView(this);
		Cursor c = this.getContentResolver()
						.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 	//查询扩展卡内的歌曲
								new String[]{MediaStore.Audio.Media.TITLE,
								MediaStore.Audio.Media.DURATION,
								MediaStore.Audio.Media.ARTIST,
								MediaStore.Audio.Media._ID}, 
								null,null,null
								);
		//若查询结果为空
		if(c == null || c.getCount() == 0) {
			AlertDialog ad =  null;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("歌曲信息为空").setPositiveButton("确定", null);
			ad = builder.create();
			ad.show();
		}
		c.moveToFirst();
		//获取歌曲信息
		_ids = new int[c.getCount()];
		_titles = new String[c.getCount()];
		_artists = new String[c.getCount()];
		_durations = new String[c.getCount()];
		for(int i = 0;i < c.getCount();i++) {
			_ids[i] = c.getInt(3);
			_titles[i] = c.getString(0);
			_artists[i] = c.getString(2);
			if (_artists[i].equals("<unknown>")) {
				_artists[i] = "未知艺术家";
			}
 			_durations[i] = totime(c.getInt(1));
			c.moveToNext();
		}
		HashMap<String, String>  songmap = new HashMap<String, String>();
		//歌曲信息显示
		for(int i = 0;i < c.getCount();i++) {
			//songmap.put("id",String.format("%d", _ids[i]));
			songmap = new HashMap<String, String>();
			songmap.put("title",_titles[i]);
			songmap.put("artist", _artists[i]);
			songmap.put("duration",_durations[i]);
			songlist.add(songmap);
			adapter = new SimpleAdapter(this, 
										songlist, 
										R.layout.listsongcontent,
										new String[]{"title","artist","duration"}, 
										new int[]{R.id.music,R.id.artist,R.id.duration}
										);
			setListAdapter(adapter);	
		}
	}
    
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
    
    //重写歌曲列表点击事件
    //当点击某一歌曲时，跳转到歌曲播放界面，播放相应歌曲
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		//System.out.println("id--" + id);
		//System.out.println("position--" + position);
		
		//跳转到播放界面
		//将歌曲列表及点击到的歌曲位置传到歌曲播放界面
		Intent intent = new Intent();
		intent.putExtra("ids", _ids);
//		intent.putExtra("titles", _titles);
//		intent.putExtra("artists", _artists);
//		intent.putExtra("durations",_durations);
		intent.putExtra("position", position);
		
		intent.setClass(this,MusicPlay.class);
		startActivity(intent);
	}
    
}