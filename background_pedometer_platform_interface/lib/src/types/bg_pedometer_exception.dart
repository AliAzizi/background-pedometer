class BGPedometerException implements Exception {
  const BGPedometerException(this.code, this.description);

  final String code;
  final String? description;

  @override
  String toString() => 'BGPedometerException($code, $description)';
}
