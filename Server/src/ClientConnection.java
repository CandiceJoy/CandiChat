import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientConnection extends Thread
{
	private InputStream in;
	private OutputStream out;
	private BufferedReader reader;
	private PrintWriter writer;
	private String client_name;
	private LoginManager login_manager;
	
	public ClientConnection( Socket socket, LoginManager login_manager_pass )
	{
		in = null;
		out = null;
		login_manager = login_manager_pass;
		
		try
		{
			in = socket.getInputStream();
			out = socket.getOutputStream();
			reader = new BufferedReader( new InputStreamReader( in ) );
			writer = new PrintWriter( out, true );
		}
		catch( Exception e )
		{
			e.printStackTrace();
			System.exit( 0 );
		}
		
		if( in == null || out == null || reader == null || writer == null )
		{
			System.out.println( "Null stream!" );
			System.exit( 0 );
		}
	}
	
	public void negotiateName()
	{
		if( client_name != null )
		{
			return;
		}
		
		while( client_name == null )
		{
			send( "What name would you like?" );
			String requested_name = receive();
			
			if( requested_name == null )
			{
				return;
			}
			
			if( isValidName( requested_name ) )
			{
				client_name = requested_name;
			}
		}
	}

	private boolean isValidName( String name )
	{		
		if( name.length() < Server.MINIMUM_NAME_LENGTH )
		{
			send( "That name is too short." );
			return false;
		}
		
		if( name.length() > Server.MAXIMUM_NAME_LENGTH )
		{
			send( "That name is too long." );
			return false;
		}
		
		for( ClientConnection client : login_manager.getClients() )
		{
			if( client.getClientName().toLowerCase().equals( name.toLowerCase() ) )
			{
				send( "That name is already in use.");
				return false;
			}
		}
		
		for( String test : Server.BANNED_NAMES )
		{
			if( name.toLowerCase().contains( test.toLowerCase() ) )
			{
				send( "That name is on the list of banned names." );
				return false;
			}
		}
		
		return true;
	}
	
	public String getClientName()
	{
		return client_name;
	}
	
	public String receive()
	{
		String message = null;
		
		try
		{
			message = reader.readLine();
		}
		catch( SocketException e )
		{
			login_manager.disconnect( this );
			return null;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			System.exit( 0 );
		}
		
		return message;
	}
	
	public void send( String s )
	{
		try
		{
			writer.println( s );
		}
		catch( Exception e )
		{
			e.printStackTrace();
			System.exit( 0 );
		}
	}
	
	public void run()
	{
		while( true )
		{
			String message = receive();
			
			if( message == null )
			{
				break;
			}
			
			Server.process( this, message );
		}
	}
}