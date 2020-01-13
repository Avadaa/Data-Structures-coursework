// HashTableen lisättävät alkiot
public class HashObj{
    String category; // ['AND', 'OR', 'XOR']
    int value; // Alkion arvo
    int firstEncounter; // Millä rivillä tavattu ensimmäisen kerran
    int occurrences; // Luvun esiintymismäärä yhteensä joukoissa A sekä B
    String set; // Kummassa joukossa luku oli

    public HashObj(String category, int value, int firstEncounter, int occurrences, String set){
        this.category = category;
        this.value = value;
        this.firstEncounter = firstEncounter;
        this.occurrences = occurrences;
        this.set = set;
    }
}