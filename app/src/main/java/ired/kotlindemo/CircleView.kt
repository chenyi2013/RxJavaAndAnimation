package ired.kotlindemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View;

/**
 * Created by kevin on 16/10/11.
 */


class CircleView : View {

    private val paint = Paint();
    private var minWidth = 0;

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init();
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        init();
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attributeSet, defStyleAttr, defStyleRes) {
        init();
    }


    fun init() {
        minWidth = Utils.dpTopx(resources, 50.0f).toInt();
        paint.isAntiAlias = true;
        paint.isDither = true;
        paint.color = Color.RED;
        paint.style = Paint.Style.FILL;
        paint.strokeWidth = Utils.dpTopx(resources, 1.0f);
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(minWidth, minWidth);
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2 - 10).toFloat(), paint);
    }

}