package com.example.skoml.bioindication;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

import com.fenjuly.library.ArrowDownloadButton;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements SurfaceHolder.Callback, View.OnClickListener, Camera.PictureCallback, Camera.PreviewCallback, Camera.AutoFocusCallback
{
    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private SurfaceView preview;
    private Button shotBtn;
    //private ImageView leaf;
    private View  parentView;

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

        return parentView;
    }



    @Override
    public void onResume()
    {
        super.onResume();
        camera = Camera.open();
    }

    @Override
    public void onPause()
    {
        super.onPause();

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
        /*

        Size previewSize = camera.getParameters().getPreviewSize();
        float aspect = (float) previewSize.width / previewSize.height;
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

*/
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
    }
    @Override
    public void onPictureTaken(byte[] paramArrayOfByte, final Camera paramCamera)
    {
        // сохраняем полученные jpg

        paramCamera.stopPreview();
        shotBtn.setVisibility(View.GONE);
        //leaf.setVisibility(View.GONE);
        final ArrowDownloadButton  button = (ArrowDownloadButton) parentView.findViewById(R.id.arrow_button);

        try
        {
            /*
            File saveDir = getActivity().getCacheDir();

            if (!saveDir.exists())
            {
                saveDir.mkdirs();
            }
            File dst = new File(saveDir, String.format("%d.jpg", System.currentTimeMillis()));

            FileOutputStream out = new FileOutputStream(dst);
            ByteArrayInputStream in = new ByteArrayInputStream(paramArrayOfByte);
            ReadableByteChannel source = Channels.newChannel(in);
            WritableByteChannel target = Channels.newChannel(out);
            ByteBuffer buffer = ByteBuffer.allocate(paramArrayOfByte.length);
            while (source.read(buffer) != -1) {
                buffer.flip(); // Prepare the buffer to be drained
                while (buffer.hasRemaining()) {
                    target.write(buffer);
                }
                buffer.clear(); // Empty buffer to get ready for filling
            }

            source.close();
            target.close();
            in.close();
            out.close();
        */

            button.setVisibility(View.VISIBLE);
            button.reset();
            button.startAnimating();


            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(button.getProgress()<100)
                                button.setProgress(button.getProgress() + 1);
                            else
                            {
                                paramCamera.startPreview();
                                timer.cancel();
                                button.reset();
                                button.setVisibility(View.GONE);
                                shotBtn.setVisibility(View.VISIBLE);
                               // leaf.setVisibility(View.VISIBLE);
                                showResult(new Random().nextInt(5));
                            }
                        }
                    });
                }
            }, 500, 150);

        }
        catch (Exception e)
        {
            paramCamera.startPreview();
            button.reset();
            button.setVisibility(View.GONE);
            shotBtn.setVisibility(View.VISIBLE);
            //leaf.setVisibility(View.VISIBLE);
            paramCamera.startPreview();
        }

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

        new AlertDialog.Builder(getActivity()).
                setTitle("Result").
                setIcon(dr).
                setMessage(String.format("The quality of the environment: %d from 5",  (5-value))).show();

    }
   
}