package screen;

public class Brightness {
    private double state;
    private final double initialState;
    private final double step;

    public Brightness(double initialState){
       this(initialState, 0.1);
    }

    public Brightness(double initialState, double step){
        this.initialState = initialState;
        this.step = step;
        this.state = initialState;
    }

    public double decrease(){
        state = state <= 0.1 ? state : state - step;
        return state;
    }

    public double increase(){
        state = state >= initialState ? state : state + step;
        return state;
    }

    public double getBrightness(){
        return this.state;
    }
}
