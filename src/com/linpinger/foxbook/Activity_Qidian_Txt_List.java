package com.linpinger.foxbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Activity_Qidian_Txt_List extends ListActivity {
	private List<Map<String, Object>> data;
	private ListView lv_pagelist ;
	SimpleAdapter adapter;
	public FoxMemDB oDB  ; // 默认使用MemDB
	
	private void renderListView() { // 刷新LV
		adapter = new SimpleAdapter(this, data,
				android.R.layout.simple_list_item_1, new String[] { "name" },
				new int[] { android.R.id.text1 });
		lv_pagelist.setAdapter(adapter);
	}
	
	private void init_LV_item_click() { // 初始化 单击 条目 的行为
		OnItemClickListener listener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, Object> chapinfo = (HashMap<String, Object>) parent.getItemAtPosition(position);
				String tmpurl = (String) chapinfo.get("url");
				String tmpname = (String) chapinfo.get("name");
				Integer tmpid = (Integer) chapinfo.get("id");

				// setTitle(parent.getItemAtPosition(position).toString());
				Intent intent = new Intent(Activity_Qidian_Txt_List.this,
						Activity_ShowPage.class);
				intent.putExtra("iam", 1); // from DB
				intent.putExtra("chapter_id", tmpid);
				intent.putExtra("chapter_name", tmpname);
				intent.putExtra("chapter_url", tmpurl);
				intent.putExtra("searchengine", 1); // SE
				Activity_ShowPage.oDB = oDB;
				startActivity(intent);
			}
		};
		lv_pagelist.setOnItemClickListener(listener);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) { // 入口
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qidian_txt_list);
		lv_pagelist = getListView();
		
		// 获取传入的数据
		Intent itt = getIntent();
//		if ( itt.getScheme().equalsIgnoreCase("file") ) { // 处理txt文件的读取
		String txtPath = itt.getData().getPath(); // 从intent获取txt路径
		
		oDB = new FoxMemDB(this.getApplicationContext()) ; // 创建内存数据库
		String BookName = FoxMemDBHelper.importQidianTxt(txtPath, oDB); //导入txt到数据库
			
		foxtip("处理:" + txtPath);
		setTitle(BookName);
		
		data = FoxMemDBHelper.getPageList("", oDB); // 获取页面列表
				
		renderListView();  // 处理好data后再刷新列表
		init_LV_item_click() ; // 初始化 单击 条目 的行为
	}
	
	private void foxtip(String sinfo) { // Toast消息
		Toast.makeText(getApplicationContext(), sinfo, Toast.LENGTH_SHORT).show();
	}
}
