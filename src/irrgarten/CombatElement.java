
package irrgarten;

//Clase abstracta (no se puede instanciar)
public abstract class CombatElement{
    private float effect;
    private int uses;
    
    
    public CombatElement(float effect, int uses){
        this.effect=effect;
        this.uses=uses;
    }
    protected float produceEffect(){
        float salida=0.0f;
        if (this.uses>0){
            salida=this.effect;
            this.uses--; 
        }
        return salida;
    }
    public boolean discard(){
        return Dice.discardElement(this.uses);
    }
    @Override
    public String toString (){
        String effect = String.format("%.3f", this.effect);
        return "[e: " + effect + ", u: "+ this.uses+"]" ;
    }
    
    
}
