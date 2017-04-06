package mx.com.interware.generadorScripts.bean;

import java.io.Serializable;

public class LineaTO implements Serializable{
	
	private static final long serialVersionUID = -787653490621655576L;
	
	private String lineaCompleta;

	
	public String getLineaCompleta() {
		return lineaCompleta;
	}

	public void setLineaCompleta(String lineaCompleta) {
		this.lineaCompleta = lineaCompleta;
	}
	
}