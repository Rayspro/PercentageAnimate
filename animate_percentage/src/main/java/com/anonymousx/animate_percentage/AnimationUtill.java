/*
 *    * Created by Ravi Pratap Singh(rayspro143@gmail.com) on 1/8/19 4:06 PM
 *   * Copyright (c) 2019 . All right reserved.
 *   * Last modified 26/7/19 2:43 PM
 */

package com.anonymousx.animate_percentage;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;
import java.text.DecimalFormat;

public class AnimationUtill extends View {

    private static final int ARC_START_ANGLE = 270;
    public float THICKNESS_SCALE = 0.04f;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private RectF mCircleOuterBounds;
    private RectF mCircleInnerBounds;
    private Boolean isPercentage=true;
    private Paint mCirclePaint;
    private Paint circle;
    private Paint mEraserPaint;
    private float outof=100;
    private float mCircleSweepAngle;
    private ValueAnimator mTimerAnimator;
    private Paint textPaint;
    private float gain;
    private String concat="";

    public AnimationUtill(Context context) {
        this(context, null);
    }
    public AnimationUtill(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public AnimationUtill(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        Typeface typeface = Typeface.create(Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_bold.ttf"), Typeface.BOLD);
        int circleColor = Color.RED;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Animate);
            if(ta != null) {
                circleColor = ta.getColor(R.styleable.Animate_circleColor, circleColor);
                ta.recycle();
            }
        }

        textPaint=new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setTypeface(typeface);
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(circleColor);
        circle = new Paint();
        circle.setAntiAlias(true);
        circle.setColor(getResources().getColor(R.color.transparent_white));
        mEraserPaint = new Paint();
        mEraserPaint.setAntiAlias(true);
        mEraserPaint.setColor(Color.TRANSPARENT);
        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mBitmap.eraseColor(Color.TRANSPARENT);
            mCanvas = new Canvas(mBitmap);
        }
        super.onSizeChanged(w, h, oldw, oldh);
        updateBounds();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        float relation = (float)(Math.sqrt(canvas.getWidth() * canvas.getHeight()))/300;
        textPaint.setTextSize((50*relation));
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        if (mCircleSweepAngle > 0f) {
            mCanvas.drawArc(mCircleOuterBounds, ARC_START_ANGLE, 360, true, circle);
            mCanvas.drawArc(mCircleOuterBounds, ARC_START_ANGLE, mCircleSweepAngle, true, mCirclePaint);
            mCanvas.drawOval(mCircleInnerBounds, mEraserPaint);
            String result="0";
            if(isPercentage){
                if(concat.equals("")){
                    result = decimalFormat.format(mCircleSweepAngle/3.6);
                }else{
                    result = decimalFormat.format(mCircleSweepAngle/3.6)+concat;
                }
            }else{
                if(concat.equals("")){
                    result = ((int)((mCircleSweepAngle*outof)/360))+concat;
                }else{
                    result = ((int)((mCircleSweepAngle*outof)/360))+concat+(int)outof;
                }
            }
            mCanvas.drawText(result,(canvas.getWidth()/2),(canvas.getHeight()/2)+10,textPaint);
        }else {
            mCanvas.drawArc(mCircleOuterBounds, ARC_START_ANGLE, 360, true, circle);
            mCanvas.drawArc(mCircleOuterBounds, ARC_START_ANGLE, mCircleSweepAngle, true, mCirclePaint);
            mCanvas.drawOval(mCircleInnerBounds, mEraserPaint);
            textPaint.setTextAlign(Paint.Align.CENTER);
            String result="0";
            if(isPercentage){
                result = decimalFormat.format(mCircleSweepAngle)+concat;
            }else{
                if(concat.equals("")){
                    result = decimalFormat.format(mCircleSweepAngle);
                }else{
                    result = decimalFormat.format(mCircleSweepAngle)+concat+(int)outof;
                }
            }
            mCanvas.drawText(result,(canvas.getWidth()/2),(canvas.getHeight()/2)+10,textPaint);
        }
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    public void start(float get,float outOf,Boolean isPercent,String concat) {
        stop();
        gain=get;
        this.concat=concat;
        this.isPercentage=isPercent;
        float out_of_final=360/outOf;
        this.outof=outOf;

        if(isPercent){

            if(get<=0){

                drawProgress(gain);

            }else{
                mTimerAnimator = ValueAnimator.ofFloat(0.0f,gain*out_of_final);
                mTimerAnimator.setDuration(3000);
                mTimerAnimator.setInterpolator(new BounceInterpolator());
                mTimerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        drawProgress((float)animation.getAnimatedValue());
                    }
                });
                mTimerAnimator.start();
            }
        }else{

            if(get<=0){

                drawProgress(get);

            }else{
                mTimerAnimator = ValueAnimator.ofFloat(0.0f,gain*out_of_final);
                mTimerAnimator.setDuration(3000);
                mTimerAnimator.setInterpolator(new BounceInterpolator());
                mTimerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        drawProgress((float)animation.getAnimatedValue());
                    }
                });
                mTimerAnimator.start();
            }
        }
    }

    public void stop() {
        if (mTimerAnimator != null && mTimerAnimator.isRunning()) {
            mTimerAnimator.cancel();
            mTimerAnimator = null;
            drawProgress(0);
        }
    }

    private void drawProgress(float progress) {
        mCircleSweepAngle = progress;
        invalidate();
    }

    private void updateBounds() {
        final float thickness = getWidth() * THICKNESS_SCALE;
        mCircleOuterBounds = new RectF(0, 0, getWidth(), getHeight());
        mCircleInnerBounds = new RectF(
                mCircleOuterBounds.left + thickness,
                mCircleOuterBounds.top + thickness,
                mCircleOuterBounds.right - thickness,
                mCircleOuterBounds.bottom - thickness);
        invalidate();
    }
}