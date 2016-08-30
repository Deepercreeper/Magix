package org.deepercreeper.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class RemoteClient<C extends RemoteClient<C>>
{
    private final Server<C> server;

    private final Socket socket;

    private final PrintWriter out;

    private final BufferedReader in;

    private Runnable closeAction;

    private boolean running = true;

    public RemoteClient(Server<C> server, Socket socket) throws IOException
    {
        this.server = server;
        this.socket = socket;
        socket.setReuseAddress(true);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public final boolean isRunning()
    {
        return running;
    }

    public final boolean isConnected()
    {
        return !socket.isClosed() && socket.isConnected();
    }

    public Server<C> getServer()
    {
        return server;
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

    public final void stop()
    {
        if (!isRunning())
        {
            throw new IllegalStateException("Cannot stop server when not running");
        }
        close();
    }

    final void close()
    {
        running = false;
        disconnect();
        disconnected();
        closeAction.run();
    }

    final void init(Runnable closeAction)
    {
        this.closeAction = closeAction;
        new Thread(new MessageListener(this)).start();
    }

    final BufferedReader getIn()
    {
        return in;
    }

    private void disconnect()
    {
        try
        {
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected abstract void receive(String message);

    protected abstract void disconnected();
}
