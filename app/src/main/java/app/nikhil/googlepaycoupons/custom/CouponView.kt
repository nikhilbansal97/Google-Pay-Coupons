package app.nikhil.googlepaycoupons.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import app.nikhil.googlepaycoupons.R
import app.nikhil.googlepaycoupons.databinding.LayoutCouponViewBinding
import kotlin.math.abs

class CouponView @JvmOverloads constructor(
  context: Context,
  attributeSet: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {

  companion object {
    private const val STROKE_WIDTH = 20F
    private const val TOUCH_TOLERANCE = 4F
  }

  interface CouponScratchListener {
    fun onCouponScratched()
  }

  private lateinit var scratchPatternCanvas: Canvas
  private lateinit var scratchPatternBitmap: Bitmap
  private val scratchPaint: Paint =
    Paint(Paint.DITHER_FLAG).apply { color = resources.getColor(R.color.colorAccent) }

  private val erasePath: Path = Path()
  private val erasePaint: Paint = Paint()

  private var currentX = 0F
  private var currentY = 0F

  private var shouldInterceptTouchEvents = true
  private var isRewardShown = false
  private var isScratched = false

  private var clickListener: OnClickListener? = null
  private var couponScratchListener: CouponScratchListener? = null

  init {
    LayoutCouponViewBinding.inflate(LayoutInflater.from(context), this, true)
    // Initialize the erase paint
    erasePaint.apply {
      isAntiAlias = true
      isDither = true
      style = Paint.Style.STROKE
      strokeJoin = Paint.Join.BEVEL
      strokeCap = Paint.Cap.ROUND
      xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
      strokeWidth = 6 * STROKE_WIDTH
    }
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)

    if (::scratchPatternBitmap.isInitialized) {
      scratchPatternBitmap.recycle()
    }

    scratchPatternBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    // Create a Canvas object backed by the scratch pattern bitmap.
    scratchPatternCanvas = Canvas(scratchPatternBitmap)
    scratchPatternCanvas.drawRect(0F, 0F, w.toFloat(), h.toFloat(), scratchPaint)
  }

  override fun dispatchDraw(canvas: Canvas) {
    super.dispatchDraw(canvas)
    when {
      isRewardShown || isScratched -> canvas.drawColor(Color.TRANSPARENT)
      else -> canvas.drawBitmap(scratchPatternBitmap, 0F, 0F, scratchPaint)
    }
  }

  /*
  * Intercept all the touch events by returning true instead of passing them
  * to the child views.
  */
  override fun onInterceptTouchEvent(event: MotionEvent?): Boolean = true

  override fun onTouchEvent(event: MotionEvent): Boolean {
    // Do Nothing!
    return when {
      shouldInterceptTouchEvents -> {
        // Get the coordinates of the event
        val x = event.x
        val y = event.y

        // Check for the action of the event
        when (event.action) {
          MotionEvent.ACTION_DOWN -> {
            touchStart(x, y)
            invalidate()
          }
          MotionEvent.ACTION_MOVE -> {
            touchMove(x, y)
            invalidate()
          }
          MotionEvent.ACTION_UP -> {
            touchEnd()
            invalidate()
          }
          else -> {
            // Do Nothing!
          }
        }
        true
      }
      else -> {
        clickListener?.onClick(this)
        false
      }
    }
  }

  override fun setOnClickListener(l: OnClickListener?) {
    shouldInterceptTouchEvents = false
    clickListener = l
    super.setOnClickListener(l)
  }

  private fun touchMove(x: Float, y: Float) {
    val dx = abs(x - currentX)
    val dy = abs(y - currentY)
    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
      erasePath.quadTo(currentX, currentY, (x + currentX) / 2, (y + currentY) / 2)
      currentX = x
      currentY = y
      drawErasePath()
    }
  }

  private fun touchEnd() {
    if (!erasePath.isEmpty)
      couponScratchListener?.onCouponScratched()
    drawErasePath()
  }

  private fun drawErasePath() {
    scratchPatternCanvas.drawPath(erasePath, erasePaint)
    // Reset to avoid double draw
    erasePath.reset()
    erasePath.moveTo(currentX, currentY)
  }

  private fun touchStart(x: Float, y: Float) {
    erasePath.reset()
    erasePath.moveTo(x, y)
    currentX = x
    currentY = y
  }

  fun setCouponScratchListener(listener: CouponScratchListener) {
    this.couponScratchListener = listener
  }

  fun removeCouponScratchListener() {
    this.couponScratchListener = null
  }

  /*
  * Clear the Scratch Pattern Bitmap
  */
  fun showReward() {
    scratchPatternBitmap.recycle()
    isRewardShown = true
    invalidate()
  }
}