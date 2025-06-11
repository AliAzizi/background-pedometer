import 'package:flutter_test/flutter_test.dart';
import 'package:background_pedometer_android/background_pedometer_android.dart';
import 'package:background_pedometer_android/background_pedometer_android_platform_interface.dart';
import 'package:background_pedometer_android/background_pedometer_android_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockBackgroundPedometerAndroidPlatform
    with MockPlatformInterfaceMixin
    implements BackgroundPedometerAndroidPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final BackgroundPedometerAndroidPlatform initialPlatform = BackgroundPedometerAndroidPlatform.instance;

  test('$MethodChannelBackgroundPedometerAndroid is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelBackgroundPedometerAndroid>());
  });

  test('getPlatformVersion', () async {
    BackgroundPedometerAndroid backgroundPedometerAndroidPlugin = BackgroundPedometerAndroid();
    MockBackgroundPedometerAndroidPlatform fakePlatform = MockBackgroundPedometerAndroidPlatform();
    BackgroundPedometerAndroidPlatform.instance = fakePlatform;

    expect(await backgroundPedometerAndroidPlugin.getPlatformVersion(), '42');
  });
}
