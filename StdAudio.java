import javax.sound.sampled.*;

public final class StdAudio {

    public static final int SAMPLE_RATE = 44100;

    private static final int BYTES_PER_SAMPLE = 2;                // 16-bit audio
    private static final int BITS_PER_SAMPLE = 16;                // 16-bit audio
    private static final double MAX_16_BIT = Short.MAX_VALUE;     // 32,767
    private static final int SAMPLE_BUFFER_SIZE = 4096;


    private static SourceDataLine line;   // to play the sound
    private static byte[] buffer;         // our internal buffer
    private static int bufferSize = 0;

    // not-instantiable
    private StdAudio() {
    }


    // static initializer
    static {
        init();
    }

    // open up an audio stream
    private static void init() {
        try {
            // 44,100 samples per second, 16-bit audio, mono, signed PCM, little Endian
            AudioFormat format = new AudioFormat((float) SAMPLE_RATE, BITS_PER_SAMPLE, 1, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE);

            buffer = new byte[SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE / 3];
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        // no sound gets made before this call
        line.start();
    }


    /**
     * Close standard audio.
     */
    public static void close() {
        line.drain();
        line.stop();
    }

    /**
     * Write one sample (between -1.0 and +1.0) to standard audio. If the sample
     * is outside the range, it will be clipped.
     */
    public static void play(double in) {

        // clip if outside [-1, +1]
        if (in < -1.0) in = -1.0;
        if (in > +1.0) in = +1.0;

        // convert to bytes
        short s = (short) (MAX_16_BIT * in);
        buffer[bufferSize++] = (byte) s;
        buffer[bufferSize++] = (byte) (s >> 8);   // little Endian

        // send to sound card if buffer is full
        if (bufferSize >= buffer.length) {
            line.write(buffer, 0, buffer.length);
            bufferSize = 0;
        }
    }

    /**
     * Write an array of samples (between -1.0 and +1.0) to standard audio. If a sample
     * is outside the range, it will be clipped.
     */
    public static void play(double[] input) {
        for (int i = 0; i < input.length; i++) {
            play(input[i]);
        }
    }

    private static double[] tone(double hz, double duration) {
        int N = (int) (StdAudio.SAMPLE_RATE * duration);
        double[] a = new double[N + 1];
        for (int i = 0; i <= N; i++)
            a[i] = 1 * Math.sin(2 * Math.PI * i * hz / StdAudio.SAMPLE_RATE);
        return a;
    }

    /**
     * Test client - play an A major scale to standard audio.
     */
    public static void main(String[] args) {

        double[] c = tone(261.63,0.3);
        double[] cs = tone(277.18,0.3);
        double[] d = tone(293.67,0.3);
        double[] ds = tone(311.13,0.3);
        double[] e = tone(329.63,0.3);
        double[] f = tone(349.23,0.3);
        double[] fs = tone(369.99,0.3);
        double[] g = tone(392.00,0.3);
        double[] gs = tone(415.30,0.3);
        double[] a = tone(440,0.3);
        double[] as = tone(466.16,0.3);
        double[] b = tone(493.88,0.3);
        CoreControl controller = new CoreControl();
        controller.makeScore();
        int[][] score = controller.getScore();
        for (int i = 0; i < score.length; i++) {
            double[] noteToPlay = tone(0,0.3);
            int numNotesAdded = 0;
            if (score[i][0] == 1) {
                numNotesAdded++;
                for (int k = 0; k < b.length; ++k)
                    noteToPlay[k] += b[k];
            }
            if (score[i][2] == 1) {
                numNotesAdded++;
                for (int k = 0; k < cs.length; ++k)
                    noteToPlay[k] += cs[k];
            }
            if (score[i][4] == 1) {
                numNotesAdded++;
                for (int k = 0; k < ds.length; ++k)
                    noteToPlay[k] += ds[k];
            }
            if (score[i][5] == 1) {
                numNotesAdded++;
                for (int k = 0; k < e.length; ++k)
                    noteToPlay[k] += e[k];
            }
            if (score[i][7] == 1) {
                numNotesAdded++;
                for (int k = 0; k < fs.length; ++k)
                    noteToPlay[k] += fs[k];
            }
            if (score[i][9] == 1) {
                numNotesAdded++;
                for (int k = 0; k < gs.length; ++k)
                    noteToPlay[k] += gs[k];
            }
            if (score[i][11] == 1) {
                numNotesAdded++;
                for (int k = 0; k < as.length; ++k)
                    noteToPlay[k] += as[k];
            }
            for (int j = 0; j < noteToPlay.length; j++) {
                noteToPlay[j] /= numNotesAdded;
            }
            StdAudio.play(noteToPlay);
        }
    }
}
