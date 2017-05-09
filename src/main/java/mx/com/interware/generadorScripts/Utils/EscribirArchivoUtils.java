package mx.com.interware.generadorScripts.Utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mx.com.interware.generadorScripts.bean.Cfdv3;

public class EscribirArchivoUtils {
	
	private static final int BLOQUE = 5000;

	/**
	 * Genera archivos sql con instrucciones(inserts, updates, etc)
	 * en bloques anonimos con registros en bloques de 5000
	 * @param mapa
	 * @param path
	 * @throws IOException
	 */
	public static void generarArchivoSalidaBloquesSQL(Map<String,String> mapa,final String path) throws IOException {
		
		if(mapa == null || mapa.isEmpty()
				|| path == null
				|| path.isEmpty()){
			
			System.out.println("NO SE OBTUVOS LINEAS PARA ESCRIBIR EN EL ARCHIVO");
		}else{
			
			Iterator itera = mapa.entrySet().iterator();

			int cont = 0;
			int version = 0;
			String ruta = path + "_" + (version+1) + ".sql";
			PrintWriter pw = new PrintWriter(ruta, "UTF-8");
			String salida = null;
			int contCommit = 0;
			
			pw.println("DECLARE \n	dummy NUMBER; \nBEGIN");
			
			try {
				
				System.out.println("ESCRIBIENDO EN ARCHIVO.........");
				
				while (itera.hasNext()) {
				    Map.Entry e = (Map.Entry)itera.next();
				    
				    	salida =  (String)e.getValue();
						pw.println(salida);
						
						// cada vez qu llegue a 2000 se genera un nuevo objeto
						if ( (cont+1) == BLOQUE
								&& BLOQUE > 1) {
		
							pw.println("COMMIT;");
							pw.println("DBMS_OUTPUT.PUT_LINE('Se hizo COMMIT'||'" + (contCommit+1) +"');");
							pw.println("EXCEPTION \n	WHEN OTHERS THEN \n	 DBMS_OUTPUT.PUT_LINE(SQLERRM); \nEND;");
							
							
							pw.flush();
							pw.close();
							version++;
							ruta = path + "_" + (version+1) + ".sql";
							pw = new PrintWriter(ruta, "UTF-8");
							pw.println("DECLARE \n	dummy NUMBER; \nBEGIN");
							
							cont = 0;
							contCommit ++;
						}else{
							cont++;
						}
				} // fin while archi no existente
					
				pw.println("COMMIT;");
				pw.println("DBMS_OUTPUT.PUT_LINE('Se hizo COMMIT'||'" + (contCommit+1) +"');");
				pw.println("EXCEPTION \n	WHEN OTHERS THEN \n	 DBMS_OUTPUT.PUT_LINE(SQLERRM); \nEND;");
				
				System.out.println("PROCESO DE ESCRITURA TERMINADO!!!!");
			} catch (Exception exe) {
				exe.printStackTrace();
			} finally {
				try {
					if(pw != null){
						pw.flush();
						pw.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	} // fin de metodo
	
	/**
	 * Metodo que escribe en archivo csv apartir de un hashmap, ya sea 
	 * en bloques de archivos o en un solo archivo dependiendo de la 
	 * constante BLOQUE (BLOQUE = 1 --> UNICO ARCHIVO, 
	 * BLOQUE > 1 --> ARCHIVOS CON BLOQUE LINEAS).
	 * @param mapa
	 * @param path
	 * @throws IOException
	 */
	public static void generarArchivoSalidaBloquesCSV(Map<String,Cfdv3> mapa,final String path) throws IOException {

		Iterator itera = mapa.entrySet().iterator();

		int cont = 0;
		int version = 0;
		String ruta = path + "_" + (version+1) + ".csv";
		PrintWriter pw = new PrintWriter(ruta, "UTF-8");
		String salida = null;
		
		try {
			while (itera.hasNext()) {
			    Map.Entry e = (Map.Entry)itera.next();
			    Cfdv3 cfdv3 = (Cfdv3)e.getValue();
						
			    	salida =  cfdv3.getSerie() + "|" + cfdv3.getFolio();
			    	
			    	if(cfdv3.getDescError() != null
			    			&& !cfdv3.getDescError().trim().isEmpty()){
			    		salida += "|" + cfdv3.getDescError().trim();
			    	}
			    	
					pw.println(salida);
					
					// cada vez qu llegue a 2000 se genera un nuevo objeto
					if ( cont == BLOQUE
							&& BLOQUE > 1) {
	
						pw.flush();
						pw.close();
						version++;
						ruta = path + "_" + (version+1) + ".csv";
						pw = new PrintWriter(ruta, "UTF-8");
						cont = 0;
					}
				cont++;
			} // fin while archi no existente
			
		} catch (Exception exe) {
			exe.printStackTrace();
		} finally {
			try {
				if(pw != null){
					pw.flush();
					pw.close();
				}   
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * Renombra el archivo pasado como parametro con el nuevo nombre
	 * y si la tuta es otra, lo mueve a la ruta destino con el nuevo
	 * nombre.
	 * @param archivoViejo : Archivo que se va a renombrar o mover
	 * @param nombreArchivoExt : Nombre nuevo que tendra el archivo(ya debe incluir la extension).
	 * @param rutaDestino : Ruta destino donde se pretende renombrar o mover el archivo.
	 * @return archRenombrado:  true si fue renombrado correctamente, false en caso
	 * contrario.
	 */
	public static boolean renombrarArchivo(File archivoViejo, String nombreArchivoExt, String rutaDestino) {
		
		boolean archRenombrado = false;
		try {
			rutaDestino += nombreArchivoExt;
			archRenombrado = archivoViejo.renameTo(new File(rutaDestino));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return archRenombrado;
	}
	
	public static void main(String[] args) {
		
		String rutaDestino = "C:\\BORRAME\\testJavaCopyFiles\\carpeta1\\";
		String rutaArchivoViejo = "C:\\BORRAME\\testJavaCopyFiles\\carpeta1";
		
		File directorioEntrada = new File(rutaArchivoViejo);
		System.out.println("DIRECTORIO DE ENTRADA: " + directorioEntrada.getAbsolutePath());
		
		if(directorioEntrada != null){
			List<File> archivosTotales = LeerArchivoUtils.getFilesFromDirectory(directorioEntrada);
			System.out.println("archivos totales: " + archivosTotales.size());
			
			int cont = 1; 
			for (File file : archivosTotales) {
				System.out.println(file.getAbsolutePath());
				String nombreArchivoExt = "nombre_" + cont + ".docx";
				boolean renomrado = EscribirArchivoUtils.renombrarArchivo(file, nombreArchivoExt, rutaDestino);
				if(renomrado){
					System.out.println("SE RENOMBRO EL ARCHIVO: " + file.getName() + " a " + nombreArchivoExt);
				}
				cont++;
			}
		}
	}

} // fin de la clase