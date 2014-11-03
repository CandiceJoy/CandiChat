import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class LoginManager extends Thread
{
	private final int port;
	private ArrayList<ClientConnection> clients;
	
	public LoginManager( int port_requested )
	{
		port = port_requested;
		clients = new ArrayList<ClientConnection>();
	}
	
	public void disconnect( ClientConnection connection )
	{
		if( clients.remove( connection ) )
		{
			System.out.println( connection.getClientName() + " exiting!" );
			Server.globalMessageExcept( connection.getClientName() + " has left!", connection );
		}
	}

	public ArrayList<ClientConnection> getClients()
	{
		return clients;
	}
	
	public void run()
	{
		ServerSocket server = null;
		
		try
		{
			server = new ServerSocket( port );
		}
		catch( Exception e )
		{
			e.printStackTrace();
			System.exit( 0 );
		}
		
		if( server == null )
		{
			System.out.println( "ServerSocket is null!" );
			System.exit( 0 );
		}
		
		System.out.println( "Awaiting connections..." );
		
		while( true )
		{
			Socket client_socket = null;
			
			try
			{
				client_socket = server.accept();
			}
			catch( Exception e )
			{
				e.printStackTrace();
				System.exit( 0 );
			}
			
			if( client_socket == null )
			{
				continue;
			}
			
			System.out.print( "Someone is connecting..." );
			
			ClientConnection client_connection = new ClientConnection( client_socket, this );
			client_connection.negotiateName();
			
			if( client_connection.getClientName() == null )
			{
				System.out.println( "~" );
				continue;
			}
			
			client_connection.start();
			clients.add( client_connection );
			
			System.out.println( client_connection.getClientName() );
			Server.globalMessageExcept( client_connection.getClientName() + " has connected.", client_connection );
		}
	}
}