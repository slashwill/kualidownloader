import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;

/**
 * @author sashwill
 * This program is intended to download the zip file for Kuali Coeus and the dependent zip files. It assumes the the URLs and the files names remain consistent.
 *
 */
public class DownloadKc {

  public static final String KC_VERSION = "1608.0042";
  public static final String PATH = "C:/LocalSVN/KC/";

  public static void main(String[] args) {
    System.out.println("starting process");
    DownloadKc downloadKc = new DownloadKc();
    downloadKc.deleteDirectory(new File(PATH));
    new File(PATH);
    downloadKc.getfile("https://github.com/kuali/kc/archive/coeus-" + KC_VERSION+".zip","kc-coeus-"+KC_VERSION+".zip");
                /*https://github.com/kuali/kc/archive/coeus-1608.0042.zip*/
    downloadKc.unZip();
    downloadKc.getZipfiles();
    System.out.println("finished");
  }

  private void getfile(String urlString,String fileName) {
System.out.println("getting file: "+urlString);
    try {
      URL url = new URL(urlString);
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      InputStream in = connection.getInputStream();
      FileOutputStream out = new FileOutputStream(PATH + fileName);
      copy(in, out, 1024);
      out.close();
    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void copy(InputStream input,
      FileOutputStream output,
      int bufferSize) throws IOException {

    byte[] buf = new byte[bufferSize];
    int n = input.read(buf);
    while (n >= 0) {
      output.write(buf, 0, n);
      n = input.read(buf);
    }
    output.flush();
  }

  private void unZip() {
    System.out.println("unZipping");
    UnzipUtility unzipper = new UnzipUtility();
    try {
      unzipper.unzip(PATH + "kc-coeus-"+KC_VERSION+".zip", PATH);
    }
    catch (Exception ex) {
      // some errors occurred
      ex.printStackTrace();
    }
  }
  
  private void getZipfiles(){
    try {
      System.out.println("reading pom");
      ReadXMLFile rm = new ReadXMLFile();
      Document doc = rm.readXMLFile(PATH +"kc-coeus-"+ KC_VERSION+"/pom.xml");
      XPath xpath = XPathFactory.newInstance().newXPath();
      String schemaSpyFile = xpath.evaluate("/project/properties/schemaspy.version/text()", doc);
      
      String coeusRiceFile = xpath.evaluate("/project/properties/rice.version/text()", doc);
      
      String coeusApiFile = xpath.evaluate("/project/properties/coeus-api-all.version/text()", doc);
      
      
      String s2sFile = xpath.evaluate("/project/properties/coeus-s2sgen.version/text()", doc);
      System.out.println("<property name=\"schemaSpyFile\" value=\"schemaspy-schemaspy-"+schemaSpyFile+"\"/>");
      System.out.println("<property name=\"coeusRiceFile\" value=\"kc-rice-rice-"+coeusRiceFile+"\"/>");
      System.out.println("<property name=\"coeusApiFile\" value=\"kc-api-coeus-api-"+coeusApiFile+"\"/>");
      System.out.println("<property name=\"s2sFile\" value=\"kc-s2sgen-coeus-s2sgen-"+s2sFile+"\"/>");
      System.out.println("<property name=\"coeusFile\" value=\"kc-coeus-"+KC_VERSION+"\"/>");
      
      System.out.println("getting schemaspy-"+schemaSpyFile);
      getfile("https://github.com/kuali/schemaspy/archive/schemaspy-"+schemaSpyFile+".zip","schemaspy-schemaspy-"+schemaSpyFile+".zip");
//             https://github.com/kuali/schemaspy/archive/schemaspy-1508.0162.zip
//             https://github.com/kuali/schemaspy/archive/schemaspy-1508.0162.zip
      
      System.out.println("getting rice-"+coeusRiceFile);
      getfile("https://github.com/kuali/kc-rice/archive/rice-"+coeusRiceFile+".zip","kc-rice-rice-"+coeusRiceFile+".zip");
//             https://github.com/kuali/kc-rice/archive/rice-2.5.3.1608.0005-kualico.zip
//             https://github.com/kuali/kc-rice/archive/rice-2.5.3.1608.0006-kualico.zip
      
      System.out.println("getting coeus-api-"+coeusApiFile);
      getfile("https://github.com/kuali/kc-api/archive/coeus-api-"+coeusApiFile+".zip","kc-api-coeus-api-"+coeusApiFile+".zip");
//             https://github.com/kuali/kc-api/archive/coeus-api-1604.0001.zip
//             https://github.com/kuali/kc-api/archive/coeus-api-1604.0001.zip
      
      System.out.println("gettingcoeus-s2sgen-"+s2sFile);
      getfile("https://github.com/kuali/kc-s2sgen/archive/coeus-s2sgen-"+s2sFile+".zip","kc-s2sgen-coeus-s2sgen-"+s2sFile+".zip");
//             https://github.com/kuali/kc-s2sgen/archive/coeus-s2sgen-1608.0003.zip
//             https://github.com/kuali/kc-s2sgen/archive/coeus-s2sgen-1608.0003.zip
      
      
      
      
    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  
   public void deleteDirectory(File fdir) {
     try {
      FileUtils.cleanDirectory(fdir);
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
     }
  
}
