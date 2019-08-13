package com.dastanapps.dastanlib.customUI

import android.animation.ObjectAnimator
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import kotlin.math.abs

/**
 * An OnGlobalLayoutListener interface that will adjust and resize a fullscreen scene's view when the soft keyboard is shown
 * or hidden.
 *
 * Provides custom layout behavior to the 'adjustResize' windowSoftInputMode for a View layout by enforcing an adjustable
 * container with the option of a static footer view. The adjustable container is "adjusted" by increasing or decreasing the
 * view's translationY property equal to the distance of the adjustable view that was obscured by the soft keyboard.
 *
 * When adhering to this interface, be sure to set the relevant Activity's windowSoftInputMode to 'adjustResize' so that the
 * onGlobalLayout method is called when the keyboard is shown and dismissed.
 *
 * Created by Ryan Taylor on 2/2/16.
 */
class AdjustingViewGlobalLayoutListener(private val mAdjustableView: View) : ViewTreeObserver.OnGlobalLayoutListener {
    private var mOldVisibleFrameBottom: Int = 0
    private val mVisibleWindowFrameBottom: Int
    private val mSoftKeyboardEstimatedMinHeight: Int

    init {

        val visibleWindowFrame = Rect()
        mAdjustableView.getWindowVisibleDisplayFrame(visibleWindowFrame)
        mVisibleWindowFrameBottom = visibleWindowFrame.bottom
        mSoftKeyboardEstimatedMinHeight = mVisibleWindowFrameBottom / 4
    }

    override fun onGlobalLayout() {
        val visibleWindowRect = Rect()
        mAdjustableView.getWindowVisibleDisplayFrame(visibleWindowRect)

        val adjustableViewCoords = intArrayOf(0, 0)
        mAdjustableView.getLocationOnScreen(adjustableViewCoords)
        val adjustableViewBottom = adjustableViewCoords[1] + mAdjustableView.height
        val deltaVisibleWindowBottom = mOldVisibleFrameBottom - visibleWindowRect.bottom
        var translationYAnimator: ObjectAnimator? = null

        if (Math.abs(deltaVisibleWindowBottom) > mSoftKeyboardEstimatedMinHeight) {
            // The visible height of the window has changed significantly enough
            // to suggest that the soft keyboard has been shown or hidden.
            val softKeyboardVisible = deltaVisibleWindowBottom > 0
            // Subtract the height of the footer from the height of the soft keyboard to get the adjustment length
            val adjustmentLength = deltaVisibleWindowBottom - (mVisibleWindowFrameBottom - adjustableViewBottom)

            if (softKeyboardVisible) {
                translationYAnimator = ObjectAnimator.ofFloat(mAdjustableView, "translationY", -adjustmentLength.toFloat())
            } else if (abs(deltaVisibleWindowBottom) > (mAdjustableView.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin) {
                // The soft keyboard has been hidden.
                translationYAnimator = ObjectAnimator.ofFloat(mAdjustableView, "translationY", 0f)
            }
        } else if (mVisibleWindowFrameBottom - visibleWindowRect.bottom > mSoftKeyboardEstimatedMinHeight && deltaVisibleWindowBottom != 0) {
            // The soft keyboard is most likely still visible, and an attachment, such as the text suggestion bar,
            // was added to the soft keyboard.
            translationYAnimator = ObjectAnimator.ofFloat(mAdjustableView, "translationY", -deltaVisibleWindowBottom + mAdjustableView.translationY)
        }

        translationYAnimator?.start()
        mOldVisibleFrameBottom = visibleWindowRect.bottom
    }
}