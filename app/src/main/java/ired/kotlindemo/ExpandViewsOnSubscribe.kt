package ired.kotlindemo

import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.view.animation.Interpolator
import rx.Completable
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by kevin on 16/10/12.
 */
class ExpandViewsOnSubscribe(private val views:List<FloatingActionButton>,
                             private val animationType:AnimationType,
                             private val duration: Long,
                             private val interpolator: Interpolator,
                             private val paddingPx:Int): Completable.CompletableOnSubscribe {

    lateinit private var numberOfAnimationsToRun: AtomicInteger

    enum class AnimationType {
        EXPAND_HORIZONTALLY, COLLAPSE_HORIZONTALLY,
        EXPAND_VERTICALLY, COLLAPSE_VERTICALLY
    }

    override fun call(subscriber: Completable.CompletableSubscriber?) {
        if (views.isEmpty()) {
            subscriber!!.onCompleted()
            return
        }

        // We need to run as much as animations as there are views.
        numberOfAnimationsToRun = AtomicInteger(views.size)

        // Assert all FABs are the same size, we could count each item size if we're making
        // an implementation that possibly expects different-sized items.
        val fabWidth = views[0].width
        val fabHeight = views[0].height

        val horizontalExpansion = animationType == AnimationType.EXPAND_HORIZONTALLY
        val verticalExpansion = animationType == AnimationType.EXPAND_VERTICALLY

        // Only if expanding horizontally, we'll move x-translate each of the FABs by index * width.
        val xTranslationFactor = if (horizontalExpansion) fabWidth else 0

        // Only if expanding vertically, we'll move y-translate each of the FABs by index * height.
        val yTranslationFactor = if (verticalExpansion) fabHeight else 0

        // Same with padding.
        val paddingX = if (horizontalExpansion) paddingPx else 0
        val paddingY = if (verticalExpansion) paddingPx else 0

        for (i in views.indices) {

            views[i].setImageResource(R.drawable.right_arrow)
            ViewCompat.animate(views[i])
                    .translationX(i * (xTranslationFactor.toFloat() + paddingX))
                    .translationY(i * (yTranslationFactor.toFloat() + paddingY))
                    .setDuration(duration)
                    .setInterpolator(interpolator)
                    .withEndAction {
                        // Once all animations are done, call onCompleted().
                        if (numberOfAnimationsToRun.decrementAndGet() == 0) {
                            subscriber!!.onCompleted()
                        }
                    }
        }
    }
}



