package com.jokor.base.model;

import com.jokor.base.base.BaseApplication;
import com.jokor.base.model.stores_bean.MGRelation;
import com.jokor.base.model.stores_bean.MGroup;
import com.jokor.base.model.stores_bean.MPreRelations;
import com.jokor.base.model.stores_bean.MRelation;
import com.jokor.base.model.stores_bean.MSession;
import com.jokor.base.model.stores_bean.Mmsg;

import io.objectbox.Box;
import io.objectbox.BoxStore;

//数据库统一操作管理类初始化
public class DataManager {
	
	private final String TAG = "DataManager";
	
	private static DataManager dataManager;
	
	public static synchronized DataManager getInstance() {
		if (dataManager == null) {
			dataManager = new DataManager();
		}
		return dataManager;
	}
	
	private BoxStore boxStore;
	
//	好友分组信息
	private Box<MGroup> groupBox;
//	好友关系
	private Box<MRelation> relationBox;
//	会话
	private Box<MSession> sessionBox;
//	消息
	private Box<Mmsg> mmsgBox;
//	好友申请
	private Box<MPreRelations> preRelationsBox;
	//群聊
	private Box<MGRelation> mgRelationBox;
	public void init(BaseApplication baseApplication) {
		boxStore = baseApplication.getBoxStore();
		initDatas();
	}
	
	//数据库初始化
	private void initDatas() {
		groupBox = boxStore.boxFor(MGroup.class);
		relationBox = boxStore.boxFor(MRelation.class);
		sessionBox = boxStore.boxFor(MSession.class);
		mmsgBox = boxStore.boxFor(Mmsg.class);
		preRelationsBox = boxStore.boxFor(MPreRelations.class);
		mgRelationBox=boxStore.boxFor(MGRelation.class);
	}
	
}
