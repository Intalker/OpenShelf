package com.intalker.openshelf.ui.social;

import java.util.ArrayList;

import com.intalker.openshelf.data.AppData;
import com.intalker.openshelf.data.FriendInfo;
import com.intalker.openshelf.ui.control.ControlFactory;
import com.intalker.openshelf.util.DensityAdaptor;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class FriendListView extends ScrollView {

	private LinearLayout mListView = null;

	public FriendListView(Context context) {
		super(context);

		mListView = new LinearLayout(context);
		mListView.setOrientation(LinearLayout.VERTICAL);

		this.addView(mListView);
	}
	
	public void clear() {
		mListView.removeAllViews();
	}

	public void refreshList() {
		mListView.removeAllViews();

		LinearLayout.LayoutParams itemLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		itemLP.height = DensityAdaptor.getDensityIndependentValue(40);

		ArrayList<FriendInfo> friends = AppData.getInstance().getFriends();
		boolean isFirst = true;
		for (FriendInfo friendInfo : friends) {
			if (!isFirst) {
				mListView.addView(ControlFactory.createHoriSeparatorForLinearLayout(
								this.getContext()));
			}
			isFirst = false;
			FriendItemUI friendItemUI = new FriendItemUI(this.getContext());
			friendItemUI.setInfo(friendInfo);
			mListView.addView(friendItemUI, itemLP);
		}
	}
}
