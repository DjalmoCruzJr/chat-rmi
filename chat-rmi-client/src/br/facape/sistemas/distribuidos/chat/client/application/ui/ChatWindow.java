package br.facape.sistemas.distribuidos.chat.client.application.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import br.facape.sistemas.distribuidos.chat.client.application.ui.interfaces.IWindow;
import br.facape.sistemas.distribuidos.chat.utils.beans.Message;
import br.facape.sistemas.distribuidos.chat.utils.beans.User;
import br.facape.sistemas.distribuidos.chat.utils.services.interfaces.IChatService;

public class ChatWindow extends JFrame implements IWindow {

	private static final long serialVersionUID = 5901508969670833304L;
	private static final Logger LOGGER = Logger.getLogger(ChatWindow.class);
	private static final String WINDOWS_LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

	private Registry registry;
	private User user;
	private IChatService chatService;
	private SimpleDateFormat dateFormat;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnSobre;
	private JMenuItem mntmSobreSistema;
	private JMenu mnSair;
	private JMenuItem mntmSairDoSistema;
	private JMenu mnServicos;
	private JMenuItem mntmIniciarServico;
	private JSeparator separator;
	private JMenuItem mntmPararServico;
	private JScrollPane scrollPane;
	private JList<String> listConnectedUsers;
	private JScrollPane scrollPane_1;
	private JTextArea textAreaMessages;
	private JPanel panel;
	private JButton btnSend;
	private JTextField textFieldMessage;

	public ChatWindow() {
		setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		this.contentPane = new JPanel();
		this.contentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.contentPane.setBackground(Color.WHITE);
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		this.scrollPane = new JScrollPane();
		this.scrollPane.setViewportBorder(
				new TitledBorder(null, "Usu\u00E1rios Conectados", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.contentPane.add(this.scrollPane, BorderLayout.WEST);

		this.listConnectedUsers = new JList<String>();
		this.listConnectedUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.scrollPane.setViewportView(this.listConnectedUsers);

		this.scrollPane_1 = new JScrollPane();
		this.scrollPane_1.setViewportBorder(
				new TitledBorder(null, "Mensagens", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.contentPane.add(this.scrollPane_1, BorderLayout.CENTER);

		this.textAreaMessages = new JTextArea();
		this.textAreaMessages.setEditable(false);
		this.scrollPane_1.setViewportView(this.textAreaMessages);

		this.panel = new JPanel();
		this.panel.setBorder(new TitledBorder(null, "Digite sua mensagem e pressione o bot\u00E3o para enviar",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.panel.setBackground(Color.WHITE);
		this.contentPane.add(this.panel, BorderLayout.SOUTH);
		this.panel.setLayout(new BorderLayout(0, 0));

		this.btnSend = new JButton("Enviar");
		this.btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		this.panel.add(this.btnSend, BorderLayout.EAST);

		this.textFieldMessage = new JTextField();
		this.panel.add(this.textFieldMessage, BorderLayout.CENTER);
		this.textFieldMessage.setColumns(10);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setAlwaysOnTop(true);
		setTitle("Chat RMI Client v1.0");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 746, 502);

		this.menuBar = new JMenuBar();
		setJMenuBar(this.menuBar);

		this.mnServicos = new JMenu("Chat");
		this.menuBar.add(this.mnServicos);

		this.mntmIniciarServico = new JMenuItem("Entrar no Chat");
		this.mntmIniciarServico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connect();
			}
		});
		this.mnServicos.add(this.mntmIniciarServico);

		this.separator = new JSeparator();
		this.mnServicos.add(this.separator);

		this.mntmPararServico = new JMenuItem("Sair do Chat");
		this.mntmPararServico.setEnabled(false);
		this.mntmPararServico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disconnect();
			}
		});
		this.mnServicos.add(this.mntmPararServico);

		this.mnSobre = new JMenu("Sobre");
		this.menuBar.add(this.mnSobre);

		this.mntmSobreSistema = new JMenuItem("Sobre o sistema");
		this.mntmSobreSistema.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AboutWindow aboutWindow = new AboutWindow();
				aboutWindow.start();
			}
		});
		this.mnSobre.add(this.mntmSobreSistema);

		this.mnSair = new JMenu("Sair");
		this.menuBar.add(this.mnSair);

		this.mntmSairDoSistema = new JMenuItem("Sair do sistema");
		this.mntmSairDoSistema.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});

		this.mnSair.add(this.mntmSairDoSistema);

		enableFields(false);

		this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		try {
			UIManager.setLookAndFeel(WINDOWS_LOOK_AND_FEEL);
		} catch (ClassNotFoundException e) {
			LOGGER.error(e);
		} catch (InstantiationException e) {
			LOGGER.error(e);
		} catch (IllegalAccessException e) {
			LOGGER.error(e);
		} catch (UnsupportedLookAndFeelException e) {
			LOGGER.error(e);
		}
	}

	private void sendMessage() {
		try {
			String text = textFieldMessage.getText().trim();
			if (text.equals(""))
				return;
			Date date = new Date();
			String nickname = this.user.getNickname().toUpperCase();
			String host = this.user.getHost();
			text = String.format("[%s] %s(%s): %s", this.dateFormat.format(date), nickname, host, text);
			Message m = new Message(this.user, text, date);
			this.chatService.sendMessage(m);
			this.textFieldMessage.setText("");
			this.textFieldMessage.requestFocus();
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
			LOGGER.error(e);
		}
	}

	private void disconnect() {
		try {
			if (this.chatService == null)
				return;
			this.chatService.disconnect(this.user);
			this.user = null;
			this.chatService = null;
			enableFields(false);
			updateConnectedUsersList(false);
			updateMessagesList(false);
			clearAllFields();
			JOptionPane.showMessageDialog(this, "Desconectado com sucesso!", "MENSAGEM",
					JOptionPane.INFORMATION_MESSAGE);
			this.mntmIniciarServico.setEnabled(true);
			this.mntmPararServico.setEnabled(false);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
			LOGGER.error(e);
		}
	}

	private void clearAllFields() {
		this.listConnectedUsers.setListData(new String[] {});
		this.textAreaMessages.setText("");
	}

	private void connect() {
		try {
			String nickname = JOptionPane.showInputDialog(this, "Informe seu nickname:");
			String serverHost = JOptionPane.showInputDialog(this, "Informe o host(ip) do servidor:");
			String serviceName = JOptionPane.showInputDialog(this, "Informe o nome do serviço:");
			int servicePort = Integer.parseInt(JOptionPane.showInputDialog(this, "Informe a porta de conexao:"));
			if (nickname == null || serverHost == null || serviceName == null || servicePort == 0) {
				String msg = "É necessário informar todos os dados corretamente para entrar no chat!";
				JOptionPane.showMessageDialog(this, msg, "AVISO", JOptionPane.WARNING_MESSAGE);
				return;
			}
			String host = InetAddress.getLocalHost().getHostAddress();
			this.user = new User(nickname, host);
			this.registry = LocateRegistry.getRegistry(serverHost, servicePort);
			this.chatService = (IChatService) this.registry.lookup(serviceName);
			if (!this.chatService.connect(this.user)) {
				String msg = "Não foi possível conectar-se ao servidor. VERIFIQUE!";
				JOptionPane.showMessageDialog(this, msg, "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(this, "Conectado com sucesso!", "MENSAGEM", JOptionPane.INFORMATION_MESSAGE);
			updateConnectedUsersList(true);
			updateMessagesList(true);
			enableFields(true);
			this.mntmIniciarServico.setEnabled(false);
			this.mntmPararServico.setEnabled(true);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
			LOGGER.error(e);
		} catch (NotBoundException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
			LOGGER.error(e);
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
			LOGGER.error(e);
		}
	}

	private void enableFields(boolean status) {
		this.btnSend.setEnabled(status);
		this.textFieldMessage.setEnabled(status);
	}

	private void updateConnectedUsersList(boolean stopFlag) {
		if (this.chatService == null)
			return;
		new Thread() {
			public void run() {
				while (stopFlag == true) {
					try {
						List<User> users = chatService.getConnectedUsers();
						if (users == null)
							return;
						String[] data = new String[users.size()];
						for (int i = 0; i < users.size(); i++) {
							data[i] = String.format("%s (%s)", users.get(i).getNickname().toUpperCase(),
									users.get(i).getHost());
						}
						listConnectedUsers.setListData(data);
						sleep(1000);
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	private void updateMessagesList(boolean stopFlag) {
		if (this.chatService == null)
			return;
		new Thread() {
			public void run() {
				while (stopFlag == true) {
					try {
						List<Message> messages = chatService.getMessages();
						if (messages == null)
							return;
						textAreaMessages.setText("");
						for (Message m : messages) {
							textAreaMessages.append(m.getText() + "\n");
						}
						sleep(1000);
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public void start() {
		this.setVisible(true);
	}

	@Override
	public void close() {
		String msg = "Ao sair do sistema todos os serviços ativos serão finalizados. \nDeseja Continuar ?";
		String title = "AVISO!";
		int result = JOptionPane.showConfirmDialog(ChatWindow.this, msg, title, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (result != JOptionPane.OK_OPTION)
			return;
		disconnect();
		this.dispose();
	}
}
