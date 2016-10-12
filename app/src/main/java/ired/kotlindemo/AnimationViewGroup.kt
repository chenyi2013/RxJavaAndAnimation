package ired.kotlindemo

import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.FrameLayout
import rx.Observable
import rx.Subscription
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by kevin on 16/10/11.
 */
class AnimationViewGroup : FrameLayout {

    private var rectangleLeft: RectangleView? = null;

    private var rectangleRight: RectangleView? = null;

    private val circleViews: ArrayList<View> = ArrayList();

    // Subscription to circle views movement animations.
    private var animationSubscription: Subscription? = null

    private val ANIMATION_DURATION_MS: Long = 1000;


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


    fun Observable<View>.translateView(translationX: Float,
                                       translationY: Float,
                                       duration: Long,
                                       interpolator: Interpolator): Observable<View> =
            lift<View>(TranslateViewOperator(translationX, translationY, duration, interpolator))


    fun init() {
        rectangleLeft = RectangleView(context)
        rectangleRight = RectangleView(context)

        addView(rectangleLeft)
        addView(rectangleRight)

        // Add 10 circles.
        for (i in 0..9) {
            val cv = CircleView(context);
            circleViews.add(cv)
            addView(cv)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        rectangleRight!!.layout(width - rectangleRight!!.measuredWidth, 0, width, rectangleRight!!.measuredHeight);
    }


    fun startAnimation() {
        // First, unsubscribe from previous animations.
        animationSubscription?.unsubscribe()

        // Timer observable that will emit every half second.
        val timerObservable = Observable.interval(0, 500, TimeUnit.MILLISECONDS)

        // Observable that will emit circle views from the list.
        val viewsObservable = Observable.from(circleViews)
                // As each circle view is emitted, stop animations on it.
                .doOnNext { v -> ViewCompat.animate(v).cancel() }
                // Just take those circles that are not already in the right rectangle.
                .filter { v -> v.translationX < rectangleRight!!.left }

//        // First, zip the timer and circle views observables, so that we get one circle view every half a second.
        animationSubscription = Observable.zip(viewsObservable, timerObservable) { view, time -> view }
                // As each view comes in, translate it so that it ends up inside the right rectangle.
                .translateView(rectangleRight!!.left.toFloat(), rectangleRight!!.top.toFloat(), ANIMATION_DURATION_MS, DecelerateInterpolator())
                .subscribe()
    }

    fun reverseAnimation() {
        // First, unsubscribe from previous animations.
        animationSubscription?.unsubscribe()

        // Timer observable that will emit every half second.
        val timerObservable = Observable.interval(0, 500, TimeUnit.MILLISECONDS)

        // Observable that will emit circle views from the list but in reverse order,
        // so that the last one that was animated is now a first one to be animated.
        val viewsObservable = Observable.from(circleViews.asReversed())
                // As each circle view is emitted, stop animations on it.
                .doOnNext { v -> ViewCompat.animate(v).cancel() }
                // Just take those circles that are not already in the left rectangle.
                .filter { v -> v.translationX > rectangleLeft!!.left }

        // First, zip the timer and circle views observables, so that we get one circle view every half a second.
        animationSubscription = Observable.zip(viewsObservable, timerObservable) { view, time -> view }
                // As each view comes in, translate it so that it ends up inside the left rectangle.
                .translateView(rectangleLeft!!.left.toFloat(), rectangleLeft!!.top.toFloat(), ANIMATION_DURATION_MS, AccelerateInterpolator())
                .subscribe()
    }

}