import java.io.*;
import java.util.*;

class RedBlackTree {
    class Node {
        int key;
        Node left, right, parent;
        boolean color; // true for Red, false for Black

        Node(int key) {
            this.key = key;
            this.left = getTNULL();
            this.right = getTNULL();
            this.parent = null;
            this.color = true; // New node is always red
        }
    }

    private Node root;
    private final Node TNULL;

    public RedBlackTree() {
        TNULL = new Node(0);
        getTNULL().color = false;
        root = getTNULL();
    }

    public Node getTNULL() {
        return TNULL;
        
    }

    private void fixInsert(Node k) {
        Node u;
        while (k.parent != null && k.parent.color == true) {
            if (k.parent == k.parent.parent.right) {
                u = k.parent.parent.left;
                if (u.color == true) {
                    u.color = false;
                    k.parent.color = false;
                    k.parent.parent.color = true;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rightRotate(k);
                    }
                    k.parent.color = false;
                    k.parent.parent.color = true;
                    leftRotate(k.parent.parent);
                }
            } else {
                u = k.parent.parent.right;
                if (u.color == true) {
                    u.color = false;
                    k.parent.color = false;
                    k.parent.parent.color = true;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.color = false;
                    k.parent.parent.color = true;
                    rightRotate(k.parent.parent);
                }
            }
            if (k == root) {
                break;
            }
        }
        root.color = false;
    }

    public void insert(int key) {
        Node node = new Node(key);
        Node y = null;
        Node x = this.root;

        while (x != getTNULL()) {
            y = x;
            if (node.key < x.key) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        node.parent = y;
        if (y == null) {
            root = node;
        } else if (node.key < y.key) {
            y.left = node;
        } else {
            y.right = node;
        }

        if (node.parent == null) {
            node.color = false;
            return;
        }

        if (node.parent.parent == null) {
            return;
        }

        fixInsert(node);
    }

    public void deleteNode(int key) {
        deleteNodeHelper(this.root, key);
    }

    private void deleteNodeHelper(Node node, int key) {
        Node z = getTNULL();
        Node x, y;
        while (node != getTNULL()) {
            if (node.key == key) {
                z = node;
            }

            if (node.key <= key) {
                node = node.right;
            } else {
                node = node.left;
            }
        }

        if (z == getTNULL()) {
            System.out.println("Couldn't find key in the tree: " + key);
            return;
        }

        y = z;
        boolean yOriginalColor = y.color;
        if (z.left == getTNULL()) {
            x = z.right;
            rbTransplant(z, z.right);
        } else if (z.right == getTNULL()) {
            x = z.left;
            rbTransplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                rbTransplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }

            rbTransplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (yOriginalColor == false) {
            fixDelete(x);
        }
    }

    private void rbTransplant(Node u, Node v) {
        if (u.parent == null) {
            this.root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private Node minimum(Node node) {
        while (node.left != getTNULL()) {
            node = node.left;
        }
        return node;
    }

    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != getTNULL()) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != getTNULL()) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    private void fixDelete(Node x) {
        Node s;
        while (x != root && x.color == false) {
            if (x == x.parent.left) {
                s = x.parent.right;
                if (s.color == true) {
                    s.color = false;
                    x.parent.color = true;
                    leftRotate(x.parent);
                    s = x.parent.right;
                }

                if (s.left.color == false && s.right.color == false) {
                    s.color = true;
                    x = x.parent;
                } else {
                    if (s.right.color == false) {
                        s.left.color = false;
                        s.color = true;
                        rightRotate(s);
                        s = x.parent.right;
                    }

                    s.color = x.parent.color;
                    x.parent.color = false;
                    s.right.color = false;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                s = x.parent.left;
                if (s.color == true) {
                    s.color = false;
                    x.parent.color = true;
                    rightRotate(x.parent);
                    s = x.parent.left;
                }

                if (s.right.color == false && s.right.color == false) {
                    s.color = true;
                    x = x.parent;
                } else {
                    if (s.left.color == false) {
                        s.right.color = false;
                        s.color = true;
                        leftRotate(s);
                        s = x.parent.left;
                    }

                    s.color = x.parent.color;
                    x.parent.color = false;
                    s.left.color = false;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = false;
    }

    public Node search(int key) {
        Node currentNode = root;
        while (currentNode != getTNULL() && key != currentNode.key) {
            if (key < currentNode.key) {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
        }
        return currentNode;
    }
}

public class RuboNegra {
    public static void main(String[] args) {
        try {
            File file = new File("dados100mil.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            RedBlackTree rbt = new RedBlackTree();
            String line;
            Random rand = new Random();

            // Medir o tempo de inserção na árvore Rubro-Negra
            long startTime = System.currentTimeMillis();
            while ((line = br.readLine()) != null) {
                try {
                    int num = Integer.parseInt(line.trim());
                    rbt.insert(num);
                } catch (NumberFormatException e) {
                    System.err.println("Linha ignorada (não é um número válido): " + line);
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Tempo de inserção na Árvore Rubro-Negra: " + (endTime - startTime) + " ms");

            br.close();

            // Gerar números aleatórios e realizar operações
            int[] randomKeys = new int[50000];
            for (int i = 0; i < randomKeys.length; i++) {
                randomKeys[i] = rand.nextInt(19999) - 9999; // Números entre -9999 e 9999
            }

            startTime = System.currentTimeMillis();
            for (int key : randomKeys) {
                if (key % 3 == 0) {
                    rbt.insert(key);
                } else if (key % 5 == 0) {
                    rbt.deleteNode(key);
                } else {
                    // Contagem de ocorrências
                    RedBlackTree.Node foundNode = rbt.search(key);
                    if (foundNode != rbt.getTNULL()) {
                        System.out.println("Número " + key + " encontrado na árvore.");
                    } else {
                        System.out.println("Número " + key + " não encontrado na árvore.");
                    }
                }
            }
            long endTimeOp = System.currentTimeMillis();
            System.out.println("Tempo de operações na Árvore Rubro-Negra: " + (endTimeOp - startTime) + " ms");

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }
}
