package nl.windesheim.ictm2o.peach.monitor;

import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MonitorServer {

    private ServerSocket serverSocket;

    public void start() {
        try {
            openSocket();
            acceptClients();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Server gecrasht: " + e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptClients() throws Exception {
        while (true) {
            var client = serverSocket.accept();
            new Thread(() -> {
                try {
                    handleClient(client);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void handleClient(Socket client) throws Exception {
        boolean authenticated = false;
        String identifier = null;

        client.setTcpNoDelay(true);

        var scanner = new Scanner(client.getInputStream());
        var output = client.getOutputStream();

        while (true) {
            while (!scanner.hasNextLine())
                Thread.yield();

            String line = scanner.nextLine();
            System.out.println("MonitorServer " + client.getPort() + ": " + line);
            if (!line.contains(" ")) {
                output.write("ERROR Invalid Command Syntax\n".getBytes(StandardCharsets.UTF_8));
                break;
            }

            final var parts = line.split(" ", 2);
            if (parts[0].equals("IDENTIFY")) {
                if (identifier != null) {
                    output.write("ERROR Already Identified".getBytes(StandardCharsets.UTF_8));
                    output.write("GOODBYE\n".getBytes(StandardCharsets.UTF_8));
                    break;
                }
                identifier = parts[1];
            } else if (parts[0].equals("LOGOUT")) {
                output.write("GOODBYE\n".getBytes(StandardCharsets.UTF_8));
            } else if (parts[0].equals("LOGIN")) {
                if (authenticated)
                    output.write("OK\n".getBytes(StandardCharsets.UTF_8));
                else if (identifier == null) {
                    output.write("ERROR Please Identify Yourself\n".getBytes(StandardCharsets.UTF_8));
                } else {
                    if (parts[1].equals("uFMCSYau1vq7IvhZdWl5OTszlHk8WKdQqoQLi3S4HdbnTdZYMTqyjByeA9Vdwd8ebQ5j5zRf1DSjdRBpwafB3TOKhB4FdCP25p6MuIwJq0Ps8THiHQOfTtZ9dF9OgBKZ")) {
                        output.write("ACCESS GRANTED\n".getBytes(StandardCharsets.UTF_8));
                        authenticated = true;
                    } else {
                        output.write("ACCESS DENIED\n".getBytes(StandardCharsets.UTF_8));
                        output.write("GOODBYE\n".getBytes(StandardCharsets.UTF_8));
                        break;
                    }
                }
            } else if (parts[0].equals("HEARTBEAT")) {
                if (!authenticated) {
                    output.write("ERROR Not Authenticated\n".getBytes(StandardCharsets.UTF_8));
                    output.write("GOODBYE\n".getBytes(StandardCharsets.UTF_8));
                    break;
                }

                try {
                    var tokener = new JSONTokener(parts[1]);
                    var object = new JSONObject(tokener);

                    final var cpu = object.getJSONObject("cpu");
                    final var memory = object.getJSONObject("memory");
                    final var disk = object.getJSONObject("disk");

                    MonitorDataManager.append(identifier, new MonitorData(
                            cpu.getInt("percent"),
                            memory.getLong("total"),
                            memory.getLong("used"),
                            disk.getLong("total"),
                            disk.getLong("used")
                    ));
                } catch (Exception exception) {
                    output.write("ERROR Invalid HEARTBEAT Syntax\n".getBytes(StandardCharsets.UTF_8));
                    exception.printStackTrace();
                }
            } else {
                output.write("ERROR Unknown Command\n".getBytes(StandardCharsets.UTF_8));
                if (!authenticated) {
                    output.write("GOODBYE\n".getBytes(StandardCharsets.UTF_8));
                    break;
                }
            }

            output.flush();
        }

        scanner.close();
        output.close();
        client.close();
    }

    private void openSocket() throws Exception {
        serverSocket = new ServerSocket(1312);
    }

}
