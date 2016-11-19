package njain.com.receipter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Camera mCamera;         // object to access hardware camera
    private CameraView mCameraView; // preview of camera feed

    Bitmap pictureTaken;          // bitmap that holds the image

    private Button mPictureButton;          // button that takes pictures

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mCamera = Camera.open();
        mCameraView = new CameraView(this);
//        mCameraView.setOnTouchListener(this);

        setContentView(mCameraView);





    } // end of onCreate


    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // TAKE PICTURE CLASS

    private class TakePictureAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            Camera.PictureCallback jpegPictureCallback = new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {


                    Bitmap pictureBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    // Create a rotated bitmap
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);

                    Bitmap rotatedBitmap = Bitmap.createBitmap(pictureBitmap , 0, 0, pictureBitmap.getWidth(),
                            pictureBitmap.getHeight(), matrix, true);

                    pictureTaken = rotatedBitmap;


                }
            };

            // takes the picture

            mCamera.takePicture(null, null, null, jpegPictureCallback);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // CUSTOM VIEW TO SHOW CAMERA

    // extending SurfaceView to render the camera images
    private class CameraView extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;

        public CameraView(Context context) {
            super(context);

            mHolder = this.getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            setFocusable(true);

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            try {
                mCamera.setDisplayOrientation(90); // set proper orientation (got rid of stretching)
                mCamera.setPreviewDisplay(mHolder);


            } catch (IOException e) {
                mCamera.release();
            }

            mCamera.startPreview();

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

            mCamera.stopPreview();
            mCamera.release();

        }

    } // end of CameraView class



}
