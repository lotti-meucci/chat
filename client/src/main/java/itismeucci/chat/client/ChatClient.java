package itismeucci.chat.client;
import java.util.UUID;

import itismeucci.chat.lib.*;


public class ChatClient {

	public Schema join(String username){
		try {
			var schema = new JoinSchema(username);
			// invio al server
			return new JoinOkSchema(new UUID(0, 0));
		}
		catch (JoinException e)
		{
			return e.getResponse();
		}
		catch (Exception e) {
			return null;
		}
	}

	public void setID(UUID id){

	}

	public UUID getID(){
		return null;
	}

	/*public ListUpdateSchema list(){

	}*/

	public void exit(){

	}
}
