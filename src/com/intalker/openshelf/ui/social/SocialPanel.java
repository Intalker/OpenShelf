package com.intalker.openshelf.ui.social;

import com.intalker.openshelf.R;
import com.intalker.openshelf.HomeActivity;
import com.intalker.openshelf.cloud.CloudAPI;
import com.intalker.openshelf.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.openshelf.data.AppData;
import com.intalker.openshelf.ui.control.HaloButton;
import com.intalker.openshelf.util.LayoutUtil;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SocialPanel extends RelativeLayout {

	private RelativeLayout mMainLayout = null;
	private RelativeLayout mTopBanner = null;
	private RelativeLayout mBottomBanner = null;
	private HaloButton mFriendBtn = null;
	private HaloButton mMessageBtn = null;
	private HaloButton mMakeFriendsBtn = null;
	private HaloButton mReturnBtn = null;
	private FriendListView mFriendsView = null;
	private UsersView mUsersView = null;

	public SocialPanel(Context context) {
		super(context);
		mMainLayout = new RelativeLayout(context);
		RelativeLayout.LayoutParams mainLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		mainLP.width = LayoutUtil.getSocialPanelWidth();
		mMainLayout.setLayoutParams(mainLP);
		mMainLayout.setBackgroundResource(R.drawable.stone_bg);
		this.addView(mMainLayout);
		
		mTopBanner = new RelativeLayout(this.getContext());
		mTopBanner.setBackgroundResource(R.drawable.stone_bg);
		RelativeLayout.LayoutParams topBannerLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		topBannerLP.height = LayoutUtil.getGalleryTopPanelHeight();
		mMainLayout.addView(mTopBanner, topBannerLP);
		
//		this.addView(ControlFactory.createHoriSeparatorForRelativeLayout(context,
//				LayoutUtil.getSocialPanelWidth(),
//				DensityAdaptor.getDensityIndependentValue(32)));
		createFriendsView();
		createUsersView();
		mUsersView.setVisibility(INVISIBLE);
		
		mBottomBanner = new RelativeLayout(this.getContext());
		mBottomBanner.setBackgroundResource(R.drawable.stone_bg);
		RelativeLayout.LayoutParams bottomBannerLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		bottomBannerLP.height = LayoutUtil.getGalleryBottomPanelHeight();
		bottomBannerLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mMainLayout.addView(mBottomBanner, bottomBannerLP);
		
		createButtons();
	}

	private void createButtons() {
		int smallMargin = LayoutUtil.getSmallMargin();
		int largeMargin = LayoutUtil.getLargeMargin();
		mFriendBtn = new HaloButton(this.getContext(), R.drawable.friend);
		RelativeLayout.LayoutParams friendBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		friendBtnLP.leftMargin = largeMargin;
		friendBtnLP.topMargin = smallMargin;
		mFriendBtn.setLayoutParams(friendBtnLP);
		mTopBanner.addView(mFriendBtn);

		mMessageBtn = new HaloButton(this.getContext(), R.drawable.message);
		RelativeLayout.LayoutParams msgBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		msgBtnLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		msgBtnLP.rightMargin = largeMargin;
		msgBtnLP.topMargin = smallMargin;
		mMessageBtn.setLayoutParams(msgBtnLP);
		mTopBanner.addView(mMessageBtn);
		
		mMessageBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "Coming soon...",
						Toast.LENGTH_SHORT).show();
			}
		});
		
		mMakeFriendsBtn = new HaloButton(this.getContext(), R.drawable.group);
		RelativeLayout.LayoutParams bottomCenterLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		bottomCenterLP.addRule(RelativeLayout.CENTER_IN_PARENT);
		mMakeFriendsBtn.setLayoutParams(bottomCenterLP);
		mBottomBanner.addView(mMakeFriendsBtn);
		
		mMakeFriendsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CloudAPI.getAllUsers(v.getContext(),
						new ICloudAPITaskListener() {

							@Override
							public void onFinish(int returnCode) {
								if (CloudAPI.isSuccessful(
										HomeActivity.getApp(), returnCode)) {
									turnOnUsersView();
									mUsersView.fillWithStrangersData(AppData.getInstance().getAllUsers());
								}
							}

						});
			}
		});
		
		mReturnBtn = new HaloButton(this.getContext(), R.drawable.back);
		mReturnBtn.setLayoutParams(bottomCenterLP);
		mBottomBanner.addView(mReturnBtn);
		
		mReturnBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CloudAPI.getFollowings(v.getContext(), new ICloudAPITaskListener(){

					@Override
					public void onFinish(int returnCode) {
						if (CloudAPI.isSuccessful(HomeActivity.getApp(), returnCode)) {
							turnOffUsersView();
							mFriendsView.refreshList();
						}
					}
					
				});
			}
		});

		mReturnBtn.setVisibility(GONE);
	}

	private void createFriendsView() {
		mFriendsView = new FriendListView(this.getContext());

		RelativeLayout.LayoutParams friendsViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		friendsViewLP.topMargin = LayoutUtil.getGalleryTopPanelHeight();
		friendsViewLP.bottomMargin = LayoutUtil.getGalleryBottomPanelHeight();

		mFriendsView.setLayoutParams(friendsViewLP);

		mMainLayout.addView(mFriendsView);
	}
	
	private void createUsersView() {
		mUsersView = new UsersView(this.getContext());

		RelativeLayout.LayoutParams usersViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		usersViewLP.topMargin = LayoutUtil.getGalleryTopPanelHeight();
		usersViewLP.bottomMargin = LayoutUtil.getGalleryBottomPanelHeight();

		mUsersView.setLayoutParams(usersViewLP);

		mMainLayout.addView(mUsersView);
	}

	public FriendListView getFriendsView() {
		return mFriendsView;
	}
	
	public UsersView getUsersView() {
		return mUsersView;
	}

	public void turnOnUsersView() {
		mUsersView.setVisibility(VISIBLE);
		mFriendsView.setVisibility(GONE);
		mMakeFriendsBtn.setVisibility(GONE);
		mReturnBtn.setVisibility(VISIBLE);
	}

	public void turnOffUsersView() {
		mUsersView.setVisibility(GONE);
		mFriendsView.setVisibility(VISIBLE);
		mMakeFriendsBtn.setVisibility(VISIBLE);
		mReturnBtn.setVisibility(GONE);
	}
}
