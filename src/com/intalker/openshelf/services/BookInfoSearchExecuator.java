package com.intalker.openshelf.services;

import com.intalker.openshelf.isbn.ISBNResolver;

import android.content.Context;
import android.graphics.Bitmap;

public class BookInfoSearchExecuator  implements IInProcessServiceInterface.IExecuator{
	public BookInfoSearchExecuator(Context c,String isbn)
	{
		mContext = c;
		mISBN = isbn;
	}

	@Override
	public void process() {
		// search one book info on internet here!
		ISBNResolver.getInstance().getBookInfoByISBN(mContext, mISBN);
	}

	private String mISBN = null;
	private Context mContext = null;
}
