import java.io.*;
import java.net.Socket;
import java.security.spec.ECField;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        // args[0] : server ip, args[1] : port for chat with Client , args[2] : port for #GET, #PUT
        if (args.length != 3) {
            System.out.println("There is wrong input.");
            System.exit(-1);
        }

        Socket socket = null;
        Socket fileSocket = null;

        PrintWriter writer = null;
        BufferedReader reader = null;

        String filePath = System.getProperty("user.dir") +"/Files";

        while (true) {
            System.out.println("채팅방 생성 : #CREATE (채팅방 이름) (사용자 이름)");
            System.out.println("채팅방 입장 : #JOIN (채팅방 이름) (사용자 이름)");
            Scanner scanner = new Scanner(System.in);
            String fullCommand = scanner.nextLine();
            String[] commandList = fullCommand.split(" ");

            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                socket = new Socket(args[0], Integer.parseInt(args[1])); // 서버 아이피와 포트번호로 접속
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                fileSocket = new Socket(args[0], Integer.parseInt(args[2]));
                DataInputStream dataInputStream = new DataInputStream(fileSocket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(fileSocket.getOutputStream());

                if(commandList[0].equals("#CREATE")) {
                    writer.println(fullCommand);
                    writer.flush();

                    PrintThread thread = new PrintThread(socket, reader,commandList[1],commandList[2]);
                    thread.start();

                    FilePrintThread filePrintThread = new FilePrintThread(fileSocket,commandList[1],commandList[2]);
                    filePrintThread.start();

                    String msg;
                    while (true) {
                        if(socket.isClosed()) {
                            writer.close();
                            break;
                        }

                        String pathWithName = filePath + "/" +commandList[2];
                        File folder = new File(pathWithName);

                        if(!folder.exists()) {
                            try {
                                folder.mkdir();
                            } catch (Exception e) {
                                e.getStackTrace();
                            }
                        }

                        msg = input.readLine();

                        if(msg.equals("#EXIT")) {
                            msg = msg + " " + commandList[1] + " " + commandList[2];
                            writer.println(msg);
                            writer.flush();

                            try {
                                if (writer != null) writer.close();
                                if (reader != null) reader.close();
                                if (socket != null) socket.close();
                                break;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if(msg.split(" ")[0].equals("#GET")) {
                            writer.println(msg);
                            writer.flush();
                        } else if(msg.split(" ")[0].equals("#PUT")) {
                            String fileToPut = msg.split(" ")[1];
                            File file = new File(pathWithName + "/" + fileToPut);
                            if(file.isFile()) {
                                dataOutputStream.writeUTF(file.getName());
                                FileInputStream fileInputStream = new FileInputStream(file);
                                dataOutputStream.writeLong(file.length());

                                int length;
                                byte[] buffer = new byte[1024];

                                StringBuilder fileIndex = new StringBuilder();
                                while ((length = fileInputStream.read(buffer))!=-1) {
                                    dataOutputStream.write(buffer,0,length);
                                    dataOutputStream.flush();
                                    fileIndex.append("#");
                                    System.out.println(fileIndex.toString());
                                }
                            } else {
                                System.out.println("해당 파일이 존재하지 않습니다.");
                            }

                        } else if(msg.split(" ")[0].equals("#STATUS")) {
                            msg = "@" + commandList[1] + " #STATUS";
                            writer.println(msg);
                            writer.flush();
                        } else {
                            msg = "@" + commandList[1] + " " + commandList[2] + " " + msg;
                            writer.println(msg);
                            writer.flush();
                        }
                    }
                } else if (commandList[0].equals("#JOIN"))
                {
                    writer.println(fullCommand);
                    writer.flush();

                    PrintThread thread = new PrintThread(socket, reader,commandList[1],commandList[2]);
                    thread.start();

                    String msg;
                    while (true) {
                        if(socket.isClosed()) {
                            writer.close();
                            break;
                        }
                        fileSocket = new Socket(args[0], Integer.parseInt(args[2]));

                        msg = input.readLine();

                        if(msg.equals("#EXIT")) {
                            msg = msg + " " + commandList[1] + " " + commandList[2];
                            writer.println(msg);
                            writer.flush();

                            try {
                                if (writer != null) writer.close();
                                if (reader != null) reader.close();
                                if (socket != null) socket.close();
                                break;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            msg = "@" + commandList[1] + " " + commandList[2] + " " + msg;
                            writer.println(msg);
                            writer.flush();
                        }
                    }
                } else {
                    System.out.println("WRONG COMMAND");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}

class FilePrintThread extends Thread {
    private final Socket socket;

    private final String roomName;
    private final String userName;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final String filePath;

    public FilePrintThread(Socket socket, String roomName, String userName) throws IOException {
        this.socket = socket;
        this.roomName = roomName;
        this.userName = userName;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.filePath = System.getProperty("user.dir") +"/Files/"+userName;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (dataInputStream.available() != 0) {
                    String fileNameToPut = dataInputStream.readUTF();
                    File file = new File(filePath+"/"+fileNameToPut); // 여기에 경로 + 파일명
                    file.createNewFile();
                    System.out.println("File created.");
                    long fileSize = dataInputStream.readLong();
                    long data = 0;
                    int length;
                    byte[] buffer = new byte[1024];

                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("진행도 : #");
                    while((length = dataInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer,0,length);
                        data += length;

                        System.out.println(stringBuilder.toString());
                        stringBuilder.append("#");

                        if(data == fileSize) break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (this.socket != null) this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class PrintThread extends Thread {
    private final Socket socket;
    private final BufferedReader reader;

    private final String roomName;
    private final String userName;

    public PrintThread(Socket socket, BufferedReader reader, String roomName, String userName) {
        this.socket = socket;
        this.reader = reader;
        this.roomName = roomName;
        this.userName = userName;
    }

    @Override
    public void run() {
        try {
            String line;
            while((line = this.reader.readLine()) != null) {
                String[] lineList = line.split(" ",3);
                if(lineList[0].equals("#FAIL") && lineList[1].equals(this.userName)) {
                    try {
                        if (this.reader != null) this.reader.close();
                        if (this.socket != null) this.socket.close();
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else  {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (this.reader != null) this.reader.close();
                if (this.socket != null) this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

