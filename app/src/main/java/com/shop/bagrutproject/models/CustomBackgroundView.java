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
    public CustomBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();

        // שרטוט הגלים בחלק העליון
        drawUpperWaves(canvas, w, h);

        // שרטוט הגלים בחלק התחתון
        drawLowerWaves(canvas, w, h);
    }

    private void drawUpperWaves(Canvas canvas, int w, int h) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        Path path = new Path();

        // גל 1 - צבעים בגוון טורקיז בהיר עם ירוק
        paint.setShader(new LinearGradient(0, 0, w, h * 0.5f,
                0x60A8E6A5, 0x60A2F3E7, Shader.TileMode.CLAMP)); // שקיפות גבוהה יותר (60% לא שקוף)
        path.moveTo(0, h * 0.2f);  // התחלה נמוכה יותר
        path.cubicTo(w * 0.2f, h * 0.05f, w * 0.4f, h * 0.15f, w * 0.5f, h * 0.2f); // קשת חלקה שמתחילה נמוך
        path.cubicTo(w * 0.6f, h * 0.25f, w * 0.8f, h * 0.35f, w, h * 0.3f); // קשת רחבה ומרשימה
        path.lineTo(w, 0);  // עד הקצה העליון
        path.lineTo(0, 0);
        path.close();
        canvas.drawPath(path, paint);

        // גל 2 - צבעים בגוון כחול בהיר
        path.reset();
        paint.setShader(new LinearGradient(w, 0, 0, h * 0.5f,
                0x60A2DFF7, 0x60A8C6E4, Shader.TileMode.CLAMP)); // צבעי כחול בהיר עם שקיפות גבוהה יותר
        path.moveTo(w, 0);
        path.cubicTo(w * 0.85f, h * 0.15f, w * 0.7f, h * 0.25f, w * 0.5f, h * 0.2f); // קשת גדולה
        path.cubicTo(w * 0.3f, h * 0.25f, w * 0.1f, h * 0.2f, 0, h * 0.25f); // קשת הפוכה
        path.lineTo(0, 0);
        path.lineTo(w, 0);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawLowerWaves(Canvas canvas, int w, int h) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        Path path = new Path();

        // גל 1 - צבע ירוק בהיר עם גוון טורקיז
        paint.setShader(new LinearGradient(0, h * 0.5f, w, h,
                0x60A2F3E7, 0x60A8D7A6, Shader.TileMode.CLAMP)); // ירוק בהיר עם גוון טורקיז ושקיפות גבוהה
        path.moveTo(0, h * 0.75f);  // התחלה בחלק התחתון
        path.cubicTo(w * 0.1f, h * 0.8f, w * 0.3f, h * 0.85f, w * 0.5f, h * 0.85f); // קשת חלקה
        path.cubicTo(w * 0.7f, h * 0.85f, w * 0.9f, h * 0.8f, w, h * 0.75f); // קשת נוספת
        path.lineTo(w, h); // המשך עד לחלק התחתון
        path.lineTo(0, h); // המשך עד לחלק התחתון
        path.close();
        canvas.drawPath(path, paint);

        // גל 2 - צבע סגול עדין עם שקיפות גבוהה
        path.reset();
        paint.setShader(new LinearGradient(0, h * 0.6f, w, h,
                0x60BDA1F2, 0x60A4C9D6, Shader.TileMode.CLAMP)); // גוון סגול עדין עם שקיפות גבוהה
        path.moveTo(0, h * 0.9f);  // התחלה בתחתית
        path.cubicTo(w * 0.3f, h * 1.0f, w * 0.6f, h * 1.05f, w * 0.8f, h * 1.0f); // קשת מורחבת
        path.cubicTo(w * 0.9f, h * 0.95f, w, h * 0.85f, w, h * 0.9f); // קשת לסיום
        path.lineTo(w, h);
        path.lineTo(0, h);
        path.close();
        canvas.drawPath(path, paint);
    }


}
