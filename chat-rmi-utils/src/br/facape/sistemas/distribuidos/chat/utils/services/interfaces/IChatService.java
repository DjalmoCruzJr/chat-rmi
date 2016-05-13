package br.facape.sistemas.distribuidos.chat.utils.services.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import br.facape.sistemas.distribuidos.chat.utils.beans.Message;
import br.facape.sistemas.distribuidos.chat.utils.beans.User;

public interface IChatService extends Remote {
	
	public boolean connect(User user) throws RemoteException;

	public boolean disconnect(User user) throws RemoteException;
	
	public void sendMessage(Message message) throws RemoteException;

	public List<Message> getMessages() throws RemoteException;

	public List<User> getConnectedUsers() throws RemoteException;

}
