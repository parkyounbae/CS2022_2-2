import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Server {
    private Selector selector = null;
    private Vector room = new Vector();

    private HashMap<String, SocketChannel> idWithSocket = new HashMap<>();
    private HashMap<String, String> idWithRoom = new HashMap<>();
    Charset charset = StandardCharsets.UTF_8;

    public void initServer(int port) throws IOException {
        selector = Selector.open(); // Selector 열고
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); // 채널 열고
        serverSocketChannel.configureBlocking(false); // Non-blocking 모드 설정
        serverSocketChannel.bind(new InetSocketAddress(port)); // 12345 포트를 열어줍니다.

        // 서버소켓 채널을 셀렉터에 등록한다.
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void startServer() throws Exception {
        System.out.println("Server Start");

        while (true) {
            int ready = selector.select(); //select() 메소드로 준비된 이벤트가 있는지 확인한다.

            if(ready == 0) {
                continue;
            }
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator iterator = selectionKeySet.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = (SelectionKey) iterator.next();

                if (selectionKey.isAcceptable()) {
                    accept(selectionKey);
                }

                if (selectionKey.isReadable()) {
                    read(selectionKey);
                }

                iterator.remove();
            }
        }
    }

    private void accept(SelectionKey key) throws Exception {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();

        // 서버소켓 accept() 메소드로 서버소켓을 생성한다.
        SocketChannel socketChannel = server.accept();
        // 생성된 소켓채널을 비 블록킹과 읽기 모드로 셀렉터에 등록한다.

        if (socketChannel == null)
            return;

        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        room.add(socketChannel); // 접속자 추가
        System.out.println(socketChannel.toString() + "클라이언트가 접속했습니다.");
    }

    private void read(SelectionKey key) {
        // SelectionKey 로부터 소켓채널을 얻는다.
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024); // buffer 생성
        StringBuilder stringBuilder = new StringBuilder();

        try {
            while(socketChannel.read(byteBuffer)>0) {
                byteBuffer.flip();
                stringBuilder.append(charset.decode(byteBuffer));
            } // 클라이언트 소켓으로부터 데이터를 읽음
            key.interestOps(SelectionKey.OP_READ);
        }
        catch (IOException ex) {
            try {
                socketChannel.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            room.remove(socketChannel);
            ex.printStackTrace();
        }

        String[] commandList = stringBuilder.toString().split(" ");
        // command roomName id (message)
        if(commandList[0].equals("#CREATE")) {
            createAction(commandList,socketChannel);
        } else if(commandList[0].equals("#JOIN")) {
            joinAction(commandList,socketChannel);
        } else if(commandList[0].equals("#STATUS")) {
            getMember(commandList[1],commandList[2]);
        } else if(commandList[0].equals("#GET")) {

        } else if(commandList[0].equals("#PUT")) {

        } else if(commandList[0].equals("#MESSAGE")) {
            try {
                broadcast(stringBuilder.toString(),commandList[1]);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        byteBuffer.clear();
    }

    private void joinAction(String[] commandList, SocketChannel socketChannel) {
        boolean isDup = false;
        for(Map.Entry<String, String> temp : idWithRoom.entrySet()) {
            if(temp.getValue().equals(commandList[1])) {
                isDup = true;
                break;
            }
        }

        if(isDup) {
            try {
                broadcastOne("@" + commandList[1] + " " + commandList[2] + " entered.", commandList[2]);
                idWithRoom.put(commandList[2], commandList[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                broadcastOne("#FAIL " + commandList[2] + " NOCHATROOM", commandList[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private void createAction(String[] commandList, SocketChannel socketChannel) {
        boolean isDup = false;
        for(Map.Entry<String, String> temp : idWithRoom.entrySet()) {
            if(temp.getValue().equals(commandList[1])) {
                isDup = true;
                break;
            }
        }

        if(isDup) {
            try {
                broadcastOne("#FAIL ALREADYEXIST", commandList[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                broadcastOne("@" + commandList[1] + " has been created.", commandList[2]);
                idWithRoom.put(commandList[2], commandList[1]);
                idWithSocket.put(commandList[2], socketChannel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void getMember(String roomName, String id) {
        StringBuilder memberList = new StringBuilder();

        for(Map.Entry<String, String> temp : idWithRoom.entrySet()) {
            if(temp.getValue().equals(roomName)) {
                memberList.append(temp.getKey()).append(" ");
            }
        }
        try {
            broadcastOne(memberList.toString(), id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(String content, String id) throws IOException {
        Iterator iterator = room.iterator();
        while (iterator.hasNext()) {
            // room select
            SocketChannel socketChannel = (SocketChannel) iterator.next();

            if (socketChannel != null) {
                socketChannel.write(charset.encode(content));
            }
        }
    }

    private void broadcastOne(String content, String id) throws IOException {
        idWithSocket.get(id).write(charset.encode(content));
    }

    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("There is wrong input.");
            System.exit(-1);
        }

        Server chatServer = new Server();
        Server fileServer = new Server();
        try {
            chatServer.initServer(Integer.parseInt(args[0]));
            chatServer.startServer();

            fileServer.initServer(Integer.parseInt(args[1]));
            fileServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.getStackTrace();
        }

    }
}