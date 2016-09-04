package org.deepercreeper.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;

public class FrameRemoteClient extends RemoteClient<FrameRemoteClient>
{
    private final JFrame frame = new JFrame("Server");

    public FrameRemoteClient(Server<FrameRemoteClient> server, Socket socket) throws IOException
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
                    String message = field.getText();
                    send(message);
                    System.out.println("Sent: " + message);
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
        System.out.println("Received: " + message);
        frame.setVisible(true);
    }

    @Override
    protected void disconnected()
    {
        System.out.println("Disconnected");
    }
}
