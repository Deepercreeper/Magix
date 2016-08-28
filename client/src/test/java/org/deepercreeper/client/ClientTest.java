package org.deepercreeper.client;

import org.deepercreeper.common.util.Util;
import org.junit.Test;

public class ClientTest
{
    @Test
    public void testClient()
    {
        String address = "";
        int port = 8080;

        Client client = new TestClient(address, port);
        client.start();

        Util.sleep(20 * 60 * 1000);

        client.stop();
    }

    private class TestClient extends Client
    {
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
