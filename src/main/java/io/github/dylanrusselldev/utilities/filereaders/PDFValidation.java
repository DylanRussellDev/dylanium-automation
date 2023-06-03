/*
 * Filename: PDFValidation.java
 * Author: Dylan Russell
 * Purpose: Provides different methods that can be used to validate PDFs during test executions.
 */

package io.github.dylanrusselldev.utilities.filereaders;

import io.github.dylanrusselldev.utilities.core.CommonMethods;
import io.github.dylanrusselldev.utilities.core.Constants;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class PDFValidation {

    /**
     * Method to validate a PDF's text that is open in a browser tab/window.
     *
     * @param driver       WebDriver object
     * @param textToVerify the text to check for
     * @param pdfName      the PDF name for exception message
     */
    public static void verifyTextInBrowserPDF(WebDriver driver, String textToVerify, String pdfName) throws IOException {
        String content;
        PDFTextStripper p = new PDFTextStripper();

        if (driver.getCurrentUrl().contains("blob")) {
            saveBlobPDF(driver, pdfName);
            File file = new File("./downloadedPDF");
            PDDocument doc = PDDocument.load(file);
            content = p.getText(doc);
            doc.close();
            file.delete();
        } else {
            URL pdfURL = new URL(driver.getCurrentUrl());
            InputStream is = pdfURL.openStream();
            BufferedInputStream bf = new BufferedInputStream(is);
            PDDocument docu = PDDocument.load(bf);
            content = p.getText(docu);
            docu.close();
        } // end if else

        assertTrue(content.contains(textToVerify), "The expected text: " + textToVerify + " was not present in the pdf: " + pdfName);

    } // end verifyPDFContent()

    /**
     * Method to validate a PDF's text that is locally downloaded.
     *
     * @param txtVerify the text to check
     * @param pdfName   the PDF name for exception message
     */
    public static void verifyDownloadedPDFText(String txtVerify, String pdfName) throws IOException {
        URL pdfLoc = new URL("file:///" + CommonMethods.getNewestFile(Constants.TARGET_FILE_DOWNLOADS, "pdf"));
        InputStream is = pdfLoc.openStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        PDDocument doc = PDDocument.load(bis);
        PDFTextStripper ts = new PDFTextStripper();
        String txt = ts.getText(doc);
        doc.close();
        bis.close();
        is.close();

        String p = String.valueOf(CommonMethods.getNewestFile(Constants.TARGET_FILE_DOWNLOADS, "pdf"));
        Path path = Paths.get(p);

        try {
            Files.deleteIfExists(path);
        } catch (IOException i) {
            fail("File could not be located");
        } // end try-catch

        assertTrue(txt.contains(txtVerify), "Validation failed. The text: '" + txtVerify + " ' "
                + " was not present in the " + pdfName + " PDF\n");

    } // end verifyDownloadedPDFText()

    /**
     * Saves a local copy of a PDF that is accessed via a blob url.
     *
     * @param driver  WebDriver object
     * @param pdfName name of the PDF for exception handling
     */
    public static void saveBlobPDF(WebDriver driver, String pdfName) {
        String url = driver.getCurrentUrl();
        File file = new File("./downloadedPDF.pdf");

        try {
            FileOutputStream fos = new FileOutputStream(file);
            String b64 = getBytesBase64FromBlob(driver, url);
            byte[] decoder = Base64.getDecoder().decode(b64);
            fos.write(decoder);
            fos.close();
        } catch (Exception e) {
            fail("Could not save local copy of the report: " + pdfName);
        } // end try-catch

    } // end savePDFInTab

    /**
     * Executes a script that saves the page contents into a Base64 String
     * to use for converting into a PDF.
     *
     * @param driver WebDriver object
     * @param url    the URL
     * @return the result as a Base64 string to convert
     */
    private static String getBytesBase64FromBlob(WebDriver driver, String url) {
        final String script = " "
                + "var uri = arguments[0];"
                + "var callback = arguments[1];"
                + "var toBase64 = function(buffer)"
                + "{for(var r,n=new Uint8Array(buffer),t=n.length,a=new Uint8Array(4*Math.ceil(t/3)),"
                + "i=new Uint8Array(64),o=0,c=0;64>c;++c)i[c]="
                + "'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/'.charCodeAt(c);"
                + "for(c=0;t-t%3>c;c+=3,o+=4)r=n[c]<<16|n[c+1]<<8|n[c+2],a[o]=i[r>>18],a[o+1]=i[r>>12&63],a[o+2]=i[r>>6&63],a[o+3]=i[63&r];"
                + "return t%3===1?(r=n[t-1],a[o]=i[r>>2],a[o+1]=i[r<<4&63],a[o+2]=61,a[o+3]=61):t%3===2"
                + "&&(r=(n[t-2]<<8)+n[t-1],a[o]=i[r>>10],a[o+1]=i[r>>4&63],a[o+2]=i[r<<2&63],a[o+3]=61),"
                + "new TextDecoder('ascii').decode(a)};"
                + "var xhr = new XMLHttpRequest();"
                + "xhr.responseType = 'arraybuffer';"
                + "xhr.onload = function(){ callback(toBase64(xhr.response)) };"
                + "xhr.onerror = function(){ callback(xhr.status) };"
                + "xhr.open('GET','" + url + "');"
                + "xhr.send();";

        return (String) ((RemoteWebDriver) driver).executeAsyncScript(script, url);
    } // end getBytesBase64FromBlob

} // end PDFValidations.java
