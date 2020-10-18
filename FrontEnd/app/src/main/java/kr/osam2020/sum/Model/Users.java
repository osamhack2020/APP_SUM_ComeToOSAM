package kr.osam2020.sum.Model;

public class Users {
    private String id;
    private String username;
    private String introduction;
    private String imageURL;

    private String indexIntimacy;
    private IndexExpert indexExpert;

    private double intimacyScore;
    private double expertScore;
    private double mixScore;
    private double representationScore;

    private String associationVector;
    private String associationVectorExpert;

    // Constructors
    public Users() {

    }

    public Users(String id, String username, String introduction, String imageURL) {
        this.id = id;
        this.username = username;
        this.introduction = introduction;
        this.imageURL = imageURL;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public IndexExpert getIndexExpert() {
        return indexExpert;
    }

    public void setIndexExpert(IndexExpert indexExpert) {
        this.indexExpert = indexExpert;
    }

    public String getIndexIntimacy() {
        return indexIntimacy;
    }

    public void setIndexIntimacy(String indexIntimacy) {
        this.indexIntimacy = indexIntimacy;
    }

    public double getIntimacyScore() {
        return intimacyScore;
    }

    public void setIntimacyScore() {
        if (indexIntimacy == null) {
            intimacyScore = 0;
            return;
        }

        int result = 0;

        for (int i=0; i<associationVector.length(); i++) {
            if (associationVector.charAt(i) == '1' && indexIntimacy.charAt(i) == '1')
                result++;
        }

        intimacyScore = result / 8.0;
    }

    public double getExpertScore() {
        return expertScore;
    }

    public void setExpertScore() {
        if (indexExpert == null) {
            expertScore = 0;
            return;
        }

        indexExpert.setVector();
        int[] vector = indexExpert.getVector();
        int result = 0;

        for (int i=0; i<AssociationMatrix.THE_NUMBER_OF_INDEX_EXPERT * AssociationMatrix.THE_NUMBER_OF_CATEGORY; i++) {
            int index = i / AssociationMatrix.THE_NUMBER_OF_INDEX_EXPERT;
            result = result + (vector[i] * Integer.parseInt(associationVectorExpert.substring(index, index+1)));
        }
        expertScore = result / 100.0;
    }

    public double getMixScore() {
        return mixScore;
    }

    public void setMixScore() {
        this.mixScore = (getExpertScore() + getIntimacyScore()) / 2;
    }

    public double getRepresentationScore() {
        return representationScore;
    }

    public void setRepresentationScore(double representationScore) {
        this.representationScore = representationScore;
    }

    public String getAssociationVector() {
        return associationVector;
    }

    public void setAssociationVector(String associationVector) {
        this.associationVector = associationVector;
    }

    public String getAssociationVectorExpert() {
        return associationVectorExpert;
    }

    public void setAssociationVectorExpert(String associationVectorExpert) {
        this.associationVectorExpert = associationVectorExpert;
    }

    public String getIntimacyResultVector() {
        if (indexIntimacy == null) {
            String result = "";
            for (int i=0; i<associationVector.length(); i++)
                result += "0";
            return result;
        }

        String result = "";

        for (int i=0; i<associationVector.length(); i++) {
            if (associationVector.charAt(i) == '1' && indexIntimacy.charAt(i) == '1')
                result += "1";
            else
                result += "0";
        }
        return result;
    }

    public String getExpertResultVector() {
        if (indexExpert == null) {
            String result = "";
            for (int i=0; i<AssociationMatrix.THE_NUMBER_OF_CATEGORY * AssociationMatrix.THE_NUMBER_OF_INDEX_EXPERT; i++)
                result += "0";
            return result;
        }
        indexExpert.setVector();
        int[] vector = indexExpert.getVector();
        String result = "";

        for (int i=0; i<AssociationMatrix.THE_NUMBER_OF_INDEX_EXPERT * AssociationMatrix.THE_NUMBER_OF_CATEGORY; i++) {
            int index = i / AssociationMatrix.THE_NUMBER_OF_INDEX_EXPERT;
            int tempResult = vector[i] * Integer.parseInt(associationVectorExpert.substring(index, index+1));
            if (tempResult > 20)
                result += "1";
            else
                result += "0";
        }
        return result;
    }
}
