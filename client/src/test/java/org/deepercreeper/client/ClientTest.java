package org.deepercreeper.client;

import org.deepercreeper.common.util.Util;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ClientTest
{
    @Test
    public void testClient()
    {
        String address = "127.0.0.1";
        int port = 8080;
        int localPort = 8081;

        Client client = new TestClient(address, port, localPort);
        client.start();

        listen(client);

        client.stop();
    }

    private void listen(Client client)
    {
        JFrame frame = new JFrame("Client");
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
                    client.send(field.getText());
                    field.setText("");
                }
            }
        });
        frame.pack();
        frame.setVisible(true);
        while (frame.isVisible())
        {
            Util.sleep(100);
        }
    }

    private class TestClient extends Client
    {
        public TestClient(String address, int port, int localPort)
        {
            super(address, port, localPort);
        }

        public TestClient(String address, int port)
        {
            super(address, port);
        }

        @Override
        protected void receive(String message)
        {
            System.out.println(message);
        }

        @Override
        protected void disconnected()
        {
            System.out.println("Disconnected");
        }
    }
}
