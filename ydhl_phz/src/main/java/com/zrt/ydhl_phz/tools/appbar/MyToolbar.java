package com.zrt.ydhl_phz.tools.appbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.view.GravityCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;

import com.zrt.ydhl_phz.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：Zrt
 * @date: 2022/11/7
 */
public class MyToolbar extends Toolbar{
    private static final String TAG = "Toolbar";

    private ActionMenuView mMenuView;
    private View mTitleTextView;
    private TextView mSubtitleTextView;
    private ImageButton mNavButtonView;
    private ImageView mLogoView;

    private Drawable mCollapseIcon;
    private CharSequence mCollapseDescription;
    ImageButton mCollapseButtonView;
    View mExpandedActionView;

    /** Context against which to inflate popup menus. */
    private Context mPopupContext;

    /** Theme resource against which to inflate popup menus. */
    private int mPopupTheme;

    private int mTitleTextAppearance;
    private int mSubtitleTextAppearance;

    int mButtonGravity;

    private int mMaxButtonHeight;

    private int mTitleMarginStart;
    private int mTitleMarginEnd;
    private int mTitleMarginTop;
    private int mTitleMarginBottom;

    private int mContentInsetStartWithNavigation;
    private int mContentInsetEndWithActions;

    private int mGravity = GravityCompat.START | Gravity.CENTER_VERTICAL;

    private CharSequence mTitleText;
    private CharSequence mSubtitleText;

    private ColorStateList mTitleTextColor;
    private ColorStateList mSubtitleTextColor;

    private boolean mEatingTouch;
    private boolean mEatingHover;

    public static final int UNDEFINED = Integer.MIN_VALUE;

    // Clear me after use.
    private final ArrayList<View> mTempViews = new ArrayList<View>();

    // Used to hold views that will be removed while we have an expanded action view.
    private final ArrayList<View> mHiddenViews = new ArrayList<>();

    private final int[] mTempMargins = new int[2];

    Toolbar.OnMenuItemClickListener mOnMenuItemClickListener;

    private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener =
            new ActionMenuView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (mOnMenuItemClickListener != null) {
                        return mOnMenuItemClickListener.onMenuItemClick(item);
                    }
                    return false;
                }
            };

    private ToolbarWidgetWrapper mWrapper;
    //    private ActionMenuPresenter mOuterActionMenuPresenter;
//    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    private MenuBuilder.Callback mMenuBuilderCallback;

    private boolean mCollapsible;

    private final Runnable mShowOverflowMenuRunnable = new Runnable() {
        @Override public void run() {
            showOverflowMenu();
        }
    };

    public MyToolbar(Context context) {
        this(context, null);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }
    @SuppressLint("RestrictedApi")
    public MyToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // Need to use getContext() here so that we use the themed context
//        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
//                R.styleable.Toolbar, defStyleAttr, 0);
//        mTitleTextAppearance = a.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
//        mSubtitleTextAppearance = a.getResourceId(R.styleable.Toolbar_subtitleTextAppearance, 0);
//        mGravity = a.getInteger(R.styleable.Toolbar_android_gravity, mGravity);
//        mButtonGravity = a.getInteger(R.styleable.Toolbar_buttonGravity, Gravity.TOP);
//        // First read the correct attribute
//        int titleMargin = a.getDimensionPixelOffset(R.styleable.Toolbar_titleMargin, 0);
//        if (a.hasValue(R.styleable.Toolbar_titleMargins)) {
//            // Now read the deprecated attribute, if it has a value
//            titleMargin = a.getDimensionPixelOffset(R.styleable.Toolbar_titleMargins, titleMargin);
//        }
//        mTitleMarginStart = mTitleMarginEnd = mTitleMarginTop = mTitleMarginBottom = titleMargin;
//
//        final int marginStart = a.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginStart, -1);
//        if (marginStart >= 0) {
//            mTitleMarginStart = marginStart;
//        }
//
//        final int marginEnd = a.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginEnd, -1);
//        if (marginEnd >= 0) {
//            mTitleMarginEnd = marginEnd;
//        }
//        final int marginTop = a.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginTop, -1);
//        if (marginTop >= 0) {
//            mTitleMarginTop = marginTop;
//        }
//        final int marginBottom = a.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginBottom,
//                -1);
//        if (marginBottom >= 0) {
//            mTitleMarginBottom = marginBottom;
//        }
//        mMaxButtonHeight = a.getDimensionPixelSize(R.styleable.Toolbar_maxButtonHeight, -1);
//        final int contentInsetStart =
//                a.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStart,
//                        UNDEFINED);
//        final int contentInsetEnd =
//                a.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEnd,
//                        UNDEFINED);
//        final int contentInsetLeft =
//                a.getDimensionPixelSize(R.styleable.Toolbar_contentInsetLeft, 0);
//        final int contentInsetRight =
//                a.getDimensionPixelSize(R.styleable.Toolbar_contentInsetRight, 0);
//        ensureContentInsets();
//        mContentInsets.setAbsolute(contentInsetLeft, contentInsetRight);
//        if (contentInsetStart != UNDEFINED ||
//                contentInsetEnd != UNDEFINED) {
//            mContentInsets.setRelative(contentInsetStart, contentInsetEnd);
//        }
//        mContentInsetStartWithNavigation = a.getDimensionPixelOffset(
//                R.styleable.Toolbar_contentInsetStartWithNavigation, UNDEFINED);
//        mContentInsetEndWithActions = a.getDimensionPixelOffset(
//                R.styleable.Toolbar_contentInsetEndWithActions, UNDEFINED);
//        mCollapseIcon = a.getDrawable(R.styleable.Toolbar_collapseIcon);
//        mCollapseDescription = a.getText(R.styleable.Toolbar_collapseContentDescription);
//        final CharSequence title = a.getText(R.styleable.Toolbar_title);
//        if (!TextUtils.isEmpty(title)) {
//            setTitle(title);
//        }
//        final CharSequence subtitle = a.getText(R.styleable.Toolbar_subtitle);
//        if (!TextUtils.isEmpty(subtitle)) {
//            setSubtitle(subtitle);
//        }
//        // Set the default context, since setPopupTheme() may be a no-op.
//        mPopupContext = getContext();
//        setPopupTheme(a.getResourceId(R.styleable.Toolbar_popupTheme, 0));
//        final Drawable navIcon = a.getDrawable(R.styleable.Toolbar_navigationIcon);
//        if (navIcon != null) {
//            setNavigationIcon(navIcon);
//        }
//        final CharSequence navDesc = a.getText(R.styleable.Toolbar_navigationContentDescription);
//        if (!TextUtils.isEmpty(navDesc)) {
//            setNavigationContentDescription(navDesc);
//        }
//        final Drawable logo = a.getDrawable(R.styleable.Toolbar_logo);
//        if (logo != null) {
//            setLogo(logo);
//        }
//        final CharSequence logoDesc = a.getText(R.styleable.Toolbar_logoDescription);
//        if (!TextUtils.isEmpty(logoDesc)) {
//            setLogoDescription(logoDesc);
//        }
//        if (a.hasValue(R.styleable.Toolbar_titleTextColor)) {
//            setTitleTextColor(a.getColorStateList(R.styleable.Toolbar_titleTextColor));
//        }
//        if (a.hasValue(R.styleable.Toolbar_subtitleTextColor)) {
//            setSubtitleTextColor(a.getColorStateList(R.styleable.Toolbar_subtitleTextColor));
//        }
//        if (a.hasValue(R.styleable.Toolbar_menu)) {
//            inflateMenu(a.getResourceId(R.styleable.Toolbar_menu, 0));
//        }
//        a.recycle();
    }
    /**
     * Specifies the theme to use when inflating popup menus. By default, uses
     * the same theme as the toolbar itself.
     *
     * @param resId theme used to inflate popup menus
     * @see #getPopupTheme()
     */
    public void setPopupTheme(@StyleRes int resId) {
        if (mPopupTheme != resId) {
            mPopupTheme = resId;
            if (resId == 0) {
                mPopupContext = getContext();
            } else {
                mPopupContext = new ContextThemeWrapper(getContext(), resId);
            }
        }
    }

    /**
     * @return resource identifier of the theme used to inflate popup menus, or
     *         0 if menus are inflated against the toolbar theme
     * @see #setPopupTheme(int)
     */
    public int getPopupTheme() {
        return mPopupTheme;
    }

    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
    }

    public void setTitle(View titleView) {
        if (titleView != null) {
            if (mTitleTextView == null) {
                mTitleTextView = titleView;
            }

            if (!isChildOrHidden(mTitleTextView)) {
                addSystemView(mTitleTextView, true);
            }
        } else if (mTitleTextView != null && isChildOrHidden(mTitleTextView)) {
            removeView(mTitleTextView);
            mHiddenViews.remove(mTitleTextView);
        }
        mTitleText = "";
    }

//    private void ensureMenu() {
//        ensureMenuView();
//        if (mMenuView.peekMenu() == null) {
//            // Initialize a new menu for the first time.
//            final MenuBuilder menu = (MenuBuilder) mMenuView.getMenu();
//            if (mExpandedMenuPresenter == null) {
//                mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
//            }
//            mMenuView.setExpandedActionViewsExclusive(true);
//            menu.addMenuPresenter(mExpandedMenuPresenter, mPopupContext);
//        }
//    }
//
//    private void ensureMenuView() {
//        if (mMenuView == null) {
//            mMenuView = new ActionMenuView(getContext());
//            mMenuView.setPopupTheme(mPopupTheme);
//            mMenuView.setOnMenuItemClickListener(mMenuViewItemClickListener);
//            mMenuView.setMenuCallbacks(mActionMenuPresenterCallback, mMenuBuilderCallback);
//            final Toolbar.LayoutParams lp = generateDefaultLayoutParams();
//            lp.gravity = GravityCompat.END | (mButtonGravity & Gravity.VERTICAL_GRAVITY_MASK);
//            mMenuView.setLayoutParams(lp);
//            addSystemView(mMenuView, false);
//        }
//    }

    void ensureCollapseButtonView() {
        if (mCollapseButtonView == null) {
            mCollapseButtonView = new AppCompatImageButton(getContext(), null,
                    R.attr.toolbarNavigationButtonStyle);
            mCollapseButtonView.setImageDrawable(mCollapseIcon);
            mCollapseButtonView.setContentDescription(mCollapseDescription);
            final MyToolbar.LayoutParams lp = generateDefaultLayoutParams();
            lp.gravity = GravityCompat.START | (mButtonGravity & Gravity.VERTICAL_GRAVITY_MASK);
            lp.mViewType = MyToolbar.LayoutParams.EXPANDED;
            mCollapseButtonView.setLayoutParams(lp);
            mCollapseButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collapseActionView();
                }
            });
        }
    }

    private void addSystemView(View v, boolean allowHide) {
        final ViewGroup.LayoutParams vlp = v.getLayoutParams();
        final MyToolbar.LayoutParams lp;
        if (vlp == null) {
            lp = generateDefaultLayoutParams();
        } else if (!checkLayoutParams(vlp)) {
            lp = generateLayoutParams(vlp);
        } else {
            lp = (MyToolbar.LayoutParams) vlp;
        }
        lp.mViewType = MyToolbar.LayoutParams.SYSTEM;

        if (allowHide && mExpandedActionView != null) {
            v.setLayoutParams(lp);
            mHiddenViews.add(v);
        } else {
            addView(v, lp);
        }
    }

    private void measureChildConstrained(View child, int parentWidthSpec, int widthUsed,
                                         int parentHeightSpec, int heightUsed, int heightConstraint) {
        final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();

        int childWidthSpec = getChildMeasureSpec(parentWidthSpec,
                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width);
        int childHeightSpec = getChildMeasureSpec(parentHeightSpec,
                getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin
                        + heightUsed, lp.height);

        final int childHeightMode = View.MeasureSpec.getMode(childHeightSpec);
        if (childHeightMode != View.MeasureSpec.EXACTLY && heightConstraint >= 0) {
            final int size = childHeightMode != View.MeasureSpec.UNSPECIFIED ?
                    Math.min(View.MeasureSpec.getSize(childHeightSpec), heightConstraint) :
                    heightConstraint;
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * Returns the width + uncollapsed margins
     */
    private int measureChildCollapseMargins(View child,
                                            int parentWidthMeasureSpec, int widthUsed,
                                            int parentHeightMeasureSpec, int heightUsed, int[] collapsingMargins) {
        final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();

        final int leftDiff = lp.leftMargin - collapsingMargins[0];
        final int rightDiff = lp.rightMargin - collapsingMargins[1];
        final int leftMargin = Math.max(0, leftDiff);
        final int rightMargin = Math.max(0, rightDiff);
        final int hMargins = leftMargin + rightMargin;
        collapsingMargins[0] = Math.max(0, -leftDiff);
        collapsingMargins[1] = Math.max(0, -rightDiff);

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight() + hMargins + widthUsed, lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin
                        + heightUsed, lp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        return child.getMeasuredWidth() + hMargins;
    }

    /**
     * Returns true if the Toolbar is collapsible and has no child views with a measured size > 0.
     */
    private boolean shouldCollapse() {
        if (!mCollapsible) return false;

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (shouldLayout(child) && child.getMeasuredWidth() > 0 &&
                    child.getMeasuredHeight() > 0) {
                return false;
            }
        }
        return true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;
        int childState = 0;

        final int[] collapsingMargins = mTempMargins;
        final int marginStartIndex;
        final int marginEndIndex;
        if (ViewUtils.isLayoutRtl(this)) {
            marginStartIndex = 1;
            marginEndIndex = 0;
        } else {
            marginStartIndex = 0;
            marginEndIndex = 1;
        }

        // System views measure first.

        int navWidth = 0;
        if (shouldLayout(mNavButtonView)) {
            measureChildConstrained(mNavButtonView, widthMeasureSpec, width, heightMeasureSpec, 0,
                    mMaxButtonHeight);
            navWidth = mNavButtonView.getMeasuredWidth() + getHorizontalMargins(mNavButtonView);
            height = Math.max(height, mNavButtonView.getMeasuredHeight() +
                    getVerticalMargins(mNavButtonView));
            childState = View.combineMeasuredStates(childState,
                    mNavButtonView.getMeasuredState());
        }

        if (shouldLayout(mCollapseButtonView)) {
            measureChildConstrained(mCollapseButtonView, widthMeasureSpec, width,
                    heightMeasureSpec, 0, mMaxButtonHeight);
            navWidth = mCollapseButtonView.getMeasuredWidth() +
                    getHorizontalMargins(mCollapseButtonView);
            height = Math.max(height, mCollapseButtonView.getMeasuredHeight() +
                    getVerticalMargins(mCollapseButtonView));
            childState = View.combineMeasuredStates(childState,
                    mCollapseButtonView.getMeasuredState());
        }

        final int contentInsetStart = getCurrentContentInsetStart();
        width += Math.max(contentInsetStart, navWidth);
        collapsingMargins[marginStartIndex] = Math.max(0, contentInsetStart - navWidth);

        int menuWidth = 0;
        if (shouldLayout(mMenuView)) {
            measureChildConstrained(mMenuView, widthMeasureSpec, width, heightMeasureSpec, 0,
                    mMaxButtonHeight);
            menuWidth = mMenuView.getMeasuredWidth() + getHorizontalMargins(mMenuView);
            height = Math.max(height, mMenuView.getMeasuredHeight() +
                    getVerticalMargins(mMenuView));
            childState = View.combineMeasuredStates(childState,
                    mMenuView.getMeasuredState());
        }

        final int contentInsetEnd = getCurrentContentInsetEnd();
        width += Math.max(contentInsetEnd, menuWidth);
        collapsingMargins[marginEndIndex] = Math.max(0, contentInsetEnd - menuWidth);

        if (shouldLayout(mExpandedActionView)) {
            width += measureChildCollapseMargins(mExpandedActionView, widthMeasureSpec, width,
                    heightMeasureSpec, 0, collapsingMargins);
            height = Math.max(height, mExpandedActionView.getMeasuredHeight() +
                    getVerticalMargins(mExpandedActionView));
            childState = View.combineMeasuredStates(childState,
                    mExpandedActionView.getMeasuredState());
        }

        if (shouldLayout(mLogoView)) {
            width += measureChildCollapseMargins(mLogoView, widthMeasureSpec, width,
                    heightMeasureSpec, 0, collapsingMargins);
            height = Math.max(height, mLogoView.getMeasuredHeight() +
                    getVerticalMargins(mLogoView));
            childState = View.combineMeasuredStates(childState,
                    mLogoView.getMeasuredState());
        }

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final MyToolbar.LayoutParams lp = (MyToolbar.LayoutParams) child.getLayoutParams();
            if (lp.mViewType != MyToolbar.LayoutParams.CUSTOM || !shouldLayout(child)) {
                // We already got all system views above. Skip them and GONE views.
                continue;
            }

            width += measureChildCollapseMargins(child, widthMeasureSpec, width,
                    heightMeasureSpec, 0, collapsingMargins);
            height = Math.max(height, child.getMeasuredHeight() + getVerticalMargins(child));
            childState = View.combineMeasuredStates(childState, child.getMeasuredState());
        }

        int titleWidth = 0;
        int titleHeight = 0;
        final int titleVertMargins = mTitleMarginTop + mTitleMarginBottom;
        final int titleHorizMargins = mTitleMarginStart + mTitleMarginEnd;
        if (shouldLayout(mTitleTextView)) {
            titleWidth = measureChildCollapseMargins(mTitleTextView, widthMeasureSpec,
                    width + titleHorizMargins, heightMeasureSpec, titleVertMargins,
                    collapsingMargins);
            titleWidth = mTitleTextView.getMeasuredWidth() + getHorizontalMargins(mTitleTextView);
            titleHeight = mTitleTextView.getMeasuredHeight() + getVerticalMargins(mTitleTextView);
            childState = View.combineMeasuredStates(childState, mTitleTextView.getMeasuredState());
        }
        if (shouldLayout(mSubtitleTextView)) {
            titleWidth = Math.max(titleWidth, measureChildCollapseMargins(mSubtitleTextView,
                    widthMeasureSpec, width + titleHorizMargins,
                    heightMeasureSpec, titleHeight + titleVertMargins,
                    collapsingMargins));
            titleHeight += mSubtitleTextView.getMeasuredHeight() +
                    getVerticalMargins(mSubtitleTextView);
            childState = View.combineMeasuredStates(childState,
                    mSubtitleTextView.getMeasuredState());
        }

        width += titleWidth;
        height = Math.max(height, titleHeight);

        // Measurement already took padding into account for available space for the children,
        // add it in for the final size.
        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        final int measuredWidth = View.resolveSizeAndState(
                Math.max(width, getSuggestedMinimumWidth()),
                widthMeasureSpec, childState & View.MEASURED_STATE_MASK);
        final int measuredHeight = View.resolveSizeAndState(
                Math.max(height, getSuggestedMinimumHeight()),
                heightMeasureSpec, childState << View.MEASURED_HEIGHT_STATE_SHIFT);

        setMeasuredDimension(measuredWidth, shouldCollapse() ? 0 : measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final boolean isRtl =  ViewCompat.getLayoutDirection(this) ==  ViewCompat.LAYOUT_DIRECTION_RTL;
        final int width = getWidth();
        final int height = getHeight();
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        int left = paddingLeft;
        int right = width - paddingRight;

        final int[] collapsingMargins = mTempMargins;
        collapsingMargins[0] = collapsingMargins[1] = 0;

        // Align views within the minimum toolbar height, if set.
        final int minHeight = ViewCompat.getMinimumHeight(this);
        final int alignmentHeight = minHeight >= 0 ? Math.min(minHeight, b - t) : 0;

        if (shouldLayout(mNavButtonView)) {
            if (isRtl) {
                right = layoutChildRight(mNavButtonView, right, collapsingMargins,
                        alignmentHeight);
            } else {
                left = layoutChildLeft(mNavButtonView, left, collapsingMargins,
                        alignmentHeight);
            }
        }

        if (shouldLayout(mCollapseButtonView)) {
            if (isRtl) {
                right = layoutChildRight(mCollapseButtonView, right, collapsingMargins,
                        alignmentHeight);
            } else {
                left = layoutChildLeft(mCollapseButtonView, left, collapsingMargins,
                        alignmentHeight);
            }
        }

        if (shouldLayout(mMenuView)) {
            if (isRtl) {
                left = layoutChildLeft(mMenuView, left, collapsingMargins,
                        alignmentHeight);
            } else {
                right = layoutChildRight(mMenuView, right, collapsingMargins,
                        alignmentHeight);
            }
        }

        final int contentInsetLeft = getCurrentContentInsetLeft();
        final int contentInsetRight = getCurrentContentInsetRight();
        collapsingMargins[0] = Math.max(0, contentInsetLeft - left);
        collapsingMargins[1] = Math.max(0, contentInsetRight - (width - paddingRight - right));
        left = Math.max(left, contentInsetLeft);
        right = Math.min(right, width - paddingRight - contentInsetRight);

        if (shouldLayout(mExpandedActionView)) {
            if (isRtl) {
                right = layoutChildRight(mExpandedActionView, right, collapsingMargins,
                        alignmentHeight);
            } else {
                left = layoutChildLeft(mExpandedActionView, left, collapsingMargins,
                        alignmentHeight);
            }
        }

        if (shouldLayout(mLogoView)) {
            if (isRtl) {
                right = layoutChildRight(mLogoView, right, collapsingMargins,
                        alignmentHeight);
            } else {
                left = layoutChildLeft(mLogoView, left, collapsingMargins,
                        alignmentHeight);
            }
        }

        final boolean layoutTitle = shouldLayout(mTitleTextView);
        final boolean layoutSubtitle = shouldLayout(mSubtitleTextView);
        int titleHeight = 0;
        if (layoutTitle) {
            final Toolbar.LayoutParams lp = (Toolbar.LayoutParams) mTitleTextView.getLayoutParams();
            titleHeight += lp.topMargin + mTitleTextView.getMeasuredHeight() + lp.bottomMargin;
        }
        if (layoutSubtitle) {
            final Toolbar.LayoutParams lp = (Toolbar.LayoutParams) mSubtitleTextView.getLayoutParams();
            titleHeight += lp.topMargin + mSubtitleTextView.getMeasuredHeight() + lp.bottomMargin;
        }

        if (layoutTitle || layoutSubtitle) {
            int titleTop;
            final View topChild = layoutTitle ? mTitleTextView : mSubtitleTextView;
            final View bottomChild = layoutSubtitle ? mSubtitleTextView : mTitleTextView;
            final Toolbar.LayoutParams toplp = (Toolbar.LayoutParams) topChild.getLayoutParams();
            final Toolbar.LayoutParams bottomlp = (Toolbar.LayoutParams) bottomChild.getLayoutParams();
            final boolean titleHasWidth = (layoutTitle && (mTitleTextView.getMeasuredWidth() > 0))
                    || (layoutSubtitle && mSubtitleTextView.getMeasuredWidth() > 0);

            switch (mGravity & Gravity.VERTICAL_GRAVITY_MASK) {
                case Gravity.TOP:
                    titleTop = getPaddingTop() + toplp.topMargin + mTitleMarginTop;
                    break;
                default:
                case Gravity.CENTER_VERTICAL:
                    final int space = height - paddingTop - paddingBottom;
                    int spaceAbove = (space - titleHeight) / 2;
                    if (spaceAbove < toplp.topMargin + mTitleMarginTop) {
                        spaceAbove = toplp.topMargin + mTitleMarginTop;
                    } else {
                        final int spaceBelow = height - paddingBottom - titleHeight -
                                spaceAbove - paddingTop;
                        if (spaceBelow < toplp.bottomMargin + mTitleMarginBottom) {
                            spaceAbove = Math.max(0, spaceAbove -
                                    (bottomlp.bottomMargin + mTitleMarginBottom - spaceBelow));
                        }
                    }
                    titleTop = paddingTop + spaceAbove;
                    break;
                case Gravity.BOTTOM:
                    titleTop = height - paddingBottom - bottomlp.bottomMargin - mTitleMarginBottom -
                            titleHeight;
                    break;
            }
            if (isRtl) {
                final int rd = (titleHasWidth ? mTitleMarginStart : 0) - collapsingMargins[1];
                right -= Math.max(0, rd);
                collapsingMargins[1] = Math.max(0, -rd);
                int titleRight = right;
                int subtitleRight = right;

                if (layoutTitle) {
                    final Toolbar.LayoutParams lp = (Toolbar.LayoutParams) mTitleTextView.getLayoutParams();
                    final int titleLeft = titleRight - mTitleTextView.getMeasuredWidth();
                    final int titleBottom = titleTop + mTitleTextView.getMeasuredHeight();
                    mTitleTextView.layout(titleLeft, titleTop, titleRight, titleBottom);
                    titleRight = titleLeft - mTitleMarginEnd;
                    titleTop = titleBottom + lp.bottomMargin;
                }
                if (layoutSubtitle) {
                    final Toolbar.LayoutParams lp = (Toolbar.LayoutParams) mSubtitleTextView.getLayoutParams();
                    titleTop += lp.topMargin;
                    final int subtitleLeft = subtitleRight - mSubtitleTextView.getMeasuredWidth();
                    final int subtitleBottom = titleTop + mSubtitleTextView.getMeasuredHeight();
                    mSubtitleTextView.layout(subtitleLeft, titleTop, subtitleRight, subtitleBottom);
                    subtitleRight = subtitleRight - mTitleMarginEnd;
                    titleTop = subtitleBottom + lp.bottomMargin;
                }
                if (titleHasWidth) {
                    right = Math.min(titleRight, subtitleRight);
                }
            } else {
                final int ld = (titleHasWidth ? mTitleMarginStart : 0) - collapsingMargins[0];
                left += Math.max(0, ld);
                collapsingMargins[0] = Math.max(0, -ld);
                int titleLeft = left;
                int subtitleLeft = left;

                if (layoutTitle) {
                    final Toolbar.LayoutParams lp = (Toolbar.LayoutParams) mTitleTextView.getLayoutParams();
                    final int titleRight = titleLeft + mTitleTextView.getMeasuredWidth();
                    final int titleBottom = titleTop + mTitleTextView.getMeasuredHeight();
                    mTitleTextView.layout(titleLeft, titleTop, titleRight, titleBottom);
                    titleLeft = titleRight + mTitleMarginEnd;
                    titleTop = titleBottom + lp.bottomMargin;
                }
                if (layoutSubtitle) {
                    final Toolbar.LayoutParams lp = (Toolbar.LayoutParams) mSubtitleTextView.getLayoutParams();
                    titleTop += lp.topMargin;
                    final int subtitleRight = subtitleLeft + mSubtitleTextView.getMeasuredWidth();
                    final int subtitleBottom = titleTop + mSubtitleTextView.getMeasuredHeight();
                    mSubtitleTextView.layout(subtitleLeft, titleTop, subtitleRight, subtitleBottom);
                    subtitleLeft = subtitleRight + mTitleMarginEnd;
                    titleTop = subtitleBottom + lp.bottomMargin;
                }
                if (titleHasWidth) {
                    left = Math.max(titleLeft, subtitleLeft);
                }
            }
        }

        // Get all remaining children sorted for layout. This is all prepared
        // such that absolute layout direction can be used below.

        addCustomViewsWithGravity(mTempViews, Gravity.LEFT);
        final int leftViewsCount = mTempViews.size();
        for (int i = 0; i < leftViewsCount; i++) {
            left = layoutChildLeft(mTempViews.get(i), left, collapsingMargins,
                    alignmentHeight);
        }

        addCustomViewsWithGravity(mTempViews, Gravity.RIGHT);
        final int rightViewsCount = mTempViews.size();
        for (int i = 0; i < rightViewsCount; i++) {
            right = layoutChildRight(mTempViews.get(i), right, collapsingMargins,
                    alignmentHeight);
        }

        // Centered views try to center with respect to the whole bar, but views pinned
        // to the left or right can push the mass of centered views to one side or the other.
        addCustomViewsWithGravity(mTempViews, Gravity.CENTER_HORIZONTAL);
        final int centerViewsWidth = getViewListMeasuredWidth(mTempViews, collapsingMargins);
        final int parentCenter = paddingLeft + (width - paddingLeft - paddingRight) / 2;
        final int halfCenterViewsWidth = centerViewsWidth / 2;
        int centerLeft = parentCenter - halfCenterViewsWidth;
        final int centerRight = centerLeft + centerViewsWidth;
        if (centerLeft < left) {
            centerLeft = left;
        } else if (centerRight > right) {
            centerLeft -= centerRight - right;
        }

        final int centerViewsCount = mTempViews.size();
        for (int i = 0; i < centerViewsCount; i++) {
            centerLeft = layoutChildLeft(mTempViews.get(i), centerLeft, collapsingMargins,
                    alignmentHeight);
        }

        mTempViews.clear();
    }

    private int getViewListMeasuredWidth(List<View> views, int[] collapsingMargins) {
        int collapseLeft = collapsingMargins[0];
        int collapseRight = collapsingMargins[1];
        int width = 0;
        final int count = views.size();
        for (int i = 0; i < count; i++) {
            final View v = views.get(i);
            final Toolbar.LayoutParams lp = (Toolbar.LayoutParams) v.getLayoutParams();
            final int l = lp.leftMargin - collapseLeft;
            final int r = lp.rightMargin - collapseRight;
            final int leftMargin = Math.max(0, l);
            final int rightMargin = Math.max(0, r);
            collapseLeft = Math.max(0, -l);
            collapseRight = Math.max(0, -r);
            width += leftMargin + v.getMeasuredWidth() + rightMargin;
        }
        return width;
    }

    private int layoutChildLeft(View child, int left, int[] collapsingMargins,
                                int alignmentHeight) {
        final Toolbar.LayoutParams lp = (Toolbar.LayoutParams) child.getLayoutParams();
        final int l = lp.leftMargin - collapsingMargins[0];
        left += Math.max(0, l);
        collapsingMargins[0] = Math.max(0, -l);
        final int top = getChildTop(child, alignmentHeight);
        final int childWidth = child.getMeasuredWidth();
        child.layout(left, top, left + childWidth, top + child.getMeasuredHeight());
        left += childWidth + lp.rightMargin;
        return left;
    }

    private int layoutChildRight(View child, int right, int[] collapsingMargins,
                                 int alignmentHeight) {
        final Toolbar.LayoutParams lp = (Toolbar.LayoutParams) child.getLayoutParams();
        final int r = lp.rightMargin - collapsingMargins[1];
        right -= Math.max(0, r);
        collapsingMargins[1] = Math.max(0, -r);
        final int top = getChildTop(child, alignmentHeight);
        final int childWidth = child.getMeasuredWidth();
        child.layout(right - childWidth, top, right, top + child.getMeasuredHeight());
        right -= childWidth + lp.leftMargin;
        return right;
    }

    private int getChildTop(View child, int alignmentHeight) {
        final Toolbar.LayoutParams lp = (Toolbar.LayoutParams) child.getLayoutParams();
        final int childHeight = child.getMeasuredHeight();
        final int alignmentOffset = alignmentHeight > 0 ? (childHeight - alignmentHeight) / 2 : 0;
        switch (getChildVerticalGravity(lp.gravity)) {
            case Gravity.TOP:
                return getPaddingTop() - alignmentOffset;

            case Gravity.BOTTOM:
                return getHeight() - getPaddingBottom() - childHeight
                        - lp.bottomMargin - alignmentOffset;

            default:
            case Gravity.CENTER_VERTICAL:
                final int paddingTop = getPaddingTop();
                final int paddingBottom = getPaddingBottom();
                final int height = getHeight();
                final int space = height - paddingTop - paddingBottom;
                int spaceAbove = (space - childHeight) / 2;
                if (spaceAbove < lp.topMargin) {
                    spaceAbove = lp.topMargin;
                } else {
                    final int spaceBelow = height - paddingBottom - childHeight -
                            spaceAbove - paddingTop;
                    if (spaceBelow < lp.bottomMargin) {
                        spaceAbove = Math.max(0, spaceAbove - (lp.bottomMargin - spaceBelow));
                    }
                }
                return paddingTop + spaceAbove;
        }
    }

    private int getChildVerticalGravity(int gravity) {
        final int vgrav = gravity & Gravity.VERTICAL_GRAVITY_MASK;
        switch (vgrav) {
            case Gravity.TOP:
            case Gravity.BOTTOM:
            case Gravity.CENTER_VERTICAL:
                return vgrav;
            default:
                return mGravity & Gravity.VERTICAL_GRAVITY_MASK;
        }
    }

    /**
     * Prepare a list of non-SYSTEM child views. If the layout direction is RTL
     * this will be in reverse child order.
     *
     * @param views List to populate. It will be cleared before use.
     * @param gravity Horizontal gravity to match against
     */
    private void addCustomViewsWithGravity(List<View> views, int gravity) {
        final boolean isRtl =  ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
        final int childCount = getChildCount();
        final int absGrav = GravityCompat.getAbsoluteGravity(gravity,
                ViewCompat.getLayoutDirection(this));

        views.clear();

        if (isRtl) {
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = getChildAt(i);
                final MyToolbar.LayoutParams lp = (MyToolbar.LayoutParams) child.getLayoutParams();
                if (lp.mViewType == MyToolbar.LayoutParams.CUSTOM && shouldLayout(child) &&
                        getChildHorizontalGravity(lp.gravity) == absGrav) {
                    views.add(child);
                }
            }
        } else {
            for (int i = 0; i < childCount; i++) {
                final View child = getChildAt(i);
                final MyToolbar.LayoutParams lp = (MyToolbar.LayoutParams) child.getLayoutParams();
                if (lp.mViewType == MyToolbar.LayoutParams.CUSTOM && shouldLayout(child) &&
                        getChildHorizontalGravity(lp.gravity) == absGrav) {
                    views.add(child);
                }
            }
        }
    }

    private int getChildHorizontalGravity(int gravity) {
        final int ld =  ViewCompat.getLayoutDirection(this);
        final int absGrav = GravityCompat.getAbsoluteGravity(gravity, ld);
        final int hGrav = absGrav & Gravity.HORIZONTAL_GRAVITY_MASK;
        switch (hGrav) {
            case Gravity.LEFT:
            case Gravity.RIGHT:
            case Gravity.CENTER_HORIZONTAL:
                return hGrav;
            default:
                return ld == ViewCompat.LAYOUT_DIRECTION_RTL ? Gravity.RIGHT : Gravity.LEFT;
        }
    }

    private boolean shouldLayout(View view) {
        return view != null && view.getParent() == this && view.getVisibility() != GONE;
    }

    private int getHorizontalMargins(View v) {
        final ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart(mlp) +
                MarginLayoutParamsCompat.getMarginEnd(mlp);
    }

    private int getVerticalMargins(View v) {
        final ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        return mlp.topMargin + mlp.bottomMargin;
    }

    @Override
    public MyToolbar.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MyToolbar.LayoutParams(getContext(), attrs);
    }

    @Override
    protected MyToolbar.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        if (p instanceof MyToolbar.LayoutParams) {
            return new MyToolbar.LayoutParams((Toolbar.LayoutParams) p);
        } else if (p instanceof ActionBar.LayoutParams) {
            return new MyToolbar.LayoutParams((ActionBar.LayoutParams) p);
        } else if (p instanceof ViewGroup.MarginLayoutParams) {
            return new MyToolbar.LayoutParams((ViewGroup.MarginLayoutParams) p);
        } else {
            return new MyToolbar.LayoutParams(p);
        }
    }

    @Override
    protected MyToolbar.LayoutParams generateDefaultLayoutParams() {
        return new MyToolbar.LayoutParams(MyToolbar.LayoutParams.WRAP_CONTENT, MyToolbar.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p) && p instanceof Toolbar.LayoutParams;
    }

    private static boolean isCustomView(View child) {
        return ((MyToolbar.LayoutParams) child.getLayoutParams()).mViewType == MyToolbar.LayoutParams.CUSTOM;
    }



    private boolean isChildOrHidden(View child) {
        return child.getParent() == this || mHiddenViews.contains(child);
    }






    /**
     * Layout information for child views of Toolbars.
     *
     * <p>Toolbar.LayoutParams extends ActionBar.LayoutParams for compatibility with existing
     * ActionBar API. See
     * {@link androidx.appcompat.app.AppCompatActivity#setSupportActionBar(Toolbar)
     * AppCompatActivity.setSupportActionBar}
     * for more info on how to use a Toolbar as your Activity's ActionBar.</p>
     */
    public static class LayoutParams extends Toolbar.LayoutParams {
        static final int CUSTOM = 0;
        static final int SYSTEM = 1;
        static final int EXPANDED = 2;

        int mViewType = CUSTOM;

        public LayoutParams(@NonNull Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.gravity = Gravity.CENTER_VERTICAL | GravityCompat.START;
        }

        public LayoutParams(int width, int height, int gravity) {
            this(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(int gravity) {
            this(WRAP_CONTENT, MATCH_PARENT, gravity);
        }

        public LayoutParams(MyToolbar.LayoutParams source) {
            super(source);
            mViewType = source.mViewType;
        }

        public LayoutParams(ActionBar.LayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
            // ActionBar.LayoutParams doesn't have a MarginLayoutParams constructor.
            // Fake it here and copy over the relevant data.
            copyMarginsFromCompat(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        void copyMarginsFromCompat(ViewGroup.MarginLayoutParams source) {
            this.leftMargin = source.leftMargin;
            this.topMargin = source.topMargin;
            this.rightMargin = source.rightMargin;
            this.bottomMargin = source.bottomMargin;
        }
    }

    public static class SavedState extends AbsSavedState {
        int expandedMenuItemId;
        boolean isOverflowOpen;

        public SavedState(Parcel source) {
            this(source, null);
        }

        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            expandedMenuItemId = source.readInt();
            isOverflowOpen = source.readInt() != 0;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(expandedMenuItemId);
            out.writeInt(isOverflowOpen ? 1 : 0);
        }

        public static final Creator<Toolbar.SavedState> CREATOR = new ClassLoaderCreator<Toolbar.SavedState>() {
            @Override
            public Toolbar.SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new Toolbar.SavedState(in, loader);
            }

            @Override
            public Toolbar.SavedState createFromParcel(Parcel in) {
                return new Toolbar.SavedState(in, null);
            }

            @Override
            public Toolbar.SavedState[] newArray(int size) {
                return new Toolbar.SavedState[size];
            }
        };
    }

//    private class ExpandedActionViewMenuPresenter implements MenuPresenter {
//        MenuBuilder mMenu;
//        MenuItemImpl mCurrentExpandedItem;
//
//        ExpandedActionViewMenuPresenter() {
//        }
//
//        @Override
//        public void initForMenu(Context context, MenuBuilder menu) {
//            // Clear the expanded action view when menus change.
//            if (mMenu != null && mCurrentExpandedItem != null) {
//                mMenu.collapseItemActionView(mCurrentExpandedItem);
//            }
//            mMenu = menu;
//        }
//
//        @Override
//        public MenuView getMenuView(ViewGroup root) {
//            return null;
//        }
//
//        @Override
//        public void updateMenuView(boolean cleared) {
//            // Make sure the expanded item we have is still there.
//            if (mCurrentExpandedItem != null) {
//                boolean found = false;
//
//                if (mMenu != null) {
//                    final int count = mMenu.size();
//                    for (int i = 0; i < count; i++) {
//                        final MenuItem item = mMenu.getItem(i);
//                        if (item == mCurrentExpandedItem) {
//                            found = true;
//                            break;
//                        }
//                    }
//                }
//
//                if (!found) {
//                    // The item we had expanded disappeared. Collapse.
//                    collapseItemActionView(mMenu, mCurrentExpandedItem);
//                }
//            }
//        }
//
//        @Override
//        public void setCallback(Callback cb) {
//        }
//
//        @Override
//        public boolean onSubMenuSelected(SubMenuBuilder subMenu) {
//            return false;
//        }
//
//        @Override
//        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
//        }
//
//        @Override
//        public boolean flagActionItems() {
//            return false;
//        }
//
//        @Override
//        public boolean expandItemActionView(MenuBuilder menu, MenuItemImpl item) {
//            ensureCollapseButtonView();
//            ViewParent collapseButtonParent = mCollapseButtonView.getParent();
//            if (collapseButtonParent != MyToolbar.this) {
//                if (collapseButtonParent instanceof ViewGroup) {
//                    ((ViewGroup) collapseButtonParent).removeView(mCollapseButtonView);
//                }
//                addView(mCollapseButtonView);
//            }
//            mExpandedActionView = item.getActionView();
//            mCurrentExpandedItem = item;
//            ViewParent expandedActionParent = mExpandedActionView.getParent();
//            if (expandedActionParent != MyToolbar.this) {
//                if (expandedActionParent instanceof ViewGroup) {
//                    ((ViewGroup) expandedActionParent).removeView(mExpandedActionView);
//                }
//                final Toolbar.LayoutParams lp = generateDefaultLayoutParams();
//                lp.gravity = GravityCompat.START | (mButtonGravity & Gravity.VERTICAL_GRAVITY_MASK);
//                lp.mViewType = Toolbar.LayoutParams.EXPANDED;
//                mExpandedActionView.setLayoutParams(lp);
//                addView(mExpandedActionView);
//            }
//
//            removeChildrenForExpandedActionView();
//            requestLayout();
//            item.setActionViewExpanded(true);
//
//            if (mExpandedActionView instanceof CollapsibleActionView) {
//                ((CollapsibleActionView) mExpandedActionView).onActionViewExpanded();
//            }
//
//            return true;
//        }
//
//        @Override
//        public boolean collapseItemActionView(MenuBuilder menu, MenuItemImpl item) {
//            // Do this before detaching the actionview from the hierarchy, in case
//            // it needs to dismiss the soft keyboard, etc.
//            if (mExpandedActionView instanceof CollapsibleActionView) {
//                ((CollapsibleActionView) mExpandedActionView).onActionViewCollapsed();
//            }
//
//            removeView(mExpandedActionView);
//            removeView(mCollapseButtonView);
//            mExpandedActionView = null;
//
//            addChildrenForExpandedActionView();
//            mCurrentExpandedItem = null;
//            requestLayout();
//            item.setActionViewExpanded(false);
//
//            return true;
//        }
//
//        @Override
//        public int getId() {
//            return 0;
//        }
//
//        @Override
//        public Parcelable onSaveInstanceState() {
//            return null;
//        }
//
//        @Override
//        public void onRestoreInstanceState(Parcelable state) {
//        }
//    }

}
