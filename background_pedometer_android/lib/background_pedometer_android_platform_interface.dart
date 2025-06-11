import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'background_pedometer_android_method_channel.dart';

abstract class BackgroundPedometerAndroidPlatform extends PlatformInterface {
  /// Constructs a BackgroundPedometerAndroidPlatform.
  BackgroundPedometerAndroidPlatform() : super(token: _token);

  static final Object _token = Object();

  static BackgroundPedometerAndroidPlatform _instance = MethodChannelBackgroundPedometerAndroid();

  /// The default instance of [BackgroundPedometerAndroidPlatform] to use.
  ///
  /// Defaults to [MethodChannelBackgroundPedometerAndroid].
  static BackgroundPedometerAndroidPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [BackgroundPedometerAndroidPlatform] when
  /// they register themselves.
  static set instance(BackgroundPedometerAndroidPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
