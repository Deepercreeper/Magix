package org.deepercreeper.server;

import java.io.IOException;

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
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return message;
    }
}
