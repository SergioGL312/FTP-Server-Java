package paquete;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ManejadorDeCliente implements Runnable {

	private final Socket clienteSocket;

	public ManejadorDeCliente(Socket clienteSocket) throws IOException {
		this.clienteSocket = clienteSocket;
	}

	@Override
	public void run() {
		try (DataInputStream entrada = new DataInputStream(clienteSocket.getInputStream());
				DataOutputStream salida = new DataOutputStream(clienteSocket.getOutputStream())) {

			while (true) {

				String comando = entrada.readUTF();
				System.out.println("[ + ] Comando recibido del cliente " + clienteSocket.getRemoteSocketAddress() + ": "
						+ comando);

				String respuesta = "";

				switch (comando.toUpperCase()) {
				case "?":
				case "HELP":
					respuesta = "\tCLOSE\n" + "\tGET <archivo>\n" + "\tPUT <archivo>\n" + "\tLCD <directorio>\n"
							+ "\tCD <directorio>\n" + "\tLS\n" + "\tDELETE <archivo/s>\n" + "\tMPUT <archivos>\n"
							+ "\tMGET <archivos>\n" + "\tRMDIR <nomDir>\n" + "\tPWD\n";
					break;

				case "CLOSE": // 1
					respuesta = "Sesion cerrada";

					salida.writeUTF(respuesta);

					clienteSocket.close();
					break;

				case "GET ": // 2
					respuesta = "GET archivo";
					break;

				case "PUT ": // 3
					respuesta = "PUT archivo";
					break;

				case "LCD ": // 4
					respuesta = "LCD directorio";
					break;

				case "CD ": // 5
					respuesta = "CD directorio";
					break;

				case "LS": // 6
					respuesta = "Listado de archivos y directorios";
					break;

				case "DELETE ": // 7
					respuesta = "DELETE archivo/s";
					break;

				case "MPUT ": // 8
					respuesta = "MPUT archivos";
					break;

				case "MGET ": // 9
					respuesta = "MGET archivos";
					break;

				case "RMDIR ": // 10
					respuesta = "RMDIR nomDir";
					break;

				case "PWD": // 11
					respuesta = "Directorio actual";
					break;

				default:
					// throw new IllegalArgumentException("Unexpected value: " +
					// comando.toUpperCase());
					respuesta = "[ - ] " + comando + ": Comando no encontrado";
				}

				salida.writeUTF(respuesta);

			}
		} catch (IOException e) {
			System.out.println(
					"[ * ] El cliente " + clienteSocket.getRemoteSocketAddress() + " ha cerrado la conexión.\n");
		}
	}
}
