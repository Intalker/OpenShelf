package com.intalker.openshelf.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.intalker.openshelf.data.AppData;
import com.intalker.openshelf.data.BookInfo;
import com.intalker.openshelf.ui.book.BookShelfItem;
import com.intalker.openshelf.ui.control.FlipPanel;
import com.intalker.openshelf.util.DensityAdaptor;
import com.intalker.openshelf.util.LayoutUtil;

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
    
    public BookInfoPanel(Context ctx)
    {
    	mContext = ctx;
    	mFlipPanel = new FlipPanel(mContext);
//    	mFlipPanel.setBackgroundImage(R.drawable.white_panel_frame);
    	mFlipPanel.setBackgroundImage(R.drawable.login_bg);
    	
    	mFullSizeImageDialog = new FullSizeImageDialog(mContext);
    	
    	createBasicInfoPanel();
    	createSocialInfoPanel();
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
    	mBasicInfoPanel.setBackgroundResource(R.drawable.parchment_bg);
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
		
		//Description textview
		ScrollView scrollView = new ScrollView(mContext);
		
		LinearLayout scrollContent = new LinearLayout(mContext);
		scrollContent.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(scrollContent);
		
		mDescriptionTextView = new TextView(mContext);
		mDescriptionTextView.setText("???");
		mDescriptionTextView.setTextColor(Color.BLACK);
		scrollContent.addView(mDescriptionTextView);
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		int margin = LayoutUtil.getDetailDialogBoundMargin();
		lp.leftMargin = margin;
		lp.rightMargin = margin;
		lp.topMargin = margin * 2 + LayoutUtil.getDetailDialogHeight() / 2 - DensityAdaptor.getDensityIndependentValue(6);
		lp.width = LayoutUtil.getDetailDialogWidth() - margin * 2;
		lp.height = LayoutUtil.getDetailDialogHeight() * 3 / 10;;
		
		scrollView.setLayoutParams(lp);
		mBasicInfoPanel.addView(scrollView);
		//
        
        Button goBtn = new Button(mContext);
        goBtn.setText("Go");
        goBtn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	mFlipPanel.flip(true);
            }
        });
        
        RelativeLayout.LayoutParams lpGoBtn = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpGoBtn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpGoBtn.addRule(RelativeLayout.CENTER_HORIZONTAL);
        goBtn.setLayoutParams(lpGoBtn);
        mBasicInfoPanel.addView(goBtn);
        mFlipPanel.setViewA(mBasicInfoPanel);
    }
    
    private void createSocialInfoPanel()
    {
    	mSocialInfoPanel = new RelativeLayout(mContext);
    	mSocialInfoPanel.setBackgroundResource(R.drawable.stone_bg);
        FrameLayout.LayoutParams lpB = new FrameLayout.LayoutParams(specWidth, specHeight);
        lpB.gravity = Gravity.CENTER;
        mSocialInfoPanel.setLayoutParams(lpB);
        
        TextView t2 = new TextView(mContext);
        t2.setText("[TODO]:\nShow social info here.");
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.CENTER_IN_PARENT);
        t2.setLayoutParams(lp2);
        mSocialInfoPanel.addView(t2);

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
			
//			if (HomeActivity.getApp().getBookGallery().isMyGallery()) {
//				this.mDeleteButton.setVisibility(View.VISIBLE);
//				this.mShareButton.setVisibility(View.VISIBLE);
//				this.mSearchButton.setVisibility(View.VISIBLE);
//				this.mBorrowButton.setVisibility(View.GONE);
//			} else {
//				this.mDeleteButton.setVisibility(View.GONE);
//				this.mShareButton.setVisibility(View.GONE);
//				this.mSearchButton.setVisibility(View.GONE);
//				this.mBorrowButton.setVisibility(View.VISIBLE);
//			}
		}
	}
}
