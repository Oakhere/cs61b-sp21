package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {
    public static final double CONCERT_A = 440.0;
    public static void main(String[] args) {

        int keys = 37;
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        GuitarString[] strings = new GuitarString[keys];
        for (int i = 0; i < keys; i++) {
            double frequency = CONCERT_A * Math.pow(2, (i - 24) / 12.0);
            strings[i] = new GuitarString(frequency);
        }

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int keyIndex = keyboard.indexOf(key);
                if (keyIndex >= 0) {
                    strings[keyIndex].pluck();
                }
            }
            double sample = 0.0;
            for (int i = 0; i < keys; i++) {
                sample += strings[i].sample();
                strings[i].tic();
            }

            StdAudio.play(sample);

        }
    }
}
