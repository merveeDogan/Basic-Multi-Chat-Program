package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Socket client;
	private BufferedReader bfReader;
	private BufferedWriter bfWriter;
	private String clientName;

	public ClientHandler(Socket clientSocket) {
		try {
			this.client = clientSocket;
			this.bfReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			this.bfWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			this.clientName = bfReader.readLine();
			clientHandlers.add(this);
			broadcastMessage(clientName + " is connected!");
		} catch (Exception e) {
			
		}
	}

	@Override
	public void run() {
		String message;
		while (client.isConnected()) {
			try {
				message = bfReader.readLine();
				broadcastMessage(message);

			} catch (Exception e) {
				
				break;
			}
		}

	}

	private void broadcastMessage(String message) {
		for (ClientHandler clientHandler : clientHandlers) {
			try {
				if (!clientHandler.clientName.equals(clientName)) {
					clientHandler.bfWriter.write(message);
					clientHandler.bfWriter.newLine();
					clientHandler.bfWriter.flush();

				}

			} catch (Exception e) {
				
			}
		}

	}

	public void removeClient() { // NULL CHECKİ GEREKEBİLİR
		if (!clientHandlers.isEmpty()) {
			clientHandlers.remove(this);
			broadcastMessage(this.clientName + " has left the server");
		} else {
			
		}

	}

	
}
