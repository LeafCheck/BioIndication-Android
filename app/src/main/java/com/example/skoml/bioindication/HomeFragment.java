package com.example.skoml.bioindication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

import com.ecometr.app.R;
import com.fenjuly.library.ArrowDownloadButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Effects.Effect;
import Effects.eEdgeDetection;

public class HomeFragment extends Fragment implements SurfaceHolder.Callback, View.OnClickListener, Camera.PictureCallback, Camera.PreviewCallback, Camera.AutoFocusCallback
{
    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private SurfaceView preview;
    private Button shotBtn, retryBtn;
    //private ImageView leaf;
    private View  parentView;


    ImageView iv;
    Bitmap bm;

    ArrowDownloadButton button;
    Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.home, container, false);
        // наше SurfaceView имеет имя SurfaceView01
        preview = (SurfaceView) parentView.findViewById(R.id.SurfaceView01);

        surfaceHolder = preview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // кнопка имеет имя Button01
        shotBtn = (Button) parentView.findViewById(R.id.Button01);
        shotBtn.setText("Shot");
        shotBtn.setOnClickListener(this);
        //leaf = (ImageView) parentView.findViewById(R.id.leaf);

        button = (ArrowDownloadButton) parentView.findViewById(R.id.arrow_button);
        iv = (ImageView) parentView.findViewById(R.id.imageView);
        retryBtn  = (Button) parentView.findViewById(R.id.Button02);
        retryBtn.setText("Retry");
        retryBtn.setOnClickListener(this);

        final SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(getActivity());

       if( pref.getBoolean("useRuler", false))
          parentView.findViewById(R.id.ruler_view).setVisibility(View.VISIBLE);


        return parentView;
    }

    @Override
    public void onClick(View v)
    {
        if (v == shotBtn)
        {
            // либо делаем снимок непосредственно здесь
            // 	либо включаем обработчик автофокуса

            List<String> supportedFocusModes = camera.getParameters().getSupportedFocusModes();
            boolean hasAutoFocus = supportedFocusModes != null && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO);
            if(hasAutoFocus)
                camera.autoFocus(this);
            else
                camera.takePicture(null, null, null, this);

        }
        if (v == retryBtn)
        {


            timer.cancel();
            button.reset();
            iv.setVisibility(View.INVISIBLE);
            retryBtn.setVisibility(View.INVISIBLE);


            preview.setVisibility(View.VISIBLE);
            shotBtn.setVisibility(View.VISIBLE);

            setCameraDisplayOrientation(getActivity(), Camera.CameraInfo.CAMERA_FACING_BACK, camera);
            camera.startPreview();
            if(bm!=null)
                bm.recycle();
            bm = null;


        }
    }


    @Override
    public void onResume()
    {
        super.onResume();

            camera = Camera.open();
            setCameraDisplayOrientation(getActivity(), Camera.CameraInfo.CAMERA_FACING_BACK, camera);


    }
    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        //int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // do something for phones running an SDK before lollipop
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        camera.setDisplayOrientation(result);
    }

    public int setPhotoOrientation(Activity activity, int cameraId) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        // do something for phones running an SDK before lollipop
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }


    @Override
    public void onPause()
    {
        super.onPause();

        if(bm!=null)
        bm.recycle();
        bm = null;

        if (camera != null)
        {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int previewSurfaceWidth, int previewSurfaceHeight)
    {

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        try
        {
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Size previewSize = camera.getParameters().getPreviewSize();
        float aspect = (float) previewSize.width / previewSize.height;

        int previewSurfaceWidth = preview.getWidth();
        int previewSurfaceHeight = preview.getHeight();

        LayoutParams lp = preview.getLayoutParams();

        // здесь корректируем размер отображаемого preview, чтобы не было искажений

        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
        {
            // портретный вид
            camera.setDisplayOrientation(90);
            lp.height = previewSurfaceHeight;
            lp.width = (int) (previewSurfaceHeight / aspect);
            ;
        }
        else
        {
            // ландшафтный
            camera.setDisplayOrientation(0);
            lp.width = previewSurfaceWidth;
            lp.height = (int) (previewSurfaceWidth / aspect);
        }

        preview.setLayoutParams(lp);
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
    }


    @Override
    public void onPictureTaken(byte[] paramArrayOfByte, final Camera paramCamera)
    {

        paramCamera.stopPreview();


        shotBtn.setVisibility(View.INVISIBLE);

        processPicture(paramArrayOfByte, paramCamera);

    }



    @Override
    public void onAutoFocus(boolean paramBoolean, Camera paramCamera)
    {
        if (paramBoolean)
        {
            // если удалось сфокусироваться, делаем снимок
            paramCamera.takePicture(null, null, null, this);
        }
    }

    @Override
    public void onPreviewFrame(byte[] paramArrayOfByte, Camera paramCamera)
    {
        // здесь можно обрабатывать изображение, показываемое в preview
    }




    private void showResult(int value){
        int icon;
        switch (value){
            case 0: //very happy
                icon = R.drawable.ic_sentiment_very_satisfied_black_48px;
                break;
            case 1: //happy
                icon = R.drawable.ic_sentiment_satisfied_black_48px;
                break;
            case 2: //neutral
                icon = R.drawable.ic_sentiment_neutral_black_48px;
                break;
            case 3: //unhappy
                icon = R.drawable.ic_sentiment_dissatisfied_black_48px;
                break;
            case 4: //very unhappy
                icon = R.drawable.ic_sentiment_very_dissatisfied_black_48px;
                break;
            default:
                icon = R.drawable.ic_sentiment_neutral_black_48px;
                break;
        }

        Drawable dr =  getResources().getDrawable(icon);
        dr = DrawableCompat.wrap(dr);
        DrawableCompat.setTint(dr.mutate(), Color.WHITE);

        new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK).
                setTitle("Result").
                setIcon(dr).
                setMessage(String.format("The quality of the environment: %d from 5",  (5-value))).show();

    }


    void processPicture(final byte[] paramArrayOfByte, final Camera paramCamera)
    {

        if(bm!=null)
            bm.recycle();
        bm = null;

            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){

                boolean finished = false;
                @Override
                public void onPreExecute(){

                    button.setVisibility(View.VISIBLE);
                    button.startAnimating();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            publishProgress(null);
                        }
                    }, 25, 25);
                }

                @Override
                protected void onProgressUpdate(Void...values){
                    super.onProgressUpdate(values);
                    if(button.getProgress()<99)
                        button.setProgress(button.getProgress() + 1);
                    else {
                        if (!finished)
                            button.setProgress(0);
                        
                        else {
                            if(button.getProgress()==99)
                                button.setProgress(100);
                            else
                            {
                                preview.setVisibility(View.INVISIBLE);
                                iv.setVisibility(View.VISIBLE);
                                iv.setImageBitmap(bm);

                                button.setVisibility(View.INVISIBLE);
                                retryBtn.setVisibility(View.VISIBLE);
                                iv.setImageBitmap(bm);
                                //showResult(3);
                            }

                        }
                    }
                }

                @Override
                protected Void doInBackground(Void... params) {


                        boolean cameraFront = false;

                        int screenWidth = getResources().getDisplayMetrics().widthPixels;
                        int screenHeight = getResources().getDisplayMetrics().heightPixels;


                        //MiscMethods.log("Screen size "+width +"x"+height +" diagonal "+diagonalSize(context));

                        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
                            bm = decodeSampledBitmapFromArray(paramArrayOfByte, screenWidth, screenHeight);
                        else
                            bm = decodeSampledBitmapFromArray(paramArrayOfByte, screenHeight, screenWidth);

                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            // Notice that width and height are reversed
                            Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
                            int w = scaled.getWidth();
                            int h = scaled.getHeight();
                            // Setting post rotate to 90
                            Matrix mtx = new Matrix();

                            int CameraEyeValue = setPhotoOrientation(getActivity(), cameraFront == true ? 1 : 0); // CameraID = 1 : front 0:back
                            if (cameraFront) { // As Front camera is Mirrored so Fliping the Orientation
                                if (CameraEyeValue == 270) {
                                    mtx.postRotate(90);
                                } else if (CameraEyeValue == 90) {
                                    mtx.postRotate(270);
                                }
                            } else {
                                mtx.postRotate(CameraEyeValue); // CameraEyeValue is default to Display Rotation
                            }

                            bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
                        } else {// LANDSCAPE MODE
                            //No need to reverse width and height
                            Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth, screenHeight, true);
                            bm = scaled;
                        }


                        // String path = getArguments().getString("path");
 /*




                    File saveDir = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS);//getActivity().getCacheDir();

                    if (!saveDir.exists())
                    {
                        saveDir.mkdirs();
                    }
                    final File dst = new File(saveDir, String.format("%d.jpg", System.currentTimeMillis()));

                    FileOutputStream fos = new FileOutputStream(dst);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    fos.write(byteArray);
                    fos.close();

                   */

                        final SharedPreferences pref = PreferenceManager
                                .getDefaultSharedPreferences(getActivity());

                        eEdgeDetection effect = new eEdgeDetection();
                        effect.setFilter(pref.getInt("edge_filter", 1), pref.getInt("edge_operator", 1));

                        effect.setBitmap(bm);
                        effect.apply();
                        bm = effect.getBitmap();

                    return null;
                }


                @Override
                protected void onPostExecute(Void exp) {
                    finished = true;
                }
            };
            task.execute();






    }


    public static Bitmap decodeSampledBitmapFromArray(byte[] arr, int reqWidth, int reqHeight) {


        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(arr, 0, arr.length, options);//.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bm =BitmapFactory.decodeByteArray(arr, 0, arr.length, options);

        //new File(path).delete();
        return bm;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}