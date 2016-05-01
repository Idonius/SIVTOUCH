package ec.facturacion.firmaxades.util.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.xpath.XPathConstants;

import ec.facturacion.autorizacion.AutorizacionComprobantesWs;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	
	
    /**
     * Transforma un archivo (File) a bytes
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] archivoToByte(File file) throws IOException {

        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException("EOF reached while trying to read the whole file");
            }
        } finally {
            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                //TODO LOGGER
            }
        }

        return buffer;
    }
    
    
    /**
     * Convierte el contenido de un String a archivo (File)
     * 
     * @param rutaArchivo
     * @param contenidoArchivo
     * @return 
     */
    public static File stringToArchivo(String rutaArchivo, String contenidoArchivo) {

        FileOutputStream fos = null;
        File archivoCreado = null;

        try {

            fos = new FileOutputStream(rutaArchivo);
            OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
            for (int i = 0; i < contenidoArchivo.length(); ++i) {
                out.write(contenidoArchivo.charAt(i));
            }
            out.close();

            archivoCreado = new File(rutaArchivo);

        } catch (Exception ex) {
            //Logger.getLogger(ArchivoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception ex) {
                //Logger.getLogger(ArchivoUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return archivoCreado;
    }

    
    
    /**
     * Permite obtener un valor de un archivo xml a traves de una expresion de xpath
     * 
     * @param xmlDocument
     * @param expression
     * @return 
     */
    public static String obtenerValorXML(File xmlDocument, String expression) {

        String valor = null;

        try {
            LectorXPath reader = new LectorXPath(xmlDocument.getPath());
            valor = (String) reader.leerArchivo(expression, XPathConstants.STRING);

        } catch (Exception e) {
            //Logger.getLogger(ArchivoUtils.class.getName()).log(Level.SEVERE, null, e);
        }

        return valor;
    }
    
    /**
     * Decodifica el elemento comprobante de una Respuesta de Autorizacion
     * 
     * 
     * @param pathArchivo
     * @return 
     */
    public static String decodeArchivoBase64(String pathArchivo) {
        String xmlDecodificado = null;
        try {
            File file = new File(pathArchivo);
            if (file.exists()) {

                String encd = obtenerValorXML(file, "/*/comprobante");
                xmlDecodificado = encd;

            } else {
                System.out.print("File not found!");
            }
        } catch (Exception e) {
            Logger.getLogger(AutorizacionComprobantesWs.class.getName()).log(Level.SEVERE, null, e);
        }
        return xmlDecodificado;
    }
    
    /**
     * Copia un archivo de un directorio a otro
     * 
     * @param archivoOrigen
     * @param pathDestino
     * @throws IOException 
     */
    public static boolean copiarArchivo(File archivoOrigen, String pathDestino) {
        FileReader in = null;
        boolean resultado = false;
        try {

            File outputFile = new File(pathDestino);
            in = new FileReader(archivoOrigen);
            FileWriter out = new FileWriter(outputFile);
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            in.close();
            out.close();
            resultado = true;

        } catch (Exception ex) {
            // TODO Logger.getLogger(ArchivoUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                // TODO Logger.getLogger(ArchivoUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return resultado;
    }

    /**
     * Convierte un array de bytes a archivo 
     * 
     * @param arrayBytes
     * @param rutaArchivo
     * @return 
     */
    public static boolean byteToFile(byte[] arrayBytes, String rutaArchivo) {
        boolean respuesta = false;
        try {
            File file = new File(rutaArchivo);
            file.createNewFile();
            FileInputStream fileInputStream = new FileInputStream(rutaArchivo);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrayBytes);
            OutputStream outputStream = new FileOutputStream(
                    rutaArchivo);
            int data;
            while ((data = byteArrayInputStream.read()) != -1) {
                outputStream.write(data);
            }

            fileInputStream.close();
            outputStream.close();
            respuesta = true;
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }
    
    public static File inputStreamToFile(InputStream inputStream, String fileName) {
	OutputStream outputStream = null;
        File response =  new File(fileName);
 
	try {
		// write the inputStream to a FileOutputStream
		outputStream =  new FileOutputStream(response);
 
		int read = 0;
		byte[] bytes = new byte[1024];
 
		while ((read = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}
 
		System.out.println("Done!");
 
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (outputStream != null) {
			try {
				outputStream.close();
                                
			} catch (IOException e) {
				e.printStackTrace();
			}
 
		}
	}
        return response;
    }

}
