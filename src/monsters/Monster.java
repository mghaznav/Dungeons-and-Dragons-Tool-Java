package monsters;

import java.util.ArrayList;
import java.io.Serializable;

public class Monster implements Serializable {

	private String monsterName;
	private String upperBound;
	private String lowerBound;
	private String description;
    
	public Monster(){

	}

	public Monster(String name, String upper, String lower, String desc){
		setName(name);
		setUpperBound(upper);
		setLowerBound(lower);
		setDescription(desc);

	}
	
    //might be easier to make some constructors here

	

    
    public void setName(String name){
		monsterName = name;
	}
	public void setUpperBound(String upper){
		upperBound = upper;
	}
	public void setLowerBound(String lower){
		lowerBound = lower;
    }
	public void setDescription(String desc){
		description = desc;
	}
    
    public String getName(){
		return monsterName;
	}
	public String getUpper(){
		return upperBound;
	}
	public String getLower(){
		return lowerBound;
	}
	public String getDescription(){
		return description;
	}
	
	@Override
	public String toString(){
		return getName() + " max:" + getUpper();  //TODO needs a better toString()

	}
}
