

import java.util.LinkedList;


public class Cliente extends Thread {

	//..................................Atributos..............................

	//Lista donde se van a almacenar los mensajes.
	private  LinkedList<Mensaje> list;

	//Buffer del cliente
	private Buffer buffer;

	//Identificador del cliente
	private int id;

	//Numero de mensajes del cliente
	private int numeroMensajes;

	//...................................Metodos...............................

	//El parámetro numMensajes se refiere al numero de consultas que va a hacer el cliente, lo que
	//corresponde al número de mensajes que el cliente va a enviar.
	public Cliente(Buffer buff, int numMensajes, int identi){

		buffer = buff;
		id = identi;
		numeroMensajes = numMensajes;

		//Inicializar la lista de mensajes y agregar mensajes, para esto se utiliza el tamaño 
		list = new LinkedList<>();
		for (int i = 0; i < numMensajes; i++){
			Mensaje mes = new Mensaje(i,this);
			list.add(mes);
		}

	}

	public void enviarMensaje(Mensaje mensaje){
		//Espere si el Buffer esta lleno
		while(buffer.darCapacidad() == 0) {
			System.out.println("El buffer esta ocupado, el cliente " + id + " esta esperando.");
			//El cliente debe ceder el procesador después de cada intento
			yield();
		}

		//El buffer tiene disponibilidad ahora el cliente puede enviar su mensaje
		buffer.guardarMensaje(mensaje);

		synchronized (mensaje) {
			try {
				//Si no es posible depositar el mensaje se queda en espera activa
				mensaje.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//El notify all va aqui o va en el método guardar mensaje del buffer?????????????????????????
		//notify();
	}

	@Override
	public void run() {

		for (int i = 0; i < numeroMensajes; i++) {
			//El contenido del mensaje es i*id para que los mensajes sean diferentes
			Mensaje mensaje = new Mensaje(i*id, this);
			System.out.println("El cliente "+ id + " envio el mensaje: " + mensaje.getMensaje());
			enviarMensaje(mensaje);
		}
		
		int numeroActual = buffer.darNumClientes()-1;
		buffer.setNumClientes(numeroActual);
		System.out.println("El numero de clientes es: " + buffer.darNumClientes());
	}
}

