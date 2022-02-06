import java.net.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Receive{
	private static String dest;
	private static String destDir = "out";
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
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		BufferedReader nameIn = null;
		try{
			//Create the output directory
			File dir = new File(destDir);
			if(!dir.exists()){
				if(!dir.mkdirs()){
					System.out.println("Error: Could not create the speicifed output directories!");

					return;
				}
			}

			//Read in the source file name
			//TODO: Replace with a more reliable solution
			nameIn = new BufferedReader(new InputStreamReader(
							clientSocket.getInputStream()));
			String fileName = nameIn.readLine();
			System.out.println(fileName);

			File file = new File(destDir + "/" + fileName);
			//Make sure that the file is created in the designated folder
			System.out.println(file.toPath().toAbsolutePath());
			System.out.println(dir.toPath().toAbsolutePath());
			if(!file.toPath().toAbsolutePath().startsWith(
							dir.toPath().toAbsolutePath())){
				System.out.println("WARNING: The file name specified by the client would not be created in the designated output directory. Terminating connection!");
				return;
			}

			in = new BufferedInputStream(clientSocket.getInputStream());
			out = new BufferedOutputStream(new FileOutputStream(file));

			//TODO: Change the while loop from available to instead read bytes until a protocol termination message is sent
			while(in.available() > 0){
				out.write(in.read());
			}

		}
		catch(IOException exception){
			System.out.println("Error: Failed to read client input!");
			System.out.println("\t" + exception);
		}
		finally{
			try{
				if(in != null) in.close();
				if(out != null) out.close();
				if(nameIn != null) nameIn.close();
			}
			catch(IOException exception){
				System.out.println(exception);
			}
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

		while(true){
			try{
				Socket clientSocket = socket.accept();
				receiveFile(clientSocket);		
				clientSocket.close();
			}
			catch(IOException exception){
				System.out.println("Error:");
			}
		}

		//closeConnection(socket);

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
				case "--output-dir":
				case "-o":
					destDir = param;
					break;
			}
		}
	}

	public static void main(String args[]){
		parse(args);
		listen();
	}
}
