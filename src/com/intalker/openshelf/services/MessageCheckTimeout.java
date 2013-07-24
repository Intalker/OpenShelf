package com.intalker.openshelf.services;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.intalker.openshelf.cloud.CloudAPI;
import com.intalker.openshelf.cloud.CloudUtility;
import com.intalker.openshelf.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.openshelf.data.AppData;
import com.intalker.openshelf.data.MessageInfo;
import com.intalker.openshelf.data.UserInfo;
import com.intalker.openshelf.notification.NotificationManager;
import com.intalker.openshelf.ui.book.BookShelfItem;
import com.intalker.openshelf.ui.book.BookShelfView;

public class MessageCheckTimeout implements IInProcessServiceInterface.ITimerTimeout{
	
	public MessageCheckTimeout(Context c)
	{
		mContext  =c;
	}
	

	@Override
	public void onTimeOut() {
		if (!CloudUtility.isLoggedIn() || CloudAPI.IsRunning) {
			return;
		}
		CloudAPI.getAllMessages(mContext, false, new ICloudAPITaskListener() {

			@Override
			public void onFinish(int returnCode) {
				if (CloudAPI.isSuccessfulWithoutToast(returnCode)) {
					ArrayList<MessageInfo> messages = AppData.getInstance()
							.getIncomeMessages();
					HashMap<String, BookShelfItem> map = BookShelfView
							.getInstance().getISBNUIMap();
					for (MessageInfo msg : messages) {
						BookShelfItem item = map.get(msg.getISBN());
						if (null != item
								&& ((UserInfo.getCurUserId().compareTo(
										msg.getFriendId()) == 0) || (UserInfo
										.getCurUserId().compareTo(
												msg.getHostId()) == 0))) {
							item.attachMessage(msg);
						}
					}
					NotificationManager.getInstance().fire();
				}
			}

		});
		
	}
	
	private Context mContext = null;

}
