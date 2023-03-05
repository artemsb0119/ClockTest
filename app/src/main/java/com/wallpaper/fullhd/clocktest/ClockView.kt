package com.wallpaper.fullhd.clocktest

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import java.lang.Float.min

import java.util.*
import kotlin.math.min


class ClockView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val TAG = "ClockView"

    private var mWidth = 0
    private var mHeight = 0
    private var mPadding = 0
    private var mFontSize = 0
    private var mBorderColor = Color.BLACK
    private var mBorderWidth = 0f
    private var mHourHandColor = Color.BLACK
    private var mMinuteHandColor = Color.BLACK
    private var mSecondHandColor = Color.BLACK
    private var mHourTextColor = Color.BLACK
    private var mMarkingColor = Color.BLACK
    private var mClockColor = Color.WHITE
    private var mHourHandWidth = 0f
    private var mMinuteHandWidth = 0f
    private var mSecondHandWidth = 0f
    private var mMarkingWidth = 0f
    private var mCenterX = 0
    private var mCenterY = 0
    private var mRadius = 0
    private var mFontSizeScale = 0.08f
    private var mSecondHandLength = 0f
    private var mMinuteHandLength = 0f
    private var mHourHandLength = 0f
    private var mCenterRadius = 0f

    private val mHourPaint = Paint()
    private val mMinutePaint = Paint()
    private val mSecondPaint = Paint()
    private val mTextPaint = Paint()
    private val mMarkingPaint = Paint()
    private val mBorderPaint = Paint()
    private val mBackgoundPaint = Paint()

    fun setHourHandColor(color: Int) {
        this.mHourHandColor = color
        invalidate()
    }

    fun setMinuteHandColor(color: Int) {
        this.mMinuteHandColor = color
        invalidate()
    }

    fun setSecondHandColor(color: Int) {
        this.mSecondHandColor = color
        invalidate()
    }

    fun setBorderColor(color: Int) {
        this.mBorderColor = color
        invalidate()
    }

    fun setHourTextColor(color: Int) {
        this.mHourTextColor = color
        invalidate()
    }

    fun setBorderWidth(borderWidth: Float) {
        this.mBorderWidth = borderWidth
        invalidate()
    }


    private val mCalendar: Calendar = Calendar.getInstance()

    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_UPDATE_TIME -> {
                    mCalendar.timeInMillis = System.currentTimeMillis()
                    invalidate()
                    sendEmptyMessageDelayed(MSG_UPDATE_TIME, 1000)
                }
            }
        }
    }

    companion object {
        private const val MSG_UPDATE_TIME = 1
    }

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ClockView,
            0, 0
        )

        try {
            mHourHandColor =
                typedArray.getColor(R.styleable.ClockView_hourHandColor, Color.BLACK)
            mMinuteHandColor =
                typedArray.getColor(R.styleable.ClockView_minuteHandColor, Color.BLACK)
            mSecondHandColor =
                typedArray.getColor(R.styleable.ClockView_secondHandColor, Color.BLACK)
            mHourTextColor =
                typedArray.getColor(R.styleable.ClockView_hourTextColor, Color.BLACK)
            mMarkingColor =
                typedArray.getColor(R.styleable.ClockView_markingColor, Color.BLACK)
            mClockColor =
                typedArray.getColor(R.styleable.ClockView_clockColor, Color.WHITE)
            mHourHandWidth = typedArray.getFloat(R.styleable.ClockView_hourHandWidth, 5f)
            mSecondHandWidth = typedArray.getFloat(R.styleable.ClockView_secondHandWidth, 2f)
            mMarkingWidth = typedArray.getFloat(R.styleable.ClockView_markingWidth, 1f)
            mFontSizeScale = typedArray.getFloat(R.styleable.ClockView_fontSizeScale, 0.1f)
            mBorderColor = typedArray.getColor(R.styleable.ClockView_borderColor, Color.BLACK)
            mBorderWidth = typedArray.getDimension(R.styleable.ClockView_borderWidth, 40f)
        } finally {
            typedArray.recycle()
        }

        mBackgoundPaint.isAntiAlias = true
        mBackgoundPaint.color = mClockColor
        mBackgoundPaint.style = Paint.Style.FILL

        mHourPaint.isAntiAlias = true
        mHourPaint.strokeWidth = mHourHandWidth
        mHourPaint.color = mHourHandColor
        mHourPaint.strokeCap = Paint.Cap.ROUND
        mHourPaint.style = Paint.Style.STROKE

        mMinutePaint.isAntiAlias = true
        mMinutePaint.strokeWidth = mMinuteHandWidth
        mMinutePaint.color = mMinuteHandColor
        mMinutePaint.strokeCap = Paint.Cap.ROUND
        mMinutePaint.style = Paint.Style.STROKE

        mSecondPaint.isAntiAlias = true
        mSecondPaint.strokeWidth = mSecondHandWidth
        mSecondPaint.color = mSecondHandColor
        mSecondPaint.strokeCap = Paint.Cap.ROUND
        mSecondPaint.style = Paint.Style.STROKE

        mTextPaint.isAntiAlias = true
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = mFontSize.toFloat()
        mTextPaint.color = mHourTextColor
        mTextPaint.typeface = Typeface.createFromAsset(context.assets, "tupo-vyaz_bold.ttf")

        mMarkingPaint.isAntiAlias = true
        mMarkingPaint.strokeWidth = mMarkingWidth
        mMarkingPaint.color = mMarkingColor
        mMarkingPaint.strokeCap = Paint.Cap.ROUND
        mMarkingPaint.style = Paint.Style.STROKE

        mBorderPaint.isAntiAlias = true
        mBorderPaint.strokeWidth = mBorderWidth
        mBorderPaint.color = mBorderColor
        mBorderPaint.style = Paint.Style.STROKE

        mHandler.sendEmptyMessage(MSG_UPDATE_TIME)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mWidth = measuredWidth
        mHeight = measuredHeight
        mPadding = Math.min(mWidth, mHeight) / 20
        mRadius = Math.min(mWidth, mHeight) / 2 - mPadding
        mFontSize = (mRadius * mFontSizeScale).toInt()
        mCenterX = mWidth / 2
        mCenterY = mHeight / 2

        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h

        val centerX = w / 2f
        val centerY = h / 2f
        mRadius = (Math.min(centerX, centerY) - mPadding).toInt()

        mHourHandLength = 0.5f * mRadius * 0.0017f
        mMinuteHandLength = 0.7f * mRadius * 0.0017f
        mSecondHandLength = 0.9f * mRadius * 0.0017f

        mCenterRadius = mRadius.toFloat() / 12

        mHourPaint.textSize = mFontSizeScale * mRadius
        mTextPaint.textSize = 1.2f*mFontSizeScale * mRadius
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(mWidth / 2f, mHeight / 2f, (min(mWidth, mHeight) / 2f - mPadding), mBackgoundPaint)
        drawMarkings(canvas)
        drawMarkingsHours(canvas)
        drawNumbers(canvas)
        drawHourHand(canvas)
        drawMinuteHand(canvas)
        drawSecondHand(canvas)
        drawCenter(canvas)
        canvas.drawCircle(mWidth / 2f, mHeight / 2f, mRadius.toFloat(), mBorderPaint)

    }

    private fun drawMarkings(canvas: Canvas) {
        for (i in 0..59) {
            val angle = Math.PI / 30 * i
            val startX = (mCenterX + (mRadius - 0.5 * mPadding) * Math.sin(angle)).toFloat()
            val startY = (mCenterY - (mRadius - 0.5 * mPadding) * Math.cos(angle)).toFloat()
            val stopX = (mCenterX + (mRadius - mPadding) * Math.sin(angle)).toFloat()
            val stopY = (mCenterY - (mRadius - mPadding) * Math.cos(angle)).toFloat()
            canvas.drawLine(startX, startY, stopX, stopY, mMarkingPaint)
        }
    }

    private fun drawMarkingsHours(canvas: Canvas) {
        for (i in 0..11) {
            val angle = Math.PI / 6 * i
            val startX = (mCenterX + (mRadius - 1.5 * mPadding) * Math.sin(angle)).toFloat()
            val startY = (mCenterY - (mRadius - 1.5 * mPadding) * Math.cos(angle)).toFloat()
            val stopX = (mCenterX + (mRadius - mPadding) * Math.sin(angle)).toFloat()
            val stopY = (mCenterY - (mRadius - mPadding) * Math.cos(angle)).toFloat()
            canvas.drawLine(startX, startY, stopX, stopY, mMarkingPaint)
        }
    }

    private fun drawNumbers(canvas: Canvas) {
        for (i in 1..12) {
            val numberAngle = Math.PI / 6 * (i - 3)
            val numberX = (mCenterX + (mRadius - mPadding - mFontSize) * Math.cos(numberAngle)).toFloat()
            val numberY = (mCenterY + (mRadius - mPadding - mFontSize) * Math.sin(numberAngle)).toFloat()
            canvas.drawText(i.toString(), numberX, numberY + mFontSize / 2, mTextPaint)
        }
    }
    private fun drawHourHand(canvas: Canvas) {
        val hours = mCalendar.get(Calendar.HOUR_OF_DAY) % 12
        val angle = Math.PI * ((hours + mCalendar.get(Calendar.MINUTE) / 60.0) / 6.0 - 0.5)
        val handLength = mRadius * mHourHandLength
        val handX = (mCenterX + handLength * Math.cos(angle)).toFloat()
        val handY = (mCenterY + handLength * Math.sin(angle)).toFloat()
        canvas.drawLine(mCenterX.toFloat(), mCenterY.toFloat(), handX, handY, mHourPaint)
    }

    private fun drawMinuteHand(canvas: Canvas) {
        val minutes = mCalendar.get(Calendar.MINUTE)
        val angle = Math.PI * (minutes / 30.0 - 0.5)
        val handLength = mRadius * mMinuteHandLength
        val handX = (mCenterX + handLength * Math.cos(angle)).toFloat()
        val handY = (mCenterY + handLength * Math.sin(angle)).toFloat()
        canvas.drawLine(mCenterX.toFloat(), mCenterY.toFloat(), handX, handY, mMinutePaint)
    }

    private fun drawSecondHand(canvas: Canvas) {
        val seconds = mCalendar.get(Calendar.SECOND)
        val angle = Math.PI * (seconds / 30.0 - 0.5)
        val handLength = mRadius * mSecondHandLength
        val handX = (mCenterX + handLength * Math.cos(angle)).toFloat()
        val handY = (mCenterY + handLength * Math.sin(angle)).toFloat()
        canvas.drawLine(mCenterX.toFloat(), mCenterY.toFloat(), handX, handY, mSecondPaint)
    }

    private fun drawCenter(canvas: Canvas) {
        canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mCenterRadius.toFloat(), mSecondPaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeMessages(MSG_UPDATE_TIME)
    }
}