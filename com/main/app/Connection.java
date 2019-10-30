package com.main.app;

public class Connection {
    private int id;
    private String cmd;
    private int result = 0;

    Connection(int nbr) {
        this.id = nbr;
    }

    public void setCommand(String cm) {
        this.cmd = cm;
    }

    public void setResult(int nbr) {
        this.result = nbr;
    }

    public int getId() {
        return this.id;
    }

    public String getCommand() {
        return this.cmd;
    }

    public int getResult() {
        return this.result;
    }

}