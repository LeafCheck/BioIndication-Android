package Effects;

import android.graphics.Bitmap;

public abstract class Effect {

    int[] pix;
    int w,h;
    int progress, progressMax;


    public Effect() {
    }


    public void setBitmap(Bitmap img) {
        this.w = img.getWidth();
        this.h = img.getHeight();
        pix = new int[w * h];

        int[] lineRGB = new int[w];
        for (int y = 0; y < h; y++) {
            img.getPixels(lineRGB, 0, w, 0, y, w, 1);
            System.arraycopy(lineRGB, 0, pix, y*w, lineRGB.length);
        }
        lineRGB = null;

    }

    public Bitmap getBitmap() {
       return Bitmap.createBitmap(pix, w, h, Bitmap.Config.ARGB_8888);
    }

    public void setRGB(int[] pix, int w, int h) {
        this.pix = pix;
        this.w = w;
        this.h = h;
    }

    //Получение массива RGB
    public int[] getPixels() {
        return pix;
    }




    //Применить эффект (заменить на автоматическую)

    public void apply() {
        applyEffect();
    }

    abstract void applyEffect();
}

