import 'package:flutter/foundation.dart';

@immutable
class BGPedometerSettings {
  BGPedometerSettings({required this.androidSettings});

  final BGPedometerAndroidSettings androidSettings;
}

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

extension BGPedometerAndroidSettingsMapper on BGPedometerAndroidSettings {
  Map<String, dynamic> toMap() {
    return {
      'autoRestart': autoRestart,
      'autoRunOnBoot': autoRunOnBoot,
      'autoRunOnMyPackageReplaced': autoRunOnMyPackageReplaced,
      'shutdownAware': shutdownAware,
      'sensorFallbackOrder': sensorFallbackOrder.map((e) => e.index).toList(),
      'notification': notification?.toMap(),
    };
  }
}

extension BGPedometerNotificationSettingsMapper
    on BGPedometerNotificationSettings {
  Map<String, dynamic> toMap() {
    return {
      'channelId': channelId,
      'channelName': channelName,
      'icon': icon,
      'messageTemplate': messageTemplate,
      'notificationID': notificationID,
      'channelDescription': channelDescription,
    };
  }
}
