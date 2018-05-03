package com.csscaps.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.List;


/**
 * Created by tl on 2016/9/26.
 * 自定义多段进度条控件
 */
public class MySeekBar extends View {
    private Paint paint;
    private Context context;
    private Canvas canvas;
    private int h, w, max;
    private List<SeekBarPart> partList;
    float ratio;
    private Point cP;
    int x = 0;


    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MySeekBar(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        w = canvas.getWidth();
        h = canvas.getHeight();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        onDrawBg(canvas);
        onDrawBar(canvas, (int) (x * ratio));
        updateProgress(x);
    }

    private void onDrawBg(Canvas canvas) {
        paint.setColor(Color.parseColor("#e9e7e7"));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(12);//设置画笔宽度
        RectF rect = new RectF(10, h / 2 - 6, w - 10, h / 2 + 6);
        canvas.drawRoundRect(rect, 6, 6, paint);
    }

    private void onDrawBar(Canvas canvas, int x) {
        paint.setColor(Color.parseColor("#3686fe"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);//设置画笔宽度
        cP = new Point(16 + x, h / 2);
        canvas.drawCircle(16 + x, h / 2, 15, paint);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(16 + x, h / 2, 14, paint);

    }


    public void setPart(List<SeekBarPart> partList) {
        this.partList = partList;
        for (int i = 0; i < partList.size(); i++) {
            max = max + partList.get(i).getPl();
            partList.get(i).setZpl(max);
        }
        ratio = (float) (w - 32) / (float) max;
        handler.sendEmptyMessage(0);
    }


    public void updateProgress(int x) {
        Rect rect = null;
        if (partList == null) return;
        int size = partList.size();
        SeekBarPart part0 = partList.get(0);
        if (part0.getPartFlag() == 0) {
            paint.setColor(Color.parseColor("#a29f9f"));
        } else {
            paint.setColor(Color.parseColor("#3686fe"));
        }
        RectF rect1 = new RectF(10, h / 2 - 6, 22, h / 2 + 6);
        canvas.drawArc(rect1, 90, 180, true, paint);
        if (size < 2) {
            paint.setColor(Color.parseColor("#3686fe"));
            rect = new Rect(16, h / 2 - 6, (int) (x * ratio + 16), h / 2 + 6);
            canvas.drawRect(rect, paint);
        } else {
            for (int i = 0; i < size; i++) {
                SeekBarPart part = partList.get(i);
                Paint mpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mpaint.setStyle(Paint.Style.FILL);
                mpaint.setStrokeWidth(12);//设置画笔宽度
                if (part.getPartFlag() == 0) {
                    mpaint.setColor(Color.parseColor("#a29f9f"));
                } else {
                    mpaint.setColor(Color.parseColor("#3686fe"));
                }

                if (x > part.getZpl()) {

                    if (i == 0) {
                        rect = new Rect(16, h / 2 - 6, (int) (x * ratio + 16), h / 2 + 6);
                    } else {
                        rect = new Rect((int) (partList.get(i - 1).getZpl() * ratio + 16), h / 2 - 6, (int) (partList.get(i).getZpl() * ratio + 16), h / 2 + 6);
                    }
                    canvas.drawRect(rect, mpaint);
                }

                if (x <= part.getZpl()) {
                    if (i == 0) {
                        rect = new Rect(16, h / 2 - 6, (int) (x * ratio + 16), h / 2 + 6);
                    } else {
                        rect = new Rect((int) (partList.get(i - 1).getZpl() * ratio + 16), h / 2 - 6, (int) (x * ratio + 16), h / 2 + 6);
                    }
                    canvas.drawRect(rect, mpaint);
                    break;
                }
            }
        }
        onDrawBar(canvas, (int) (x * ratio));
    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            x++;
            invalidate();
            if (x < max) handler.sendEmptyMessageDelayed(0, 1);

        }
    };

    boolean isCBar;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float sx = event.getX();
                float sy = event.getY();
                sx = sx - cP.x;
                sy = sy - cP.y;
                if (Math.pow(sx, 2) + Math.pow(sy, 2) < Math.pow(h / 2, 2)) {
                    handler.removeMessages(0);
                    isCBar = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isCBar) {
                    float x1 = event.getX();
                    x = (int) (x1 / ratio - 16);
                    if (x >= 0 && x < max) invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isCBar) {
                    float x1 = event.getX();
                    x = (int) (x1 / ratio - 16) - 1;
                    if (x >= 0 && x < max) handler.sendEmptyMessage(0);
                    isCBar = false;
                }
                break;
        }
        return true;
    }
}

class SeekBarPart {
    private int pl;//段长
    private int partFlag;// 1 ：蓝色  0：灰色
    private int zpl;

    public int getPl() {
        return pl;
    }

    public void setPl(int pl) {
        this.pl = pl;
    }

    public int getPartFlag() {
        return partFlag;
    }

    public void setPartFlag(int partFlag) {
        this.partFlag = partFlag;
    }

    public int getZpl() {
        return zpl;
    }

    public void setZpl(int zpl) {
        this.zpl = zpl;
    }
}
