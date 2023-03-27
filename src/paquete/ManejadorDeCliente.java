package paquete;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class ManejadorDeCliente implements Runnable {

	private final Socket clienteSocket;
	private static final String RUTA = "C:\\Users\\ricar\\Visual Studio Code\\Java\\FTP Server Optimizado\\directorio_trabajo\\";
	private String rutaActual = "";
	
	DataInputStream entrada;
	DataOutputStream salida; 

	public ManejadorDeCliente(Socket clienteSocket) throws IOException {
		this.clienteSocket = clienteSocket;
		this.rutaActual = RUTA;
		this.entrada = new DataInputStream(clienteSocket.getInputStream());
		this.salida = new DataOutputStream(clienteSocket.getOutputStream());
	}
	
	private void getFile(String archivo) {
		try {
			File file = new File(rutaActual + archivo);
			
			if (file.exists()) {
				if (!file.isDirectory()) {
					byte [] buffer = new byte[(int)file.length()];
					
					FileInputStream fileIn = new FileInputStream(rutaActual + archivo);
					
					fileIn.read(buffer, 0, buffer.length);
					salida.write(buffer, 0, buffer.length);
					return ;
				}
			}
			salida.writeBytes("No existe el archivo o el directorio");
		} catch(IOException e) {
			System.out.println("[ - ]" + e.getMessage());
		}
		
		
	}

	private String getLS() {
		File objetos = new File(rutaActual);

		if (objetos.exists()) {
			File[] files = objetos.listFiles();
			String res = "";

			for (File file : files) {
				
				Date date = new Date(file.lastModified());
				
				if (file.isDirectory()) {

					res += date + "    <DIR>          " + file.getName() + "\n\r";
					
				} else {
					
					res += date + "                   " + file.length() + " " + file.getName() + "\n\r";
					
				}
			}

			return res;
		}
		return "No existe esa ruta";
	}

	@Override
	public void run() {
		try {

			while (true) {

				String comando = entrada.readUTF();
				System.out.println("[ + ] Comando recibido del cliente " + clienteSocket.getRemoteSocketAddress() + ": "
						+ comando);

				String respuesta = "";
				
				String[] tokens = comando.split(" ");
				
				comando = tokens[0];

				switch (comando.toLowerCase()) {
				case "?":
				case "help":
					respuesta = "\tclose\n" + "\tget <archivo>\n" + "\tput <archivo>\n" + "\tlcd <directorio>\n"
							+ "\tcd <directorio>\n" + "\tls\n" + "\tdelete <archivo/s>\n" + "\tmput <archivos>\n"
							+ "\tmget <archivos>\n" + "\trmdir <nomDir>\n" + "\tpwd\n";
					break;

				case "close": // 1

					clienteSocket.close();
					return ;

				case "get": // 2
					getFile(tokens[1]);
					break;

				case "put": // 3
					respuesta = "PUT archivo";
					break;

				case "lcd": // 4
					respuesta = "LCD directorio";
					break;

				case "cd": // 5
					respuesta = "CD directorio";
					break;

				case "ls": // 6
					respuesta = getLS();
					break;

				case "delete": // 7
					respuesta = "DELETE archivo/s";
					break;

				case "mput": // 8
					respuesta = "MPUT archivos";
					break;

				case "mget": // 9
					respuesta = "MGET archivos";
					break;

				case "rmdir": // 10
					respuesta = "RMDIR nomDir";
					break;

				case "pwd": // 11
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
