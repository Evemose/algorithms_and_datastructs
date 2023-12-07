package org.example.lab3.tree;

import javax.swing.tree.TreeModel;
import java.util.List;

public interface Tree {
    void insert(int key);
    void remove(int key);
    boolean contains(int key);
    void visualize();
    TreeModel toTreeModel();
    int countStepsToKey(int key);
    List<Integer> toList();
}
