package mx.com.interware.generadorScripts.service;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ReadFile {
	
	private static final long INICIO_LINEA = 1;
	
	/**
	 * Cargar lineas de un archivo
	 * @param archivosRecibidos
	 * @return
	 */
	public Map<String,String> cargarNombresNuevosArchivo(File[] archivosRecibidos){
		
		Scanner sc = null;
	    PrintWriter pw = null;
	    Map<String,String> mapArchivosRecibidos  = new HashMap<String,String>();
	    
	    try {
	    		
	    		// Se hace un recorrido sobre los  archivos
				for (int cont = 0; cont < archivosRecibidos.length; cont++) {
					
					System.out.println("ARCHIVO LECTURA: "
							+ archivosRecibidos[cont].getName());
					
					sc = new Scanner(archivosRecibidos[cont]);
					
					String lineaProc;
					long contLineaProc = 0;
					String datosLineaProc [] = null;
					
					String nombreNuevo = null;
					String folio = null;
					
					while (sc.hasNextLine()) {
						lineaProc = sc.nextLine();
						
						if(lineaProc!= null && !lineaProc.isEmpty()){
//							System.out.println("LINEA PROCESADA A LEER " + (contLineaProc+1) + " : " + lineaProc);
							
							datosLineaProc = lineaProc.split("\\|");
							
							if(datosLineaProc != null 
									&& datosLineaProc.length == 2){
								
								if( (contLineaProc+1) > INICIO_LINEA){
									
									folio = datosLineaProc[0].trim();
									nombreNuevo = datosLineaProc[1].trim();
																		
									if(!mapArchivosRecibidos.containsKey(folio)){
										
										mapArchivosRecibidos.put(folio, nombreNuevo);
									}else{
										System.out.println("FOLIO YA EXISTE EN EL HASH");
									}
								}else{
									System.out.println("NO SE METIO LINEA: " +  (contLineaProc+1) + " VALOR: " + lineaProc);
								}
							}else{
								System.out.println("NO SE OBTUVO ARREGLO DE DATOS LINEA: " + (contLineaProc+1) + " LONGITUD: " + datosLineaProc.length);
							}
						}else{
							System.out.println("LINEA " + (contLineaProc+1) + " INVALIDA VACIA");
						}
						contLineaProc++;
					} // FIN WHILE
				} // FIN FOR	    	
		}  catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(sc != null){
					sc.close();
				}
				if(pw != null){
					pw.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	    return mapArchivosRecibidos;
	}// fin archivo
	
	public Map<String,String> cargarNombresNuevosArchivoPdfsXmls(File[] archivosRecibidos){
		
		Scanner sc = null;
	    PrintWriter pw = null;
	    Map<String,String> mapArchivosRecibidos  = new HashMap<String,String>();
	    
	    try {
	    		
	    		// Se hace un recorrido sobre los  archivos
				for (int cont = 0; cont < archivosRecibidos.length; cont++) {
					
					System.out.println("ARCHIVO LECTURA: "
							+ archivosRecibidos[cont].getName());
					
					sc = new Scanner(archivosRecibidos[cont]);
					
					String lineaProc;
					long contLineaProc = 0;
					String datosLineaProc [] = null;
					
					String serie = null;
					String nombreNuevo = null;
					String folio = null;
					String poliza = null;
					String nombre = null;
					String llave =null;
					
					while (sc.hasNextLine()) {
						lineaProc = sc.nextLine();
						
						if(lineaProc!= null && !lineaProc.isEmpty()){
//							System.out.println("LINEA PROCESADA A LEER " + (contLineaProc+1) + " : " + lineaProc);
							
							datosLineaProc = lineaProc.split("\\|");
							
							if(datosLineaProc != null 
									&& datosLineaProc.length == 4){
								
								if( (contLineaProc+1) > INICIO_LINEA){
									
									serie = datosLineaProc[0].trim();
									folio = datosLineaProc[1].trim();
									poliza = datosLineaProc[2].trim();
									nombre = datosLineaProc[3].trim().replace(" ", "_");
									llave = serie + folio; 
									
									nombreNuevo = poliza + "_" + folio + "_" + nombre; 
																		
									if(!mapArchivosRecibidos.containsKey(llave)){
										mapArchivosRecibidos.put(llave, nombreNuevo);
									}else{
										System.out.println("LLAVE YA EXISTE EN EL HASH");
									}
								}else{
									System.out.println("NO SE METIO LINEA: " +  (contLineaProc+1) + " VALOR: " + lineaProc);
								}
							}else{
								System.out.println("NO SE OBTUVO ARREGLO DE DATOS LINEA: " + (contLineaProc+1) + " LONGITUD: " + datosLineaProc.length);
							}
						}else{
							System.out.println("LINEA " + (contLineaProc+1) + " INVALIDA VACIA");
						}
						contLineaProc++;
					} // FIN WHILE
				} // FIN FOR	    	
		}  catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(sc != null){
					sc.close();
				}
				if(pw != null){
					pw.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	    return mapArchivosRecibidos;
	}// fin archivo
	
	
	/**
	 * Metodo que carga en una lista de String todas las lineas
	 * de los archivos pasados como parametro.
	 * @param archivosRecibidos
	 * @return
	 */
	public List<String> cargarNombres(File[] archivosRecibidos){
		
		List<String> nombresArchivos = new ArrayList<String>();
		Scanner sc = null;
	    PrintWriter pw = null;
	    
	    try {
	    	
	    	// Se hace un recorrido sobre los  archivos
			for (int cont = 0; cont < archivosRecibidos.length; cont++) {
				
				System.out.println("ARCHIVO LECTURA: "
						+ archivosRecibidos[cont].getName());
				
				sc = new Scanner(archivosRecibidos[cont]);
				
				String lineaProc;
				long contLineaProc = 0;
				
				while (sc.hasNextLine()) {
					lineaProc = sc.nextLine();
					
					if(lineaProc!= null && !lineaProc.isEmpty()){
						
						nombresArchivos.add(lineaProc.trim());
//						System.out.println("Archivo: " + lineaProc);
					}else{
						System.out.println("LINEA " + (contLineaProc+1) + " INVALIDA VACIA");
					}
					contLineaProc++;
				}// fin while
				
			} // FIN FOR
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(sc != null){
					sc.close();
				}
				if(pw != null){
					pw.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return nombresArchivos;
	} // fin metodo de cargarNombres
	
	/**
	 * Metodo que convierte una lista de archivos en un hashMap
	 * con el nomre del archivo como llave y el archivo como valor.
	 * @param listArchivos
	 * @return HashMap <String,File> con llave nombre de archivo
	 */
	public Map<String,File> getFileKeyName(List<File> listArchivos){
		Map<String,File> mapArchivosRecibidos  = new HashMap<String,File>();
		
		if(listArchivos == null
				|| listArchivos.isEmpty()){
			System.out.println("LISTA DE ARCHIVOS PARA CONVERTIR A MAPA ESTA VACIA");
			return mapArchivosRecibidos;
		}
		
		for (File file : listArchivos) {
//			System.out.println("LLAVE: " + file.getName().trim());
			mapArchivosRecibidos.put(file.getName().trim(), file);
		}
		return mapArchivosRecibidos;
	}// fin metodo getFileKeyName

}
