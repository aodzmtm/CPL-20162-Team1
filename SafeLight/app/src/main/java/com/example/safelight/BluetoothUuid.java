package com.example.safelight;

import java.util.UUID;

/**
 * Created by changsu on 2015-03-23.
 */
public class BluetoothUuid {
    public static final UUID WIZTURN_PROXIMITY_UUID = UUID.fromString("d5756247-57a2-4344-915d-9599497940a7");      // Pebble Beacon
    public static final UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final UUID SIMPLE_PROFILE_SERVICE_UUID = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    public static final UUID SIMPLE_PROFILE_SET_UUID = UUID.fromString("0000fff1-0000-1000-8000-00805f9B34fb");
    public static final UUID IBEACON_SERVICE_UUID = UUID.fromString("d5753000-57a2-4344-915d-9599497940a7");
    public static final UUID IBEACON_SET_MAJOR_UUID = UUID.fromString("d5753001-57a2-4344-915d-9599497940a7");
    public static final UUID IBEACON_SET_MINOR_UUID = UUID.fromString("d5753002-57a2-4344-915d-9599497940a7");
    public static final UUID IBEACON_SET_UUID1_UUID = UUID.fromString("d5753003-57a2-4344-915d-9599497940a7");
    public static final UUID IBEACON_SET_UUID2_UUID = UUID.fromString("d5753004-57a2-4344-915d-9599497940a7");
    public static final UUID IBEACON_SET_TXPOWER_UUID = UUID.fromString("d5753011-57a2-4344-915d-9599497940a7");
    public static final UUID IBEACON_SET_ADVINT_UUID = UUID.fromString("d5753012-57a2-4344-915d-9599497940a7");
    public static final UUID IBEACON_SET_MPOWER_UUID = UUID.fromString("d5753021-57a2-4344-915d-9599497940a7");
    public static final UUID IBEACON_SET_PASS_UUID = UUID.fromString("d5753031-57a2-4344-915d-9599497940a7");
    public static final UUID IBEACON_SET_BATTERY_UUID = UUID.fromString("d5753041-57a2-4344-915d-9599497940a7");

    public static final UUID DEVINFO_SERVICE_UUID = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    public static final UUID FIRMWARE_VERSION_UUID = UUID.fromString("00002a26-0000-1000-8000-00805F9B34fb");
    public static final UUID HARDWARE_VERSION_UUID = UUID.fromString("00002a27-0000-1000-8000-00805F9B34fb");
    public static final UUID BETTERY_SERVICE_UUID = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
    public static final UUID BETTERY_LEVEL_UUID = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");
    public static final UUID DEVINFO_MODEL_NUMBER_UUID = UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb");
    public static final UUID DEVINFO_SERIAL_NUMBER_UUID = UUID.fromString("00002a25-0000-1000-8000-00805f9b34fb");
    public static final UUID DEVINFO_SOFTWARE_REV_UUID = UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb");
    public static final UUID DEVINFO_MANUFACTURER_NAME_UUID = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");
    public static final UUID GAP_SERVICE_UUID = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
    public static final UUID GATT_SERVICE_UUID = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");

    public static final UUID WINI_UUID = UUID.fromString("b9407f30-f5f8-466e-aff9-25556b57fe6d");   // TI CC2541
}
