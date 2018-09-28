package com.illuminati.iss.lightcontrol;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkExplorer {

    public static HashMap<String, String> retardedScan()
    {

        int timeout=500;

        HashMap<String, String> foundAddresses = new HashMap<>();

        try
        {
            String currentIP = InetAddress.getLocalHost().toString();
            String subnet = getSubnet(currentIP);

            for (int i=1;i<254;i++){

                String hostAddress = subnet + i;

                InetAddress host = InetAddress.getByName(hostAddress);
                if (host.isReachable(timeout))
                {
                    //address is reachable
                    String hostName = host.getHostName();
                    foundAddresses.put(hostAddress, hostName);
                }
            }
        }
        catch(Exception e){
            //System.out.println(e);
        }

        return foundAddresses;
    }

    private static String getSubnet(String currentIP)
    {
        int firstSeparator = currentIP.lastIndexOf("/");
        int lastSeparator = currentIP.lastIndexOf(".");
        return currentIP.substring(firstSeparator+1, lastSeparator+1);
    }

}
