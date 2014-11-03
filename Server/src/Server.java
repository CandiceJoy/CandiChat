import java.util.ArrayList;


public class Server
{
	public static final int PORT = 4444;
	public static final int MINIMUM_NAME_LENGTH = 3;
	public static final int MAXIMUM_NAME_LENGTH = 15;
	public static final String[] BANNED_NAMES = { "server", "admin", "host", "~", "`", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "{", "}", "[", "]", ";", ":", "'", "\"" ,"<" ,">", ",", ".", "/", "?", "|", "\\", "shit", "fuck", "damn", "hell", "nigger", "cracker", "wetback" };
	private static final LoginManager login_manager = new LoginManager( PORT );
	
	public static void main( String str[] )
	{
		login_manager.start();
	}
	
	public static void process( ClientConnection source, String message )
	{
		System.out.println( "<" + source.getClientName() + "> " + message );
		
		globalMessageExcept( "<" + source.getClientName() + "> " + message, source );
	}
	
	public static void globalMessage( String message )
	{
		globalMessageExcept( message, null );
	}
	
	public static void globalMessageExcept( String message, ClientConnection exclusion )
	{
		ArrayList<ClientConnection> clients = login_manager.getClients();
		
		for( ClientConnection client : clients )
		{
			if( client != exclusion )
			{
				client.send( message );
			}
		}
	}
}