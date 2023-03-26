package paquete;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
	
	private static final String HOST = "localhost";
	private static final int PORT = 1090;
	private Scanner scanner;
	private String comandos;

	public Cliente() {
		comandos = "";
		scanner = new Scanner(System.in);
	}

	public void iniciarCliente() {
		try {
			Socket miSocket = new Socket(HOST, PORT);
			
			DataOutputStream flujoSalida = new DataOutputStream(miSocket.getOutputStream());
			DataInputStream flujoEntrada = new DataInputStream(miSocket.getInputStream());
			
			while (true) {
				System.out.print("> ");
				comandos = scanner.nextLine();
				
				flujoSalida.writeUTF(comandos); // Envia el cmd al servidor
				
				String respuestaServidor = flujoEntrada.readUTF();
				System.out.println(respuestaServidor);
				
				if (comandos.equalsIgnoreCase("close")) {
	                break;
	            }
			}
			
			
			
			flujoSalida.close();
			flujoEntrada.close();
		} catch(Exception e) {
			System.out.println("[ - ] " + e.getMessage() + " - Servidor no esta en linea");
		}
	}
	
	public static void main(String[] args) {
		Cliente cliente = new Cliente();
		
		cliente.iniciarCliente();
	}

	
}
