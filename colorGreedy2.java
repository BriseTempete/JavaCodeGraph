import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class colorGreedy2 {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static LinkedList readL(String line) { // Renvoie une liste chaînée contenant en 1er élément
		LinkedList L=new LinkedList();     // single si on a une ligne de connection simple
		int n=line.length();         // double si on a une ligne de connection double
		int i=0;                     // liste vide si on a une ligne de texte ( dans ce cas print)
		int node=0;
		if (line.charAt(i)=='G' || line.charAt(i)=='K') {// Print si texte
			System.out.println(line);
			return L;
		}
		else { 
			while (i<n && line.charAt(i)!=' ' && line.charAt(i)!='<') {//Lecture 1 chiffre
				node=node*10+Character.getNumericValue(line.charAt(i));
				i++;
				}
			if (line.charAt(i)==' ') { // test du type de ligne: single? 
				L.add("single");
				L.add(node);// 2eme élément: node père
				i=i+5;
				while (i<n) {// Eléments suivants: nodes fils
					node=0;  
					while (i<n && line.charAt(i)!=' ') {// détermination nodes fils
						node=node*10+Character.getNumericValue(line.charAt(i));
						i++;
						}
					L.add(node);
					i++;
					}
				}
			else if (line.charAt(i)=='<') {//test du type de ligne: double?
				L.add("double");
				i=0;
				while (i<n) {// On stocke deux noeuds liés double dans un tableau à 2 éléments
					int[] C=new int[2];
					node=0;
					while (i<n && line.charAt(i)!='<') {//1er élément
						node=node*10+Character.getNumericValue(line.charAt(i));
						i++;
						}
					C[0]=node;
					node=0;
					i=i+3;
					while (i<n && line.charAt(i)!=' ') {//2eme élément
						node=node*10+Character.getNumericValue(line.charAt(i));
						i++;
						}
					i++;
					C[1]=node;
					L.add(C);
					}	
				}
		}
		return L;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Integer> size(String sample) throws IOException { //Renvoie une liste d'entier correspondant
		ArrayList allSize=new ArrayList<Integer>();              //au plus grand noeud de chaque graphe +2
		BufferedReader in = new BufferedReader(new FileReader(sample));
		String line=in.readLine();
		while (line!=null) {
			int n=0;
			int m=0;
			line=in.readLine();
			int p=line.length();
			int K=0;
			for (int s=2;s<p;s++) {
				K=K*10+Character.getNumericValue(line.charAt(s));
			}
			line=in.readLine();
			while (line!=null && line.charAt(0)!='G') {
				LinkedList L=readL(line);
				line=in.readLine();
				if (L.get(0)=="single") {
					L.remove(0);
					n=(int) L.remove(0);
				}
				else if (L.get(0)=="double") {
					L.remove(0);
					while (!L.isEmpty()) {
						int[] link=(int[]) L.remove(0);
						m=Math.max(Math.max(m, link[0]), link[1]);
					}
				}
			}
			allSize.add(Math.max(K-1,Math.max(n, m))+2);
		}
		return allSize;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList<Integer>[] convert(LinkedList[] graph) {//Forme un tableau dont la case i contient les noeuds 
		LinkedList link=graph[graph.length-1];     // doublement liés au noeud i
		ArrayList<Integer>[] output=new ArrayList[graph.length-1];
		for (int j=0;j<output.length;j++) {
			output[j]=new ArrayList();
		}
		for (int i=0;i<link.size();i++) {
			int[] C=(int[]) link.get(i);
			output[C[0]].add(C[1]);
			output[C[1]].add(C[0]);
		}
		return output;
	}
	public static int[] greedy(LinkedList[] graph,int K) {//Colore le graphe avec un algo greedy amélioré
		int size=graph.length;
		int[] color=new int[size];                            //Size est le nombre de noeuds, color[i] la coloration du noeud i
		for (int i=0;i<K;i++) {//Initialisation à -1
			color[i]=i;
		}
		for (int k=K;k<size;k++) {
			color[k]=-1;
		}
		ArrayList<Integer>[] link=convert(graph);
		int colorUsed=K;//Nombre de couleurs utilisées 
		for (int j=K;j<size-1;j++) {//On colore chaque node un à un
			LinkedList node=graph[j];
			if (color[j]==-1) {
				ArrayList<Integer> C=new ArrayList<Integer>();//On stocke les couleurs des voisins de node dans C
				while (!node.isEmpty()) {
					C.add(color[(int) node.remove(0)]);
				}
				Collections.sort(C);//On les trie
				while (!link[j].isEmpty() && color[link[j].get(0)]==-1) {
					link[j].remove(0);
				}
				if (!link[j].isEmpty() && !C.contains(color[link[j].get(0)])) {
					color[j]=color[link[j].get(0)];
				}
				else {
					int col=0;
					boolean b=false;
					while (!C.isEmpty() && !b) {//On détermine la plus petite couleur non présente chez les voisins
						int adj=C.remove(0);
						if (adj>col) {
							b=true;
						}
						else if (adj==col) { 
							col++;
						}
					}
					color[j]=col;
				}//On colore le noeud			
			}
		}
		int hints=0;
		LinkedList H=graph[size-1];
		while (!H.isEmpty()) {
			int[] Cl=(int[]) H.remove(0);
			if (color[Cl[0]]==color[Cl[1]]) {
				hints++;
			}
		}
		color[size-1]=hints;
		return color;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "resource" })
	public static LinkedList<int[]> readAll(String sample) throws IOException {//renvoie un tableau de listes graph tel que:
		BufferedReader in = new BufferedReader(new FileReader(sample)); //graph[i] contient tous les sommets connectés au sommet i
		String line=in.readLine();//On saute les lignes inintéressantes 
		ArrayList<Integer> size=size(sample);
		LinkedList<int[]> output=new LinkedList<int[]>();
		while (line!=null) {
			int s=size.remove(0);
			LinkedList[] graph=new LinkedList[s];                    
			for (int j=0;j<s;j++) {                                   
				graph[j]=new LinkedList();                               
			}
			line=in.readLine();
			int p=line.length();
			int K=0;
			for (int i=2;i<p;i++) {
				K=K*10+Character.getNumericValue(line.charAt(i));
			}
			line=in.readLine();
			while (line!=null && line.charAt(0)!='G') {//Tant qu'on lit les lignes du même graphe
				LinkedList m=readL(line);//On lit la ligne
				if (m.get(0)=="single") {//Si elle contient les connections simples on travaille dessus
					m.remove(0);
					int node=(int) m.remove(0);//Noeud lié à tous les suivants dans m
					for (int k=0;k<m.size();k++) {
						graph[node].add(m.get(k));//On ajoute les voisins dans la liste correspondante du tableau
					}
				}
				else if (m.remove(0)=="double") {
					graph[s-1]=m;
				}
				line=in.readLine();//Ligne suivante
			}
			output.add(greedy(graph,K));
		}
		return output;
	}
	public static void print(String path,String sample) {
		try{
			File ff=new File(path); // définir l'arborescence
			ff.createNewFile();
			FileWriter ffw=new FileWriter(ff);
			LinkedList resultat=readAll(sample);
			int j=1;
			while (!resultat.isEmpty()) {
				int[] color=(int[]) resultat.remove(0);
				ffw.write("Graphe "+j+":\n"); 
				int n=color.length;
				for (int i=0;i<n-1;i++) {
					ffw.write(i+" -c> "+color[i]+"\n");
				}
				j++;
			}
			ffw.close(); // fermer le fichier à la fin des traitements
		} catch (Exception e) {}
	}
}
