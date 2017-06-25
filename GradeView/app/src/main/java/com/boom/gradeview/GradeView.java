package com.boom.gradeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Boom on 2017/6/25.
 */

public class GradeView extends View {
    private  Bitmap bigger_grade_fcous;
    private Bitmap bigger_grade_normal;
    private int mTotal=5;
    private int width,height;
    private int mSingleBitmapWidth=0;
    private int currentGrade=0;

    /**
     * 设置 this 表示调用下面的  避免重复写代码
     * @param context
     */
    public GradeView(Context context) {
        this(context, null);
    }

    public GradeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        //获取两张图片的B
        bigger_grade_normal = BitmapFactory.decodeResource(getResources(), R.drawable.bigger_grade_normal);
        bigger_grade_fcous = BitmapFactory.decodeResource(getResources(), R.drawable.bigger_grade_fcous);
        mSingleBitmapWidth = bigger_grade_fcous.getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //根据bitmap计算宽高  得到宽度不要忘了还要涉及padding
        width =  mSingleBitmapWidth *mTotal+getPaddingLeft()+getPaddingLeft();
        height = bigger_grade_fcous.getHeight()+getPaddingTop()+getPaddingBottom();

        //1.获取测量模式  wrap_content  100dp  match_parent
        //测量模式  mode  AT_MOST 表示 wrap_content  EXACTLY 表示设置固定值或者是match_parent
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode==MeasureSpec.EXACTLY){
            int userWidth = MeasureSpec.getSize(widthMeasureSpec);
            if (userWidth<width){
                throw new IllegalArgumentException("给的宽度不够！");
            }
            width = userWidth;
        }

        //2.设置宽高
        setMeasuredDimension(width,height);
    }

    /**
     * 用来绘制界面
     * TextView Paint
     * bitmap你要画的图片 left 距离左边的距离  top距离顶部的距离  paint 画笔
     * drawBitmap(@NonNull Bitmap bitmap, float left, float top, @Nullable Paint paint)
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawBitmap(bigger_grade_normal,0,0,null);
        for (int i=0;i<mTotal;i++){
            if (i<currentGrade){
                canvas.drawBitmap(bigger_grade_fcous,i*mSingleBitmapWidth,0,null);
            }
            else{
                canvas.drawBitmap(bigger_grade_normal,i*mSingleBitmapWidth,0,null);
            }
        }
    }

    /**
     * 处理手指滑动的方法
     * 思路：
     *  1.获取手指触摸的位置
     *  2.根据触摸位置计算到底该打几分
     *  3.计算分数之后需要重新绘制界面  通过 invalidate()
     *  currentGrade 当前分数
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            //手指按下和触摸都是一样的执行效果
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                if(moveX<=0){
                    currentGrade = -1 ;
                }else {
                    currentGrade = moveX/mSingleBitmapWidth+1;
                }
                Log.e("TAG", "分数"+ currentGrade );
                invalidate();   //这个方法一调用会重新调用onDraw()方法
                break;
        }
        //如果这个地方返回true 代表会不断重新执行
        return true;
    }
    /**
     * 获取分数
     */
    public int getScore(){
        return currentGrade;
    }
}
