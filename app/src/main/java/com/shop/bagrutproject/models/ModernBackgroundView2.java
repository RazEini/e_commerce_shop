package com.shop.bagrutproject.models;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class ModernBackgroundView2 extends View {
    public ModernBackgroundView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();

        // שרטוט רקע נוסף עם גלים מודרניים וצבעים משתנים
        drawSmoothWaves(canvas, w, h);
    }

    private void drawSmoothWaves(Canvas canvas, int w, int h) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        // גל ראשון - צבעים שקטים עם שקיפות
        Shader shader = new LinearGradient(0, 0, w, h,
                0x80D3C1F1, 0x80A4A9F7, Shader.TileMode.CLAMP); // צבעי תכלת עדינים עם שקיפות גבוהה
        paint.setShader(shader);

        Path wavePath = new Path();
        wavePath.moveTo(0, h * 0.7f);  // התחלה בתחתית
        wavePath.quadTo(w * 0.4f, h * 0.55f, w * 0.6f, h * 0.6f);  // גל רך
        wavePath.cubicTo(w * 0.8f, h * 0.75f, w, h * 0.5f, w, h * 0.6f); // גל רחב ושטוח
        wavePath.lineTo(w, h);
        wavePath.lineTo(0, h);
        wavePath.close();
        canvas.drawPath(wavePath, paint);

        // גל שני - גוון פנינה עדין
        shader = new LinearGradient(0, 0, w, h,
                0x80B8E0F9, 0x80D1F0E6, Shader.TileMode.CLAMP);  // גוון פנינה עדין
        paint.setShader(shader);

        wavePath.reset();
        wavePath.moveTo(0, h * 0.8f);  // התחלה גבוה יותר
        wavePath.quadTo(w * 0.5f, h * 0.65f, w * 0.7f, h * 0.7f);  // גל רחב יותר
        wavePath.cubicTo(w * 0.9f, h * 0.85f, w, h * 0.9f, w, h); // קשת שטוחה יותר
        wavePath.lineTo(w, h);
        wavePath.lineTo(0, h);
        wavePath.close();
        canvas.drawPath(wavePath, paint);
    }
}
