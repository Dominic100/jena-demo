package org.example;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GraphVizExporter {
    
    public static void exportToDot(Model model, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("digraph PS2Games {\n");
            writer.write("    rankdir=LR;\n");
            writer.write("    node [shape=box, style=filled];\n");
            writer.write("    graph [bgcolor=white, fontname=\"Arial\", fontsize=16];\n");
            writer.write("    edge [fontname=\"Arial\", fontsize=10];\n\n");
            
            // Define colors for different node types
            Map<String, String> nodeColors = new HashMap<>();
            nodeColors.put("Game", "#FF6B6B");
            nodeColors.put("RPG", "#FF6B6B");
            nodeColors.put("ActionGame", "#FF6B6B");
            nodeColors.put("FightingGame", "#FF6B6B");
            nodeColors.put("Developer", "#4ECDC4");
            nodeColors.put("Character", "#45B7D1");
            nodeColors.put("Protagonist", "#45B7D1");
            nodeColors.put("Antagonist", "#FF4757");
            nodeColors.put("GameSeries", "#96CEB4");
            nodeColors.put("Genre", "#FFEAA7");
            nodeColors.put("Award", "#DDA0DD");
            nodeColors.put("Publisher", "#74B9FF");
            
            // Process all statements
            StmtIterator iter = model.listStatements();
            
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                Resource subject = stmt.getSubject();
                Property predicate = stmt.getPredicate();
                RDFNode object = stmt.getObject();
                
                String subjectId = getNodeId(subject);
                String predicateLabel = getPredicateLabel(predicate);
                
                // Add subject node
                String subjectType = getNodeType(model, subject);
                String subjectColor = nodeColors.getOrDefault(subjectType, "#CCCCCC");
                String subjectLabel = getNodeLabel(model, subject);
                
                writer.write(String.format("    \"%s\" [label=\"%s\", fillcolor=\"%s\", tooltip=\"%s\"];\n", 
                    subjectId, subjectLabel, subjectColor, subjectType));
                
                if (object.isResource()) {
                    Resource objectRes = object.asResource();
                    String objectId = getNodeId(objectRes);
                    String objectType = getNodeType(model, objectRes);
                    String objectColor = nodeColors.getOrDefault(objectType, "#CCCCCC");
                    String objectLabel = getNodeLabel(model, objectRes);
                    
                    writer.write(String.format("    \"%s\" [label=\"%s\", fillcolor=\"%s\", tooltip=\"%s\"];\n", 
                        objectId, objectLabel, objectColor, objectType));
                    
                    // Add edge
                    writer.write(String.format("    \"%s\" -> \"%s\" [label=\"%s\"];\n", 
                        subjectId, objectId, predicateLabel));
                }
            }
            
            writer.write("}\n");
            System.out.println("GraphViz DOT file created: " + filename);
            System.out.println("To create an image, run: dot -Tpng " + filename + " -o ps2_games_graph.png");
            System.out.println("Or use online GraphViz tools like: https://dreampuf.github.io/GraphvizOnline/");
            
        } catch (IOException e) {
            System.err.println("Error writing DOT file: " + e.getMessage());
        }
    }
    
    private static String getNodeId(Resource resource) {
        String uri = resource.getURI();
        if (uri != null && uri.contains("#")) {
            return uri.substring(uri.indexOf("#") + 1);
        }
        return resource.toString().replaceAll("[^a-zA-Z0-9]", "_");
    }
    
    private static String getNodeLabel(Model model, Resource resource) {
        // Try to get a human-readable label
        String ns = "http://example.org/ps2games#";
        Property hasName = model.getProperty(ns + "hasName");
        Property hasTitle = model.getProperty(ns + "hasTitle");
        
        if (resource.hasProperty(hasName)) {
            return resource.getProperty(hasName).getString();
        }
        if (resource.hasProperty(hasTitle)) {
            return resource.getProperty(hasTitle).getString();
        }
        
        // Fall back to local name
        return getNodeId(resource);
    }
    
    private static String getNodeType(Model model, Resource resource) {
        StmtIterator typeIter = resource.listProperties(RDF.type);
        while (typeIter.hasNext()) {
            Statement stmt = typeIter.nextStatement();
            RDFNode type = stmt.getObject();
            if (type.isResource()) {
                String typeUri = type.asResource().getURI();
                if (typeUri != null && typeUri.contains("#")) {
                    return typeUri.substring(typeUri.indexOf("#") + 1);
                }
            }
        }
        return "Unknown";
    }
    
    private static String getPredicateLabel(Property predicate) {
        String uri = predicate.getURI();
        if (uri != null && uri.contains("#")) {
            return uri.substring(uri.indexOf("#") + 1);
        }
        return predicate.getLocalName();
    }
    
    public static void main(String[] args) {
        // Load the model from the turtle file
        Model model = ModelFactory.createDefaultModel();
        
        try {
            model.read("ps2_games_database.ttl", "TURTLE");
            exportToDot(model, "ps2_games_graph.dot");
        } catch (Exception e) {
            System.err.println("Error loading model: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
