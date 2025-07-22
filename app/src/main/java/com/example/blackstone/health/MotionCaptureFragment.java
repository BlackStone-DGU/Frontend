package com.example.blackstone.health;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.blackstone.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MotionCaptureFragment extends Fragment {

    private PreviewView viewFinder;
    private OverlayView overlayView;
    private PoseDetector poseDetector;
    private ExecutorService analysisExecutor;
    private int cam[] = {CameraSelector.LENS_FACING_BACK, CameraSelector.LENS_FACING_FRONT};
    private int camCode = 0;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if(isGranted)
                    startCamera();
                else
                    Toast.makeText(requireContext(), "denied", Toast.LENGTH_SHORT).show();
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_motion_capture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        viewFinder = view.findViewById(R.id.viewFinder);
        overlayView = new OverlayView(requireContext());
        ImageButton switchButton = view.findViewById(R.id.camera_switch_button);

        if(view instanceof ViewGroup)
            ((ViewGroup) view).addView(overlayView);

        analysisExecutor = Executors.newSingleThreadExecutor();
        PoseDetectorOptions options = new PoseDetectorOptions.Builder()
                .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                .build();
        poseDetector = PoseDetection.getClient(options);

        switchButton.setOnClickListener(v->{
            camCode = (camCode+1)%2;
            startCamera();
        });

        if(allPermissionsGranted())
            startCamera();
        else
            requestPermissionLauncher.launch((Manifest.permission.CAMERA));
    }

    private boolean allPermissionsGranted(){
        return ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA)
                ==
                PackageManager.PERMISSION_GRANTED;
    }

    private void startCamera(){
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(()->{
            try{
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
                imageAnalysis.setAnalyzer(analysisExecutor, this :: analyzeFrame);

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cam[camCode])
                        .build();
                overlayView.setCameraFacing(cam[camCode]==CameraSelector.LENS_FACING_FRONT);

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(getViewLifecycleOwner(),
                        cameraSelector,preview,imageAnalysis);

            }catch(ExecutionException | InterruptedException e) {
                Log.e("HealthFragment", "Use case binding failed", e);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void analyzeFrame(@NonNull ImageProxy imageProxy){
        int rotationDegree = imageProxy.getImageInfo().getRotationDegrees();
        int imageWidth = (rotationDegree==0||rotationDegree==180) ? imageProxy.getWidth() : imageProxy.getHeight();
        int imageHeight = (rotationDegree==0||rotationDegree==180) ? imageProxy.getHeight() : imageProxy.getWidth();

        InputImage image = InputImage.fromMediaImage(imageProxy.getImage(), rotationDegree);

        poseDetector.process(image).addOnSuccessListener(pose->{
            overlayView.updatePose(pose, imageWidth, imageHeight);
            imageProxy.close();
        }).addOnFailureListener(e->{
            Log.e("PoseDetection", "Pose detection failed", e);
            imageProxy.close();
        });
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        analysisExecutor.shutdown();
        if(poseDetector!=null) poseDetector.close();
    }

}