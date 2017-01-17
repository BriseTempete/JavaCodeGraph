import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.LineNumberReader;

public class valid_coloring {
	public valid_coloring() { 
	}			
	@SuppressWarnings({ "rawtypes", "unchecked" })
	
	/*Fonction de lecture d'une ligne, utilisée dans les autres classes*/
	public static LinkedList readL(String line) { // Renvoie une liste chaînée contenant en 1er élément
		LinkedList L=new LinkedList();            // single si on a une ligne de connection simple
		int n=line.length();                      // double si on a une ligne de connection double
		int i=0;                                  // liste vide si on a une ligne de texte ( dans ce cas print)
		int node=0;
		if (line.charAt(i)=='G' || line.charAt(i)=='K') {// Print si texte
			System.out.println(line);
			return L;
		}
		else { 
			while (i<n && line.charAt(i)!=' ' && line.charAt(i)!='<') {//Lecture du premier chiffre
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

	public static ArrayList<int[]> readColor(String samplecolor) throws IOException {// donne le tableau des couleurs 
		@SuppressWarnings("resource")                                                //de tous les graphes
		BufferedReader in = new BufferedReader(new FileReader(samplecolor));         //dans une arraylist
		ArrayList<int[]> output=new ArrayList<int[]>();
		String line;
		line=in.readLine();
		while (line!=null) {//Tant qu'on a pas atteint le bout du fichier texte
			line=in.readLine();
			int sizeMax=0;//taille du graphe
			int colMax=0;//coloration maximale
			List<Integer> node=new LinkedList<Integer>();
	        List<Integer> col=new LinkedList<Integer>();
			while (line!=null && line.charAt(0)!='G') {//On mémorise les couleurs 
				LinkedList L=readL(line);
				node.add((Integer) L.get(1));//Les nodes
				col.add((Integer) L.get(2));// Les couleurs
				sizeMax=(Integer) L.get(1);// La taille actuelle du tableau
				colMax=Math.max((Integer) L.get(2),colMax); //Le nombre de couleur maximal
				line=in.readLine();
				}
			int[] color=new int[sizeMax+2];
			for (int k=0;k<node.size();k++) {//On remplit le tableau
				color[node.get(k)]=col.get(k);
			}
			color[sizeMax+1]=colMax; //Stockage de la couleur maximale dans la dernière case du tableau
			output.add(color);
		}
		return output;
	}
	
    public static String checkSingle(int[] color,List edge) {//Vérifie si la ligne single associée à la
    	boolean b=true;                               // liste edge est correcte
    	int m=color.length-1;                           // Si incorrect donne la raison (missing,
    	int node=(int) edge.remove(0);                //not valid) 
    	while ( b && !(edge.isEmpty())) {// Tant que les couleurs sont bonnes
    		int c=(int) edge.remove(0);
    		//System.out.println(" node="+node+" coloré en "+color[node]+" , c="+c+" coloré en"+color[c]);
    		if (node<m && c<m) {// Si le numéro des nodes dépasse la taille de color, 
    			b=!(color[node]==color[c]);// il manque des couleurs,
    			}                          // sinon on checke simplement que les couleurs ne
    		else { return "missing";}      // sont pas les mêmes
    	}
    	if (b) {// Sortie de boucle: si tout va bien ok, sinon on le signale
    		return "ok";} 
    	else {
    		return "not valid";}
    }
    
    public static int checkDouble(int[] color,List edge) {//Renvoie le nombre de liaison double
    	int hints=0;                                      //vérifiée pour la ligne associée à edge
    	boolean full=true;//Vérifie si le tableau color n'est pas dépassé
    	int m=color.length-1;
    	while (full && !(edge.isEmpty())) {
    		int[] C=(int[]) edge.remove(0);
    		if (C[0]<m && C[1]<m) {
    			if (color[C[0]]==color[C[1]]) {
    				hints++;
    				}
    			}
    		else {
    			hints=-1;//Si dépassé on place hints à -1, convention -> missing
    			full=false;
    			}
    		}
    	return hints;
    }
    
    public static void valid(String samplecolor,String sample) throws IOException {//Fonction finale
    	int nbvalid=0;
    	int nbnovalid=0;
    	int nbmissing=0;
    	int nbhints=0;
    	int hintstot=0;
    	int nbColor=0;
    	BufferedReader in = new BufferedReader(new FileReader(sample));
    	String line=in.readLine();
    	ArrayList<int[]> color=readColor(samplecolor);
    	while (line!=null) {
    		int[] colorG=color.remove(0);//On considère les couleurs de tous les graphes
    		int colorUsed=colorG[colorG.length-1];
    		boolean b=true;
    		String s="valid";
    		int hints=0;
    		System.out.print(line+" ");//On imprime le nom du graphe
    		line=in.readLine();//Saut de ligne correspondant à K=k
    		line=in.readLine();//Saut de ligne pour arriver à la première ligne single
    		while (b && line!=null && line.charAt(0)!='G') {//Tant qu'on est encore dans le graphe
    			LinkedList L=readL(line);//On lit la ligne
    			if (L.get(0)=="single") {//On vérifie la nature de la ligne
    				L.remove(0);//On enlève le "single"
    				s=checkSingle(colorG,L);
    				if (s=="missing" || s=="not valid") {
    					b=false;
    					if (s=="missing") {
    						nbmissing++;
    					}
    					else {
    						nbnovalid++;
    					}
    				}
    			}
    			else if (L.get(0)=="double") {
    				L.remove(0);//On enlève le "double"
    				hintstot=hintstot+L.size();
    				hints=checkDouble(colorG,L);
    				if (hints<0) {//Check missing (convention hints=-1 -->missing)
    					s="missing";
    					nbmissing++;
    					b=false;
    					}
    				}
    			line=in.readLine();
    			}
    		
    		if (b) {//Si satisfied on print le nombre de liaisons doubles vérifiées
    			System.out.print(" valid ("+hints+" satisfied)"+" ("+colorUsed+" colors used)");
    			nbhints=nbhints+hints;
    			nbvalid++;
    			nbColor=nbColor+colorUsed;
    			}
    		else {//Sinon on print la raison de l'échec
    			System.out.print(s);
    			}
    		while(line!=null && line.charAt(0)!='G') {//On passe à la ligne correspondant
    			line=in.readLine();                   //au graphe suivant
    			}
    		System.out.println(" ");
    		}
    	float percent=100*nbhints/hintstot; // Calcul du pourcentage d'hints vérifiés
    	System.out.println("NO_VALID = "+nbvalid);
    	System.out.println("NO_NOT_VALID = "+nbnovalid);
    	System.out.println("NO_MISSING = "+nbmissing);
    	System.out.println("NO_HINTS_SATISFIED = "+nbhints);
    	System.out.println("%_HINTS_SATISFIED = "+percent);
    	System.out.println("NO_COLORS_USED = "+nbColor);
    	in.close();
    	}
	}
