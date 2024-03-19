package demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

interface ChatService extends Remote {
    boolean setPassword(String username, String password) throws RemoteException;
    boolean login(String username, String password) throws RemoteException;
    boolean logout(String username) throws RemoteException;
    void sendMessage(String username, String message) throws RemoteException;
}

class ChatServiceImpl extends UnicastRemoteObject implements ChatService {
    private static final long serialVersionUID = 1L;

    public ChatServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean setPassword(String username, String password) throws RemoteException {
        // Code to set password for the given username
        return true;
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        // Code to perform login with the given username and password
        return true;
    }

    @Override
    public boolean logout(String username) throws RemoteException {
        // Code to perform logout for the given username
        return true;
    }

    @Override
    public void sendMessage(String username, String message) throws RemoteException {
        // Code to send message
        System.out.println(username + ": " + message);
    }
}

public class kt12 extends JFrame {
    private JButton sendButton;
    private JButton sendImageButton;
    private JButton sendFileButton;

    private ChatService chatService;
    private String username;

    public kt12(ChatService chatService, String username) {
        this.chatService = chatService;
        this.username = username;

        setTitle("Chat Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        sendButton = new JButton("Send Message");
        sendImageButton = new JButton("Send Image");
        sendFileButton = new JButton("Send File");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = JOptionPane.showInputDialog(kt12.this, "Enter message:");
                if (message != null && !message.isEmpty()) {
                    try {
                        chatService.sendMessage(username, message);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        sendImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(kt12.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Code to send image
                    ImageIcon imageIcon = new ImageIcon(selectedFile.getPath());
                    JOptionPane.showMessageDialog(kt12.this, new JLabel(imageIcon));
                }
            }
        });

        sendFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(kt12.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Code to send file
                    JOptionPane.showMessageDialog(kt12.this, "File downloaded successfully.");
                }
            }
        });

        add(sendButton);
        add(sendImageButton);
        add(sendFileButton);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            ChatServiceImpl chatService = new ChatServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ChatService", chatService);

            String username = JOptionPane.showInputDialog(null, "Enter username:");
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new kt12(chatService, username);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}