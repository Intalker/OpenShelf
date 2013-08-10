package com.intalker.openshelf.isbn.parser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import android.widget.Toast;

import com.intalker.openshelf.util.StringUtil;
import com.intalker.openshelf.util.WebUtil;


public class NativeServerParser extends BookInfoParser {
	private final static String ISBN_SEARCHURL = "http://www.gokaven.com/openshelf/cloud_api/index.php?op=GetBookInfoByISBN&isbn=";

	public NativeServerParser() {
	}

	@Override
	public void parse() {
		String url = ISBN_SEARCHURL + mISBN;
		try {

			HttpURLConnection conn = (HttpURLConnection) new URL(url)
					.openConnection();
			//conn.setReadTimeout(20000);
			conn.setConnectTimeout(8000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {

				InputStream inputStream = conn.getInputStream();

				BufferedReader br = new BufferedReader(new InputStreamReader(
						inputStream, "UTF-8"));

				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

				br.close();
				String strResult = sb.toString();

				JSONObject jsonObject = new JSONObject(strResult);

				if (null == mCoverImage) {
					String imageURL = jsonObject.getString("cover_image");
					if (!StringUtil.isEmpty(imageURL)) {
						mCoverImage = WebUtil.getImageFromURL(imageURL);
					}
				}
				mBookName = jsonObject.getString("title");
				mAuthor = jsonObject.getString("author");
				mPublisher = jsonObject.getString("publisher");
				mPageCount = jsonObject.getString("page_count");
				mDescription = jsonObject.getString("description");
			}
			if (null != conn) {
				conn.disconnect();
			}
		} catch (Exception e) {
			//[TODO] handle some server connection exception here.
			// we should show something to user.
			return;
		}
	}
	
	public static String formatStringArray(JSONArray jsonArray, String separator) {
		if (null == separator) {
			separator = "";
		}
		StringBuilder sb = new StringBuilder();
		try {
			int length = jsonArray.length();
			for (int i = 0; i < length; ++i) {
				String str = (String) jsonArray.get(i);
				if (str.length() > 0) {
					if (i > 0) {
						sb.append(separator);
					}
					sb.append(str);
				}
			}
		} catch (Exception ex) {
		}
		return sb.toString();
	}
}
