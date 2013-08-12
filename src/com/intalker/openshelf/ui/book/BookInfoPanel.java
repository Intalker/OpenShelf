package com.intalker.openshelf.ui.book;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.intalker.openshelf.HomeActivity;
import com.intalker.openshelf.R;
import com.intalker.openshelf.cloud.CloudAPI;
import com.intalker.openshelf.cloud.CloudAPIAsyncTask.ICloudAPITaskListener;
import com.intalker.openshelf.data.AppData;
import com.intalker.openshelf.data.BookInfo;
import com.intalker.openshelf.isbn.ISBNResolver;
import com.intalker.openshelf.ui.FullSizeImageDialog;
import com.intalker.openshelf.ui.UIConfig;
import com.intalker.openshelf.ui.control.ControlFactory;
import com.intalker.openshelf.ui.control.FlipPanel;
import com.intalker.openshelf.ui.control.HaloButton;
import com.intalker.openshelf.ui.social.BookOwnersDialog;
import com.intalker.openshelf.ui.social.NewMessageItemView;
import com.intalker.openshelf.ui.social.SendMessageDialog;
import com.intalker.openshelf.ui.social.SendMessageDialog.ISendHandler;
import com.intalker.openshelf.util.DensityAdaptor;
import com.intalker.openshelf.util.LayoutUtil;
import com.intalker.openshelf.util.ShareUtil;

public class BookInfoPanel {
	private Context mContext = null;
	private BookShelfItem mBookItem = null;
    private int specWidth = LayoutUtil.getDetailDialogWidth();
    private int specHeight = LayoutUtil.getDetailDialogHeight();
    private FlipPanel mFlipPanel = null;
    private RelativeLayout mBasicInfoPanel = null;
    private RelativeLayout mSocialInfoPanel = null;
    private FullSizeImageDialog mFullSizeImageDialog = null;
    private boolean mHasCoverImage = false;
    
    //Elements for basic info panel
    private Bitmap mCoverImage = null;
    private ImageView mCoverImageView = null;
    private RelativeLayout mDetailInfoPanel = null;
    private TextView mTitleTextView = null;
    private TextView mAuthorTextView = null;
    private TextView mPublisherTextView = null;
    private TextView mPublishTimeTextView = null;
    private TextView mPageCountTextView = null;
    private TextView mISBNTextView = null;
    
    private TextView mDescriptionTextView = null;
    
	private HaloButton mDeleteButton = null;
	private HaloButton mShareButton = null;
//	private HaloButton mCloseButton = null;
	
	private HaloButton mSocialButton = null;
	private HaloButton mSearchButton = null;
	private HaloButton mBorrowButton = null;
	
    public BookInfoPanel(Context ctx)
    {
    	mContext = ctx;
    	mFlipPanel = new FlipPanel(mContext);
    	mFlipPanel.setBackgroundImage(R.drawable.white_panel_frame);
//    	mFlipPanel.setBackgroundImage(R.drawable.login_bg);
    	
    	mFullSizeImageDialog = new FullSizeImageDialog(mContext);
    	
    	createBasicInfoPanel();
    	createSocialInfoPanel();
    	
    	addListenersForBasicInfoPanel();
    	addListenersForSocialPanel();
    }
//    
//    public void setBookInfo(BookShelfItem bookItem)
//    {
//    	mBookItem = bookItem;
//    	Boo
//        mCoverImageView.setImageBitmap(mBookItem.getCoverImage());
//    }
//    
    public void show()
    {
    	if (null == mBookItem)
    	{
    		return;
    	}
        mFlipPanel.show();
    }
    
    public void dismiss()
    {
        mFlipPanel.dismiss();
    }
    
    private void createBasicInfoPanel()
    {
    	mBasicInfoPanel = new RelativeLayout(mContext);
    	mBasicInfoPanel.setBackgroundResource(R.drawable.new_wood_bg);
        FrameLayout.LayoutParams lpBasicInfoPanel = new FrameLayout.LayoutParams(specWidth, specHeight);
        lpBasicInfoPanel.gravity = Gravity.CENTER;
        mBasicInfoPanel.setLayoutParams(lpBasicInfoPanel);
        
        mCoverImageView = new ImageView(mContext);
		RelativeLayout.LayoutParams coverImageLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		coverImageLP.width = LayoutUtil.getDetailDialogWidth() / 2;
		coverImageLP.height = LayoutUtil.getDetailDialogHeight() / 2;
		int boundMargin = LayoutUtil.getDetailDialogBoundMargin();
		coverImageLP.leftMargin = boundMargin;
		coverImageLP.topMargin = boundMargin;
//		mCoverImageView.setImageResource(R.drawable.bookcover_unknown);
		mCoverImageView.setScaleType(ScaleType.FIT_CENTER);
        mBasicInfoPanel.addView(mCoverImageView, coverImageLP);
        
		mDetailInfoPanel = new RelativeLayout(mContext);
		RelativeLayout.LayoutParams detailInfoPanelLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		detailInfoPanelLP.width = LayoutUtil.getDetailDialogWidth() / 2 - boundMargin * 3;
		detailInfoPanelLP.height = LayoutUtil.getDetailDialogHeight() / 2;
		detailInfoPanelLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		detailInfoPanelLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		detailInfoPanelLP.topMargin = boundMargin;
		detailInfoPanelLP.rightMargin = boundMargin;
		mBasicInfoPanel.addView(mDetailInfoPanel, detailInfoPanelLP);
        
		mTitleTextView = addInfoTextViewToBasicPanel(R.string.book_name, 0);
		mAuthorTextView = addInfoTextViewToBasicPanel(R.string.author, 1);
		mPublisherTextView = addInfoTextViewToBasicPanel(R.string.publisher, 2);
		mPageCountTextView = addInfoTextViewToBasicPanel(R.string.page_count, 3);
		mISBNTextView = addInfoTextViewToBasicPanel(R.string.isbn, 4);
		
		int separatorY = boundMargin + LayoutUtil.getDetailDialogHeight() / 2;
		mBasicInfoPanel.addView(ControlFactory.createHoriSeparatorForRelativeLayout(mContext,
				LayoutUtil.getDetailDialogWidth(),
				separatorY));
		
		//Description textview
		ScrollView scrollView = new ScrollView(mContext);
		
		LinearLayout scrollContent = new LinearLayout(mContext);
		scrollContent.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(scrollContent);
		
		mDescriptionTextView = new TextView(mContext);
		mDescriptionTextView.setText("???");
		mDescriptionTextView.setTextColor(Color.BLACK);
		scrollContent.addView(mDescriptionTextView);
		
		RelativeLayout.LayoutParams descriptionLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		int margin = LayoutUtil.getDetailDialogBoundMargin();
		descriptionLP.leftMargin = margin;
		descriptionLP.rightMargin = margin;
		descriptionLP.topMargin = margin * 2 + LayoutUtil.getDetailDialogHeight() / 2 - DensityAdaptor.getDensityIndependentValue(6);
		descriptionLP.width = LayoutUtil.getDetailDialogWidth() - margin * 2;
		descriptionLP.height = LayoutUtil.getDetailDialogHeight() * 3 / 10;;
		
		scrollView.setLayoutParams(descriptionLP);
		mBasicInfoPanel.addView(scrollView);
		//
		
		mBasicInfoPanel.addView(ControlFactory.createHoriSeparatorForRelativeLayout(mContext,
				LayoutUtil.getDetailDialogWidth(),
				separatorY + descriptionLP.height + boundMargin - DensityAdaptor.getDensityIndependentValue(8)));
        
//        Button goBtn = new Button(mContext);
//        goBtn.setText("Go");
//        goBtn.setOnClickListener(new View.OnClickListener() {
//            
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//            	mFlipPanel.flip(true);
//            }
//        });
//        
//        RelativeLayout.LayoutParams lpGoBtn = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
//        lpGoBtn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        lpGoBtn.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        goBtn.setLayoutParams(lpGoBtn);
//        mBasicInfoPanel.addView(goBtn);
        mFlipPanel.setViewA(mBasicInfoPanel);
        
        //buttons

		// Delete button.
		mDeleteButton = new HaloButton(mContext, R.drawable.trash);
		
		RelativeLayout.LayoutParams deleteBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		deleteBtnLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		deleteBtnLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		deleteBtnLP.leftMargin = boundMargin;
		deleteBtnLP.bottomMargin = boundMargin;
		
		mBasicInfoPanel.addView(mDeleteButton, deleteBtnLP);
		
		// Share button.
		mShareButton = new HaloButton(mContext, R.drawable.share);
		
		RelativeLayout.LayoutParams shareBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		shareBtnLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		shareBtnLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		shareBtnLP.rightMargin = boundMargin;
		shareBtnLP.bottomMargin = boundMargin;
		
		mBasicInfoPanel.addView(mShareButton, shareBtnLP);
		
		// Close button.
//		mCloseButton = new HaloButton(mContext, R.drawable.close);
//		
//		RelativeLayout.LayoutParams closeBtnLP = new RelativeLayout.LayoutParams(
//				RelativeLayout.LayoutParams.WRAP_CONTENT,
//				RelativeLayout.LayoutParams.WRAP_CONTENT);
//		closeBtnLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//		closeBtnLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//		closeBtnLP.rightMargin = boundMargin;
//		closeBtnLP.topMargin = boundMargin;
		
//		mBasicInfoPanel.addView(mCloseButton, closeBtnLP);
		
		// Search button.
		mBorrowButton = new HaloButton(mContext, R.drawable.borrow);

		RelativeLayout.LayoutParams borrowBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		borrowBtnLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		borrowBtnLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		borrowBtnLP.bottomMargin = boundMargin;

		mBasicInfoPanel.addView(mBorrowButton, borrowBtnLP);
		mBorrowButton.setVisibility(View.GONE);
		
		mSocialButton = new HaloButton(mContext, R.drawable.chat);

		RelativeLayout.LayoutParams socialtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		socialtnLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		socialtnLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		socialtnLP.bottomMargin = socialtnLP.rightMargin = boundMargin;

		mBasicInfoPanel.addView(mSocialButton, socialtnLP);
    }
    
    private void createSocialInfoPanel()
    {
    	mSocialInfoPanel = new RelativeLayout(mContext);
    	mSocialInfoPanel.setBackgroundResource(R.drawable.new_metal_bg);
        FrameLayout.LayoutParams lpB = new FrameLayout.LayoutParams(specWidth, specHeight);
        lpB.gravity = Gravity.CENTER;
        mSocialInfoPanel.setLayoutParams(lpB);
        
//        TextView t2 = new TextView(mContext);
//        t2.setText("[TODO]:\nShow social info here.");
//        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
//        lp2.addRule(RelativeLayout.CENTER_IN_PARENT);
//        t2.setLayoutParams(lp2);
//        mSocialInfoPanel.addView(t2);
        
        ScrollView scrollView = new ScrollView(mContext);
        LinearLayout scrollPanel = new LinearLayout(mContext);
        scrollPanel.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(scrollPanel);
        for(int i = 0; i < 10; ++i)
        {
        	scrollPanel.addView(new NewMessageItemView(mContext));
        }
        RelativeLayout.LayoutParams scrollLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
//		scrollLP.leftMargin = scrollLP.rightMargin = scrollLP.topMargin = DensityAdaptor
//				.getDensityIndependentValue(10);
		scrollLP.bottomMargin = DensityAdaptor.getDensityIndependentValue(50);
        mSocialInfoPanel.addView(scrollView, scrollLP);
        
        Button goBackBtn = new Button(mContext);
        goBackBtn.setText("Go Back");
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	mFlipPanel.flip(false);
            }
        });
        RelativeLayout.LayoutParams lpGoBackBtn = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpGoBackBtn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpGoBackBtn.addRule(RelativeLayout.CENTER_HORIZONTAL);
        goBackBtn.setLayoutParams(lpGoBackBtn);
        mSocialInfoPanel.addView(goBackBtn);
        mFlipPanel.setViewB(mSocialInfoPanel);
        
		// Search button.
        int boundMargin = LayoutUtil.getDetailDialogBoundMargin();
		mSearchButton = new HaloButton(mContext, R.drawable.search);

		RelativeLayout.LayoutParams searchBtnLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		searchBtnLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		searchBtnLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		searchBtnLP.leftMargin = searchBtnLP.bottomMargin = boundMargin;

		mSocialInfoPanel.addView(mSearchButton, searchBtnLP);
    }
    
	private TextView addInfoTextViewToBasicPanel(int textResId, int index)
	{
		Context context = mContext;
		TextView label = new TextView(context);
		label.setTextColor(UIConfig.getNormalTextColor());
		label.setText(textResId);
		
		RelativeLayout.LayoutParams labelLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		int lineHeight = LayoutUtil.getDetailInfoLineHeight();
		int topMargin = lineHeight * index * 5 / 2 + lineHeight;
		labelLP.topMargin = topMargin;
		
		mDetailInfoPanel.addView(label, labelLP);
		
		TextView valueText = new TextView(context);
		valueText.setSingleLine();
		valueText.setMovementMethod(new ScrollingMovementMethod());
		
		
		valueText.setText("???");
		valueText.setTextSize(12.0f);
		valueText.setTextColor(Color.BLACK);
		
		RelativeLayout.LayoutParams valueTextLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		valueTextLP.topMargin = topMargin + lineHeight;
		valueTextLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mDetailInfoPanel.addView(valueText, valueTextLP);

		return valueText;
	}
	
	public void setData(BookShelfItem bookItem) {
		if (null != bookItem) {
			mBookItem = bookItem;
			BookInfo bookInfo = bookItem.getInfo();
			if (null != bookInfo) {
				mCoverImage = bookInfo.getCoverImage();
				if (null != mCoverImage) {
					mCoverImageView.setImageBitmap(mCoverImage);
					mFullSizeImageDialog.setImage(mCoverImage);
				} else {
					mCoverImageView
							.setImageResource(R.drawable.bookcover_unknown);
					mFullSizeImageDialog.setImage(R.drawable.bookcover_unknown);
				}
				
				String bookName = bookInfo.getBookName();
				if (null == mCoverImage || bookName.length() < 1) {
					mHasCoverImage = false;
				} else {
					mHasCoverImage = true;
				}
				mTitleTextView.setText(bookName);
				mAuthorTextView.setText(bookInfo.getAuthor());
				mPublisherTextView.setText(bookInfo.getPublisher());
				mPageCountTextView.setText(bookInfo.getPageCount());
				mISBNTextView.setText(bookInfo.getISBN());
				mDescriptionTextView.setText(bookInfo.getSummary());
				
				AppData.getInstance().setISBN(bookInfo.getISBN());
			}
			
			if (HomeActivity.getApp().getBookGallery().isMyGallery()) {
				this.mDeleteButton.setVisibility(View.VISIBLE);
				this.mShareButton.setVisibility(View.VISIBLE);
				this.mSearchButton.setVisibility(View.VISIBLE);
				this.mBorrowButton.setVisibility(View.GONE);
			} else {
				this.mDeleteButton.setVisibility(View.GONE);
				this.mShareButton.setVisibility(View.GONE);
				this.mSearchButton.setVisibility(View.GONE);
				this.mBorrowButton.setVisibility(View.VISIBLE);
			}
		}
	}
	
	private void addListenersForBasicInfoPanel() {
		mDeleteButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Context context = v.getContext();
				String confirm = context.getString(R.string.confirm);
				String deleteBookConfirm = context.getString(R.string.delete_book_confirm);
				String ok = context.getString(R.string.ok);
				String cancel = context.getString(R.string.cancel);

				AlertDialog alertDialog = new AlertDialog.Builder(context)
						.setTitle(confirm)
						.setMessage(deleteBookConfirm)
						.setIcon(R.drawable.question)
						.setPositiveButton(ok,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										AppData.getInstance().removeOwnedBook(
												mBookItem.getInfo());
										mBookItem.setVisibility(View.GONE);
										mFlipPanel.dismiss();
										
										String isbn = mBookItem.getInfo()
												.getISBN();

										CloudAPI.deleteBook(
												HomeActivity.getApp(), isbn,
												new ICloudAPITaskListener() {

													@Override
													public void onFinish(
															int returnCode) {

														Context context = HomeActivity
																.getApp();
														CloudAPI.isSuccessful(
																context,
																returnCode);
													}

												});
									}
								})
						.setNegativeButton(cancel,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).create();
				alertDialog.show();
			}
		});
		
		mShareButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				ShareUtil.fireShareIntent(mBookItem.getInfo());
			}
		});
		
//		mCloseButton.setOnClickListener(new View.OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				mFlipPanel.dismiss();
//			}
//		});
		
		mCoverImageView.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				if (mHasCoverImage) {
					mFullSizeImageDialog.show();
				} else {
					ISBNResolver.getInstance().refreshBookInfo(v.getContext(),
							mBookItem);
				}
			}
		});
		
		mBorrowButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SendMessageDialog dialog = new SendMessageDialog(v.getContext(), new ISendHandler(){

					@Override
							public void onSend(String msg) {
								AppData.getInstance().setMessage(msg);
								CloudAPI.sendMessage(HomeActivity.getApp(),
										"0",
										HomeActivity.getApp().getBookGallery()
												.getCurOwner().getId(), AppData
												.getInstance().getISBN(), null);
							}
					
				});
				
				dialog.show();
			}
		});
		
		mSocialButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				mFlipPanel.flip(true);
			}
		});
	}
	
	private void addListenersForSocialPanel()
	{
		
		mSearchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CloudAPI.getUsersByISBN(v.getContext(), mBookItem.getInfo()
						.getISBN(), new ICloudAPITaskListener() {

					@Override
					public void onFinish(int returnCode) {
						// TODO Auto-generated method stub
						BookOwnersDialog usersDialog = new BookOwnersDialog(
								HomeActivity.getApp());
						usersDialog.show();
					}

				});
			}
		});
	}
}
