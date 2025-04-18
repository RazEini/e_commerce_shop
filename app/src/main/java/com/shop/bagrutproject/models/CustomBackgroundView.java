package com.shop.bagrutproject.models;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class CustomBackgroundView extends View {
    private Paint gradientPaint;
    private Paint wavePaint;

    public CustomBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        gradientPaint = new Paint();
        wavePaint = new Paint();
        wavePaint.setStyle(Paint.Style.FILL);
        wavePaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        canvas.drawRect(0, 0, width, height, gradientPaint);

        drawSubtleWaves(canvas, width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // רקע עדין – אפרפר בהיר מאוד עם גוון שמנת
        LinearGradient gradient = new LinearGradient(
                0, 0, w, h,
                new int[]{0xFFEFEFEF, 0xFFF9F8F3}, // פחות לבן, יותר רך
                null, Shader.TileMode.CLAMP
        );
        gradientPaint.setShader(gradient);
    }

    private void drawSubtleWaves(Canvas canvas, int w, int h) {
        // גל ראשון – אפור-כחלחל יוקרתי
        Shader shader1 = new LinearGradient(0, 0, w, h,
                0x70D0D3D4, 0x70E0E3E4, Shader.TileMode.CLAMP); // שקיפות פחותה (70)
        wavePaint.setShader(shader1);
        Path wave1 = new Path();
        wave1.moveTo(0, h * 0.76f);
        wave1.quadTo(w * 0.3f, h * 0.73f, w * 0.6f, h * 0.78f);
        wave1.quadTo(w * 0.9f, h * 0.80f, w, h * 0.76f);
        wave1.lineTo(w, h);
        wave1.lineTo(0, h);
        wave1.close();
        canvas.drawPath(wave1, wavePaint);

        // גל שני – פנינה אפרפר
        Shader shader2 = new LinearGradient(0, 0, w, h,
                0x60F0F0F0, 0x60FFFFFF, Shader.TileMode.CLAMP); // פחות שקוף
        wavePaint.setShader(shader2);
        Path wave2 = new Path();
        wave2.moveTo(0, h * 0.81f);
        wave2.quadTo(w * 0.3f, h * 0.78f, w * 0.7f, h * 0.83f);
        wave2.quadTo(w * 0.9f, h * 0.86f, w, h * 0.82f);
        wave2.lineTo(w, h);
        wave2.lineTo(0, h);
        wave2.close();
        canvas.drawPath(wave2, wavePaint);

        // גל שלישי – גוון שמנת רכה
        Shader shader3 = new LinearGradient(0, 0, w, h,
                0x50FFFDF4, 0x50FFF8E1, Shader.TileMode.CLAMP); // גם כאן פחות שקוף
        wavePaint.setShader(shader3);
        Path wave3 = new Path();
        wave3.moveTo(0, h * 0.86f);
        wave3.quadTo(w * 0.4f, h * 0.87f, w, h * 0.86f);
        wave3.lineTo(w, h);
        wave3.lineTo(0, h);
        wave3.close();
        canvas.drawPath(wave3, wavePaint);
    }



}
