package br.facape.sistemas.distribuidos.chat.server.services;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import br.facape.sistemas.distribuidos.chat.utils.beans.Message;
import br.facape.sistemas.distribuidos.chat.utils.beans.User;
import br.facape.sistemas.distribuidos.chat.utils.services.interfaces.IChatService;

public class ChatService extends UnicastRemoteObject implements IChatService {

	private static final long serialVersionUID = 7697627873101803687L;
	private static final Logger LOGGER = Logger.getLogger(ChatService.class);

	private List<User> connectedUsers;
	private List<Message> messages;
	private SimpleDateFormat dateFormat;

	public ChatService() throws RemoteException {
		super();
		LOGGER.info("Instanciando servico de chat.");
		this.connectedUsers = new ArrayList<>();
		this.messages = new ArrayList<>();
		this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		LOGGER.info("Servico de chat isntanciado com sucesso!");
	}

	private User getUserWithNickname(String nickname) {
		for (User u : this.connectedUsers) {
			if (u.getNickname().equalsIgnoreCase(nickname.trim()))
				return u;
		}
		return null;
	}

	@Override
	public boolean connect(User user) throws RemoteException {
		LOGGER.info(String.format("Novo usuario tentando se conectar: %s", user));
		if (getUserWithNickname(user.getNickname()) != null) {
			LOGGER.error(String.format("Ja existe um usuario conectado com o nickname %s!", user.getNickname().toUpperCase()));
			return false;
		}
		this.connectedUsers.add(user);
		Date date = new Date();
		String nickname = user.getNickname().toUpperCase();
		String host = user.getHost();
		String text = String.format("[%s] %s(%s) ENTROU!", this.dateFormat.format(date), nickname, host);
		Message m = new Message(user, text, date);
		sendMessage(m);
		LOGGER.info(text);
		return true;
	}

	@Override
	public boolean disconnect(User user) throws RemoteException {
		LOGGER.info(String.format("Usuario tentando se desconectar: %s", user));
		if(getUserWithNickname(user.getNickname()) == null) {
			LOGGER.error(String.format("Usuario nao encontrado : %s", user));
			return false;
		}
		this.connectedUsers.remove(getUserWithNickname(user.getNickname()));
		Date date = new Date();
		String nickname = user.getNickname().toUpperCase();
		String host = user.getHost();
		String text = String.format("[%s] %s(%s) SAIU!", this.dateFormat.format(date), nickname, host);
		Message m = new Message(user, text, date);
		sendMessage(m);
		LOGGER.info(text);
		return true;
	}

	@Override
	public void sendMessage(Message message) throws RemoteException {
		LOGGER.info(String.format("Nova mensagem: %s", message));
		this.messages.add(message);
	}

	@Override
	public List<Message> getMessages() throws RemoteException {
		return this.messages.size() > 0 ? this.messages : null;
	}

	@Override
	public List<User> getConnectedUsers() throws RemoteException {
		return this.connectedUsers.size() > 0 ? this.connectedUsers : null;
	}

}
