package mx.com.interware.generadorScripts.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import mx.com.interware.generadorScripts.Utils.LeerArchivoUtils;

public class AppPdfsService {
	
	
	static final String pathEntradaFoliosNombres = "C:\\AXA\\retenciones\\renombradoPdfs\\nombresNuevosPdfs";
	static final String pathEntradaPDFs = "C:\\AXA\\retenciones\\renombradoPdfs\\entradaPdfsXmls";
//	static final String pathEntradaPDFs = "C:\\AXA\\retenciones\\renombradoPdfs\\testInpdf";
	static final String pathSalidaPDFs = "C:\\AXA\\retenciones\\renombradoPdfs\\salidaRenomPdfs\\";
	static final String pathSalidaRetencionesXMLs = "C:\\AXA\\retenciones\\renombradoPdfs\\salidaRenomXmls\\";
	
	static final String pathEntradaSerieFolioPolizaNombres = "C:\\AXA\\renombradoPDFs\\nuevosNombres";
	static final String pathEntradaPDFsXmls = "C:\\AXA\\renombradoPDFs\\entradaPdfsXmls";
//	static final String pathEntradaPDFsXmls = "C:\\AXA\\renombradoPDFs\\ejemploRenPdfXml";
	
	static final String pathSalidaPDFsXmls = "C:\\AXA\\renombradoPDFs\\salidaRenomPdfs\\";
	static final String pathSalidaXmls = "C:\\AXA\\renombradoPDFs\\salidaRenomXmls\\";
	
	static final String pathEntradaNombresNoEncontrados = "C:\\AXA\\conciliacion_2da\\nombresTXT";
	static final String pathEntradaNoencontrados = "C:\\AXA\\conciliacion_2da\\noEncontrados";
	
	public void renombrarPdfs(){
		
		try{
			
			ReadFile readFile = new ReadFile();
			
			Map<String,String> mapFoliosNombres = new HashMap<String, String>();
			
			
			File[] filesFoliosNombres = LeerArchivoUtils.obtenerArchivosDeDirectorio(pathEntradaFoliosNombres);
			
	    	System.out.println("CARGANDO NOMBRES NUEVOS DE PDFS y XMLS......");
	    	mapFoliosNombres.putAll(readFile.cargarNombresNuevosArchivo(filesFoliosNombres));
		
			System.out.println("Tamanio del mapa de los archivos NOMBRES NUEVOS PDFS "+ mapFoliosNombres.size());
		
		
			File[] filesPdfs = LeerArchivoUtils.obtenerArchivosDeDirectorio(pathEntradaPDFs);
			String nuevoNombre = null;
			
			int contadorFallidos = 0;
			int contEncontradosXML = 0;
			int contEncontradosPDF = 0;
			int contArchivosInvalidos = 0;
			 
			for (int cont=0; cont< filesPdfs.length ; cont++) {
				
				File archivoPdf = filesPdfs[cont];
				
				String extension = LeerArchivoUtils.getExtensionFile(archivoPdf);
//				System.out.println("EXTENSION: " + extension);
				
				if(!extension.equals("pdf")
						&& !extension.equals("xml")){
					System.out.println("EL ARCHIVO: " + archivoPdf.getName() + " NO ES UN pdf NI xml");
				}else{
					
					String nombreOldPdfArray[] =  archivoPdf.getName().trim().split("_");
					
					if(nombreOldPdfArray != null
							&& nombreOldPdfArray.length == 5){
								
						if(mapFoliosNombres.containsKey(nombreOldPdfArray[2].trim())){
														
							if(LeerArchivoUtils.getExtensionFile(archivoPdf).equals("pdf")){
								
								nuevoNombre =  pathSalidaPDFs + mapFoliosNombres.get(nombreOldPdfArray[2].trim()) + ".pdf";
								contEncontradosPDF++;
							}else if(LeerArchivoUtils.getExtensionFile(archivoPdf).equals("xml")){
								nuevoNombre =  pathSalidaRetencionesXMLs + mapFoliosNombres.get(nombreOldPdfArray[2].trim()) + ".xml";
								contEncontradosXML++;
							}
//							System.out.println("NUEVO NOMBRE: " + nuevoNombre);
							
							boolean correcto = archivoPdf.renameTo(new File(nuevoNombre));
							if(!correcto){
								System.out.println("FALLO AL RENOMBRAR ARCHIVO: " + archivoPdf.getName());
								contadorFallidos++;
							}
						}else{
							System.out.println("EL FOLIO: " + nombreOldPdfArray[2].trim() + " NO ESTA EN EL MAPA");
						}
					}else{
						System.out.println("NO SE OBTUVO ARREGLO DEL ARCHIVO: " + archivoPdf.getName().trim());
						contArchivosInvalidos++;
					}
				}
			} // fin for pdfs
			
			System.out.println("TOTAL DE ARCHIVOS A RENOMBRAR:"  + filesPdfs.length);
			System.out.println("TOTAL FALLIDOS: " + contadorFallidos);
			System.out.println("TOTAL RENOMBRADOS PDF: " + contEncontradosPDF);
			System.out.println("TOTAL RENOMBRADOS XML: " + contEncontradosXML);
			System.out.println("TOTAL DE ARCHIVOS INVALIDOS: " + contArchivosInvalidos);
			System.out.println("TOTAL ARCHIVOS RENOMBRADOS PDF Y XML: " + (contEncontradosPDF + contEncontradosXML));
			
		}catch (Exception e) {
				e.printStackTrace();
		} finally {
				
		}
	}
	
	public void renombrarPdfsXmls(){
		
		ReadFile readFile = new ReadFile();
		
		try {
			
			Map<String,String> mapFoliosNombres = new HashMap<String, String>();
			
			
			File[] filesFoliosNombres = LeerArchivoUtils.obtenerArchivosDeDirectorio(pathEntradaSerieFolioPolizaNombres);
			
	    	System.out.println("CARGANDO NOMBRES NUEVOS DE PDFS Y XMLS......");
	    	mapFoliosNombres.putAll(readFile.cargarNombresNuevosArchivoPdfsXmls(filesFoliosNombres));
		
			System.out.println("Tamanio del mapa de los archivos NOMBRES NUEVOS PDFS Y XMLS " + mapFoliosNombres.size());
			
			File[] filesPdfXmls = LeerArchivoUtils.obtenerArchivosDeDirectorio(pathEntradaPDFsXmls);
			
			
			int contadorFallidos = 0;
			int contEncontradosXML = 0;
			int contEncontradosPDF = 0;
			int contArchivosInvalidos = 0;
			
			for (int cont=0; cont< filesPdfXmls.length ; cont++) {
				
				File archivoPdfXml = filesPdfXmls[cont];
				
				// Solo se extrae el nombre del archivo sin la extencion
				String nombreArchivo = archivoPdfXml.getName().substring(0, archivoPdfXml.getName().indexOf("."));
				
				String nombreOldPdfArray[] =  nombreArchivo.trim().split("_");
					
				String serie = null;
				long folio = 0;
				String llave = null;
				
				if(LeerArchivoUtils.getExtensionFile(archivoPdfXml).equals("pdf")
						|| LeerArchivoUtils.getExtensionFile(archivoPdfXml).equals("xml")){
					
					if(nombreOldPdfArray != null
							&& nombreOldPdfArray.length == 2){
						
						serie = nombreOldPdfArray[0].trim();
						folio = Long.valueOf(nombreOldPdfArray[1].trim());
						llave = serie + folio;
						
						if(mapFoliosNombres.containsKey(llave)){
								
							String nuevoNombre = null;
							if(LeerArchivoUtils.getExtensionFile(archivoPdfXml).equals("pdf")){
								
								nuevoNombre =  pathSalidaPDFsXmls + mapFoliosNombres.get(llave) + ".pdf";
								contEncontradosPDF++;
							}else if(LeerArchivoUtils.getExtensionFile(archivoPdfXml).equals("xml")){
								nuevoNombre =  pathSalidaXmls + mapFoliosNombres.get(llave) + ".xml";
								contEncontradosXML++;
							}
														
//							System.out.println("NUEVO NOMBRE: " + nuevoNombre);
							boolean correcto = archivoPdfXml.renameTo(new File(nuevoNombre));
							if(!correcto){
								System.out.println("FALLO AL RENOMBRAR EL ARCHIVO: " + archivoPdfXml.getName());
								contadorFallidos++;
							}
							
						}else{
							System.out.println("LA LLAVE: " + llave + " NO ESTA EN EL MAPA");
						}						
					}else{
//						System.out.println("NO SE OBTUVO ARREGLO DEL ARCHIVO: " + archivoPdfXml.getName().trim() + " LONGITUD= " + nombreOldPdfArray.length);
						contArchivosInvalidos++;
					}

				}else{
					System.out.println("EL ARCHIVO: " + archivoPdfXml.getName() + " NO ES UN ARCHIVO VALIDO PARA RENOMBRAR");
				}
					
			} // fin for 
			
			System.out.println("TOTAL DE ARCHIVOS A RENOMBRAR:"  + filesPdfXmls.length);
			System.out.println("TOTAL FALLIDOS: " + contadorFallidos);
			System.out.println("TOTAL RENOMBRADOS PDF: " + contEncontradosPDF);
			System.out.println("TOTAL RENOMBRADOS XML: " + contEncontradosXML);
			System.out.println("TOTAL DE ARCHIVOS INVALIDOS: " + contArchivosInvalidos);
			System.out.println("TOTAL ARCHIVOS RENOMBRADOS PDF Y XML: " + (contEncontradosPDF + contEncontradosXML));
			
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		
	}
	
	public void renombrarTxTs(){
		
		String pathEntradaNoEncontrados = "C:\\AXA\\conciliacion_2da\\noEncontrados";
		String pathEntradaRENOMBRADOS = "C:\\AXA\\conciliacion_2da\\RENOMBRADOS";
		String NOMBRE = "\\noEncontrado_";
		
		try{
		
			File[] filesPdfs = LeerArchivoUtils.obtenerArchivosDeDirectorio(pathEntradaNoEncontrados);
			String nuevoNombre = null;
			
			int contadorTxt = 1553;
			int contArchivosValidos =0;
			 
			for (int cont=0; cont< filesPdfs.length ; cont++) {
				
				File archivoPdf = filesPdfs[cont];
				
				String extension = LeerArchivoUtils.getExtensionFile(archivoPdf);
//				System.out.println("EXTENSION: " + extension);
				
				if(!extension.equals("txt")){
					System.out.println("EL ARCHIVO: " + archivoPdf.getName() + " NO ES UN txt");
				}else{
						
					nuevoNombre =  pathEntradaRENOMBRADOS + NOMBRE + contadorTxt  + ".txt";
					boolean correcto = archivoPdf.renameTo(new File(nuevoNombre));
					if(!correcto){
						System.out.println("FALLO AL RENOMBRAR ARCHIVO: " + archivoPdf.getName());
						
					}else{
						contArchivosValidos++;
					}
				}
				
				contadorTxt++;
			} // fin for pdfs
			
			System.out.println("TOTAL DE ARCHIVOS A RENOMBRAR:"  + filesPdfs.length);
			System.out.println("TOTAL DE ARCHIVOS VALIDOS: " + contArchivosValidos);
			
		}catch (Exception e) {
				e.printStackTrace();
		} finally {
				
		}
	}
	
	
}