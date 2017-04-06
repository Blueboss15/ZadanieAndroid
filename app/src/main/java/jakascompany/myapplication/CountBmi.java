package jakascompany.myapplication;



public class CountBmi implements ICountBMI {

    static final float MIN_WEIGHT = 10.0f;
    static final float MAX_WEIGHT = 250.0f;
    static final float MIN_HEIGHT = 0.5f;
    static final float MAX_HEIGHT = 2.5f;


    @Override
    public boolean isValidMass(float mass) {
        return mass> MIN_WEIGHT && mass< MAX_WEIGHT;
    }

    @Override
    public boolean isValidHeight(float height) {
        return height>MIN_HEIGHT && height< MAX_HEIGHT;
    }

    @Override
    public float countBMI(float mass, float height) {
        if(!isValidMass(mass) || !isValidHeight(height) ) throw new IllegalArgumentException("Zle dane");
        return mass/(height*height);
    }
}
