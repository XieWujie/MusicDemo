package com.example.xiewujie.musicdemo.view.myview



import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class PlayProgressView:View{

    val data = ProgressData()
    val paintWidth = dp2px(2)
    val radius = dp2px(5)
    lateinit var bPaint: Paint
    lateinit var fPaint: Paint
    lateinit var rPaint: Paint
    val positionMap = HashMap<Int,Float>()
    var barLeft = 0f
    var barY = 0f
    var barRight = 0f
    var barWidth = 0f
    var positionSpace = 1f
    var positionListener:PositionListener? = null

    constructor(context: Context):super(context){
        initPaint()
    }
    constructor(context: Context,attr:AttributeSet):super(context,attr){
        initPaint()
    }

   private fun initPaint(){
        bPaint = Paint()
        bPaint.color = Color.parseColor("#66999999")
        bPaint.strokeWidth = paintWidth
        bPaint.style = Paint.Style.FILL
        fPaint = Paint()
        fPaint.color = Color.parseColor("#21c250")
        fPaint.style = Paint.Style.FILL
        fPaint.strokeWidth = paintWidth
        rPaint = Paint()
        rPaint.style = Paint.Style.FILL
        rPaint.color = Color.parseColor("#ffffff")
    }

    private fun drawLine(canvas: Canvas){
        canvas.drawLine(barLeft,barY,data.position,barY,fPaint)
        canvas.drawLine(data.position,barY,barRight,barY,bPaint)
        canvas.drawCircle(data.position,barY,radius,rPaint)
    }

    fun start(nowTime:Int){
        post {
            data.playTime = nowTime
            if (positionMap.containsKey(nowTime)){
                data.position = positionMap[nowTime]!!
            }
            invalidate()
        }

    }

    fun initData(allTime: Int){
        post {
            data.allTime = allTime
            loadPosition(allTime)
        }
    }

    fun loadPosition(allTime: Int){
        for (i in 0..allTime){
            positionMap[i] = barLeft+i*barWidth/data.allTime
        }
        positionSpace = barWidth/allTime

    }

    private fun initBar(){
        val hMargin = measuredWidth/10f
        barLeft = hMargin
        barRight = measuredWidth - hMargin
        barY = measuredHeight/2f
        data.oldPosition = barLeft
        data.position = barLeft
        barWidth = barRight - barLeft
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        initBar()
    }

    override fun onDraw(canvas: Canvas?) {
        drawLine(canvas!!)
    }
    fun addPositionListener(listener: PositionListener){
        if (positionListener==null){
            positionListener = listener
        }
    }
    private fun dp2px(dp:Int):Float{
        val density = resources.displayMetrics.density
        return density*dp
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event!=null){
            when(event.action){
                MotionEvent.ACTION_DOWN,MotionEvent.ACTION_MOVE,MotionEvent.ACTION_UP
                ->{
                    if (event.x in barLeft..barRight) {
                        val second = getSecondFromPosition(event.x)
                        positionListener?.callBack(second)
                    }
                }

            }
        }
        return super.onTouchEvent(event)
    }

    fun getSecondFromPosition(position: Float):Int{

        return ((position-barLeft)/positionSpace).toInt()
    }

    data class ProgressData(
            var playTime:Int= 0,
            var allTime:Int= 0,
            var position:Float = 0f,
            var oldPosition:Float = 0f
    )

    interface PositionListener{
        fun callBack(second:Int)
    }
}