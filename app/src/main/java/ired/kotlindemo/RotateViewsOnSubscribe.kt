package ired.kotlindemo

import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.view.animation.Interpolator
import rx.Completable
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by kevin on 16/10/12.
 */
class RotateViewsOnSubscribe(private val views: List<FloatingActionButton>,
                             private val animationType: RotateViewsOnSubscribe.AnimationType,
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

        val angle = if (animationType == AnimationType.ROTATE_TO_0) 0.0f else 90.0f;

        for (i in views.indices) {
            ViewCompat
                    .animate(views[i])
                    .rotation(angle)
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
        ROTATE_TO_90, ROTATE_TO_0
    }
}