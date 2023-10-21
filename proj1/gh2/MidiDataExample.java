package gh2;
import java.io.*;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

public class MidiDataExample {
    public static void main(String[] args) {
        try {
            // Load your MIDI data (replace with your MIDI file)
            byte[] midiData = loadMidiDataFromFile("/Users/oak/Downloads/something.mid");

            // Compress the MIDI data using GZIP
            ByteArrayOutputStream compressedData = new ByteArrayOutputStream();
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(compressedData)) {
                gzipOutputStream.write(midiData);
            }

            // Encode the compressed data in Base64
            byte[] base64Encoded = Base64.getEncoder().encode(compressedData.toByteArray());

            // Convert the byte array to a string
            String base64String = new String(base64Encoded);

            // Print the Base64-encoded and GZIP-compressed MIDI data
            System.out.println(base64String);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Replace this method with your own MIDI loading logic
    private static byte[] loadMidiDataFromFile(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        byte[] midiData = new byte[fileInputStream.available()];
        fileInputStream.read(midiData);
        fileInputStream.close();
        return midiData;
    }
}
