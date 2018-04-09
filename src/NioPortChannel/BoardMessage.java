package NioPortChannel;

import java.io.Serializable;

public class BoardMessage implements Serializable{
	private String personId;
	private String content;
	private String messageType;
	
	public BoardMessage(String personId, String messageType, String content){
		this.personId = personId;
		this.messageType = messageType;
		this.content = content;
	}
	

	/*Returns message in String Format*/
	public String toString(){
		return (personId + " : "+ messageType + " : "+ content+"\n");
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
}
