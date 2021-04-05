package com.ruuvi.station.calibration.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.ruuvi.station.R
import com.ruuvi.station.calibration.model.CalibrationType
import com.ruuvi.station.databinding.ActivityCalibrationBinding

class CalibrationActivity : AppCompatActivity() {

    lateinit var binding: ActivityCalibrationBinding

    lateinit var tagId: String
    lateinit var calibrationType: CalibrationType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalibrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        tagId = intent.getStringExtra(TAG_ID) ?: ""
        calibrationType = intent.getSerializableExtra(CALIBRATION_TYPE) as CalibrationType
        //binding.testTextView.text = tagId
        openFragment()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    fun openFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left)
        val fragment = when (calibrationType) {
            CalibrationType.HUMIDITY -> CalibrateHumidityFragment.newInstance()
            CalibrationType.TEMPERATURE -> CalibrateTemperatureFragment.newInstance()
            CalibrationType.PRESSURE -> CalibratePressureFragment.newInstance()
        }
        transaction.replace(R.id.calibration_frame, fragment).commit()
    }

    companion object {
        const val TAG_ID = "TAG_ID"
        const val CALIBRATION_TYPE = "CALIBRATION_TYPE"


        fun start(context: Context, tagId: String, calibrationType: CalibrationType) {
            val intent = Intent(context, CalibrationActivity::class.java)
            intent.putExtra(TAG_ID, tagId)
            intent.putExtra(CALIBRATION_TYPE, calibrationType)
            context.startActivity(intent)
        }
    }
}