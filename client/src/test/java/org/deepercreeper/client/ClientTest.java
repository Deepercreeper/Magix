package org.deepercreeper.client;

import org.deepercreeper.common.util.Util;
import org.junit.Test;

public class ClientTest
{
    @Test
    public void testClient()
    {
        String address = "127.0.0.1";
        int port = 8080;
        int localPort = 8081;

        Client client = new TestClient(address, port, localPort);
        client.start();

        Util.sleep(1000);

        client.send("Hi");

        Util.sleep(1000);

        client.stop();
    }

    private class TestClient extends Client
    {
        public TestClient(String address, int port, int localPort)
        {
            super(address, port, localPort);
        }

        public TestClient(String address, int port)
        {
            super(address, port);
        }

        @Override
        protected void receive(String message)
        {
            System.out.println(message);
        }

        @Override
        protected void disconnected()
        {
            System.out.println("Disconnected");
        }
    }
}
