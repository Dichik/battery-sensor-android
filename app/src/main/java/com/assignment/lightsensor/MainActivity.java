package com.assignment.lightsensor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {
    private TextView text0;
    private SensorManager manager;
    private List<Sensor> sensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text0 = findViewById(R.id.text0);
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // Display the current value on the screen
            text0.setText(String.valueOf(sensorEvent.values[0]));
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensors = manager.getSensorList(Sensor.TYPE_LIGHT);
        if (!sensors.isEmpty()) {
            manager.registerListener(listener, sensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // If the sensor is not available, display an AlertDialog with a warning and close the app
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setMessage("This sensor is not available on the device");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    finish();
                }
            });
            builder.show();
        }
    }
}
