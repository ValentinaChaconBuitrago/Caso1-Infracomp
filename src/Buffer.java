import java.io.*; 
import java.util.LinkedList;
public class Buffer {

	//..................................Atributos..............................

	//Capacidad del Buffer
	private int capacidad;

	private LinkedList<Mensaje> mensajes;
	
	private int numeroClientes;


	//....................................Metodos..............................
	

	public Buffer(int tam, int numCli) {
		capacidad = tam;
		mensajes = new LinkedList<>();
		numeroClientes = numCli;
	}
	
	public int darCapacidad() {
		return capacidad;
	}
	
	public int darNumClientes() {
		return numeroClientes;
	}
	
	public void setNumClientes(int num) {
		numeroClientes =  num;
	}
	
	public LinkedList<Mensaje> darMensajes(){
		return mensajes;
	}
	
	/**
	 * NOTE: When one thread is executing a synchronized method for an object, all other threads that invoke 
	 * synchronized methods for the same object block (suspend execution) until the first thread is done with
	 * the object.
	 */
	public synchronized void guardarMensaje(Mensaje mes) {
		
		//Agrega a la lista el mensaje que el cliente mando
		mensajes.add(mes);
		
		//Se reduce la capacidad del Buffer
		capacidad--;
		
		//SI ESTA BIEN ESTE NOTIFY ALL??????????????????????????????????????????????????????????
		this.notifyAll();
	}
	
	public synchronized Mensaje retirarMensaje() {
		//El primer mensaje en ingresar a la lista es el primero en salir
		Mensaje mess = mensajes.removeFirst();
		
		System.out.println("El mensaje: " + mess.getMensaje() + " ha sido procesado por el servidor");
		//SI ESTA BIEN ESTE NOTIFY ALL??????????????????????????????????????????????????????????
		this.notifyAll();
		
		capacidad++;
		return mess;
	}
	
	
	//Metodo main
	public static void main(String[] args) throws NumberFormatException, IOException{


		/**
		 * Primera linea del archivo de texto tiene un numero n de clientes, un numero f de servidores
		 * y un numero j que representa la capacidad del buffer
		 * Continuan n filas con un numero r de mensajes de cada cliente.
		 */
		BufferedReader br =  new BufferedReader(new FileReader("Test.txt"));
		String[] numeros = br.readLine().split(" ");
		
		//---------Leer el archivo de texto e inicializar los Threads con esa informacion--------
		int capacidadBuffer = Integer.parseInt(numeros[0]);
		int numClientes = Integer.parseInt(numeros[1]);
		int numServidores = Integer.parseInt(numeros[2]);
		Buffer buffer =  new Buffer(capacidadBuffer, numClientes);

		int count = 0;
		while(count < numClientes) {

			count++;
			int numMensajes = Integer.parseInt(br.readLine());
			Cliente nuevoCliente = new Cliente(buffer, numMensajes, count);
			System.out.println("Se creo el cliente: " + count + " con un numero de mensajes: " + numMensajes );
			nuevoCliente.start();	
			
			System.out.println("La capacidad del buffer es: " + buffer.darCapacidad());
		}

		int count2 = 0;
		while(count2 < numServidores){

			count2++;
			
			Servidor server = new Servidor(buffer);
			System.out.println("Se creo el servidor: "+ count2);
			server.start();		
		}
		
		while(numClientes != 0 && buffer.darMensajes().size()!= 0) {
			
		}
	}
}
