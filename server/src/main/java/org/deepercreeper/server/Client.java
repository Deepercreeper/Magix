package org.deepercreeper.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Client
{
    private final Socket socket;

    private final PrintWriter out;

    private final BufferedReader in;

    private Runnable closeAction;

    private boolean running = true;

    public Client(Socket socket) throws Exception
    {
        this.socket = socket;
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

    public final void send(String message)
    {
        out.write(message);
    }

    public final void stop()
    {
        running = false;
        close();
        disconnected();
        closeAction.run();
    }

    final void closed()
    {
        running = false;
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

    private void close()
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
