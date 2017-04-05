package mx.com.interware.generadorScripts;

import mx.com.interware.generadorScripts.service.AppArchivosTextoService;
import mx.com.interware.generadorScripts.service.AppPdfsService;
import mx.com.interware.generadorScripts.service.AppService;


public class App {
			
	
    public static void main( String[] args ){
    	App app = new App();
//    	app.generarInsertsRetenciones();
    	app.renombrarPDFs();
//    	app.renombrarPdfsXmls();
//    	app.renombrarLiqFac();
    	
    }
    
    /**
     * Metodo para generar inserts de retenciones
     * en base a los archivos iwregistro tipo xml
     */
    public void generarInsertsRetenciones(){
    	
    	AppService appService = new AppService();
    	appService.generarInsertsRetenciones();
    }
    
    public void renombrarPDFs(){
    	
    	AppPdfsService appPdfsService = new AppPdfsService();    	
    	appPdfsService.renombrarPdfs();
    }
    
    public void renombrarPdfsXmls(){
    	AppPdfsService appPdfsService = new AppPdfsService();
    	appPdfsService.renombrarPdfsXmls();
    } 
    
    public void renombrarLiqFac(){
    	AppArchivosTextoService archTexto = new AppArchivosTextoService();
    	archTexto.renombrarArchBoveda();
    }
 
}