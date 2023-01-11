import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main(List<String> args) {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static final channel = MethodChannel('vivek/getBattery');

  String battery = "Waiting....";
  var li = [];

  void getBatteryPercentage() async {
    try {
      var data = await channel.invokeMethod('getBatteryinfo');
      li = data;
    } catch (e) {
      print(e.toString());
    }
    setState(() {
      // battery = 'Welcome $data';
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text("Call From Native Side"),
        ),
        body: ListView.builder(
          itemCount: li.length,
          itemBuilder: (context, index) {
            return Text(li[index].toString());
          },
        ),
        floatingActionButton:
            FloatingActionButton(onPressed: getBatteryPercentage),
      ),
    );
  }
}
