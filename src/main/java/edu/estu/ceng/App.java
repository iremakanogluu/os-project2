package edu.estu.ceng;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;


public class App 
{
    public static void main( String[] args ) throws CloneNotSupportedException{
        MyOptions myOptions = new MyOptions();
        CmdLineParser parser = new CmdLineParser(myOptions);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }

        String fileName = myOptions.fileName;
        List<Node> nodes = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    continue;
                }
                String[] tokens = line.split("->");

                if(tokens.length==1){
                    Node node = new Node(tokens[0]);
                    nodes.add(node);
                }
                else{
                    String[] dependencies = tokens[0].split(",");
                    Node node = new Node(tokens[1]);

                    addToNodes(nodes, dependencies, node);
                }
            }

            for(Node node:nodes){
                node.start();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void addToNodes(List<Node> nodes, String[] dependencies, Node node) {
        if(!nodes.contains(node)){
            addDependenciesToNodes(nodes, dependencies, node);
            nodes.add(node);
        }
        else{
            node = nodes.get(nodes.indexOf(node));
            addDependenciesToNodes(nodes, dependencies, node);
        }
    }

    private static void addDependenciesToNodes(List<Node> nodes, String[] dependencies, Node node) {
        for(String dependency : dependencies){
            Node depNode = new Node(dependency);
            if(!nodes.contains(depNode)){
                nodes.add(depNode);
                node.addDependencies(depNode);
            }
            else{
                node.addDependencies(nodes.get(nodes.indexOf(depNode)));
            }
        }
    }
}

