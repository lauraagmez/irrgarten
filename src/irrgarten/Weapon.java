
package irrgarten;


public class Weapon extends CombatElement{

    public Weapon (float power, int uses){
        //CombatElement(float effect, int uses)
        super(power, uses);
    }

    public float attack(){
       return produceEffect();
    }
    
    //Heredado
    @Override
    public String toString (){
        return "W " + super.toString() ;
    }
    
   
}
