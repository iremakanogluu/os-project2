package edu.estu.ceng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Node extends Thread {
    private final String name;
    private final List<Node> dependencies;

    public Node(String name) {
        this.name = name;
        this.dependencies = new ArrayList<>();
    }

    public void perform() {
        int time = new Random().nextInt(2000) + 1;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addDependencies(Node node) {
        dependencies.add(node);
    }

    public void run() {
        if (!dependencies.isEmpty()) {
            List<String> dependenciesNames = new ArrayList<>();
            for (Node node : dependencies) {
                dependenciesNames.add(node.name);
            }
            System.out.println("Node " + name + " is waiting for " + dependenciesNames);
        }
        for (Node dependency : dependencies) {
            if (dependency.isAlive()) {
                try {
                    dependency.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        System.out.println("Node" + name + " is being started");
        perform();
        System.out.println("Node" + name + " is completed.");
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            return ((Node) o).name.equals(this.name);
        }
        return false;
    }

    
}
