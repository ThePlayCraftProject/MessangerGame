package com.example.messangergame;

public class Server {
    private String name;
    private String ip;
    private int port;
    private int querryPort = 25566;
    private Checker checker;
    private long id;

    public Server(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public Server(long id, String name, String ip, int port) {
        this(name, ip, port);
        this.id = id;
    }


    public void setChecker(Checker checker) {
        this.checker = checker;
    }


    public void setId(long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void stop() {
        checker.askToStop();
        checker.interrupt();
    }
}
