package com.sep.billardapp.example.billard_app_02;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.sep.billardapp.Helpers.UnionFind;
import com.sep.billardapp.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/***
 * Main class for finding the optimal ball path
 */
public class CameraActivity extends AppCompatActivity {

    float x,y;

    // Factor for how different the colors need to be in order to be considered an edge
    public static final double edgeThreshold=0.3;

    // min circles needed to be in cluster in order to merge cluster into ball
    public static final int partitionThreshold=3;
    private static final Bitmap.Config ARGB_8888 = Bitmap.Config.ARGB_8888;

    //private static final Bitmap.Config ARGB_8888 = Bitmap.Config.ARGB_8888;

    // Button, that user clicks, in order to take a photo
    private Button mPhotoButton;

    // PATH to photo file
    private String currentPhotoPath;

    // bitmap for photo
    public Bitmap bitmap;

    // signal program recieves from user Activity. If sigmal is -1, which is the deafult signal, then the next actions are taken
    private static final int RESULT_OK = -1;
    private static final String TAG = "MainActivity";

    // saves the X (bzw. Y) coordinate of the clicked Colored ball
    float clickedCoordX = -1;
    float clickedCoordY = -1;
    //saves the X (bzw. Y) coordinate of the clicked white ball
    float whiteBallX = -1;
    float whiteBallY = -1; // make on touch for this

    // billard table holes
    ArrayList<Hole> holes = new ArrayList<>();

    // the x, y coords of the chosen ball
    double[] chosenBallCoords;

    // the x, y coords of the white ball
    double[] whiteBallCoords;

    // the vector from whiteball to colored ball, and from colored ball to target billard table hole
    WhiteBallVector wBallVector;

    //ArrayList of balls
    ArrayList<Ball> balls;

    Hole targethole;

    Animation scaleUp, scaleDown;

    // automatically run method by application manager. This runs the onCLick method, when the mPhotoButton is clicked
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mPhotoButton = (Button) findViewById(R.id.button);

        ImageView imageView = findViewById(R.id.imageView);

        System.out.print(whiteBallCoords);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        // Allows user to take photo upon clicking mPhotoButton
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPhotoButton.startAnimation(scaleUp);
                mPhotoButton.startAnimation(scaleDown);

                String fileName = "billard_table_photo";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                try {
                    //saves path to taken photo
                    File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
                    currentPhotoPath = imageFile.getAbsolutePath(); // to get the bitmap

                    // third Parameter, imageFile: the file for which we want the URI
                    Uri imageUri = FileProvider.getUriForFile(CameraActivity.this, "com.example.billard_app_02.fileprovider", imageFile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // takes pic
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 0);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });

        // when user touches somewhere (ideally a colored ball) on the View (ideally photo of Billard Table with balls), a TOAST pops up which notifies the user that the ball has bin touched, and the ball's coordinates are saved.

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                clickedCoordX = event.getX();
                clickedCoordY = event.getY();

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    System.out.println("x-Koordinate: " + clickedCoordX + " y-Koordinate: " + clickedCoordY);
                    Toast.makeText(CameraActivity.this, "Ball ausgewählt", Toast.LENGTH_SHORT).show();

                    // just for test
                /*    Paint p = new Paint();
                    p.setColor(Color.WHITE);
                    p.setStrokeWidth(8);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inMutable = true;
                    options.inSampleSize = 4;

                    bitmap = BitmapFactory.decodeFile(currentPhotoPath, options); // **bitmap is the actual (decoded) image code

                    Canvas canvas = new Canvas(bitmap);
                    //imageView.draw(canvas);

                    canvas.drawLine(0, 100, clickedCoordX, clickedCoordY, p);
                    //drawCirclesOnImage(bitmap);
*/
                   try {
                        double ballRadius = balls.get(0).r;

                        holes = initializeHoles(balls);
                        chosenBallCoords= findCorrespondingBall(balls, clickedCoordX, clickedCoordY);


    //whiteBallCoords= findCorrespondingBall(balls, whiteBallX, whiteBallY);

                        for (Hole h: holes) {
                            h.initializeDistances(chosenBallCoords);
                        }

                        Hole hole = findClosestHole(holes);
                        targethole = chooseTargetHole(hole, ballRadius);

                        wBallVector.findCollisionPoint(); // calls drawVectorToBall and drawVectorToHole, so instantly lines are shown
                    } catch (ArrayIndexOutOfBoundsException ie){
                        System.out.println("No balls recognized!");
                    } catch (Exception e){
                        System.out.println("Error!");
                    }

                   bothBallsClicked();

                }
                return true;
            }
        });



        // make 2nd onTouchListener for white ball click
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                whiteBallX = (int)event.getX();
                whiteBallY = (int)event.getY();

                whiteBallCoords= findCorrespondingBall(balls, whiteBallX, whiteBallY);

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    System.out.println("x-Koordinate: " + whiteBallX + " y-Koordinate: " + whiteBallY);
                    Toast.makeText(CameraActivity.this, " Weißen Ball ausgewählt", Toast.LENGTH_SHORT).show();
                }
                bothBallsClicked();
                return true;
            }
        });
    }


    public boolean bothBallsClicked() {

        if (clickedCoordX != -1 && clickedCoordY != -1) {
            if (whiteBallX != -1 && whiteBallY != -1) {
                wBallVector.drawVectorToBall(whiteBallX, whiteBallY, bitmap, currentPhotoPath);
                wBallVector.drawVectorToHole(targethole, bitmap, currentPhotoPath);
            }
        }
        return true;
    }



    /***
     * Once another activity is done (photo is taken), algorithm within this method runs to recognize and separate balls.
     * Also recognizes and saves the bilalrd table holes, saves which hole user should try to get ball into, and
     * calls the findCollisionPoint() method to draw line of billard table photo so that user can see it and know in which direction to hit ball.
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // not super necc., but matches API and don't need to remember to add the call in the future when the behaviour of the base class changes


        if (requestCode == 0 && resultCode == RESULT_OK) { // RESULT_OK is default code

            bitmap = BitmapFactory.decodeFile(currentPhotoPath); // **bitmap is the actual (decoded) image code

            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap); // set image (bitmap) to the alloted ImageView
        }

        CircleDetector bd = new CircleDetector();
        ArrayList<Circle> circles = new ArrayList<>();
        int tileWidth=16;
        int tileHeight=16;
        for (int x=1; x<bitmap.getWidth()-tileWidth; x+=tileWidth/2){
            for (int y=1; y<bitmap.getHeight()-tileHeight; y+=tileWidth/2){
                Circle circle= processTile(bitmap, tileWidth, tileHeight, bd, x, y);
                if (circle!=null) {
                    circles.add(circle);
                }
            }
        }

        UnionFind<Circle> cs = clusterCircles(circles);
        balls = mergeCircles(cs);  // balls (w/ xyr)
        //optimalWeg(bitmap);

    }

    /***
     * recursive method which returns targethole (and initializes whiteBallVector with it) as soon as there is a minHole
     * without another ball in the way from chosenBall to hole
     * @param minHole the hole with the minimum distance from chosenBall
     * @param radius radius of a ball
     * @return targetHole, which is the hole which would be recommended to the user to hit into
     */
    public Hole chooseTargetHole(Hole minHole, double radius){
        double slopeY = clickedCoordY - minHole.y;
        double slopeX = clickedCoordX - minHole.x;
        double slopeBetweenBallAndHole = slopeY/slopeX;
        int counter=0;
        minHole = findClosestHole(holes);
        if (minHole.y < (slopeBetweenBallAndHole-radius) * minHole.x + chosenBallCoords[0] || minHole.y > (slopeBetweenBallAndHole+radius)* minHole.x + chosenBallCoords[0]){
            wBallVector = new WhiteBallVector((float)minHole.getX(), (float)minHole.getY(), chosenBallCoords[0], chosenBallCoords[1], whiteBallCoords[0], whiteBallCoords[1], radius);
        } else {
            minHole = findClosestHole(holes);
            counter++;
            if (!(counter <6))
                return chooseTargetHole(minHole, radius);
        }
        return findClosestHole(holes);
    }

    /***
     * find hole closest to chosen ball (their distance to ball already set through initializeDistances method)
     * @param holes
     * @return
     */
    public Hole findClosestHole(ArrayList<Hole> holes){
        double minDist = 100000000;
        Hole minHole = null;
        for (Hole h: holes){
            if (h.dist<minDist){
                minDist=h.dist;
                minHole=h;
            }
        }
        holes.remove(minHole); // so that in next loop this hole isn't considered anymore
        return minHole;
    }

    /***
     * finds which balls are holes
     * @param balls balls contain balls which are candidates to be holes
     * @return Array List of holes
     */
    public ArrayList<Hole> initializeHoles(ArrayList<Ball> balls) {
        double[] xCoords = new double[50];
        ArrayList<Ball> sortedBalls = new ArrayList<>();
        ArrayList<Hole> holes = new ArrayList<>();
        for (int i = 0; i < balls.size(); i++) {
            double x = balls.get(i).x;
            xCoords[i] = x;
        }

        // print before (for test)
        int n2 = xCoords.length;
        for (int i = 0; i < n2; ++i)
            System.out.print(xCoords[i] + " ");
        System.out.println();

        int n = xCoords.length;
        // sort coords from least to greatest
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (xCoords[j] > xCoords[j + 1]) {
                    // swap arr[j+1] and arr[j]
                    double temp1 = xCoords[j];
                    xCoords[j] = xCoords[j + 1];
                    xCoords[j + 1] = temp1;
                }
            }
        }

        // print after (for test)
        int n3 = xCoords.length;
        for (int i = 0; i < n3; ++i)
            System.out.print(xCoords[i] + " ");
        System.out.println();

        for (int i = 0; i < xCoords.length; i++) {
            if (xCoords[i]==balls.get(i).x){
                sortedBalls.add(balls.get(i));
            }
        }
        //save balls w/ smallest 3 and largest 3 x coords as holes
        int counter=0;
        for (Ball b: sortedBalls){
            if (counter<3) {
                Hole hole = new Hole(b.x, b.y);
                holes.add(hole);
            }

            if (counter > sortedBalls.size()-3){
                Hole hole = new Hole(b.x, b.y);
                holes.add(hole);
            }
            counter++;
        }

        return holes;
    }

    // finds the ball corresponding to the place where user clicked
    private double[] findCorrespondingBall(ArrayList<Ball> balls, double x, double y) {
        double distance=0;
        double minDistance=100000000;
        double matchingX = 0;
        double matchingY = 0;
        for (int i = 0; i< balls.size(); i++){
            distance= calcEukDistance(balls.get(i).x-clickedCoordX, balls.get(i).y-clickedCoordY);
            if (distance<minDistance) {
                minDistance = distance;
                matchingX = balls.get(i).x;
                matchingY = balls.get(i).y;
            }
        }

        double[] chosebBallsCoords = {matchingX, matchingY};
        return chosebBallsCoords;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void drawCirclesOnImage(Bitmap img) {
        //DisplayMetrics dm = new DisplayMetrics();
        //Bitmap imgMutable = img.createBitmap (dm, img.getWidth(), img.getHeight(), ARGB_8888);
        Canvas g = new Canvas(img);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(8);
        //for(Circle c: balls) {
            g.drawOval(
                    300,
                    350,
                    400, 250, paint
            );
        //}
    }

    /***
     * Circle class for circles. Saves the center x coordinate, center y coordinate, and radius of the
     * detected circles
     */
    static class Circle{
        double x, y, r;

        public Circle(double x, double y, double r) {
            this.x = x;
            this.y = y;
            this.r = r;
        }
    }

    /***
     * merges circles clusters together containing more balls than the partitionThreshold into ball, and calculates
     * and saves the balls' centers' x and y coordinate as well as radius
     * @Param cs clusters of circles, which are candidates to becoming balls
     */
    private static ArrayList<Ball> mergeCircles(UnionFind<Circle> cs) {

        ArrayList<Ball> balls = new ArrayList<>();
        for (Set<Circle> partition: cs.partitions()) {

            if(partition.size()<partitionThreshold)
                continue;

            double sumX = 0;
            double sumY = 0;
            double r=0;
            for (Circle c : partition) {
                sumX += c.x;
                sumY += c.y;
            }
            double averageCenterX=sumX/partition.size(); // avg centerX
            double averageCenterY=sumY/partition.size(); // avg centerY

            for (Circle c: partition){
                double xDiff= averageCenterX-c.x;
                double yDiff= averageCenterY-c.y;
                double centerDiff = calcEukDistance(xDiff, yDiff);
                double newRadiusCandidate = c.r+centerDiff;
                if (r<newRadiusCandidate)
                    r=newRadiusCandidate;
            }
            Ball ball = new Ball(averageCenterX, averageCenterY, r);
            balls.add(ball);
        }

        return balls;
    }

    /***
     * clusters circles near enough to eachother
     * @param circles circles which are candidates to become clustered
     * @return clusters of circles, which are candidates to becoming balls
     */
    private static UnionFind<Circle> clusterCircles(ArrayList<Circle> circles) {
        UnionFind uInstance = new UnionFind();
        for(Circle c1: circles) {
            for (Circle c2 : circles) {
                double xDiff= c1.x-c2.x;
                double yDiff= c1.y-c2.y;
                double distance = calcEukDistance(xDiff, yDiff);
                if(distance<10)
                    uInstance.union(c1,c2); //Call the union method for creating union for each similar circles
            }
        }
        return uInstance;
    }

    /***
     * calculates the 2D euclidian distance between two distances, distance1 and distance2.
     * @param distance1 distance from x1 to x2
     * @param distance2 distance from y1 to y2
     * @return
     */
    public static double calcEukDistance(double distance1, double distance2){
        double distance = Math.sqrt(distance1*distance1 + distance2*distance2);
        return distance;
    }

    /***
     * goes through each pixel of tile, and checks whether the difference between two pixels are large enough.
     * The difference between two pixels are weighted by the radius
     * @param img the image's bitmap
     * @param tileWx the x width of tile
     * @param tileWy the y width of tile
     * @param bd the CircleDetector instance
     * @param x0 the starting x pixel
     * @param y0 the starting y pixel
     * @return circle (ball candidates)
     */
    private static Circle processTile(Bitmap img, int tileWx, int tileWy, CircleDetector bd, int x0, int y0) {
        bd.reset();
        for (int x=x0; x<x0+tileWx-1; x++){
            for (int y=y0; y<y0+tileWy-1;y++){
                int c1=img.getPixel(x,y);
                int c2=img.getPixel(x-1,y);
                int c3=img.getPixel(x,y-1);

                double colorDiff = (colorDifference(c1, c2));

                if (colorDiff>edgeThreshold) {
                    double weight = Math.pow(colorDiff, 4);
                    bd.add(x - 0.5, y, weight);
                }

                colorDiff = (colorDifference(c1, c3));
                if (colorDiff>edgeThreshold) {
                    double weight = Math.pow(colorDiff, 4);
                    bd.add(x, y - 0.5, weight);
                }
            }
        }

        if(bd.counter>8) {
            //System.out.println("radius value: " + bd.r);
            bd.fit();
            if (bd.r > 5 & bd.r < 50) {
                return new Circle(bd.cx, bd.cy, bd.r);
            }
        }

        return null;
    }

    /***
     * calculates the color difference between int c1 and int c2
     * @param c1 int value of color1
     * @param c2 int caluve of color2
     * @return difference between two colors
     */
    private static double colorDifference(int c1, int c2) {
        int redC1 = (c1 >> 16)&0xFF;
        int greenC1 = (c1 >> 8)&0xFF;
        int blueC1 = c1 &0xFF;

        int redC2 = (c2 >> 16)&0xFF;
        int greenC2 = (c2 >> 8)&0xFF;
        int blueC2 = c2 &0xFF;


        double difference = Math.sqrt((redC2-redC1)*(redC2-redC1) + (greenC2-greenC1)*(greenC2-greenC1) + (blueC1-blueC2)*(blueC1-blueC2));  // redo for euk distance of 3D
        return difference/255.0;
    }
}






