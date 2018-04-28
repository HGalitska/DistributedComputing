import jdk.internal.org.xml.sax.ErrorHandler;
import jdk.internal.org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;

class SimpleErrorHandler implements ErrorHandler, org.xml.sax.ErrorHandler {
    public void warning(SAXParseException e) {
        System.out.println(e.getMessage());
    }

    public void error(SAXParseException e) {
        System.out.println(e.getMessage());
    }

    public void fatalError(SAXParseException e) {
        System.out.println(e.getMessage());
    }

    @Override
    public void warning(org.xml.sax.SAXParseException exception) {

    }

    @Override
    public void error(org.xml.sax.SAXParseException exception) {

    }

    @Override
    public void fatalError(org.xml.sax.SAXParseException exception) {

    }
}