package jakascompany.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;


public class CountBMITest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void MassUnderZeroInvalid() {
        //Given
        float mass = -1.0f;
        //When
        ICountBMI test = new CountBmi();
        assertFalse(test.isValidMass(mass));
    }
    @Test
    public void HeightAboveLimitInvalid() {
        //Given
        float height = 2.5f;
        //When
        ICountBMI test = new CountBmi();
        //Then
        assertFalse(test.isValidHeight(height));

    }
    @Test
    public void IsProperHeightValid() {
        //Given
        float height = 1.6f;
        //When
        ICountBMI test = new CountBmi();
        //Then
        assertTrue(test.isValidHeight(height));

    }
    @Test
    public void InvalidCountBMICall() {
        //Given
        float height = 2.6f;
        float weight = -1.0f;
        //When
        ICountBMI test = new CountBmi();
        //Then
        test.countBMI(weight,height);

    }
}
