import java.io.*; 
import java.util.LinkedList;

/**
 * Clase que representa un buffer donde se almacenan los mensajes que son dejados por los clientes y procesados por los hilos del servidor.
 */
public class Buffer {

	//..................................Atributos..............................

	/**
	 * Capacidad de mensajes del Buffer
	 */
	private int capacidad;

	/**
	 * Mensajes almacenados en el buffer
	 */
	private LinkedList<Mensaje> mensajes;
	
	/**
	 * Número de clientes que están usando el buffer
	 */
	private int numeroClientes;


	//....................................Metodos..............................
	
	/**
	 * Constructor del Buffer
	 * @param tam tamaño de la lista de mensajes
	 * @param numCli número de clientes que va a atender el buffer
	 */
	public Buffer(int tam, int numCli) {
		capacidad = tam;
		mensajes = new LinkedList<>();
		numeroClientes = numCli;
	}
	
	/**
	 * Retorna la capacidad de mensajes del buffer
	 * @return capacidad del buffer
	 */
	public int darCapacidad() {
		return capacidad;
	}
	
	/**
	 * Retorna el número actual de clientes 
	 * @return número actual de clientes
	 */
	public int darNumClientes() {
		return numeroClientes;
	}
	
	/**
	 * Decrementa el número de clientes e imprime el número de clientes en la consola
	 */
	public synchronized void decNumClientes() {
		numeroClientes--;
		System.out.println("El numero de clientes es: " + numeroClientes);
	}
	
	/**
	 * Retorna el número de mensajes que hay en el servidor
	 * @return número de mensajes
	 */
	public int darNumeroMensajes() {
		return mensajes.size();
	}
	
	/**
	 * Guarda un mensaje en la lista de mensajes del buffer
	 * @param mes mensaje a guardar en el buffer
	 */
	public synchronized void guardarMensaje(Mensaje mes) {
		//Agrega a la lista el mensaje que el cliente mando
		mensajes.add(mes);
		//Se reduce la capacidad del Buffer
		capacidad--;
	}
	
	/**
	 * Retira un mensaje del buffer
	 * @return Mensaje retirado
	 */
	public synchronized Mensaje retirarMensaje() {
		//El primer mensaje en ingresar a la lista es el primero en salir
		Mensaje mess = mensajes.removeFirst();
		System.out.println("El mensaje: " + mess.getMensaje() + " ha sido retirado por el servidor");
		
		capacidad++;
		return mess;
	}
	
	
	/**
	 * Método main que lee la información del número de clientes, servidores, capacidad del buffer y número de mensajes por servidor
	 * de un archivo .txt ubicado en la carpeta data
	 * @param args
	 * @throws NumberFormatException si el archivo no tiene el formato adecuado
	 * @throws IOException si hay algún problema para leer el archivo
	 */
	public static void main(String[] args) throws NumberFormatException, IOException{

		/*
		 * Primera linea del archivo de texto tiene un numero n de clientes, un numero f de servidores
		 * y un numero j que representa la capacidad del buffer
		 * Continuan n filas con un numero r de mensajes de cada cliente.
		 */
		BufferedReader br =  new BufferedReader(new FileReader("data/config.txt"));
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
			System.out.println("Se creo el cliente: " + nuevoCliente.getId() + " con un numero de mensajes: " + numMensajes );
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
		
		br.close();
		
		while(numClientes != 0 && buffer.darNumeroMensajes() != 0) {
			
		}	
	}
}
