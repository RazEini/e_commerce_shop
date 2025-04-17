package com.shop.bagrutproject.models;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class ModernBackgroundView extends View {
    public ModernBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();

        // שרטוט רקע מודרני עם גלים עדינים ופשוטים
        drawCleanWaves(canvas, w, h);
    }

    private void drawCleanWaves(Canvas canvas, int w, int h) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        // גל ראשון - צבע בהיר וקטעים מעוגלים
        Shader shader = new LinearGradient(0, 0, w, h,
                0x80A8E6F0, 0x80C1D3F8, Shader.TileMode.CLAMP); // גוון כחול בהיר עם שקיפות
        paint.setShader(shader);

        Path wavePath = new Path();
        wavePath.moveTo(0, h * 0.6f);  // התחלה נמוכה יותר
        wavePath.quadTo(w * 0.3f, h * 0.4f, w * 0.5f, h * 0.55f);  // קשת רכה מאוד
        wavePath.cubicTo(w * 0.7f, h * 0.7f, w * 0.85f, h * 0.65f, w, h * 0.5f); // קשת מורחבת בסוף
        wavePath.lineTo(w, h);
        wavePath.lineTo(0, h);
        wavePath.close();
        canvas.drawPath(wavePath, paint);

        // גל שני - צבעים שונים בשקיפות גבוהה
        shader = new LinearGradient(w, 0, 0, h * 0.5f,
                0x80A2DFF7, 0x80A8C6E4, Shader.TileMode.CLAMP); // כחול בהיר עם שקיפות גבוהה
        paint.setShader(shader);

        wavePath.reset();
        wavePath.moveTo(0, h);  // התחלה בתחתית
        wavePath.quadTo(w * 0.3f, h * 0.85f, w * 0.5f, h * 0.75f);  // גל מעגלי
        wavePath.cubicTo(w * 0.7f, h * 0.7f, w * 0.9f, h * 0.8f, w, h * 0.9f); // גל מרוחב
        wavePath.lineTo(w, h);
        wavePath.lineTo(0, h);
        wavePath.close();
        canvas.drawPath(wavePath, paint);
    }
}
