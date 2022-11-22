import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client2 {
    public static void main(String[] args) {
        // args[0] : server ip, args[1] : port for chat with Client , args[2] : port for #GET, #PUT
        if (args.length != 3) {
            System.out.println("There is wrong input.");
            System.exit(-1);
        }

        Socket socket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;

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

                if(commandList[0].equals("#CREATE")) {
                    writer.println(fullCommand);
                    writer.flush();

                    PrintThread thread = new PrintThread(socket, reader,commandList[1],commandList[2]);
                    thread.start();

                    String msg;
                    while (true) {
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
                } else if (commandList[0].equals("#JOIN")) {
                    writer.println(fullCommand);
                    writer.flush();

                    PrintThread thread = new PrintThread(socket, reader,commandList[1],commandList[2]);
                    thread.start();

                    String msg;
                    while (true) {
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