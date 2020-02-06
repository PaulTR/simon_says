package com.devrelconf.simonsays

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import kotlinx.android.synthetic.main.activity_create_challenge.*

class CreateChallengeActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_challenge)

        btn_take_picture.setOnClickListener( View.OnClickListener {
            startCamera()
        })
    }

    private fun startCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK ) {
            Log.e("Test", "its an image")
            if( data == null || data!!.extras == null || data!!.extras!!.get("data") == null ) {

            } else {
                val photo = data?.extras?.get("data") as Bitmap

                var rotation = 0

                try {
                    rotation = getRotationCompensation(this)
                } catch (e: CameraAccessException) {
                    Log.e("Machine Learning", "camera access exception")
                }

                val matrix = Matrix()
                matrix.postRotate(rotation.toFloat())
                val rotatedPhoto =
                    Bitmap.createBitmap(photo, 0, 0, photo.width, photo.height, matrix, true)
                matrix.postRotate(rotation.toFloat() + 90)
                image.setImageBitmap(Bitmap.createBitmap(photo, 0, 0, photo.width, photo.height, matrix, true))
                val firebaseVisionImage = FirebaseVisionImage.fromBitmap(rotatedPhoto)

                val labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler()


                labeler.processImage(firebaseVisionImage)
                    .addOnSuccessListener { labels ->
                        for( label in labels ) {
                            Log.e("Test", "label: " + label.text)
                        }

                        labels_list.setHasFixedSize(true)

                        val layoutManager = LinearLayoutManager(baseContext)
                        layoutManager.orientation = LinearLayoutManager.VERTICAL
                        labels_list.layoutManager = layoutManager
                        labels_list.adapter = LabelsRVAdapter(labels, this@CreateChallengeActivity)
                    }
            }
        }
    }

    @Throws(CameraAccessException::class)
    protected fun getRotationCompensation(activity: Activity): Int {

        val manager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        var camIds: Array<String>? = null

        try {
            camIds = manager.cameraIdList
        } catch (e: CameraAccessException) {
            Log.w("Machine Learning", "Cannot get the list of available cameras", e)
        }

        if (camIds == null || camIds.size < 1) {
            Log.d("Machine Learning", "No cameras found")

            return 0
        }

        val deviceRotation = activity.windowManager.defaultDisplay.rotation
        var rotationCompensation = ORIENTATIONS.get(deviceRotation)

        val cameraManager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val sensorOrientation = cameraManager
            .getCameraCharacteristics(camIds[0])
            .get(CameraCharacteristics.SENSOR_ORIENTATION)!!
        rotationCompensation = (rotationCompensation + sensorOrientation + 270) % 360

        val result: Int
        when (rotationCompensation) {
            0 -> result = FirebaseVisionImageMetadata.ROTATION_0
            90 -> result = FirebaseVisionImageMetadata.ROTATION_90
            180 -> result = FirebaseVisionImageMetadata.ROTATION_180
            270 -> result = FirebaseVisionImageMetadata.ROTATION_270
            else -> {
                result = FirebaseVisionImageMetadata.ROTATION_0
                Log.e("Machine Learning", "Bad rotation value: $rotationCompensation")
            }
        }
        return result
    }

    companion object {

        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }

}