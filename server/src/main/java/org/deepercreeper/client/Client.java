package org.deepercreeper.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public abstract class Client
{
    private final String address;

    private final int port;

    private final int localPort;

    private Socket socket = null;

    private boolean running = false;

    private PrintWriter out;

    private BufferedReader in;

    public Client(String address, int port, int localPort)
    {
        this.address = address;
        this.port = port;
        this.localPort = localPort;
    }

    public Client(String address, int port)
    {
        this(address, port, -1);
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
        if (!isRunning() || !isConnected())
        {
            throw new IllegalStateException("Cannot send messages when not running or connected");
        }
        if (message.contains("\n"))
        {
            throw new IllegalArgumentException("Cannot send messages containing a console return");
        }
        out.write(message + '\n');
        out.flush();
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
        close();
    }

    final void close()
    {
        running = false;
        disconnect();
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
            socket = new Socket();
            socket.setReuseAddress(true);
            if (localPort >= 0)
            {
                socket.bind(new InetSocketAddress(localPort));
            }
            socket.connect(new InetSocketAddress(address, port));
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(new MessageListener(this)).start();
        }
        catch (IOException e)
        {
            running = false;
            disconnect();
            throw new RuntimeException(e);
        }
    }

    private void disconnect()
    {
        if (socket != null)
        {
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
        out = null;
        in = null;
    }

    protected abstract void receive(String message);

    protected abstract void disconnected();
}
