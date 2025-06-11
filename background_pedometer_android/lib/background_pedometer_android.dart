
import 'background_pedometer_android_platform_interface.dart';

class BackgroundPedometerAndroid {
  Future<String?> getPlatformVersion() {
    return BackgroundPedometerAndroidPlatform.instance.getPlatformVersion();
  }
}
