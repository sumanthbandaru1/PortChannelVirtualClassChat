package PortChannel;

import java.io.Serializable;

public class BoardMessage implements Serializable{
	private String student_Id;
	private String content;
	private String msg_Type;
	
	public BoardMessage(String student_Id, String msg_Type, String content){
		this.student_Id = student_Id;
		this.msg_Type = msg_Type;
		this.content = content;
	}
	

	/*Returns message in String Format*/
	public String toString(){
		return (student_Id + " : "+ msg_Type + " : "+ content+"\n");
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPersonId() {
		return student_Id;
	}

	public void setPersonId(String student_Id) {
		this.student_Id = student_Id;
	}
	public String getMessageType() {
		return msg_Type;
	}

	public void setMessageType(String msg_Type) {
		this.msg_Type = msg_Type;
	}
	
}
