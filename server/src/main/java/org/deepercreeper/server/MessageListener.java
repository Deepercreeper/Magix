package org.deepercreeper.server;

import java.io.IOException;
import java.net.SocketException;

class MessageListener implements Runnable
{
    private final RemoteClient remoteClient;

    MessageListener(RemoteClient remoteClient)
    {
        this.remoteClient = remoteClient;
    }

    @Override
    public void run()
    {
        while (remoteClient.isRunning() && remoteClient.isConnected())
        {
            receive();
        }
    }

    private void receive()
    {
        String message = readLine();
        if (message != null)
        {
            remoteClient.receive(message);
        }
        else
        {
            remoteClient.close();
        }
    }

    private String readLine()
    {
        String message = null;
        try
        {
            message = remoteClient.getIn().readLine();
        }
        catch (SocketException e)
        {
            if (!e.getMessage().equals("Socket closed"))
            {
                e.printStackTrace();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return message;
    }
}
