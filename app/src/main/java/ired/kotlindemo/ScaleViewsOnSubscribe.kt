package ired.kotlindemo

import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.view.animation.Interpolator
import rx.Completable
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by kevin on 16/10/12.
 */

class ScaleViewsOnSubscribe(private val views: List<FloatingActionButton>,
                            private val animationType: ScaleViewsOnSubscribe.AnimationType,
                            private val duration: Long,
                            private val interpolator: Interpolator
) : Completable.CompletableOnSubscribe {

    lateinit private var numberOfAnimationsToRun: AtomicInteger;

    override fun call(subscriber: Completable.CompletableSubscriber?) {

        if (views.isEmpty()) {
            subscriber!!.onCompleted();
            return;
        }

        numberOfAnimationsToRun = AtomicInteger(views.size);

        val scaleFactory = if (animationType == AnimationType.SCALE_DOWN) 0.0f else 1.0f;

        for (i in views.indices) {
            ViewCompat
                    .animate(views[i])
                    .scaleX(scaleFactory)
                    .scaleY(scaleFactory)
                    .setDuration(duration)
                    .setInterpolator(interpolator)
                    .withEndAction {
                        if (numberOfAnimationsToRun.decrementAndGet() == 0) {
                            subscriber!!.onCompleted()
                        }
                    }
        }
    }

    enum class AnimationType {
        SCALE_DOWN, SCALE_UP
    }


}