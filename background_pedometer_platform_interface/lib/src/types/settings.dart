import 'package:flutter/foundation.dart';

@immutable
class BGPedometerSettings {}

@immutable
class BGPedometerAndroidSettings {
  BGPedometerAndroidSettings({
    required this.autoRestart,
    required this.autoRunOnBoot,
    required this.autoRunOnMyPackageReplaced,
    required this.shutdownAware,
    required this.sensorFallbackOrder,
    required this.notification,
  });

  final bool autoRestart;
  final bool autoRunOnBoot;
  final bool autoRunOnMyPackageReplaced;
  final BGPedometerNotificationSettings? notification;
  final List<BGPedometerAndroidSensorType> sensorFallbackOrder;
  final bool shutdownAware;
}

@immutable
class BGPedometerNotificationSettings {
  BGPedometerNotificationSettings({
    required this.channelId,
    required this.channelName,
    required this.icon,
    required this.messageTemplate,
    required this.notificationID,
    required this.channelDescription,
  });

  final String? channelDescription;
  final String channelId;
  final String channelName;
  final String icon;
  final String messageTemplate;
  final int notificationID;
}

enum BGPedometerAndroidSensorType { stepCounter }


class BGPedometerIOSSettings {}