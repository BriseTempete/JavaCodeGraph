import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class colorGreedy {
	public static int [] greedyapproach(Node [] E, int k) {
		int n = E.length;
		int [] color = new int[n];
		int max = k;
		for (int j = 0; j <k; j ++) {  /* on colorie la clique */
			color[j] = j+1;
		}
		for (int i = k; i <n; i ++) {  				 /* On parcourt les noeuds du graphe*/
			int a = 0;  							/* C'est la couleur du noeud i */
			int [] colorused = new int[max+1];          /* colourused nous dis si l'un des voisins du noeud i a la couleur i sur lui*/
			for (int b : E[i].neighbors) {            /* On parcours les voisins */
				if (color[b] == a) {       			 /* Un voisin a la couleur qu'on veut donner à notre noeud */
					boolean value = true; 
					colorused[a] = 1; 				 /* On mets qu'on a vu la couleur du voisin b*/
					while (value){
						if (a>max) {
							value = false;			/* On donne une nouvelle couleur au noeud */
						}
						else {
							if (colorused[a]== 1) {
								a++;                /* La couleur suivante a déjà été vue*/
							}
							else {
								value = false;      /* On a trouvé une couleur non encore visité */
							}
						}
						
					}
					
				}
				colorused[color[b]] = 1;     /* On mets qu'on a vu la couleur du voisin b*/
			}
			if (a > max) {
				max = a;
			}
			color[E[i].number] = a;  /* On associe le numero et la couleur et pas la position dans le tableau des noeuds */
		}
		
		return color;
		
	}
	public static LinkedList<int[]> readAllgreedy(String sample) throws IOException {//renvoie un tableau de listes graph tel que:
		BufferedReader in = new BufferedReader(new FileReader(sample)); //graph[i] contient tous les sommets connectés au sommet i
		String line=in.readLine();//On saute les lignes inintéressantes 
		ArrayList<Integer> size=colorGreedy2.size(sample);
		LinkedList<int[]> output=new LinkedList();
		while (line!=null) {
			int s=size.remove(0);
			Node[] graph=new Node[s]; 
			for (int k=0;k<s;k++) {
				ArrayList<Integer> l=new ArrayList<Integer>();
				graph[k]=new Node(k,l);
			}
			line=in.readLine();
			int p=line.length();
			int K=0;
			for (int i=2;i<p;i++) {
				K=K*10+Character.getNumericValue(line.charAt(i));
				
			}
			line=in.readLine();
			while (line!=null && line.charAt(0)!='G') {//Tant qu'on lit les lignes du même graphe
				LinkedList m=colorGreedy2.readL(line);//On lit la ligne
				if (m.get(0)=="single") {//Si elle contient les connections simples on travaille dessus
					m.remove(0);
					int node=(int) m.remove(0);//Noeud lié à tous les suivants dans m			
					for (int k=0;k<m.size();k++) {
						graph[node].neighbors.add((Integer) m.get(k));//On ajoute les voisins dans la liste correspondante du tableau
					}
				}
				line=in.readLine();//Ligne suivante
			}
			output.add(greedyapproach(graph,K));
		}
		return output;
	}
	
	public static void printgreedy(String path,String sample) {
		try{
			File ff=new File(path); // définir l'arborescence
			ff.createNewFile();
			FileWriter ffw=new FileWriter(ff);
			LinkedList resultat=readAllgreedy(sample);
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
