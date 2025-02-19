import 'package:flutter/services.dart';

class AuthService {
  static const platform = MethodChannel('com.example.medilink/auth');

  Future<bool> login(String email, String password) async {
    try {
      final bool result = await platform.invokeMethod('login', {
        'email': email,
        'password': password,
      });
      return result;
    } on PlatformException catch (e) {
      print("Failed to login: '${e.message}'.");
      return false;
    }
  }

  Future<bool> register(String email, String password, String fullName) async {
    try {
      final bool result = await platform.invokeMethod('register', {
        'email': email,
        'password': password,
        'fullName': fullName,
      });
      return result;
    } on PlatformException catch (e) {
      print("Failed to register: '${e.message}'.");
      return false;
    }
  }

  Future<bool> resetPassword(String email) async {
    try {
      final bool result = await platform.invokeMethod('resetPassword', {
        'email': email,
      });
      return result;
    } on PlatformException catch (e) {
      print("Failed to reset password: '${e.message}'.");
      return false;
    }
  }
} 