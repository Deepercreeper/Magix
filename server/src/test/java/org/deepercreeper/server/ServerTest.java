package org.deepercreeper.server;

import org.deepercreeper.client.Client;
import org.deepercreeper.common.util.Util;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class ServerTest
{
    private static final int PORT = 8080;

    private static final int LOCAL_PORT = 8081;

    @Test
    public void testServer()
    {
        Server<FrameRemoteClient> server = new Server<>(FrameRemoteClient::new, PORT);
        server.start();
        boolean stop = false;
        //noinspection ConstantConditions
        while (!stop)
        {
            Util.sleep(1000);
        }
        server.stop();
    }

    @Test
    public void testConnect()
    {
        Server<TestRemoteClient> server = startServer();
        TestClient client = startClient();
        TestRemoteClient remoteClient = getRemoteClient(server);

        Assert.assertTrue(remoteClient.connected);
        Assert.assertTrue(client.connected);

        server.stop();
    }

    @Test
    public void testServerDisconnect()
    {
        Server<TestRemoteClient> server = startServer();
        TestClient client = startClient();
        TestRemoteClient remoteClient = getRemoteClient(server);

        server.stop();

        assertDisconnected(server, remoteClient, client);
    }

    @Test
    public void testClientDisconnect()
    {
        Server<TestRemoteClient> server = startServer();
        TestClient client = startClient();
        TestRemoteClient remoteClient = getRemoteClient(server);

        client.stop();

        assertDisconnected(server, remoteClient, client);

        server.stop();
    }

    @Test
    public void testMessaging()
    {
        Server<TestRemoteClient> server = startServer();
        TestClient client = startClient();
        TestRemoteClient remoteClient = getRemoteClient(server);

        client.send("Message");

        Util.sleep(100);

        Assert.assertTrue(remoteClient.receivedMessage);
        Assert.assertTrue(client.receivedMessage);

        server.stop();
    }

    private TestRemoteClient getRemoteClient(Server<TestRemoteClient> server)
    {
        Util.sleep(100);

        return server.getClients().get(0);
    }

    private Server<TestRemoteClient> startServer()
    {
        Server<TestRemoteClient> server = new Server<>(TestRemoteClient::new, PORT);
        server.start();
        return server;
    }

    private TestClient startClient()
    {
        TestClient client = new TestClient("127.0.0.1", PORT, LOCAL_PORT);
        client.start();
        return client;
    }

    private void assertDisconnected(Server server, TestRemoteClient remoteClient, TestClient client)
    {
        Util.sleep(100);

        Assert.assertTrue(server.getClients().isEmpty());
        Assert.assertTrue(remoteClient.disconnected);
        Assert.assertTrue(client.disconnected);
    }

    private class TestRemoteClient extends RemoteClient<TestRemoteClient>
    {
        private boolean receivedMessage = false;

        private boolean connected = false;

        private boolean disconnected = false;

        public TestRemoteClient(Server<TestRemoteClient> server, Socket socket) throws IOException
        {
            super(server, socket);
            connected = true;
        }

        @Override
        protected void receive(String message)
        {
            receivedMessage = true;
            send(message);
        }

        @Override
        protected void disconnected()
        {
            disconnected = true;
        }
    }

    private class TestClient extends Client
    {
        private boolean receivedMessage = false;

        private boolean connected = false;

        private boolean disconnected = false;

        public TestClient(String address, int port, int localPort)
        {
            super(address, port, localPort);
            connected = true;
        }

        @Override
        protected void receive(String message)
        {
            receivedMessage = true;
        }

        @Override
        protected void disconnected()
        {
            disconnected = true;
        }
    }
}
