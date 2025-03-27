package ClientSide;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UberClient {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

   


    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 8080;

        try (Socket socket = new Socket(hostname, port)) {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.println(input.readLine()); // Register/Login message
            System.out.flush();
            String action = userInput.readLine();
            if (action == null || action.isBlank()) {
                System.out.println("Invalid input! Exiting...");
                return;
            }
            output.println(action);

            System.out.println(input.readLine()); // Username prompt
            String username = userInput.readLine();
            output.println(username);

            System.out.println(input.readLine()); // Password prompt
            String password = userInput.readLine();
            output.println(password);

            String response = input.readLine();
            System.out.println(response);//print response to client console
            
            // If registering, ask for type
            if (response.contains("Enter type")) {
                String type = userInput.readLine();
                output.println(type);
                response = input.readLine();
                System.out.println(response);
            }
            
            
            new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = input.readLine()) != null) {
                        System.out.println(serverResponse);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            while (true) {
                String userChoice = userInput.readLine();
                output.write(userChoice + "\n");
                output.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        
    }


