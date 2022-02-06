import java.net.*;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Receive{
	private static String dest;
	private static int port = 6660;
	private static int timeout = 1000;

	//Calculate a checksum around the file
	static void checksum(){

	}

	static void closeConnection(ServerSocket socket){
		try{
			socket.close();
		}
		catch(IOException exception){
			System.out.println("Error: Failed to close socket connection!");
			System.out.println("\t" + exception);
		}
	}

	//Send the file over the network
	static void receiveFile(Socket clientSocket){
		BufferedReader in;
		try{
			in = new BufferedReader(new InputStreamReader(
							clientSocket.getInputStream()));

			System.out.println(in.readLine());
		}
		catch(IOException exception){
			System.out.println("Error: Failed to read client input!");
			System.out.println("\t" + exception);
		}
	}

	static void listen(){
		ServerSocket socket;
		try{
			socket = new ServerSocket(port);
		}
		catch(IOException exception){
			System.out.println("Error: Could open a socket at the specified port");
			System.out.println("Exception: " + exception);
			return;
		}

		for(int i = 0; i < 10; i++){
			try{
				Socket clientSocket = socket.accept();
				receiveFile(clientSocket);		
			}
			catch(IOException exception){
				System.out.println("Error:");
			}
		}

		closeConnection(socket);

	}

	static void parse(String args[]){
		for(int i = 0; i < args.length; i += 2){
			String arg = args[i];
			if(!(i + 1 < args.length)){
				if(arg.equals("--help") || arg.equals("-h")){

				}
				else{

				}
				
				break;
			} 

			String param = args[i + 1];

			switch(arg){
				case "--help":
				case "-h":
					System.out.println("");
					i--;
					break;

				case "--destination":
				case "-d":
					dest = param;
					break;
				case "-p":
					port = Integer.parseInt(param);
					break;
			}
		}
	}

	public static void main(String args[]){
		parse(args);
		listen();
	}
}
