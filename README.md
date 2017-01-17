# JavaCodeGraph
Exercise with grap coloring
A heuristic to solve the problem of graph coloring with hints. Graphs are considered with this form:
Graph 1:
K=3
Graph 2:
K=0
0 --> 1 2
1 --> 0 3
2 --> 0 3
3 --> 1 2
1<->2 0<->3
...

Node contains the description of the class node used in colorGreedy2.
Valid_coloring is used to check the coloration of a graph.

How to use the .jar file:
-From your labmachine, enter the directory where you have put  all the .jar files.

-Invoke java -jar colorGreedy.jar output.txt dataset.txt or java -jar colorGreedy2.jar output.txt dataset.txt to produce a coloration of the graphe. Here output.txt is the name of the file you will create , which will contain the coloration of the graph given by dataset.txt

-output.txt is created in your current directory. Invoke now  java -jar valid_coloring.jar dataset.txt output.txt to check if the coloration is correct. 

-Invoke java -jar valid_coloring.txt dataset.txt output.txt to check the coloration given by output.txt on the graph dataset.txt.

-colorGreedy2.jar uses the greedy method whereas colorChaitin.jar uses the Chaitin method and colorWelsh.jar the Wlesh method.
