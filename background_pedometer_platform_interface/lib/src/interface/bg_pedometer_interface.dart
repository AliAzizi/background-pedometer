import 'package:flutter/material.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import '../channel/bg_method_channel.dart';
import '../types/pedometer_data.dart';
import '../types/settings.dart';

abstract class BGPedometerPlatformInterface extends PlatformInterface {
  BGPedometerPlatformInterface() : super(token: _token);

  static BGPedometerPlatformInterface _instance = BGPedometerMethodChannel();
  static final Object _token = Object();

  static BGPedometerPlatformInterface get instance => _instance;

  static set instance(BGPedometerPlatformInterface instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<bool> get isSupported {
    throw UnimplementedError('isSupported() has not been implemented.');
  }

  Future<bool> get isRunning {
    throw UnimplementedError('isRunning() has not been implemented.');
  }

  Future<void> start({required BGPedometerSettings settings}) {
    throw UnimplementedError('start() has not been implemented.');
  }

  Future<void> stop() {
    throw UnimplementedError('stop() has not been implemented.');
  }

  Future<BGPedometerData> getPedometerDataInRange(DateTimeRange range) {
    throw UnimplementedError(
      'getPedometerDataInRange() has not been implemented.',
    );
  }

  Future<BGPedometerData> getPedometerDataForRanges(
    List<DateTimeRange> ranges,
  ) {
    throw UnimplementedError(
      'getPedometerDataForRanges() has not been implemented.',
    );
  }

  Stream<BGPedometerData> getPedometerStream({required DateTime start}) {
    throw UnimplementedError('getPedometerStream() has not been implemented.');
  }
}
