//package com.zrt.mybase.zpd;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.app.ActionBar;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TabHost;
//import android.widget.TabWidget;
//import android.widget.TextView;
//
//import com.zpdyf.mobileemr.R;
//import com.zpdyf.mobileemr.ZpdyfBaseActivity;
//import com.zpdyf.mobileemr.db.wrapper.HuanzheWrapper;
//import com.zpdyf.mobileemr.tools.StringHelper;
//import com.zpdyf.mobileemr.utils.Constants;
//
///**
// * 盐城三院 V9.3.4.5
// * 标本检查 离线提交优化
// * @author Administrator
// *
// */
//public class BiaobenListActivity extends ZpdyfBaseActivity
//{
//	ActionBar actionBar;
//	private BiaobenListViewAdapter adapter;
//	private BiaobenListViewAdapter adapter_one;
//	private ListView biaoben_dialog;
//	private AlertDialog.Builder builder;
//	private View dialoagView;
//	private AlertDialog dialog;
//	private boolean dialogState = false;
//	private TextView dialog_title;
//	private List<Map<String, Object>> listdata;
//	private ArrayList<Map<String, Object>> onedata;
//	private String tab_state = "weihedui";
//	private TabHost tabhost;
//	private View v;
//	private ListView weihedui;
//	private ListView yijiaodui;
//	private ListView jieshou;
//
//	/**扫描**/
//	private boolean currentScanBar;
//	private String mScanLastTiaoma = "";
//	private boolean huandialogState=false;
//	private void initData()
//	{
//		setDingbuHuanZheXinXi();
//		this.weihedui = (ListView)findViewById(R.id.weihedui);
//		this.yijiaodui = (ListView)findViewById(R.id.yijiaodui);
//		this.jieshou = (ListView)findViewById(R.id.jieshou);
//		this.listdata = new ArrayList<Map<String,Object>>();
//		this.onedata = new ArrayList<Map<String,Object>>();
//		getWeiheduiData();
//
//		this.tabhost = (TabHost)findViewById(R.id.mytab);
//		this.tabhost.setup();
//		TabWidget localTabWidget = this.tabhost.getTabWidget();
//		this.tabhost.addTab(this.tabhost.newTabSpec("weihedui").setIndicator("未核对").setContent(R.id.weihedui));
//		this.tabhost.addTab(this.tabhost.newTabSpec("yijiaodui").setIndicator("已核对").setContent(R.id.yijiaodui));
////    this.tabhost.addTab(this.tabhost.newTabSpec("jieshou").setIndicator("已接收").setContent(R.id.jieshou));
//
//		Intent intent = getIntent();
//		String stringExtra = intent.getStringExtra("paramString");
//		if(null!=stringExtra&&!"".equals(stringExtra)){
//			biaobenDialog(stringExtra);
//		}
//
//		for (int i = 0; i < localTabWidget.getChildCount(); i++) {
//
//			TextView localTextView = (TextView)localTabWidget.getChildAt(i).findViewById(16908310);
//			localTextView.setTextSize(16.0F);
//			localTextView.setTextColor(getResources().getColorStateList(2131296268));
//
////		TextView localTextView = (TextView) localTabWidget.getChildAt(i).findViewById(android.R.id.title);
////		localTextView.setTextSize(16.0F);
////		localTextView.setTextColor(getResources().getColorStateList(android.R.color.tab_indicator_text));
//		}
//
//		this.tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
//		{
//			public void onTabChanged(String paramAnonymousString)
//			{
//				updateUserActionTime();	// 更新用户最后动作时间
//
//				if (paramAnonymousString.equals("weihedui"))
//				{
//					getWeiheduiData();
//					tab_state = "weihedui";
//				}else if (paramAnonymousString.equals("yijiaodui")){
//					getYiheduiData();
//					tab_state = "yijiaodui";
//				}else{
//					getJieShouData();
//					tab_state = "jieshou";
//				}
//			}
//		});
//
//	}
//
//
//	//TODO
//	public void biaobenDialog(final String paramString)
//	{
//		String str0 = "SELECT * FROM zhuyuan_fuzhujiancha WHERE " + getTiaomaQueryMode(paramString) + " ";
//		Cursor localCursor0 = this.db.rawQuery(str0, new String[0]);
//		if(localCursor0.getCount() <= 0){
//			localCursor0.close();
//			tipDialogWithError(this.context, "提示", "该标本不存在");
//			return;
//		}
//
//		if(this.current_application.biaoben_hedui_zidong_qiehuan_huanzhe){
//			String zhuyuan_id = "";
//			String jiancha_zhuangtai = "";
//			while (localCursor0.moveToNext()) {
//				zhuyuan_id = localCursor0.getString(localCursor0.getColumnIndex("zhuyuan_id"));
//				jiancha_zhuangtai = localCursor0.getString(localCursor0.getColumnIndex("jiancha_zhuangtai"));
//			}
//			localCursor0.close();
//
//			// 如果扫描的配液瓶签患者不是当前患者，切换患者执行
//			if(!zhuyuan_id.equals(this.current_application.current_patient_zhuyuan_id)){
//				HuanzheWrapper localHuanzheWrapper = this.mZhuyuanHuanzheDao.getHuanzheByZhuyuanID(zhuyuan_id);
//				if (localHuanzheWrapper == null)
//				{
//					tipDialogWithError(this.context, "提示", "该患者不存在");
//					return;
//				}else{
//					localHuanzheWrapper.setGlobalData(this.current_application);
//					setDingbuHuanZheXinXi();
//					if ("已核对".equals(jiancha_zhuangtai)){
//						if  (tab_state.equals("yijiaodui")) {
//							getYiheduiData();
//						}
//						tabhost.setCurrentTabByTag("yijiaodui");
//					}else if("已接收".equals(jiancha_zhuangtai)){
//						tabhost.setCurrentTabByTag("jieshou");
//					}else{
//						if  (tab_state.equals("weihedui")) {
//							getWeiheduiData();
//						}
//						tabhost.setCurrentTabByTag(tab_state);
//					}
//				}
//			}
//		}
//		this.onedata = new ArrayList<Map<String,Object>>();
//		String stra = "SELECT * FROM zhuyuan_fuzhujiancha WHERE zhuyuan_id = '" + this.current_application.current_patient_zhuyuan_id + "' and "+getTiaomaQueryMode(paramString)+" ";
//		Cursor localCursora = this.db.rawQuery(stra, new String[0]);
//		if(localCursora.getCount() <= 0){
//			localCursora.close();
//			tipDialogWithError(this.context, "提示", "该标本不属于当前患者，请核对！");
//			return;
//		}
//		while (localCursora.moveToNext()){
//			//TODO
//			String str2 = "";
//			String str3 = "SELECT * FROM zhuyuan_fuzhujiancha WHERE zhuyuan_id = '" + this.current_application.current_patient_zhuyuan_id + "' and tiaoma = '" + localCursora.getString(localCursora.getColumnIndex("tiaoma")) + "' ORDER BY id ASC ";
//			Cursor localCursor3 = this.db.rawQuery(str3, new String[0]);
//			while (localCursor3.moveToNext()){
//				str2 = str2 + localCursor3.getString(localCursor3.getColumnIndex("jiancha_mingcheng"));
//				if (!localCursor3.isLast())
//					str2 = str2 + "\r\n";
//			}
//			localCursor3.close();
//			HashMap<String, Object> localHashMap = new HashMap<String, Object>();
//			localHashMap.put("id", localCursora.getString(localCursora.getColumnIndex("id")));
//			localHashMap.put("mingcheng", str2);
//			localHashMap.put("songjianwu", localCursora.getString(localCursora.getColumnIndex("songjianwu")));
//			localHashMap.put("keshi", localCursora.getString(localCursora.getColumnIndex("jiancha_keshi")));
//			localHashMap.put("state", localCursora.getString(localCursora.getColumnIndex("jiancha_zhuangtai")));
//			localHashMap.put("tiaoma",localCursora.getString(localCursora.getColumnIndex("tiaoma")));
//			localHashMap.put("hedui_hushi_mingcheng", localCursora.getString(localCursora.getColumnIndex("hedui_hushi_mingcheng")));
//			localHashMap.put("hedui_hushi_id", localCursora.getString(localCursora.getColumnIndex("hedui_hushi_id")));
//			localHashMap.put("hedui_time", localCursora.getString(localCursora.getColumnIndex("hedui_time")));
//			localHashMap.put("submission_hushi_name", localCursora.getString(localCursora.getColumnIndex("submission_hushi_name")) == null ? "" : localCursora.getString(localCursora.getColumnIndex("submission_hushi_name")));
//			localHashMap.put("submission_hushi_id", localCursora.getString(localCursora.getColumnIndex("submission_hushi_id")) == null ? "" : localCursora.getString(localCursora.getColumnIndex("submission_hushi_id")));
//			localHashMap.put("submission_hushi_time", localCursora.getString(localCursora.getColumnIndex("submission_hushi_time")) == null ? "" : localCursora.getString(localCursora.getColumnIndex("submission_hushi_time")));
//			localHashMap.put("jiancha_code", localCursora.getString(localCursora.getColumnIndex("jiancha_code")) == null ? "" : localCursora.getString(localCursora.getColumnIndex("jiancha_code")));
//			localHashMap.put("jiancha_biaoben_yanse", localCursora.getString(localCursora.getColumnIndex("jiancha_biaoben_yanse")) == null ? "" : localCursora.getString(localCursora.getColumnIndex("jiancha_biaoben_yanse")));
//			localHashMap.put("jiancha_biaoben_shuoming", localCursora.getString(localCursora.getColumnIndex("jiancha_biaoben_shuoming")) == null ? "" : localCursora.getString(localCursora.getColumnIndex("jiancha_biaoben_shuoming")));
//			localHashMap.put("jiancha_biaoben_rongqi", localCursora.getString(localCursora.getColumnIndex("jiancha_biaoben_rongqi")) == null ? "" : localCursora.getString(localCursora.getColumnIndex("jiancha_biaoben_rongqi")));
//			localHashMap.put("receive_hushi_name", localCursora.getString(localCursora.getColumnIndex("receive_hushi_name"))  == null ? "" : localCursora.getString(localCursora.getColumnIndex("receive_hushi_name")));
//			localHashMap.put("receive_hushi_id", localCursora.getString(localCursora.getColumnIndex("receive_hushi_id")) == null ? "" : localCursora.getString(localCursora.getColumnIndex("receive_hushi_id")));
//			localHashMap.put("receive_hushi_time", localCursora.getString(localCursora.getColumnIndex("receive_hushi_time")) == null ? "" : localCursora.getString(localCursora.getColumnIndex("receive_hushi_time")));
//			this.onedata.add(localHashMap);
//		}
//		localCursora.close();
//		final String state = onedata.get(0).get("state").toString();
//		if  ("接收".equals(state) || "已核对".equals(state)){
//
//			if("接收".equals(state)){
//				if  (tab_state.equals("jieshou")) {
//					getJieShouData();
//				}
//				tabhost.setCurrentTabByTag("jieshou");
//			}else{
//				if  (tab_state.equals("yijiaodui")) {
//					getYiheduiData();
//				}
//				tabhost.setCurrentTabByTag("yijiaodui");
//			}
//			tipDialogWithError(this.context, "提示", "这个标本已"+state);
//			return;
//		}
//
//		playMedia("beep");
//		tab_state = "weihedui";
//		tabhost.setCurrentTabByTag(tab_state);
//		getWeiheduiData();
//
//		//播报患者信息
//		playSoundHuanzheInfo();
//
//		if(dialogState && !"".equals(mScanLastTiaoma) && mScanLastTiaoma.equals(paramString)){
//			dialogState = false;
//			mScanLastTiaoma = "";
//			insertBiaobenData(paramString, "已核对", "0");
//			return;
//		}
//
//
//		if(current_application.scan_biaobenhedui_dialog){
//			insertBiaobenData(paramString, "已核对", "0");
//			return;
//		}
//
//		this.builder = new AlertDialog.Builder(this);
//		this.dialoagView = getLayoutInflater().inflate(R.layout.click_dialog, null);
//		this.builder.setView(this.dialoagView);
//		this.builder.setPositiveButton("核对", new DialogInterface.OnClickListener()
//		{
//			public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
//			{
//				insertBiaobenData(paramString, "已核对", "0");
//			}
//		}).setNegativeButton("取消", new DialogInterface.OnClickListener()
//		{
//			public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
//			{
//				dialogState = false;
//			}
//		}).setOnKeyListener(new DialogInterface.OnKeyListener()
//		{
//			public boolean onKey(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
//			{
//				dialog.dismiss();
//				return false;
//			}
//		});
//		this.dialog = this.builder.show();
//		this.dialog.setCancelable(false);
//		this.dialogState = true;
//		this.mScanLastTiaoma = paramString;
//
//
//
//
//
//
//		this.dialog_title = (TextView)this.dialoagView.findViewById(R.id.dialog_title);
//		this.dialog_title.setText(state);
//		this.biaoben_dialog = (ListView)this.dialoagView.findViewById(R.id.biaoben_dialog);
//		this.adapter_one = new BiaobenListViewAdapter(this, this.onedata);
//		this.biaoben_dialog.setAdapter(this.adapter_one);
//
//	}
//
//
//	//TODO
//	public void getWeiheduiData()
//	{
//
//		this.listdata = new ArrayList<Map<String,Object>>();
//		listdata.addAll(queryBiaobenData("未核对"));
//
//		this.adapter = new BiaobenListViewAdapter(this, this.listdata);
//		this.weihedui.setAdapter(this.adapter);
//		this.weihedui.setOnItemClickListener(new AdapterView.OnItemClickListener()
//		{
//			public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, final int paramAnonymousInt, long paramAnonymousLong)
//			{
//
//				updateUserActionTime();	// 更新用户最后动作时间
//				if(current_application.shoudian_biaobenhedui){
//					return;
//				}
//				final Map<String, Object> map = listdata.get(paramAnonymousInt);
//				LayoutInflater localLayoutInflater = getLayoutInflater();
//				dialoagView = localLayoutInflater.inflate(R.layout.click_dialog, null);
//				AlertDialog.Builder localBuilder = new AlertDialog.Builder(BiaobenListActivity.this);
//				localBuilder.setView(dialoagView);
//				localBuilder.setPositiveButton("核对", new DialogInterface.OnClickListener()
//				{
//					public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
//					{
//						if(null == listdata || listdata.size() == 0 || listdata.size() <= paramAnonymousInt){
//							return;
//						}
//						insertBiaobenData(StringHelper.get2(map, "tiaoma"), "已核对", "1");
//
//					}
//				}).setNegativeButton("取消", new DialogInterface.OnClickListener()
//				{
//					public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
//					{
//					}
//				});
//				localBuilder.show();
//				if(null == listdata || listdata.size() == 0 || listdata.size() <= paramAnonymousInt){
//					return;
//				}
//				dialog_title = (TextView)dialoagView.findViewById(R.id.dialog_title);
//				dialog_title.setText(map.get("state").toString());
//				biaoben_dialog = (ListView)dialoagView.findViewById(R.id.biaoben_dialog);
//				ArrayList<Map<String, Object>>  localArrayList = new ArrayList<Map<String,Object>>();
//				localArrayList.add(map);
//				adapter_one = new BiaobenListViewAdapter(BiaobenListActivity.this, localArrayList);
//				biaoben_dialog.setAdapter(adapter_one);
//			}
//		});
//
//	}
//
//	public void getYiheduiData() {
//		this.listdata = new ArrayList<Map<String,Object>>();
//		listdata.addAll(queryBiaobenData("已核对"));
//		this.adapter = new BiaobenListViewAdapter(this, this.listdata);
//		this.yijiaodui.setAdapter(this.adapter);
//	}
//
//	public void getJieShouData(){
//		this.listdata = new ArrayList<Map<String,Object>>();
//		listdata.addAll(queryBiaobenData("已接收"));
//		this.adapter = new BiaobenListViewAdapter(this, this.listdata);
//		this.jieshou.setAdapter(this.adapter);
//	}
//
//	protected void onCreate(Bundle paramBundle) {
//		super.onCreate(paramBundle);
//		setContentView(R.layout.activity_biaoben_list);
//		this.actionBar = getActionBar();
//		this.actionBar.setCustomView(R.layout.common_header);
//		this.actionBar.setDisplayOptions(16);
//		this.actionBar.setDisplayShowCustomEnabled(true);
//		this.currentScanBar = true;
//		super.initUserInfo();
//		super.openRightHuanzheList();
//		this.tiaoZhuangActivity = BiaobenListActivity.class;
//
//		initData();
//
//	}
//	public void insertBiaobenData(String tiaoma, String update_state, String is_shoudong) {
//
//		String str2 = "UPDATE zhuyuan_fuzhujiancha SET state = 'update',jiancha_zhuangtai = '已核对',hedui_hushi_mingcheng = '" + current_application.current_user_name
//				+ "',hedui_hushi_id = '" + current_application.current_user_number
//				+ "',hedui_time = datetime('now', 'localtime'),is_shoudong='"+is_shoudong+"'  WHERE "+getTiaomaQueryMode(tiaoma)+";";
//		BiaobenListActivity.this.db.execSQL(str2);
//		addHuliTongji("3", "标本采集");
//
//		if (dialog != null) {
//			dialog.dismiss();
//		}
//		dialogState = false;
//		mScanLastTiaoma = "";
//		tab_state = "yijiaodui";
//		tabhost.setCurrentTabByTag(tab_state);
//		getYiheduiData();
//	}
//
//	@Override
//	protected void onDestroy() {
//		this.scanner.scanExit();
//		super.onDestroy();
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		this.currentScanBar = false;
//
//		/*activity不可见的时候取消线程*/
//		mHandlerTimerExit.removeCallbacks(mTask01);
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		this.currentScanBar = true;
//		updateUserActionTime();
//		/*activity显示的时候启动线程*/
//		mHandlerTimerExit.postAtTime(mTask01, intervalKeypadeSaver);
//
//	}
//
//	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
//	{
//		switch (paramInt) {
//			case KeyEvent.KEYCODE_BACK:
//				super.goMain(this.v);
//				break;
//		}
//		return true;
//	}
//
//	@Override
//	public boolean onKeyUp(int keyCode, KeyEvent event) {
//		return super.onKeyUp(keyCode, event);
//	}
//
//	//TODO
//	public void sucessScanBarbode(String paramString)
//	{
//		//标本检查扫描结果
//		if(this.tiaoZhuangActivity == BiaobenListActivity.class && this.currentScanBar){
//
//			updateUserActionTime();	// 更新用户最后动作时间
//			dismissErrorDialog(this);
//			String str1 = convertBarbode(paramString);
//			if("".equals(StringHelper.notEmpty(paramString)) || paramString.length() < 2){
//				tipDialogWithError(this, "提示", "扫描结果为空");
//				return;
//			}
//
//			String str2 = str1.substring(0, 2);
//			if (str2.equals(this.current_application.patient_bar_code_title))
//			{
//				//切换患者
//				if (this.current_application.biaoben_hedui_huanzhe_qiehuan)
//				{
//					String str3 = str1.substring(2);
//					Log.i("zpd", "patient_zhuyuan_id:" + str3);
//					HuanzheWrapper localHuanzheWrapper = this.mZhuyuanHuanzheDao.getHuanzheInfoById(str3);
//					if (localHuanzheWrapper == null)
//					{
//						if (!current_application.scan_patient_switch_department){
//							tipDialogWithError(this.context, "提示", "该患者不存在");
//						}
//					}else{
//						playMedia("beep");
//						this.current_application.huanzhe_yijing_saomiaochuangtouka = true;
//						huandialogState=true;
//						localHuanzheWrapper.setGlobalData(this.current_application);
//						setDingbuHuanZheXinXi();
//						//从新加载数据
//						//getWeiheduiData();
//						//getYiheduiData();
//						//已校对
//						if("yijiaodui".equals(tab_state)){
//							tabhost.setCurrentTabByTag(tab_state);
//							getYiheduiData();
//						}else{
//							//未核对
//							tab_state = "weihedui";
//							tabhost.setCurrentTabByTag(tab_state);
//							getWeiheduiData();
//						}
//
//						//播报患者信息
//						playSoundHuanzheInfo();
//
//					}
//					return;
//				}else{
//					tipDialogWithError(this.context, "提示", "该界面不支持患者切换功能");
//					return;
//				}
//			}
//			else
//			{
//				if (this.current_application.biaoben_need_saomiao_chuantouka && (!this.current_application.huanzhe_yijing_saomiaochuangtouka) &&
//						!this.current_application.biaoben_hedui_zidong_qiehuan_huanzhe) {
//					tipDialogWithError(this.context, getString(R.string.tishi), getString(R.string.yuxiansaomiao_touka));
//					return;
//				}
//				//标本检查
//				if (str2.equals(this.current_application.biaoben_bar_code_title)||str2.equals(this.current_application.yizhu_bar_code_title))
//				{
//					if(current_application.biaoben_must_scan_wandai&&!this.current_application.biaoben_hedui_zidong_qiehuan_huanzhe){
//						if (huandialogState){
//							biaobenDialog(str1.substring(2));
//							return;
//						}else{
//							dialogState = false;
//							tipDialogWithError(this.context, "提示", "未扫描患者腕带，不能执行标本!");
//							return;
//						}
//					}else{
//						biaobenDialog(str1.substring(2));
//						return;
//					}
//				}
//				else
//				{
//					// 扫描登录卡，切换登录用户
//					if(str1.indexOf(this.current_application.user_bar_code_title) == 0)
//					{
//						tipDialogWithError(this.context, "提示", "该界面不支持切换登录用户");
//						return;
//					}
//				}
//				tipDialogWithError(this.context, "提示", "条码无效请核对");
//				return;
//			}
//		}
//	}
//
//	/**
//	 * 条码的查询方式，使用 "=" 或者 "like"
//	 * @param paramString
//	 * @return
//	 */
//	private String getTiaomaQueryMode(String paramString) {
//
//		if ("北京市和平里医院".equals(Constants.APP_REGION)) {
//
//			return " tiaoma like '%" + paramString + "%' ";
//		}
//
//		return " tiaoma = '" + paramString + "' ";
//	}
//
//	public ArrayList<Map<String, Object>> queryBiaobenData(String state) {
//		ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
//		String str1= "SELECT * FROM zhuyuan_fuzhujiancha WHERE zhuyuan_id = '" + this.current_application.current_patient_zhuyuan_id
//				+ "' and jiancha_zhuangtai = '" + state + "' group by tiaoma ORDER BY ";
//		if ("龙山县人民医院".equals(Constants.APP_REGION)){
//			str1 = str1 + " (shenqing_time) DESC, (id) asc  ";
//		}else{
//			str1 = str1 + " id ASC ";
//		}
//		Cursor localCursor1 = this.db.rawQuery(str1, new String[0]);
//		while(localCursor1.moveToNext()){
//			String str3 = "SELECT * FROM zhuyuan_fuzhujiancha WHERE zhuyuan_id = '" + this.current_application.current_patient_zhuyuan_id
//					+ "' and jiancha_zhuangtai = '" + state + "' and "+getTiaomaQueryMode(localCursor1.getString(localCursor1.getColumnIndex("tiaoma")))+" ORDER BY id ASC ";
//			Cursor localCursor2 = this.db.rawQuery(str3, new String[0]);
//			String str2 = "";
//			while (localCursor2.moveToNext()) {
//				str2 = str2 + localCursor2.getString(localCursor2.getColumnIndex("jiancha_mingcheng"));
//				if (!localCursor2.isLast())
//					str2 = str2 + "\r\n";
//			}
//			localCursor2.close();
//			HashMap<String, Object> localHashMap = new HashMap<String, Object>();
//			localHashMap.put("id", localCursor1.getString(localCursor1.getColumnIndex("id")));
//			localHashMap.put("mingcheng", str2);
//			localHashMap.put("zhuyuan_id", localCursor1.getString(localCursor1.getColumnIndex("zhuyuan_id")));
//			localHashMap.put("songjianwu", localCursor1.getString(localCursor1.getColumnIndex("songjianwu")));
//			localHashMap.put("keshi", localCursor1.getString(localCursor1.getColumnIndex("jiancha_keshi")));
//			localHashMap.put("tiaoma",localCursor1.getString(localCursor1.getColumnIndex("tiaoma")));
//			localHashMap.put("state", localCursor1.getString(localCursor1.getColumnIndex("jiancha_zhuangtai")));
//			localHashMap.put("hedui_hushi_mingcheng", localCursor1.getString(localCursor1.getColumnIndex("hedui_hushi_mingcheng")));
//			localHashMap.put("hedui_hushi_id", localCursor1.getString(localCursor1.getColumnIndex("hedui_hushi_id")));
//			localHashMap.put("hedui_time", localCursor1.getString(localCursor1.getColumnIndex("hedui_time")));
//			localHashMap.put("shenqing_time", localCursor1.getString(localCursor1.getColumnIndex("shenqing_time")));
//			localHashMap.put("receive_hushi_name", localCursor1.getString(localCursor1.getColumnIndex("receive_hushi_name"))  == null ? "" : localCursor1.getString(localCursor1.getColumnIndex("receive_hushi_name")));
//			localHashMap.put("receive_hushi_id", localCursor1.getString(localCursor1.getColumnIndex("receive_hushi_id")) == null ? "" : localCursor1.getString(localCursor1.getColumnIndex("receive_hushi_id")));
//			localHashMap.put("receive_hushi_time", localCursor1.getString(localCursor1.getColumnIndex("receive_hushi_time")) == null ? "" : localCursor1.getString(localCursor1.getColumnIndex("receive_hushi_time")));
//			dataList.add(localHashMap);
//		}
//		localCursor1.close();
//		return dataList;
//	}
//}
