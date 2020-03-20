package com.example.notekeeper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class ModuleStatusView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;


    private Boolean []mModuleStatus;
    private float mOutLineWidth;
    private float mShapeSize;
    private float mSpacing;
    private Rect[] mModuleRectangles;
    private int mOutLineColor;
    private Paint mPaintOutLine;
    private int mFillColor;
    private Paint mPaintFill;

    public Boolean[] getModuleStatus() {
        return mModuleStatus;
    }

    public void setModuleStatus(Boolean[] moduleStatus) {
        mModuleStatus = moduleStatus;
    }

    public ModuleStatusView(Context context) {
        super(context);
        init(null, 0);
    }

    public ModuleStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ModuleStatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ModuleStatusView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.ModuleStatusView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.ModuleStatusView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.ModuleStatusView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.ModuleStatusView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.ModuleStatusView_exampleDrawable);
            assert mExampleDrawable != null;
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        mOutLineWidth = 6f;
        mShapeSize = 144f;
        mSpacing = 30f;
        // Set up a default TextPaint object
        setupModuleRectangle();
        // Update TextPaint and text measurements from attributes
        mOutLineColor = Color.BLACK;
        mPaintOutLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOutLine.setStyle(Paint.Style.STROKE);
        mPaintOutLine.setStrokeWidth(mOutLineWidth);
        mPaintOutLine.setColor(mOutLineColor);

        mFillColor = getContext().getColor(R.color.orange);
        mPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(mFillColor);

        invalidateTextPaintAndMeasurements();
    }

    private void setupModuleRectangle() {
        mModuleRectangles = new Rect[mModuleStatus.length];
        for (int moduleIndex = 0 ; moduleIndex < mModuleRectangles.length ; moduleIndex++)
        {
            int x = (int) ( moduleIndex *(mShapeSize + mSpacing));
            int y = 0;
            mModuleRectangles[moduleIndex] = new Rect(x , y , x + (int)mShapeSize , y + (int) mShapeSize);
        }
    }

    private void invalidateTextPaintAndMeasurements() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.


        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
