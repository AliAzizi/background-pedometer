import 'dart:async';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import '../interface/bg_pedometer_interface.dart';
import '../types/bg_pedometer_exception.dart';
import '../types/pedometer_data.dart';
import '../types/settings.dart';

class BGPedometerMethodChannel extends BGPedometerPlatformInterface {
  static const _methodChannel = MethodChannel(
    'com.github.aliazizi/background_pedometer_platform_interface',
  );

  static const _eventChannel = EventChannel(
    'com.github.aliazizi/background_pedometer_platform_interface/stream',
  );

  Stream<BGPedometerData>? _pedometerStream;

  DateTime? _latestStreamStartDate;

  @override
  Stream<BGPedometerData> getPedometerStream({required DateTime start}) {
    if (_pedometerStream != null) {
      _validateStreamStartDate(start);
      return _pedometerStream!;
    }

    final eventStream = _eventChannel.receiveBroadcastStream(
      start.millisecondsSinceEpoch,
    );

    var wrappedStream = _wrapStream(eventStream);

    _pedometerStream = wrappedStream
        .map<BGPedometerData>(_mapEventToPedometerData)
        .handleError(_handleStreamError);

    _latestStreamStartDate = start;
    return _pedometerStream!;
  }

  Stream<dynamic> _wrapStream(Stream<dynamic> incoming) {
    return incoming.asBroadcastStream(
      onCancel: (subscription) {
        subscription.cancel();
        _resetStreamState();
      },
    );
  }

  BGPedometerData _mapEventToPedometerData(dynamic element) {
    try {
      return BGPedometerData.fromMap(element);
    } catch (e) {
      throw BGPedometerException(
        'DATA_PARSING_ERROR',
        'Failed to parse pedometer data: $e',
      );
    }
  }

  void _handleStreamError(Object error, StackTrace stackTrace) {
    _resetStreamState();

    if (error is PlatformException) {
      throw BGPedometerException(error.code, error.message);
    }

    Error.throwWithStackTrace(error, stackTrace);
  }

  void _validateStreamStartDate(DateTime requestedStart) {
    if (requestedStart != _latestStreamStartDate) {
      if (kDebugMode) {
        debugPrint(
          'BGPedometer Warning: Attempted to start stream with different date. '
          'Expected: $_latestStreamStartDate, Requested: $requestedStart. '
          'Returning existing stream.',
        );
      }
    }
  }

  void _resetStreamState() {
    _pedometerStream = null;
    _latestStreamStartDate = null;
  }

  @override
  Future<bool> get isRunning async {
    try {
      final result = await _methodChannel.invokeMethod<bool>('isRunning');
      return result ?? false;
    } on PlatformException catch (e) {
      throw BGPedometerException(e.code, e.message);
    }
  }

  @override
  Future<bool> get isSupported async {
    try {
      final result = await _methodChannel.invokeMethod<bool>('isSupported');
      return result ?? false;
    } on PlatformException catch (e) {
      throw BGPedometerException(e.code, e.message);
    }
  }

  @override
  Future<void> start({required BGPedometerSettings settings}) async {
    if (Platform.isIOS) {
      if (kDebugMode) {
        debugPrint('BGPedometer: Skipping start on iOS platform');
      }
      return;
    }

    try {
      await _methodChannel.invokeMethod(
        'start',
        settings.androidSettings.toMap(),
      );
    } on PlatformException catch (e) {
      throw BGPedometerException(e.code, e.message);
    }
  }

  @override
  Future<void> stop() async {
    try {
      await _methodChannel.invokeMethod('stop');
      _resetStreamState();
    } on PlatformException catch (e) {
      throw BGPedometerException(e.code, e.message);
    }
  }
}
