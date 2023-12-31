package com.example.platformer

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint

const val PLAYER_RUN_SPEED = 6.0f
val PLAYER_JUMP_FORCE: Float = -(GRAVITY / 2f)
val LEFT = 1.0f
val RIGHT = -1.0f

class Player(spriteName: String, xpos: Float, ypos: Float) : DynamicEntity(spriteName, xpos, ypos) {
    val TAG = "Player"
    var facing = LEFT

    override fun update(dt: Float) {
        val controls: InputManager = engine.getControls()
        val direction: Float = controls._horizontalFactor
        velX = direction * PLAYER_RUN_SPEED
        facing = getFacingDirection(direction)
        if (controls._isJumping && isOnGround) {
            velY = PLAYER_JUMP_FORCE
            isOnGround = false
        }
        super.update(dt) //parent will integrate our velocity and time with our position
    }

    private fun getFacingDirection(direction: Float): Float{
        if(direction < 0.0f){
            return LEFT
        }else if (direction > 0.0f){
            return RIGHT
        }
        return facing
    }

    override fun render(canvas: Canvas, transform: Matrix, paint: Paint) {
        transform.preScale(facing, 1.0f)
        if (facing == RIGHT){
            val offset = engine.worldToScreenX(width)
            transform.postTranslate(offset, 0.0f)
        }
        super.render(canvas, transform, paint)
    }
}