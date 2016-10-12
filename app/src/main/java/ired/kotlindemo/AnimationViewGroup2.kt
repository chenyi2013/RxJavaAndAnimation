package ired.kotlindemo

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import rx.Completable

/**
 * Created by kevin on 16/10/12.
 */
class AnimationViewGroup2 : FrameLayout {

    // Holds current menu items.
    private var currentItems = mutableListOf<FloatingActionButton>()

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

        for (x in 0..5) {
            var button: FloatingActionButton = FloatingActionButton(context);
            button.z = 0.0f;
            currentItems.add(button);
            button.setImageResource(R.drawable.right_arrow);
            button.compatElevation = 0.0f;
            addView(button);
        }
    }


    fun startAnimation() {
        expandMenuItemsHorizontally(currentItems).subscribe()
    }

    fun reverseAnimation() {
        collapseMenuItemsHorizontally(currentItems).subscribe()
    }


    fun expandMenuItemsHorizontally(items: MutableList<FloatingActionButton>): Completable =
            Completable.create(ExpandViewsOnSubscribe(items, ExpandViewsOnSubscribe.AnimationType.EXPAND_HORIZONTALLY, 300L, AccelerateDecelerateInterpolator(), 32))

    fun collapseMenuItemsHorizontally(items: MutableList<FloatingActionButton>): Completable =
            Completable.create(ExpandViewsOnSubscribe(items, ExpandViewsOnSubscribe.AnimationType.COLLAPSE_HORIZONTALLY, 300L, AccelerateDecelerateInterpolator(), 32))

    fun rotateMenuItemsBy90(items: MutableList<FloatingActionButton>): Completable =
            Completable.create(RotateViewsOnSubscribe(items, RotateViewsOnSubscribe.AnimationType.ROTATE_TO_90, 300L, DecelerateInterpolator()));

    fun rotateMenuItemsToOriginalPosition(items: MutableList<FloatingActionButton>): Completable =
            Completable.create(RotateViewsOnSubscribe(items, RotateViewsOnSubscribe.AnimationType.ROTATE_TO_0, 300L, DecelerateInterpolator()))

    fun scaleDownMenuItems(items: MutableList<FloatingActionButton>): Completable =
            Completable.create(ScaleViewsOnSubscribe(items, ScaleViewsOnSubscribe.AnimationType.SCALE_DOWN, 400L, DecelerateInterpolator()))

    fun scaleUpMenuItems(items: MutableList<FloatingActionButton>): Completable =
            Completable.create(ScaleViewsOnSubscribe(items, ScaleViewsOnSubscribe.AnimationType.SCALE_UP, 400L, DecelerateInterpolator()))

    fun removeMenuItems(items: MutableList<FloatingActionButton>): Completable = Completable.fromAction {
        removeAllViews()
    }


    fun addItemsScaledDownAndRotated(items: MutableList<FloatingActionButton>): Completable = Completable.fromAction {
        this.currentItems = items
        for (item in items) {
            item.scaleX = 0.0f
            item.scaleY = 0.0f
            item.rotation = 90f
            item.setImageResource(R.drawable.square_72px)
            addView(item)
        }
    }


    fun setMenuItems(newItems: MutableList<FloatingActionButton>) {
        collapseMenuItemsHorizontally(currentItems)
                .andThen(rotateMenuItemsBy90(currentItems))
                .andThen(scaleDownMenuItems(currentItems))
                .andThen(removeMenuItems(currentItems))
                .andThen(addItemsScaledDownAndRotated(newItems))
                .andThen(scaleUpMenuItems(newItems))
                .andThen(rotateMenuItemsToOriginalPosition(newItems))
                .andThen(expandMenuItemsHorizontally(newItems))
                .subscribe()
    }

}