package org.deepercreeper.server;

import java.net.Socket;

public interface ClientFactory<T extends Client>
{
    T create(Socket socket) throws Exception;
}
