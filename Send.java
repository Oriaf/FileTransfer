import java.net.*;
import java.io.IOException;

import java.io.PrintWriter;

public class Send{
	private static String dest;
	private static int port = 6610;
	private static int timeout = 1000;

	//Calculate a checksum around the file
	static void checksum(){

	}

	static void closeConnection(Socket socket){
		try{
			socket.close();
		}
		catch(IOException exception){
			System.out.println("Error: Failed to close socket connection!");
			System.out.println("\t" + exception);
		}
	}

	//Send the file over the network
	static void sendFile(){
		Socket socket = new Socket();
		try{
			socket.bind(null);
		}
		catch(IOException exception){
			System.out.println("Exception: " + exception);
		}
		if(!socket.isBound()){
			System.out.println("Error: Could not find a valid port and ip address!");
			closeConnection(socket);
			return;
		}

		//Resolve the given destination name to valid ip address
		InetSocketAddress destAddress = null;
		try{
			destAddress = new InetSocketAddress(InetAddress.getByName(dest), port);
		}
		catch(UnknownHostException exception){
			System.out.println("Error: Could not resolve the given destination name!");
			System.out.println("\t" + exception);

			closeConnection(socket);
			return;
		}

		//Establish a TCP connection to the destination
		try{
			socket.connect(destAddress, timeout);
		}
		catch(SocketTimeoutException exception){
			System.out.println("Connection attempt timedout!");
		}
		catch(IOException exception){
			System.out.println("Error: Could not connect to destination!");
			System.out.println("\t" + exception);
		}
		if(!socket.isConnected()){
			closeConnection(socket);
			return;
		}

		try{
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("Message");
		}
		catch(IOException exception){
			System.out.println(exception);
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
		sendFile();
	}
}
