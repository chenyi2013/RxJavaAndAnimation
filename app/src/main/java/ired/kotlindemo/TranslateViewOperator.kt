package ired.kotlindemo

import android.support.v4.view.ViewCompat
import android.view.View
import android.view.animation.Interpolator
import rx.Observable
import rx.Subscriber
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class TranslateViewOperator(private val translationX: Float,
                            private val translationY: Float,
                            private val duration: Long,
                            private val interpolator: Interpolator) : Observable.Operator<View, View> {

    // Counts the number of animations in progress.
    // Used for properly propagating onComplete() call to the subscriber.
    private val numberOfRunningAnimations = AtomicInteger(0)

    // Indicates whether this operator received the onComplete() call or not.
    private val isOnCompleteCalled = AtomicBoolean(false)

    override fun call(subscriber: Subscriber<in View>) = object : Subscriber<View>() {
        override fun onError(e: Throwable?) {
            // In case of onError(), just pass it down to the subscriber.
            if (!subscriber.isUnsubscribed) {
                subscriber.onError(e)
            }
        }

        override fun onNext(view: View) {
            // Don't start animation if the subscriber has unsubscribed.
            if (subscriber.isUnsubscribed) return

            // Run the animation.
            numberOfRunningAnimations.incrementAndGet()
            ViewCompat.animate(view)
                    .translationX(translationX)
                    .translationY(translationY)
                    .setDuration(duration)
                    .setInterpolator(interpolator)
                    .withEndAction {
                        numberOfRunningAnimations.decrementAndGet()

                        // Once the animation is done, check if the subscriber is still subscribed
                        // and pass the animated view to onNext().
                        if (!subscriber.isUnsubscribed) {
                            subscriber.onNext(view)

                            // If we received the onComplete() event sometime while the animation was running,
                            // wait until all animations are done and then call onComplete() on the subscriber.
                            if (numberOfRunningAnimations.get() == 0 && isOnCompleteCalled.get()) {
                                subscriber.onCompleted()
                            }
                        }
                    }
        }

        override fun onCompleted() {
            isOnCompleteCalled.set(true)

            // Call onComplete() immediately if all animations are finished.
            if (!subscriber.isUnsubscribed && numberOfRunningAnimations.get() == 0) {
                subscriber.onCompleted()
            }
        }
    }
}