package mx.com.interware.generadorScripts.bean;

import java.io.Serializable;

public class LineaTO implements Serializable{
	
	private static final long serialVersionUID = -787653490621655576L;
	
	/**
	 * Atributo para poner toda la linea leida
	 */
	private String lineaCompleta;

	/** camibio mismo en master y rama
	 * 
	 * @return
	 */
	public String getLineaCompleta() {
		return lineaCompleta;
	}
	// lomismo a cambiar
	// lo mismo que se camio se modifica
	public void setLineaCompleta(String lineaCompleta) {
		this.lineaCompleta = lineaCompleta;
	}
	
}