import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:background_pedometer_android/background_pedometer_android_method_channel.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  MethodChannelBackgroundPedometerAndroid platform = MethodChannelBackgroundPedometerAndroid();
  const MethodChannel channel = MethodChannel('background_pedometer_android');

  setUp(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(
      channel,
      (MethodCall methodCall) async {
        return '42';
      },
    );
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(channel, null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
