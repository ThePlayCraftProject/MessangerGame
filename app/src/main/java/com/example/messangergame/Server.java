package com.example.messangergame;

public class Server {
    public static final int ONLINE = 1;
    public static final int OFFLINE = -1;
    public static final int UNDEFINED = 0;


    private String name;
    private String ip;
    private int port;
    private int querryPort = 25566;
    private Checker checker;
    private long id;
    public int online = UNDEFINED;
    public int players = 0;
    public int max = 0;

    public Server(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public Server(long id, String name, String ip, int port) {
        this(name, ip, port);
        this.id = id;
    }

    public boolean isHavingChecker() {
        return checker != null;
    }


    public void setChecker(Checker checker) {
        if (this.checker != null) {
            this.checker.askToStop();
            this.checker.interrupt();
            this.checker = null;
        }
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
        if (checker != null) {
            checker.askToStop();
            checker = null;
        }
    }
}
