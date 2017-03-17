package myproject.paintjob;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by User on 3/13/2017.
 */

public class DrawView extends View {
    private static final String TAG = "DrawView";
    private float mX;
    private float mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<DrawDetails> lists;
    private float brushSize = 4f;
    private int color = R.color.colorOne;
    private ArrayList<DrawDetails> cacheDetails;
    private int undoCount = 0;

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
        mPath = new Path();
        lists = new ArrayList<>();
        cacheDetails = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < lists.size(); i++) {
            DrawDetails detail = lists.get(i);
            mPath = detail.getPath();
            mPaint = detail.getPaint();
            canvas.drawPath(mPath, mPaint);
        }

    }

    private void startTouch(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void moveEvent(float x, float y) {
        mPath.quadTo(mX, mY, x, y);
        mX = x;
        mY = y;
    }

    private void endTouch(float x, float y) {
        //mPath.lineTo(mX, mY);
        mPath.setLastPoint(x, y);
    }

    public void clearCanvas() {
        for (int i = 0; i < lists.size(); i++) {
            DrawDetails detail = lists.get(i);
            mPath = detail.getPath();
            mPath.reset();
        }
        invalidate();
    }

    //undo drawing
    public boolean undo() {
        if (lists.size() > 0) {
            cacheDetails.add(lists.get(lists.size() - 1));
            Log.i(TAG, "undo:size "+lists.size());
            lists.remove(lists.size() - 1);
            invalidate();
            return true;
        }
        return false;
    }

    //redo drawing
    public boolean redo() {
        if (cacheDetails != null) {
            if (cacheDetails.size() > 0) {
                lists.add(cacheDetails.get(0));
                cacheDetails.remove(0);
                ArrayList<DrawDetails> dummy = new ArrayList<>();
                for (DrawDetails details : cacheDetails) {
                    dummy.add(details);
                }
                cacheDetails.clear();
                cacheDetails.addAll(dummy);
                invalidate();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float dX = event.getX();
        float dY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                addPath(brushSize, color);
                startTouch(dX, dY);
                break;
            case MotionEvent.ACTION_MOVE:
                moveEvent(dX, dY);
                break;
            case MotionEvent.ACTION_UP:
                endTouch(dX, dY);
                break;
        }
        invalidate();
        return true;
    }

    public void setSize(float size) {
        this.brushSize = size;
    }

    public void setPaintColor(int color) {
        this.color = color;
    }

    //new Path and paint
    private void addPath(float size, int color) {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(size);
        mPaint.setColor(getResources().getColor(color));
        lists.add(new DrawDetails(mPaint, mPath));

    }
}
