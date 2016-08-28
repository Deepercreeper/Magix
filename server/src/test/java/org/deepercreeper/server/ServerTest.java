package org.deepercreeper.server;

import org.deepercreeper.common.util.Util;
import org.junit.Test;

import java.net.Socket;

public class ServerTest
{
    @Test
    public void testServer()
    {
        int port = 8080;
        Server server = new Server<>(TestClient::new, port);
        server.start();

        Util.sleep(20 * 60 * 1000);

        server.stop();
    }

    private class TestClient extends Client
    {
        public TestClient(Socket socket) throws Exception
        {
            super(socket);
        }

        @Override
        protected void receive(String message)
        {
            System.out.println(message);
            send("RE: " + message);
        }

        @Override
        protected void disconnected()
        {
            System.out.println("Disconnected");
        }
    }
}
