package com.longshihan.require.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Administrator
 * @time 2016/7/29 13:56
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class BezierView extends View{
    private Paint mPaint;
    private Path mPath;
    private Point startPoint;
    private Point endPoint;
    // 辅助点
    private Point assistPoint;
    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPath = new Path();
        startPoint = new Point(300, 600);
        endPoint = new Point(900, 600);
        assistPoint = new Point(600, 900);
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 防抖动
        mPaint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLACK);
        // 笔宽
        mPaint.setStrokeWidth((float) 10.0);
        // 空心
        mPaint.setStyle(Paint.Style.STROKE);
        // 重置路径
        mPath.reset();
        // 起点,moveTo 不会进行绘制，只用于移动移动画笔。
        mPath.moveTo(startPoint.x, startPoint.y);
        // 重要的就是这句,quadTo 用于绘制圆滑曲线，即贝塞尔曲线。
        //cubicTo 同样是用来实现贝塞尔曲线的。
        //mPath.cubicTo(x1, y1, x2, y2, x3, y3) (x1,y1) 为控制点，(x2,y2)为控制点，(x3,y3) 为结束点。
        mPath.quadTo(assistPoint.x, assistPoint.y, endPoint.x, endPoint.y);
        // 画路径
        canvas.drawPath(mPath, mPaint);
        // 画辅助点
        canvas.drawPoint(assistPoint.x, assistPoint.y, mPaint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                assistPoint.x = (int) event.getX();
                assistPoint.y = (int) event.getY();
                Log.i("1", "assistPoint.x = " + assistPoint.x);
                Log.i("2", "assistPoint.Y = " + assistPoint.y);
                invalidate();
                break;
        }
        return true;
    }

}
