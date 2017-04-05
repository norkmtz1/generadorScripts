package mx.com.interware.generadorScripts.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import mx.com.interware.generadorScripts.Utils.EscribirArchivoUtils;
import mx.com.interware.generadorScripts.Utils.LeerArchivoUtils;

public class AppService {
	
//	static final String pathEntradaRetenciones = "C:\\AXA\\retenciones\\entradaTest";
	static final String pathEntradaRetenciones = "C:\\AXA\\retenciones\\entradaInsertRetenciones";
	static final String pathSalidaRetenciones = "C:\\AXA\\retenciones\\salidaInsertRetenciones\\";	
	
	/**
	 * Lee los archivos iwregistro para generar los inserts 
	 * de retenciones y mandarlos a escribir a archivos sql.
	 */
	public void generarInsertsRetenciones(){
		
		ReadXMLFile readXMLFile = new ReadXMLFile();
    	
    	//obteniendo archivos de directorio. Dependiendo los niveles se llamará varias veces
    	File[] files1 = LeerArchivoUtils.obtenerArchivosDeDirectorio(pathEntradaRetenciones);
    	    	
    	//genero una mapa en donde vamos a poner objetos de sistemas emisores
    	Map<String,String> mapaRegistrosXML = new HashMap<String,String>();
    	
    	System.out.println("CARGANDO XMLS......");
    	//por cada archivo de  xml
    	for (int i = 0; i < files1.length; i++) {		
    		mapaRegistrosXML.putAll(readXMLFile.insertsRegistrosDeXML(files1[i]));
		}
    	
		System.out.println("Tamanio del mapa de los archivos XML: "+ mapaRegistrosXML.size());
		
		try {
				EscribirArchivoUtils.generarArchivoSalidaBloquesSQL(mapaRegistrosXML, pathSalidaRetenciones + "insertRetenciones");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("PROCESO FINALIZADO!!!!");
		
	}
	
} // FIN DE CLASE
