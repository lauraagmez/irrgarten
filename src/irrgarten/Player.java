
package irrgarten;
import java.util.ArrayList;
import java.util.Iterator;

public class Player extends LabyrinthCharacter {
    private static final int MAX_WEAPONS=2;
    private static final int MAX_SHIELDS=3;
    private static final int INITIAL_HEALTH=10;
    private static final int HITS2LOSE=3;
    private char number;
    private int consecutiveHits=0;
    private ShieldCardDeck shieldCardDeck;
    private WeaponCardDeck weaponCardDeck;
   
    private ArrayList <Shield> shields;
    private ArrayList <Weapon> weapons;
    
    public Player (char number, float intelligence, float strength){
        super("Player "+number, intelligence, strength, INITIAL_HEALTH);
        this.number=number;
        this.consecutiveHits=0;

        // Hay que inicializar los ArrayList
        this.weapons= new ArrayList<>();
        this.shields= new ArrayList<>();
        this.shieldCardDeck= new ShieldCardDeck();
        this.weaponCardDeck= new WeaponCardDeck();

    }
    public Player (Player other){
        super(other); // Se usa el constructor de copia de LabyrinthCharacter
        this.number=other.number;
        this.consecutiveHits=other.consecutiveHits;
        
        // Hay que inicializar los ArrayList
        this.weapons= new ArrayList<>(other.weapons);
        this.shields= new ArrayList<>(other.shields);
        this.shieldCardDeck= new ShieldCardDeck();
        this.weaponCardDeck= new WeaponCardDeck();
        
        // Hay que inicializar las barajas de cartas
       
    }
    public void resurrect(){
        this.setHealth(INITIAL_HEALTH);
        this.resetHits();
        this.weapons.clear();
        this.shields.clear();
    }
    
    public char getNumber(){
        return number;
    }
    
    /*Determina la dirección de movimiento final: Si la dirección preferida es 
    inválida y otros movimientos posibles, elige el primero. 
    En cualquier otro caso, usa la dirección preferida.*/
    public Directions move(Directions direction, ArrayList <Directions> validMoves){
        int size=validMoves.size();
        boolean contained=validMoves.contains(direction);
        Directions salida;
        if (size>0 && !contained){
            salida= validMoves.get(0);
        }else{
            salida= direction;
        }
        return salida;
    }
    
    /*Procesa todas las recompensas ganadas por el jugador: armas, escudos y salud.*/
    public void receiveReward(){
        int wReward=Dice.weaponsReward();
        int sReward=Dice.shieldsReward();
        for (int i=0; i<wReward; i++){
            Weapon wnew= newWeapon();
            receiveWeapon(wnew);
        }
        for (int i=0; i<sReward; i++){
            Shield snew= newShield();
            receiveShield(snew);
        }
        int extraHealth=Dice.healthReward();
        this.setHealth(this.getHealth()+extraHealth);
     
    }
    @Override
    public String toString(){
        String salida=super.toString();
        salida +=" [ch: "+this.consecutiveHits+"]";
        
        String w="";
        for (int i = 0; i < this.weapons.size(); i++) {
            w += this.weapons.get(i).toString();
            if (i < this.weapons.size() - 1) {
                w += ", ";
            }
        }
        String s="";
        for (int i = 0; i < this.shields.size(); i++) {
            s += this.shields.get(i).toString(); 
            if (i < this.shields.size() - 1) {
                s += ", ";
            }
        }
        salida += "\nWeapons: " + w + "\n" + "Shields: " + s + "\n";
        return salida;
        
    }
    
    private void receiveWeapon(Weapon w){
        Iterator <Weapon> it=weapons.iterator();
        Weapon wi;
        while(it.hasNext()){
            wi=it.next();
            if(wi.discard()){
                it.remove();
            }
        }

        if (weapons.size() < MAX_WEAPONS){
            weapons.add(w);
        }
    }
    private void receiveShield(Shield s){
        Iterator <Shield> it=shields.iterator();
        Shield si;
        while(it.hasNext()){
            si=it.next();
            if(si.discard()){
                it.remove();
            }
        }
        
        int size=shields.size();
        if (size < MAX_SHIELDS){
            shields.add(s);
        }
    }
    private Weapon newWeapon(){ 
        return this.weaponCardDeck.nextCard();
    } 
    private Shield newShield(){
        return this.shieldCardDeck.nextCard();

    }
    protected float sumWeapons(){
        float salida=0.0f;
        for (int i=0; i<this.weapons.size(); ++i){
            salida+=this.weapons.get(i).attack();
        }
        return salida;
    }
    protected float sumShields(){
        float salida=0.0f;
        for (int i=0; i<this.shields.size(); ++i){
            salida+=this.shields.get(i).protect();
        }
        return salida;
    }
    protected float defensiveEnergy(){
        return (this.getIntelligence() + sumShields());
    }
    private boolean manageHit(float receivedAttack){
        boolean lose;
        float defense = defensiveEnergy();
        
        if (defense<receivedAttack){
            gotWounded(); //Es atacado
            incConsecutiveHits();
        }else{
            resetHits();
        }
        if ((consecutiveHits == HITS2LOSE)|| dead()){
            resetHits();
            lose=true;
        }else{
            lose=false;
        }
        return lose;
    }
    private void resetHits(){
        consecutiveHits=0;
    }
    
    private void incConsecutiveHits(){
        consecutiveHits++;
    }
   
    @Override
    public float attack(){
        return this.getStrength()+this.sumWeapons();
    }
    @Override
    public boolean defend(float receivedAttack){
        return manageHit(receivedAttack);
    }
}
