package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import Date.Date;

public class Client {
	private Socket socket;
	private BufferedReader bfReader;
	private BufferedWriter bfWriter;
	private String clientName;

	public Client(Socket socket, String clientName) {
		try {
			this.socket = socket;
			this.bfReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.bfWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.clientName = clientName;

		} catch (Exception e) {
			closeAll(socket, bfReader, bfWriter);
		}
	}

	public void messageToOtherClients() {
		try {
			LocalTime localTime = LocalTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
			String localTimeString = localTime.format(formatter);
			String file = Date.createFileName();
			
			bfWriter.write(clientName);
			bfWriter.newLine();
			bfWriter.flush();
			Scanner scan = new Scanner(System.in);
			while (socket.isConnected()) {
				String message = scan.nextLine();
				bfWriter.write(clientName + ": " + message);
				bfWriter.newLine();
				bfWriter.flush();
				Date.writeIntoFile(file,localTimeString);
				Date.writeIntoFile(file, " "+clientName + ": " + message+ "\n");
			}
			scan.close();
		} catch (Exception e) {

		}
	}

	public void listenOtherClients() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String message;
				while (socket.isConnected()) {
					try {
						message = bfReader.readLine();
						System.out.println(message);
					} catch (Exception e) {
						closeAll(socket, bfReader, bfWriter);
					}
				}

			}
		}).start();
	}

	private void closeAll(Socket socket, BufferedReader bfReader, BufferedWriter bfWriter) {
		try {
			if (socket != null) {
				socket.close();
			}
			if (bfReader != null) {
				bfReader.close();
			}
			if (bfWriter != null) {
				bfWriter.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter client name");
		String clientName = scan.nextLine();
		Socket socket = new Socket("localhost", 1235);
		Client client = new Client(socket, clientName);
		client.listenOtherClients();
		client.messageToOtherClients();
		 scan.close();

	}

}
