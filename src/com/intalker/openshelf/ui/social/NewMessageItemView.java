package com.intalker.openshelf.ui.social;

import com.intalker.openshelf.util.DensityAdaptor;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewMessageItemView extends RelativeLayout {
	private BigUserView mAvatarView = null;
	private TextView mMessageTextView = null;
	public NewMessageItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		createUI(context);
	}

	private void createUI(Context context)
	{
		LinearLayout.LayoutParams mainLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
//				DensityAdaptor.getDensityIndependentValue(60));
		this.setLayoutParams(mainLP);
		
		mAvatarView = new BigUserView(context);
		mAvatarView.setUserName("<User Name>");
		RelativeLayout.LayoutParams avatarLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		avatarLP.leftMargin = DensityAdaptor.getDensityIndependentValue(10);
		avatarLP.topMargin = avatarLP.bottomMargin = DensityAdaptor.getDensityIndependentValue(4);
		avatarLP.addRule(RelativeLayout.CENTER_VERTICAL);
		mAvatarView.setLayoutParams(avatarLP);
		this.addView(mAvatarView);
		
		mMessageTextView = new TextView(context);
		mMessageTextView.setText("<Message from friend>");
		RelativeLayout.LayoutParams msgLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		msgLP.leftMargin = DensityAdaptor.getDensityIndependentValue(80);
		msgLP.addRule(RelativeLayout.CENTER_VERTICAL);
		mMessageTextView.setLayoutParams(msgLP);
		this.addView(mMessageTextView);
	}
}
