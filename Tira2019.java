// Tietorakenteet 2019 harjoitustyö


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.Scanner;

// SISÄKKÄISISTÄ SILMUKOISTA JOHTUEN SUURTEN TIEDOSTOJEN (50 000+ RIVIÄ) PROSESSOINTI VOI VIEDÄ HETKEN AIKAA
// Ohjelma vaikuttaa jäävän jumiin, mutta suorittaa kyllä tehtävän jokusen minuutin kuluttua prosessorista riippuen
public class Tira2019{

	static Scanner scanner = new Scanner(System.in);
	static HashTable table = new HashTable();

	static private ArrayList readInput(String filename){
		
		String line;
		ArrayList arr = new ArrayList<Integer>();

		try {
			BufferedReader br = new BufferedReader( new FileReader(filename));

			// Lue rivit annetusta tiedostosta ja lisää ne ArrayListaan
			while((line = br.readLine()) != null)
				arr.add(Integer.parseInt(line));
			

		} 
		catch(IOException e){
			System.out.println("File not found.");;
		}
		return arr;
	}

	static private void writeOutput(ArrayList rows, String filename){

		try {
			System.out.println("Writing file " + filename + "...");
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename)); 		

			// Käy annetun ArrayListin stringit läpi ja lisää ne yksitellen tiedostoon 'filename.txt'
			for(Object row : rows)
				bw.write((String)row + "\n");
			bw.close();
			
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}


	// Löytää yhteiset tekijät ArrayListoista A ja B
	// Sijoittaa löydetyt kokonaisluvut ArrayListaan, ja syöttää sen 'addArr' - funktioon HashTablessa
	public static ArrayList and(ArrayList A, ArrayList B, String from){

		ArrayList and = new ArrayList<HashObj>();

		boolean contains = false;

		// Käy läpi B jokaista A:n alkiota vastaan
		for(int i = 0; i < A.size(); i++)
			for(int j = 0; j < B.size(); j++){

				contains = false;
					
				// Vertailut eivät tuntemattomasta syystä toimineet yhtäsuuruusmerkeillä, joten piti kierrättää stringin kautta
				if(((Integer)A.get(i)).toString().equals(((Integer)B.get(j)).toString())){

					// Tarkista, löytyykö kyseistä A:n arvoa 'and' -ArrayListasta
					for(int k = 0; k < and.size(); k++){
						
						// Sama homma vertailun kanssa
						if((((Integer)((HashObj)and.get(k)).value)).toString().equals(((Integer)A.get(i)).toString()))
							contains = true;
					}
						
					
					
					// Jos ei, lisää arvo HashObjektina
					if(contains == false)
						and.add(new HashObj("and", ((Integer)A.get(i)), i + 1, -1, " "));
					
					break;
				}
			}

		// Jos funktiota kutsuttiin luonnollisesti, lisää 'and' -ArrayLista 'table' -HashTableen
		// Älä lisää, jos kutsu tuli 'xor' -funktiosta
		if(from.equals("xor") == false)
			table.addArr(and);
		
		return and;
	}

	// Löytää kaikki luvut joukosta A sekä B
	// Syöttää löydetyt luvut funktioon, joka lisää ne 'table' -HashTableen
	public static ArrayList or(ArrayList A, ArrayList B, String from){

		ArrayList or = new ArrayList<HashObj>();

		for(int i = 0; i < 2; i++){
			// Valitse tarkasteltava joukko
			ArrayList arr = new ArrayList<Integer>();
			if(i == 0)
				arr = A;
			if(i == 1)
				arr = B;
			
			for(Object e : arr){
				boolean contains = false;

				// Käy läpi 'or' -ArrayLista ja katso löytyykö tarkasteltavan joukon alkio sieltä
				// Jos löytyy, lisää alkion esiintymismäärää yhdellä
				for(int j = 0; j < or.size(); j++)
					if(((HashObj)or.get(j)).value == ((Integer)e)){
						((HashObj)or.get(j)).occurrences++;
						contains = true;
					}

				// Jos ei, lisää se HashObjektina
				// Tällöin 'occurrences' asetetaan yhteen
				if(contains == false)
					or.add(new HashObj("or", ((Integer)e), -1, 1, Integer.toString(i + 1)));
			}
		}

		if(from.equals("xor") == false)
			table.addArr(or);

		return or;
	}

	// Etsii luvut (A tai B) - (A ja B)
	public static ArrayList xor(ArrayList A, ArrayList B){

		// Hakee tarkasteltavat joukot
		ArrayList andArr = and(A, B, "xor");
		ArrayList orArr = or(A, B, "xor");

		ArrayList xor = new ArrayList<HashObj>();

		// Käy läpi 'orArr' alkiot, ja tarkasta löytyykö kyseistä alkiota ArrayListasta 'andArr'
		for(Object e : orArr){

			boolean contains = false;


			for(Object obj : andArr)
				if(((HashObj)obj).value == ((HashObj)e).value)
					contains = true;

			// Jos ei löydy, lisää se
			if(contains == false)
				xor.add(new HashObj("xor", ((HashObj)e).value, -1, -1, ((HashObj)e).set));
		}

		// Lisää löydetyt alkiot 'table' -HashTableen
		table.addArr(xor);

		return xor;
	}

	// Muodostaa kysytyt tekstitiedostot
	private static void toFile(int longestNum, String category, String filename){

		ArrayList rows = new ArrayList<String>();
		String line;


		// Käy läpi kaikki 'table' -HashTablen alkiot
		for(HashObj e : table.table){
			line = "";

			// Jos kyseinen alkio on halutusta kategoriasta ['OR', 'AND', 'XOR']
			if(e.category.equals(category)){
				line += e.value;
				
				// Lisää rivejä tarvittava määrä, jotta sarakkeet ovat samalla tasolla lukujen pituudesta riippumatta
				for(int i = 0; i < longestNum - String.valueOf(e.value).length(); i++)
					line += " ";

				// Toisen sarakkeen tiedot
				switch(category){
					case "or":
						line += e.occurrences;
						break;
					case "and":
						line += e.firstEncounter;
						break;
					case "xor":
						line += e.set;
						break;
				}
				rows.add(line);
			}

		}

		// Kutsu tekstitiedostonmuodostajafunktiota
		writeOutput(rows, filename);
	}

	// Etsi pisin kokonaisluku joukosta A sekä B
	private static int longestInt(ArrayList A, ArrayList B){
		int longest = 0;

		for(Object e : A)
			longest = String.valueOf(e).length() > longest ? String.valueOf(e).length() : longest;

		for(Object e : B)
			longest = String.valueOf(e).length() > longest ? String.valueOf(e).length() : longest;

		// Lisää kaksi välilyöntiä, jotta pisin alkio ei olisi kiinni toisessa sarakkeessa
		return longest + 2;
	}


	
	// Juttelu käyttäjän kanssa
	// Ehkä vähän pitkä main-funktio, mutta eipä sillä väliä
	public static void main(String[] args){
		
		System.out.println("The hash table enlarges itself dynamically\n");

		ArrayList A = readInput("testA.txt");
		ArrayList B = readInput("testB.txt");

		System.out.println("Size of setA.txt: " + A.size());
		System.out.println("Size of setB.txt: " + B.size() + "\n");

		and(A, B, "");
		or(A, B, "");
		xor(A, B);


		System.out.println("Would you like to remove entries from the hash table? [Y/N]");

		String remove = scanner.nextLine().toUpperCase();
		while(!(remove.equals("Y") || remove.equals("N"))){
			System.out.println("Please answer 'Y' or 'N'");
			remove = scanner.nextLine().toUpperCase();
		}

		String set;
		int num = 0;
		boolean viableNum = false;
		boolean found;

		while(remove.equals("Y")){
			viableNum = false;

			System.out.println("From which set would you like to remove an item? [AND/OR/XOR]");
			set = scanner.nextLine().toUpperCase();

			while(!(set.equals("AND") || set.equals("OR") || set.equals("XOR"))){
				System.out.println("Please answer 'AND', 'OR' or 'XOR'");
				set = scanner.nextLine().toUpperCase();
			}

			System.out.println("Which value would you like to remove? [SOME INTEGER]");
			while(viableNum == false){
				try{
					num = Integer.parseInt(scanner.nextLine().toUpperCase());
					viableNum = true;
				} catch(Exception e){
					System.out.println("Please enter a valid integer");
				}
			}

			// Poista haluttu alkio ja varastoi lopputulos
			found = table.remove(new HashObj(set.toLowerCase(), num, -1, -1, " "));

			if(found == true)
				System.out.println("Removed " + num + " from " + set + "\n");
			else
				System.out.println("There is no " + num + " in " + set + "\n");


			System.out.println("Would you like to remove another entry? [Y/N]");

			remove = scanner.nextLine().toUpperCase();
			while(!(remove.equals("Y") || remove.equals("N"))){
				System.out.println("Please answer 'Y' or 'N'");
				remove = scanner.nextLine().toUpperCase();
			}
		}
		
		int longestNum = longestInt(A, B);
		toFile(longestNum, "or", "or.txt");
		toFile(longestNum, "and", "and.txt");
		toFile(longestNum, "xor", "xor.txt");
	}
}
