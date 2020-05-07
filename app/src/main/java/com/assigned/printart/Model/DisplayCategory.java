package com.assigned.printart.Model;

public class DisplayCategory
{
    private String CategoryID;
    private String CategoryName, Art, ArtPic;

    public DisplayCategory()
    {

    }

    public DisplayCategory(String categoryID, String categoryName, String art, String artPic) {
        CategoryID = categoryID;
        CategoryName = categoryName;
        Art = art;
        ArtPic = artPic;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getArt() {
        return Art;
    }

    public void setArt(String art) {
        Art = art;
    }

    public String getArtPic() {
        return ArtPic;
    }

    public void setArtPic(String artPic) {
        ArtPic = artPic;
    }
}
