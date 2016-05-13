package br.facape.sistemas.distribuidos.chat.server.application.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import br.facape.sistemas.distribuidos.chat.server.application.ui.interfaces.IWindow;
import br.facape.sistemas.distribuidos.chat.server.utils.ServicesUtils;

public class SettingsWindow extends JDialog implements IWindow {

	private static final long serialVersionUID = -3343623309215062054L;
	private final JPanel contentPanel = new JPanel();
	private JLabel label;
	private JTextField textFieldName;
	private JLabel lblPortaDoServio;
	private JTextField textFieldPort;

	public SettingsWindow() {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setTitle("Chat RMI Server [Configura\u00E7\u00F5es do Servi\u00E7o]");
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setBounds(100, 100, 517, 220);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		this.contentPanel.setBorder(new TitledBorder(null, "Informa\u00E7\u00F5es do Servi\u00E7o", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		this.label = new JLabel("Nome do Servi\u00E7o:");
		this.label.setBounds(14, 31, 97, 16);
		contentPanel.add(this.label);
		
		this.textFieldName = new JTextField();
		this.textFieldName.setColumns(10);
		this.textFieldName.setBounds(14, 48, 480, 28);
		contentPanel.add(this.textFieldName);
		
		this.lblPortaDoServio = new JLabel("Porta do Servi\u00E7o:");
		this.lblPortaDoServio.setBounds(14, 88, 97, 16);
		contentPanel.add(this.lblPortaDoServio);
		
		this.textFieldPort = new JTextField();
		this.textFieldPort.setColumns(10);
		this.textFieldPort.setBounds(14, 105, 480, 28);
		contentPanel.add(this.textFieldPort);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Salvar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						saveConfigurations();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Calcelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						close();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		loadConfigurations();
	}
	
	private void loadConfigurations() {
		try {
			String serviceName = ServicesUtils.loadProperty(ServicesUtils.PROPERTY_SERVICE_NAME);
			String servicePort = ServicesUtils.loadProperty(ServicesUtils.PROPERTY_SERVICE_PORT);
			if(serviceName == null || servicePort ==null) {
				String msg 	 = "Não foi possível carregar as configurações do serviço. VERIFIQUE!";
				String title = "ERRO";
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
				close();
			}
			this.textFieldName.setText(serviceName);
			this.textFieldPort.setText(servicePort);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveConfigurations() {
		try {
			String serviceName = textFieldName.getText().trim();
			String servicePort = textFieldPort.getText().trim();
			if(serviceName.equals("") || servicePort.equals("")) {
				String msg 	 = "Preencha todos os dados corretamente!";
				String title = "ALERTA";
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				return;
			}
			ServicesUtils.saveProperty(ServicesUtils.PROPERTY_SERVICE_NAME, serviceName);
			ServicesUtils.saveProperty(ServicesUtils.PROPERTY_SERVICE_PORT, servicePort);
			String title = "MENSAGEM";
			String msg   = "Configurações gravadas com sucesso!";
			JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
			this.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start() {
		this.setVisible(true);
	}

	@Override
	public void close() {
		if(textFieldName.getText().trim().equals("") || textFieldPort.getText().trim().equals("")) {
			String msg = "Se não informar as configurações o serviço não funcionará corretamente. \nDeseja Continuar ?";
			String title = "AVISO!";
			int result = JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (result != JOptionPane.OK_OPTION)
				return;
		}
		this.dispose();
	}
}
