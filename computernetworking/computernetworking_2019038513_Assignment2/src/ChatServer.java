import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    public static void main(String[] args) {
        // args[0] : port for chat with Client , args[1] : port for #GET, #PUT
        if(args.length != 2) {
            System.out.println("There is wrong input.");
            System.exit(-1);
        }
        while (true) {
            try {
                ServerSocket server = new ServerSocket(Integer.parseInt(args[0])); // 서버소켓 -> 클라이언트로부터 연결요청을 기다렸다가 오면 연결해주고 다른 소켓을 만든다.
                ServerSocket fileServer = new ServerSocket(Integer.parseInt(args[1]));
                while (true) {
                    System.out.println("- 클라이언트의 요청 대기중 -");
                    Socket socket = server.accept(); // 클라이언트가 연결 요철할 때 까지 대 // 여기서 blocking
                    ChatThread thread = new ChatThread(socket,fileServer);
                    thread.start();

                    if(!socket.isClosed()) {
                        System.out.println("파일서버 연결 준비..");
                        Socket fileSocket = fileServer.accept();
                        String userName = thread.getUserId();
                        String roomName = thread.getRoomName();
                        FileThread fileThread = new FileThread(fileSocket,userName,roomName);
                        fileThread.start();
                        System.out.println("파일서버 연결 완료!");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class FileThread extends Thread {
    private final Socket socket;
    private final String id;
    private final String roomName;
    private final BufferedReader reader;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final String filePath;

    public static final HashMap<String, PrintWriter> socketMap = new HashMap<>();
    public static final HashMap<String, String> idWithRoom = new HashMap<>();
    public static final HashMap<String, String> fileWithRoom = new HashMap<>();

    public FileThread(Socket socket, String id, String roomName) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.id = id;
        this.roomName = roomName;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.filePath = System.getProperty("user.dir") +"/Files/ServerFiles";
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = this.reader.readLine()) != null) {
                String[] lineSplit = line.split(" ");
                StringBuilder fileNameList = new StringBuilder();
                fileNameList.append("fileList : ");
                if (lineSplit[1].equals("#GET")) {
                    for(Map.Entry<String, String> temp : fileWithRoom.entrySet()) {
                        if(temp.getValue().equals(this.roomName)) {
                            fileNameList.append(temp.getKey() + " ");
                        }
                    }
                    broadcastOne(fileNameList.toString(),this.id);
                    String fileName = this.reader.readLine(); // file 이름 받기

                    for(Map.Entry<String, String> temp : fileWithRoom.entrySet()) {
                        if(temp.getValue().equals(fileName) && temp.getKey().equals(this.roomName)) {
                            File file = new File(filePath+"/"+fileName); // 여기에 경로 + 파일명
                            dataOutputStream.writeUTF(file.getName());
                            FileInputStream fileInputStream = new FileInputStream(file);
                            dataOutputStream.writeLong(file.length());

                            int length;
                            byte[] buffer = new byte[1024];

                            while ((length = fileInputStream.read(buffer))!=-1) {
                                dataOutputStream.write(buffer,0,length);
                                dataOutputStream.flush();
                            }

                            break;
                        }
                    }
                } else if (lineSplit[1].equals("#PUT")) {
                    String fileNameToPut = dataInputStream.readUTF();
                    File file = new File(filePath+"/"+fileNameToPut); // 여기에 경로 + 파일명
                    file.createNewFile();
                    System.out.println("File created.");
                    long fileSize = dataInputStream.readLong();
                    long data = 0;
                    int length;
                    byte[] buffer = new byte[1024];

                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while((length = dataInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer,0,length);
                        data += length;
                        if(data == fileSize) break;
                    }

                    synchronized (fileWithRoom) {
                        fileWithRoom.put(fileNameToPut,this.roomName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastOne(String message, String name) {
        System.out.println(message);
        synchronized (socketMap) {
            PrintWriter writer = socketMap.get(name);
            writer.println(message);
            writer.flush();
        }
    }
}



class ChatThread extends Thread {
    private final Socket socket;
    private final String id;
    private final String roomName;
    private final BufferedReader reader;

    public static final HashMap<String, PrintWriter> socketMap = new HashMap<>();
    public static final HashMap<String, String> idWithRoom = new HashMap<>();

    public ChatThread(Socket socket, ServerSocket fileServer) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // 명령어 입력받음
        String command = reader.readLine();
        String[] commandList = command.split(" ");

        this.id = commandList[2];
        this.roomName = commandList[1];

        synchronized (socketMap) {
            socketMap.put(this.id, new PrintWriter(new OutputStreamWriter(socket.getOutputStream())));
        }

        if(commandList[0].equals("#CREATE")) {
            synchronized (idWithRoom) {
                boolean isDup = false;
                for(Map.Entry<String, String> temp : idWithRoom.entrySet()) {
                    if(temp.getValue().equals(commandList[1])) {
                        isDup = true;
                        break;
                    }
                }

                if(isDup) {
                    broadcastOne("#FAIL " + commandList[2] + " ALREADYEXIST", commandList[2]);
                    this.socket.close();
                } else {
                    broadcastOne("@" + commandList[1] + " has been created.", commandList[2]);
                    idWithRoom.put(commandList[2], commandList[1]);
                }
            }
        } else if (commandList[0].equals("#JOIN")) {
            synchronized (idWithRoom) {
                boolean isDup = false;
                for(Map.Entry<String, String> temp : idWithRoom.entrySet()) {
                    if(temp.getValue().equals(commandList[1])) {
                        isDup = true;
                        break;
                    }
                }

                if(isDup) {
                    broadcastOne("@" + commandList[1] + " " + commandList[2] + " entered.", commandList[2]);
                    idWithRoom.put(commandList[2], commandList[1]);
                } else {
                    broadcastOne("#FAIL " + commandList[2] + " NOCHATROOM", commandList[2]);
                    this.socket.close();
                }
            }

        } else {
            broadcastOne("#FAIL " + commandList[2] + " WRONGCOMMAND", commandList[2]);
            this.socket.close();
        }
    }

    public String getMember() {
        StringBuilder memberList = new StringBuilder();
        synchronized (idWithRoom) {
            for(Map.Entry<String, String> temp : idWithRoom.entrySet()) {
                if(temp.getValue().equals(roomName)) {
                    memberList.append(temp.getKey()).append(" ");
                }
            }
        }
        return memberList.toString();
    }

    public String getUserId() {
        return this.id;
    }

    public String getRoomName() {
        return this.roomName;
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = this.reader.readLine()) != null) {

                if(line.split(" ")[1].equals("#STATUS")) {
                    line = getMember();
                }
                broadcast(line,roomName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            synchronized (socketMap) {
                socketMap.remove(this.id);
            }
            synchronized (idWithRoom) {
                idWithRoom.remove(this.id);
            }
            broadcast("@"+this.roomName + " " +this.id + " quit this chat.",roomName);
            try {
                if (this.socket != null) {
                    this.socket.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void broadcastOne(String message, String name) {
        System.out.println(message);
        synchronized (socketMap) {
            PrintWriter writer = socketMap.get(name);
            writer.println(message);
            writer.flush();
        }
    }

    public void broadcast(String message, String roomName) {
        System.out.println(message);

        synchronized (idWithRoom) {
            for(Map.Entry<String, String> temp : idWithRoom.entrySet()) {
                if(temp.getValue().equals(roomName)) {
                    synchronized (socket) {
                        PrintWriter writer = socketMap.get(temp.getKey());
                        writer.println(message);
                        writer.flush();
                    }
                }
            }
        }
    }
}