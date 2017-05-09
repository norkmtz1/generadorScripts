package mx.com.interware.generadorScripts.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class JavaIOUtils {

	 /**
	  * Metodo que copia el archivo siempre y cuando no el archivo destino 
	  * no exista.
	  * @param archivOrigen
	  * @param archivoDestino
	  * @return
	  * @throws IOException
	  */
	 @SuppressWarnings("resource")
	public static boolean copyFile(File archivOrigen, File archivoDestino) throws IOException {
		 
		 	boolean copiado = true;
		    if(archivoDestino.exists()) {
		    	System.out.println("EL ARCHIVO: " + archivoDestino.getName() + " YA EXISTE");
		    	return false;
		    }
		 
		    archivoDestino.createNewFile();
		    
		    FileChannel origen = null;
		    FileChannel destino = null;
		    try {
		        origen = new FileInputStream(archivOrigen).getChannel();
		        destino = new FileOutputStream(archivoDestino).getChannel();
		 
		        long count = 0;
		        long size = origen.size();              
		        while((count += destino.transferFrom(origen, count, size-count))<size);
		        
		    }catch(Exception e){
		    	copiado = false;
		    	e.printStackTrace();
		    }finally {
		        if(origen != null) {
		            origen.close();
		        }
		        if(destino != null) {
		            destino.close();
		        }
		    }
		    return copiado;
	}
	
	 public boolean copiarArchivo(File archivOrigen, File archivoDestino) {
		 
	        if (archivOrigen.exists()) {
	        	
	        	InputStream in = null;
                OutputStream out = null;
	        	
	            try {
	                in = new FileInputStream(archivOrigen);
	                out = new FileOutputStream(archivoDestino);
	                // Usamos un buffer para la copia.
	                byte[] buf = new byte[1024];
	                int len;
	                while ((len = in.read(buf)) > 0) {
	                    out.write(buf, 0, len);
	                }
	                in.close();
	                out.close();
	                return true;
	            } catch (IOException ioe) {
	                ioe.printStackTrace();
	                return false;
	            }finally{
	            	try {
						in.close();
						 out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        } else {
	            return false;
	        }
	 }	
} // fin de clase