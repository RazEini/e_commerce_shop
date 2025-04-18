package com.shop.bagrutproject.models;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class CustomBackgroundView2 extends View {
    public CustomBackgroundView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();

        // רקע עדין עם גרדיאנט שקוף
        Paint gradientPaint = new Paint();
        LinearGradient gradient = new LinearGradient(0, 0, w, h,
                0x80D4E0F6, 0x80F2F6FC, Shader.TileMode.CLAMP);
        gradientPaint.setShader(gradient);
        canvas.drawRect(0, 0, w, h, gradientPaint);

        // ציור גלים רכים
        drawSoftWaves(canvas, w, h);
    }

    private void drawSoftWaves(Canvas canvas, int w, int h) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        // גל ראשון – תכלת
        paint.setShader(new LinearGradient(0, 0, w, h, 0x80B9D6FF, 0x80D7E8FF, Shader.TileMode.CLAMP));
        Path wavePath1 = new Path();
        wavePath1.moveTo(0, h * 0.82f);  // היה 0.75
        wavePath1.quadTo(w * 0.3f, h * 0.78f, w * 0.5f, h * 0.80f);
        wavePath1.cubicTo(w * 0.7f, h * 0.85f, w * 0.9f, h * 0.79f, w, h * 0.76f);
        wavePath1.lineTo(w, h);
        wavePath1.lineTo(0, h);
        wavePath1.close();
        canvas.drawPath(wavePath1, paint);

        // גל שני – כחול בהיר יותר
        paint.setShader(new LinearGradient(0, 0, w, h, 0x80B4E6FF, 0x80C8F1FF, Shader.TileMode.CLAMP));
        Path wavePath2 = new Path();
        wavePath2.moveTo(0, h * 0.85f);  // היה 0.78
        wavePath2.quadTo(w * 0.4f, h * 0.81f, w * 0.6f, h * 0.83f);
        wavePath2.cubicTo(w * 0.75f, h * 0.85f, w * 0.85f, h * 0.78f, w, h * 0.76f);
        wavePath2.lineTo(w, h);
        wavePath2.lineTo(0, h);
        wavePath2.close();
        canvas.drawPath(wavePath2, paint);

        // גל שלישי – שמנת בהירה
        paint.setShader(new LinearGradient(0, 0, w, h, 0x60FFF5C1, 0x60FFE8B5, Shader.TileMode.CLAMP));
        Path wavePath3 = new Path();
        wavePath3.moveTo(0, h * 0.89f);  // היה 0.83
        wavePath3.quadTo(w * 0.2f, h * 0.87f, w * 0.4f, h * 0.88f);
        wavePath3.cubicTo(w * 0.6f, h * 0.89f, w * 0.8f, h * 0.87f, w, h * 0.85f);
        wavePath3.lineTo(w, h);
        wavePath3.lineTo(0, h);
        wavePath3.close();
        canvas.drawPath(wavePath3, paint);
    }
}
