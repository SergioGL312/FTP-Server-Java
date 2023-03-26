package paquete;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	
    private static final int PUERTO = 1090;

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado. Esperando conexiones . . .\n");

            while (true) {
                Socket clienteSocket = servidor.accept();
                System.out.println("[ * ] Nuevo cliente conectado: " + clienteSocket.getRemoteSocketAddress());

                ManejadorDeCliente manejadorDeCliente = new ManejadorDeCliente(clienteSocket);
                new Thread(manejadorDeCliente).start();
            }

        } catch (IOException e) {
            System.err.println("[ - ] Error al iniciar el servidor: " + e.getMessage());
        }
    }
}

