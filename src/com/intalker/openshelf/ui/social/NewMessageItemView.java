package com.intalker.openshelf.ui.social;

import java.util.Date;

import com.intalker.openshelf.R;
import com.intalker.openshelf.ui.control.HaloButton;
import com.intalker.openshelf.util.DensityAdaptor;
import com.intalker.openshelf.util.LayoutUtil;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewMessageItemView extends RelativeLayout {
	private BigUserView mAvatarView = null;
	private TextView mMessageTextView = null;
	private TextView mTimestampTextView = null;
	private HaloButton mReplyButton = null;
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
		
		mTimestampTextView = new TextView(context);
		mTimestampTextView.setTextColor(Color.LTGRAY);
		mTimestampTextView.setTextSize(10.0f);
		Date curDate = new Date(System.currentTimeMillis());
		mTimestampTextView.setText(curDate.toLocaleString());
        RelativeLayout.LayoutParams timestampLP = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        timestampLP.rightMargin = DensityAdaptor.getDensityIndependentValue(40);
        timestampLP.topMargin = DensityAdaptor.getDensityIndependentValue(40);
        timestampLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mTimestampTextView.setLayoutParams(timestampLP);
        this.addView(mTimestampTextView);
		
		mReplyButton = new HaloButton(context, R.drawable.reply);
		RelativeLayout.LayoutParams replyLP = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
		replyLP.rightMargin = LayoutUtil.getMediumMargin();
        replyLP.addRule(RelativeLayout.CENTER_VERTICAL);
        replyLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mReplyButton.setLayoutParams(replyLP);
        this.addView(mReplyButton);
	}
}
