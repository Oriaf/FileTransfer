import java.net.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;


public class Send{
	private static String dest;
	private static String source;
	private static int port = 6660;
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
	static void sendFile(Socket socket){
		BufferedInputStream sourceIn = null;
		BufferedOutputStream out = null;
		try{
			//Read from the file
			File sourceFile = new File(source);
			sourceIn = new BufferedInputStream(
						new FileInputStream(sourceFile));

			//Access the connection's output stream
			out = new BufferedOutputStream(socket.getOutputStream());

			//Transfer the contents of the file
			while(sourceIn.available() > 0){
				out.write(sourceIn.read());
			}
		}
		catch(IOException exception){
			System.out.println("Error: Failed to read the specified file!");
			System.out.println(exception);

			return;
		}
		finally {
			try{
				if(sourceIn != null) sourceIn.close();
				if(out != null) out.close();
			}
			catch(IOException exception){
				System.out.println(exception);
			}
		}
	}

	//Connect to the server at the destination
	static void connect(){
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

		sendFile(socket);

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
				case "--port":
				case "-p":
					port = Integer.parseInt(param);
					break;
				case "--source":
				case "-s":
					source = param;
					break;
			}
		}
	}

	public static void main(String args[]){
		parse(args);
		connect();
	}
}
