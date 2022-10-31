import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Main {
    static String name = "your name";
    static InetAddress addr;
    static int port = 3456;
    static MulticastSocket group;

    public static void main(String[] args) {

        try {//from  w  ww  . j a va2  s  . c  o  m
            addr = InetAddress.getByName("225.206.145.198");
            group = new MulticastSocket(port);
            new Receiver().start();
            new Sender().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Sender extends Thread {
        public void run() {
            try {
                BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    String msg = name + ":" + fromUser.readLine();
                    byte[] out = msg.getBytes();
                    DatagramPacket pkt = new DatagramPacket(out, out.length, addr, port);
                    group.send(pkt);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class Receiver extends Thread {
        public void run() {
            try {
                byte[] in = new byte[256];
                DatagramPacket pkt = new DatagramPacket(in, in.length);
                group.joinGroup(addr);
                while (true) {
                    group.receive(pkt);
                    System.out.println(new String(pkt.getData(), 0, pkt.getLength()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}