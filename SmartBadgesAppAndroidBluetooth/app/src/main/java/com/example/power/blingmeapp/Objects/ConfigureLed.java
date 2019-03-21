package com.example.power.blingmeapp.Objects;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.power.blingmeapp.JavaScriptInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ConfigureLed { // this are the uuid from the badge service and characteristics
	public UUID serviceUUID = convertFromInteger(0x1200);
	public UUID UUID_RED = convertFromInteger(0x1201);
	public UUID UUID_GREEN = convertFromInteger(0x1202);
	public  UUID UUID_BLUE = convertFromInteger(0x1203);
	public UUID UUID_BRIGHTNESS = convertFromInteger(0x1204);
	ColorSetting colorStetting;
	BluetoothDevice btDevice = null;
	BluetoothGatt mGatt;

	public ConfigureLed(ColorSetting colorStetting) {
		this.colorStetting = colorStetting;
		this.btDevice = JavaScriptInterface.BLUETOOTH_BADGE;

	}
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
	public boolean configureColors(Context context) throws IOException {
		BluetoothGattCallback gattCallback = new BluetoothGattCallback() {// create callback for bluetooth state change
			@Override
			public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
				Log.i("onConnectionStateChange", "Status: " + status);
				switch (newState) {
					case BluetoothProfile.STATE_CONNECTED: // if the bluetooth is enable discover services available
						gatt.discoverServices();
						break;
					case BluetoothProfile.STATE_DISCONNECTED:
						Log.e("gattCallback", "STATE_DISCONNECTED");
						Log.i("gattCallback", "reconnecting...");
						break;
					default:
						Log.e("gattCallback", "STATE_OTHER");
						break;
				}

			}

			public void onServicesDiscovered(BluetoothGatt gatt, int status) {
				List<BluetoothGattService> services = gatt.getServices();
				Log.i("onServicesDiscovered", services.toString());
				Iterator<BluetoothGattService> serviceIterator = services.iterator();
				while(serviceIterator.hasNext()){// iterate though the services until the service with the right id is found
					BluetoothGattService bleService = serviceIterator.next();
					if(bleService.getUuid().equals(serviceUUID) ){
						changeColor();// if the service is found right the color in the badge
					}
				}

			}
			public void changeColor(){
				String redString = Integer.toHexString(colorStetting.getColor().getR());
				BluetoothGattCharacteristic characteristicRed =
						mGatt.getService(serviceUUID)
								.getCharacteristic(UUID_RED);
				characteristicRed.setValue(hexStringToByteArray(redString));
				mGatt.writeCharacteristic(characteristicRed);


			}
			public void onCharacteristicWrite (BluetoothGatt gatt,
											   BluetoothGattCharacteristic characteristic,
											   int status){
				if (characteristic.getUuid().equals(UUID_RED)){//when the red color is written,  start writing the green color in the device
					String greenString = Integer.toHexString(colorStetting.getColor().getG());
					BluetoothGattCharacteristic characteristicGreen =
							mGatt.getService(serviceUUID)
									.getCharacteristic(UUID_GREEN);
					characteristicGreen.setValue(hexStringToByteArray(greenString));
					gatt.writeCharacteristic(characteristicGreen);
				} if (characteristic.getUuid().equals(UUID_GREEN)){ //when the green color is written,  start writing the blue color in the device
					String blueString = Integer.toHexString(colorStetting.getColor().getB());
					BluetoothGattCharacteristic characteristicBlue =
							gatt.getService(serviceUUID)
									.getCharacteristic(UUID_BLUE);
					characteristicBlue.setValue(hexStringToByteArray(blueString));
					 gatt.writeCharacteristic(characteristicBlue);

				} 	if (characteristic.getUuid().equals(UUID_BLUE)){//when the blue color is written,  start writing the brightness in the device
				    int brightness = 255*colorStetting.getBrightness()/100;
					String brightnessString = Integer.toHexString(brightness);
					BluetoothGattCharacteristic characteristicBrightness =
							gatt.getService(serviceUUID)
									.getCharacteristic(UUID_BRIGHTNESS);
					characteristicBrightness.setValue(hexStringToByteArray(brightnessString));
					gatt.writeCharacteristic(characteristicBrightness);
				} if (characteristic.getUuid().equals(UUID_BRIGHTNESS)){// when the brightness is written, finish
					System.out.print(1);
				}

			}

		};
		mGatt = btDevice.connectGatt(context,true,gattCallback);// add the callback in the device
		return false;
	}
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void initializeDevice(Context context){ // write the colors red, green, blue and then turn the device off
	    final List<ColorSetting> colorSettingList = new ArrayList<>();
	    colorSettingList.add(new ColorSetting(new ColorCustomized(255,0,0)));
        colorSettingList.add(new ColorSetting(new ColorCustomized(0,255,0)));
        colorSettingList.add(new ColorSetting(new ColorCustomized(0,0,255)));
        colorSettingList.add(new ColorSetting(new ColorCustomized(0,0,0)));
        colorStetting = colorSettingList.get(0);
        BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                Log.i("onConnectionStateChange", "Status: " + status);
                switch (newState) {
                    case BluetoothProfile.STATE_CONNECTED:
                        gatt.discoverServices();
                        break;
                    case BluetoothProfile.STATE_DISCONNECTED:
                        Log.e("gattCallback", "STATE_DISCONNECTED");
                        Log.i("gattCallback", "reconnecting...");
                        break;
                    default:
                        Log.e("gattCallback", "STATE_OTHER");
                        break;
                }

            }

            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                List<BluetoothGattService> services = gatt.getServices();
                Log.i("onServicesDiscovered", services.toString());
                Iterator<BluetoothGattService> serviceIterator = services.iterator();
                while(serviceIterator.hasNext()){
                    BluetoothGattService bleService = serviceIterator.next();
                    if(bleService.getUuid().equals(serviceUUID) ){
                        changeColor();
                    }
                }

            }
            public void changeColor(){
                String redString = Integer.toHexString(colorStetting.getColor().getR());
                BluetoothGattCharacteristic characteristicRed =
                        mGatt.getService(serviceUUID)
                                .getCharacteristic(UUID_RED);
                characteristicRed.setValue(hexStringToByteArray(redString));
                mGatt.writeCharacteristic(characteristicRed);


            }
            public void onCharacteristicWrite (BluetoothGatt gatt,
                                               BluetoothGattCharacteristic characteristic,
                                               int status){
                if (characteristic.getUuid().equals(UUID_RED)){
                    String greenString = Integer.toHexString(colorStetting.getColor().getG());
                    BluetoothGattCharacteristic characteristicGreen =
                            mGatt.getService(serviceUUID)
                                    .getCharacteristic(UUID_GREEN);
                    characteristicGreen.setValue(hexStringToByteArray(greenString));
                    gatt.writeCharacteristic(characteristicGreen);
                }
                if (characteristic.getUuid().equals(UUID_GREEN)){
                    String blueString = Integer.toHexString(colorStetting.getColor().getB());
                    BluetoothGattCharacteristic characteristicBlue =
                            gatt.getService(serviceUUID)
                                    .getCharacteristic(UUID_BLUE);
                    characteristicBlue.setValue(hexStringToByteArray(blueString));
                    gatt.writeCharacteristic(characteristicBlue);

                }
                if (characteristic.getUuid().equals(UUID_BLUE)){
                    int brightness = 255*colorStetting.getBrightness()/100;
                    String brightnessString = Integer.toHexString(brightness);
                    BluetoothGattCharacteristic characteristicBrightness =
                            gatt.getService(serviceUUID)
                                    .getCharacteristic(UUID_BRIGHTNESS);
                    characteristicBrightness.setValue(hexStringToByteArray(brightnessString));
                    gatt.writeCharacteristic(characteristicBrightness);
                }

                if (characteristic.getUuid().equals(UUID_BRIGHTNESS)){
                    int index = colorSettingList.indexOf(colorStetting);
                    if (index < colorSettingList.size() - 1) {
                        colorStetting = colorSettingList.get(index + 1);
                        // wait an amount until the next color is ready
						try {
							TimeUnit.MILLISECONDS.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						changeColor();

                    }
                }

            }

        };
        mGatt = btDevice.connectGatt(context,true,gattCallback);
	}
	public ColorSetting getColorStetting() {
		return colorStetting;
	}
	public void setColorStetting(ColorSetting colorStetting) {
		this.colorStetting = colorStetting;
	}
	public UUID convertFromInteger(int i) {
		final long MSB = 0x0000000000001000L;
		final long LSB = 0x800000805f9b34fbL;
		long value = i & 0xFFFFFFFF;
		return new UUID(MSB | (value << 32), LSB);
	}
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		if (len == 1){
			len=2;
			s="0"+s;
		} else if (len > 2){
			s = "FF";
			len = 2;
		}
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}
	
}
