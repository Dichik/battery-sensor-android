package com.assignment.lightsensor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button bStart;
    private Button bStop;
    private BroadcastReceiver receiver;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        InitReceiver();
    }

    private void InitReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Стан заряду батареї
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                int healt = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
                int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
                int icon = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, -1);
                float voltage = (float) intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) / 1000;
                boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
                float temperature = (float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10; // Загальний стан батареї
                String shealth = "Not reported";
                switch (healt) {
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        shealth = "Dead";
                        break;
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        shealth = "Good";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        shealth = "Over voltage";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        shealth = "Over heating";
                        break;
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        shealth = "Unknown";
                        break;
                    case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                        shealth = "Unspecified failure";
                        break;
                }
                String sStatus = "Not reported";
                switch (status) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        sStatus = "Charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        sStatus = "Discharging";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        sStatus = "Full";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        sStatus = "Not Charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        sStatus = "Unknown";
                        break;
                }
                // Тип зарядки
                String splugged = "Not Reported";
                switch (plugged) {
                    case BatteryManager.BATTERY_PLUGGED_AC:
                        splugged = "On AC";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_USB:
                        splugged = "On USB";
                        break;
                }
                int chargedPct = (level * 100) / scale;
                String batteryInfo = "Battery Info:" +
                        "\nHealth: " + shealth +
                        "\nStatus: " + sStatus +
                        "\nCharged: " + chargedPct + "%" + "\nPlugged: " + splugged +
                        "\nVoltage: " + voltage +
                        "\nTechnology: " + technology + "\nTemperature: " + temperature + "C" + "\nBattery present: " + present + "\n";
                text.setText(batteryInfo);
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bStart:
                // Запускаємо моніторинг стану батареї
                registerReceiver(receiver,
                        new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                text.setText("Start phone info listener...");
                break;
            case R.id.bStop:
                // Зупиняємо моніторинг стану батареї
                unregisterReceiver(receiver);
                text.setText("Listener is stopped");
                break;
        }
    }

    @Override
    protected void onPause() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onPause();
    }
}
