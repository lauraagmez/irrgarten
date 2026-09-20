package irrgarten;
import java.util.Random;
import java.util.ArrayList;

public class Dice {
    private static final int  MAX_USES=5;
    private static final float MAX_INTELLIGENCE=10.0f;
    private static final float MAX_STRENGTH=10.0f;
    private static final float RESURRECT_PROB=0.3f;
    private static final int WEAPONS_REWARD=2;
    private static final int SHIELDS_REWARD=3;
    private static final int HEALTH_REWARD=5;
    private static final float MAX_ATTACK=3.0f;
    private static final float MAX_SHIELD=2f;
    private static Random generator = new Random();
    
    
    public static int randomPos(int max){
        return generator.nextInt(max);//Entre 0 y max-1, añadimos max con +1
    }
    public static int whoStarts(int nplayers){
        return generator.nextInt(nplayers);
    }
    public static float randomIntelligence (){
        return generator.nextFloat(MAX_INTELLIGENCE);
    }
    public static float randomStrength(){
        return generator.nextFloat(MAX_STRENGTH);
    }
    public static boolean resurrectPlayer(){
        return generator.nextFloat()<=RESURRECT_PROB;
    }
    public static int weaponsReward(){
        return generator.nextInt(WEAPONS_REWARD+1);  
    }
    public static int shieldsReward(){
        return generator.nextInt(SHIELDS_REWARD+1);  
    }
    public static int healthReward(){
        return generator.nextInt(HEALTH_REWARD+1);
    }
    public static float weaponPower(){
        return generator.nextFloat(MAX_ATTACK);
    }
    public static float shieldPower(){
        return generator.nextFloat(MAX_SHIELD);
    }
    public static int usesLeft(){
        return generator.nextInt(MAX_USES+1);
    }
    public static float intensity(float competence){
        return generator.nextFloat(competence);
    }
    public static boolean discardElement(int usesLeft){
        if (usesLeft==MAX_USES) return false;
        if (usesLeft==0) return true;
        //PROBAR CON USES=0 MUY PROBABLE QUE SALGA TRUE
        return generator.nextFloat()<(1-(usesLeft*1.0/MAX_USES));
    }
    //Practica 4
    /**
     * En función de la inteligencia dada, devolverá ,con más probabilidad si intelligence es mayor,
     * preference y ,con menos, una dirección aleatoria de validMoves
     * @param preference Dirección preferida hacia la que moverse
     * @param validMoves Direcciones posibles para moverse
     * @param intelligence Inteligencia del jugador
     * @return Dirección elegida
     */
    public static Directions nextStep (Directions preference, ArrayList <Directions> validMoves, float intelligence){
        Directions salida=preference;
        
        if(Dice.randomIntelligence()>intelligence){
            if (!validMoves.isEmpty()) {
                int i=generator.nextInt(validMoves.size());
                salida=validMoves.get(i); 
            }          
        }
        return salida;
    }
   
        
    
}
