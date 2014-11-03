import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ServerConnection extends Thread
{
	private InputStream in;
	private OutputStream out;
	private BufferedReader reader;
	private PrintWriter writer;
	
	public ServerConnection( String host, int port )
	{	
		in = null;
		out = null;
		
		Socket socket = null;

		try
		{
			socket = new Socket( host, port );
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
		
		if( socket == null || in == null || out == null || reader == null || writer == null )
		{
			System.out.println( "Null stream!" );
			System.exit( 0 );
		}
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
			System.out.println( "Disconnected from server." );
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
			
			Client.process( message );
		}
		
		System.exit( 0 );
	}
}