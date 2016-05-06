package Effects;

public class eEdgeDetection extends Effect {

    private static final float DIVIDER = 1.8f;
    
    private static final String[] OPERATOR_NAMES = {
        "Roberts", "Prewitt", "Sobel", "Scharr"
    };
    
    private static final int[][] MATRIX_HORIZONTAL = {
        // Roberts
        {0, 0, 0,
         0, 1, 0,
         0, 0, -1 },
        // Prewitt
        {-1, 0, 1,
         -1, 0, 1,
         -1, 0, 1 },
        // Sobel
        {-1, 0, 1,
         -2, 0, 2,
         -1, 0, 1 },
        // Scharr
        { 3, 0,  -3,
         10, 0, -10,
          3, 0,  -3 }
    };
    
    private static final int[][] MATRIX_VERTICAL = {
        // Roberts
        {0,  0, 0,
         0,  0, 1,
         0, -1, 0 },
        // Prewitt
        {-1, -1, -1,
          0,  0,  0,
          1,  1,  1 },
        // Sobel
        {-1, -2, -1,
          0,  0,  0,
          1,  2,  1 },
        // Scharr
        {  3,  10,  3,
           0,   0,  0,
          -3, -10, -3 }
    };

    int[][] getAmount() {
        return new int[][]{
                new int[] {0, 0},
                new int[] {3, 2},
                new int[] {1, 0}};
    }


    int mode = 1;
    int operator = 1;

    public void setFilter(int mode, int operator) {
        this.mode = mode ;
        this.operator = operator;
    }

void applyEffect() {
        //int operator = 1;
        //int mode = 1;
        if (mode == 0) {
           colorEdges(MATRIX_VERTICAL[operator], MATRIX_HORIZONTAL[operator]);
        } else if (mode == 1) {
            grayEdges(MATRIX_VERTICAL[operator], MATRIX_HORIZONTAL[operator]);
        } else if (mode == 2) {
            substractEdges(MATRIX_VERTICAL[operator], MATRIX_HORIZONTAL[operator]);
       }

    }


    // Для цветных граней
    void colorEdges(int[] vEdgeMatrix, int[] hEdgeMatrix) {
        int[] newraster = new int[w * h];
        int index = 0;
        int rh, gh, bh, rv, gv, bv;

        super.progressMax = h;
        for (int y = 0; y < h; y++) {
            super.progress = y;
            for (int x = 0; x < w; x++) {
                int r, g, b;
                rh = gh = bh = 0;
                rv = gv = bv = 0;

                for (int row = -1; row <= 1; row++) {
                    int iy = y + row;
                    int ioffset;

                    if ((0 <= iy) && (iy < h)) {
                        ioffset = iy * w;
                    } else {
                        ioffset = y * w;
                    }

                    int moffset = (3 * (row + 1)) + 1;

                    for (int col = -1; col <= 1; col++) {
                        int ix = x + col;

                        if (!((0 <= ix) && (ix < w))) {
                            ix = x;
                        }

                        int horizValue = hEdgeMatrix[moffset + col];
                        int vertValue = vEdgeMatrix[moffset + col];
                        
                        int rgb = pix[ioffset + ix];
                        r = (rgb & 0xff0000) >> 16;
                        g = (rgb & 0x00ff00) >> 8;
                        b = rgb & 0x0000ff;
                        rh += (horizValue * r);
                        gh += (horizValue * g);
                        bh += (horizValue * b);
                        rv += (vertValue * r);
                        gv += (vertValue * g);
                        bv += (vertValue * b);
                    }
                }
                r = (int) (Math.sqrt((rh * rh) + (rv * rv)) / DIVIDER);
                g = (int) (Math.sqrt((gh * gh) + (gv * gv)) / DIVIDER);
                b = (int) (Math.sqrt((bh * bh) + (bv * bv)) / DIVIDER);
                if (r > 255) r = 255;
                if (g > 255) g = 255;
                if (b > 255) b = 255;
               // newraster[index] = (pix[index] & 0xFF000000) | (r << 16) | (g << 8) | b;
                newraster[index] =  0xFF000000 | (r << 16) | (g << 8) | b;
                index++;
            }
        }
        pix = newraster;
    }
    
    // Для чёрно-белых граней
    void grayEdges(int[] vEdgeMatrix, int[] hEdgeMatrix) {
        int[] newraster = new int[w * h];
        int index = 0;
        int rgbh, rgbv;

        super.progressMax = h;
        for (int y = 0; y < h; y++) {
            super.progress = y;
            for (int x = 0; x < w; x++) {
                int gray;
                rgbh = 0;
                rgbv = 0;

                for (int row = -1; row <= 1; row++) {
                    int iy = y + row;
                    int ioffset;

                    if ((0 <= iy) && (iy < h)) {
                        ioffset = iy * w;
                    } else {
                        ioffset = y * w;
                    }

                    int moffset = (3 * (row + 1)) + 1;

                    for (int col = -1; col <= 1; col++) {
                        int ix = x + col;

                        if (!((0 <= ix) && (ix < w))) {
                            ix = x;
                        }

                        int horizValue = hEdgeMatrix[moffset + col];
                        int vertValue = vEdgeMatrix[moffset + col];
                        
                        int rgb = pix[ioffset + ix];
                        int r = (rgb & 0xff0000) >> 16;
                        int g = (rgb & 0x00ff00) >> 8;
                        int b = rgb & 0x0000ff;
                        rgb = (r + g + b) / 3;
                        rgbh += (horizValue * rgb);
                        rgbv += (vertValue * rgb);
                    }
                }
                gray = (int) (Math.sqrt((rgbh * rgbh) + (rgbv * rgbv)) / DIVIDER);
                if (gray > 255) gray = 255;
               // newraster[index] = (pix[index] & 0xFF000000) | (gray << 16) | (gray << 8) | gray;
                newraster[index] =  0xFF000000 | (gray << 16) | (gray << 8) | gray;
                index++;
            }
        }
        pix = newraster;
    }
    
    // Вычитание граней
    void substractEdges(int[] vEdgeMatrix, int[] hEdgeMatrix) {
        int[] newraster = new int[w * h];
        int index = 0;
        int rh, gh, bh, rv, gv, bv;

        super.progressMax = h;
        for (int y = 0; y < h; y++) {
            super.progress = y;
            for (int x = 0; x < w; x++) {
                int r, g, b;
                int or, og, ob;
                rh = gh = bh = 0;
                rv = gv = bv = 0;

                for (int row = -1; row <= 1; row++) {
                    int iy = y + row;
                    int ioffset;

                    if ((0 <= iy) && (iy < h)) {
                        ioffset = iy * w;
                    } else {
                        ioffset = y * w;
                    }

                    int moffset = (3 * (row + 1)) + 1;

                    for (int col = -1; col <= 1; col++) {
                        int ix = x + col;

                        if (!((0 <= ix) && (ix < w))) {
                            ix = x;
                        }

                        int horizValue = hEdgeMatrix[moffset + col];
                        int vertValue = vEdgeMatrix[moffset + col];
                        
                        int rgb = pix[ioffset + ix];
                        r = (rgb & 0xff0000) >> 16;
                        g = (rgb & 0x00ff00) >> 8;
                        b = rgb & 0x0000ff;
                        rh += (horizValue * r);
                        gh += (horizValue * g);
                        bh += (horizValue * b);
                        rv += (vertValue * r);
                        gv += (vertValue * g);
                        bv += (vertValue * b);
                    }
                }
                r = (int) (Math.sqrt((rh * rh) + (rv * rv)) / DIVIDER);
                g = (int) (Math.sqrt((gh * gh) + (gv * gv)) / DIVIDER);
                b = (int) (Math.sqrt((bh * bh) + (bv * bv)) / DIVIDER);
                
                int rgb = pix[index];
                or = (rgb & 0xff0000) >> 16;
                og = (rgb & 0x00ff00) >> 8;
                ob = rgb & 0x0000ff;
                
                or -= r;
                og -= g;
                ob -= b;
                if (or < 0) or = 0;
                if (og < 0) og = 0;
                if (ob < 0) ob = 0;
                
                //newraster[index] = (pix[index] & 0xFF000000) | (or << 16) | (og << 8) | ob;
                newraster[index] =  0xFF000000| (or << 16) | (og << 8) | ob;

                index++;
            }
        }
        pix = newraster;
    }
    
}