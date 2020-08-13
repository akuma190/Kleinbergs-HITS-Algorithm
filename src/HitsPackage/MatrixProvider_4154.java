package HitsPackage;

public class MatrixProvider_4154 {

    private int[][] adjMat;
    private int noOfVertices = 0;

    public MatrixProvider_4154(int noOfVertices) {
        this.noOfVertices = noOfVertices;
        this.adjMat = new int[noOfVertices][noOfVertices];
        for (int i = 0; i < noOfVertices; i++)
            for (int j = 0; j < noOfVertices; j++)
                adjMat[i][j] = 0;
    }

    public void addEdge(int i, int j) {
        if (i >= noOfVertices || i < 0 || j >= noOfVertices || j < 0) {
            throw new IllegalArgumentException("Vertex does not exist: " + i + ", " + j);
        }
        adjMat[i][j] = 1;
    }

    public int get(int i, int j) {
        if (i >= noOfVertices || i < 0 || j >= noOfVertices || j < 0) {
            throw new IllegalArgumentException("Vertex does not exist: " + i + ", " + j);
        }
        return adjMat[i][j];
    }
}