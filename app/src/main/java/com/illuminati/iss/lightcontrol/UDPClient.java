package com.illuminati.iss.lightcontrol;

import java.io.*;
import java.nio.charset.Charset;

class UDPClient
{
    public static void send() throws Exception
    {
        BufferedReader inFromUser =
                new BufferedReader(new InputStreamReader(System.in));


        Runnable r = new Runnable() {
            public void run() {
                try{
                    java.net.DatagramSocket clientSocket = new java.net.DatagramSocket();
                    java.net.InetAddress IPAddress = java.net.InetAddress.getByName("192.168.1.104");
                    byte[] sendData = new byte[1024];
                    byte[] receiveData = new byte[1024];
                    String sentence = "{\"smartlife.iot.smartbulb.lightingservice\":{\"transition_light_state\":{\"on_off\":1,\"transition_period\":10,\"hue\":0,\"saturation\":0,\"color_temp\":0,\"brightness\":10}}}";
                    sendData = sentence.getBytes(Charset.availableCharsets().get("UTF-8"));
                    java.net.DatagramPacket sendPacket = new java.net.DatagramPacket(sendData, sendData.length, IPAddress, 9999);
                    clientSocket.send(sendPacket);
                    clientSocket.close();
                }
                catch (Exception e) {
                    System.out.println(e.getStackTrace());
                }
            }
        };

        Thread netThread = new Thread(r);
        netThread.start();


//        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//        clientSocket.receive(receivePacket);
//        String modifiedSentence = new String(receivePacket.getData());
//        System.out.println("FROM SERVER:" + modifiedSentence);

    }
}