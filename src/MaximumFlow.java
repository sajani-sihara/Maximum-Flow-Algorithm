import java.text.DecimalFormat;
import java.util.*;

/* Author name    : Sajani Sihara
   IIT ID         : 2018075
   UOW ID         : w1714883
   Class name     : MaximumFlow
   Class function : Consists of the function findMaximumFlow that
                    1. finds the maximum flow of a graph
                    2. prints the graph provided
                    3. prints the residual graph
                    4. prints the time time taken for each graph
*/

public class MaximumFlow {
    public static class Graph {
            int nodes; // number of nodes in the graph
            int graph[][]; // 2 dimensional array - data structure used to input values into the algorithm

            // constructor for the Graph class
            public Graph(int nodes, int[][] graph) {
                this.nodes = nodes;
                this.graph = graph;
            }

            /* Name       : findMaximumFlow
               Function   : Finds the maximum flow of the graph provided
                            and prints the residual graph
               Parameters : source (start) node and sink (end) node
            */

            public int findMaximumFlow(int source, int sink) {
                // 2 dimensional array to store the residual graph
                // graph with edges with positive residual capacity
                // residual capacity = capacity - flow
                int[][] residualGraph = new int[nodes][nodes];
                // graph to keep track of distributed flows as a justification of the maximum flow possible
                int[][] distributedFlow = new int[nodes][nodes];

                // residual graph is initialised to the graph provided using two for loops
                for (int x = 0; x < nodes; x++) {
                    for (int y = 0; y < nodes; y++) {
                        residualGraph[x][y] = graph[x][y];
                    }
                }

                // array stores the previously visited nodes
                int[] parent = new int[nodes];

                // maximum flow is initialised to zero
                int maximumFlow = 0;

                // will enter this loop if isPathExist_BFS method returns a true value
                while (doesPathExistBFS(residualGraph, source, sink, parent)) {
                   int flowCapacity = Integer.MAX_VALUE;

                    // finds the minimum residual capacity of the edges of the path found
                    // finds the maximum flow of the path found
                    int t = sink;
                    while (t != source) {
                        int s = parent[t];
                        flowCapacity = Math.min(flowCapacity, residualGraph[s][t]);
                        t = s;
                    }

                    //updates the residual graph based on the flow capacity found
                    t = sink;
                    while (t != source) {
                        int s = parent[t];
                        // adding the residual capacity to the residual graph
                        residualGraph[s][t] -= flowCapacity; // source to sink - substract the flow capacity from the flow
                        residualGraph[t][s] += flowCapacity; // sink to source - add the flow capacity to the weight in the residual capacity
                        t = s;
                    }
                    //adding the flow capacities to the maximum value
                    maximumFlow += flowCapacity;
                }

                // distributed flow = graph - residual graph
                for (int i = 0; i < nodes; i++) {
                    for (int j = 0; j < nodes; j++) {
                        distributedFlow[i][j] = graph[i][j] - residualGraph[i][j]; //initialize residualFlow
                        if (distributedFlow[i][j] < 0) {
                            distributedFlow[i][j] = 0;
                        }
                    }
                }

                // printing the final residual graph - justifcation of the maximum flow possible
                System.out.println("Matrix of Residual Graph \n");
                for (int i = 0; i < distributedFlow.length; i++)
                    System.out.println(Arrays.toString(distributedFlow[i]));

                return maximumFlow;
            }

            /* Name      : doesPathExistBFS
              Function   : Finds out whether there is a path between the source node
                           and the sink node in a given graph using the breadth first
                           search traversal method
              Parameters : residual graph matrix, startValue, endValue and parent array
           */
            public boolean doesPathExistBFS(int[][] residualGraph, int startValue, int endValue, int[] parent) {
                // this boolean is set to false in the beginning
                // if there is a path found, this method will return a true value (pathExists = true)
                boolean pathExists = false;

                // array that stores the nodes that have been visited
                //if the graph has 6 nodes, then this array has 6 false values
                boolean[] visited = new boolean[nodes];

                // data structure used to traverse the graph using the breadth first search method
                Queue<Integer> BFS_Queue = new LinkedList<>();

                // adding zero (source node) to the queue
                BFS_Queue.add(startValue);
                // there is no previous node for 0 therefore parent is set at -1
                parent[startValue] = -1;
                // once the node is visited, this is set as true
                visited[startValue] = true;

                //while loop is entered if the queue is not empty
                while (BFS_Queue.isEmpty() == false) {
                    //poll removes the first element of the array
                    int u = BFS_Queue.poll(); //0 equates to "u" now

                    //visit all the adjacent nodes
                    for (int v = 0; v < nodes; v++) {
                        //if node is not visited and [u][v] edge capacity > 0, it will enter the if condition
                        if (visited[v] == false && residualGraph[u][v] > 0) {
                            BFS_Queue.add(v); //add that value to the queue
                            parent[v] = u;
                            visited[v] = true;
                        }
                    }
                }
                // leave the loop when the sink node has been visited
                // meaning it has traversed the whole graph going from source to sink therefore there is a path.
                pathExists = visited[endValue];
                return pathExists;
            }

         /* Name       : modifyMatrix
            Function   : Allows the user to,
                         1. delete an edge from the given graph
                         2. modify the capacity of a particular edge of a given graph
         */

        public int modifyMatrix() {
            // scanner to gather user input
            Scanner scanner = new Scanner(System.in);
            int loop        = 1; // used to iterate through the console
            int userChoice  = 0; // used to store the menu choice of the user
            int userColumn  = 0; // used to address the column of the matrix the user would like to modify
            int userRow     = 0; // used to address the row of the matrix the user would like to modify
            int newCapacity = 0; // used to store the new capacity the user enters

            // enters the loop as long as 1 is not equals to 0
            while (loop != 0) {

                // this is the sub menu console
                // it has 3 options
                System.out.println(
                        "SUB-MENU\n"                 +
                        "1 - Delete edge\n"          +
                        "2 - Change capacity\n"      +
                        "3 - Go back to main menu\n" +
                        "Select an option from menu above: ");

                // stores user's menu choice
                userChoice = scanner.nextInt();

                // if the user chooses to delete an edge
                if (userChoice == 1) {
                    System.out.println("Enter the start node: "); //asks the user for the row in which the element is
                    userRow    = scanner.nextInt(); // stores the row value
                    System.out.println("Enter the end node: "); //asks the user for the column in which the element is
                    userColumn = scanner.nextInt(); // stores the column value
                    //addresses the value in the specific row and column in the graph and then deletes it by making the value equate to 0
                    graph[userRow][userColumn] = 0;
                }

                // if the user chooses to change the capacity of an edge
                else if (userChoice == 2) {
                    System.out.println("Enter the start node: "); //asks the user for the row in which the element is
                    userRow     = scanner.nextInt(); // stores the row value
                    System.out.println("Enter the end node: "); //asks the user for the column in which the element is
                    userColumn  = scanner.nextInt(); // stores the column value
                    System.out.println("Enter the new capacity: "); //asks the user for the new capacity
                    newCapacity = scanner.nextInt(); // stores the new capacity value
                    //addresses the value in the specific row and column in the graph and then changes the value to the new capacity value
                    graph[userRow][userColumn] = newCapacity;
                }

                // if the user chooses to go back to the main menu
                else if (userChoice == 3){
                    MaximumFlow.mainConsole(); // mainConsole method will be called
                }

                // if the user enters any other input, it is invalid
                else {
                    System.out.println("Invalid input.");
                }

                // this will run the editedDataset method
                editedDataset(graph);

                // asks the user whether they would like to quit the program or see the menu again
                System.out.println("Would you like to see the menu again?\n" +
                        "1 - yes\n" +
                        "0 - exit console\nSelect an option from above: ");
                loop = scanner.nextInt();
            }
            return newCapacity;
        }
    }

    /* Name       : editedDataset
       Function   : Main function is to print the maximum flow once the user modifies the graph
       Parameters : the 2D graph array
    */
    public static void editedDataset(int graph[][]){

        // number of nodes is set to the length of the graph
        int nodes = graph.length;

        //print the provided graph as a matrix
        System.out.println("Matrix of Provided Graph \n");
        for (int i = 0; i < graph.length; i++)
            System.out.println(Arrays.toString(graph[i]));
        System.out.println(" ");

        Graph g         = new Graph(nodes, graph);  // a new graph object is called here
        int source      = 0;                        // source node is set to 0
        int sink        = nodes - 1;                // sink node is always the number of nodes - 1 (if there are 6 nodes, the sink node is 5)
        int max_flow    = g.findMaximumFlow(source, sink); // variable stores the max flow found using the findMaximumFlow method

        System.out.println("\nMaximum flow from " + source + " to " + sink + " = " + max_flow + ".\n"); // printing the maximum flow
        g.modifyMatrix();
    }

    /* Name       : firstDataset
       Function   : Contains the matrix of 6 nodes
    */
    public static double firstDataset(){
        Stopwatch startTime = new Stopwatch(); // new Stopwatch object is called here
        int nodes = 6; // number of nodes is set to 6
        int graph[][] = {
                //0  1    2   3   4   5
                {0, 16,  13,  0,  0,  0}, //0
                {0,  0,  10, 12,  0,  0}, //1
                {0,  4,   0,  0, 14,  0}, //2
                {0,  0,   9,  0,  0, 20}, //3
                {0,  0,   0,  7,  0,  4}, //4
                {0,  0,   0,  0,  0,  0}  //5
        };

        // print the provided graph as a matrix
        System.out.println("Matrix of Provided Graph \n");
        for (int i = 0; i < graph.length; i++)
            System.out.println(Arrays.toString(graph[i]));
        System.out.println(" ");


        Graph g     = new Graph(nodes, graph);
        int source  = 0;
        int sink    = 5;
        int max_flow = g.findMaximumFlow(source, sink);
        System.out.println("\nMaximum flow from " + source + " to " + sink + " = " + max_flow);

        double endExecTime = startTime.elapsedTime();
        System.out.println("Time taken for first dataset = " + endExecTime + " ps\n");
        g.modifyMatrix();
        return endExecTime; // returning the time taken
    }

    /* Name       : secondDataset
      Function   : Contains the matrix of 12 nodes
   */
    public static double secondDataset(){
        Stopwatch startTime = new Stopwatch();
        int nodes = 12;
        int graph[][] = {
                //0  1   2   3   4   5  6   7   8   9   10  11
                {0, 20, 15, 0,  0,  0,  0,  0,  0,  0,  0,  0}, //0
                {0, 0, 10, 16,  0,  0,  0,  0,  0,  0,  0,  0}, //1
                {0, 0,  0, 14, 12,  0,  0,  0,  0,  0,  0,  0}, //2
                {0, 0,  0,  0, 15, 14,  0,  0,  0,  0,  0,  0}, //3
                {0, 0,  0,  0,  0,  9, 11,  0,  0,  0,  0,  0}, //4
                {0, 0,  0,  0,  0,  0, 10, 12,  0,  0,  0,  0}, //5
                {0, 0,  0,  0,  0,  0,  0,  0, 10, 13,  0,  0}, //6
                {0, 0,  0,  0,  0,  0,  0,  0,  7, 11,  0,  0}, //7
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  9,  0}, //8
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  6, 10}, //9
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 10}, //10
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}  //11
        };

        System.out.println("Matrix of Provided Graph \n");
        for (int i = 0; i < graph.length; i++)
            System.out.println(Arrays.toString(graph[i]));
        System.out.println(" ");

        Graph g = new Graph(nodes, graph);
        int source = 0;
        int sink = 11;
        int max_flow = g.findMaximumFlow(source, sink);
        System.out.println("\nMaximum flow from " + source + " to " + sink + " = " + max_flow);

        double endExecTime = startTime.elapsedTime();
        System.out.println("Time taken for second dataset = " + endExecTime + " ps\n");
        g.modifyMatrix();
        return endExecTime;
    }

    /* Name       : thirdDataset
      Function   : Contains the matrix of 24 nodes
   */
    public static double thirdDataset(){
        Stopwatch startTime = new Stopwatch();
        int nodes = 24;
        int graph[][] = {
                //0  1   2   3   4   5  6   7   8   9   10  11  12  13  14  15  16  17  18  19  20  21  22  23
                {0, 20, 15, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //0
                {0, 0, 10, 16,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //1
                {0, 0,  0, 14, 12,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //2
                {0, 0,  0,  0, 15, 14,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //3
                {0, 0,  0,  0,  0,  0, 11,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //4
                {0, 0,  0,  0,  0,  0, 10, 12,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //5
                {0, 0,  0,  0,  0,  0,  0,  0, 10, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //6
                {0, 0,  0,  0,  0,  0,  0,  0,  7, 11,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //7
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  9,  9,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //8
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  6, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //9
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //10
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15, 14,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //11
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //12
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 13, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //13
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 18, 10,  0,  0,  0,  0,  0,  0,  0,  0}, //14
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 20, 10,  0,  0,  0,  0,  0,  0,  0}, //15
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 11, 20,  0,  0,  0,  0,  0,  0}, //16
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15, 16,  0,  0,  0,  0,  0}, //17
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 13, 13,  0,  0,  0,  0}, //18
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 12,  0,  0,  0}, //19
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 18,  0,  0}, //20
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 16, 13, 20}, //21
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15}, //22
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}  //23
        };

        System.out.println("Matrix of Provided Graph \n");
        for (int i = 0; i < graph.length; i++)
            System.out.println(Arrays.toString(graph[i]));
        System.out.println(" ");

        Graph g = new Graph(nodes, graph);
        int source = 0;
        int sink = 23;
        int max_flow = g.findMaximumFlow(source, sink);
        System.out.println("\nMaximum flow from " + source + " to " + sink + " = " + max_flow);

        double endExecTime = startTime.elapsedTime();
        System.out.println("Time taken for third dataset = " + endExecTime + " ps\n");
        g.modifyMatrix();
        return endExecTime;
    }

    /* Name       : fourthDataset
      Function   : Contains the matrix of 48 nodes
   */
    public static double fourthDataset(){
        Stopwatch startTime = new Stopwatch();
        int nodes = 48;
        int graph[][] = {
                //0  1   2   3   4   5  6   7   8   9   10,  11,  12,  13,  14,  15,  16  17  18  19  20  21  22  23  24  25  26  27  28  29  30  31  32  33  34  35  36  37  38  39  40  41  42  43  44  45  46  47
                {0, 20, 15, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //0
                {0, 0, 10, 16,  0,  0,  0, 13, 15, 17, 18, 13, 20, 25,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //1
                {0, 0,  0, 14, 12, 10, 11, 12, 13, 14, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //2
                {0, 0,  0,  0, 15, 14,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //3
                {0, 0,  0,  0,  0,  9, 11,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //4
                {0, 0,  0,  0, 15,  0, 10, 12,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //5
                {0, 8,  0,  0,  0,  0,  0,  0, 10, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //6
                {0, 0,  0, 12,  0, 15,  0,  0,  7, 11,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //7
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  9,  9,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //8
                {0, 0,  0, 15,  0,  0,  0,  0,  0,  0,  6, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //9
                {0, 0,  0,  0,  0, 12,  0,  0,  0,  0,  0, 10,  10,  11,  12,  13,  14,  15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //10
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 14,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //11
                {0, 0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0,  0, 13,  10,  11,  12,  13,  14,  15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //12
                {0, 0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0, 15, 10,  11,  12,  13,  14,  15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //13
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //14
                {0, 0,  0,  0,  0, 12,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //15
                {0, 0, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //16
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 12,  0,  0,  0,  0, 16,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //17
                {0, 0,  0,  0,  0, 12,  0,  0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //18
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 13,  0,  0,  0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //19
                {0, 0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 18,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //20
                {0, 0,  0,  0, 12,  0,  0, 13,  0,  0,  0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0,  0, 13, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //21
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //22
                {0, 0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0, 16,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //23
                {0, 0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0, 19,  9,  0, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //24
                {0, 0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  6, 18,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //25
                {0, 0,  0,  0, 12,  0,  0, 13,  0,  0,  0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0, 16, 13, 20,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //26
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 10, 15,  0,  0,  0,  0, 19,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //27
                {0, 0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0, 15, 16,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //28
                {0, 0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0, 13,  9, 10, 15,  0,  0,  0,  0, 11,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //29
                {0, 0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  6, 18, 17,  0,  0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //30
                {0, 0,  0,  0, 12,  0,  0, 13,  0,  0,  0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0, 16, 13, 20,  0,  0,  0,  0,  0,  4,  4,  0, 17,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //31
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 10, 15,  0,  0,  0,  0,  0,  0,  5,  0,  0, 18,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //32
                {0, 0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0, 15, 16,  0,  0,  0,  0,  6,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //33
                {0, 0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0,  0,  9, 10, 15,  0,  0,  0,  6,  0,  0,  0,  0,  0, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //34
                {0, 0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  6, 18,  0,  0,  0, 10, 13,  0,  0,  8,  0,  0,  0,  0,  0,  0, 18,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //35
                {0, 0,  0,  0, 12,  0,  0, 13,  0,  0,  0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0, 16, 13, 20,  0,  0, 20, 10,  0,  0,  0,  0,  0,  0,  0, 20,  0, 18,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //36
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 10, 15,  0,  0,  0, 12, 19,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //37
                {0, 0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0, 15, 16,  0,  0,  0, 13, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0, 19,  0,  0,  0,  0,  0,  0,  0,  0}, //38
                {0, 0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0,  0,  9, 10, 15,  0,  0,  0, 14, 11,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17,  0,  0,  0,  0,  0,  0,  0}, //39
                {0, 0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  6, 18,  0,  0,  0,  0,  0,  0,  0,  0,  9, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0, 14,  0,  0,  0,  0,  0,  0}, //40
                {0, 0,  0,  0, 12,  0,  0, 13,  0,  0,  0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0, 16, 13, 20,  0,  0,  0,  0,  0,  4,  4,  4, 17,  0,  0,  0,  0,  0,  0,  0,  0,  0, 19,  0,  0,  0,  0,  0}, //41
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 10, 15,  0,  0,  0,  0,  0,  0,  5,  0, 16, 18,  0,  0,  0,  0,  0,  0,  0,  0,  0, 25, 20,  0,  0,  0}, //42
                {0, 0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0, 15, 16,  0,  0,  0,  0,  6,  0,  0,  0, 19, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0, 16,  0,  0,  0}, //43
                {0, 0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0,  0,  9, 10, 15,  0,  0,  0,  6,  0,  0,  0,  0, 10, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0, 19,  0,  0}, //44
                {0, 0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  6, 18,  0,  0,  0, 10, 13,  0,  0,  8,  0,  0,  0,  0,  0, 10, 18,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15, 18}, //45
                {0, 0,  0,  0, 12,  0,  0, 13,  0,  0,  0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0, 16, 13, 20,  0,  0, 20, 10,  0,  0,  0,  0,  0,  0,  0, 20,  2, 18,  0,  0,  0,  0,  0,  0,  0,  0,  0, 20}, //46
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}  //47
        };

        System.out.println("Matrix of Provided Graph \n");
        for (int i = 0; i < graph.length; i++)
            System.out.println(Arrays.toString(graph[i]));
        System.out.println(" ");

        Graph g = new Graph(nodes, graph);
        int source = 0;
        int sink = 47;
        int max_flow = g.findMaximumFlow(source, sink);
        System.out.println("\nMaximum flow from " + source + " to " + sink + " = " + max_flow);

        double endExecTime = startTime.elapsedTime();
        System.out.println("Time taken for fourth dataset = " + endExecTime + " ps\n");
        g.modifyMatrix();
        return endExecTime;
    }

    /* Name      : dhFirstDataset
      Function   : Contains the matrix of 6 nodes without the modifyMatrix method
                   This is used to demonstrate the doubling hypothesis methodology
   */
    public static double dhFirstDataset(){
        Stopwatch startTime = new Stopwatch();
        int nodes = 6;
        int graph[][] = {
                //0  1    2   3   4   5
                {0, 16,  13,  0,  0,  0},
                {0,  0,  10, 12,  0,  0},
                {0,  4,   0,  0, 14,  0},
                {0,  0,   9,  0,  0, 20},
                {0,  0,   0,  7,  0,  4},
                {0,  0,   0,  0,  0,  0}
        };
        System.out.println("Matrix of Provided Graph \n");
        for (int i = 0; i < graph.length; i++)
            System.out.println(Arrays.toString(graph[i]));
        System.out.println(" ");
        Graph g = new Graph(nodes, graph);
        int source = 0;
        int sink = 5;
        int max_flow = g.findMaximumFlow(source, sink);
        System.out.println("\nMaximum flow from " + source + " to " + sink + " = " + max_flow);
        double endExecTime = startTime.elapsedTime();
        System.out.println("Time taken for first dataset = " + endExecTime + "\n");
        return endExecTime;
    }
    /* Name      : dhSecondDataset
      Function   : Contains the matrix of 12 nodes without the modifyMatrix method
                   This is used to demonstrate the doubling hypothesis methodology
    */
    public static double dhSecondDataset(){
        Stopwatch startTime = new Stopwatch();
        int nodes = 12;
        int graph[][] = {
                //0  1   2   3   4   5  6   7   8   9   10  11
                {0, 20, 15, 0,  0,  0,  0,  0,  0,  0,  0,  0}, //0
                {0, 0, 10, 16,  0,  0,  0,  0,  0,  0,  0,  0}, //1
                {0, 0,  0, 14, 12,  0,  0,  0,  0,  0,  0,  0}, //2
                {0, 0,  0,  0, 15, 14,  0,  0,  0,  0,  0,  0}, //3
                {0, 0,  0,  0,  0,  9, 11,  0,  0,  0,  0,  0}, //4
                {0, 0,  0,  0,  0,  0, 10, 12,  0,  0,  0,  0}, //5
                {0, 8,  0,  0,  0,  0,  0,  0, 10, 13,  0,  0}, //6
                {0, 0,  0,  0,  0,  0,  0,  0,  7, 11,  0,  0}, //7
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  9,  0}, //8
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  6, 10}, //9
                {0, 0,  0,  0,  0, 12,  0,  0,  0,  0,  0, 10}, //10
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}  //11
        };
        System.out.println("Matrix of Provided Graph \n");
        for (int i = 0; i < graph.length; i++)
            System.out.println(Arrays.toString(graph[i]));
        System.out.println(" ");
        Graph g = new Graph(nodes, graph);
        int source = 0;
        int sink = 11;
        int max_flow = g.findMaximumFlow(source, sink);
        System.out.println("\nMaximum flow from " + source + " to " + sink + " = " + max_flow);
        double endExecTime = startTime.elapsedTime();
        System.out.println("Time taken for second dataset = " + endExecTime + "\n");
        return endExecTime;
    }
    /* Name      : dhThirdDataset
       Function  : Contains the matrix of 24 nodes without the modifyMatrix method
                   This is used to demonstrate the doubling hypothesis methodology
   */
    public static double dhThirdDataset(){
        Stopwatch startTime = new Stopwatch();
        int nodes = 24;
        int graph[][] = {
                //0  1   2   3   4   5  6   7   8   9   10  11  12  13  14  15  16  17  18  19  20  21  22  23
                {0, 20, 15, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //0
                {0, 0, 10, 16,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //1
                {0, 0,  0, 14, 12,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //2
                {0, 0,  0,  0, 15, 14,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //3
                {0, 0,  0,  0,  0,  9, 11,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //4
                {0, 0,  0,  0,  0,  0, 10, 12,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //5
                {0, 8,  0,  0,  0,  0,  0,  0, 10, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //6
                {0, 0,  0,  0,  0,  0,  0,  0,  7, 11,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //7
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  9,  9,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //8
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  6, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //9
                {0, 0,  0,  0,  0, 12,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //10
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15, 14,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //11
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //12
                {0, 0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0, 13, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //13
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 18, 10,  0,  0,  0,  0,  0,  0,  0,  0}, //14
                {0, 0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0, 20, 10,  0,  0,  0,  0,  0,  0,  0}, //15
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 11, 20,  0,  0,  0,  0,  0,  0}, //16
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15, 16,  0,  0,  0,  0,  0}, //17
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 13, 13,  0,  0,  0,  0}, //18
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  9, 12,  0,  0,  0}, //19
                {0, 0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  6, 18,  0,  0}, //20
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 16, 13, 20}, //21
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 10, 15}, //22
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}  //23
        };
        System.out.println("Matrix of Provided Graph \n");
        for (int i = 0; i < graph.length; i++)
            System.out.println(Arrays.toString(graph[i]));
        System.out.println(" ");
        Graph g = new Graph(nodes, graph);
        int source = 0;
        int sink = 23;
        int max_flow = g.findMaximumFlow(source, sink);
        System.out.println("\nMaximum flow from " + source + " to " + sink + " = " + max_flow);
        double endExecTime = startTime.elapsedTime();
        System.out.println("Time taken for third dataset = " + endExecTime + "\n");
        return endExecTime;
    }
    /* Name      : dhFourthDataset
      Function   : Contains the matrix of 48 nodes without the modifyMatrix method
                   This is used to demonstrate the doubling hypothesis methodology
   */
    public static double dhFourthDataset(){
        Stopwatch startTime = new Stopwatch();
        int nodes = 48;
        int graph[][] = {
                //0  1   2   3   4   5  6   7   8   9   10,  11,  12,  13,  14,  15,  16  17  18  19  20  21  22  23  24  25  26  27  28  29  30  31  32  33  34  35  36  37  38  39  40  41  42  43  44  45  46  47
                {0, 20, 15, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //0
                {0, 0, 10, 16,  0,  0,  0, 13, 15, 17, 18, 13, 20, 25,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //1
                {0, 0,  0, 14, 12, 10, 11, 12, 13, 14, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //2
                {0, 0,  0,  0, 15, 14,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //3
                {0, 0,  0,  0,  0,  9, 11,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //4
                {0, 0,  0,  0, 15,  0, 10, 12,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //5
                {0, 8,  0,  0,  0,  0,  0,  0, 10, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //6
                {0, 0,  0, 12,  0, 15,  0,  0,  7, 11,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //7
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  9,  9,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //8
                {0, 0,  0, 15,  0,  0,  0,  0,  0,  0,  6, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //9
                {0, 0,  0,  0,  0, 12,  0,  0,  0,  0,  0, 10,  10,  11,  12,  13,  14,  15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //10
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 14,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //11
                {0, 0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0,  0, 13,  10,  11,  12,  13,  14,  15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //12
                {0, 0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0, 15, 10,  11,  12,  13,  14,  15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //13
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //14
                {0, 0,  0,  0,  0, 12,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //15
                {0, 0, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //16
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 12,  0,  0,  0,  0, 16,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //17
                {0, 0,  0,  0,  0, 12,  0,  0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //18
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 13,  0,  0,  0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //19
                {0, 0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 18,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //20
                {0, 0,  0,  0, 12,  0,  0, 13,  0,  0,  0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0,  0, 13, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //21
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //22
                {0, 0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0, 16,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //23
                {0, 0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0, 19,  9,  0, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //24
                {0, 0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  6, 18,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //25
                {0, 0,  0,  0, 12,  0,  0, 13,  0,  0,  0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0, 16, 13, 20,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //26
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 10, 15,  0,  0,  0,  0, 19,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //27
                {0, 0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0, 15, 16,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //28
                {0, 0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0, 13,  9, 10, 15,  0,  0,  0,  0, 11,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //29
                {0, 0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  6, 18, 17,  0,  0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //30
                {0, 0,  0,  0, 12,  0,  0, 13,  0,  0,  0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0, 16, 13, 20,  0,  0,  0,  0,  0,  4,  4,  0, 17,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //31
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 10, 15,  0,  0,  0,  0,  0,  0,  5,  0,  0, 18,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //32
                {0, 0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0, 15, 16,  0,  0,  0,  0,  6,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //33
                {0, 0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0,  0,  9, 10, 15,  0,  0,  0,  6,  0,  0,  0,  0,  0, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //34
                {0, 0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  6, 18,  0,  0,  0, 10, 13,  0,  0,  8,  0,  0,  0,  0,  0,  0, 18,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //35
                {0, 0,  0,  0, 12,  0,  0, 13,  0,  0,  0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0, 16, 13, 20,  0,  0, 20, 10,  0,  0,  0,  0,  0,  0,  0, 20,  0, 18,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //36
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 10, 15,  0,  0,  0, 12, 19,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //37
                {0, 0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0, 15, 16,  0,  0,  0, 13, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0, 19,  0,  0,  0,  0,  0,  0,  0,  0}, //38
                {0, 0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0,  0,  9, 10, 15,  0,  0,  0, 14, 11,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17,  0,  0,  0,  0,  0,  0,  0}, //39
                {0, 0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  6, 18,  0,  0,  0,  0,  0,  0,  0,  0,  9, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0, 14,  0,  0,  0,  0,  0,  0}, //40
                {0, 0,  0,  0, 12,  0,  0, 13,  0,  0,  0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0, 16, 13, 20,  0,  0,  0,  0,  0,  4,  4,  4, 17,  0,  0,  0,  0,  0,  0,  0,  0,  0, 19,  0,  0,  0,  0,  0}, //41
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 10, 15,  0,  0,  0,  0,  0,  0,  5,  0, 16, 18,  0,  0,  0,  0,  0,  0,  0,  0,  0, 25, 20,  0,  0,  0}, //42
                {0, 0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0, 15, 16,  0,  0,  0,  0,  6,  0,  0,  0, 19, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0, 16,  0,  0,  0}, //43
                {0, 0,  0,  0,  0, 13,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15,  0,  0,  0,  0,  0,  0,  9, 10, 15,  0,  0,  0,  6,  0,  0,  0,  0, 10, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0, 19,  0,  0}, //44
                {0, 0,  0,  0,  0,  0,  0,  0, 10,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  6, 18,  0,  0,  0, 10, 13,  0,  0,  8,  0,  0,  0,  0,  0, 10, 18,  0,  0,  0,  0,  0,  0,  0,  0,  0, 15, 18}, //45
                {0, 0,  0,  0, 12,  0,  0, 13,  0,  0,  0,  0, 12,  0,  0,  0,  0,  0,  0,  0,  0, 16, 13, 20,  0,  0, 20, 10,  0,  0,  0,  0,  0,  0,  0, 20,  2, 18,  0,  0,  0,  0,  0,  0,  0,  0,  0, 20}, //46
                {0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}  //47
        };
        System.out.println("Matrix of Provided Graph \n");
        for (int i = 0; i < graph.length; i++)
            System.out.println(Arrays.toString(graph[i]));
        System.out.println(" ");
        Graph g = new Graph(nodes, graph);
        int source = 0;
        int sink = 47;
        int max_flow = g.findMaximumFlow(source, sink);
        System.out.println("\nMaximum flow from " + source + " to " + sink + " = " + max_flow);
        double endExecTime = startTime.elapsedTime();
        System.out.println("Time taken for fourth dataset = " + endExecTime + "\n");
        return endExecTime;
    }
    /* Name      : doublingHypothesis()
       Function  : This is used to demonstrate the doubling hypothesis methodology
     */
    public static void doublingHypothesis() {
        DecimalFormat df=new DecimalFormat("0.00");

        // stores the time taken from each dataset into these variables
        double time1    = MaximumFlow.dhFirstDataset();
        double time2    = MaximumFlow.dhSecondDataset();
        double time3    = MaximumFlow.dhThirdDataset();
        double time4    = MaximumFlow.dhFourthDataset();

        // ratios of the times are calculated as follows
        double ratio1   = time2/time1;
        double ratio2   = time3/time2;
        double ratio3   = time4/time3;

        // the ratios are used to calculate the log ratios
        double lgRatio1 = Math.log(ratio1) / Math.log(2);
        double lgRatio2 = Math.log(ratio2) / Math.log(2);
        double lgRatio3 = Math.log(ratio3) / Math.log(2);

        // printing the input data size, time taken, ratio and log ratios
        System.out.println("Input\tTime (ps)\tRatio\tlg Ratio");
        System.out.println("6\t\t"  + df.format(time1)  + "\t\t" +      " - "        + "\t\t" +         "  - "      );
        System.out.println("12\t\t" + df.format(time2)  + "\t\t" + df.format(ratio1) + "\t"   + df.format(lgRatio1));
        System.out.println("24\t\t" + df.format(time3)  + "\t\t" + df.format(ratio2) + "\t"   + df.format(lgRatio2));
        System.out.println("48\t\t" + df.format(time4)  + "\t\t" + df.format(ratio3) + "\t"   + df.format(lgRatio3));
    }

    /*Name     : mainConsole()
      Function : Contains the main menu
                 1. User can run the first dataset with the 6 node matrix
                 2. User can run the second dataset with the 12 node matrix
                 3. User can run the third dataset with the 24 node matrix
                 4. User can run the fourth dataset with the 48 node matrix
                 5. User can see the doubling hypothesis methodology applied to the four datasets
     */
    public static void mainConsole(){
        Scanner scanner = new Scanner(System.in);
        int loop       = 1;
        int userChoice = 0;

        while (loop != 0) {
            System.out.println("MAIN MENU\n" +
                    "1 - Run first dataset  (6  nodes)\n" +
                    "2 - Run second dataset (12 nodes)\n" +
                    "3 - Run third dataset  (24 nodes)\n" +
                    "4 - Run fourth dataset (48 nodes)\n" +
                    "5 - Doubling Hypothesis\n"           +
                    "Select an option from menu above: ");
            userChoice = scanner.nextInt();

            if (userChoice == 1) {
                MaximumFlow.firstDataset();
            } else if (userChoice == 2) {
                MaximumFlow.secondDataset();
            } else if (userChoice == 3) {
                MaximumFlow.thirdDataset();
            } else if (userChoice == 4) {
                MaximumFlow.fourthDataset();
            } else if (userChoice == 5) {
            MaximumFlow.doublingHypothesis();
            } else {
                System.out.println("Invalid input.");
            }
            System.out.println("Would you like to see the menu again?\n" +
                    "1 - yes\n" +
                    "0 - exit console\nSelect an option from above:  ");
            loop = scanner.nextInt();
        }
    }

    public static void main(String[] args) {
        MaximumFlow.mainConsole();
    }
}