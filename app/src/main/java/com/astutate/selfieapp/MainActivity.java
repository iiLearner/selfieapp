package com.astutate.selfieapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;

public class MainActivity extends AppCompatActivity {

    /* global vars */
    private CameraView camera;
    private ImageView imageCapture, imageWatermark, imageSend, imageAgain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* acquire all the view items */
        imageCapture = findViewById(R.id.capture);
        imageWatermark = findViewById(R.id.watermark);
        imageSend = findViewById(R.id.send);
        imageAgain = findViewById(R.id.tryagain);

        //initialize the camera
        camera = findViewById(R.id.camera);
        camera.setLifecycleOwner(this);


        /* what happens when user clicks on the *take picture* button */
        imageCapture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //async function. result is sent to *onPictureTaken*
                camera.takePicture();
            }
        });

        /* what happens when user clicks on the *try again* button */
        imageAgain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                imageSend.setVisibility(View.GONE);
                imageAgain.setVisibility(View.GONE);
                camera.open();
                imageCapture.setVisibility(View.VISIBLE);
                imageWatermark.setVisibility(View.VISIBLE);
            }
        });

        /* what happens when user clicks on the *send image* button */
        imageSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //closing the camera to have the image locked on what was captured and then showing and hiding certain view
                camera.close();
                imageCapture.setVisibility(View.GONE);
                imageWatermark.setVisibility(View.GONE);
                imageSend.setVisibility(View.GONE);
                imageAgain.setVisibility(View.GONE);

                //redirect user to the capture activity where the image is sent to the server and the processing screen is showed to the user
                startActivity(new Intent(getApplication(), captureActivity.class));
            }
        });


        //camera listeners. we'll only add the *onPictureTaken* listener since thats all we need
        camera.addCameraListener(new CameraListener() {

            @Override
            public void onPictureTaken(@NonNull final PictureResult result) {

                //get the bytes from the picture taken and convert it to base64
                byte[]  bytes = result.getData();
                Appdata.setEncodedData(Base64.encodeToString(bytes, Base64.DEFAULT));

                //close to camera to have it locked to what was just captured
                camera.close();

                //show/hide the UI buttons
                imageCapture.setVisibility(View.GONE);
                imageWatermark.setVisibility(View.GONE);
                imageSend.setVisibility(View.VISIBLE);
                imageAgain.setVisibility(View.VISIBLE);
            }

        });

    }


    //when the user closes the phone or switches to another app and comes back we want to open the camera for them and show/hide the UI items accordingly
    @Override
    protected void onResume() {
        super.onResume();
        imageCapture.setVisibility(View.VISIBLE);
        imageWatermark.setVisibility(View.VISIBLE);
        imageSend.setVisibility(View.GONE);
        imageAgain.setVisibility(View.GONE);
        camera.open();

    }

    //when the user closes the phone or switches to another app we release the camera
    @Override
    protected void onPause() {
        super.onPause();
        camera.close();
    }

    //when the user stops using the app we release the camera permanently
    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.destroy();
    }

}


