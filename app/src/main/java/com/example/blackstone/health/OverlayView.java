package com.example.blackstone.health;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.List;

public class OverlayView extends View {

    private Pose latestPose = null;
    private final Paint pointPaint;
    private int sourceImageWidth = 1;
    private int sourceImageHeight = 1;
    private boolean isFrontFacing = false;
    private final Matrix transformationMatrix = new Matrix();

    public OverlayView(Context context){
        super(context);
        pointPaint = new Paint();
        pointPaint.setColor(Color.GREEN);
        pointPaint.setStrokeWidth(20f);
    }

    public void setCameraFacing(boolean isFront) {
        isFrontFacing = isFront;
    }

    public void updatePose(Pose pose, int imageWidth, int imageHeight){
        latestPose = pose;
        sourceImageWidth = imageWidth;
        sourceImageHeight = imageHeight;

        invalidate();
    }

    private void updateTransformationMatrix(){
        float viewWidth = getWidth();
        float viewHeight = getHeight();

        float scaleFactor = Math.max(
                viewWidth/(float) sourceImageWidth,
                viewHeight/(float) sourceImageHeight
        );

        float postScaleWidthOffset = (viewWidth-(sourceImageWidth*scaleFactor))/2;
        float postScaleHeightOffset = (viewHeight-(sourceImageHeight*scaleFactor))/2;

        transformationMatrix.reset();
        transformationMatrix.postScale(scaleFactor,scaleFactor);
        transformationMatrix.postTranslate(postScaleWidthOffset, postScaleHeightOffset);

        if(isFrontFacing)
            transformationMatrix.postScale(-1f,1f,viewWidth/2f,viewHeight/2f);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if(latestPose==null) return;

        updateTransformationMatrix();

        List<PoseLandmark> allPoseLandmarks = latestPose.getAllPoseLandmarks();

        for(PoseLandmark landmark : allPoseLandmarks){
            if(landmark.getInFrameLikelihood()>0.5f){
                float point[] = new float[]{
                        landmark.getPosition().x,
                        landmark.getPosition().y
                };
                transformationMatrix.mapPoints(point);
                canvas.drawPoint(point[0],point[1],pointPaint);
            }
        }
    }


}
