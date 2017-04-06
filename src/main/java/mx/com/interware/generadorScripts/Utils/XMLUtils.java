package mx.com.interware.generadorScripts.Utils;

public class XMLUtils {
	
	/**
	 * Falta por implementar este metodo
	 * 
	 * @return
	 */
	@Deprecated
	public String xmlString(){
		String resValor = null;
		
		String responseStr="<dhsakdhjsajkda>dksjahdkjashdkjsahdksa>><zxzx<jhzxjk<dkjhdkjsadak  <EstatusUUID>dsdsa</EstatusUUID>>kjdhskjdhaskjdh attr=\"algo\" attr2=\"otro\" sakjdhsak>>Dsjkaldjaslkdjsal";
		int inicio= responseStr.indexOf("attr=")+"attr=".length();
		int fin =responseStr.indexOf(" attr2", responseStr.indexOf("attr=")+"attr=q".length());
		responseStr=responseStr.substring(
		    inicio
		    ,fin
		  );
		
		return resValor;
	}
	
}
