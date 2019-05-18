package socketMulticast;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

class Processador {
	private static int porta = 5555;
	private static MulticastSocket socket;
	private static InetAddress endereco;
	private static final Logger LOGGER = Logger.getLogger(Processador.class.getName());

	public static void main(String args[]) {
		System.out.println("Processador");
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

		byte[] recvData = new byte[1000000];
		DatagramPacket recvPacket;
		recvPacket = new DatagramPacket(recvData, recvData.length);
		try {
			socket.receive(recvPacket);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Erro ao receber pacote: " + e);
		}
		byte[] imagemBytes = recvPacket.getData();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(imagemBytes);
		BufferedImage imagem = null;
		try {
			imagem = ImageIO.read(inputStream);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Erro na leitura dos dados: " + e);
		}
		// processamento
		System.out.println(recvPacket.getAddress().toString() + ": " + imagem.getWidth() + "x" + imagem.getHeight());
		LOGGER.log(Level.INFO, "Processador é " + socket.getRemoteSocketAddress());
		//
		recvPacket = null;
	}
}
