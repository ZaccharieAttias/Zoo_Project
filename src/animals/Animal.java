package animals;

import DesignPatterns.AnimalColor;
import diet.IDiet;
import food.EFoodType;
import food.IEdible;
import graphics.IAnimalBehavior;
import graphics.IDrawable;
import graphics.ZooPanel;
import mobility.Mobile;
import mobility.Point;
import utilities.MessageUtility;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Abstract class that defines the attributes of the animals
 *
 * @version 17.0.2
 * @author Attias Zaccharie, Amar Yuval
 * @see Mobile,IEdible
 */
public abstract class Animal extends Mobile implements IEdible, IDrawable, IAnimalBehavior, AnimalColor, Cloneable, Runnable {
    private String name;
    private double weight;
    private IDiet diet;
    private final int EAT_DISTANCE = 10;
    private int size;
    private String col;
    private int horSpeed;
    private int verSpeed;
    private boolean coordChanged;
    //protected Thread thread;
    protected boolean threadSuspended;
    private int x_dir;
    private int y_dir;
    private int eatCount;
    private ZooPanel pan;
    private BufferedImage img1, img2;
    protected Point location;
    private boolean isalive;


    /**
     * Constructor of the object Animal : it sets the attributes of the object
     *
     * @param name A String that represent the name of the Animal
     * @param p A Point object that represent the actual location of the Animal
     * @param animalSize A String that represent the animal's size
     * @param horizontalspeed An Integer that represent the animal's horizontal speed
     * @param verticalspeed An Integer that represent the animal's vertical speed
     * @param weight A Double that represent the animal's weight
     * @param animalcolor A String that represent the animal's color
     * @param pan is a ZooPanel representing the animal GUI Panel
     */
    public Animal(String name, Point p, int animalSize, int horizontalspeed, int verticalspeed, double weight, String animalcolor,ZooPanel pan) {
        super(p);
        //this.thread = new Thread(this);
        this.threadSuspended = false;
        this.name = name;
        this.size = animalSize;
        this.horSpeed = horizontalspeed;
        this.verSpeed = verticalspeed;
        this.col = animalcolor;
        this.weight = weight;
        this.coordChanged = true;
        this.x_dir = 1;
        this.y_dir = 1;
        this.eatCount=0;
        this.pan =pan;
        this.location = new Point(p);
        this.isalive = false;
        this.addObserver(pan.getController());
    }

    @Override
    public void PaintAnimal(String color){
        this.setColor(color);
        switch (this.getAnimalName()) {
            case "Bear" -> this.loadImages("bea_" + color.toLowerCase().charAt(0));
            case "Elephant" -> this.loadImages("elf_" + color.toLowerCase().charAt(0));
            case "Giraffe" -> this.loadImages("grf_" + color.toLowerCase().charAt(0));
            case "Lion" -> this.loadImages("lio_" + color.toLowerCase().charAt(0));
            case "Turtle" -> this.loadImages("trt_" + color.toLowerCase().charAt(0));
        }
    }

    /**
     * Clone method
     */
    public Object clone()throws CloneNotSupportedException{
        Animal copy = (Animal)super.clone();
        copy.location = new Point(location);
        copy.setLocation(new Point(copy.location));
        copy.threadSuspended = threadSuspended;
        copy.name = name;
        copy.size = size;
        copy.horSpeed =horSpeed;
        copy.verSpeed = verSpeed;
        copy.col = col;
        copy.weight = weight;
        copy.coordChanged = coordChanged;
        copy.x_dir = x_dir;
        copy.y_dir = y_dir;
        copy.eatCount = eatCount;
        copy.pan = pan;
        copy.isalive = isalive;
        copy.addObserver(pan.getController());
        return copy;
    }

    /**
     * Change the color of the animal
     *
     * @param color is a String representing the color of the Animal
     */
    public void setColor (String color) {this.col = color; }


    public void interrupt()
    {
        this.isalive = false;
        notifyObservers();
    }

    /**
     * Constructor of the object Animal : it sets the attributes of the object
     *
     * @param name A String that represent the name of the Animal
     * @param p A Point object that represent the actual location of the Animal
     */
    public Animal(String name, Point p){
        super(p);
        MessageUtility.logConstractor("Animal", name);
        setName(name);
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
        if(d!=0) {
            double temp = getWeight();
            setWeight(temp-(d*temp*0.00025));
            setChanges(true);
        }
        MessageUtility.logBooleanFunction(this.name,"move",p, d != 0);
        return d;

    }


    /**
     * Getter method for the attribute diet
     * @see IDiet
     * @return An object diet which represents the type of diet of the animal
     */
    public IDiet getDiet(){
        MessageUtility.logGetter(this.name, "getDiet", this.diet);
        return this.diet;
    }


    /**
     * Getter method for the attribute col
     *
     * @return The animal color
     */
    public String getColor(){return this.col; }

    /**
     * Getter method for the attribute name
     *
     * @return The animal's name
     */
    public String getAnimalName() { return this.name; }

    /**
     * Getter method for the attribute size
     *
     * @return The animal's size
     */
    public int getSize() { return this.size; }

    /**
     * Getter method for the attribute eatcount
     *
     * @return The animal's eat counter
     */
    public int getEatCount() {return this.eatCount; }

    /**
     * Increasing eat counter field eatcount of the animal by one
     */
    public void eatInc() { this.eatCount++; }

    /**
     * Getter method for the attribute coordChanged
     * @return If the coordinate of the animal changed
     */
    public boolean getChanges () {return this.coordChanged; }

    /**
     * Change the status of coordChanged of the animal
     *
     * @param state is a Boolean representing the status coordChanged of the Animal
     */
    public void setChanges (boolean state) {this.coordChanged = state; }

    /**
     * Getter method for the attribute horSpeed
     * @return The animal's horizontal speed
     */
    public int getHorSpeed() { return this.horSpeed; }

    /**
     * Getter method for the attribute verSpeed
     * @return The animal's vertical speed
     */
    public int getVerSpeed() { return this.verSpeed; }

    /**
     * Getter method for the attributes of the animal
     * @return The animal's attributes
     */
    public String getanimal(){return this.name + ", "+this.size+ ", "+this.col+ ", x="+this.getLocation().getx()+ ", y="+getLocation().gety() ;}

    /**
     * Getter method for the attributes EAT_DISTANCE
     * @return The animal's EAT_DISTANCE
     */
    public int geteatdistance(){return EAT_DISTANCE;}


    /**
     * Calls the eat method (from interface IDiet) with the food given.
     * It returns the weight gained form the action. The eating was successful if the weight is greater than zero.
     * If the eating was successful, then the weight of the animal is update and calls the function makeSound, else nothing.
     *
     * @see IEdible,IDiet
     * @param food is a String representing the food type (an object of type EFoodType)
     * @return True if the eating was successful, else False
     */
    public boolean eat(IEdible food){
        double gainWeight = diet.eat(this, food);
        if(gainWeight>0){
            setWeight((this.weight+gainWeight));
            makeSound();
            MessageUtility.logBooleanFunction(this.name,"eat", food,true);
            return true;
        }
        MessageUtility.logBooleanFunction(this.name,"eat", food,false);
        return false;
    }


    /**
     * Setter method for the attribute weight
     * return True if the weight intake is in the valid range (greater than zero), else False
     *
     * @param weight is a Double representing of the weight intake of the Animal
     * @return True if the setter succeed, else False
     */
    public  boolean setWeight( double weight){
        if (weight<=0){
            MessageUtility.logSetter(this.name,"setWeight",weight,false);
            return false;
        }
        this.weight = weight;
        MessageUtility.logSetter(this.name,"setWeight",weight,true);
        return true;

    }


    /**
     * Setter method for the attribute diet
     * return True if the diet is in the valid options, else False
     * Note : diet can only be Omnivore, Herbivore or Carnivore
     * @param diet is an IDiet representing the diet of the animal
     * @return True if the setter succeed, else False
     */
    public boolean setDiet(IDiet diet){
        this.diet = diet;
        MessageUtility.logSetter(this.name,"setDiet",diet,true);
        return true;
    }


    /**
     * Getter method for the attribute name
     *
     * @return The Animal's name
     */
    public  String getName(){
        MessageUtility.logGetter(this.name, "getName", this.name);
        return this.name;
    }


    /**
     * Getter method for the attribute weight
     *
     * @return The Animal's weight
     */
    public  double getWeight(){
        MessageUtility.logGetter(this.name, "getWeight", this.weight);
        return this.weight;
    }


    /**
     * Getter method for Animal food type (all animals food type is MEAT, expect Lion which is NOTFOOD)
     * @see EFoodType
     * @return The Animal's food type
     */
    @Override
    public  EFoodType getFoodType(){
        MessageUtility.logGetter(this.name, "getFoodType", EFoodType.MEAT);
        return EFoodType.MEAT;
    }

    public  ZooPanel getpanel(){
        return pan;
    }

    /**
     * Setter method for the attribute name
     * @param name is a String representing the name of the Animal
     * @return True if the setter succeed, else False
     */
    public boolean setName(String name){
        this.name = name;
        MessageUtility.logSetter(this.name,"setName",name,true);
        return true;
    }


    /**
     * load image method for the attribute img1
     * Note : catch error from IOException if cannot load the image
     *
     * @param nm is a String representing the name animal type
     */

    public void loadImages(String nm) {
        try { img1 = ImageIO.read(new File(PICTURE_PATH + nm+ "_1.png"));
            img2 = ImageIO.read(new File(PICTURE_PATH + nm+ "_2.png"));}
        catch (IOException e) { System.out.println("Cannot load image");
            System.out.println(e.toString());}
    }

    /**
     * draw image method for the attribute img1
     * Note : draw the image in first time as default location , paint different image in case of different side
     *
     * @param g is a Graphics object that return from repaint to paintComponent
     */

    public void drawObject (Graphics g) {
        if(x_dir==1)//right side
            g.drawImage(img1, getLocation().getx()-size/2, getLocation().gety()-size/10, size/2, size, pan);
        else //left side
            g.drawImage(img2, getLocation().getx(), getLocation().gety()-size/10, size/2, size, pan);
    }
    

    /**
     * Representation of the object as a string
     *
     * @return a String of the object data in the requested format
     */
    @Override
    public String toString() {return "[" + this.getClass().getSimpleName() + "] "+this.name;}


    public abstract void makeSound();

    /**
     * Setter for the attribute threadSuspended
     */
    @Override
    public synchronized void setSuspended(){
        if (!this.threadSuspended)
            this.threadSuspended = true;
    }

    /**
     * Setter for the attribute threadSuspended
     */
    @Override
    public synchronized void setResumed(){
        if (this.threadSuspended){
            this.threadSuspended = false;
            notify();
        }
    }


    /**
     * Getter method for the attribute threadSuspended
     *
     * @return threadSuspended
     */
    public  boolean getThreadSuspended(){return this.threadSuspended;}

    public  boolean getisalive(){return this.isalive;}


    /**
     * Run method
     * @see Thread
     */
    @Override
    public void run() {
        this.isalive = true;
        while (isalive) {
            if (this.threadSuspended) {
                synchronized(this) {
                    try {
                        wait();
                    } catch (InterruptedException a) {
                        System.out.println(getName() + "  is sleeping");
                    }
                }
            }
            else if ((this.pan.getmeat()!=null && this.getDiet().canEat(EFoodType.MEAT))||this.pan.getplant()!=null && this.getDiet().canEat(EFoodType.VEGETABLE))
            {
                double oldSpead = Math.sqrt(horSpeed*horSpeed+verSpeed*verSpeed);
                double newHorSpeed = oldSpead*(location.getx() - pan.getWidth()/2)/(Math.sqrt(Math.pow(location.getx() - pan.getWidth()/2,2)+ Math.pow(location.gety() - pan.getHeight()/2,2)));
                double newVerSpeed = oldSpead*(location.gety() - pan.getHeight()/2)/ (Math.sqrt(Math.pow(location.getx() - pan.getWidth()/2,2)+ Math.pow(location.gety() - pan.getHeight()/2,2)));
                int v = 1;
                if(newVerSpeed<0)
                {
                    v=-1;
                    newVerSpeed = -newVerSpeed;
                }
                if(newVerSpeed > 10)
                    newVerSpeed = 10;
                else if(newVerSpeed < 1) {
                    if(location.gety() != pan.getHeight()/2)
                        newVerSpeed = 1;
                    else
                        newVerSpeed = 0;
                }
                int h = 1;
                if(newHorSpeed<0) { h=-1; newHorSpeed = -newHorSpeed; }
                if(newHorSpeed > 10)
                    newHorSpeed = 10;
                else if(newHorSpeed < 1) {
                    if(location.getx() != pan.getWidth()/2)
                        newHorSpeed = 1;
                    else
                        newHorSpeed = 0;
                }
                location.setpoint((int)(location.getx() - newHorSpeed*h), (int)(location.gety() - newVerSpeed*v));
                if(location.getx()<pan.getWidth()/2)
                    x_dir = 1;
                else
                    x_dir = -1;
                this.move(location);
            }
            else
            {
                if (location.getx() + horSpeed*x_dir>=800)
                    x_dir=-1;
                if (location.getx() + horSpeed*x_dir<=0)
                    x_dir=1;
                if (location.gety() + verSpeed*y_dir>=600-getSize())
                    y_dir=-1;
                if (location.gety() + verSpeed*y_dir<=0)
                    y_dir=1;
                location.setpoint(location.getx() + horSpeed*x_dir,location.gety() + verSpeed*y_dir);
                this.move(location);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println(getName()+ "  is dead...");
                return;
            }
            setChanged();
        }
    }
}
