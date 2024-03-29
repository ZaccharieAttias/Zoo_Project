package graphics;

import DesignPatterns.ChangeColorDialog;
import DesignPatterns.ThreadPool;
import DesignPatterns.ZooController;
import DesignPatterns.ZooMemento;
import animals.Animal;
import plants.Cabbage;
import plants.Lettuce;
import plants.Plant;
import privateutil.Meat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;



/**
 * A class representing the GUI main panel
 * Note : It inherits from JPanel and implements from ActionListener
 *
 * @version 17.0.2
 * @author Attias Zaccharie, Amar Yuval
 * @see JPanel
 * @see ActionListener
 */
public class ZooPanel extends JPanel implements  ActionListener {
    private static ZooPanel instance = null;
    private JPanel actionPanel;
    private ArrayList<Animal> Animallist;
    private ZooFrame f;
    private Plant plant = null;
    private Meat meat = null;
    private ZooController controller;
    private ThreadPool threadpool;
    private ArrayList<ZooMemento> memento;
    private int background;
    /**
     * The constructor of the ZooPanel object: it sets the attributes of the object
     * Note : ZooPanel contain two panels, one for control buttons, and the other for the visual board
     */
    private ZooPanel(ZooFrame frame){
        this.f = frame;
        this.background = 0;
        actionPanel = new JPanel(new FlowLayout());
        JButton addanimal = new JButton("Add Animal");
        JButton sleep = new JButton("Sleep");
        JButton wakeup = new JButton("Wake up");
        JButton changecolor = new JButton("Change Color");
        JButton clear = new JButton("Clear");
        JButton food = new JButton("Food");
        JButton info = new JButton("Info");
        JButton save = new JButton("Save");
        JButton restore = new JButton("Restore");
        JButton exit = new JButton("Exit");

        Animallist = new ArrayList<Animal>();
        memento = new ArrayList<ZooMemento>();

        addanimal.addActionListener(this);
        sleep.addActionListener(this);
        wakeup.addActionListener(this);
        changecolor.addActionListener(this);
        clear.addActionListener(this);
        food.addActionListener(this);
        info.addActionListener(this);
        save.addActionListener(this);
        restore.addActionListener(this);
        exit.addActionListener(this);

        actionPanel.add(addanimal);
        actionPanel.add(sleep);
        actionPanel.add(wakeup);
        actionPanel.add(changecolor);
        actionPanel.add(clear);
        actionPanel.add(food);
        actionPanel.add(info);
        actionPanel.add(save);
        actionPanel.add(restore);
        actionPanel.add(exit);

        actionPanel.setBackground(Color.BLUE);
        this.setLayout(new BorderLayout());
        this.add(actionPanel,BorderLayout.PAGE_END);
        this.threadpool = new ThreadPool();
        this.controller = new ZooController();
        this.controller.start();
    }

    /**
     * Invoked when an action occurs
     * @param e the event to be processed
     */
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand()){
            case "Exit":
                System.exit(0);
                threadpool.end();
                break;
            case "Add Animal":
                if(Animallist.size()==15)
                    JOptionPane.showMessageDialog(this, "You cannot add more than 15 animals.", "Message", JOptionPane.WARNING_MESSAGE);
                else{
                    synchronized (this.Animallist) {
                        new AddAnimalDialog(this, Animallist);
                        if(Animallist.size()>10)
                            JOptionPane.showMessageDialog(this, "The new animal enter in the queue", "Message", JOptionPane.WARNING_MESSAGE);
                    }
                }
                break;

            case "Change Color":
                if(Animallist.size()==0)
                    JOptionPane.showMessageDialog(this, "There are no animals in the zoo.", "Message", JOptionPane.WARNING_MESSAGE);
                else{
                    synchronized (this.Animallist) {
                        new ChangeColorDialog(this, Animallist);
                    }
                }
                break;

            case "Sleep":
                if(Animallist.size()==0)
                    JOptionPane.showMessageDialog(this, "There are no animals in the zoo.", "Message", JOptionPane.WARNING_MESSAGE);
                else {
                    synchronized (this.Animallist) {
                        for (int i = 0; i < Animallist.size(); i++)
                            Animallist.get(i).setSuspended();
                    }
                }
                break;

            case "Wake up":
                if(Animallist.size()==0)
                    JOptionPane.showMessageDialog(this, "There are no animals in the zoo.", "Message", JOptionPane.WARNING_MESSAGE);
                else {
                    synchronized (this.Animallist) {
                        for (int i = 0; i < Animallist.size(); i++)
                            Animallist.get(i).setResumed();
                    }
                }
                break;
            case "Food": Object[] options = {"Lettuce", "Cabbage", "Meat"};
                int foodchoice = JOptionPane.showOptionDialog(f, "Please choose food:", "Food for animal", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
                if(foodchoice == 0)
                    this.plant = Lettuce.getInstance(this);
                else if(foodchoice == 1)
                    this.plant = Cabbage.getInstance(this);
                else if(foodchoice ==2)
                    this.meat =Meat.getInstance(this);
                repaint();
                break;

            case "Info": JFrame infoframe = new JFrame("Info");
                Info model = new Info(Animallist);
                JTable table = new JTable(model);
                table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                table.setPreferredScrollableViewportSize(new Dimension(500, 100));
                table.setFillsViewportHeight(true);
                infoframe.add(new JScrollPane(table),BorderLayout.CENTER);

                infoframe.setLocation((this.f.getSize().width/2)-250,-50+this.f.getSize().height/2);
                table.getColumnModel().getColumn(0).setHeaderValue("Animal");
                table.getColumnModel().getColumn(1).setHeaderValue("Color");
                table.getColumnModel().getColumn(2).setHeaderValue("Weight");
                table.getColumnModel().getColumn(3).setHeaderValue("Hor. Speed");
                table.getColumnModel().getColumn(4).setHeaderValue("Ver. Speed");
                table.getColumnModel().getColumn(5).setHeaderValue("Eat Counter");

                int total =0;
                for(Animal an : Animallist)
                    total+= an.getEatCount();
                JTable footer = new JTable(1, 6);
                footer.setValueAt("Total",0,0);
                footer.setValueAt(total,0,5);
                infoframe.add(footer,BorderLayout.SOUTH);

                infoframe.pack();
                infoframe.setVisible(true);
                break;

            case "Clear":
                synchronized (this.Animallist) {
                    for (int i = 0; i < Animallist.size(); i++)
                        Animallist.get(i).interrupt();
                    this.Animallist.clear();
                }
                this.plant = null;
                this.meat = null;
                threadpool.restart();
                repaint();
                break;
            case "Save":
                saveSate();
                JOptionPane.showMessageDialog(this,"The state has been saved");
                break;
            case "Restore":
                if(memento.size() == 0)
                    JOptionPane.showMessageDialog(this,"You haven't saved states");
                else{
                    restoreState();
                JOptionPane.showMessageDialog(this,"The state has been restored");}
                break;
        }
    }

    public static ZooPanel getInstance(ZooFrame frame){
        if(instance == null)
            instance = new ZooPanel(frame);
        return instance;
    }

    /**
     * The controller of the program, follow after all the user actions and update the program accordingly
     * Check if one animal can eat another one
     * Check if one animal can eat a plant or the meat
     */
    public void manageZoo() {
        repaint();
        if (plant != null) {
            synchronized (this.plant) {
                for (int i=0;i<Animallist.size();i++) {
                    Animal animal= Animallist.get(i);
                    if (animal.calcDistance(plant.getLocation()) <= animal.geteatdistance() && (animal.getDiet().canEat(plant.getFoodType()))) {
                        animal.eat(plant);
                        animal.eatInc();
                        plant = null;
                        repaint();
                        return;
                    }
                }
            }
        }
        if (meat != null) {
            synchronized (this.meat) {
                for (int i=0;i<Animallist.size();i++) {
                    Animal animal= Animallist.get(i);
                    if (animal.calcDistance(meat.getLocation()) <= animal.geteatdistance() && (animal.getDiet().canEat(meat.getFoodType()))) {
                        animal.eat(meat);
                        animal.eatInc();
                        meat = null;
                        repaint();
                        return;
                    }
                }
            }
        }
        synchronized (this.Animallist) {
            for (int i = 0; i < Animallist.size(); i++) {
                Animal animalpreda = Animallist.get(i);
                if(!animalpreda.getisalive())
                    continue;
                for (int j = 0; j < Animallist.size(); j++) {
                    Animal animalpreay = Animallist.get(j);
                    if(!animalpreay.getisalive())
                        continue;
                    if (animalpreda.equals(animalpreay))
                        continue;
                    if ((animalpreda.getDiet().canEat(animalpreay.getFoodType())) && (animalpreda.getWeight() > animalpreay.getWeight() * 2) && (animalpreda.calcDistance(animalpreay.getLocation()) < animalpreay.getSize())&&!animalpreda.getThreadSuspended()) {
                        animalpreda.eat(animalpreay);
                        animalpreda.eatInc();
                        animalpreay.interrupt();
                        Animallist.remove(Animallist.indexOf(animalpreay));
                        repaint();
                        return;
                    }
                }
            }
        }
    }


    /**
     * Check if anything has changed in our program
     * @return True if something have changed, else False
     */
    public boolean isChange(){
        for (Animal animal : Animallist)
            if(animal.getChanges())
            {
                animal.setChanges(false);
                return true;
            }
        return false;
    }


    /**
     * Printing all the components in our frame
     * @param g the graphics context
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(plant!= null)
            plant.drawObject(g);
        if(meat!=null)
            meat.drawObject(g);
        if(Animallist.size()!=0)
            for(int i = 0; i < Animallist.size(); i++)
                if(Animallist.get(i).getisalive())
                    Animallist.get(i).drawObject(g);
    }

    /**
     * Getter method for the attribute f
     * @return The frame where the panel is located
     */
    public ZooFrame getF(){return this.f;}


    /**
     * Getter method for the attribute meat
     * @return meat
     */
    public Meat getmeat(){return this.meat;}

    /**
     * Getter method for the attribute plant
     * @return plant
     */
    public Plant getplant(){return this.plant;}

    public ThreadPool getThreadpool(){return threadpool;}

    public ZooController getController(){return controller;}

    public void saveSate(){
        ZooMemento state;
        try {
            state = new ZooMemento(Animallist, meat, plant, background);
            if(memento.size() == 3)
                memento.remove(0);
            memento.add(state);
        }
        catch (CloneNotSupportedException e) {e.printStackTrace();}
    }

    public void restoreState(){
        synchronized (this.Animallist) {
            for (int i = 0; i < Animallist.size(); i++)
                Animallist.get(i).interrupt();
            this.Animallist.clear();
        }
        this.plant = null;
        this.meat = null;
        repaint();

        background = memento.get(memento.size() - 1).getbackground();
        switch (background) {
            case 0 -> this.f.setnone();
            case 1 -> this.f.setgreen();
            case 2 -> this.f.setimage();
        }
        threadpool.restart();
        ArrayList<Animal> restoredAnimallist = memento.get(memento.size()-1).getAnimalList();
        for (int i = 0; i < restoredAnimallist.size(); i++)
            Animallist.add(restoredAnimallist.get(i));
        for (int i = 0; i < Animallist.size(); i++){
            threadpool.addtopoll(Animallist.get(i));
            Animallist.get(i).setLocation(Animallist.get(i).getLocation());
            }
        plant= memento.get(memento.size()-1).getplant();
        meat = memento.get(memento.size()-1).getmeat();
        memento.remove(memento.size()-1);
        repaint();
    }

    public void setbackground(int nbr){this.background=nbr;}
}
