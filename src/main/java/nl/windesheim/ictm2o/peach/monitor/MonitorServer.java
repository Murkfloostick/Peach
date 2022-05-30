package nl.windesheim.ictm2o.peach.monitor;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MonitorServer {

    private static final MonitorServer INSTANCE = new MonitorServer();

    @NotNull
    public static MonitorServer getInstance() {
        return INSTANCE;
    }

    public static void startInBackground() {
        new Thread(INSTANCE::start, "MonitorServer").start();
    }

    private boolean isStarted = false;

    private MonitorServer() {
        // Verberg constructor omdat dit een singleton class is.
    }

    private ServerSocket serverSocket;

    public void start() {
        if (isStarted)
            throw new IllegalStateException("Already started");

        synchronized (this) {
            isStarted = true;
        }

        final String[] errorBoxChoices = {"Terug naar hoofdmenu", "Opnieuw Proberen"};

        try {
            openSocket();
            acceptClients();
        } catch (BindException e) {
            isStarted = false;
            e.printStackTrace();
            if (MonitorPage.instance != null) {
                int index = JOptionPane.showOptionDialog(MonitorPage.instance, "Monitor Services konden niet worden geïnitialiseerd. Is Peach al in een ander venster geopend?",
                        "NerdyGadgets Peach - Fout", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, errorBoxChoices, "??");
                switch (index) {
                    case 0 -> MonitorPage.instance.backToStartPage();
                    case 1 -> startInBackground();
                }
            }
        } catch (Exception e) {
            isStarted = false;
            e.printStackTrace();
            if (MonitorPage.instance != null)
                JOptionPane.showMessageDialog(MonitorPage.instance, "Server gecrasht: " + e,
                        "NerdyGadgets Peach - Fout", JOptionPane.ERROR_MESSAGE);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isStarted = false;
    }

    private void acceptClients() throws Exception {
        while (serverSocket.isBound()) {
            var client = serverSocket.accept();
            new Thread(() -> {
                try {
                    handleClient(client);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "MonitorClient-" + client.getInetAddress().getHostAddress() + ":" + client.getPort()).start();
        }
    }

    private void handleClient(Socket client) throws Exception {
        boolean authenticated = false;
        String identifier = null;

        client.setTcpNoDelay(true);

        var scanner = new Scanner(client.getInputStream());
        var output = client.getOutputStream();

        label:
        while (true) {
            while (!scanner.hasNextLine())
                Thread.yield();

            String line = scanner.nextLine();
            if (!line.contains(" ")) {
                output.write("ERROR Invalid Command Syntax\n".getBytes(StandardCharsets.UTF_8));
                break;
            }

            final var parts = line.split(" ", 2);
            switch (parts[0]) {
                case "IDENTIFY":
                    if (identifier != null) {
                        output.write("ERROR Already Identified".getBytes(StandardCharsets.UTF_8));
                        output.write("GOODBYE\n".getBytes(StandardCharsets.UTF_8));
                        break label;
                    }
                    identifier = parts[1];
                    break;
                case "LOGOUT":
                    output.write("GOODBYE\n".getBytes(StandardCharsets.UTF_8));
                    break;
                case "LOGIN":
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
                            break label;
                        }
                    }
                    break;
                case "HEARTBEAT":
                    if (!authenticated) {
                        output.write("ERROR Not Authenticated\n".getBytes(StandardCharsets.UTF_8));
                        output.write("GOODBYE\n".getBytes(StandardCharsets.UTF_8));
                        break label;
                    }

                    try {
                        var tokener = new JSONTokener(parts[1]);
                        var object = new JSONObject(tokener);

                        final var cpu = object.getJSONObject("cpu");
                        final var memory = object.getJSONObject("memory");
                        final var disk = object.getJSONObject("disk");
                        final var io = object.getJSONObject("io");

                        MonitorDataManager.append(identifier, new MonitorData(
                                cpu.getInt("percent"),
                                memory.getLong("total"),
                                memory.getLong("used"),
                                disk.getLong("total"),
                                disk.getLong("used"),
                                cpu.getLong("processes"),
                                cpu.getLong("windows-services"),
                                io.getLong("bytes-sent"),
                                io.getLong("bytes-received")
                        ), client);
                    } catch (Exception exception) {
                        output.write("ERROR Invalid HEARTBEAT Syntax\n".getBytes(StandardCharsets.UTF_8));
                        exception.printStackTrace();
                    }
                    break;
                default:
                    output.write("ERROR Unknown Command\n".getBytes(StandardCharsets.UTF_8));
                    if (!authenticated) {
                        output.write("GOODBYE\n".getBytes(StandardCharsets.UTF_8));
                        break label;
                    }
                    break;
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

    public boolean isStarted() {
        return isStarted;
    }

}
