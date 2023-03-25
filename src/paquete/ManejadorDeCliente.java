package paquete;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ManejadorDeCliente implements Runnable {
	
    private final Socket clienteSocket;

    public ManejadorDeCliente(Socket clienteSocket) {
        this.clienteSocket = clienteSocket;
    }

    @Override
    public void run() {
        try (DataInputStream entrada = new DataInputStream(clienteSocket.getInputStream())) {
            while (true) {
                String comando = entrada.readUTF();
                System.out.println("[ + ] Comando recibido del cliente " + clienteSocket.getRemoteSocketAddress() + ": " + comando);
            }
        } catch (IOException e) {
            System.out.println("[ * ] El cliente " + clienteSocket.getRemoteSocketAddress() + " ha cerrado la conexión.\n");
        }
    }
}
