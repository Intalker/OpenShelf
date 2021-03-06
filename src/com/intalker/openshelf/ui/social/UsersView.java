package com.intalker.openshelf.ui.social;

import java.util.ArrayList;

import com.intalker.openshelf.data.AppData;
import com.intalker.openshelf.data.UserInfo;
import com.intalker.openshelf.ui.control.ControlFactory;
import com.intalker.openshelf.util.DensityAdaptor;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class UsersView extends ScrollView {

	private LinearLayout mUserList = null;

	public UsersView(Context context) {
		super(context);
		mUserList = new LinearLayout(context);
		mUserList.setOrientation(LinearLayout.VERTICAL);
		
		this.addView(mUserList);
	}
	
	public void clear() {
		mUserList.removeAllViews();
	}

	public void fillWithStrangersData(ArrayList<UserInfo> users) {
		mUserList.removeAllViews();

		LinearLayout.LayoutParams itemLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		itemLP.height = DensityAdaptor.getDensityIndependentValue(40);

		//ArrayList<UserInfo> users = AppData.getInstance().getAllUsers();
		boolean isFirst = true;
		for (UserInfo userInfo : users) {
			if (!isFirst) {
				mUserList.addView(ControlFactory.createHoriSeparatorForLinearLayout(
								this.getContext()));
			}
			isFirst = false;
			UserItemUI userItemUI = new StrangerUserItemUI(this.getContext());
			userItemUI.setInfo(userInfo);
			mUserList.addView(userItemUI, itemLP);
		}
	}
	
	public void fillWithBookOwnersData(ArrayList<UserInfo> owners) {
		mUserList.removeAllViews();

		LinearLayout.LayoutParams itemLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		itemLP.height = DensityAdaptor.getDensityIndependentValue(40);

		//ArrayList<UserInfo> users = AppData.getInstance().getAllUsers();
		boolean isFirst = true;
		for (UserInfo ownerInfo : owners) {
			if (!isFirst) {
				mUserList.addView(ControlFactory.createHoriSeparatorForLinearLayout(
								this.getContext()));
			}
			isFirst = false;
			BookOwnerItemUI bookOwnerItemUI = new BookOwnerItemUI(this.getContext());
			bookOwnerItemUI.setInfo(ownerInfo);
			mUserList.addView(bookOwnerItemUI, itemLP);
		}
	}
}
