package com.example.skoml.bioindication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.ecometr.app.R;
import com.fenjuly.library.ArrowDownloadButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Effects.Effect;
import Effects.eEdgeDetection;

public class HomeFragment extends Fragment implements SurfaceHolder.Callback, View.OnClickListener, Camera.PictureCallback, Camera.PreviewCallback, Camera.AutoFocusCallback {
    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private SurfaceView preview;
    private Button shotBtn;
    private Button retryBtn;
    private Button pickBtn;
    private Button nextBtn;
    //private ImageView leaf;
    private View parentView;


    LinesDrawer linesDrawer;
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
        shotBtn = (Button) parentView.findViewById(R.id.shot);
        shotBtn.setText("Shot");
        shotBtn.setOnClickListener(this);

        pickBtn = (Button) parentView.findViewById(R.id.pick);
        pickBtn.setText("Pick file");
        pickBtn.setOnClickListener(this);
        //leaf = (ImageView) parentView.findViewById(R.id.leaf);

        button = (ArrowDownloadButton) parentView.findViewById(R.id.arrow_button);
        linesDrawer = (LinesDrawer) parentView.findViewById(R.id.lines_drawer);

        retryBtn = (Button) parentView.findViewById(R.id.retry);
        retryBtn.setText("Retry");
        retryBtn.setOnClickListener(this);

        nextBtn = (Button) parentView.findViewById(R.id.next);
        nextBtn.setText("Next");
        nextBtn.setOnClickListener(this);

        final SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(getActivity());

        if (pref.getBoolean("useRuler", false))
            parentView.findViewById(R.id.ruler_view).setVisibility(View.VISIBLE);


        return parentView;
    }

    @Override
    public void onClick(View v) {
        if (v == shotBtn) {
            // либо делаем снимок непосредственно здесь
            // 	либо включаем обработчик автофокуса

            List<String> supportedFocusModes = camera.getParameters().getSupportedFocusModes();
            boolean hasAutoFocus = supportedFocusModes != null && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO);
            if (hasAutoFocus)
                camera.autoFocus(this);
            else
                camera.takePicture(null, null, null, this);

        }
        if (v == retryBtn) {


            timer.cancel();
            button.reset();
            linesDrawer.reset();
            linesDrawer.setVisibility(View.INVISIBLE);
            retryBtn.setVisibility(View.INVISIBLE);


            preview.setVisibility(View.VISIBLE);
            parentView.findViewById(R.id.shotOrPick).setVisibility(View.VISIBLE);

            setCameraDisplayOrientation(getActivity(), Camera.CameraInfo.CAMERA_FACING_BACK, camera);
            camera.startPreview();
            if (bm != null)
                bm.recycle();
            bm = null;


        }
        if (v == pickBtn) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            try {
                startActivityForResult(
                        Intent.createChooser(intent, "Select Photo"),
                        FILE_SELECT_CODE);
                if (camera != null)
                    camera.stopPreview();
            } catch (Exception ex) {
                // Potentially direct the user to the Market with a Dialog
                Toast.makeText(getActivity(), ex.toString(),
                        Toast.LENGTH_SHORT).show();
            }

        }
        if (v == nextBtn) {
            linesDrawer.nextStep();
        }
    }


    private static final int FILE_SELECT_CODE = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();

                    System.out.println(uri);

                    // Get the path
                    String path = getPath(getActivity(), uri);

                    System.out.println(path);

                    byte[] paramArrayOfByte = getIcon(getActivity(), path);


                    processPicture(paramArrayOfByte, camera);

                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(projection[0]);
                if (cursor.moveToFirst()) {
                    String path = cursor.getString(column_index);

                    cursor.close();
                    return path;
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Eat it
            }
            if (cursor != null)
                cursor.close();
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private byte[] getIcon(Context context, String fileName) {
        try {
            File dst = new File(fileName);
            if (!dst.exists())
                return null;

            if (dst.length() == 0)
                return new byte[0];

            FileInputStream inStream = new FileInputStream(dst);
            FileChannel inChannel = inStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int) inChannel.size());
            inChannel.read(buffer);
            buffer.rewind();
            inChannel.close();
            inStream.close();
            return buffer.array();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (camera == null)
            camera = Camera.open();
        if (camera != null)
            setCameraDisplayOrientation(getActivity(), Camera.CameraInfo.CAMERA_FACING_BACK, camera);

        PackageManager pm = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        final List<ResolveInfo> pkgAppsList = pm.queryIntentActivities(intent, 0);
        if (pkgAppsList == null || pkgAppsList.size() == 0)
            pickBtn.setVisibility(View.GONE);
        else {
            if (pickBtn.getVisibility() == View.GONE)
                pickBtn.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onPause() {
        super.onPause();

        if (bm != null)
            bm.recycle();
        bm = null;
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }

    }

    public void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
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
    public void surfaceChanged(SurfaceHolder holder, int format, int previewSurfaceWidth, int previewSurfaceHeight) {

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(this);

            Size previewSize = camera.getParameters().getPreviewSize();
            float aspect = (float) previewSize.width / previewSize.height;

            int previewSurfaceWidth = preview.getWidth();
            int previewSurfaceHeight = preview.getHeight();

            LayoutParams lp = preview.getLayoutParams();

            // здесь корректируем размер отображаемого preview, чтобы не было искажений

            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                // портретный вид
                camera.setDisplayOrientation(90);
                lp.height = previewSurfaceHeight;
                lp.width = (int) (previewSurfaceHeight / aspect);
                ;
            } else {
                // ландшафтный
                camera.setDisplayOrientation(0);
                lp.width = previewSurfaceWidth;
                lp.height = (int) (previewSurfaceWidth / aspect);
            }

            preview.setLayoutParams(lp);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }


    @Override
    public void onPictureTaken(byte[] paramArrayOfByte, final Camera paramCamera) {

        paramCamera.stopPreview();

        processPicture(paramArrayOfByte, paramCamera);

    }


    @Override
    public void onAutoFocus(boolean paramBoolean, Camera paramCamera) {
        if (paramBoolean) {
            // если удалось сфокусироваться, делаем снимок
            paramCamera.takePicture(null, null, null, this);
        }
    }

    @Override
    public void onPreviewFrame(byte[] paramArrayOfByte, Camera paramCamera) {
        // здесь можно обрабатывать изображение, показываемое в preview
    }


    private void showResult(int value) {
        int icon;
        switch (value) {
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

        Drawable dr = getResources().getDrawable(icon);
        dr = DrawableCompat.wrap(dr);
        DrawableCompat.setTint(dr.mutate(), Color.WHITE);

        new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK).
                setTitle("Result").
                setIcon(dr).
                setMessage(String.format("The quality of the environment: %d from 5", (5 - value))).show();

    }


    void processPicture(final byte[] paramArrayOfByte, final Camera paramCamera) {

        if (bm != null)
            bm.recycle();
        bm = null;

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            boolean finished = false;

            @Override
            public void onPreExecute() {


                parentView.findViewById(R.id.shotOrPick).setVisibility(View.INVISIBLE);

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
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
                if (button.getProgress() < 99)
                    button.setProgress(button.getProgress() + 1);
                else {
                    if (!finished)
                        button.setProgress(0);

                    else {
                        if (button.getProgress() == 99)
                            button.setProgress(100);
                    }
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length, options);//.decodeResource(res, resId, options);

                bm = BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length, null);
                Matrix mtx = new Matrix();
                mtx.postRotate(90);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mtx, false);

                return null;
            }


            @Override
            protected void onPostExecute(Void exp) {
//                imageView.setImageBitmap(bm);
                linesDrawer.setLeaf(new LeafData(bm));
                preview.setVisibility(View.INVISIBLE);
                linesDrawer.setVisibility(View.VISIBLE);

                button.setVisibility(View.INVISIBLE);
//                retryBtn.setVisibility(View.VISIBLE);

                parentView.findViewById(R.id.shotOrPick).setVisibility(View.INVISIBLE);
                parentView.findViewById(R.id.retryOrNext).setVisibility(View.VISIBLE);
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
        Bitmap bm = BitmapFactory.decodeByteArray(arr, 0, arr.length, options);

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