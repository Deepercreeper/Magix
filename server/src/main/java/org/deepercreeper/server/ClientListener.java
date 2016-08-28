package org.deepercreeper.server;

import java.net.SocketTimeoutException;

class ClientListener<C extends Client> implements Runnable
{
    private final Server<C> server;

    private final ClientFactory<C> clientFactory;

    ClientListener(Server<C> server, ClientFactory<C> clientFactory)
    {
        this.server = server;
        this.clientFactory = clientFactory;
    }

    @Override
    public void run()
    {
        while (server.isRunning() && server.isConnected())
        {
            accept();
        }
    }

    private void accept()
    {
        try
        {
            C client = clientFactory.create(server.getSocket().accept());
            server.add(client);
            client.init(() -> server.remove(client));
        }
        catch (SocketTimeoutException ignored)
        {
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
