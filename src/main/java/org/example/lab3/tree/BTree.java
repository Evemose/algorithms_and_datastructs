package org.example.lab3.tree;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import java.io.Serializable;
import java.util.*;
import java.util.List;

@Data
public class BTree implements Tree, Serializable {

    private final int factor;

    private Node root;

    private final DefaultTreeModel treeModel = new DefaultTreeModel(new DefaultMutableTreeNode("Root"));

    public BTree(int factor) {
        this.factor = factor;
        root = new Node();
    }

    public BTree() {
        this(50);
    }

    public BTree(Collection<Integer> values) {
        this();
        for (var value : values) {
            insert(value);
        }
    }

    public BTree(Collection<Integer> values, int factor) {
        this(factor);
        for (var value : values) {
            insert(value);
        }
    }

    @RequiredArgsConstructor
    @Data
    private class Node implements Comparable<Node> {

        @ToString.Exclude
        private Node parent;

        private NavigableSet<Integer> values = new TreeSet<>();

        private NavigableSet<Node> children = new TreeSet<>();

        @Override
        public String toString() {
            return "Node{" +
                    "parent=" + (parent == null ? "null" : parent.values) +
                    ", values=" + values +
                    ", children=" + children +
                    '}';
        }

        public Node(int... values) {
            for (var value : values) {
                this.values.add(value);
            }
        }

        public JTree toJTree() {
            return new JTree(toTreeModel());
        }

        private DefaultTreeModel toTreeModel() {
            return new DefaultTreeModel(toCell(null));
        }

        public List<Integer> toList() {
            var result = new LinkedList<Integer>();
            var valuesIterator = values.iterator();
            var childrenIterator = children.iterator();
            while (valuesIterator.hasNext()) {
                var value = valuesIterator.next();
                if (childrenIterator.hasNext()) {
                    var child = childrenIterator.next();
                    result.addAll(child.toList());
                }
                result.add(value);
            }
            if (!isLeaf()) {
                result.addAll(children.last().toList());
            }
            return result;
        }

        private DefaultMutableTreeNode toCell(MutableTreeNode cellParent) {
            var result = new DefaultMutableTreeNode(values.toString());
            for (var child : children) {
                result.add(child.toCell(result));
            }
            return result;
        }

        public boolean isLeaf() {
            return children == null || children.isEmpty();
        }

        public boolean contains(int key) {
            if (values.contains(key)) return true;
            if (isLeaf()) return false;
            var child = findChildThatContainsKey(key);
            return child.contains(key);
        }

        private Node split() {
            var left = new Node();
            var right = new Node();
            var middle = values.size() / 2;
            int middleValue;
            var iterator = values.iterator();

            for (int i = 0; i < middle; i++) {
                left.values.add(iterator.next());
            }
            middleValue = iterator.next();
            for (int i = middle + 1; i < values.size(); i++) {
                right.values.add(iterator.next());
            }

            values.clear();

            if (!isLeaf()) {
                var childIterator = children.iterator();
                for (int i = 0; i <= middle; i++) {
                    var child = childIterator.next();
                    left.children.add(child);
                    child.parent = left;
                }
                for (int i = middle + 1; i < children.size(); i++) {
                    var child = childIterator.next();
                    right.children.add(child);
                    child.parent = right;
                }
                children.clear();
            }

            Node result;
            if (parent == null) {
                result = new Node(middleValue);
                BTree.this.root = result;
            } else {
                result = this.parent.insert(middleValue, false);
                result.children.removeIf((val) -> val.values.isEmpty());
            }

            result.children.add(left);
            result.children.add(right);
            left.parent = result;
            right.parent = result;
            return result;
        }

        public void insert(int key) {
            insert(key, true);
        }

        private Node insert(int key, boolean checkChildren) {
            var result = this;
            if (values.size() >= factor * 2 - 1) {
                result = split();
            }
            if (checkChildren) {
                result.insertInner(key);
            } else {
                result.values.add(key);
            }
            return result;
        }

        private void insertInner(int key) {
            if (isLeaf()) {
                values.add(key);
            } else {
                var iterator = values.iterator();
                int i = 0;
                while (iterator.hasNext() && iterator.next() < key) {
                    i++;
                }
                var iteratorChildren = children.iterator();
                for (int j = 0; j < i; j++) {
                    iteratorChildren.next();
                }
                var child = iteratorChildren.next();
                child.insert(key);
            }
        }

        private void merge(@NonNull Node node) {
            values.addAll(node.values);
            children.addAll(node.children);
            node.children.forEach((val) -> val.parent = this);
            if (parent != null) {
                parent.children.remove(node);
            }
        }

        private int minWithRemove() {
            if (isLeaf()) {
                var result = values.first();
                remove(result);
                return result;
            } else {
                return children.first().minWithRemove();
            }
        }

        private int maxWithRemove() {
            if (isLeaf()) {
                var result = values.last();
                remove(result);
                return result;
            } else {
                return children.last().maxWithRemove();
            }
        }


        public void remove(int key) {
            if (values.contains(key)) {
                if (isLeaf()) {
                    values.remove(key);
                } else {
                    var left = findChildThatPrecedesKey(key+1);
                    var leftLength = left.values.size();
                    if (leftLength > factor - 1) {
                        var newKey = left.maxWithRemove();
                        values.remove(key);
                        values.add(newKey);
                    } else {
                        var right = findChildThatSucceedsKey(key-1);
                        var rightLength = right.values.size();
                        if (rightLength > factor - 1) {
                            var newKey = right.minWithRemove();
                            values.remove(key);
                            values.add(newKey);
                        } else {
                            left.values.add(key);
                            values.remove(key);
                            left.merge(right);
                            children.remove(right);
                            if (values.isEmpty()) {
                                if (parent == null) {
                                    left.parent = null;
                                    BTree.this.root = left;
                                } else {
                                    parent.children.remove(parent);
                                    parent.children.add(this);
                                    left.parent = parent;
                                }
                            }
                            left.remove(key);
                        }
                    }
                }
            } else {
                var childThatContainsKey = findChildThatContainsKey(key);
                if (childThatContainsKey.values.size() == factor - 1) {
                    var donorSibling = findChildThatPrecedesKey(key);
                    if (childThatContainsKey != donorSibling && donorSibling.values.size() > factor - 1) {
                        var precedingKey = findKeyThatPrecedesKey(key);
                        var donorKey = donorSibling.values.pollLast();

                        childThatContainsKey.values.add(precedingKey);
                        values.remove(precedingKey);

                        if (!donorSibling.isLeaf()) {
                            childThatContainsKey.children.add(donorSibling.children.pollLast());
                            donorSibling.children.last().parent = childThatContainsKey;
                        }

                        values.add(donorKey);

                    } else {
                        donorSibling = findChildThatSucceedsKey(key);
                        var succeedingKey = findKeyThatSucceedsKey(key);
                        if (donorSibling.values.size() > factor - 1) {
                            var donorKey = donorSibling.values.pollFirst();

                            childThatContainsKey.values.add(succeedingKey);
                            values.remove(succeedingKey);

                            if (!donorSibling.isLeaf()) {
                                childThatContainsKey.children.add(donorSibling.children.pollFirst());
                                donorSibling.children.first().parent = childThatContainsKey;
                            }

                            values.add(donorKey);
                        } else {
                            childThatContainsKey.values.add(succeedingKey);
                            values.remove(succeedingKey);
                            if (values.isEmpty()) {
                                if (parent == null) {
                                    childThatContainsKey.parent = null;
                                    BTree.this.root = childThatContainsKey;
                                } else {
                                    parent.children.remove(parent);
                                    parent.children.add(this);
                                    childThatContainsKey.parent = parent;
                                }
                            }
                            childThatContainsKey.merge(donorSibling);
                        }
                    }
                }
                childThatContainsKey.remove(key);
            }
        }

        private Node findChildThatPrecedesKey(int key) {
            return children.lower(new Node(findKeyThatPrecedesKey(key)));
        }

        private Node findChildThatContainsKey(int key) {
            if (values.first() > key) return children.first();
            if (values.last() < key) return children.last();
            return children.ceiling(new Node(findKeyThatPrecedesKey(key)));
        }

        private Node findChildThatSucceedsKey(int key) {
            return children.higher(new Node(findKeyThatSucceedsKey(key)));
        }

        private int findKeyThatPrecedesKey(int key) {
            return Objects.requireNonNullElse(values.lower(key), values.first());
        }

        private int findKeyThatSucceedsKey(int key) {
            return Objects.requireNonNullElse(values.higher(key), values.last());
        }

        @Override
        public int compareTo(@NonNull Node o) {
            if (o.values == null) return 1;
            if (values == null) return -1;
            if (values.isEmpty()) return o.values.isEmpty() ? 0 : -1;
            if (o.values.isEmpty()) return 1;
            return values.first().compareTo(o.values.first());
        }
    }
    @Override
    public void insert(int key) {
        if (!root.contains(key)) {
            root.insert(key);
        }
    }

    @Override
    public void remove(int key) {
        if (root.contains(key)) {
            root.remove(key);
        }
    }

    @Override
    public boolean contains(int key) {
        return root.contains(key);
    }

    @Override
    public int countStepsToKey(int key) {
        var result = 0;
        var current = root;
        while (current != null) {
            result++;
            if (current.values.contains(key)) {
                return result;
            }
            current = current.findChildThatContainsKey(key);
        }
        return -1;
    }

    @Override
    public List<Integer> toList() {
        return root.toList();
    }

    @Override
    public TreeModel toTreeModel() {
        return root.toTreeModel();
    }

    @Override
    public void visualize() {
        var treeVisualization = new TreeVisualization(this);
        treeVisualization.addInsertButton();
        treeVisualization.addRemoveButton();
        treeVisualization.addFindButton();
        treeVisualization.visualize();
    }
}
