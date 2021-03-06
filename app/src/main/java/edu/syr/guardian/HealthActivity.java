package edu.syr.guardian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class HealthActivity extends AppCompatActivity implements SensorEventListener {

    private TextView tempTextView;
    private TextView tempWarningTextView;
    private SensorManager sensorManager;
    private Sensor tempSensor;
    private boolean isTempSensorAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        isTempSensorAvailable = false;

        tempTextView = findViewById(R.id.tempTextView);
        tempWarningTextView = findViewById(R.id.tempWarningTextView);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        } else {
            tempTextView.setText("Temp Sensor is not available");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float currentValue = event.values[0];
        tempTextView.setText(getResources().getString(
                R.string.label_temp, currentValue));
        if (event.values[0] < 10.0){
            tempWarningTextView.setText("Temperature is too low!");
            isTempSensorAvailable = true;
        }else if (event.values[0] > 35.0){
            tempWarningTextView.setText("Temperature is too high!");
            isTempSensorAvailable = false;
        } else {
            tempWarningTextView.setText("Temperature is all right!");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isTempSensorAvailable){
            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isTempSensorAvailable){
            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isTempSensorAvailable){
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isTempSensorAvailable){
            sensorManager.unregisterListener(this);
        }
    }
}