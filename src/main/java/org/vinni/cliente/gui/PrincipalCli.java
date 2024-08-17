package org.vinni.cliente.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class PrincipalCli extends javax.swing.JFrame {
    private final String SERVER_ADDRESS = "localhost"; // Change to server IP if needed
    private final int SERVER_PORT = 12345;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public PrincipalCli() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        this.setTitle("Cliente ...");

        bConectar = new javax.swing.JButton();
        bEnviar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        mensajeTxt = new JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        nombreTxt = new JTextField();
        enviarTxt = new JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        bConectar.setFont(new java.awt.Font("Segoe UI", 0, 18));
        bConectar.setText("CONECTAR");
        bConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bConectarActionPerformed(evt);
            }
        });
        getContentPane().add(bConectar);
        bConectar.setBounds(100, 80, 150, 40);

        bEnviar.setFont(new java.awt.Font("Segoe UI", 0, 18));
        bEnviar.setText("ENVIAR");
        bEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEnviarActionPerformed(evt);
            }
        });
        getContentPane().add(bEnviar);
        bEnviar.setBounds(270, 80, 150, 40);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel1.setText("Nombre:");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(20, 20, 70, 20);

        nombreTxt.setColumns(20);
        getContentPane().add(nombreTxt);
        nombreTxt.setBounds(100, 20, 320, 30);

        mensajeTxt.setColumns(30);
        mensajeTxt.setRows(5);
        jScrollPane1.setViewportView(mensajeTxt);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 130, 450, 70);

        enviarTxt.setColumns(20);
        getContentPane().add(enviarTxt);
        enviarTxt.setBounds(20, 210, 340, 30);

        setSize(new java.awt.Dimension(500, 290));
        setLocationRelativeTo(null);
    }

    private void bConectarActionPerformed(java.awt.event.ActionEvent evt) {
        new Thread(() -> {
            try {
                socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Send client name
                String nombre = nombreTxt.getText();
                if (!nombre.isEmpty()) {
                    out.println(nombre);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un nombre.");
                    return;
                }

                // Read messages from server
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                    final String message = "Server: " + serverMessage + "\n";
                    SwingUtilities.invokeLater(() -> mensajeTxt.append(message));
                }
            } catch (IOException e) {
                e.printStackTrace();
                final String errorMessage = "Error de conexión: " + e.getMessage() + "\n";
                SwingUtilities.invokeLater(() -> mensajeTxt.append(errorMessage));
            }
        }).start();
    }

    private void bEnviarActionPerformed(java.awt.event.ActionEvent evt) {
        if (out != null) {
            out.println(enviarTxt.getText());
            enviarTxt.setText("");
        } else {
            mensajeTxt.append("No estás conectado al servidor.\n");
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new PrincipalCli().setVisible(true));
    }

    private javax.swing.JButton bConectar;
    private javax.swing.JButton bEnviar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextArea mensajeTxt;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nombreTxt;
    private javax.swing.JTextField enviarTxt;
}
