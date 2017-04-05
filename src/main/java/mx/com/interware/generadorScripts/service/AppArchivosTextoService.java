package mx.com.interware.generadorScripts.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mx.com.interware.generadorScripts.Utils.JavaIOUtils;
import mx.com.interware.generadorScripts.Utils.LeerArchivoUtils;

public class AppArchivosTextoService {
	
	static final String pathEntradaNuevosNombres = "C:\\AXA\\shells\\testShells\\archEntradaLiq";
	static final String pathEntradaBoveda = "C:\\AXA\\shells\\testShells\\testBusqueda";
	
	static final String pathSalidaEncontrados = "C:\\AXA\\shells\\testShells\\encontradosRenombrados\\";

	
	public List<String> renombrarArchBoveda(){
		
		List<String> nombresArchivos = new ArrayList<String>();
		ReadFile readFile = new ReadFile();
		List<File> archivosBovedaList = null;
		try {
			
			File[] filesFoliosNombres = LeerArchivoUtils.obtenerArchivosDeDirectorio(pathEntradaNuevosNombres);
			
	    	System.out.println("CARGANDO NOMBRES PARA BUSCAR......");
	    	nombresArchivos.addAll(readFile.cargarNombres(filesFoliosNombres));
	    	System.out.println("TAMAÑO DE LISTA CON LOS NOMBRES: " + nombresArchivos.size());
	    	
	    	File directorioEntrada = new File(pathEntradaBoveda);
			
	    	if(directorioEntrada != null){
				System.out.println("PATH DIRECTORIO ENTRADA: " + directorioEntrada.getAbsolutePath());
				archivosBovedaList = LeerArchivoUtils.getFilesFromDirectory(directorioEntrada);
				System.out.println("ARCHIVOS OBTENIDOS BOVEDA: " + archivosBovedaList.size());
			}
	    	
	    	Map<String, File> mapaArchivosBoveda = readFile.getFileKeyName(archivosBovedaList);
	    	System.out.println("ARCHIVOS TOTALES OBTENIDOS BOVEDA: " + archivosBovedaList.size());
	    	
	    	String nombreQuitar = null;
			String parteNombre = null;
			String extension = null;
			String nuevoNombre = null;
	    	
	    	// COMPARA LOS NOMBRES CONTRA LOS NOMBRES DE LOS ARCHIVOS OBTENIDOS
	    	for (String nombreArchivo : nombresArchivos) {
	    		
	    		// valida que exista el nombre del archivo para hacer el renombrado
				if(mapaArchivosBoveda.containsKey(nombreArchivo)){
					
					String nombreArchivoArray[] =  nombreArchivo.split("\\.");
					
					if(nombreArchivoArray != null
						&& nombreArchivoArray.length == 3){
						
						nombreQuitar = nombreArchivoArray[0].trim();
						parteNombre = nombreArchivoArray[1].trim();
						extension = nombreArchivoArray[2].trim();
						nuevoNombre = parteNombre + "." + extension;
						
//						System.out.println("NUEVO NOMBRE: " + nuevoNombre);
						boolean copiado = JavaIOUtils.copyFile(mapaArchivosBoveda.get(nombreArchivo), new File(pathSalidaEncontrados + nuevoNombre));
						if(!copiado){
//							System.out.println("FALLO AL COPIAR Y RENOMBRAR EL ARCHIVO: " + nombreArchivo);
						}
					}else{
						System.out.println("NO SE OBTUVO ARREGLO DEL NOMBRE DE ARCHIVO: " + nombreArchivo.trim() + " LONGITUD: " + nombreArchivoArray.length);
					}					
				}else{
					System.out.println("EL ARCHIVO: " + nombreArchivo + " NO ESTA EN LA BOVEDA");
				} // fin valida si existe el nombre del archivo
	    	}// fin for
	    	
	    	System.out.println("PROCESO TERMINADO!!!!!");
	    	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		return nombresArchivos;
	}
}