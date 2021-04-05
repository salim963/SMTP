//Marc-Alexandru Baetica
//Scott Ryan

//this was a side project to edit and fix the SMTPConnection.java class as to provide a working implementation of the application having been already provided the JFrame and message envelope

//Workflow logic:
//this class gets called and the SMTPConnection is created and a socket is set up
//a stream is set up to read and write to the socket
//connected to the default SMPT port 25
//if our server responds OK the SMPT handshake is established and message is sent
//the SMTP connection closes
//the socket closes

//Commands and (Replies):
//DATA(354), HELO (250), MAIL FROM (250), QUIT (221), RCPT TO (250) 


import java.net.*;
import java.io.*;
import java.util.*;


/**
* Open an SMTP connection to a remote machine and send one mail.
*
*/

public class SMTPConnection {
	/* The socket to the server */
	private Socket connection;
	/* Streams for reading and writing the socket */
	private BufferedReader fromServer;
	private DataOutputStream toServer;
	private static final int SMTP_PORT = 1025;
	private static final String CRLF = "\r\n";
	/* Are we connected? Used in close() to determine what to do. */
	private boolean isConnected = false;
	
	/* Create an SMTPConnection object. Create the socket and the associated streams. Initialize SMTP connection. */
	public SMTPConnection(Envelope envelope) throws IOException {
		
		connection = new Socket("localhost",SMTP_PORT); //create standard socket connection on port 25
		fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		toServer = new DataOutputStream(connection.getOutputStream());

		/* Read a line from server and check that the reply code is	220. If not, throw an IOException. */
		String text = fromServer.readLine();
		System.out.println("the code we gat from server is " + parseReply(text));
		if (parseReply(text) != 220)
		throw new IOException("Reply code not 220");
		System.out.println("Reply code not 220");
		
		/* SMTP handshake. We need the name of the local machine. Send the appropriate SMTP handshake command. */
		String localhost = "localhost";
		sendCommand("HELO "+localhost+ CRLF , 250);
		isConnected = true;
	}
	
	/* Send the message. Write the correct SMTP-commands in the	correct order. No checking for errors, just throw them to the caller. */
	public void send(Envelope envelope) throws IOException {
		/* Send all the necessary commands to send a message. Call sendCommand() to do the dirty work. Do _not_ catch the exception thrown from sendCommand(). */
		sendCommand("MAIL FROM: " + envelope.Sender,250);
	    sendCommand("RCPT TO: " + envelope.Recipient,250);
	    sendCommand("DATA" ,354);
	    sendCommand(envelope.Message.toString(),250);
	    sendCommand(".",250);
	}

	/* Close the connection. First, terminate on SMTP level, then close the socket. */
	public void close() {
		isConnected = false;
		try {
			sendCommand("QUIT" + CRLF, 221);
			connection.close();
		} catch (IOException e) {
			System.out.println("Unable to close connection: " + e);
			isConnected = true;
		}
	}
	/* Send an SMTP command to the server. Check that the reply code is what is is supposed to be according to RFC 821. */
	private void sendCommand(String command, int rc) throws
	IOException	{
		/* Write command to server and read reply from server. */
		System.out.println("Command to server: " + command +CRLF);
	    toServer.writeBytes(command+CRLF);
	    System.out.println("Server reply: " + fromServer.readLine());
	    
	    /* Check that the server's reply code is the same as the parameter rc. If not, throw an IOException. */
	    /*if (parseReply(fromServer.readLine()) != rc){
	        System.out.println("The reply code is not the same as the rc");
	        throw new IOException("The reply code is not the same as the rc");
	    }*/
	}
	
	/* Parse the reply line from the server. Returns the reply code. */
	private int parseReply(String reply) {
		StringTokenizer tokens = new StringTokenizer(reply," ");
	    String rc = tokens.nextToken();
	    return Integer.parseInt(rc);
	}
	
	/* Destructor. Closes the connection if something bad happens. */
	protected void finalize() throws Throwable {
		if(isConnected) {
			close();
		}
		super.finalize();
	}
} 