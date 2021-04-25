package de.undefinedhuman.projectcreate.server;

public class Main {

    public static void main(String[] args) {
        int i = 500;
        while(i > 0) {
            try {
                Thread.sleep(1000);
                System.out.println("Server still running!");
                i--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("SERVER STOPPED!");
    }

}
