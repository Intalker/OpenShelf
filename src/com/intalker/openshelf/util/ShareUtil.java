package com.intalker.openshelf.util;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.intalker.openshelf.R;
import com.intalker.openshelf.HomeActivity;
import com.intalker.openshelf.data.BookInfo;

public class ShareUtil {
	public static void fireShareIntent(BookInfo bookInfo) {
		StorageUtil.saveCoverImage(bookInfo);
		
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		File imageFile = new File(StorageUtil.getCoverImagePath(bookInfo
				.getISBN()));
		if (imageFile.exists()) {
			Uri uri = Uri.fromFile(imageFile);
			intent.putExtra(Intent.EXTRA_STREAM, uri);
		}
		Context context = HomeActivity.getApp();
		intent.putExtra("subject", context.getString(R.string.share_subject));
		String bodyMsg = context.getString(R.string.share_msg_prefix)
				+ bookInfo.getBookName()
				+ context.getString(R.string.share_msg_suffix);
		intent.putExtra(Intent.EXTRA_TEXT, bodyMsg);
		intent.putExtra("sms_body", bodyMsg);
		intent.setType("image/*");
		HomeActivity.getApp().startActivity(intent);
	}
}
