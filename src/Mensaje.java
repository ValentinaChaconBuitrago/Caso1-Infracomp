
public class Mensaje {
	
	//..................................Atributos..............................
	
	private int contenido;
	
	private Cliente cliente;
	
	//....................................Metodos..............................
	
	public Mensaje(int cont, Cliente cli){
		contenido = cont;
		cliente = cli;
	}
	
	public void setMensaje(int mensaje) {
		contenido = mensaje;
	}
	
	public int getMensaje() {
		return contenido;
	}
	
	public void setCliente(Cliente cli) {
		cliente = cli;
	}
	
	public Cliente getCliente() {
		return cliente;
	}	
}
