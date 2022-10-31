
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.MessageDigest;
import java.util.Scanner;

public class UDPClient {
    static String name = "your name";
    static InetAddress addr;
    static int port = -1;
    static MulticastSocket group;

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Wrong! Please set Port.");
        } else {
            port = Integer.parseInt(args[0]);
        }


        System.out.println("insert #JOIN <room> <name>");
        Scanner sc = new Scanner(System.in);
        String allMessage = sc.nextLine();
        String[] splittedString = allMessage.split(" ", 3);
        if(splittedString[0].equals("#JOIN")) {
            try {
                System.out.println("Fffff");
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(splittedString[1].getBytes());
                byte[] bytes = md.digest();
                StringBuilder builder = new StringBuilder();
                builder.append("225");
                for (int i=bytes.length-3 ; i<bytes.length ; i++) {

                    builder.append(String.format(".%d", bytes[i]&0xff));
                }

                System.out.println(builder.toString() + " " + port);

                addr = InetAddress.getByName(builder.toString());
                name = splittedString[2];
                group = new MulticastSocket(port);

                new Receiver().start();
                new Sender().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class Sender extends Thread {
        public void run() {
            try {
                System.out.println("run sender");
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
                System.out.println("run receiver");
                byte[] in = new byte[512];
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
