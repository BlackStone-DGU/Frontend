package com.example.blackstone.health;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.List;

public class OverlayView extends View {

    private Pose latestPose = null;
    private final Paint pointPaint, textPaint;
    private int sourceImageWidth = 1;
    private int sourceImageHeight = 1;
    private boolean isFrontFacing = false;
    private final Matrix transformationMatrix = new Matrix();

    private double angle = -1f;
    private int count = 0;
    private int statusCode = 0;
    private String status[] = {"DOWN", "UP"};
    String exName = "squat";
    double upThreshold = 160f;
    double downThreshold = 70f;
    String angleName[] = {"RightKnee", "LeftKnee"};

    // ▶リスナー インターフェイス
    public interface OnRepetitionUpdateListener {
        void onRepetitionUpdate(int count);
    }

    private OnRepetitionUpdateListener repetitionListener;

    public void setOnRepetitionUpdateListener(OnRepetitionUpdateListener listener) {
        this.repetitionListener = listener;
    }

    public OverlayView(Context context) {
        super(context);
        pointPaint = new Paint();
        pointPaint.setColor(Color.GREEN);
        pointPaint.setStrokeWidth(20f);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(80f);
        textPaint.setShadowLayer(5.0f, 0f, 0f, Color.BLACK);
    }

    public void setCameraFacing(boolean isFront) {
        isFrontFacing = isFront;
    }

    public void updatePose(Pose pose, int imageWidth, int imageHeight) {
        latestPose = pose;
        sourceImageWidth = imageWidth;
        sourceImageHeight = imageHeight;

        angle = pose == null ? -1 : calculateAngles(pose);

        invalidate();
    }

    private double getAngle(PoseLandmark aPoint, PoseLandmark midPoint, PoseLandmark bPoint) {
        if (aPoint == null || midPoint == null || bPoint == null) return -1f;

        double res = Math.toDegrees(
                Math.atan2(bPoint.getPosition().y - midPoint.getPosition().y,
                        bPoint.getPosition().x - midPoint.getPosition().x)
                        -
                        Math.atan2(aPoint.getPosition().y - midPoint.getPosition().y,
                                aPoint.getPosition().x - midPoint.getPosition().x)
        );

        res = Math.abs(res);
        if (res > 180)
            res = 360 - res;

        return res;
    }

    private double calculateAngles(Pose pose) {
        PoseLandmark aPoint = null, midPoint = null, bPoint = null;
        for (String ang : angleName) {
            switch (ang) {
                case "RightKnee":
                    aPoint = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
                    midPoint = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
                    bPoint = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);
                    break;
                case "LeftKnee":
                    aPoint = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
                    midPoint = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
                    bPoint = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
                    break;
            }
            double res = getAngle(aPoint, midPoint, bPoint);
            if (res != -1) return res;
        }

        return -1;
    }

    private void updateTransformationMatrix() {
        float viewWidth = getWidth();
        float viewHeight = getHeight();

        float scaleFactor = Math.max(
                viewWidth / (float) sourceImageWidth,
                viewHeight / (float) sourceImageHeight
        );

        float postScaleWidthOffset = (viewWidth - (sourceImageWidth * scaleFactor)) / 2;
        float postScaleHeightOffset = (viewHeight - (sourceImageHeight * scaleFactor)) / 2;

        transformationMatrix.reset();
        transformationMatrix.postScale(scaleFactor, scaleFactor);
        transformationMatrix.postTranslate(postScaleWidthOffset, postScaleHeightOffset);

        if (isFrontFacing)
            transformationMatrix.postScale(-1f, 1f, viewWidth / 2f, viewHeight / 2f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (angle != -1) {
            if (statusCode == 0) {
                if (angle < downThreshold)
                    statusCode = 1;
            } else {
                if (angle > upThreshold) {
                    count++;
                    statusCode = 0;

                    if (repetitionListener != null) {
                        repetitionListener.onRepetitionUpdate(count);

                        // FragmentResult 전달
                        Bundle result = new Bundle();
                        result.putInt("repetitionCount", count);

                        if (getContext() instanceof androidx.fragment.app.FragmentActivity) {
                            androidx.fragment.app.FragmentActivity activity = (androidx.fragment.app.FragmentActivity) getContext();
                            activity.getSupportFragmentManager()
                                    .setFragmentResult("repetitionResult", result);
                        }
                    }
                }
            }
        }

        if (latestPose == null) return;

        updateTransformationMatrix();

        List<PoseLandmark> allPoseLandmarks = latestPose.getAllPoseLandmarks();

        for (PoseLandmark landmark : allPoseLandmarks) {
            if (landmark.getInFrameLikelihood() > 0.5f) {
                float point[] = new float[]{
                        landmark.getPosition().x,
                        landmark.getPosition().y
                };
                transformationMatrix.mapPoints(point);
                canvas.drawPoint(point[0], point[1], pointPaint);
            }
        }
    }
}
