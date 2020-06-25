package com.bifan.txtreaderlib.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.bifan.txtreaderlib.R;



public class CircleView extends View {
    private Paint mPaint;
    private float Radius = 0.0F;
    private int CoverColor = Color.parseColor("#66ffffff");

    public CircleView(Context context) {
        super(context);
        this.init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
        this.init();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(attrs);
        this.init();
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CircleView);
        Radius = ta.getDimension(R.styleable.CircleView_CircleRadius, Radius);
        CoverColor = ta.getColor(R.styleable.CircleView_BgColor, CoverColor);
        ta.recycle();
    }

    private void init() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(this.CoverColor);

    }

    public void setCoverColor(int CoverColor) {
        this.CoverColor = CoverColor;
        this.mPaint.setColor(CoverColor);
        this.postInvalidate();
    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int vWidth = this.getWidth();
        int vHeight = this.getHeight();
        int radius = Math.max(vHeight, vWidth) / 2;
        if (Radius > 0) {
            radius = (int) Radius;
        }
        float Cx = (float) (vWidth / 2);
        float Cy = (float) (vHeight / 2);
        this.mPaint.setColor(Color.WHITE);
        canvas.drawCircle(Cx, Cy, (float) radius, this.mPaint);
        this.mPaint.setColor(CoverColor);
        canvas.drawCircle(Cx, Cy, (float) radius-3, this.mPaint);
    }
}