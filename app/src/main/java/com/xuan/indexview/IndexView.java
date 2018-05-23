package com.xuan.indexview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * com.xuan.indexview
 *
 * @author by xuan on 2018/5/20
 * @version [版本号, 2018/5/20]
 * @update by xuan on 2018/5/20
 * @descript
 */
public class IndexView extends View {

    private int defaultColor= ContextCompat.getColor(getContext(),R.color.colorAccent);
    private int focusColor=ContextCompat.getColor(getContext(),R.color.colorPrimary);
    private int fontSize= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,15,getResources().getDisplayMetrics());

    public static String[] letterList = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#" };
    private Paint letterPaint;
    private Paint focusPaint;

    private String indexLetter;//当前角标字母
    private int beforeIndex;

    public IndexView(Context context) {
        this(context,null);
    }

    public IndexView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context,attrs);

        letterPaint=new Paint();
        letterPaint.setAntiAlias(true);
        letterPaint.setTextSize(fontSize);
        letterPaint.setColor(defaultColor);

        focusPaint=new Paint();
        focusPaint.setAntiAlias(true);
        focusPaint.setTextSize(fontSize);
        focusPaint.setColor(focusColor);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.IndexView);

        defaultColor=typedArray.getColor(R.styleable.IndexView_defaultColor,defaultColor);
        focusColor=typedArray.getColor(R.styleable.IndexView_focusColor,focusColor);
        fontSize=typedArray.getDimensionPixelSize(R.styleable.IndexView_fontSize,fontSize);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //计算宽度 字母宽度+padding
        float textWidth=letterPaint.measureText("A");
        int width= (int) (getPaddingLeft()+getPaddingRight()+textWidth);

        //高度match_parent 直接获取
        int height=MeasureSpec.getSize(heightMeasureSpec)+getPaddingTop()+getPaddingBottom();

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // ctrl+alt+f8

        Log.i(IndexView.class.getSimpleName(),"getPaddingTop()   " +getPaddingTop());

        for(int i=0;i<letterList.length;i++){

//            Paint.FontMetrics fontMetrics = letterPaint.getFontMetrics();
//            //计算宽度 字体的长度有关
//            Rect bounds=new Rect();//矩形
//            //给矩形设置边界
//            letterPaint.getTextBounds(text,0,text.length(),bounds);
//
//            int dx=getWidth()/2-bounds.width()/2;
//            int dy= (int) ((fontMetrics.bottom-fontMetrics.top)/2-fontMetrics.bottom);
//            int baseLine=getHeight()/2+dy;


            // x 直接放最中间 x = 宽度/2-文字宽度/2
            int textWidth= (int) letterPaint.measureText(letterList[i]);
            int x=getWidth()/2-textWidth/2;

            Paint.FontMetrics fontMetrics = letterPaint.getFontMetrics();
            //计算宽度 字体的长度有关
            Rect bounds=new Rect();//矩形
            //给矩形设置边界
            letterPaint.getTextBounds(letterList[i],0,letterList[i].length(),bounds);
            int dy= (int) ((fontMetrics.bottom-fontMetrics.top)/2-fontMetrics.bottom);
            /*
              1.获取单个字母的高度 总高度/数量
              2.获取单个字母的中心位置 自己的中心+上一个字母的高度+字母间距
              3.获取基线
             */
            int letterHeight=(getHeight()-getPaddingTop()-getPaddingBottom())/letterList.length;
            int center=letterHeight/2+letterHeight*i+10;
            int y=center+dy;

            if(letterList[i].equals(indexLetter)){
                canvas.drawText(letterList[i],x,y,focusPaint);

                if(inter!=null){
                    inter.selectIndexText(indexLetter);
                }

            }else{
                canvas.drawText(letterList[i],x,y,letterPaint);
            }


        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                selectIndex(event);
                break;
            case MotionEvent.ACTION_MOVE:
                selectIndex(event);
                break;
            case MotionEvent.ACTION_UP:
                if(inter!=null){
                    inter.cancelSelect();
                }
                break;
            default:
                break;
        }

        return true;
    }

    private void selectIndex(MotionEvent event){
        float moveY=event.getY();
        // 当前的Y轴坐标/每一个字母的高度 就是当前字母的位置 index
        int letterHeight=(getHeight()-getPaddingTop()-getPaddingBottom())/letterList.length;
        int index= (int) (moveY/letterHeight);
        // moveY有可能是负数 判断需要角标
        if(index>=0&&index<letterList.length && index!=beforeIndex){
            indexLetter = letterList[index];
            invalidate();
        }

        beforeIndex=index;
    }

    public void addIndexViewInter(IndexViewCallBackInter inter){
        this.inter=inter;
    }

    private IndexViewCallBackInter inter;
    public interface IndexViewCallBackInter{
        void selectIndexText(String text);
        void cancelSelect();
    }

    /*
        View的绘制流程
            


    */
}
