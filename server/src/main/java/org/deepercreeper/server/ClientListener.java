package org.deepercreeper.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

class ClientListener<C extends RemoteClient<C>> implements Runnable
{
    private final Server<C> server;

    private final RemoteClientFactory<C> remoteClientFactory;

    ClientListener(Server<C> server, RemoteClientFactory<C> remoteClientFactory)
    {
        this.server = server;
        this.remoteClientFactory = remoteClientFactory;
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
            Socket socket = server.getSocket().accept();
            C client = remoteClientFactory.create(server, socket);
            server.add(client);
            client.init(() -> server.remove(client));
        }
        catch (SocketTimeoutException ignored)
        {
        }
        catch (SocketException e)
        {
            if (!e.getMessage().equals("Socket operation on nonsocket: configureBlocking"))
            {
                e.printStackTrace();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
