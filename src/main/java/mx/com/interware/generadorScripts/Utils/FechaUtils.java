package mx.com.interware.generadorScripts.Utils;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;



public final class FechaUtils {

	private static final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
	private static final String DATE_PATTERN_GUIONBAJO = "(0?[1-9]|[12][0-9]|3[01])_(0?[1-9]|1[012])_((19|20)\\d\\d)";
	private static final String DATE_PATTERN_YYYYMMDD_SLASH ="((19|20)\\d\\d)/(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])";
	private static final String DATE_PATTERN_MMDDYYYY_SLASH = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)";
	private static HashMap<Integer, String> representacionesMes = null;
	private static HashMap<Integer, String> representacionesDias = null;
	public static final String FORMATO_SLASH_DDMMYYYY = "dd/MM/yyyy";
	public static final String FORMATO_SLASH_MMDDYYYY = "MM/dd/yyyy";
	public static final String FORMATO_GUION_YYYYMMDD = "yyyy-MM-dd";
	public static final String FORMATO_YYYYMMDD = "yyyyMMdd";
	public static final String FORMATO_PUNTO_DDMMYYYY = "dd.MM.yyyy";
	
	public static final String FORMATO_FECHA_COMPLETO = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'";
		
	/// FECHA DE CREACION Y CANCELACION 
	// 2017-03-09 00:00:00.0
	
	// ESTE FORMATO SE NECESITA EN LOS INSERTS RETENCIONES 2017-02-01 20:01:24.0
	public static final String FORMATO_FECHA_COMPLETO_2 = "yyyy-MM-dd kk:mm:ss.S";
	
	// FECHA EMISION Y TIMBRADO en el archivo
	// 2017-02-01T20:01:16
	public static final String FORMATO_FECHA_COMPLETO_3 = "yyyy-MM-dd'T'kk:mm:ss";
	
	
	public static final String FORMATO_FECHA_COMPLETO_US =  "EEE MMM dd HH:mm:ss z yyyy";
	public static final String FORMATO_FECHA_SLASH_HORA_MINUTO = "dd/MM/yyyy HH:mm";
	public static final String FORMATO_FECHA_HORA = "yyyy-MM-dd hh:mm aa";
	public static final String FORMATO_YYMMDD = "yyMMdd";
	
	public static final int ID_FECHA_HOY = 0;
	public static final int ID_FECHA_MANIANA = 0;
	public static final int ID_FECHA_AYER = -1;

	private static final String FECHA_PATTERN = "dd/MM/yyyy";
	private static final SimpleDateFormat FORMATEADOR = new SimpleDateFormat(
			FECHA_PATTERN);

	public static final String FECHA_PATTERN_ANIO = "yyyy/MM/dd";
	private static final SimpleDateFormat FORMATEADORANIO = new SimpleDateFormat(
			FECHA_PATTERN_ANIO);

	/**
	 * Convierte una fecha Date en un String
	 * 
	 * @param fecha
	 * @return
	 */
	public static String convertirDateToString(Date fecha) {
		String fechaString = null;
		if (fecha != null) {
			fechaString = FORMATEADOR.format(fecha);
		}

		return fechaString;
	}

	/**
	 * Convierte una fecha Date en un String
	 * 
	 * @param fecha
	 * @return
	 */
	public static String convertirDateToStringAnio(Date fecha) {
		String fechaString = null;
		if (fecha != null) {
			fechaString = FORMATEADORANIO.format(fecha);
		}
		return fechaString;
	}

	public static String cambiarFormatoAnio(String fecha)
			throws Exception {
		Calendar fechaC;
		try {
			fechaC = stringToCalendar(fecha);
		} catch (Exception e) {
			throw new Exception("cambiarFormatoAnioEx", e);
		}

		return convertirDateToStringAnio(fechaC.getTime());

	}

	/**
	 * Metodo Que regresa la fecha con un formato de entrada
	 * 
	 * @param Calendar
	 *            fechaA
	 * @param String
	 *            formato
	 * @return String
	 */
	public static String formato(Calendar fechaA, String formato) {

		SimpleDateFormat formatter = new SimpleDateFormat(formato);
		return formatter.format(fechaA.getTime());
	}

	/**
	 * Metodo que se encarga de cambiar el formato de
	 * "dd/MM/yyyy a "dd-MMM-yyyy" el formato explicitamente lo cambiara en
	 * notacion español
	 * 
	 * @param fechaString
	 * @param formatoAnterior
	 * @param formatoNuevo
	 * @return
	 * @throws Exception
	 */
	public static String cambiarFormato(String fechaString)
			throws Exception {

		String formato = "";
		if (esfechaValida(fechaString)) {
			String[] elems = fechaString.split("\\/");
			formato = elems[0] + "-"
					+ getRepresentacionesMes().get(new Integer(elems[1])) + "-"
					+ elems[2];
		} else {
			throw new Exception();
		}
		return formato;
	}

	/**
	 * Metodo que se encarga de cambiar el formato de la fecha recibida en
	 * String al formato de puntos: dd.MM.yyyy
	 * 
	 * @param fechaString
	 * @return
	 */
	public static String cambiarFormatoPuntos(String fechaString)
			throws Exception {
		String formato = "";
		if (esfechaValida(fechaString)) {
			formato = fechaString.replace('/', '.');
		} else {
			throw new Exception();
		}
		return formato;
	}

	/**
	 * M&eacute;todo que se encarga de cambiar el formato de la fecha recibida
	 * en String al formato de guiones: dd-<NOMBRE MES>-yyyy Ejemplo :
	 * 28/02/2013 cambia por 28-FEBRERO-2013
	 * 
	 * @param fechaString
	 *            fecha en formato dd/MM/yyyy
	 * @return
	 */
	public static String cambiarFormatoGuionDDMMYYYY(String fechaString)
			throws Exception {
		String formato = "";

		String mes = "";
		HashMap<Integer, String> meses = getRepresentacionesMesNombresCompletos();
		String nombreMes = "";
		if (esfechaValida(fechaString)) {
			formato = fechaString.replace('/', '-');
			mes = formato.substring(3, 5);
			nombreMes = (String) meses.get(new Integer(mes));

			formato = formato.replace("-" + mes + "-", "-" + nombreMes + "-");

		} else {
			throw new Exception();
		}
		return formato;
	}

	/**
	 * Metodo que convierte una fecha de tipo String a Calendar
	 * 
	 * @param fechaS
	 * @return objeto calendario con la fecha convertida
	 * @throws Exception
	 */
	public static Calendar stringToCalendar(String fechaS)
			throws Exception {

		SimpleDateFormat curFormater = new SimpleDateFormat(
				FORMATO_SLASH_DDMMYYYY);
		Date dateObj = null;
		Calendar calendar = null;
		if (fechaS != null && !fechaS.trim().isEmpty()) {
			try {
				dateObj = curFormater.parse(fechaS);
				if (dateObj != null) {
					calendar = Calendar.getInstance();
					calendar.setTime(dateObj);
				}
			} catch (ParseException e) {

				throw new Exception();

			}
		}
		return calendar;
	}
	
	/***
	 * Convierte una fecha en un formato especifico a Calendar.
	 * @param fechaS
	 * @param formatoFecha
	 * @return
	 * @throws Exception
	 */
	public static Calendar stringToCalendarFormato(String fechaS, String formatoFecha)
			throws Exception {

		SimpleDateFormat curFormater = new SimpleDateFormat(
				formatoFecha);
		Date dateObj = null;
		Calendar calendar = null;
		if (fechaS != null && !fechaS.trim().isEmpty()) {
			try {
				dateObj = curFormater.parse(fechaS);
				if (dateObj != null) {
					calendar = Calendar.getInstance();
					calendar.setTime(dateObj);
				}
			} catch (ParseException e) {

				throw new Exception();

			}
		}
		return calendar;
	}

	
	/**
	 * Metodo que convierte una fecha de tipo String a Calendar CQP
	 * 
	 * @param fechaS
	 * @return objeto calendario con la fecha convertida
	 * @throws Exception
	 */
	public static Calendar stringToCalendarGuion(String fechaS)
			throws Exception {

		SimpleDateFormat curFormater = new SimpleDateFormat(
				FORMATO_GUION_YYYYMMDD);
		Date dateObj = null;
		Calendar calendar = null;
		if (fechaS != null && !fechaS.trim().isEmpty()) {
			try {
				dateObj = curFormater.parse(fechaS);
				if (dateObj != null) {
					calendar = Calendar.getInstance();
					calendar.setTime(dateObj);
				}
			} catch (ParseException e) {

				throw new Exception();

			}
		}
		return calendar;
	}
	
	
	
	/**
	 * Validate date format with regular expression
	 * 
	 * @param date
	 *            date address for validation
	 * @return true valid date fromat, false invalid date format
	 * @throws ReporteException
	 */
	public static boolean esfechaValida(final String date) {
		final Pattern pattern = Pattern.compile(DATE_PATTERN);
		Matcher matcher;
		if (date == null) {
			return false;
		}
		matcher = pattern.matcher(date);

		if (matcher.matches()) {

			matcher.reset();

			if (matcher.find()) {

				String day = matcher.group(1);
				String month = matcher.group(2);
				int year = Integer.parseInt(matcher.group(3));

				if (verifica31Dias(day, month)) {
					// only 1,3,5,7,8,10,12 has 31 days
					return false;
				} else if (esFebrero(month)) {
					// leap year
					if (aniosBisiestos(day, year)) {
						return false;
					} else {
						if ("29".equals(day) || "30".equals(day)
								|| "31".equals(day)) {
							return false;
						}
					}
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * check: only 1,3,5,7,8,10,12 has 31 days
	 * 
	 * @param day
	 * @param month
	 * @return
	 */
	private static boolean verifica31Dias(String day, String month) {
		return "31".equals(day)
				&& ("04".equals(month) || "6".equals(month)
						|| "9".equals(month) || "11".equals(month)
						|| "04".equals(month) || "06".equals(month) || "09"
							.equals(month));
	}

	private static boolean esFebrero(String month) {
		return "2".equals(month) || "02".equals(month);
	}

	private static boolean aniosBisiestos(String day, int year) {
		return (year % 4 == 0) && ("30".equals(day) || "31".equals(day));
	}

	public static boolean esfechaValidaGuionBajo(final String date) {
		final Pattern pattern = Pattern.compile(DATE_PATTERN_GUIONBAJO);
		Matcher matcher;
		if (date == null) {
			return false;
		}
		matcher = pattern.matcher(date);

		if (matcher.matches()) {

			matcher.reset();

			if (matcher.find()) {

				String day = matcher.group(1);
				String month = matcher.group(2);
				int year = Integer.parseInt(matcher.group(3));

				if (verifica31Dias(day, month)) {
					// only 1,3,5,7,8,10,12 has 31 days
					return false;
				} else if (esFebrero(month)) {
					// leap year
					if (aniosBisiestos(day, year)) {
						return false;
					} else {
						if ("29".equals(day) || "30".equals(day)
								|| "31".equals(day)) {
							return false;
						}
					}
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/*
	 * Regresa un Map con la descripcion de todos los meses
	 */
	public static HashMap<Integer, String> getRepresentacionesMes() {
		if (representacionesMes == null) {
			representacionesMes = new HashMap<Integer, String>();
			representacionesMes.put(1, "ENE");
			representacionesMes.put(2, "FEB");
			representacionesMes.put(3, "MAR");
			representacionesMes.put(4, "ABR");
			representacionesMes.put(5, "MAY");
			representacionesMes.put(6, "JUN");
			representacionesMes.put(7, "JUL");
			representacionesMes.put(8, "AGO");
			representacionesMes.put(9, "SEP");
			representacionesMes.put(10, "OCT");
			representacionesMes.put(11, "NOV");
			representacionesMes.put(12, "DIC");
		}
		return representacionesMes;
	}

	/*
	 * Regresa un Map con el nombre de todos los meses
	 */
	public static HashMap<Integer, String> getRepresentacionesMesNombresCompletos() {
		if (representacionesMes == null) {
			representacionesMes = new HashMap<Integer, String>();
			representacionesMes.put(1, "ENERO");
			representacionesMes.put(2, "FEBRERO");
			representacionesMes.put(3, "MARZO");
			representacionesMes.put(4, "ABRIL");
			representacionesMes.put(5, "MAYO");
			representacionesMes.put(6, "JUNIO");
			representacionesMes.put(7, "JULIO");
			representacionesMes.put(8, "AGOSTO");
			representacionesMes.put(9, "SEPTIEMBRE");
			representacionesMes.put(10, "OCTUBRE");
			representacionesMes.put(11, "NOVIEBRE");
			representacionesMes.put(12, "DICIEMBRE");
		}
		return representacionesMes;
	}

	/**
	 * M&eacute;todo que compara las 2 fechas que llegan como cadenas en formato
	 * DD/MM/YYYY
	 * 
	 * @param fechaUno
	 * @param fechaDos
	 * @return int > 0 si fechaUno es mayor a la fechaDos int < 0 si fechaUno es
	 *         menor a la fechaDos int == 0 si fechaUno es igual a la fechaDos
	 * @throws Exception
	 */
	public static int compararFechas(String fechaUno, String fechaDos)
			throws Exception {

		if (fechaUno == null || fechaDos == null) {
			throw new IllegalArgumentException(
					"Alguna de las fechas que se desea comparar no est\u00e1 instanciada");
		}

		Calendar f1 = stringToCalendar(fechaUno);
		Calendar f2 = stringToCalendar(fechaDos);

		return f1.getTime().compareTo(f2.getTime());
	}

	/**
	 * M&eacute;todo que genera una instancia de un Mapa con los d&iacute;as de
	 * la semana.
	 * 
	 * @return HashMap<Integer, String> que tiene como llave el identificador
	 *         del d&iacute;a y como valor el nombre del d&iacute;a.
	 */
	public static HashMap<Integer, String> getRepresentacioneDias() {
		if (representacionesDias == null) {
			representacionesDias = new HashMap<Integer, String>();
			representacionesDias.put(Calendar.MONDAY, "Lunes");
			representacionesDias.put(Calendar.TUESDAY, "Martes");
			representacionesDias.put(Calendar.WEDNESDAY, "Mi\u00e9rcoles");
			representacionesDias.put(Calendar.THURSDAY, "Jueves");
			representacionesDias.put(Calendar.FRIDAY, "Viernes");
			representacionesDias.put(Calendar.SATURDAY, "S\u00e1bado");
			representacionesDias.put(Calendar.SUNDAY, "Domingo");
		}

		return representacionesDias;
	}

	/**
	 * M&eacute;todo que regresa el nombre del d&iacute;a de la semana,
	 * correspondiente al identificador que se proporciona como par&aacute;metro
	 * 
	 * @param idDia
	 *            Identificador del d&iacute;a de la semana.
	 * @return Cadena con el nombre correspondiente al identificador del
	 *         d&iacute;a.
	 */
	public static String getNombreDia(Integer idDia) {
		if (idDia == null) {
			throw new IllegalArgumentException(
					"El identificador del d\u00eda no puede ser nulo.");
		}

		String nombreDia = getRepresentacioneDias().get(idDia);
		if (nombreDia == null) {
			throw new IllegalArgumentException(
					"El identificador del d\u00eda no corresponde a los valores "
							+ "indicados por la clase Calendar");
		}
		return nombreDia;
	}

	/**
	 * Obtiene la diferencia en dias e 2 fechas en formato dd/MM/yyyy
	 * 
	 * @param firstDate
	 * @param lastDate
	 * @return
	 */
	public static int obtenerDiasDiferenciaFechas(String firstDate,
			String lastDate) throws Exception {
		if (firstDate == null || lastDate == null
				|| firstDate.trim().equals("") || lastDate.trim().equals("")) {
			return -1;
		}
		Calendar primFecha = Calendar.getInstance();
		Calendar segFecha = Calendar.getInstance();
		primFecha = stringToCalendar(firstDate);
		segFecha = stringToCalendar(lastDate);

		long mSegundos = 0L;
		int direfenciaDias = 0;
		mSegundos = Math.abs(segFecha.getTime().getTime()
				- primFecha.getTime().getTime());
		
		int segundos = (int)mSegundos / 1000;
		int minutos = segundos / 60;
		int horas = minutos / 60;
		int dias = horas / 24;
		
		System.out.println("segundos: " + segundos);
		System.out.println("minutos: " + minutos);
		System.out.println("horas: " + horas);
		System.out.println("dias: " + dias);
		
		
		direfenciaDias = (int) (mSegundos / 1000 / 60 / 60 / 24);
		return direfenciaDias;
	}

	/**
	 * Incrementa o decrementa la fecha con respecto a amount.
	 * 
	 * @param fecha
	 *            Cadena correpondiente a una fecha en formato dd/MM/yyyy
	 * @param amount
	 *            cantidad de dias que se desean aumentar.
	 * @return
	 * @throws Exception
	 */
	public static String agregarDias(String fecha, int amount)
			throws Exception {
		String fechaResultado = null;
		try {
			Calendar fechaC = stringToCalendar(fecha);
			fechaC.add(Calendar.DAY_OF_MONTH, amount);
			fechaResultado = convertirDateToString(fechaC.getTime());

		} catch (Exception e) {
			throw new Exception("AgregarDiasEx", e);
		}

		return fechaResultado;
	}

	/**
	 * Incrementa meses a la fecha de acuerdo al par&aactue;metro amount.
	 * 
	 * @param fecha
	 *            Cadena correpondiente a una fecha en formato dd/MM/yyyy
	 * @param amount
	 *            cantidad de meses que se desean aumentar.
	 * @return
	 * @throws Exception
	 */
	public static String agregaMeses(String fecha, int amount)
			throws Exception {
		String fechaResultado = null;
		try {
			Calendar fechaC = stringToCalendar(fecha);
			fechaC.add(Calendar.MONTH, amount);
			fechaResultado = convertirDateToString(fechaC.getTime());

		} catch (Exception e) {
			throw new Exception("AgregarMesesEx", e);
		}

		return fechaResultado;
	}

	/**
	 * Dada una fecha cualquiere regresa el primer d&iacute;a del mes al que
	 * corresponde la fecha.
	 * 
	 * @param fecha
	 *            Cadena correpondiente a una fecha en formato dd/MM/yyyy
	 * @return
	 * @throws Exception
	 */
	public static String obtenerPrimerDiaDelMes(String fecha)
			throws Exception {
		String primerDiaMes = null;

		try {
			Calendar fechaParametro = stringToCalendar(fecha);
			fechaParametro.set(Calendar.DATE, 1);
			primerDiaMes = convertirDateToString(fechaParametro.getTime());

		} catch (Exception e) {
			throw new Exception("ObtenerPrimerDiaDelMesEx", e);
		}

		return primerDiaMes;
	}

	/**
	 * Dada una fecha cualquiere regresa el &uacute;ltimo d&iacute;a del mes al
	 * que corresponde la fecha.
	 * 
	 * @param fecha
	 *            Cadena correpondiente a una fecha en formato dd/MM/yyyy
	 * @return
	 * @throws Exception
	 */
	public static String obtenerUltimoDiaDelmes(String fecha)
			throws Exception {
		String ultimoDiaMes = null;

		try {
			Calendar fechaParametro = stringToCalendar(fecha);
			fechaParametro.set(Calendar.DATE,
					fechaParametro.getActualMaximum(Calendar.DAY_OF_MONTH));
			ultimoDiaMes = convertirDateToString(fechaParametro.getTime());

		} catch (Exception e) {
			throw new Exception("ObtenerUltimoDiaDelmesEx", e);
		}

		return ultimoDiaMes;
	}
	
	/**
	 * Metodo que convierte una fecha de tipo String a Calendar con el formato
	 * especificado.
	 * 
	 * @param formato
	 * @param fechaHoraActual
	 * @return
	 * @throws Exception
	 */
	public static Calendar recuperarFecha(String formato, String fechaHoraActual)
			throws Exception {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formato);
		
		Date fechaDate = null;
		
		Calendar calendar = null;
		
		if (fechaHoraActual != null && !fechaHoraActual.isEmpty()) {
			try {
				fechaDate = simpleDateFormat.parse(fechaHoraActual);
				if (fechaDate != null) {
					calendar = Calendar.getInstance();
					calendar.setTime(fechaDate);
				}
			} catch (ParseException e) {

				throw new Exception();

			}
		}
		return calendar;
	}
	
	/**
	 * 	Convierte una fecha String en formato completo
	 *  regresa una  fecha String en fromato "yyyy-MM-dd" 
	 * 
	 * @param String fecha "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'"
	 * @return String fecha "yyyy-MM-dd"
	 */
	public static String cambiarFormatoCompletoToYYYYMMDD(String fecha) {
		String fechaString = null;
		if (fecha != null) {
			SimpleDateFormat form = new SimpleDateFormat(FORMATO_GUION_YYYYMMDD);
			Calendar calendar = null;
			try {
				calendar = FechaUtils.recuperarFecha(
						FORMATO_FECHA_COMPLETO, fecha);
				fechaString = form.format(calendar.getTime());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fechaString;
	}
	
	
	/**
	 * 	Convierte una fecha String en formato completo
	 *  regresa una  fecha String en fromato "yyyyMMdd" 
	 * 
	 * @param String fecha "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'"
	 * @return String fecha "yyyy-MM-dd"
	 */
	public static String cambiarFormatoYYYYMMDD(String fecha) {
		String fechaString = null;
		if (fecha != null) {
			SimpleDateFormat form = new SimpleDateFormat(FORMATO_YYYYMMDD);
			Calendar calendar = null;
			try {
				calendar = FechaUtils.recuperarFecha(
						FORMATO_FECHA_COMPLETO, fecha);
				fechaString = form.format(calendar.getTime());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return fechaString;
	}
	/**
	 *  Convirte una fecha string de tipo EEE MMM dd HH:mm:ss z yyyy
	 *  a una fecha string de tipo dd/MM/yy.
	 * @param fechaActual de tipo EEE MMM dd HH:mm:ss z yyyy
	 * @return fechaFormateada con formato dd/MM/yy.
	 */
	public static String convertStringToString(String fechaActual) {

		String fechaFormateada = "";
		try {
			if (fechaActual != null && !fechaActual.trim().isEmpty()) {
				DateFormat formatter = new SimpleDateFormat(
						FORMATO_FECHA_COMPLETO_US, Locale.US);
				Date date = (Date) formatter.parse(fechaActual);

				SimpleDateFormat format = new SimpleDateFormat(
						FORMATO_SLASH_DDMMYYYY);
				fechaFormateada = format.format(date);
			}

		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return fechaFormateada;
	}
	
	/**
	 * Convierte una fecha String en formato completo
	 *  regresa una  fecha String en fromato "dd/MM/YYYY".
	 *  
	 * @param fecha de tipo "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'".
	 * @return fechaString de tipo "yyyy-MM-dd".
	 * @throws Exception 
	 */
	public static String cambiarFormatoCompletoToDDMMYYYY(String fecha) throws Exception {
		String fechaString = null;
		if (fecha != null) {
			SimpleDateFormat form = new SimpleDateFormat(FORMATO_SLASH_DDMMYYYY);
			Calendar calendar = null;
			try {
				calendar = FechaUtils.recuperarFecha(
						FORMATO_FECHA_COMPLETO, fecha);
				fechaString = form.format(calendar.getTime());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		}
		return fechaString;
	}

	
	/**
	 * Obtiene una instancia de Calendar en funcion de un Date
	 * @param date
	 * @return
	 */
	public static Calendar dateToCalendar(Date date){ 
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(date);
		  return cal;
		}

	/**
	 * Se encarga de obtener la fecha en base a una cadena. <br/>
	 * Verifica si la fecha esta en formato dd/MM/YYYY, YYYY/MM/dd, MM/dd/YYYY ;
	 * en ese orden.
	 * 
	 * @param fecha
	 * @return fecha transformada a Calendar de acuerdo al formato
	 * @throws Exception  si la fecha no es valida
	 */
	public static Calendar stringToCalendarFormat(String fecha) throws Exception {
		if (fecha == null || fecha.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Argumentos insuficientes para transformar la fecha");
		}
		Calendar fechaCalendar = null;
		Pattern pattern = Pattern.compile(DATE_PATTERN);
		Matcher matcher;
		matcher = pattern.matcher(fecha);
		if (matcher.matches()) {
			fechaCalendar = stringToCalendar(fecha); // formato slash
		}else{
			matcher.reset();
			pattern = Pattern.compile(DATE_PATTERN_YYYYMMDD_SLASH);
			if(pattern.matcher(fecha).matches()){
				fechaCalendar = recuperarFecha(FECHA_PATTERN_ANIO, fecha);				
			}else{
				matcher.reset();
				pattern = Pattern.compile(DATE_PATTERN_MMDDYYYY_SLASH);
				if(pattern.matcher(fecha).matches()){
					fechaCalendar = recuperarFecha(FORMATO_SLASH_MMDDYYYY, fecha);	
				}
			}
		}
		
		if(fechaCalendar == null){
			throw new Exception();
		}
		
		return fechaCalendar;
	}
	
	
	/**
	 * Cambia de un formato de fecha dd/MM/yyyy a un formato yyMMdd
	 * @param fecha
	 * @return
	 */
	public static String cambiarFormatoToYYMMDD(String fecha) {
		String fechaString = null;
		if (fecha != null && !fecha.trim().isEmpty()) {
			SimpleDateFormat form = new SimpleDateFormat(FORMATO_YYMMDD);
			Calendar calendar = null;
			try {
				calendar = FechaUtils.recuperarFecha(
						FORMATO_SLASH_DDMMYYYY, fecha);
				fechaString = form.format(calendar.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fechaString;
	}
	
	/**
	 * Metodo que convierte una fecha de tipo String en formato YYMMDD 
	 * a tipo Calendar
	 * @param fechaS
	 * @return
	 * @throws Exception
	 */
	public static Calendar stringToCalendarYYMMDD(String fechaS)
			throws Exception {

		SimpleDateFormat curFormater = new SimpleDateFormat(
				FORMATO_YYMMDD);
		Date dateObj = null;
		Calendar calendar = null;
		if (fechaS != null && !fechaS.trim().isEmpty()) {
			try {
				dateObj = curFormater.parse(fechaS);
				if (dateObj != null) {
					calendar = Calendar.getInstance();
					calendar.setTime(dateObj);
				}
			} catch (ParseException e) {

				throw new Exception();

			}
		}
		return calendar;
	}
		
	/**
	 * M&eacute;todo que compara las 2 fechas que llegan como cadenas en formato
	 * YYMMDD
	 * @param fechaUno
	 * @param fechaDos
	 * @return int > 0 si fechaUno es mayor a la fechaDos int < 0 si fechaUno es
	 *         menor a la fechaDos int == 0 si fechaUno es igual a la fechaDos
	 * @throws Exception
	 */
	public static int compararFechasYYMMDD(String fechaUno, String fechaDos)
			throws Exception {

		if (fechaUno == null || fechaDos == null) {
			throw new IllegalArgumentException(
					"Alguna de las fechas que se desea comparar no est\u00e1 instanciada");
		}

		Calendar f1 = stringToCalendarYYMMDD(fechaUno);
		Calendar f2 = stringToCalendarYYMMDD(fechaDos);

		return f1.getTime().compareTo(f2.getTime());
	}
	
	/**
	 * Convierte una fecha de tipo Date a fecha de tipo XMLGregorianCalendar.
	 * 
	 * @param date
	 * @return
	 */
	public static XMLGregorianCalendar dateAsXMLGregorianCalendar(Date date) {
		try {
			GregorianCalendar now = new GregorianCalendar();
			now.setTime(date);
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(now);
			
		} catch (DatatypeConfigurationException dce) {
			return null;
		}
	}
	
	/**
	 * 	Convierte una fecha String en formato completo
	 *  regresa una  fecha String en fromato "yyyy-MM-dd" 
	 * 
	 * @param String fecha "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'"
	 * @return Date fecha
	 */
	public static Date cambiarFormatoCompletoToDate(String fecha) {
		Date fechaDate = null;
		if (fecha != null) {
			Calendar calendar = null;
			try {
				calendar = FechaUtils.recuperarFecha(
						FORMATO_GUION_YYYYMMDD, fecha);
				fechaDate = calendar.getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fechaDate;
	}
	
	public static Calendar cambiarFormatoCompletoToCalendar(String fecha) {
		Calendar fechaCalendar = null;
		if (fecha != null) {
			SimpleDateFormat form = new SimpleDateFormat(FORMATO_GUION_YYYYMMDD);
			try {
				fechaCalendar = FechaUtils.recuperarFecha(
						FORMATO_FECHA_COMPLETO, fecha);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fechaCalendar;
	}
	
	/**
	 * Obtiene los minutos de diferencia entre una fecha y otra en formato
	 * dd/MM/yyyy hh:mm
	 * @param firstDate
	 * @param lastDate
	 * @return
	 * @throws Exception
	 */
	public static int obtenerMinutosDiferenciaFechas(String firstDate,
			String lastDate) throws Exception {
		if (firstDate == null || lastDate == null
				|| firstDate.trim().isEmpty() || lastDate.trim().isEmpty()) {
			return -1;
		}
		Calendar primFecha = Calendar.getInstance();
		Calendar segFecha = Calendar.getInstance();
		primFecha = stringToCalendarFormato(firstDate, FORMATO_FECHA_SLASH_HORA_MINUTO);
		segFecha = stringToCalendarFormato(lastDate, FORMATO_FECHA_SLASH_HORA_MINUTO);

		long mSegundos = 0L;
		int direfenciaMinutos = 0;
		mSegundos = Math.abs(segFecha.getTime().getTime()
				- primFecha.getTime().getTime());
		
		int segundos = (int)mSegundos / 1000;
		direfenciaMinutos = segundos / 60;
		
		System.out.println("segundos: " + segundos);
		System.out.println("minutos: " + direfenciaMinutos);
		
		return direfenciaMinutos;
	}
	
	public static String cambiarFormatoTO(String fecha) {
		String fechaString = null;
		if (fecha != null) {
			SimpleDateFormat form = new SimpleDateFormat(FORMATO_FECHA_COMPLETO_2);
			Calendar calendar = null;
			try {
				// 
				calendar = FechaUtils.recuperarFecha(
						FORMATO_FECHA_COMPLETO_3, fecha);
				fechaString = form.format(calendar.getTime());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return fechaString;
	}
	
	public static void main(String[] args){
		
//		"dd/MM/yyyy hh:mm"
		String fecha="2017-02-01T20:01:16";
//		fecha=fecha.substring(0,10);
		
		System.out.println("fecha: " + fecha);

		try {
			
			String fechaFormato = cambiarFormatoTO(fecha);
			
			System.out.println("fechaFormato:" + fechaFormato);

		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}
