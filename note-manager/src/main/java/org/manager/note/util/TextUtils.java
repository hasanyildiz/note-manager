package org.manager.note.util;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public final class TextUtils {
    private TextUtils() {}

    private static final TransformerFactory transformerFactory;
    private static final Transformer xmlTransformer;

    static {
        transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 10);

        try {
            xmlTransformer = transformerFactory.newTransformer(new StreamSource(new File("note-manager/xml-template.xsl")));

            xmlTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            xmlTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatXml(String raw) throws Exception {
        InputSource src = new InputSource(new StringReader(raw));
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src);

        Writer out = new StringWriter();

        xmlTransformer.transform(new DOMSource(document), new StreamResult(out));

        return out.toString();
    }
}
