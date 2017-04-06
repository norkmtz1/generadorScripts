package mx.com.interware.generadorScripts.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeerArchivoUtils {
	
	public static File[] obtenerArchivosDeDirectorio(final String path) {
		
		List<File> archivosTotales = new ArrayList<File>();
		File[] filesDirectorio = new File(path).listFiles();
		File[] filesArchivosTotal = null;
		
		if(filesDirectorio != null){
			for (int i = 0; i < filesDirectorio.length; i++) {
                if (filesDirectorio[i].isDirectory()) {
                	System.out.println("Directorio: " + filesDirectorio[i].getPath());
                	archivosTotales.addAll(obtenerListaDeArchivos(filesDirectorio[i].getPath()));
                }else{
                	archivosTotales.addAll(Arrays.asList(filesDirectorio[i]));
                }
			}
		}
		
		if(!archivosTotales.isEmpty()){
			filesArchivosTotal = new File[archivosTotales.size()];
			filesArchivosTotal = archivosTotales.toArray(filesArchivosTotal);
		}
		
		System.out.println("ARCHIVOS LEIDOS: " + filesArchivosTotal.length);
		return filesArchivosTotal;
	}
	
	
	public static List<File> obtenerListaDeArchivos(final String path) {

		List<File> archivosList = null;
		
		File[] filesArr = new File(path).listFiles();
		if(filesArr != null && filesArr.length > 0){
			archivosList = Arrays.asList(filesArr);
		}		
		
		return archivosList;

	} // fin obtenerListaDeArchivos
	
	/**
	 * Metodo para obtener la exrtension de un archivo.
	 * @param archivo
	 * @return String con la extension del archivo.
	 */
	 public static String getExtensionFile(File archivo) {
         
         if (archivo == null || archivo.isDirectory()){
               return "nulo o directorio";
         }else if (archivo.isFile()){
               int index = archivo.getName().lastIndexOf('.');
               if (index == -1) {
                     return "";
               } else {
                     return archivo.getName().substring(index + 1);
               }
         }else{
               return "";
         }
   }  
	
	/**
	 * Metodo Recursivo que obtiene todos los archivos del 
	 * directorio y subidirectorios.
	 * @param directorio tipo carpeta qiue puede tener mas carpetas y archivos.
	 * @return Lista con los archivos encontrados
	 */
	public static List<File> getFilesFromDirectory(File directorio) {
		
		List<File> archivosTotales = new ArrayList<File>();
		
		File listFile[] = directorio.listFiles();
		if (listFile != null) {
			for (int i = 0; i < listFile.length; i++) {
				if (listFile[i].isDirectory()) {
					archivosTotales.addAll(getFilesFromDirectory(listFile[i]));
				} else {
					archivosTotales.add(listFile[i]);
//					System.out.println(listFile[i].getPath());
				}
			}
		}else{
			System.out.println("DIRECTORIO VACIO");
		}		
		return archivosTotales;
	}
	
	public static void main(String[] args) {
		
		String direcEntrada = "C:\\AXA\\shells\\testShells\\testBusqueda";
		File directorioEntrada = new File(direcEntrada);
		
		if(directorioEntrada != null){
			System.out.println("PATH DIRECTORIO ENTRADA: " + directorioEntrada.getAbsolutePath());
			List<File> archivosTotales = LeerArchivoUtils.getFilesFromDirectory(directorioEntrada);
			System.out.println("ARCHIVOS TOTALES OBTENIDOS: " + archivosTotales.size());
		}
	}


} // fin de la clase
