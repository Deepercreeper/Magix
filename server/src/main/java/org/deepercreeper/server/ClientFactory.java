package org.deepercreeper.server;

import java.net.Socket;

public interface ClientFactory<C extends Client<C>>
{
    C create(Server<C> server, Socket socket) throws Exception;
}
