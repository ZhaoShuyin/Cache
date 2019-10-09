//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.ecg.connect;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class DrawUtils {
    private static float ScaleFactor = 1.0F;
    private static float unit = 0.41F;
    private static float revise_Y = 0.65F;
    private static float fHeight = 0.6F;
    public static float[] vertex_1;
    public static float[] vertex_2;
    public static float[] vertex_3;
    public static float[] vertex_aVR;
    public static float[] vertex_aVL;
    public static float[] vertex_aVF;
    public static float[] vertex_V1;
    public static float[] vertex_V2;
    public static float[] vertex_V3;
    public static float[] vertex_V4;
    public static float[] vertex_V5;
    public static float[] vertex_V6;
    public static float[] vertex62_1;
    public static float[] vertex62_2;
    public static float[] vertex62_3;
    public static float[] vertex62_aVR;
    public static float[] vertex62_aVL;
    public static float[] vertex62_aVF;
    public static float[] vertex62_V1;
    public static float[] vertex62_V2;
    public static float[] vertex62_V3;
    public static float[] vertex62_V4;
    public static float[] vertex62_V5;
    public static float[] vertex62_V6;
    public static float[] vertex621_1;
    public static float[] vertex621_2;
    public static float[] vertex621_3;
    public static float[] vertex621_aVR;
    public static float[] vertex621_aVL;
    public static float[] vertex621_aVF;
    public static float[] vertex621_V1;
    public static float[] vertex621_V2;
    public static float[] vertex621_V3;
    public static float[] vertex621_V4;
    public static float[] vertex621_V5;
    public static float[] vertex621_V6;
    public static float[] vertex621_X;
    public static float[] coord;
    public static float[] vertex26_1;
    public static float[] vertex26_2;
    public static float[] vertex26_3;
    public static float[] vertex26_4;
    public static float[] vertex26_5;
    public static float[] vertex26_6;
    private static final float hScale = 0.014F;
    private static float vScale;
    private static int displayMode;
    private static int displayMode2x6;
    private static final float displaySpeed5 = 0.0028000001F;
    private static final float displaySpeed10 = 0.0056000003F;
    private static final float displaySpeed125 = 0.007F;
    private static final float displaySpeed25 = 0.014F;
    private static final float displaySpeed50 = 0.028F;
    private static float displaySpeed;
    private static float displayGain;
    private static final float displayGain5;
    private static final float displayGain10;
    private static final float displayGain20;
    private static final float displayGain2_5;
    private static float displayGainVx;
    private static final float displayGainVx2_5;
    private static final float displayGainVx5;
    private static final float displayGainVx10;
    private static final float displayGainVx20;
    private static int rhythmIndex;
    private static boolean[] fontFlag;
    private static boolean fontChanged;

    static {
        vertex_1 = new float[]{-10.5F, 11.0F * unit - revise_Y, 0.0F, -9.5F, 11.0F * unit - revise_Y, 0.0F, -10.5F, 11.0F * unit - revise_Y + fHeight, 0.0F, -9.5F, 11.0F * unit - revise_Y + fHeight, 0.0F};
        vertex_2 = new float[]{-10.5F, 9.0F * unit - revise_Y, 0.0F, -9.5F, 9.0F * unit - revise_Y, 0.0F, -10.5F, 9.0F * unit - revise_Y + fHeight, 0.0F, -9.5F, 9.0F * unit - revise_Y + fHeight, 0.0F};
        vertex_3 = new float[]{-10.5F, 7.0F * unit - revise_Y, 0.0F, -9.5F, 7.0F * unit - revise_Y, 0.0F, -10.5F, 7.0F * unit - revise_Y + fHeight, 0.0F, -9.5F, 7.0F * unit - revise_Y + fHeight, 0.0F};
        vertex_aVR = new float[]{-10.5F, 5.0F * unit - revise_Y, 0.0F, -9.5F, 5.0F * unit - revise_Y, 0.0F, -10.5F, 5.0F * unit - revise_Y + fHeight, 0.0F, -9.5F, 5.0F * unit - revise_Y + fHeight, 0.0F};
        vertex_aVL = new float[]{-10.5F, 3.0F * unit - revise_Y, 0.0F, -9.5F, 3.0F * unit - revise_Y, 0.0F, -10.5F, 3.0F * unit - revise_Y + fHeight, 0.0F, -9.5F, 3.0F * unit - revise_Y + fHeight, 0.0F};
        vertex_aVF = new float[]{-10.5F, unit - revise_Y, 0.0F, -9.5F, unit - revise_Y, 0.0F, -10.5F, unit - revise_Y + fHeight, 0.0F, -9.5F, unit - revise_Y + fHeight, 0.0F};
        vertex_V1 = new float[]{-10.5F, -unit - revise_Y, 0.0F, -9.5F, -unit - revise_Y, 0.0F, -10.5F, -unit - revise_Y + fHeight, 0.0F, -9.5F, -unit - revise_Y + fHeight, 0.0F};
        vertex_V2 = new float[]{-10.5F, -3.0F * unit - revise_Y, 0.0F, -9.5F, -3.0F * unit - revise_Y, 0.0F, -10.5F, -3.0F * unit - revise_Y + fHeight, 0.0F, -9.5F, -3.0F * unit - revise_Y + fHeight, 0.0F};
        vertex_V3 = new float[]{-10.5F, -5.0F * unit - revise_Y, 0.0F, -9.5F, -5.0F * unit - revise_Y, 0.0F, -10.5F, -5.0F * unit - revise_Y + fHeight, 0.0F, -9.5F, -5.0F * unit - revise_Y + fHeight, 0.0F};
        vertex_V4 = new float[]{-10.5F, -7.0F * unit - revise_Y, 0.0F, -9.5F, -7.0F * unit - revise_Y, 0.0F, -10.5F, -7.0F * unit - revise_Y + fHeight, 0.0F, -9.5F, -7.0F * unit - revise_Y + fHeight, 0.0F};
        vertex_V5 = new float[]{-10.5F, -9.0F * unit - revise_Y, 0.0F, -9.5F, -9.0F * unit - revise_Y, 0.0F, -10.5F, -9.0F * unit - revise_Y + fHeight, 0.0F, -9.5F, -9.0F * unit - revise_Y + fHeight, 0.0F};
        vertex_V6 = new float[]{-10.5F, -11.0F * unit - revise_Y, 0.0F, -9.5F, -11.0F * unit - revise_Y, 0.0F, -10.5F, -11.0F * unit - revise_Y + fHeight, 0.0F, -9.5F, -11.0F * unit - revise_Y + fHeight, 0.0F};
        vertex62_1 = new float[]{-10.5F, 11.0F * unit - revise_Y, 0.0F, -9.4F, 11.0F * unit - revise_Y, 0.0F, -10.5F, 11.0F * unit - revise_Y + fHeight, 0.0F, -9.4F, 11.0F * unit - revise_Y + fHeight, 0.0F};
        vertex62_2 = new float[]{-10.5F, 7.0F * unit - revise_Y, 0.0F, -9.3F, 7.0F * unit - revise_Y, 0.0F, -10.5F, 7.0F * unit - revise_Y + fHeight, 0.0F, -9.3F, 7.0F * unit - revise_Y + fHeight, 0.0F};
        vertex62_3 = new float[]{-10.5F, 3.0F * unit - revise_Y, 0.0F, -9.3F, 3.0F * unit - revise_Y, 0.0F, -10.5F, 3.0F * unit - revise_Y + fHeight, 0.0F, -9.3F, 3.0F * unit - revise_Y + fHeight, 0.0F};
        vertex62_aVR = new float[]{-10.5F, -unit - revise_Y, 0.0F, -9.3F, -unit - revise_Y, 0.0F, -10.5F, -unit - revise_Y + fHeight, 0.0F, -9.3F, -unit - revise_Y + fHeight, 0.0F};
        vertex62_aVL = new float[]{-10.5F, -5.0F * unit - revise_Y, 0.0F, -9.3F, -5.0F * unit - revise_Y, 0.0F, -10.5F, -5.0F * unit - revise_Y + fHeight, 0.0F, -9.3F, -5.0F * unit - revise_Y + fHeight, 0.0F};
        vertex62_aVF = new float[]{-10.5F, -9.0F * unit - revise_Y, 0.0F, -9.3F, -9.0F * unit - revise_Y, 0.0F, -10.5F, -9.0F * unit - revise_Y + fHeight, 0.0F, -9.3F, -9.0F * unit - revise_Y + fHeight, 0.0F};
        vertex62_V1 = new float[]{0.04F, 11.0F * unit - revise_Y, 0.0F, 1.06F, 11.0F * unit - revise_Y, 0.0F, 0.04F, 11.0F * unit - revise_Y + fHeight, 0.0F, 1.06F, 11.0F * unit - revise_Y + fHeight, 0.0F};
        vertex62_V2 = new float[]{0.04F, 7.0F * unit - revise_Y, 0.0F, 1.06F, 7.0F * unit - revise_Y, 0.0F, 0.04F, 7.0F * unit - revise_Y + fHeight, 0.0F, 1.06F, 7.0F * unit - revise_Y + fHeight, 0.0F};
        vertex62_V3 = new float[]{0.04F, 3.0F * unit - revise_Y, 0.0F, 1.06F, 3.0F * unit - revise_Y, 0.0F, 0.04F, 3.0F * unit - revise_Y + fHeight, 0.0F, 1.06F, 3.0F * unit - revise_Y + fHeight, 0.0F};
        vertex62_V4 = new float[]{0.04F, -unit - revise_Y, 0.0F, 1.06F, -unit - revise_Y, 0.0F, 0.04F, -unit - revise_Y + fHeight, 0.0F, 1.06F, -unit - revise_Y + fHeight, 0.0F};
        vertex62_V5 = new float[]{0.04F, -5.0F * unit - revise_Y, 0.0F, 1.06F, -5.0F * unit - revise_Y, 0.0F, 0.04F, -5.0F * unit - revise_Y + fHeight, 0.0F, 1.06F, -5.0F * unit - revise_Y + fHeight, 0.0F};
        vertex62_V6 = new float[]{0.04F, -9.0F * unit - revise_Y, 0.0F, 1.06F, -9.0F * unit - revise_Y, 0.0F, 0.04F, -9.0F * unit - revise_Y + fHeight, 0.0F, 1.06F, -9.0F * unit - revise_Y + fHeight, 0.0F};
        vertex621_1 = new float[]{-10.5F, 11.0F * unit - revise_Y, 0.0F, -9.4F, 11.0F * unit - revise_Y, 0.0F, -10.5F, 11.0F * unit - revise_Y + fHeight, 0.0F, -9.4F, 11.0F * unit - revise_Y + fHeight, 0.0F};
        vertex621_2 = new float[]{-10.5F, 7.5F * unit - revise_Y, 0.0F, -9.3F, 7.5F * unit - revise_Y, 0.0F, -10.5F, 7.5F * unit - revise_Y + fHeight, 0.0F, -9.3F, 7.5F * unit - revise_Y + fHeight, 0.0F};
        vertex621_3 = new float[]{-10.5F, 4.0F * unit - revise_Y, 0.0F, -9.3F, 4.0F * unit - revise_Y, 0.0F, -10.5F, 4.0F * unit - revise_Y + fHeight, 0.0F, -9.3F, 4.0F * unit - revise_Y + fHeight, 0.0F};
        vertex621_aVR = new float[]{-10.5F, 0.5F * unit - revise_Y, 0.0F, -9.3F, 0.5F * unit - revise_Y, 0.0F, -10.5F, 0.5F * unit - revise_Y + fHeight, 0.0F, -9.3F, 0.5F * unit - revise_Y + fHeight, 0.0F};
        vertex621_aVL = new float[]{-10.5F, -3.0F * unit - revise_Y, 0.0F, -9.3F, -3.0F * unit - revise_Y, 0.0F, -10.5F, -3.0F * unit - revise_Y + fHeight, 0.0F, -9.3F, -3.0F * unit - revise_Y + fHeight, 0.0F};
        vertex621_aVF = new float[]{-10.5F, -6.5F * unit - revise_Y, 0.0F, -9.3F, -6.5F * unit - revise_Y, 0.0F, -10.5F, -6.5F * unit - revise_Y + fHeight, 0.0F, -9.3F, -6.5F * unit - revise_Y + fHeight, 0.0F};
        vertex621_V1 = new float[]{0.04F, 11.0F * unit - revise_Y, 0.0F, 1.06F, 11.0F * unit - revise_Y, 0.0F, 0.04F, 11.0F * unit - revise_Y + fHeight, 0.0F, 1.06F, 11.0F * unit - revise_Y + fHeight, 0.0F};
        vertex621_V2 = new float[]{0.04F, 7.5F * unit - revise_Y, 0.0F, 1.06F, 7.5F * unit - revise_Y, 0.0F, 0.04F, 7.5F * unit - revise_Y + fHeight, 0.0F, 1.06F, 7.5F * unit - revise_Y + fHeight, 0.0F};
        vertex621_V3 = new float[]{0.04F, 4.0F * unit - revise_Y, 0.0F, 1.06F, 4.0F * unit - revise_Y, 0.0F, 0.04F, 4.0F * unit - revise_Y + fHeight, 0.0F, 1.06F, 4.0F * unit - revise_Y + fHeight, 0.0F};
        vertex621_V4 = new float[]{0.04F, 0.5F * unit - revise_Y, 0.0F, 1.06F, 0.5F * unit - revise_Y, 0.0F, 0.04F, 0.5F * unit - revise_Y + fHeight, 0.0F, 1.06F, 0.5F * unit - revise_Y + fHeight, 0.0F};
        vertex621_V5 = new float[]{0.04F, -3.0F * unit - revise_Y, 0.0F, 1.06F, -3.0F * unit - revise_Y, 0.0F, 0.04F, -3.0F * unit - revise_Y + fHeight, 0.0F, 1.06F, -3.0F * unit - revise_Y + fHeight, 0.0F};
        vertex621_V6 = new float[]{0.04F, -6.5F * unit - revise_Y, 0.0F, 1.06F, -6.5F * unit - revise_Y, 0.0F, 0.04F, -6.5F * unit - revise_Y + fHeight, 0.0F, 1.06F, -6.5F * unit - revise_Y + fHeight, 0.0F};
        vertex621_X = new float[]{-10.5F, -10.0F * unit - revise_Y, 0.0F, -9.3F, -10.0F * unit - revise_Y, 0.0F, -10.5F, -10.0F * unit - revise_Y + fHeight, 0.0F, -9.3F, -10.0F * unit - revise_Y + fHeight, 0.0F};
        coord = new float[]{0.0F, ScaleFactor * 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F};
        vertex26_1 = new float[]{-10.5F, 11.0F * unit - revise_Y, 0.0F, -9.4F, 11.0F * unit - revise_Y, 0.0F, -10.5F, 11.0F * unit - revise_Y + fHeight, 0.0F, -9.4F, 11.0F * unit - revise_Y + fHeight, 0.0F};
        vertex26_2 = new float[]{-10.5F, 7.0F * unit - revise_Y, 0.0F, -9.3F, 7.0F * unit - revise_Y, 0.0F, -10.5F, 7.0F * unit - revise_Y + fHeight, 0.0F, -9.3F, 7.0F * unit - revise_Y + fHeight, 0.0F};
        vertex26_3 = new float[]{-10.5F, 3.0F * unit - revise_Y, 0.0F, -9.3F, 3.0F * unit - revise_Y, 0.0F, -10.5F, 3.0F * unit - revise_Y + fHeight, 0.0F, -9.3F, 3.0F * unit - revise_Y + fHeight, 0.0F};
        vertex26_4 = new float[]{-10.5F, -1.0F * unit - revise_Y, 0.0F, -9.3F, -1.0F * unit - revise_Y, 0.0F, -10.5F, -1.0F * unit - revise_Y + fHeight, 0.0F, -9.3F, -1.0F * unit - revise_Y + fHeight, 0.0F};
        vertex26_5 = new float[]{-10.5F, -5.0F * unit - revise_Y, 0.0F, -9.3F, -5.0F * unit - revise_Y, 0.0F, -10.5F, -5.0F * unit - revise_Y + fHeight, 0.0F, -9.3F, -5.0F * unit - revise_Y + fHeight, 0.0F};
        vertex26_6 = new float[]{-10.5F, -9.0F * unit - revise_Y, 0.0F, -9.3F, -9.0F * unit - revise_Y, 0.0F, -10.5F, -9.0F * unit - revise_Y + fHeight, 0.0F, -9.3F, -9.0F * unit - revise_Y + fHeight, 0.0F};
        vScale = 5.55E-4F;
        displayMode = 0;
        displayMode2x6 = 0;
        displaySpeed = 0.014F;
        displayGain = vScale;
        displayGain5 = vScale;
        displayGain10 = vScale * 2.0F;
        displayGain20 = vScale * 4.0F;
        displayGain2_5 = vScale * 0.5F;
        displayGainVx = vScale;
        displayGainVx2_5 = vScale * 0.5F;
        displayGainVx5 = vScale;
        displayGainVx10 = vScale * 2.0F;
        displayGainVx20 = vScale * 4.0F;
        rhythmIndex = 0;
        fontFlag = new boolean[]{false, false, true, false, false, true, false, false, true, false, false, false};
        fontChanged = false;
    }

    public DrawUtils() {
    }

    public static Bitmap initFontBitmap(String font) {
        Bitmap bitmap = Bitmap.createBitmap(48, 48, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(0);
        Paint p = new Paint();
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, 3);
        p.setAntiAlias(true);
        p.setColor(-16711936);
        p.setTypeface(typeface);
        p.setTextSize(24.0F);
        canvas.drawText(font, 2.0F, 35.0F, p);
        return bitmap;
    }

    public static Bitmap initFontBitmap(String font, int index) {
        Bitmap bitmap = Bitmap.createBitmap(64, 64, Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(0);
        Paint p = new Paint();
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, 1);
        p.setAntiAlias(true);
        if(fontFlag[index]) {
            p.setColor(Color.argb(255, 144, 206, 245));
        } else {
            p.setColor(-16711936);
        }

        p.setTypeface(typeface);
        p.setTextSize(30.0F);
        canvas.drawText(font, 2.0F, 35.0F, p);
        return bitmap;
    }

    public static FloatBuffer makeFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

    public static void setFontFlag(int index) {
        fontFlag[index] = !fontFlag[index];
    }

    public static void setChangState() {
        fontChanged = true;
    }

    public static boolean getChangState() {
        return fontChanged;
    }

    public static void setRhtythmIndex(int mode) {
        rhythmIndex = mode;
    }

    public static int getRhythmIndex() {
        return rhythmIndex;
    }

    public static void setDisplayMode(int mode) {
        displayMode = mode;
    }

    public static int getDisplayMode() {
        return displayMode;
    }

    public static void setDisplayMode2x6(int mode2x6) {
        displayMode2x6 = mode2x6;
    }

    public static int getDisplayMode2x6() {
        return displayMode2x6;
    }

    public static void setDisplaySpeed(int speed) {
        switch(speed) {
        case 1:
            displaySpeed = 0.007F;
            break;
        case 2:
            displaySpeed = 0.014F;
            break;
        case 3:
            displaySpeed = 0.028F;
            break;
        case 4:
            displaySpeed = 0.0028000001F;
            break;
        case 5:
            displaySpeed = 0.0056000003F;
        }

    }

    public static float getDisplaySpeed() {
        return displaySpeed;
    }

    public static void setDisplayGain(int gain) {
        switch(gain) {
        case 1:
            displayGain = displayGain5;
            displayGainVx = displayGainVx5;
            break;
        case 2:
            displayGain = displayGain10;
            displayGainVx = displayGainVx10;
            break;
        case 3:
            displayGain = displayGain20;
            displayGainVx = displayGainVx20;
            break;
        case 4:
            displayGain = displayGain2_5;
            displayGainVx = displayGainVx2_5;
            break;
        case 5:
            displayGain = displayGain10;
            displayGainVx = displayGainVx5;
            break;
        case 6:
            displayGain = displayGain20;
            displayGainVx = displayGainVx10;
        }

    }

    public static float getDisplayGain() {
        return displayGain;
    }

    public static float getDisplayGainVx() {
        return displayGainVx;
    }
}
