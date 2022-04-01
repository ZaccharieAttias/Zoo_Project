package animals;
import diet.IDiet;
import food.EFoodType;
import food.IEdible;
import mobility.Mobile;
import mobility.Point;
import utilities.MessageUtility;


/**
 * Abstract class that defines the attributes of the animals
 *
 * @version 17.0.2
 * @author Attias Zaccharie, Amar Yuval
 * @see Mobile,IEdible
 */
public abstract class Animal extends Mobile implements IEdible  {
    private String name;
    private double weight;
    private IDiet diet;


    /**
     * Constructor of the object Animal : it sets the attributes of the object
     *
     * @param name A String that represent the name of the Animal
     * @param p A Point object that represent the actual location of the Animal
     */
    public Animal(String name, Point p){
        super(p);
        this.name = name;
    }


    /**
     * Moving the animal to a new location, calculate the weight he lost on the way and update his new current weight
     *
     * @param p Point object that indicate a location
     * @return the distance traveled by the animal
     */
    @Override
    public double move(Point p)
    {
        double d = super.move(p);
        setWeight(this.weight-(d*this.weight*0.00025));
        return d;
    }


    /**
     * Getter method for the attribute diet
     * @see IDiet
     * @return An object diet which represents the type of diet of the animal
     */
    public IDiet getDiet(){return diet;}




    public boolean eat(IEdible ed){
        if(diet.canEat(ed.getFoodType())){
            makeSound();
            return true;
        }
        return false;
    }


    /**
     * Setter method for the attribute weight
     * return True if the weight intake is in the valid range (greater than zero), else False
     *
     * @param w is a Double representing of the weight intake of the Animal
     * @return True if the setter succeed, else False
     */
    public  boolean setWeight( double w){
        if (w<=0)
            return false;
        this.weight += w;
        return true;

    }



    public boolean setdiet( IDiet d){
        this.diet = d;
        return true;
    }

    /**
     * Getter method for the attribute name
     *
     * @return The Animal's name
     */
    public  String getName(){return this.name;}


    /**
     * Getter method for the attribute weight
     *
     * @return The Animal's weight
     */
    public  double getWeight(){return this.weight;}


    /**
     * Getter method for Animal food type (all animals food type is MEAT, expect Lion which is NOTFOOD)
     * @see EFoodType
     * @return The Animal's food type
     */
    public  EFoodType getFoodType(){return EFoodType.MEAT;}



    public void setName(String name){
        this.name = name;
        // in main this need a void function but in her pdf it need bool
    }

    public abstract void makeSound();

}
