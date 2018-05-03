package com.csscaps.common.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import java.math.BigDecimal;

/**
 *
 * @class  ZoomImageView
 * @description  { 自定义控件 }
 * @time 2015年4月1日  上午12:31:37
 *
 */
public class ZoomImageView extends android.support.v7.widget.AppCompatImageView implements OnGlobalLayoutListener, OnScaleGestureListener,
        OnTouchListener{
    private boolean isFirstLoad;//只加载一次
    private float mMinScale;//初始化缩放比例
    private float mMidScale;//点击放大的比例
    private float mMaxScale;//最大缩放比例
    //多点触控放大、缩小
    private Matrix mScaleMatrix;//矩阵主要用于控制图片的缩放、移动
    private ScaleGestureDetector mScaleGestureDetector;//多点触控的监听类
    //图片的自由移动
    private int mLastPointer;//上一次手势中手指的数量
    private float mLastX;//手势变动时，新的中心点
    private float mLastY;
    private int mTouchSlop;//判断是否移动的标准
    private boolean isCanDrag;//是否可以移动
    private boolean isCheckLeftAndRight;//是否需要检查左右
    private boolean isCheckTopAndBottom;//是否需要检查上下
    //双击放大、缩小
    private GestureDetector mGestureDetector;
    private boolean isAlreadyAutoScale;
    /**
     *
     * @class ZoomImageView
     *
     * @author  { 张建辉 }
     *
     * @description  重写父类的构造方法
     *
     * @time 2015年4月1日  上午12:38:23
     *
     * Ps：
     *   在此处重写父类构造方法的时候，有一个技巧：在一个参数的构造函数里调用两个参数的构造函数，在两个参数的构造函数里调用三个参数的构造函数，
     * 而且把初始化数据的方法都写在三个参数的构造方法里，那么无论哪个构造函数都会去初始化数据。
     *
     * BigDecimal：精确计算图片
     */
    public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //initData
        super.setScaleType(ScaleType.MATRIX);//这条属性一定要设置否则不会有放大效果
        mScaleMatrix = new Matrix();
        setOnTouchListener(this);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //如果已经在放大或者缩小了，就不可以继续操作
                if(isAlreadyAutoScale){
                    return true;
                }

                float x = e.getX();
                float y = e.getY();

                if(getCurrentScale() < mMidScale){
//					mScaleMatrix.postScale(mMidScale/getCurrentScale(), mMidScale/getCurrentScale(), x, y);
//					setImageMatrix(mScaleMatrix);
                    postDelayed(new AutoScaleRunnable(mMidScale, x, y), 10);
                    isAlreadyAutoScale = true;
                }else {
//					mScaleMatrix.postScale(mMinScale/getCurrentScale(), mMinScale/getCurrentScale(), x, y);
//					setImageMatrix(mScaleMatrix);
                    postDelayed(new AutoScaleRunnable(mMinScale, x, y), 10);
                    isAlreadyAutoScale = true;
                }
                return true;
            }

			
            
        });
    }
    private class AutoScaleRunnable implements Runnable{
        //缩放的目标值
        private float mScaleTarget;
        //缩放的中心点
        private float x;
        private float y;
        private final float BIGGER = 1.07f;
        private final float SMALLER = 0.93f;

        private float tempScale ;
        public AutoScaleRunnable(float mScaleTarget, float x, float y) {
            this.mScaleTarget = mScaleTarget;
            this.x = x;
            this.y = y;

            if(getCurrentScale() < mScaleTarget){
                tempScale = BIGGER;
            }
            if(getCurrentScale() > mScaleTarget){
                tempScale = SMALLER;
            }
        }

        @Override
        public void run() {

            mScaleMatrix.postScale(tempScale, tempScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
            //如果没有到达放大或者缩小的范围，继续回调
            if((tempScale > 1.0f && getCurrentScale() < mScaleTarget) || (tempScale < 1.0f && getCurrentScale() > mScaleTarget)){
                postDelayed(this, 10);
            }else{
                float scale = mScaleTarget/getCurrentScale();
                mScaleMatrix.postScale(scale, scale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);
                isAlreadyAutoScale = false;
            }
        }
    }
    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ZoomImageView(Context context) {
        this(context,null);
    }
    /**
     *
     * @method onAttachedToWindow
     *
     * @desription { 控件加载到视图上时 }
     *
     * @time 2015年4月1日上午1:01:49
     *
     * Ps:在此处添加对布局的监听事件
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }
    /**
     *
     * @method onDetachedFromWindow
     *
     * @desription { 控件从视图上移除时 }
     *
     * @time 2015年4月1日上午1:02:17
     *
     * Ps:在此处添加对布局的监听事件
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }
    /**
     *
     * @method onGlobalLayout
     *
     * @desription { 当视图中加载布局之后 ，将图片进行缩放}
     *
     *
     * @time 2015年4月1日上午12:56:37
     *
     * Ps:此方法必须在调用，才会触发方法体内的函数
     */
    @Override
    public void onGlobalLayout() {
        if(!isFirstLoad){
            //得到控件的宽、高
            int width = getWidth();
            int height = getHeight();
            //得到图片及宽、高
            Drawable drawable = getDrawable();
            int intrinsicWidth = 0;
            int intrinsicHeight = 0;
            BigDecimal bigDecimalIntrinsicWidth,bigDecimalIntrinsicHeight;
            float scale = 1.0f;//图片缩放值
            if(drawable == null){
                return ;
            }else{
                intrinsicWidth = drawable.getIntrinsicWidth();//intrinsic adj.固有的，本质的
                intrinsicHeight = drawable.getIntrinsicHeight();
                
               bigDecimalIntrinsicWidth=new BigDecimal(intrinsicWidth);
               bigDecimalIntrinsicHeight=new BigDecimal(intrinsicHeight);
            }
           
            BigDecimal bigDecimalWidth=new BigDecimal(width);
            BigDecimal bigDecimalHeight=new BigDecimal(height);
            
         
            
            
            //图片宽度大于控件宽度，高度小于控件高度
            if(intrinsicWidth>width && intrinsicHeight<height){
//                scale = width*1.0f/intrinsicWidth;
                scale = bigDecimalWidth.divide(bigDecimalIntrinsicWidth,2,BigDecimal.ROUND_DOWN).floatValue();
            }
            //图片高度大于控件高度，宽度小于控件宽度
            if(intrinsicHeight>height && intrinsicWidth<width){
//                scale = height*1.0f/intrinsicHeight;
                scale = bigDecimalHeight.divide(bigDecimalIntrinsicHeight,2,BigDecimal.ROUND_DOWN).floatValue();
            }
            //图片宽度大于控件宽度，高度大于控件高度
            if(intrinsicWidth>width && intrinsicHeight>height){
//                scale = Math.min((width*1.0f/intrinsicWidth),(height*1.0f/intrinsicHeight));
                scale = Math.min(bigDecimalWidth.divide(bigDecimalIntrinsicWidth,2,BigDecimal.ROUND_DOWN).floatValue(),bigDecimalHeight.divide(bigDecimalIntrinsicHeight,2,BigDecimal.ROUND_DOWN).floatValue());
            }
            //图片宽度小于控件宽度，高度小于控件高度
            if(intrinsicWidth<width && intrinsicHeight<height){
//                scale = Math.min((width*1.0f/intrinsicWidth),(height*1.0f/intrinsicHeight));
                scale = Math.min(bigDecimalWidth.divide(bigDecimalIntrinsicWidth,2,BigDecimal.ROUND_DOWN).floatValue(),bigDecimalHeight.divide(bigDecimalIntrinsicHeight,2,BigDecimal.ROUND_DOWN).floatValue());
            }

            //图片加载进来之后的缩放比例
            mMinScale = scale;
            mMidScale = scale * 2;
            mMaxScale = scale * 4;

            //将图片移动到控件的中心
            int deltaX = width/2 - intrinsicWidth/2;
            int deltaY = height/2 - intrinsicHeight/2;

            //通过矩阵将图片移到控件中心，并进行缩放
            mScaleMatrix.postTranslate(deltaX, deltaY);
            mScaleMatrix.postScale(mMinScale, mMinScale, width/2, height/2);
            setImageMatrix(mScaleMatrix);


            isFirstLoad = true;
        }
    }
    /**
     *
     * @method onScaleBegin
     *
     * @desription { 多点触控的监听事件 }
     *
     * @time 2015年4月1日上午2:44:17
     *
     * Ps:想要实现此功能，必须要将屏幕的时间传递进来才可以，因此必须实现 onTouchListener
     */
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        float currentScale = getCurrentScale();//当前图片比例
        float scaleFactor = detector.getScaleFactor();//手势比例

        if(getDrawable() == null){//如果图片为空直接返回
            return true;
        }

        //缩放控制        scaleFactor>1.0f 放大 ；scaleFactor<1.0f 缩小。
        if((currentScale<mMaxScale && scaleFactor>1.0f)||(currentScale>mMinScale && scaleFactor<1.0f)){

            //如果手势放大的比例和当前尺寸之积大于最大放大比例，则直接将放大系数设置为MAX
            if(scaleFactor*currentScale>mMaxScale){
                scaleFactor = mMaxScale/currentScale;
            }
            //如果手势缩小的比例和当前尺寸之积小于最小放大比例，则直接将缩小系数设置为MIN
            if(scaleFactor*currentScale<mMinScale){
                scaleFactor = mMinScale/currentScale;
            }

//			//通过矩阵将图片移到控件中心，并进行缩放 (控件的中心点)
//			mScaleMatrix.postScale(scaleFactor, scaleFactor, getWidth()/2, getHeight()/2);

            //通过矩阵将图片移到控件中心，并进行缩放 (手势的中心点)
            mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            //缩放的时候检查是否有白边，是否在中间
            checkBorderAndCenterWhenScale();

            setImageMatrix(mScaleMatrix);
        }


        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }
    /**
     *
     * @method onTouch
     *
     * @desription { 主要用于将屏幕的监听事件传递给  ScaleGestureDetector}
     *
     * @time 2015年4月1日上午2:46:47
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //将双击时间传递给 GestureDetector 监听器
        if(mGestureDetector.onTouchEvent(event)){
            return true;
        }

        mScaleGestureDetector.onTouchEvent(event);

        //中心点坐标
        float x = 0;
        float y = 0;

        int pointCount = event.getPointerCount();
        for(int i=0;i<pointCount;i++){
            x += event.getX();
            y += event.getY();
        }

        x /= pointCount;
        y /= pointCount;

        //手势变化时，记录下中心点坐标，同时重新判断是否可以移动
        if(mLastPointer != pointCount){
            isCanDrag = false;//手势变化的时候，不可以移动，必须重新判断
            mLastX = x;
            mLastY = y;
        }

        mLastPointer = pointCount;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /**
                 * 去除掉ZoomImageView和ViewPager的冲突
                 *
                 * Ps:首先获取到控件的宽高，如果控件的宽高大于屏幕的宽高，那么就屏蔽掉ViewPager的滑动事件
                 *
                 * Intercept vt.拦截，截断
                 */
                if(getParent() instanceof ViewPager){
                    RectF rectF = getMatrixRectF();
                    if((rectF.width() > getWidth()) || (rectF.height() > getHeight())){
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                /**
                 * 去除掉ZoomImageView和ViewPager的冲突
                 */
                if(getParent() instanceof ViewPager){
                    RectF rectF = getMatrixRectF();
                    if((rectF.width() > getWidth()) || (rectF.height() > getHeight())){
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                //移动的距离
                float moveDeltaX = x - mLastX;
                float moveDeltaY = y - mLastY;
                //手势变化之后，重新判断是否可以移动
                if(!isCanDrag){
                    isCanDrag = isMoveAction( moveDeltaX, moveDeltaY);
                }
                //如果可以移动，处理的函数就在这里
                if(isCanDrag){
                    //得到移动后的图片
                    RectF rectF = getMatrixRectF();
                    if(getDrawable() != null){
                        isCheckLeftAndRight = isCheckTopAndBottom = true;
                        //如果图片的宽度小于控件的宽度，不移动
                        if(rectF.width()<getWidth()){
                            isCheckLeftAndRight = false;
                            moveDeltaX = 0;
                        }
                        //如果图片的高度小于控件的高度，不移动
                        if(rectF.height()<getHeight()){
                            isCheckTopAndBottom = false;
                            moveDeltaY = 0;
                        }
                        mScaleMatrix.postTranslate(moveDeltaX, moveDeltaY);
                        checkBorderWhenTranslate();
                        setImageMatrix(mScaleMatrix);
                    }
                }

                mLastX = x;
                mLastY = y;

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //手势收起，触点置0
                mLastPointer = 0;
                break;

            default:
                break;
        }

        return true;
    }
    /**
     *
     * @method isMoveAction
     *
     * @desription { 判断是否到达了移动的标准 }
     *
     * @time 2015年4月2日上午1:40:16
     */
    private boolean isMoveAction (float moveDeltaX,float moveDeltaY){
        return Math.sqrt(moveDeltaX*moveDeltaX+moveDeltaY*moveDeltaY) > mTouchSlop;
    }
    /**
     *
     * @method checkBorderWhenTranslate
     *
     * @desription { 当图片进行移动时，检查边界 }
     *
     *
     * @time 2015年4月2日上午1:40:47
     */
    private void checkBorderWhenTranslate(){
        //首先得到缩放之后的图片的详细信息
        RectF rectF = getMatrixRectF();

        float deltaX = 0f;
        float deltaY = 0f;

        int width = getWidth();
        int height = getHeight();

        if(rectF.top > 0 && isCheckTopAndBottom){
            deltaY = - rectF.top;
        }
        if(rectF.bottom < height && isCheckTopAndBottom){
            deltaY = height - rectF.bottom;
        }

        if(rectF.left > 0 && isCheckLeftAndRight){
            deltaX = - rectF.left;
        }
        if(rectF.right < width && isCheckLeftAndRight){
            deltaX = width - rectF.right;
        }

        mScaleMatrix.postTranslate(deltaX, deltaY);
    }
    /**
     *
     * @method getCurrentScale
     *
     * @desription { 获取当前的缩放比例 }
     *
     *
     * @time 2015年4月1日上午2:51:40
     */
    private float getCurrentScale(){
        float []values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    /**
     *
     * @method checkBorderAndCenterWhenScale
     *
     * @desription { 当图片进行缩放的时候，检查边界、中心 }
     *
     * @time 2015年4月1日下午9:01:55
     *
     * Ps:去除白边，把图片放到控件的中心
     */
    private void checkBorderAndCenterWhenScale() {
        //首先得到缩放之后的图片的详细信息
        RectF rectF = getMatrixRectF();
        //移动的距离
        float deltaX = 0f;
        float deltaY = 0f;

        int width = getWidth();
        int height = getHeight();

        /**
         * 如果图片宽度大于控件宽度
         */
        if(rectF.width()>= width){
            if(rectF.left>0){
                deltaX = -rectF.left;
            }
            if(rectF.right<width){
                deltaX = width-rectF.right;
            }
        }
        /**
         * 如果图片高度大于控件高度
         */
        if(rectF.height() >= height){
            if(rectF.top>0){
                deltaY = -rectF.top;
            }
            if(rectF.bottom<height){
                deltaY = height-rectF.bottom;
            }
        }
        /**
         * 如果图片宽度小于控件宽度，则居中
         */
        if(rectF.width()<width){
            deltaX = width/2f - rectF.right +rectF.width()/2f;
        }
        /**
         * 如果图片高度小于控件高度，则居中
         */
        if(rectF.height()<height){
            deltaY = height/2f - rectF.bottom +rectF.height()/2f;
        }

        mScaleMatrix.postTranslate(deltaX, deltaY);
    }
    /**
     *
     * @method getMatrixRectF
     *
     * @desription { 得到图片放大或者缩小后的宽高，以及坐标（left/right/top/bottom） }
     *
     * @time 2015年4月1日下午9:10:36
     */
    private RectF getMatrixRectF(){
        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();
        Drawable drawable = getDrawable();
        if(drawable != null){
            rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //coordinate n.坐标
            matrix.mapRect(rectF);
        }
        return rectF;
    }


}
