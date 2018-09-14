/**
 * Clase que representa los mensajes que los clientes dejan en el buffer y el servidor procesa.
 */
public class Mensaje {
	
	//..................................Atributos..............................
	
	/**
	 * Contenido del mensaje
	 */
	private int contenido;
	
	/**
	 * Cliente que env�a el mensaje
	 */
	private Cliente cliente;
	
	//....................................Metodos..............................
	
	/**
	 * M�todo constructor del mensaje
	 * @param cont contenido del mensaje
	 * @param cli cliente que env�a el mensaje
	 */
	public Mensaje(int cont, Cliente cli){
		contenido = cont;
		cliente = cli;
	}
	
	/**
	 * Asigna el contenido del mensaje al valor dado por par�metro
	 * @param mensaje nuevo contenido del mensaje
	 */
	public void setMensaje(int mensaje) {
		contenido = mensaje;
	}
	
	/**
	 * Retorna el contenido del mensaje
	 * @return contenido del mensaje
	 */
	public int getMensaje() {
		return contenido;
	}
}
