package org.deepercreeper.client;

import java.io.IOException;
import java.net.SocketException;

class MessageListener implements Runnable
{
    private final Client client;

    MessageListener(Client client)
    {
        this.client = client;
    }

    @Override
    public void run()
    {
        while (client.isRunning() && client.isConnected())
        {
            receive();
        }
    }

    private void receive()
    {
        String message = readLine();
        if (!client.isRunning())
        {
            return;
        }
        if (message != null)
        {
            client.receive(message);
        }
        else
        {
            client.close();
        }
    }

    private String readLine()
    {
        String message = null;
        try
        {
            message = client.getIn().readLine();
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
