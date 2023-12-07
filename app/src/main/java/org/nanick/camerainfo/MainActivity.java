package org.nanick.camerainfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Range;
import android.util.Size;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},1);
        }
        setContentView(R.layout.activity_main);
        //textView = findViewById(R.id.textView);
        queryCameraCapabilities();
    }
    public CameraManager cameraManager;
    public int densityDpi = 1;
    public void queryCameraCapabilities() {
        this.cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        this.densityDpi = (int)(metrics.density * 160f);
        Log.i("CameraInfo","density dpi: " + this.densityDpi);
        try {
            // Get the list of available camera IDs
            String[] cameraIds = cameraManager.getCameraIdList();
            StringBuilder capabilitiesText = new StringBuilder();

            for (String cameraId : cameraIds) {
                // Get camera characteristics for each camera
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);

                // Query and append camera capabilities to the text
                appendCameraCapabilities(characteristics, capabilitiesText, cameraId);
            }
            Log.i("CameraCapabilities",capabilitiesText.toString());
            // Display the camera capabilities in the TextView
            //textView.setText(capabilitiesText.toString());

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    public int tvindex = 0;
    public TextView currentTv;
    public int previousId = -1;
    private <List> void showSupportedVideoSettings(StreamConfigurationMap scm, int camid, TextView tv, String orientation) {
        String nbsp = new String(new byte[]{(byte) 194, (byte) 160});
        String ind0 = nbsp + nbsp + nbsp + nbsp;
        String ind1 = ind0 + nbsp + nbsp + nbsp + nbsp;
        String ind2 = ind1 + nbsp + nbsp + nbsp + nbsp;
        tv.setEnabled(true);
        StringBuilder sbm = new StringBuilder();
        Range<Integer>[] fpsRanges = scm.getHighSpeedVideoFpsRanges();
        sbm.append(ind0 + "High speed config for camera " + camid + " " + orientation).append("\n");
        for (Range<Integer> fpsRange : fpsRanges) {
            sbm.append(ind1 + "Frame rate: " + fpsRange.toString()).append("\n");
            Size[] sizes = scm.getHighSpeedVideoSizesFor(fpsRange);
            sbm.append(ind2 + "Sizes: [");
            ArrayList sizeList = new ArrayList();
            for(Size size : sizes){
                sizeList.add(size.toString());
            }
            String joinSizes = String.join(",",sizeList);
            sbm.append(joinSizes + "]").append("\n");
        }
        String built = sbm.toString();
        //int lines = built.split("\n").length + 1;
        //ViewGroup.LayoutParams hslp = tv.getLayoutParams();
        //hslp.height = ((lines * 40)) * (this.densityDpi / 160);
        //tv.setLayoutParams(hslp);
        tv.setText(built);
        adjustTextViewHeight(tv);
        Log.i("CameraInfo", built);
    }
    private void appendCameraCapabilities(CameraCharacteristics characteristics, StringBuilder stringBuilder, String camId) {
        int camIdentifier = Integer.parseInt(camId);
        TextView cam;
        TextView cap;
        TextView his;
        switch(camIdentifier){
            case 0:
                cam = findViewById(R.id._0);
                cap = findViewById(R.id._0cap);
                his = findViewById(R.id._0hs);
                break;
            case 1:
                cam = findViewById(R.id._1);
                cap = findViewById(R.id._1cap);
                his = findViewById(R.id._1hs);
                break;
            case 2:
                cam = findViewById(R.id._2);
                cap = findViewById(R.id._2cap);
                his = findViewById(R.id._2hs);
                break;
            case 3:
                cam = findViewById(R.id._3);
                cap = findViewById(R.id._3cap);
                his = findViewById(R.id._3hs);
                break;
            case 4:
                cam = findViewById(R.id._4);
                cap = findViewById(R.id._4cap);
                his = findViewById(R.id._4hs);
                break;
            case 5:
                cam = findViewById(R.id._5);
                cap = findViewById(R.id._5cap);
                his = findViewById(R.id._5hs);
                break;
            default:
                cam = findViewById(R.id._5);
                cap = findViewById(R.id._5cap);
                his = findViewById(R.id._5hs);
                break;
        }
        stringBuilder.append("Camera ID: " + camId);
        int facing = characteristics.get(CameraCharacteristics.LENS_FACING);
        String camOrientation = "";
        switch(facing){
            case CameraCharacteristics.LENS_FACING_FRONT:
                stringBuilder.append(" Front").append("\n");
                cam.setText("Camera ID: " + camId + " Front-facing");
                camOrientation = "Front-facing";
                break;
            case CameraCharacteristics.LENS_FACING_BACK:
                stringBuilder.append(" Back").append("\n");
                cam.setText("Camera ID: " + camId + " Rear");
                camOrientation = "Rear";
                break;
            case CameraCharacteristics.LENS_FACING_EXTERNAL:
                stringBuilder.append(" External").append("\n");
                cam.setText("Camera ID: " + camId + " External");
                camOrientation = "External";
                break;
            default:
                break;
        }
        StringBuilder sb = new StringBuilder();
        int[] capabilities = characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
        String nbsp = new String(new byte[]{(byte) 194, (byte) 160});
        String indent = nbsp + nbsp + nbsp + nbsp;
        for (int capability : capabilities) {
            switch(capability){
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE :
                    sb.append(indent + "BACKWARD_COMPATIBLE ").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_BURST_CAPTURE :
                    sb.append(indent + "BURST_CAPTURE ").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_CONSTRAINED_HIGH_SPEED_VIDEO:
                    sb.append(indent + "CONSTRAINED_HIGH_SPEED_VIDEO").append("\n");
                    StreamConfigurationMap scm = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    if(scm != null){
                        showSupportedVideoSettings(scm,camIdentifier,his,camOrientation);
                    }
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_DEPTH_OUTPUT:
                    sb.append(indent + "DEPTH_OUTPUT").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_DYNAMIC_RANGE_TEN_BIT:
                    sb.append(indent + "DYNAMIC_RANGE_TEN_BIT").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_LOGICAL_MULTI_CAMERA:
                    sb.append(indent + "LOGICAL_MULTI_CAMERA").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_MANUAL_POST_PROCESSING:
                    sb.append(indent + "MANUAL_POST_PROCESSING").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_MANUAL_SENSOR:
                    sb.append(indent + "MANUAL_SENSOR").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_MONOCHROME:
                    sb.append(indent + "MONOCHROME").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_MOTION_TRACKING:
                    sb.append(indent + "MOTION_TRACKING").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_OFFLINE_PROCESSING:
                    sb.append(indent + "OFFLINE_PROCESSING").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_PRIVATE_REPROCESSING:
                    sb.append(indent + "PRIVATE_REPROCESSING").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_RAW:
                    sb.append(indent + "RAW").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_READ_SENSOR_SETTINGS:
                    sb.append(indent + "READ_SENSOR_SETTINGS").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_REMOSAIC_REPROCESSING:
                    sb.append(indent + "REMOSAIC_REPROCESSING").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_SECURE_IMAGE_DATA:
                    sb.append(indent + "SECURE_IMAGE_DATA").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_STREAM_USE_CASE:
                    sb.append(indent + "STREAM_USE_CASE").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_SYSTEM_CAMERA:
                    sb.append(indent + "SYSTEM_CAMERA").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_ULTRA_HIGH_RESOLUTION_SENSOR:
                    sb.append(indent + "ULTRA_HIGH_RESOLUTION_SENSOR").append("\n");
                    break;
                case CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_YUV_REPROCESSING:
                    sb.append(indent + "YUV_REPROCESSING").append("\n");
                    break;
                default:
                    break;
            }
        }
        //int lines = sb.toString().split("\n").length + 1;
        //ViewGroup.LayoutParams lp = cap.getLayoutParams();
        //lp.height = ((lines * 40)) * (this.densityDpi / 160);
        //cap.setLayoutParams(lp);
        cap.setText(sb.toString());
        adjustTextViewHeight(cap);
        Log.i("CameraInfo",stringBuilder.toString());
        Log.i("CameraInfo",sb.toString());
    }
    public void adjustTextViewHeight(TextView textView){
        int lineCount = textView.getLineCount();
        int lineHeight = textView.getLineHeight();
        int newHeight = lineCount * lineHeight;
        textView.getLayoutParams().height = textView.getLineCount() * textView.getLineHeight();
        textView.requestLayout();
    }
}