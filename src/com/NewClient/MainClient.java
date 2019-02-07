package com.NewClient;

public class MainClient {

    public static void main(String[] args) {
        //"194.58.96.249//"127.0.0.1"
        NewClient client = new NewClient("127.0.0.1", 4026);
        client.run();
    }
}
