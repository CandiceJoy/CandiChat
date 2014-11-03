import java.util.Scanner;

public class Client
{
	public static final String HOST = "localhost";	
	public static final int PORT = 4444;
	private static ServerConnection server;
	
	public static void main( String str[] )
	{
		Scanner in = new Scanner( System.in );

		server = new ServerConnection( HOST, PORT );
		server.start();
		
		while( true )
		{
			String message = in.nextLine();
			server.send( message );
		}
	}
	
	public static void process( String message )
	{		
		System.out.println( message );
	}
}