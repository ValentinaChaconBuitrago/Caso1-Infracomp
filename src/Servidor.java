
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
				yield();
			}
			Mensaje mensie = buffer.retirarMensaje();
			mensaje = mensie;
			System.out.println("El servidor esta procesando el mensaje: "+ mensie.getMensaje());
			
			synchronized(mensaje)
			{
				mensaje.notifyAll();
				System.out.println("El servidor notifica ");
			}
			
		}
		
	}
	
	@Override
	public void run() {
		recibirMensaje();	
	}
}
