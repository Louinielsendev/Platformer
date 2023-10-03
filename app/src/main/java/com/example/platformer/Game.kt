package com.example.platformer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.os.SystemClock.uptimeMillis
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.random.Random


const val METERS_TO_SHOW_X = 20f
const val METERS_TO_SHOW_Y = 0f
val RNG = Random(uptimeMillis())
const val NANO_TO_SECOND = 1.0f/ 1000000000f

lateinit var engine: Game
class Game(context: Context, attrs: AttributeSet? = null) : SurfaceView(context, attrs), Runnable, SurfaceHolder.Callback {
    private val visibleEntities = ArrayList<Entity>()
    private val stageHeight = getScreenHeight()/2
    private val stageWidth = getScreenWidth()/2
    var inputs = InputManager() // a valid null-controller
    init{
        engine = this
        holder.addCallback(this)
        holder.setFixedSize(stageWidth, stageHeight)
    }
    private val TAG: String = "Game"
    private lateinit var gameThread: Thread
    @Volatile
    private var isRunning = false
    private var isGameOver = false
    val camera = Viewport(stageWidth, stageHeight, METERS_TO_SHOW_X, METERS_TO_SHOW_Y)
    val pool = BitmapPool(this)
    private var levelManager = LevelManager(TestLevel())
    val paint = Paint()
    val transform = Matrix()
    val position = PointF()

    fun worldHeight() = levelManager.levelHeight
    fun worldToScreenX(worldDistance: Float) =  camera.worldToScreenX(worldDistance)
    fun worldToScreenY(worldDistance: Float) =  camera.worldToScreenY(worldDistance)
    //fun screenToWorldX(pixelDistance: Float) = (pixelDistance / pixelsPerMeters).toFloat()
    //fun screenToWorldY(pixelDistance: Float) = (pixelDistance / pixelsPerMeters).toFloat()

    fun getScreenHeight() = context.resources.displayMetrics.heightPixels
    fun getScreenWidth() = context.resources.displayMetrics.widthPixels

    fun setControls(control: InputManager){
        inputs.onPause(); //give the previous controller
        inputs.onStop(); //a chance to clean up
        inputs = control;
        inputs.onStart();
    }
    fun getControls() = inputs

    override fun run() {
        var lastFrame = System.nanoTime()
        while(isRunning){
            val deltaTime = (System.nanoTime() - lastFrame) * NANO_TO_SECOND
            lastFrame = System.nanoTime()
            update(deltaTime)
            buildVisableSet()
            render(visibleEntities)
        }
    }

    private fun buildVisableSet(){
        visibleEntities.clear()
        for (e in levelManager.entities){
            if(camera.inView(e)){
                visibleEntities.add(e)
            }
        }
    }

    private fun update(deltaTime: Float) {
        levelManager.update(deltaTime)
        camera.lookAt(levelManager.player)
    }



    private fun render(visibleSet: ArrayList<Entity>) {

        val canvas = acquireAndLockCanvas() ?: return

        canvas.drawColor(Color.BLUE)

        for (e in visibleSet){
            transform.reset()
            camera.worldToScreen(e, position)
            transform.postTranslate(position.x, position.y)
            e.render(canvas, transform, paint)
        }

        holder.unlockCanvasAndPost(canvas)
    }


    private fun acquireAndLockCanvas() : Canvas?{
        if(holder?.surface?.isValid == false){
            return null
        }

        return holder.lockCanvas()
    }



    fun pause() {
        Log.d(TAG, "Pause")
        inputs.onPause();
        isRunning = false
        try{
            gameThread.join()
        }
        catch(e: Exception){/*swallow exception, we're exiting anyway*/}
    }

    fun resume() {
        isRunning = true
        inputs.onResume();

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(TAG, "surfaceCreated")
        gameThread = Thread(this)
        gameThread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

}