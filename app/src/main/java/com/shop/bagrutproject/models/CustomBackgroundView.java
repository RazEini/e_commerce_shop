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
    private Paint linePaint;
    private Paint gradientPaint;

    public CustomBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        gradientPaint = new Paint();
        gradientPaint.setShader(new LinearGradient(
                0, 0, 0, getHeight(),
                new int[]{0xFFF5F5F5, 0xFFE0E0E0},
                null, Shader.TileMode.CLAMP
        ));

        linePaint = new Paint();
        linePaint.setColor(0xFFBDBDBD);
        linePaint.setStrokeWidth(2);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        canvas.drawRect(0, 0, width, height, gradientPaint);

        for (int i = 0; i < width; i += 150) {
            Path path = new Path();
            path.moveTo(i, 0);
            path.lineTo(i + 200, height);
            canvas.drawPath(path, linePaint);
        }

        for (int i = 0; i < width; i += 150) {
            Path path = new Path();
            path.moveTo(i, height);
            path.lineTo(i + 200, 0);
            canvas.drawPath(path, linePaint);
        }
    }
}
