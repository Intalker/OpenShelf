package com.intalker.openshelf.ui.social;

import com.intalker.borrow.R;
import com.intalker.openshelf.HomeActivity;
import com.intalker.openshelf.cloud.CloudAPI;
import com.intalker.openshelf.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;

import android.content.Context;

public class StrangerUserItemUI extends UserItemUI {

	private boolean mAdded = false;
	
	public StrangerUserItemUI(Context context) {
		super(context, R.drawable.add);
	}

	@Override
	protected void onActionButtonClick() {
		if (mAdded) {
			return;
		}
		CloudAPI.follow(this.getContext(), mInfo.getId(),
				new ICloudAPITaskListener() {

					@Override
					public void onFinish(int returnCode) {
						if (CloudAPI.isSuccessful(
								HomeActivity.getApp(), returnCode)) {
							mActionBtn.setVisibility(GONE);
						}
					}

				});
	}

}
