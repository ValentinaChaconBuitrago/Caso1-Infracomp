

import java.util.LinkedList;

/**
 * Clase que representa a los clientes que dejan los mensajes en el buffer.
 */
public class Cliente extends Thread {

	//..................................Atributos..............................

	/**
	 * Lista donde se van a almacenar los mensajes.
	 */
	private  LinkedList<Mensaje> list;

	/**
	 * Buffer del cliente
	 */
	private Buffer buffer;

	/**
	 * Identificador del cliente
	 */
	private int id;

	/**
	 * Numero de mensajes del cliente
	 */
	private int numeroMensajes;

	//...................................Metodos...............................
	
	/**
	 * Método constructor de la clase Cliente
	 * @param buff Buffer el cual el cliente va a consultar
	 * @param numMensajes Número de consultas que va a hacer el cliente
	 * @param identificador id del cliente
	 */
	public Cliente(Buffer buff, int numMensajes, int identificador){

		buffer = buff;
		id = identificador;
		numeroMensajes = numMensajes;

		//Inicializar la lista de mensajes y agregar mensajes, para esto se utiliza el tamaÃ±o 
		list = new LinkedList<>();
		for (int i = 1; i <= numeroMensajes; i++){
			//El contenido del mensaje es i*id para que los mensajes sean diferentes
			Mensaje mensaje = new Mensaje(i*id, this);
			list.add(mensaje);
		}
	}

	/**
	 * Envía un mensaje al buffer
	 * @param mensaje mensaje a enviar
	 */
	private void enviarMensaje(Mensaje mensaje){
		//Espere si el Buffer esta lleno
		while(buffer.darCapacidad() == 0) {
			System.out.println("El buffer esta ocupado, el cliente " + id + " esta esperando.");
			//El cliente debe ceder el procesador después de cada intento
			yield();
		}

		//El buffer tiene disponibilidad, ahora el cliente puede enviar su mensaje
		buffer.guardarMensaje(mensaje);

		synchronized (mensaje) {
			try {
				//El cliente espera a que su mensaje sea procesado
				mensaje.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * El cliente envía todos los mensajes que tenga en la lista
	 */
	@Override
	public void run() {
		for (Mensaje mensaje : list) {
			System.out.println("El cliente "+ id + " envio el mensaje: " + mensaje.getMensaje());
			enviarMensaje(mensaje);
		}
		//Una vez el cliente termina sus solicitudes, reduce el número de clientes
		buffer.decNumClientes();
	}
}

