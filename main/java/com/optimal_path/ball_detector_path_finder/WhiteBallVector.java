package com.sep.billardapp.example.billard_app_02;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/***
 * Class for vector which will be drawn from white ball at current position to white ball when in contact with
 * colored ball, as well as for vector which will be drawn from colored ball to target hole
 */
public class WhiteBallVector {

    // X and Y coordinates of the target hole
    float targetHoleX, targetHoleY;
    // X and Y coordinates of the colored Ball
    double coloredBallX, coloredBallY;

    // X coordinate of white ball at original position
    double whiteBallX_1, whiteBallY_1;

    float whiteCollideX, whiteCollideY;

    // Radius of ball
    double ballRadius;

    public WhiteBallVector(float targetHoleX, float targetHoleY, double coloredBallX, double coloredBallY, double whiteBallX_1, double whiteBallY_1, double ballRadius) {
        this.targetHoleX = targetHoleX;
        this.targetHoleY = targetHoleY;
        this.coloredBallX = coloredBallX;
        this.coloredBallY = coloredBallY;
        this.whiteBallX_1 = whiteBallX_1;
        this.whiteBallY_1 = whiteBallY_1;
        this.ballRadius = ballRadius;
    }

    /***
     * find where the white ball should be at when colliding with colored ball, in roder to hit colored ball into hole
     */
    public void findCollisionPoint(){
        // wX - rX = 2r(targetX-rX)
        //
        whiteCollideX = (float) (coloredBallX - 2*ballRadius*(targetHoleX-coloredBallX)/Math.sqrt((targetHoleX-coloredBallX)*(targetHoleX-coloredBallX) + (targetHoleY-coloredBallY)*(targetHoleY-coloredBallY)));
        whiteCollideY = (float) (coloredBallX - 2*ballRadius*(targetHoleX-coloredBallX)/Math.sqrt((targetHoleX-coloredBallX)*(targetHoleX-coloredBallX) + (targetHoleY-coloredBallY)*(targetHoleY-coloredBallY)));

        //double whiteVectorX = (whiteCollideX- whiteBallX);
        //double whiteVectorY = (whiteCollideX- whiteBallY);






        //drawVectorToBall(whiteCollideX, whiteCollideY); // better this way or wCollideX, wCollideY as fields?
        //drawVectorToHole(whiteCollideX, whiteCollideY);
    }

    /***
     * startPoint: (whiteBallX, whiteBallY)
     * endPoint: (whiteCollideX, whiteCollideY)
     * draws a vector from original position of whiteBall to where the whiteball should collide with colored ball
     * @param whiteBallX x coordinate of center of where white ball should collide w/ red ball
     *                      in order to hit target hole (when no other balls in the way)
     * @param whiteBallY y coordinate of center of where white ball should collide w/ red ball
     *                      in order to hit target hole (when no other balls in the way)
     */
    public void drawVectorToBall(float whiteBallX, float whiteBallY, Bitmap bitmap, String currentPhotoPath){

        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setStrokeWidth(8);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inSampleSize = 4;

        bitmap = BitmapFactory.decodeFile(currentPhotoPath, options); // **bitmap is the actual (decoded) image code

        Canvas canvas = new Canvas(bitmap);

        canvas.drawLine(whiteBallX, whiteBallY, whiteCollideX, whiteCollideY, p);



    }

    /***
     * startPoint: (whiteCollideX, whiteCollideY)
     * endPoint: (targetHoleX, targetHoleY)
     * draws a vector from chosen colored ball to the target hole
     * @param whiteCollideX x coordinate of center of where white ball should collide w/ red ball
     *                       in order to hit target hole (when no other balls in the way)
     * @param whiteCollideY y coordinate of center of where white ball should collide w/ red ball
     *                       in order to hit target hole (when no other balls in the way)
     */
    public void drawVectorToHole(Hole targethole, Bitmap bitmap, String currentPhotoPath){

        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setStrokeWidth(8);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inSampleSize = 4;

        bitmap = BitmapFactory.decodeFile(currentPhotoPath, options); // **bitmap is the actual (decoded) image code

        Canvas canvas = new Canvas(bitmap);

        targetHoleX = (float) targethole.getX();
        targetHoleY = (float) targethole.getY();

        canvas.drawLine(targetHoleX, targetHoleY, whiteCollideX, whiteCollideY, p);

    }

}
