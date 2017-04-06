package jakascompany.myapplication;



public interface ICountBMI {

    boolean isValidMass(float mass);
    boolean isValidHeight(float height);
    float countBMI(float mass, float height);

}
