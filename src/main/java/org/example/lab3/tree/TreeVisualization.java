package org.example.lab3.tree;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class TreeVisualization extends JFrame {
    private final Tree tree;
    private final JPanel panel = new JPanel();
    private final JTree currentTree;

    public TreeVisualization(Tree tree) {
        this.tree = tree;
        currentTree = new JTree();
        panel.setBounds(0, 0, 320, 50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        getContentPane().add(panel);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.NORTH);
        add(new JScrollPane(currentTree));
    }

    public void addInsertButton() {
        JButton insertButton = new JButton("Insert");
        insertButton.setBounds(0, 0, 100, 20);
        insertButton.addActionListener(e -> {
            String value = JOptionPane.showInputDialog("Enter value to insert");
            if (value != null) {
                tree.insert(parseInt(value));
                visualize();
            }
        });
        panel.add(insertButton);
    }

    public void addRemoveButton() {
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(0, 0, 100, 20);
        deleteButton.addActionListener(e -> {
            String value = JOptionPane.showInputDialog("Enter value to delete");
            if (value != null) {
                tree.remove(parseInt(value));
                visualize();
            }
        });
        panel.add(deleteButton);
    }

    public void addFindButton() {
        JButton findButton = new JButton("Find");
        findButton.setBounds(0, 0, 100, 20);
        findButton.addActionListener(e -> {
            String value = JOptionPane.showInputDialog("Enter value to find");
            if (value != null) {
                var res = tree.contains(parseInt(value));
                if (res) {
                    JOptionPane.showMessageDialog(null,
                            String.format("Tree contains value\nSteps to find: %d", tree.countStepsToKey(parseInt(value))));
                } else {
                    JOptionPane.showMessageDialog(null, "Tree does not contain value");
                }
            }
        });
        panel.add(findButton);
    }

    public void visualize() {
        var model = tree.toTreeModel();
        currentTree.setModel(model);
        setVisible(true);
        panel.setBackground(Color.BLACK);
        repaint();
    }

}
