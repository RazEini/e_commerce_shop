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

        // רקע לבן דומיננטי במרכז (לשמור על רקע לבן)
        canvas.drawColor(0xFFFFFFFF);

        // גלים יותר מעניינים עם צורות לא סימטריות במיקומים חיצוניים
        drawCreativeWaves(canvas, w, h);
    }

    private void drawCreativeWaves(Canvas canvas, int w, int h) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        // גל ראשון - דרום-מערב
        Shader shader = new LinearGradient(0, 0, w, h,
                0x20FFA8C7, 0x20FF8A00, Shader.TileMode.CLAMP);  // שקיפות נמוכה יותר (20%)
        paint.setShader(shader);
        Path wavePath = new Path();
        wavePath.moveTo(0, h);
        wavePath.quadTo(w * 0.2f, h * 0.7f, w * 0.4f, h * 0.5f);  // שינוי בפיתול למתינות
        wavePath.cubicTo(w * 0.55f, h * 0.4f, w * 0.75f, h * 0.3f, w, h * 0.4f);  // שיפוע יותר חלק
        wavePath.lineTo(w, h);
        wavePath.close();
        canvas.drawPath(wavePath, paint);

        // גל שני - צפון-מזרח - עדין יותר
        shader = new LinearGradient(0, 0, w, h,
                0x20A8C7FF, 0x205D55F5, Shader.TileMode.CLAMP);  // תכלת-סגול עם שקיפות נמוכה יותר
        paint.setShader(shader);
        wavePath.reset();
        wavePath.moveTo(w, 0);
        wavePath.quadTo(w * 0.85f, h * 0.1f, w * 0.7f, h * 0.25f);  // שינוי בכיוון הגל
        wavePath.cubicTo(w * 0.5f, h * 0.4f, w * 0.3f, h * 0.55f, 0, h * 0.45f);  // גל חלק וזורם
        wavePath.lineTo(0, 0);
        wavePath.close();
        canvas.drawPath(wavePath, paint);

        // גל שלישי - דרום-מערב
        shader = new LinearGradient(0, 0, w, h,
                0x2090EE90, 0x2000C896, Shader.TileMode.CLAMP);  // שקיפות נמוכה יותר
        paint.setShader(shader);
        wavePath.reset();
        wavePath.moveTo(0, h);
        wavePath.quadTo(w * 0.2f, h * 0.8f, w * 0.4f, h * 0.65f);  // שינוי במיקום הנקודות
        wavePath.cubicTo(w * 0.6f, h * 0.55f, w * 0.8f, h * 0.7f, w, h * 0.6f);  // גל עגלגל וזורם
        wavePath.lineTo(w, h);
        wavePath.lineTo(0, h);
        wavePath.close();
        canvas.drawPath(wavePath, paint);

        // גל רביעי - יותר טבעי
        shader = new LinearGradient(0, 0, w, h,
                0x1074C8FF, 0x10B88CFE, Shader.TileMode.CLAMP);  // כחול יותר מעודן עם שקיפות נמוכה
        paint.setShader(shader);
        wavePath.reset();
        wavePath.moveTo(w, 0);
        wavePath.quadTo(w * 0.7f, h * 0.05f, w * 0.5f, h * 0.3f);  // שינוי במיקום הנקודות
        wavePath.cubicTo(w * 0.4f, h * 0.5f, w * 0.3f, h * 0.6f, 0, h * 0.5f);  // גל חלק וזורם
        wavePath.lineTo(0, 0);
        wavePath.close();
        canvas.drawPath(wavePath, paint);

        // גל חמישי - ירוק עדין יותר
        shader = new LinearGradient(0, 0, w, h,
                0x20D4E9A9, 0x2074C5A5, Shader.TileMode.CLAMP);  // שקיפות בינונית יותר
        paint.setShader(shader);
        wavePath.reset();
        wavePath.moveTo(0, h * 0.9f);
        wavePath.quadTo(w * 0.25f, h * 0.75f, w * 0.5f, h * 0.8f);  // עלייה ושקע חלקים יותר
        wavePath.cubicTo(w * 0.7f, h * 0.85f, w * 0.85f, h * 0.7f, w, h * 0.75f);  // גל חלק
        wavePath.lineTo(w, h);
        wavePath.lineTo(0, h);
        wavePath.close();
        canvas.drawPath(wavePath, paint);

        // גל חדש - סימטרי למעלה, בלי גבעות חזקות
        shader = new LinearGradient(0, 0, w, h,
                0x20A8C7FF, 0x20B88CFE, Shader.TileMode.CLAMP);  // צבעים עדינים עם שקיפות נמוכה יותר
        paint.setShader(shader);
        wavePath.reset();
        wavePath.moveTo(0, 0);
        wavePath.quadTo(w * 0.05f, h * 0.1f, w * 0.2f, h * 0.2f);  // עלייה מתונה יותר קרוב לצד השמאלי
        wavePath.cubicTo(w * 0.35f, h * 0.3f, w * 0.65f, h * 0.35f, w, h * 0.3f);  // ירידה רכה לקצה הימני
        wavePath.lineTo(w, 0);  // עד הקצה הימני
        wavePath.lineTo(0, 0);  // סגירה מחדש
        wavePath.close();
        canvas.drawPath(wavePath, paint);
    }


}
