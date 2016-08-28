package org.deepercreeper.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Server<C extends Client<C>>
{
    private final List<C> clients = new Vector<>();

    private final ClientFactory<C> clientFactory;

    private final int port;

    private ServerSocket socket = null;

    private boolean running = false;

    public Server(ClientFactory<C> clientFactory, int port)
    {
        this.port = port;
        this.clientFactory = clientFactory;
    }

    public final boolean isRunning()
    {
        return running;
    }

    public final boolean isConnected()
    {
        return socket != null && !socket.isClosed();
    }

    public final List<C> getClients()
    {
        return new ArrayList<>(clients);
    }

    public final void start()
    {
        if (isRunning() || isConnected())
        {
            throw new IllegalStateException("Cannot start server when still running or connected");
        }
        running = true;
        connect();
    }

    public final void stop()
    {
        if (!isRunning())
        {
            throw new IllegalStateException("Cannot stop not running server");
        }
        running = false;
        clients.forEach(C::stop);
        disconnect();
    }

    final ServerSocket getSocket()
    {
        return socket;
    }

    final void add(C client)
    {
        clients.add(client);
    }

    final void remove(C client)
    {
        clients.remove(client);
    }

    private void connect()
    {
        try
        {
            socket = new ServerSocket(port);
            socket.setSoTimeout(10 * 1000);
            new Thread(new ClientListener<>(this, clientFactory)).start();
        }
        catch (IOException e)
        {
            running = false;
            socket = null;
            throw new RuntimeException(e);
        }
    }

    private void disconnect()
    {
        if (socket == null)
        {
            return;
        }
        try
        {
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        socket = null;
    }
}
