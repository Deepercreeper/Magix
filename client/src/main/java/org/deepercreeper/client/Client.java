package org.deepercreeper.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Client
{
    private final String address;

    private final int port;

    private Socket socket = null;

    private boolean running = false;

    private PrintWriter out;

    private BufferedReader in;

    public Client(String address, int port)
    {
        this.address = address;
        this.port = port;
    }

    public final boolean isRunning()
    {
        return running;
    }

    public final boolean isConnected()
    {
        return socket != null && !socket.isClosed() && socket.isConnected();
    }

    public final void send(String message)
    {
        out.write(message);
    }

    public final void start()
    {
        if (isRunning() || isConnected())
        {
            throw new IllegalStateException("Cannot start client when still running or connected");
        }
        running = true;
        connect();
    }

    public final void stop()
    {
        if (!isRunning())
        {
            throw new IllegalStateException("Cannot stop when not running");
        }
        running = false;
        disconnect();
        disconnected();
    }

    final void closed()
    {
        running = false;
        disconnected();
    }

    final BufferedReader getIn()
    {
        return in;
    }

    private void connect()
    {
        try
        {
            socket = new Socket(address, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(new MessageListener(this)).start();
        }
        catch (IOException e)
        {
            running = false;
            socket = null;
            out = null;
            in = null;
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
        out = null;
        in = null;
    }

    protected abstract void receive(String message);

    protected abstract void disconnected();
}
