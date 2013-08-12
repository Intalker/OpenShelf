package com.intalker.openshelf.ui.social;

import com.intalker.openshelf.R;
import com.intalker.openshelf.ui.UIConfig;
import com.intalker.openshelf.ui.control.HaloButton;
import com.intalker.openshelf.util.LayoutUtil;

import android.content.Context;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BigUserView extends RelativeLayout {
	private HaloButton mUserAvatar = null;
	private TextView mUserNameTextView = null;
	private static int specWidth = LayoutUtil.getBigUserViewItemWidth();
	private static int specHeight = LayoutUtil.getBigUserViewItemHeight();
	public BigUserView(Context context) {
		super(context);
		createUI(context);
	}
	
	private void createUI(Context context) {
        RelativeLayout.LayoutParams mainLP = new RelativeLayout.LayoutParams(specWidth, specHeight);
		this.setLayoutParams(mainLP);
		
		int smallMargin = LayoutUtil.getSmallMargin();
		
        mUserAvatar = new HaloButton(context, R.drawable.avatar_2);
        RelativeLayout.LayoutParams avatarLP = new RelativeLayout.LayoutParams(
                specWidth / 2,
                specHeight / 2);
        avatarLP.topMargin = smallMargin;
        avatarLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mUserAvatar.setLayoutParams(avatarLP);
        this.addView(mUserAvatar);
		
		mUserNameTextView = new TextView(context);
		mUserNameTextView.setTextSize(10.f);
		mUserNameTextView.setText("<User name>");
		mUserNameTextView.setEllipsize(TextUtils.TruncateAt.END);
		mUserNameTextView.setTextColor(UIConfig.getTipTextColor());
		RelativeLayout.LayoutParams nameLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		nameLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		nameLP.topMargin = specHeight / 2 + smallMargin;
		mUserNameTextView.setLayoutParams(nameLP);
		
		this.addView(mUserNameTextView);
	}
	
	public void setUserName(String userName) {
		mUserNameTextView.setText(userName);
	}

}
