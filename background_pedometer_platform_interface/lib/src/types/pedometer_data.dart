import 'package:flutter/foundation.dart';

@immutable
class BGPedometerData {
  BGPedometerData({
    required this.numberOfSteps,
    required this.start,
    required this.end,
  });

  factory BGPedometerData.fromMap(Map<String, dynamic> map) {
    return BGPedometerData(
      numberOfSteps: map['numberOfSteps'],
      start: DateTime.fromMillisecondsSinceEpoch(map['start'], isUtc: false),
      end: DateTime.fromMillisecondsSinceEpoch(map['end'], isUtc: false),
    );
  }

  final int numberOfSteps;
  final DateTime start;
  final DateTime end;
}
