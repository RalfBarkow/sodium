package examples;

import fridgets.*;
import javax.swing.*;

import fridgets.FrButton;
import fridgets.FrView;
import nz.sodium.*;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class button {
    public static void main(String[] args) {
        JFrame frame = new JFrame("button");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setContentPane(Transaction.run(() -> {
            FrButton b = new FrButton(new Cell<>("OK"));
            Listener l = b.sClicked.listen(
                u -> System.out.println("clicked!"));
            return new FrView(frame, b) {
                @Override
                public void removeNotify() {
                    super.removeNotify();
                    l.unlisten();
                }
            };
        }));
        frame.setSize(360,120);
        frame.setVisible(true);
    }
}

