import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.MessageDigest;
import java.util.Scanner;

public class UDPClient {
    static String name;
    static InetAddress addr;
    static int port;
    static MulticastSocket group;

    public static void main(String[] args) {
       if(args.length != 1) {
           System.out.println("Wrong! Please set Port... exit Program");
           System.exit(0);
       } else {
           port = Integer.parseInt(args[0]);
       }

       Scanner sc = new Scanner(System.in);
       while (true) {
           System.out.println("insert #JOIN <room> <name>");
           String allMessage = sc.nextLine();
           String[] splittedString = allMessage.split(" ", 3);

           if(splittedString[0].equals("#JOIN")) {
               try {
                   MessageDigest md = MessageDigest.getInstance("SHA-256");
                   md.update(splittedString[1].getBytes());
                   byte[] bytes = md.digest();
                   StringBuilder builder = new StringBuilder();
                   builder.append("225");
                   for (int i=bytes.length-3 ; i<bytes.length ; i++) {

                       builder.append(String.format(".%d", bytes[i]&0xff));
                   }
                   addr = InetAddress.getByName(builder.toString());
                   name = splittedString[2];
                   group = new MulticastSocket(port);
                   Receiver rThread = new Receiver();
                   Sender sThread = new Sender();
                   rThread.start();
                   sThread.start();

                   sThread.join();
                   rThread.join();
               } catch (Exception e) {
                   e.printStackTrace();
               }
               System.out.println("You left chat room.");
           }
       }
    }

    static class Sender extends Thread {
        public void run() {
            try {
                BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in));
                boolean isTurnOn = true;
                while (isTurnOn) {
                    String userTyped = fromUser.readLine();
                    if(userTyped.equals("#EXIT")) {
                        String msg = name + " has been left.";
                        byte[] out = msg.getBytes();
                        DatagramPacket pkt = new DatagramPacket(out, out.length, addr, port);
                        group.send(pkt);
                        isTurnOn = false;
                    } else {
                        String msg = name + ":" + userTyped;
                        byte[] out = msg.getBytes();
                        DatagramPacket pkt = new DatagramPacket(out, out.length, addr, port);
                        group.send(pkt);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class Receiver extends Thread {
        public void run() {
            try {
                System.out.println("run r");
                byte[] in = new byte[512];
                DatagramPacket pkt = new DatagramPacket(in, in.length);
                group.joinGroup(addr);
                boolean isTurnOn = true;
                while (isTurnOn) {
                    group.receive(pkt);
                    String receivedPkt = new String(pkt.getData(), 0, pkt.getLength());

                    if(receivedPkt.equals(name + " has been left.")) {
                        System.out.println("here ");
                        isTurnOn = false;
                        group.leaveGroup(addr);
                    } else {
                        System.out.println(receivedPkt);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
