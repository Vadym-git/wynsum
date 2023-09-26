package com.wynsumart.wynsum;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class TransparentTextView extends androidx.appcompat.widget.AppCompatTextView {
    private String text;
    private int backgroundColor;
    private Paint textPaint, backGroundPaint, finishPaint;
    private Canvas canvas;
    private Bitmap backgroundPicture;
    private int textX, textY;

    public TransparentTextView(Context context) {
        super(context);
        init();
    }

    public TransparentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        text = "Hello!";
        // Background Paint
        backGroundPaint = new Paint();
        backgroundColor = Color.WHITE; // Set your desired background color
        backGroundPaint.setColor(backgroundColor);
        backGroundPaint.setAlpha(20);
        // Text Paint
        textPaint = new Paint();
        textPaint.setColor(Color.TRANSPARENT); // Set text color
        textPaint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
        textPaint.setTextSize(440); // Set text size
        textPaint.setAntiAlias(true); // Enable anti-aliasing for smoother text
        // Finish Paint
        finishPaint = new Paint();
    }

    public void setText(String text, int size, int x, int y, boolean isBold) {
        // ned finish converter dpi* to pixels
        this.text = text;
        textX = x;
        textY = y;
        textPaint.setTextSize(size);
        textPaint.setFakeBoldText(isBold);
        invalidate(); // Trigger a redraw
    }

    public void updateBackground(int color, int alpha) {
        // alpha 0 - 255
        backgroundColor = color;
        finishPaint.setAlpha(alpha);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        // Draw the background
        drawText();
    }

    public void drawText() {
        backgroundPicture = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(backgroundPicture);
        canvas1.drawColor(backgroundColor);
        float textWidth = textPaint.measureText(text);
        float x = ((getWidth() - textWidth) / 2) + textX;
        float y = (getHeight() / 2) + textY;
        canvas1.drawText(text, x, y, textPaint);
        canvas.drawBitmap(backgroundPicture, 0, 0, finishPaint);
    }


}
