import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Scanner;

public class Client {

    static Selector selector = null;
    private SocketChannel socketChannel = null;
    private String id;


    public void startServer(String serverIP, int chatPort, int filePort) throws IOException {
        initServer(serverIP,chatPort,filePort);
        Receive receive = new Receive();
        new Thread(receive).start();
        startWriter();
    }

    private void initServer(String serverIP, int chatPort, int filePort) throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 12345));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void startWriter() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        try {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String message = scanner.next();
                byteBuffer.clear();
                byteBuffer.put(message.getBytes());
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            clearBuffer(byteBuffer);
        }
    }

    static void clearBuffer(ByteBuffer buffer) {
        if (buffer != null) {
            buffer.clear();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.startServer(args[0],Integer.parseInt(args[1]),Integer.parseInt(args[2]));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

class Receive implements Runnable {
    private CharsetDecoder decoder = null;

    public void run() {
        Charset charset = Charset.forName("UTF-8");
        decoder = charset.newDecoder();
        try {
            while (true) {
                int ready = Client.selector.select();
                if(ready == 0) continue;
                Iterator iterator = Client.selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = (SelectionKey) iterator.next();

                    if(key.isReadable())
                        read(key);

                    iterator.remove();
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void read(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

        try {
            byteBuffer.flip();
            String data = decoder.decode(byteBuffer).toString();
            if (data.split(" ")[0].equals("#FAIL")) {
                System.out.println(data.split(" ")[1]);
                socketChannel.close();
            }
            System.out.println("Receive Message - " + data);
            Client.clearBuffer(byteBuffer);
        }
        catch (IOException ex){
            try {
                socketChannel.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}