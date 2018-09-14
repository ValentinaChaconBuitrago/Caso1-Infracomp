import java.util.NoSuchElementException;

/**
 * Clase que representa un hilo de ejecuci�n del servidor
 */
public class Servidor extends Thread{

	//..................................Atributos..............................

	/**
	 * Mensaje que el servidor est� procesando
	 */
	private Mensaje mensaje;

	/**
	 * Buffer del cual el servidor retira los mensajes
	 */
	private Buffer buffer;

	//....................................Metodos..............................

	/**
	 * M�todo constructor del servidor
	 * @param buff buffer del cual el servidor retira los mensajes
	 */
	public Servidor(Buffer buff) {
		buffer = buff;
		mensaje = null;
	}

	/**
	 * Retira un mensaje del buffer, lo procesa y notifica al cliente
	 */
	private void recibirMensaje() {
		//Si el buffer est� vac�o pero hay clientes, cede el procesador
		while(buffer.darNumeroMensajes() == 0 && buffer.darNumClientes() != 0) {
			yield();
		}
		//Una vez el buffer deja de estar vac�o, retira el mensaje, lo procesa y notifica al cliente
		try{
			mensaje = buffer.retirarMensaje();
			procesarMensaje(mensaje);
			notificarCliente(mensaje);

		} catch(NoSuchElementException e){
			System.out.println("buffer vac�o");
		}
	}

	/**
	 * Procesa un mensaje, es decir, incrementa el contenido del mensaje en 1
	 * @param mensaje mensaje a procesar
	 */
	private void procesarMensaje(Mensaje mensaje) {
		int msg = mensaje.getMensaje();
		System.out.println("El servidor esta procesando el mensaje: "+ mensaje.getMensaje());
		mensaje.setMensaje(msg++);
	}

	/**
	 * Notifica al cliente que est� esperando en el mensaje que su mensaje ya fue procesado
	 * @param mensaje mensaje en el que est� esperando el cliente
	 */
	private void notificarCliente(Mensaje mensaje) {
		synchronized(mensaje){
			mensaje.notify();
		}
	}

	/**
	 * El servidor procesa mensajes mientras haya clientes
	 */
	@Override
	public void run() {
		while(buffer.darNumClientes() != 0){
			recibirMensaje();	
		}
	}
}
