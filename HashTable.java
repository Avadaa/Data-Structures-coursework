import java.util.ArrayList;

// Itse hajautustaulun toteutus (lineaarisesti hajautettu)
public class HashTable {

    int size; // Tauluun varastoitujen alkioiden määrä
    HashObj[] table; // Alkiot

	public HashTable(){
        size = 0;
    }

    // Lisää ArrayLista 'table' -taulukkoon
    public void addArr(ArrayList<HashObj> arr){

        // Nosta kokoa tarvittava määrä
        sizeUp(size + arr.size());
        
        // Aseta uudet alkiot tauluun
        for(int i = 0; i < arr.size(); i++)
            add(arr.get(i));
    }

    // Lisää alkio tauluun
    private void add(HashObj element){
        int index = getIndex(table, element.value);
        table[index % size] = element;
    }

    // Hae indeksi alkiolle
    private int getIndex(HashObj[] arr, int element){
        int index = element % size;

        while(arr[index % size] instanceof HashObj)
            index++;

        return index % size;
    }

    // Nosta hajautustaulun koko halutuksi
    public void sizeUp(int newSize){
        size = newSize;

        // Java ei tykkää, kun funktiota soveltaa tyhjään tauluun
        int tableLength;
        try{tableLength = table.length;}
        catch(Exception e){tableLength = 0;}

        HashObj[] newTable = new HashObj[size];

        // Siirrä vanhat alkiot
        for(int i = 0; i < tableLength; i++){
            int index = getIndex(newTable, table[i].value);
            newTable[index] = table[i];
        }

        table = newTable;
    }

    // Poista haluttu alkio
    public boolean remove(HashObj element){
        int index = element.value % size;

        int counter = 0;
        boolean found = false;

        // Yritä etsiä alkio
        // Vertaa kategoriaa sekä arvoa
        while(found == false && counter++ <= size){
            if(table[index % size].category.equals(element.category) && table[index % size].value == element.value){
                found = true;
                break;
            }
            index++;
        }

        // Jos alkio löytyy, korvaa se asettamalla kategoriaksi "removed"
        if(found == true)
            table[index % size] = new HashObj("removed", 0,-1,-1, " ");

        // Palautusarvoa käytetään informoimaan käyttäjälle poiston onnistumisen lopputuloksesta
        return found;
    }
}