package socketMulticast;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

class Controlador extends JFrame {
	private int porta = 5555;
	private static MulticastSocket socket;
	private static InetAddress endereco;
	private static final Logger LOGGER = Logger.getLogger(Controlador.class.getName());

	Controlador() {
		try {
			socket = new MulticastSocket(porta);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Não foi possível iniciar o socket: " + e);
		}
		try {
			endereco = InetAddress.getByName("227.0.0.1");
		} catch (UnknownHostException e) {
			LOGGER.log(Level.SEVERE, "Não foi possível localizar o host: " + e);
		}
		try {
			socket.joinGroup(endereco);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Não foi possível se juntar ao grupo: " + e);
		}

		JButton botao = new JButton("Selecionar imagem");
		botao.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				enviarImagem();
			}

		});
		JPanel panel = new JPanel();

		panel.add(botao);
		this.getContentPane().add(panel);

		setSize(400, 400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public String seletor() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Escolha a imagem");
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos PNG e JPG", "png", "jpg");
		jfc.addChoosableFileFilter(filter);

		int returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			return jfc.getSelectedFile().getPath();
		}
		return "";
	}

	public void enviarImagem() {
		File file = new File(seletor());
		byte ttl = (byte) 1;
		BufferedImage imagem = null;
		try {
			imagem = ImageIO.read(file);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Erro na leitura dos dados: " + e);
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			ImageIO.write(imagem, "png", outputStream);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Erro na escrita dos dados: " + e);
		}
		byte[] imagemBytes = outputStream.toByteArray();

		try {
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DatagramPacket sendPacket;
		sendPacket = new DatagramPacket(imagemBytes, imagemBytes.length, endereco, porta);
		try {
			socket.setTimeToLive(ttl);

			socket.send(sendPacket);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Erro no envio dos dados: " + e);
		}
		sendPacket = null;
	}

	public static void main(String args[]) throws Exception {
		Controlador app = new Controlador();
	}
}
