import java.util.NoSuchElementException;

public class Servidor extends Thread{

	//..................................Atributos..............................

	private Mensaje mensaje;
	private Buffer buffer;

	//....................................Metodos..............................

	public Servidor(Buffer buff) {
		buffer = buff;
		mensaje = null;
	}

	public void recibirMensaje() {
		while(buffer.darNumClientes() != 0) {

			//Espera si el buffer esta vacio
			while(buffer.darMensajes().isEmpty()) {
//				System.out.println("servidor esperando");
				yield();
			}
			try{
				Mensaje mensie = buffer.retirarMensaje();
				mensaje = mensie;
				int msg = mensaje.getMensaje();
				mensaje.setMensaje(msg++);
				System.out.println("El servidor esta procesando el mensaje: "+ mensie.getMensaje());

				synchronized(mensaje)
				{
					mensaje.notify();
					System.out.println("El servidor notifica al cliente " + mensaje.getCliente().getId());
				}

			} catch(NoSuchElementException e){
				System.out.println("buffer vacío");
			}
		}
	}

	@Override
	public void run() {
		recibirMensaje();	
	}
}
