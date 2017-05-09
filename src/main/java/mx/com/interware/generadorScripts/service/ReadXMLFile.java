package mx.com.interware.generadorScripts.service;




import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import mx.com.interware.generadorScripts.Utils.FechaUtils;
import mx.com.interware.generadorScripts.Utils.LeerArchivoUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadXMLFile {
	
	public static final String QUERY_INSERT = "INSERT INTO MXS10000477A.CFDV3 " 
			+  "(LEGADO, FOLIO, VERSION, CVERET, NUMCERT, FEC_EMISION, FEC_TIMBRADO, FEC_CANCELA, RFCEMISOR, NACIONALIDAD, " 
			+ " NOMBRE_RECPT, RFC_RECEPT, MES_INI, MES_FIN, EJERCICIO, MONTOTOPE, MONTOTGRA, MONTOTRET, UUID, ESTATUS, "
			+ " FECCREACION, FECACTUALIZA, UUIDTIMBRE, PAC, ARCHIWCFDREG) "
			+ " VALUES (";
	
	
	public static final String FEHA_CREA_ACTUALIZA = "2017-03-09 00:00:00.0";

	  /**
	   * Lee el archivo tipo iwregistro(tipo xml) y va generando
	   * los inserts de retenciones.
	   * @param archivo
	   * @return
	   */
	  public Map<String,String> insertsRegistrosDeXML(File archivo) {

			Map<String,String> listQueries = new HashMap<String, String>();
			StringBuilder queryInsert = new StringBuilder();
						
			try{
				
//				System.out.println("NOMBRE ARCHIVO: " + archivo.getName());
				
				if(!LeerArchivoUtils.getExtensionFile(archivo).equals("iwretencion")){
					System.out.println("EL ARCHIVO: " + archivo.getName() + " NO ES UN iwretencion");
					
				}else{

					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document xmlDocument = dBuilder.parse(archivo);
					xmlDocument.getDocumentElement().normalize();
					
					/**  DEFINICION DE VARIABLES PARA ARMAR EL INSERT **/
					String legado = null; 
					String folio = null; 
					String version = null;
					String cveRet = null;
					String numCert = null;
					String fechaEmision = null;
					String fechaTimbrado = null;
					String fechaCancela = "null";
					String rfcEmisor = null;
					String nacionalidad = null;
					String nombreRecept = null;
					String rfcReceptor = null;
					String mesInicio = null;
					String mesFin = null;
					String ejercicio = null;
					String montoTotOperacion = null;
					String montoTotGrav = null;
					String montoTotRet = null;
					String uuid	= null; // ui
					String estatus = "1";
					String fechCreacion	= FEHA_CREA_ACTUALIZA; // fecha fija
					String fechActualiza = FEHA_CREA_ACTUALIZA; // fecha fija
					String uuidTimbre = null; // TimbreFiscalDigital.UUID
					String pac = null;
					String archIWRegistro = "/CFDIPROD/IWCFD/retenciones/factel/storage/retenciones/boveda/";
					// atriuto que se forma con la fecha y el nombre del archivo para agregarlo a la ruta total del archivo IW Registro
					String ultParteArchIWRegistro = "";
					
					
					Element elementRaiz = xmlDocument.getDocumentElement();
					System.out.println("Elemento Raiz :" + elementRaiz.getNodeName());
					
					NamedNodeMap atributosIWCFDRegistro =  elementRaiz.getAttributes();

					Node nodPac = atributosIWCFDRegistro.getNamedItem("pac");
					pac = nodPac.getNodeValue().replaceAll(":", "");
//					System.out.println("pac: " + pac);
					
					NodeList nList = elementRaiz.getChildNodes();
					
					for (int temp = 0; temp < nList.getLength(); temp++) {

						Node nodeHijoRaiz = nList.item(temp);

//						System.out.println("Element Actual: " + nodeHijoRaiz.getNodeName());
						if(nodeHijoRaiz.getNodeName().equals("InformacionAdicional")){
							
							NodeList nHijosInfAd = nodeHijoRaiz.getChildNodes();
							
							for (int cont = 0; cont < nHijosInfAd.getLength(); cont++) {
								
								Node nodeHijoInfAd = nHijosInfAd.item(cont);

//								System.out.println("Hijo Info Adicional: " + nodeHijoInfAd.getNodeName());
								
								if(nodeHijoInfAd.getNodeName().equals("Legado")){
									legado = nodeHijoInfAd.getTextContent();
//									System.out.println("Legado: " + legado);
								}
								
								if(nodeHijoInfAd.getNodeName().equals("Folio")){
									folio = nodeHijoInfAd.getTextContent();
//									System.out.println("folio: " + folio);
								}
								
								if(nodeHijoInfAd.getNodeName().equals("UUID")){
									uuid = nodeHijoInfAd.getTextContent();
//									System.out.println("uuid: " + uuid);
								}
																
								if(nodeHijoInfAd.getNodeName().equals("FechaEmision")){
									fechaEmision = FechaUtils.cambiarFormatoTO(nodeHijoInfAd.getTextContent());
//									System.out.println("fechaEmision: " + fechaEmision);
									
									// Se separa la fecha de emision para formarla y agregarla a la ruta del archivo IW registro
									String fechEmite = nodeHijoInfAd.getTextContent().substring(0, nodeHijoInfAd.getTextContent().indexOf("T")); // se espera fecha tipo 2017-02-01T20:01:16
									String fechEmisionArray[] = fechEmite.split("-"); // se espera solo la fecha tipo 2017-02-01
									if(fechEmisionArray != null
											&& fechEmisionArray.length == 3){
										
										ultParteArchIWRegistro = "/" + fechEmisionArray[0];
										ultParteArchIWRegistro += "/" + fechEmisionArray[1];
										ultParteArchIWRegistro += "/" + fechEmisionArray[2];
										ultParteArchIWRegistro += "/" + archivo.getName();
									}
								}
								
								if(nodeHijoInfAd.getNodeName().equals("RFCEmisor")){
									rfcEmisor = nodeHijoInfAd.getTextContent();
//									System.out.println("rfcEmisor: " + rfcEmisor);
									
									// Se conctena el rfcEmisor a la ruta del archivo IW registro
									archIWRegistro += rfcEmisor;
								}
							} // fin for hijos InfoAdicional
							
							// La ruta final del archivo IW registro
							archIWRegistro+= ultParteArchIWRegistro;
//							System.out.println("archIWRegistro: " + archIWRegistro);
							
						}else if(nodeHijoRaiz.getNodeName().equals("RETENCION")){
							
							NodeList nHijosRetencion = nodeHijoRaiz.getChildNodes();
							
							for (int cont = 0; cont < nHijosRetencion.getLength(); cont++) {
								
								Node nodeHijoRet = nHijosRetencion.item(cont);

//								System.out.println("Hijo Retencion: " + nodeHijoRet.getNodeName());
								
								if(nodeHijoRet.getNodeName().equals("retenciones:Retenciones")){
									
									NamedNodeMap atributosRetencion =  nodeHijoRet.getAttributes();

									Node nodVersion = atributosRetencion.getNamedItem("Version");
									version = nodVersion.getNodeValue();
//									System.out.println("version: " + version);
									
									Node nodCveRetenc = atributosRetencion.getNamedItem("CveRetenc");
									cveRet = nodCveRetenc.getNodeValue();
//									System.out.println("cveRet: " + cveRet);
									
									Node nodNumCert = atributosRetencion.getNamedItem("NumCert");
									numCert = nodNumCert.getNodeValue();
//									System.out.println("NumCert: " + numCert);
									
									NodeList nodeHijosRetenList = nodeHijoRet.getChildNodes();
									
									for(int conHRet=0; conHRet < nodeHijosRetenList.getLength(); conHRet++){
										
										Node nodeHijoRetRet = nodeHijosRetenList.item(conHRet);
										
//										System.out.println("Hijo retenciones:Retenciones: " + nodeHijoRetRet.getNodeName());
										
										if(nodeHijoRetRet.getNodeName().equals("retenciones:Complemento")){
											
											NodeList nodeHijosRetComplList = nodeHijoRetRet.getChildNodes();
											
											for(int conHRetCom=0; conHRetCom < nodeHijosRetComplList.getLength(); conHRetCom++){
												
												Node nodeHijoRetComp = nodeHijosRetComplList.item(conHRetCom);
												
//												System.out.println("Hijo retenciones:Complemento: " + nodeHijoRetComp.getNodeName());
												
												if(nodeHijoRetComp.getNodeName().equals("tfd:TimbreFiscalDigital")){
													
													NamedNodeMap atributosRetTimFis =  nodeHijoRetComp.getAttributes();
//													
													Node nodFechaTimbrado = atributosRetTimFis.getNamedItem("FechaTimbrado");
													fechaTimbrado = FechaUtils.cambiarFormatoTO(nodFechaTimbrado.getNodeValue());
//													System.out.println("fechaTimbrado: " + fechaTimbrado);
													
													Node nodUuidTimbre = atributosRetTimFis.getNamedItem("UUID");
													uuidTimbre = nodUuidTimbre.getNodeValue();
//													System.out.println("uuidTimbre: " + uuidTimbre);
												}
											}
										}
										
										if(nodeHijoRetRet.getNodeName().equals("retenciones:Receptor")){
											
											NodeList nodeHijosRetReceplList = nodeHijoRetRet.getChildNodes();
											
											for(int conHRetRecep=0; conHRetRecep < nodeHijosRetReceplList.getLength(); conHRetRecep++){
												
												Node nodeHijoRetRecep = nodeHijosRetReceplList.item(conHRetRecep);
												
//												System.out.println("Hijo retenciones:Receptor: " + nodeHijoRetRecep.getNodeName());
												
												if(nodeHijoRetRecep.getNodeName().equals("retenciones:Nacional")){
													
													NamedNodeMap atributosRetNac =  nodeHijoRetRecep.getAttributes();
													
													Node nodNomReceptor = atributosRetNac.getNamedItem("NomDenRazSocR");
													nombreRecept = nodNomReceptor.getNodeValue();
//													System.out.println("nombreRecept: " + nombreRecept);
													
													Node nodRFCReceptor = atributosRetNac.getNamedItem("RFCRecep");
													rfcReceptor = nodRFCReceptor.getNodeValue();
//													System.out.println("rfcReceptor: " + rfcReceptor);								
												}

											} // fin for
											
											NamedNodeMap atributosRetRecep =  nodeHijoRetRet.getAttributes();
											
											Node nodNacionalidad = atributosRetRecep.getNamedItem("Nacionalidad");
											nacionalidad = nodNacionalidad.getNodeValue();
//											System.out.println("nacionalidad: " + nacionalidad);
											
										} // receptor
										
										if(nodeHijoRetRet.getNodeName().equals("retenciones:Periodo")){
											
											NamedNodeMap atributosRetPeriodo =  nodeHijoRetRet.getAttributes();
											
											Node nodMesIni = atributosRetPeriodo.getNamedItem("MesIni");
											mesInicio = nodMesIni.getNodeValue();
//											System.out.println("mesInicio: " + mesInicio);
											
											Node nodMesFin = atributosRetPeriodo.getNamedItem("MesIni");
											mesFin = nodMesFin.getNodeValue();
//											System.out.println("mesFin: " + mesFin);
											
											Node nodEjercicio = atributosRetPeriodo.getNamedItem("Ejerc");
											ejercicio = nodEjercicio.getNodeValue();
//											System.out.println("ejercicio: " + ejercicio);
											
										} // fin retenciones:Periodo
										
										if(nodeHijoRetRet.getNodeName().equals("retenciones:Totales")){
											
											NamedNodeMap atributosRetTotales =  nodeHijoRetRet.getAttributes();
											
											Node nodMontoTotOperacion = atributosRetTotales.getNamedItem("montoTotOperacion");
											montoTotOperacion = nodMontoTotOperacion.getNodeValue();
//											System.out.println("montoTotOperacion: " + montoTotOperacion);
											
											Node nodMontoTotGrav = atributosRetTotales.getNamedItem("montoTotGrav");
											montoTotGrav = nodMontoTotGrav.getNodeValue();
//											System.out.println("montoTotGrav: " + montoTotGrav);
											
											Node nodMontoTotRet = atributosRetTotales.getNamedItem("montoTotRet");
											montoTotRet = nodMontoTotRet.getNodeValue();
//											System.out.println("montoTotRet: " + montoTotRet);
											
										} // fin retenciones:Totales
										
									} // fin for hijos retenciones:Retenciones
								}
							}
						}else if(nodeHijoRaiz.getNodeName().equals("Errores")){
							// NO SE OBTIENE NADA DEL TAG ERRORES
						}
				} // FIN FOR HIJOS ELEMENTO RAIZ
					
				// SE ARMA EL QUERY INSERT
				queryInsert.append(QUERY_INSERT);
								
				queryInsert.append("'" + legado + "', '" + folio + "', " + version + ", '" + cveRet + "', '" + numCert + "',");
				queryInsert.append(" '" + fechaEmision + "','"+ fechaTimbrado + "' , " + fechaCancela + ", '" + rfcEmisor + "',");
				queryInsert.append(" '" + nacionalidad + "', '" + nombreRecept + "', '" + rfcReceptor + "', '" + mesInicio + "',");
				queryInsert.append("'" + mesFin + "', '" + ejercicio + "', " + montoTotOperacion + ", " + montoTotGrav + ", " + montoTotRet + ",");
				queryInsert.append("'" + uuid + "', " + estatus + ", '" + fechCreacion + "', '" + fechActualiza + "', '" + uuidTimbre + "', ");
				queryInsert.append("'" + pac + "', '" + archIWRegistro + "');");
				
				listQueries.put(uuid, queryInsert.toString());
//				System.out.println("QUERY: " + queryInsert.toString());	
//				System.out.println("-------------------------------------");
			} // fin validacion TIPO DE archivo
		} catch (Exception e) {
				e.printStackTrace();
		} finally {
				
		}
		return listQueries;
	}
	
} // fin de clase