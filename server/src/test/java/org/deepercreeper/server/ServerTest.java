package org.deepercreeper.server;

import org.deepercreeper.common.util.Util;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.Socket;

public class ServerTest
{
    @Test
    public void testServer()
    {
        int port = 8080;
        Server<TestClient> server = new Server<>(TestClient::new, port);
        server.start();
        while (true)
        {
            Util.sleep(1000);
        }
    }

    private class TestClient extends Client<TestClient>
    {
        private final JFrame frame = new JFrame("Server");

        public TestClient(Server<TestClient> server, Socket socket) throws Exception
        {
            super(server, socket);
            System.out.println("Connected");
            init();
        }

        private void init()
        {
            frame.setLayout(new BorderLayout());
            JTextField field = new JTextField();
            frame.add(field);
            field.setPreferredSize(new Dimension(500, 24));
            field.addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyPressed(KeyEvent e)
                {
                    if (e.getExtendedKeyCode() == KeyEvent.VK_ENTER)
                    {
                        send(field.getText());
                        field.setText("");
                    }
                }
            });
            frame.pack();
            frame.setVisible(true);
        }

        @Override
        protected void receive(String message)
        {
            System.out.println(message);
            frame.setVisible(true);
        }

        @Override
        protected void disconnected()
        {
            System.out.println("Disconnected");
        }
    }
}
