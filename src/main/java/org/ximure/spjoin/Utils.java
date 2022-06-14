package org.ximure.spjoin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Utils {
    public static List<Integer> pingServer(String serverAddress) {
        try {
            String[] addressAndPort = serverAddress.split(":");
            String address;
            Integer port;

            if (addressAndPort.length == 1) {
                address = addressAndPort[0];
                port = 25565;
            } else {
                address = addressAndPort[0];
                port = Integer.valueOf(addressAndPort[1]);
            }

            Socket connectionSocket = new Socket(address, port);
            DataOutputStream outputStream = new DataOutputStream(connectionSocket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(connectionSocket.getInputStream());
            outputStream.write(0xFE);

            StringBuilder str = new StringBuilder();

            int b;
            while ((b = inputStream.read()) != -1) {
                if (b > 16 && b != 255 && b != 23 && b != 24) {
                    str.append((char) b);
                }
            }

            String[] data = str.toString().split("§");
            // 1 - current players, 2 - max players
            return List.of(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
