import java.awt.Color;
import sodium.*;
import sodium.time.*;

public class bounce extends Shapes {
    public static void main(String[] args) {
        Animate.animate("bounce", (sys, extents) -> {
            Cell<Double> time = sys.time;
            double t0 = time.sample();
            double ballRadius = 15;
            double leftWall = -extents.x + ballRadius;
            double rightWall = extents.x - ballRadius;
            double floor = -extents.y + ballRadius;
            double roof = extents.y - ballRadius;
            Signal gravity = new Signal(t0, 0, 0, -1200);
            StreamLoop<Signal> sBounceX = new StreamLoop<>();
            StreamLoop<Signal> sBounceY = new StreamLoop<>();
            Cell<Signal> velx = sBounceX.hold(new Signal(t0, 0, 0, 350));
            Cell<Signal> vely = sBounceY.hold(gravity.integrate(0));
            Cell<Signal> posx = Signal.integrate(velx, leftWall);
            Cell<Signal> posy = Signal.integrate(vely, roof);
            sBounceX.loop(bounceAt(sys, velx, posx, leftWall)
                          .merge(bounceAt(sys, velx, posx, rightWall)));
            sBounceY.loop(bounceAt(sys, vely, posy, floor));
            return translate(
                scale(circle(Color.red), new Cell<Double>(ballRadius)),
                Cell.lift((t, x, y) -> new Point(x.at(t), y.at(t)),
                    time, posx, posy)
            );
        });
    }
    static double restitution = 0.95;
    public static Stream<Signal> bounceAt(TimerSystem<Double> sys,
                    Cell<Signal> vel, Cell<Signal> pos, double target) {
        return sys.at(pos.map(p -> p.when(target)))
                  .snapshot(vel, (t, v) ->
                      new Signal(t, v.a, v.b, -v.at(t)*restitution));
    }
}
