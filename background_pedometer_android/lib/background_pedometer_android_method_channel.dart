import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'background_pedometer_android_platform_interface.dart';

/// An implementation of [BackgroundPedometerAndroidPlatform] that uses method channels.
class MethodChannelBackgroundPedometerAndroid extends BackgroundPedometerAndroidPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('background_pedometer_android');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
