package com.example.valentin.osmofflinesample

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.BoundingBox
import android.graphics.ColorMatrixColorFilter
import android.graphics.ColorMatrix


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private val REQUEST_CODE = 103
    val area = BoundingBox(55.833338, 37.602276, 55.862664, 37.530820)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

        initMap()

        setCustomTileSource()

        addOverlay()
    }

    private fun initMap() {
        view_map.setBuiltInZoomControls(true)
        view_map.setMultiTouchControls(true)
        view_map.minZoomLevel = 14.0
        view_map.maxZoomLevel = 17.9
        view_map.setScrollableAreaLimitDouble(area)
        view_map.zoomToBoundingBox(area, false)
    }

    private fun setCustomTileSource() {
        view_map.setTileSource(
            XYTileSource(
                "custom",
                1,
                16,
                256,
                ".png",
                arrayOf()
            )
        )
    }

    private fun addOverlay() {
        val matrixA = ColorMatrix()
        matrixA.setSaturation(0.3f)

        val matrixB = ColorMatrix()
        matrixB.setScale(.90f, .87f, .85f, 1.0f)
        matrixA.setConcat(matrixB, matrixA)

        val filter = ColorMatrixColorFilter(matrixA)

        view_map.overlayManager.tilesOverlay.setColorFilter(filter) // TilesOverlay.INVERT_COLORS
    }

    public override fun onResume() {
        super.onResume()
        view_map.onResume()
    }

    public override fun onPause() {
        super.onPause()
        view_map.onPause()
    }

    private fun checkPermissions() {
        if (!checkHasPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            !checkHasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ) {
            requestPermission(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_CODE
            )
        }
    }

    private fun checkHasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isEmpty() ||
                    grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d(TAG, "Permissions has been denied by user")
                } else {
                    Log.d(TAG, "Permissions has been granted by user")
                }
            }
        }

    }
}
