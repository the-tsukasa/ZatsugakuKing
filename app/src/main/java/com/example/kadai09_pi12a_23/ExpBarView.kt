package com.example.kadai09_pi12a_23

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.FloatRange

/**
 * 经验条独立组件：蓝底 + 黄条（仅内部子 View 宽度按 currentExp/maxExp 变化）+ 外框。
 * 外框由代码绘制（金色描边、透明中间），黄色填充被限制在组件内部。
 */
class ExpBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val track: View
    private val fill: View
    private val frame: View

    private var progress: Int = 0
    private var max: Int = 100

    /** 当前进度值（只读），用于动画起始值等 */
    fun getProgress() = progress
    private var fillWidthPx: Int = 0
    private var animator: ValueAnimator? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_exp_bar, this, true)
        track = findViewById(R.id.exp_bar_track)
        fill = findViewById(R.id.exp_bar_fill)
        frame = findViewById(R.id.exp_bar_frame)
        clipChildren = true
        clipToPadding = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateFillWidth()
    }

    /** 根据 progress/max 更新黄色填充宽度，限制在经验条容器内 */
    private fun updateFillWidth() {
        val totalWidth = width - paddingLeft - paddingRight
        if (totalWidth <= 0 || max <= 0) return
        val ratio = (progress.toFloat() / max).coerceIn(0f, 1f)
        fillWidthPx = (totalWidth * ratio).toInt()
        val params = fill.layoutParams as? LayoutParams ?: return
        params.width = fillWidthPx
        params.height = LayoutParams.MATCH_PARENT
        fill.layoutParams = params
    }

    /**
     * 设置进度，黄色填充宽度 = (progress / max) * 条宽，仅限在组件内部。
     * @param progress 当前值（0..max）
     * @param max 最大值
     */
    fun setProgress(progress: Int, max: Int) {
        this.max = max.coerceAtLeast(1)
        this.progress = progress.coerceIn(0, this.max)
        updateFillWidth()
    }

    /**
     * 设置进度并播放平滑动画。
     * @param fromProgress 起始进度值
     * @param toProgress 目标进度值
     * @param max 最大值（与 setProgress 一致）
     */
    fun setProgressAnimated(fromProgress: Int, toProgress: Int, max: Int) {
        animator?.cancel()
        this.max = max.coerceAtLeast(1)
        if (fromProgress == toProgress) {
            setProgress(toProgress, max)
            return
        }
        animator = ValueAnimator.ofInt(fromProgress, toProgress).apply {
            duration = 300
            addUpdateListener { anim ->
                val value = anim.animatedValue as Int
                this@ExpBarView.progress = value.coerceIn(0, this@ExpBarView.max)
                updateFillWidth()
            }
            start()
        }
    }

    /** 按比例设置进度 [0f..1f]，内部换算为 progress = (ratio * max).toInt() */
    fun setProgressRatio(@FloatRange(from = 0.0, to = 1.0) ratio: Float) {
        val p = (ratio.coerceIn(0f, 1f) * max).toInt()
        setProgress(p, max)
    }

    override fun onDetachedFromWindow() {
        animator?.cancel()
        super.onDetachedFromWindow()
    }
}
