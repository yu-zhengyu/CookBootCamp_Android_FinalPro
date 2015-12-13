package model;

import java.io.Serializable;
@SuppressWarnings("unused")
public class Recipe implements Serializable {
	private static final long serialVersionUID = 1;
	private int recipeID;
	private int authorID;
	private int likeNumber;
	private double distance;
	
	private String recipeName;
	private String material;
	private String description;	
	private String sendTime;
	private String location;
	private String imagerui;
	
	//get
	public double getDistance() {return distance;}
	public String getImageUri() {
		return imagerui;
	}
	public int getRecipeID() {
        return recipeID;
    }
	public String getRecipeName() {
        return recipeName;
    }
	public String getMaterial() {
        return material;
    }
	public String getDescription() {
        return description;
    }
	public int getAuthorID() {
        return authorID;
    }
	public String getLocation() {
		return location;
	}
	public int getLikeNumber() {
        return likeNumber;
    }
    public String getSendTime() {
        return sendTime;
    }
	public void setLikeNumber(int likeNumber) {
		this.likeNumber = likeNumber;
	}
	public void setLocation(String location) {
		this.location = location;
	}
//	public void setSendTime(String sendTime) {
//		this.sendTime = sendTime;
//	}
	
	
	//set
	public void setDistance(double distance) {this.distance = distance;}
//	public void setRecipeID(int recipeid) {
//		this.recipeID = recipeid;
//    }
	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
    }
	public void setMaterial(String material) {
		this.material = material;
    }
	public void setDescription(String description) {
		this.description = description;
    }
	public void setAuthorID(int authorID) {
		this.authorID = authorID;
    }
	
//	public void setSendTime1(String sendTime) {
//		this.sendTime = sendTime;
//    }
	
	public void setImageUri(String uri) {
		this.imagerui = uri;
	}
	
	// construction
	public Recipe(int authorID, int likeNumber, String recipeName,
			String material, String description, String sendTime,
			String location, String imagerui) {
		this.authorID = authorID;
		this.likeNumber = likeNumber;
		this.recipeName = recipeName;
		this.material = material;
		this.description = description;
		this.sendTime = sendTime;
		this.location = location;
		this.imagerui = imagerui;
	}
	public Recipe() {
		
	}
    public Recipe (int recipeID) {
        this.recipeID = recipeID;
    }

	
}