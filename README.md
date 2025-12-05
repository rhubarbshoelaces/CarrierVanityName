# Carrier Vanity Name

Carrier Vanity Name is a very simple app to change the carrier names on unrooted Android devices.

The changed names will persist through reboots. To revert them back, click the reset button in the app.

The app can also override the ISO country code of your SIM card. This can be useful to spoof your location to some region-locked apps like the Pixel Thermometer.

The app uses [Shizuku](https://shizuku.rikka.app/) to achieve this using carrier config overrides. This method was inspired by Kyujin Cho's [pixel-volte-patch](https://github.com/kyujin-cho/pixel-volte-patch), which uses the same method to enable VoLTE on some Google Pixel and LG devices.

## 🔧 Enhancements in This Fork
This fork introduces several improvements:

- ✅ **Support for VoWiFi Carrier Label Override**  
  Overrides `sem_vowifi_opname_string` in addition to `KEY_CARRIER_NAME_STRING`, allowing you to change the carrier label that appears during WiFi calling.

- 📱 **Tasker Integration**  
  You can trigger carrier name changes via Tasker by sending a broadcast intent with Shizuku permission. Requires the [IntentTask plugin](https://play.google.com/store/apps/details?id=com.balda.intenttask) to support multiple extras.

- ⚙️ **ADB-Friendly**  
  You can also change the carrier name using ADB shell commands.

---

## 🚀 Usage

### In-App UI
1. Enter the desired carrier name
2. Optionally set an ISO region override
3. Press **Set**

### Tasker Broadcast Intent Example (via IntentTask Plugin)
```
Action: valid.name.carriername.SET_CARRIER_NAME
Package: valid.name.carriername
Class: valid.name.carriername.CarrierNameReceiver
Text Extras:
  Name: new_carrier_name
  Value: Hello 🌤️

  Name: subscription_id
  Value: 1

  Name: iso_region
  Value: us
```

### ADB Shell Command Example
```
adb shell am broadcast \
  -a valid.name.carriername.SET_CARRIER_NAME \
  -n valid.name.carriername/.CarrierNameReceiver \
  --es new_carrier_name "Hello 🌤️" \
  --es iso_region us \
  --ei subscription_id 1
```

---

## ⚠️ Disclaimer
**WARNING**: Anything using Shizuku might potentially cause harm to your device (and the data on it). I'm not responsible for any damage that might be caused by this app. You have been warned.

Building this app may also require this moddified android.jar: https://github.com/Reginer/aosp-android-jar/tree/main/android-31
---

[You can download a prebuilt APK here](https://github.com/rhubarbshoelaces/CarrierVanityName/releases)
