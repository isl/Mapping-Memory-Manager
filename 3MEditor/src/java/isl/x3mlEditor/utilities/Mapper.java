///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package isl.x3mlEditor.utilities;
//
//import eu.delving.x3ml.X3MLEngine;
//import static eu.delving.x3ml.X3MLEngine.exception;
//import eu.delving.x3ml.X3MLGeneratorPolicy;
//import eu.delving.x3ml.engine.Generator;
//import java.io.InputStream;
//import java.util.List;
//import javax.xml.parsers.DocumentBuilderFactory;
//import org.w3c.dom.Element;
//
///**
// *
// * @author samarita
// */
//public class Mapper {
//
//
//    public X3MLEngine engine(String path) {
//        List<String> errors = X3MLEngine.validate(resource(path));
////        assertTrue("Invalid: " + errors, errors.isEmpty());
//        return X3MLEngine.load(resource(path));
//    }
//
//    public InputStream resource(String path) {
//        return Mapper.class.getResourceAsStream(path);
//    }
//    public Element document(String path) {
//        try {
//            return documentBuilderFactory().newDocumentBuilder().parse(resource(path)).getDocumentElement();
//        }
//        catch (Exception e) {
//            throw exception("Unable to parse " + path);
//        }
//    }
//    public DocumentBuilderFactory documentBuilderFactory() {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setNamespaceAware(true);
//        return factory;
//    }
//}
