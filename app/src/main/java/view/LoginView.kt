package view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.hontech.icecreamutilapp.R
import util.log

class LoginView : View
{
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var radius = 0f
    private var xc = 0f
    private var yc = 0f
    private val srcPath = Path()
    private val destPath = Path()
    private val pathMeasure = PathMeasure()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var pathLength = 0f

    private var index = 0f
        set(value)
        {
            field = value
            stopIndex = pathLength * value
            startIndex = (stopIndex - ( (0.5f - Math.abs(value - 0.5f)) * pathLength) )
            invalidate()
        }

    private var startIndex = 0f
    private var stopIndex = 0f

    private val objectAnimator = ObjectAnimator.ofFloat(this, "index", 0f, 1f)

    init
    {
        objectAnimator.duration = 1000
        objectAnimator.repeatMode = ObjectAnimator.RESTART
        objectAnimator.repeatCount = ObjectAnimator.INFINITE
        objectAnimator.interpolator = LinearInterpolator()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean)
    {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus)
        {
            objectAnimator.start()
        }
    }

    override fun onWindowVisibilityChanged(visibility: Int)
    {
        super.onWindowVisibilityChanged(visibility)
        if (visibility != VISIBLE)
        {
            objectAnimator.cancel()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int)
    {
        xc = w / 2f
        yc = h / 2f
        val a = if (w < h) w / 2 else h / 2

        radius = a / (1 + 0.5f * 0.15f)

        srcPath.addCircle(xc, yc, radius, Path.Direction.CW)

        paint.color = resources.getColor(R.color.colorAccent)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = a * 0.15f
        paint.strokeCap = Paint.Cap.ROUND

        pathMeasure.setPath(srcPath, true)
        pathLength = pathMeasure.length
    }

    override fun onDraw(canvas: Canvas)
    {
        destPath.reset()
        pathMeasure.getSegment(startIndex, stopIndex, destPath, true)
        canvas.drawPath(destPath, paint)
    }
}